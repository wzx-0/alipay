package com.seehoo.rent.app.service;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderCreateReq;
import com.seehoo.rent.app.dto.OrderCreateVo;
import com.seehoo.rent.app.dto.OrderDetailVo;
import com.seehoo.rent.app.dto.OrderInfoVo;
import com.seehoo.rent.app.dto.OrderPageReq;
import com.seehoo.rent.app.entity.RentGoods;
import com.seehoo.rent.app.entity.RentGoodsSku;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import com.seehoo.rent.app.entity.RentOrderItem;
import com.seehoo.rent.app.entity.RentOrderSign;
import com.seehoo.rent.app.mapper.RentGoodsMapper;
import com.seehoo.rent.app.mapper.RentGoodsSkuMapper;
import com.seehoo.rent.app.mapper.RentOrderInstallmentMapper;
import com.seehoo.rent.app.mapper.RentOrderItemMapper;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RentOrderSignMapper;
import com.seehoo.rent.app.support.IntegrationTestBase;
import com.seehoo.rent.app.support.TestAlipayEnv;
import com.seehoo.rent.sdk.AlipayRentException;
import com.seehoo.rent.sdk.RentConstants;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 租赁订单服务集成测试：真实Spring上下文+真实MySQL落库+真实HTTP调用本地支付宝网关。
 * 网关收到的请求内容（biz_content）与数据库落库结果双向断言。
 */
class OrderServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private OrderService orderService;
    @Autowired
    private RentOrderMapper orderMapper;
    @Autowired
    private RentOrderItemMapper itemMapper;
    @Autowired
    private RentOrderInstallmentMapper installmentMapper;
    @Autowired
    private RentGoodsMapper goodsMapper;
    @Autowired
    private RentGoodsSkuMapper skuMapper;
    @Autowired
    private RentOrderSignMapper signMapper;

    // ==================== 测试数据准备（真实落库，事务回滚清理） ====================

    private RentGoods insertGoods() {
        RentGoods goods = new RentGoods();
        goods.setGoodsCode("GC" + IdUtil.getSnowflakeNextIdStr());
        goods.setGoodsName("集成测试车辆");
        goods.setOutItemId("OI" + IdUtil.getSnowflakeNextIdStr());
        goods.setItemType("CAR_ITEM");
        goods.setItemValue(new BigDecimal("150000"));
        goods.setSalePrice(new BigDecimal("1000"));
        goods.setReportStatus("2");
        goods.setGoodsStatus("1");
        goodsMapper.insert(goods);
        return goods;
    }

    private RentGoodsSku insertSku(RentGoods goods) {
        RentGoodsSku sku = new RentGoodsSku();
        sku.setGoodsId(goods.getId());
        sku.setOutSkuId("OS" + IdUtil.getSnowflakeNextIdStr());
        sku.setSkuName("月租套餐");
        sku.setDurationDays(30);
        sku.setSalePrice(new BigDecimal("1000"));
        sku.setSkuStatus("1");
        skuMapper.insert(sku);
        return sku;
    }

    private OrderCreateReq rentReq(RentGoods goods, RentGoodsSku sku) {
        OrderCreateReq req = new OrderCreateReq();
        req.setOrderType("RENT");
        req.setSourceId("SRC" + IdUtil.getSnowflakeNextIdStr());
        req.setBuyerOpenId("OPENID_IT");
        req.setGoodsId(String.valueOf(goods.getId()));
        req.setSkuId(String.valueOf(sku.getId()));
        req.setQuantity(1);
        req.setDepositPrice("5000");
        req.setFreight("50");
        OrderCreateReq.RentPlanInfo plan = new OrderCreateReq.RentPlanInfo();
        plan.setRentStartTime("2026-10-01 00:00:00");
        plan.setRentEndTime("2027-10-01 00:00:00");
        OrderCreateReq.Installment inst = new OrderCreateReq.Installment();
        inst.setInstallmentNo(1);
        inst.setInstallmentPrice("1000");
        inst.setPlanPayTime("2026-11-01 00:00:00");
        plan.setInstallments(Collections.singletonList(inst));
        req.setRentPlanInfo(plan);
        return req;
    }

    /** 插入指定状态的订单（直接落库，供履约/支付类测试用） */
    private RentOrder insertOrder(RentOrderStatus status, String outOrderId) {
        RentOrder order = new RentOrder();
        order.setOutOrderId(outOrderId);
        order.setOrderType("RENT");
        order.setOriginOrderId(null);
        order.setOrderStatus(status.name());
        order.setBuyerOpenId("OPENID_IT");
        order.setDepositPrice(new BigDecimal("5000"));
        order.setOrderPrice(new BigDecimal("1000"));
        orderMapper.insert(order);
        return order;
    }

    // ==================== 创单 ====================

    @Test
    void createRentShouldPersistAllTablesAndSyncAlipay() {
        RentGoods goods = insertGoods();
        RentGoodsSku sku = insertSku(goods);
        OrderCreateReq req = rentReq(goods, sku);

        OrderCreateVo vo = orderService.create(req);

        // 返回VO
        assertNotNull(vo.getAlipayOrderId());
        assertEquals(vo.getOutOrderId(), vo.getOutOrderId());
        assertEquals("/pages/detail", vo.getPath());

        // 订单主表已落库并回填支付宝单号
        RentOrder saved = orderMapper.selectByOutOrderId(vo.getOutOrderId());
        assertEquals(RentOrderStatus.CREATED.name(), saved.getOrderStatus());
        assertEquals(vo.getAlipayOrderId(), saved.getAlipayOrderId());
        assertEquals(0, new BigDecimal("1050").compareTo(saved.getOrderPrice())); // 运费50+1期1000
        assertEquals(0, new BigDecimal("5000").compareTo(saved.getDepositPrice()));
        assertEquals("集成测试车辆", saved.getTitle());

        // 明细快照落库
        List<RentOrderItem> items = itemMapper.selectByOrderId(saved.getId());
        assertEquals(1, items.size());
        assertEquals(goods.getOutItemId(), items.get(0).getOutItemId());
        assertEquals(sku.getOutSkuId(), items.get(0).getOutSkuId());

        // 期次落库
        List<RentOrderInstallment> installments = installmentMapper.selectByOrderId(saved.getId());
        assertEquals(1, installments.size());
        assertEquals(Integer.valueOf(1), installments.get(0).getInstallmentNo());
        assertEquals("0", installments.get(0).getBillStatus());

        // 网关收到创单请求：明细/押金/签约要素与本地一致
        String bizContent = TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_CREATE);
        assertNotNull(bizContent);
        assertTrue(bizContent.contains("\"out_item_id\":\"" + goods.getOutItemId() + "\""));
        assertTrue(bizContent.contains("\"out_sku_id\":\"" + sku.getOutSkuId() + "\""));
        assertTrue(bizContent.contains("\"deposit_price\":\"5000\""));
        assertTrue(bizContent.contains("\"zm_service_id\":\"ZM_TEST_001\""));
        assertTrue(bizContent.contains("RENT_DEDUCT"));
    }

    @Test
    void createRentShouldRejectWhenRequiredParamMissing() {
        RentGoods goods = insertGoods();
        RentGoodsSku sku = insertSku(goods);
        OrderCreateReq req = rentReq(goods, sku);
        req.setDepositPrice(null);

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.create(req));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
    }

    @Test
    void createRentShouldRejectWhenGoodsNotFound() {
        OrderCreateReq req = rentReq(insertGoods(), insertSku(insertGoods()));
        req.setSkuId("999999");

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.create(req));
        assertEquals(RentErrorCode.GOODS_NOT_FOUND.getCode(), e.getCode());
    }

    @Test
    void createShouldRejectInvalidOrderType() {
        OrderCreateReq req = rentReq(insertGoods(), insertSku(insertGoods()));
        req.setOrderType("XXX");

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.create(req));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
    }

    @Test
    void createShouldCloseLocalOrderWhenAlipayRejects() {
        RentGoods goods = insertGoods();
        RentGoodsSku sku = insertSku(goods);
        OrderCreateReq req = rentReq(goods, sku);
        TestAlipayEnv.GATEWAY.failNextWith("ISV_PARAM_ERROR", "参数有误");

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.create(req));
        assertEquals(RentErrorCode.ALIPAY_CALL_FAILED.getCode(), e.getCode());

        // 本地已关单留痕（含支付宝子错误码）
        RentOrder saved = orderMapper.selectByOutOrderId(req.getGoodsId() == null ? "" : findOutOrderIdAfterFail(req));
        assertNotNull(saved);
        assertEquals(RentOrderStatus.CLOSED.name(), saved.getOrderStatus());
        assertTrue(saved.getCloseReasonDesc().contains("ISV_PARAM_ERROR"));
        assertNotNull(saved.getCloseTime());
    }

    /** 支付宝失败场景无法从VO拿到订单号，按买家+状态查最近一条 */
    private String findOutOrderIdAfterFail(OrderCreateReq req) {
        return orderMapper.selectList(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RentOrder>()
                                .eq(RentOrder::getBuyerOpenId, "OPENID_IT")
                                .eq(RentOrder::getOrderStatus, RentOrderStatus.CLOSED.name())
                                .orderByDesc(RentOrder::getId))
                .get(0).getOutOrderId();
    }

    @Test
    void createReletShouldSnapshotItemsFromOriginOrder() {
        // 原订单+明细真实落库
        RentGoods goods = insertGoods();
        RentGoodsSku sku = insertSku(goods);
        RentOrder origin = insertOrder(RentOrderStatus.RECEIVED, "ORIGIN" + IdUtil.getSnowflakeNextIdStr());
        RentOrderItem originItem = new RentOrderItem();
        originItem.setOrderId(origin.getId());
        originItem.setItemType("CAR_ITEM");
        originItem.setOutItemId(goods.getOutItemId());
        originItem.setOutSkuId(sku.getOutSkuId());
        originItem.setItemName(goods.getGoodsName());
        originItem.setSalePrice(new BigDecimal("1000"));
        originItem.setItemCnt(1);
        itemMapper.insert(originItem);

        OrderCreateReq req = new OrderCreateReq();
        req.setOrderType("RELET");
        req.setSourceId("SRC-R");
        req.setBuyerOpenId("OPENID_IT");
        req.setOriginOrderId(origin.getOutOrderId());

        OrderCreateVo vo = orderService.create(req);

        // 续订单明细来自原订单快照
        RentOrder saved = orderMapper.selectByOutOrderId(vo.getOutOrderId());
        List<RentOrderItem> items = itemMapper.selectByOrderId(saved.getId());
        assertEquals(goods.getOutItemId(), items.get(0).getOutItemId());
        assertEquals(goods.getGoodsName(), items.get(0).getItemName());

        // 网关请求带relet_info.origin_order_id且明细非空（修复点验证）
        String bizContent = TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_CREATE);
        assertTrue(bizContent.contains("\"relet_info\":{\"origin_order_id\":\"" + origin.getOutOrderId() + "\"}"));
        assertTrue(bizContent.contains("\"out_item_id\":\"" + goods.getOutItemId() + "\""));
    }

    @Test
    void createReletShouldRejectWhenOriginOrderMissing() {
        OrderCreateReq req = new OrderCreateReq();
        req.setOrderType("RELET");
        req.setSourceId("SRC-R");
        req.setBuyerOpenId("OPENID_IT");
        req.setOriginOrderId("GHOST-ORDER");

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.create(req));
        assertEquals(RentErrorCode.ORDER_NOT_FOUND.getCode(), e.getCode());
    }

    @Test
    void createReletShouldRequireOriginOrderId() {
        OrderCreateReq req = new OrderCreateReq();
        req.setOrderType("BUYOUT");
        req.setSourceId("SRC-R");
        req.setBuyerOpenId("OPENID_IT");

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.create(req));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
    }

    // ==================== 查询 ====================

    @Test
    void pageShouldReturnPersistedOrders() {
        RentOrder order = insertOrder(RentOrderStatus.CREATED, "PAGE" + IdUtil.getSnowflakeNextIdStr());

        OrderPageReq req = new OrderPageReq();
        req.setPageNo(1);
        req.setPageSize(10);
        OrderPageReq.Query query = new OrderPageReq.Query();
        query.setOutOrderId(order.getOutOrderId());
        req.setParams(query);

        PageResult<OrderInfoVo> result = orderService.page(req);
        assertEquals(1, result.getTotal());
        assertEquals(order.getOutOrderId(), result.getRecords().get(0).getOutOrderId());
    }

    @Test
    void detailShouldAssembleAllParts() {
        RentOrder order = insertOrder(RentOrderStatus.SIGNED, "DTL" + IdUtil.getSnowflakeNextIdStr());
        RentOrderItem item = new RentOrderItem();
        item.setOrderId(order.getId());
        item.setItemType("CAR_ITEM");
        item.setItemName("集成测试车辆");
        itemMapper.insert(item);
        RentOrderInstallment inst = new RentOrderInstallment();
        inst.setOrderId(order.getId());
        inst.setInstallmentNo(1);
        inst.setInstallmentPrice(new BigDecimal("1000"));
        inst.setBillStatus("0");
        installmentMapper.insert(inst);
        RentOrderSign sign = new RentOrderSign();
        sign.setOrderId(order.getId());
        sign.setSignStatus("1");
        signMapper.insert(sign);

        OrderDetailVo vo = orderService.detail(order.getOutOrderId());
        assertEquals(order.getOutOrderId(), vo.getOrder().getOutOrderId());
        assertEquals("集成测试车辆", vo.getItems().get(0).getItemName());
        assertEquals(Integer.valueOf(1), vo.getInstallments().get(0).getInstallmentNo());
        assertEquals("1", vo.getSign().getSignStatus());
    }

    // ==================== 履约状态机（真实网关联动） ====================

    @Test
    void fulfillmentSendMerchantShouldRequireApproved() {
        RentOrder order = insertOrder(RentOrderStatus.CREATED, "FS" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.FulfillmentSend req = new OrderActionReq.FulfillmentSend();
        req.setOutOrderId(order.getOutOrderId());
        req.setOperationType("MERCHANT_DELIVERY_SEND");

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.fulfillmentSend(req));
        assertEquals(RentErrorCode.ORDER_STATUS_NOT_ALLOW.getCode(), e.getCode());
        assertTrue(TestAlipayEnv.GATEWAY.firstBizContent(
                RentConstants.METHOD_FULFILLMENT_SEND) == null);
    }

    @Test
    void fulfillmentSendMerchantShouldMoveToDelivered() {
        RentOrder order = insertOrder(RentOrderStatus.APPROVED, "FS" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.FulfillmentSend req = new OrderActionReq.FulfillmentSend();
        req.setOutOrderId(order.getOutOrderId());
        req.setOperationType("MERCHANT_DELIVERY_SEND");

        orderService.fulfillmentSend(req);

        assertEquals(RentOrderStatus.DELIVERED.name(),
                orderMapper.selectByOutOrderId(order.getOutOrderId()).getOrderStatus());
        assertNotNull(TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_FULFILLMENT_SEND));
    }

    @Test
    void fulfillmentSendUserReturnShouldMoveToReturnDelivered() {
        RentOrder order = insertOrder(RentOrderStatus.RECEIVED, "FS" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.FulfillmentSend req = new OrderActionReq.FulfillmentSend();
        req.setOutOrderId(order.getOutOrderId());
        req.setOperationType("USER_DELIVERY_SEND");

        orderService.fulfillmentSend(req);

        assertEquals(RentOrderStatus.RETURN_DELIVERED.name(),
                orderMapper.selectByOutOrderId(order.getOutOrderId()).getOrderStatus());
    }

    @Test
    void receiveConfirmShouldMoveToReceived() {
        RentOrder order = insertOrder(RentOrderStatus.DELIVERED, "RC" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.ReceiveConfirm req = new OrderActionReq.ReceiveConfirm();
        req.setOutOrderId(order.getOutOrderId());
        req.setReceiveType("MERCHANT_DELIVERY_RECEIVED");

        orderService.receiveConfirm(req);

        assertEquals(RentOrderStatus.RECEIVED.name(),
                orderMapper.selectByOutOrderId(order.getOutOrderId()).getOrderStatus());
    }

    @Test
    void finishShouldMoveToFinishedFromReturnReceived() {
        RentOrder order = insertOrder(RentOrderStatus.RETURN_RECEIVED, "FN" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.Finish req = new OrderActionReq.Finish();
        req.setOutOrderId(order.getOutOrderId());
        req.setFinishStatus("USER_RETURNED");

        orderService.finish(req);

        RentOrder saved = orderMapper.selectByOutOrderId(order.getOutOrderId());
        assertEquals(RentOrderStatus.FINISHED.name(), saved.getOrderStatus());
        assertEquals("USER_RETURNED", saved.getFinishStatus());
        assertNotNull(TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_FULFILLMENT_FINISH));
    }

    @Test
    void finishShouldRejectWrongStatus() {
        insertOrder(RentOrderStatus.RECEIVED, "FN" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.Finish req = new OrderActionReq.Finish();
        req.setOutOrderId(orderLatestOutOrderId());
        req.setFinishStatus("USER_RETURNED");

        assertThrows(BusinessException.class, () -> orderService.finish(req));
    }

    private String orderLatestOutOrderId() {
        return orderMapper.selectList(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RentOrder>()
                                .orderByDesc(RentOrder::getId))
                .get(0).getOutOrderId();
    }

    @Test
    void closeShouldSyncAlipayAndCloseLocally() {
        RentOrder order = insertOrder(RentOrderStatus.SIGNED, "CL" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.Close req = new OrderActionReq.Close();
        req.setOutOrderId(order.getOutOrderId());
        req.setReasonCode("3114");
        req.setReasonDesc("用户取消");

        orderService.close(req);

        RentOrder saved = orderMapper.selectByOutOrderId(order.getOutOrderId());
        assertEquals(RentOrderStatus.CLOSED.name(), saved.getOrderStatus());
        assertEquals("3114", saved.getCloseReasonCode());
        String bizContent = TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_CLOSE);
        assertNotNull(bizContent);
        assertTrue(bizContent.contains("\"reason_code\":\"3114\""));
    }

    @Test
    void modifyShouldSyncLocalOrderAndInstallments() {
        RentOrder order = insertOrder(RentOrderStatus.SIGNED, "MD" + IdUtil.getSnowflakeNextIdStr());
        RentOrderInstallment inst = new RentOrderInstallment();
        inst.setOrderId(order.getId());
        inst.setInstallmentNo(1);
        inst.setInstallmentPrice(new BigDecimal("1000"));
        inst.setPlanPayTime(java.time.LocalDateTime.parse("2026-11-01T00:00:00"));
        inst.setBillStatus("0");
        installmentMapper.insert(inst);

        OrderActionReq.Modify req = new OrderActionReq.Modify();
        req.setOutOrderId(order.getOutOrderId());
        req.setRentStartTime("2026-11-01 00:00:00");
        req.setRentEndTime("2027-11-01 00:00:00");
        OrderActionReq.ModifyInstallment mi = new OrderActionReq.ModifyInstallment();
        mi.setInstallmentNo(1);
        mi.setPlanPayTime("2026-12-01 00:00:00");
        req.setInstallments(Collections.singletonList(mi));

        orderService.modify(req);

        RentOrder saved = orderMapper.selectByOutOrderId(order.getOutOrderId());
        assertEquals(java.time.LocalDateTime.parse("2026-11-01T00:00:00"), saved.getRentStartTime());
        RentOrderInstallment savedInst = installmentMapper.selectByOrderIdAndNo(order.getId(), 1);
        assertEquals(java.time.LocalDateTime.parse("2026-12-01T00:00:00"), savedInst.getPlanPayTime());
        assertNotNull(TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_MODIFY));
    }

    @Test
    void modifyShouldRejectWhenInstallmentMissing() {
        RentOrder order = insertOrder(RentOrderStatus.SIGNED, "MD" + IdUtil.getSnowflakeNextIdStr());
        OrderActionReq.Modify req = new OrderActionReq.Modify();
        req.setOutOrderId(order.getOutOrderId());
        req.setRentStartTime("2026-11-01 00:00:00");
        req.setRentEndTime("2027-11-01 00:00:00");
        OrderActionReq.ModifyInstallment mi = new OrderActionReq.ModifyInstallment();
        mi.setInstallmentNo(99);
        mi.setPlanPayTime("2026-12-01 00:00:00");
        req.setInstallments(Collections.singletonList(mi));

        BusinessException e = assertThrows(BusinessException.class, () -> orderService.modify(req));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
    }

    @Test
    void allActionsShouldRejectUnknownOrder() {
        OrderActionReq.Close close = new OrderActionReq.Close();
        close.setOutOrderId("GHOST");
        close.setReasonCode("3114");
        assertThrows(BusinessException.class, () -> orderService.close(close));
        assertThrows(BusinessException.class, () -> orderService.detail("GHOST"));
    }
}
