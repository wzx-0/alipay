package com.seehoo.rent.app.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehoo.rent.app.common.BeanCopyUtil;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.config.AlipayProperties;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderCreateReq;
import com.seehoo.rent.app.dto.OrderCreateVo;
import com.seehoo.rent.app.dto.OrderDetailVo;
import com.seehoo.rent.app.dto.OrderInfoVo;
import com.seehoo.rent.app.dto.OrderPageReq;
import com.seehoo.rent.app.entity.RentGoods;
import com.seehoo.rent.app.entity.RentGoodsSku;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RentOrderSign;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import com.seehoo.rent.app.entity.RentOrderItem;
import com.seehoo.rent.app.mapper.RentGoodsMapper;
import com.seehoo.rent.app.mapper.RentGoodsSkuMapper;
import com.seehoo.rent.app.mapper.RentOrderInstallmentMapper;
import com.seehoo.rent.app.mapper.RentOrderItemMapper;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RentOrderSignMapper;
import com.seehoo.rent.app.service.OrderService;
import com.seehoo.rent.sdk.AlipayRentClient;
import com.seehoo.rent.sdk.AlipayRentException;
import com.seehoo.rent.sdk.BizFields;
import com.seehoo.rent.sdk.RentConstants;
import com.seehoo.rent.sdk.RentResponse;
import com.seehoo.rent.sdk.model.OrderCloseRequest;
import com.seehoo.rent.sdk.model.OrderCreateRequest;
import com.seehoo.rent.sdk.model.FulfillmentFinishRequest;
import com.seehoo.rent.sdk.model.FulfillmentReceiveRequest;
import com.seehoo.rent.sdk.model.FulfillmentSendRequest;
import com.seehoo.rent.sdk.model.OrderModifyRequest;
import com.seehoo.rent.sdk.model.OrderSignRequest;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import com.seehoo.rent.sdk.model.RentSignInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 租赁订单服务实现：本地落库与支付宝调用分离，创单失败本地关单 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** 订单来源：公域 */
    private static final String SOURCE_CHANNEL_PUBLIC = "1";
    /** 期次账单状态：待支付 */
    private static final String BILL_STATUS_WAIT_PAY = "0";

    @Resource
    private RentOrderMapper orderMapper;
    @Resource
    private RentOrderItemMapper itemMapper;
    @Resource
    private RentOrderInstallmentMapper installmentMapper;
    @Resource
    private RentGoodsMapper goodsMapper;
    @Resource
    private RentGoodsSkuMapper skuMapper;
    @Resource
    private RentOrderSignMapper signMapper;
    @Resource
    private AlipayRentClient alipayRentClient;
    @Resource
    private AlipayProperties alipayProperties;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public OrderCreateVo create(OrderCreateReq req) {
        checkCreateParam(req);
        RentGoods goods = null;
        RentGoodsSku sku = null;
        if (OrderCreateRequest.OrderType.RENT.name().equals(req.getOrderType())) {
            goods = goodsMapper.selectById(Long.valueOf(req.getGoodsId()));
            sku = skuMapper.selectById(Long.valueOf(req.getSkuId()));
            if (goods == null || sku == null) {
                throw new BusinessException(RentErrorCode.GOODS_NOT_FOUND);
            }
        }

        String outOrderId = IdUtil.getSnowflakeNextIdStr();
        RentOrder order = buildOrderEntity(req, outOrderId, goods, sku);
        // 商品明细快照只构建一次：本地落库与支付宝创单请求共用，避免两处数据不一致
        RentOrderItem item = buildOrderItem(order, req, goods, sku);
        saveOrderData(order, req, item);

        // 调支付宝创单（事务外），失败则本地关单留痕并向上抛出
        try {
            RentResponse<Map<String, Object>> resp = alipayRentClient.orderCreate(
                    buildCreateRequest(req, order, item));
            order.setAlipayOrderId(resp.getStr(BizFields.ORDER_ID));
            order.setDetailPath(resp.getStr(BizFields.PATH));
            orderMapper.updateById(order);
        } catch (AlipayRentException e) {
            log.error("支付宝创单失败，本地关单：outOrderId={}", outOrderId, e);
            order.setOrderStatus(RentOrderStatus.CLOSED.name());
            order.setCloseReasonDesc("支付宝创单失败：" + e.getMessage());
            order.setCloseTime(LocalDateTime.now());
            orderMapper.updateById(order);
            throw new BusinessException(RentErrorCode.ALIPAY_CALL_FAILED, e.getMessage(), e);
        }

        OrderCreateVo vo = new OrderCreateVo();
        vo.setAlipayOrderId(order.getAlipayOrderId());
        vo.setOutOrderId(outOrderId);
        vo.setPath(order.getDetailPath());
        return vo;
    }

    /** 创建入参业务校验：格式校验已由@Valid完成，此处校验订单类型与必填组合 */
    private void checkCreateParam(OrderCreateReq req) {
        OrderCreateRequest.OrderType orderType = parseOrderType(req.getOrderType());
        if (OrderCreateRequest.OrderType.RENT == orderType) {
            if (StrUtil.isBlank(req.getGoodsId()) || StrUtil.isBlank(req.getSkuId())
                    || req.getQuantity() == null || StrUtil.isBlank(req.getDepositPrice())
                    || req.getRentPlanInfo() == null) {
                throw new BusinessException(RentErrorCode.PARAM_ERROR, "RENT订单须传goodsId/skuId/quantity/depositPrice/rentPlanInfo");
            }
            return;
        }
        if (StrUtil.isBlank(req.getOriginOrderId())) {
            throw new BusinessException(RentErrorCode.PARAM_ERROR, "RELET/BUYOUT订单须传originOrderId");
        }
    }

    /** 订单类型字符串转枚举，非法值在入口拒绝 */
    private OrderCreateRequest.OrderType parseOrderType(String orderType) {
        try {
            return OrderCreateRequest.OrderType.valueOf(orderType);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(RentErrorCode.PARAM_ERROR, "订单类型非法：" + orderType);
        }
    }

    /** 组装订单主表实体 */
    private RentOrder buildOrderEntity(OrderCreateReq req, String outOrderId, RentGoods goods, RentGoodsSku sku) {
        RentOrder order = new RentOrder();
        order.setOutOrderId(outOrderId);
        order.setOrderType(req.getOrderType());
        order.setOriginOrderId(req.getOriginOrderId());
        order.setBizIdentity(RentConstants.BIZ_IDENTITY);
        order.setSourceChannel(SOURCE_CHANNEL_PUBLIC);
        order.setBuyerOpenId(req.getBuyerOpenId());
        order.setSourceId(req.getSourceId());
        order.setDepositPrice(StrUtil.isBlank(req.getDepositPrice()) ? BigDecimal.ZERO : new BigDecimal(req.getDepositPrice()));
        order.setFreight(StrUtil.isBlank(req.getFreight()) ? null : new BigDecimal(req.getFreight()));
        order.setAdditionalPrice(StrUtil.isBlank(req.getAdditionalPrice()) ? null : new BigDecimal(req.getAdditionalPrice()));
        order.setDeliveryType(OrderCreateRequest.DeliveryType.SELFPICK.name());
        order.setOrderStatus(RentOrderStatus.CREATED.name());
        if (OrderCreateRequest.OrderType.RENT.name().equals(req.getOrderType())) {
            order.setTitle(goods.getGoodsName());
            order.setRentStartTime(LocalDateTime.parse(req.getRentPlanInfo().getRentStartTime(), DATE_TIME_FMT));
            order.setRentEndTime(LocalDateTime.parse(req.getRentPlanInfo().getRentEndTime(), DATE_TIME_FMT));
        }
        if (req.getShopInfo() != null) {
            order.setShopName(req.getShopInfo().getShopName());
            order.setShopAddress(req.getShopInfo().getShopAddress());
            order.setShopTel(req.getShopInfo().getShopTel());
        }
        if (req.getReceiverInfo() != null) {
            order.setReceiverName(req.getReceiverInfo().getReceiverName());
            order.setReceiverTel(req.getReceiverInfo().getReceiverTel());
            order.setReceiverAddress(req.getReceiverInfo().getDetailedAddress());
        }
        order.setOrderPrice(calcOrderPrice(order, req));
        order.setMerchantExtInfo(req.getMerchantExtInfo() == null ? null : String.valueOf(req.getMerchantExtInfo()));
        return order;
    }

    /** 订单总价 = 运费 + 增值服务费 + Σ每期订阅金额 */
    private BigDecimal calcOrderPrice(RentOrder order, OrderCreateReq req) {
        BigDecimal total = BigDecimal.ZERO;
        if (order.getFreight() != null) {
            total = total.add(order.getFreight());
        }
        if (order.getAdditionalPrice() != null) {
            total = total.add(order.getAdditionalPrice());
        }
        if (req.getRentPlanInfo() != null) {
            for (OrderCreateReq.Installment item : req.getRentPlanInfo().getInstallments()) {
                total = total.add(new BigDecimal(item.getInstallmentPrice()));
            }
        }
        return total;
    }

    /** 订单主体+明细+期次事务落库（不含外部调用）；明细的orderId在订单插入拿到主键后回填 */
    private void saveOrderData(RentOrder order, OrderCreateReq req, RentOrderItem item) {
        transactionTemplate.executeWithoutResult(status -> {
            orderMapper.insert(order);
            item.setOrderId(order.getId());
            itemMapper.insert(item);
            if (req.getRentPlanInfo() != null) {
                installmentMapper.insertBatch(buildInstallments(order, req));
            }
        });
    }

    /** 创单商品明细快照：RENT取当前商品，RELET/BUYOUT沿用原订单商品（TODO与支付宝确认续买断item_infos规则） */
    private RentOrderItem buildOrderItem(RentOrder order, OrderCreateReq req, RentGoods goods, RentGoodsSku sku) {
        RentOrderItem item = new RentOrderItem();
        item.setOrderId(order.getId());
        item.setItemType(RentConstants.ITEM_TYPE_CAR);
        if (OrderCreateRequest.OrderType.RENT.name().equals(req.getOrderType())) {
            item.setOutItemId(StrUtil.blankToDefault(goods.getOutItemId(), goods.getGoodsCode()));
            item.setOutSkuId(sku.getOutSkuId());
            item.setItemName(goods.getGoodsName());
            item.setSalePrice(sku.getSalePrice());
            item.setItemValue(goods.getItemValue());
            item.setItemCnt(req.getQuantity());
            return item;
        }
        RentOrder originOrder = orderMapper.selectByOutOrderId(req.getOriginOrderId());
        List<RentOrderItem> originItems = originOrder == null ? null : itemMapper.selectByOrderId(originOrder.getId());
        if (originItems == null || originItems.isEmpty()) {
            throw new BusinessException(RentErrorCode.ORDER_NOT_FOUND, "原订单不存在或缺少商品明细");
        }
        RentOrderItem origin = originItems.get(0);
        item.setOutItemId(origin.getOutItemId());
        item.setOutSkuId(origin.getOutSkuId());
        item.setItemName(origin.getItemName());
        item.setSalePrice(origin.getSalePrice());
        item.setItemValue(origin.getItemValue());
        item.setItemCnt(origin.getItemCnt());
        return item;
    }

    /** 期次实体列表组装 */
    private List<RentOrderInstallment> buildInstallments(RentOrder order, OrderCreateReq req) {
        List<RentOrderInstallment> installments = new ArrayList<>();
        for (OrderCreateReq.Installment plan : req.getRentPlanInfo().getInstallments()) {
            RentOrderInstallment inst = new RentOrderInstallment();
            inst.setOrderId(order.getId());
            inst.setInstallmentNo(plan.getInstallmentNo());
            inst.setInstallmentPrice(new BigDecimal(plan.getInstallmentPrice()));
            inst.setPlanPayTime(LocalDateTime.parse(plan.getPlanPayTime(), DATE_TIME_FMT));
            inst.setBuyoutPrice(StrUtil.isBlank(plan.getBuyoutPrice()) ? null : new BigDecimal(plan.getBuyoutPrice()));
            inst.setBillStatus(BILL_STATUS_WAIT_PAY);
            installments.add(inst);
        }
        return installments;
    }

    /** 组装支付宝order.create请求（bizIdentity由SDK自动填充） */
    private OrderCreateRequest buildCreateRequest(OrderCreateReq req, RentOrder order, RentOrderItem item) {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setOrderType(parseOrderType(req.getOrderType()));
        request.setOutOrderId(order.getOutOrderId());
        request.setOpenId(req.getBuyerOpenId());
        request.setSourceId(req.getSourceId());
        if (StrUtil.isNotBlank(alipayProperties.getTradeAppId())) {
            request.setTradeAppId(alipayProperties.getTradeAppId());
        }
        request.setItemInfos(buildItemInfos(item));
        request.setRentPlanInfo(buildRentPlanInfo(req));
        request.setPriceInfo(buildPriceInfo(order));
        request.setRentSignInfo(buildRentSignInfo());
        request.setDeliveryInfo(buildDeliveryInfo(req));
        // 续订/买断溯源参数：relet_info/buyout_info.origin_order_id
        if (OrderCreateRequest.OrderType.RELET.name().equals(req.getOrderType())) {
            OrderCreateRequest.ReletInfo relet = new OrderCreateRequest.ReletInfo();
            relet.setOriginOrderId(req.getOriginOrderId());
            request.setReletInfo(relet);
        } else if (OrderCreateRequest.OrderType.BUYOUT.name().equals(req.getOrderType())) {
            OrderCreateRequest.BuyoutInfo buyout = new OrderCreateRequest.BuyoutInfo();
            buyout.setOriginOrderId(req.getOriginOrderId());
            request.setBuyoutInfo(buyout);
        }
        return request;
    }

    /** 支付宝item_infos取自本地明细快照，保证与落库数据一致 */
    private List<OrderCreateRequest.ItemInfo> buildItemInfos(RentOrderItem item) {
        OrderCreateRequest.ItemInfo info = new OrderCreateRequest.ItemInfo();
        info.setItemType(item.getItemType());
        info.setOutItemId(item.getOutItemId());
        info.setOutSkuId(item.getOutSkuId());
        info.setItemName(item.getItemName());
        info.setSalePrice(item.getSalePrice() == null ? null : item.getSalePrice().toPlainString());
        info.setItemValue(item.getItemValue() == null ? null : item.getItemValue().toPlainString());
        info.setItemCnt(item.getItemCnt());
        List<OrderCreateRequest.ItemInfo> itemInfos = new ArrayList<>();
        itemInfos.add(info);
        return itemInfos;
    }

    private OrderCreateRequest.RentPlanInfo buildRentPlanInfo(OrderCreateReq req) {
        if (req.getRentPlanInfo() == null) {
            return null;
        }
        OrderCreateRequest.RentPlanInfo plan = new OrderCreateRequest.RentPlanInfo();
        plan.setRentStartTime(req.getRentPlanInfo().getRentStartTime());
        plan.setRentEndTime(req.getRentPlanInfo().getRentEndTime());
        List<OrderCreateRequest.Installment> list = new ArrayList<>();
        for (OrderCreateReq.Installment i : req.getRentPlanInfo().getInstallments()) {
            OrderCreateRequest.Installment row = new OrderCreateRequest.Installment();
            row.setInstallmentNo(i.getInstallmentNo());
            row.setInstallmentPrice(i.getInstallmentPrice());
            row.setPlanPayTime(i.getPlanPayTime());
            row.setBuyoutPrice(i.getBuyoutPrice());
            list.add(row);
        }
        plan.setInstallments(list);
        return plan;
    }

    private OrderCreateRequest.PriceInfo buildPriceInfo(RentOrder order) {
        OrderCreateRequest.PriceInfo price = new OrderCreateRequest.PriceInfo();
        price.setOrderPrice(order.getOrderPrice().toPlainString());
        price.setDepositPrice(order.getDepositPrice().toPlainString());
        price.setFreight(order.getFreight() == null ? null : order.getFreight().toPlainString());
        price.setAdditionalPrice(order.getAdditionalPrice() == null ? null : order.getAdditionalPrice().toPlainString());
        return price;
    }

    /** 签约要素：芝麻免押credit_info + 冻结通知地址 + 代扣签约场景（zmServiceId由BD提供后配置） */
    private RentSignInfo buildRentSignInfo() {
        RentSignInfo signInfo = new RentSignInfo();
        RentSignInfo.CreditInfo credit = new RentSignInfo.CreditInfo();
        credit.setZmServiceId(alipayProperties.getZmServiceId());
        credit.setCategoryId(alipayProperties.getCategoryId());
        signInfo.setCreditInfo(credit);
        signInfo.setFreezeNotifyUrl(alipayProperties.getFreezeNotifyUrl());
        signInfo.setRentDeductInfo(buildRentDeductInfo());
        return signInfo;
    }

    /** 代扣签约要素（order.sign补偿唤起时复用） */
    private RentSignInfo.RentDeductInfo buildRentDeductInfo() {
        RentSignInfo.RentDeductInfo deduct = new RentSignInfo.RentDeductInfo();
        deduct.setSignScene(RentSignInfo.SignScene.RENT_DEDUCT);
        deduct.setAuthCategory(StrUtil.blankToDefault(alipayProperties.getAuthCategory(), null));
        return deduct;
    }

    private OrderCreateRequest.DeliveryInfo buildDeliveryInfo(OrderCreateReq req) {
        OrderCreateRequest.DeliveryInfo delivery = new OrderCreateRequest.DeliveryInfo();
        delivery.setDeliveryType(OrderCreateRequest.DeliveryType.SELFPICK);
        if (req.getShopInfo() != null) {
            OrderCreateRequest.ShopInfo shop = new OrderCreateRequest.ShopInfo();
            shop.setShopName(req.getShopInfo().getShopName());
            shop.setShopAddress(req.getShopInfo().getShopAddress());
            shop.setShopTel(req.getShopInfo().getShopTel());
            delivery.setShopInfo(shop);
        }
        if (req.getReceiverInfo() != null) {
            OrderCreateRequest.ReceiverInfo receiver = new OrderCreateRequest.ReceiverInfo();
            receiver.setReceiverName(req.getReceiverInfo().getReceiverName());
            receiver.setReceiverTel(req.getReceiverInfo().getReceiverTel());
            receiver.setDetailedAddress(req.getReceiverInfo().getDetailedAddress());
            delivery.setReceiverInfo(receiver);
        }
        return delivery;
    }

    @Override
    public PageResult<OrderInfoVo> page(OrderPageReq req) {
        Page<RentOrder> page = orderMapper.selectOrderPage(
                new Page<>(req.getPageNo(), req.getPageSize()), req.getParams());
        List<OrderInfoVo> records = BeanCopyUtil.mapperCols(page.getRecords(), OrderInfoVo.class);
        return PageResult.of(page, records);
    }

    @Override
    public OrderDetailVo detail(String outOrderId) {
        RentOrder order = mustGetOrder(outOrderId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(BeanCopyUtil.mapper(order, OrderInfoVo.class));
        vo.setItems(BeanCopyUtil.mapperCols(itemMapper.selectByOrderId(order.getId()), OrderDetailVo.ItemVo.class));
        vo.setInstallments(BeanCopyUtil.mapperCols(installmentMapper.selectByOrderId(order.getId()), OrderDetailVo.InstallmentVo.class));
        RentOrderSign sign = signMapper.selectByOrderId(order.getId());
        if (sign != null) {
            vo.setSign(BeanCopyUtil.mapper(sign, OrderDetailVo.SignVo.class));
        }
        return vo;
    }

    @Override
    public void fulfillmentSend(OrderActionReq.FulfillmentSend req) {
        RentOrder order = mustGetOrder(req.getOutOrderId());
        boolean merchantSend = FulfillmentSendRequest.SendType.MERCHANT_DELIVERY_SEND.name().equals(req.getOperationType());
        checkStatus(order, merchantSend ? RentOrderStatus.APPROVED : RentOrderStatus.RECEIVED);
        FulfillmentSendRequest request = new FulfillmentSendRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(req.getOutOrderId());
        request.setOperationType(FulfillmentSendRequest.SendType.valueOf(req.getOperationType()));
        alipayRentClient.fulfillmentSend(request);
        if (merchantSend) {
            order.setOrderStatus(RentOrderStatus.DELIVERED.name());
            order.setDeliveryTime(LocalDateTime.now());
        } else {
            order.setOrderStatus(RentOrderStatus.RETURN_DELIVERED.name());
            order.setReturnSendTime(LocalDateTime.now());
        }
        orderMapper.updateById(order);
    }

    @Override
    public void receiveConfirm(OrderActionReq.ReceiveConfirm req) {
        RentOrder order = mustGetOrder(req.getOutOrderId());
        boolean merchantDelivery = FulfillmentReceiveRequest.ReceiveType.MERCHANT_DELIVERY_RECEIVED.name().equals(req.getReceiveType());
        checkStatus(order, merchantDelivery ? RentOrderStatus.DELIVERED : RentOrderStatus.RETURN_DELIVERED);
        FulfillmentReceiveRequest request = new FulfillmentReceiveRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(req.getOutOrderId());
        request.setReceiveType(FulfillmentReceiveRequest.ReceiveType.valueOf(req.getReceiveType()));
        alipayRentClient.fulfillmentReceive(request);
        if (merchantDelivery) {
            order.setOrderStatus(RentOrderStatus.RECEIVED.name());
            order.setReceiveTime(LocalDateTime.now());
        } else {
            order.setOrderStatus(RentOrderStatus.RETURN_RECEIVED.name());
            order.setReturnReceiveTime(LocalDateTime.now());
        }
        orderMapper.updateById(order);
    }

    @Override
    public void finish(OrderActionReq.Finish req) {
        RentOrder order = mustGetOrder(req.getOutOrderId());
        checkStatus(order, RentOrderStatus.RETURN_RECEIVED);
        FulfillmentFinishRequest request = new FulfillmentFinishRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(req.getOutOrderId());
        request.setFinishStatus(FulfillmentFinishRequest.FinishStatus.valueOf(req.getFinishStatus()));
        alipayRentClient.fulfillmentFinish(request);
        order.setOrderStatus(RentOrderStatus.FINISHED.name());
        order.setFinishStatus(req.getFinishStatus());
        order.setFinishTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void close(OrderActionReq.Close req) {
        RentOrder order = mustGetOrder(req.getOutOrderId());
        if (RentOrderStatus.FINISHED.name().equals(order.getOrderStatus())
                || RentOrderStatus.CLOSED.name().equals(order.getOrderStatus())) {
            throw new BusinessException(RentErrorCode.ORDER_STATUS_NOT_ALLOW, "订单已完结或已关闭");
        }
        // 关单前置：已支付费项须全额退款（退款功能属后续阶段） TODO 退款上线后校验"已支付且未全额退款"
        OrderCloseRequest request = new OrderCloseRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(req.getOutOrderId());
        request.setReasonCode(req.getReasonCode());
        request.setReasonDesc(req.getReasonDesc());
        alipayRentClient.orderClose(request);
        order.setOrderStatus(RentOrderStatus.CLOSED.name());
        order.setCloseReasonCode(req.getReasonCode());
        order.setCloseReasonDesc(req.getReasonDesc());
        order.setCloseTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void modify(OrderActionReq.Modify req) {
        RentOrder order = mustGetOrder(req.getOutOrderId());
        OrderModifyRequest request = new OrderModifyRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(req.getOutOrderId());
        request.setType(OrderModifyRequest.ModifyType.RENT_PLAN_TIME);
        request.setRentStartTime(req.getRentStartTime());
        request.setRentEndTime(req.getRentEndTime());
        request.setRentPlanInfo(buildModifyPlan(order.getId(), req));
        alipayRentClient.orderModify(request);
        // 支付宝改期成功后同步本地租期与期次计划
        order.setRentStartTime(LocalDateTime.parse(req.getRentStartTime(), DATE_TIME_FMT));
        order.setRentEndTime(LocalDateTime.parse(req.getRentEndTime(), DATE_TIME_FMT));
        orderMapper.updateById(order);
        syncLocalInstallments(order.getId(), req.getInstallments());
    }

    /** 校验并组装调整期次（期次不存在时拒绝） */
    private OrderModifyRequest.RentPlanInfo buildModifyPlan(Long orderId, OrderActionReq.Modify req) {
        if (req.getInstallments() == null || req.getInstallments().isEmpty()) {
            return null;
        }
        List<OrderModifyRequest.Installment> list = new ArrayList<>();
        for (OrderActionReq.ModifyInstallment mi : req.getInstallments()) {
            if (installmentMapper.selectByOrderIdAndNo(orderId, mi.getInstallmentNo()) == null) {
                throw new BusinessException(RentErrorCode.PARAM_ERROR, "期次不存在：" + mi.getInstallmentNo());
            }
            OrderModifyRequest.Installment row = new OrderModifyRequest.Installment();
            row.setInstallmentNo(mi.getInstallmentNo());
            row.setPlanPayTime(mi.getPlanPayTime());
            list.add(row);
        }
        OrderModifyRequest.RentPlanInfo plan = new OrderModifyRequest.RentPlanInfo();
        plan.setInstallments(list);
        return plan;
    }

    private void syncLocalInstallments(Long orderId, List<OrderActionReq.ModifyInstallment> modifyList) {
        if (modifyList == null || modifyList.isEmpty()) {
            return;
        }
        List<RentOrderInstallment> updates = new ArrayList<>();
        for (OrderActionReq.ModifyInstallment mi : modifyList) {
            RentOrderInstallment inst = installmentMapper.selectByOrderIdAndNo(orderId, mi.getInstallmentNo());
            inst.setPlanPayTime(LocalDateTime.parse(mi.getPlanPayTime(), DATE_TIME_FMT));
            updates.add(inst);
        }
        installmentMapper.updateBatch(updates);
    }

    /** 按商家侧订单号取订单，不存在抛业务异常 */
    private RentOrder mustGetOrder(String outOrderId) {
        RentOrder order = orderMapper.selectByOutOrderId(outOrderId);
        if (order == null) {
            throw new BusinessException(RentErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    /** 订单状态前置校验 */
    private void checkStatus(RentOrder order, RentOrderStatus expectStatus) {
        if (!expectStatus.name().equals(order.getOrderStatus())) {
            throw new BusinessException(RentErrorCode.ORDER_STATUS_NOT_ALLOW,
                    "订单状态为" + order.getOrderStatus() + "，需为" + expectStatus.name());
        }
    }
}
