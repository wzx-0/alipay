package com.seehoo.rent.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.dto.PayReq;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import com.seehoo.rent.app.entity.RentPayItem;
import com.seehoo.rent.app.entity.RentPayRecord;
import com.seehoo.rent.app.mapper.RentOrderInstallmentMapper;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RentPayItemMapper;
import com.seehoo.rent.app.mapper.RentPayRecordMapper;
import com.seehoo.rent.app.service.PayService;
import com.seehoo.rent.app.config.AlipayProperties;
import com.seehoo.rent.sdk.AlipayRentClient;
import com.seehoo.rent.sdk.BizFields;
import com.seehoo.rent.sdk.RentResponse;
import com.seehoo.rent.sdk.model.OrderPayRequest;
import com.seehoo.rent.sdk.model.PayItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 支付服务实现：期次自动代扣由定时任务触发（后续阶段），本服务处理管理端/前端发起的支付 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    /** 支付渠道：支付宝 */
    private static final String PAY_CHANNEL_ALIPAY = "ALIPAY";
    private static final String PAY_STATUS_PROCESSING = "0";
    private static final String PAY_STATUS_SUCCESS = "1";
    /** 账单状态：待支付/支付中/已支付 */
    private static final String BILL_STATUS_WAIT_PAY = "0";
    private static final String BILL_STATUS_PAYING = "1";
    private static final String BILL_STATUS_PAID = "2";
    /** 交易状态：成功/完结 */
    private static final String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";
    private static final String TRADE_STATUS_FINISHED = "TRADE_FINISHED";
    /** 商家侧支付单号后缀起始序号 */
    private static final int OUT_TRADE_SEQ_START = 1;
    private static final String OUT_TRADE_SEQ_FMT = "%04d";

    @Resource
    private RentOrderMapper orderMapper;
    @Resource
    private RentPayRecordMapper payRecordMapper;
    @Resource
    private RentPayItemMapper payItemMapper;
    @Resource
    private RentOrderInstallmentMapper installmentMapper;
    @Resource
    private AlipayRentClient alipayRentClient;
    @Resource
    private AlipayProperties alipayProperties;

    @Override
    public OrderActionVo.Pay pay(PayReq req) {
        RentOrder order = orderMapper.selectByOutOrderId(req.getOutOrderId());
        if (order == null) {
            throw new BusinessException(RentErrorCode.ORDER_NOT_FOUND);
        }
        checkPayItems(req);

        String outTradeNo = genOutTradeNo(order);
        RentResponse<Map<String, Object>> resp = alipayRentClient.orderPay(
                buildPayRequest(order, req));

        BigDecimal totalAmount = sumPayAmount(req);
        RentPayRecord record = savePayRecord(order, outTradeNo, req, totalAmount, resp);
        updateInstallmentPaying(order.getId(), req);

        OrderActionVo.Pay vo = new OrderActionVo.Pay();
        vo.setTradeNo(resp.getStr(BizFields.TRADE_NO));
        vo.setOutTradeNo(outTradeNo);
        vo.setPayAmount(totalAmount.toPlainString());
        return vo;
    }

    /** 费项业务校验：类型必须合法（入口拦截，避免valueOf异常冒泡）；订阅金须传期号 */
    private void checkPayItems(PayReq req) {
        for (PayReq.PayItem item : req.getPayItems()) {
            try {
                PayItem.FeeType.valueOf(item.getFeeType());
            } catch (IllegalArgumentException e) {
                throw new BusinessException(RentErrorCode.PARAM_ERROR, "费项类型非法：" + item.getFeeType());
            }
            if (PayItem.FeeType.RENT.name().equals(item.getFeeType()) && item.getInstallmentNo() == null) {
                throw new BusinessException(RentErrorCode.PARAM_ERROR, "订阅金费项必须传期号");
            }
        }
    }

    /** 商家侧支付单号：订单号+P+四位流水（同订单重复发起时递增） */
    private String genOutTradeNo(RentOrder order) {
        int seq = payRecordMapper.selectByOrderId(order.getId()).size() + OUT_TRADE_SEQ_START;
        return order.getOutOrderId() + "P" + String.format(OUT_TRADE_SEQ_FMT, seq);
    }

    /** 组装支付宝order.pay请求（payNotifyUrl由应用配置填充） */
    private OrderPayRequest buildPayRequest(RentOrder order, PayReq req) {
        OrderPayRequest request = new OrderPayRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(req.getOutOrderId());
        request.setPayMethod(OrderPayRequest.PayMethod.valueOf(req.getPayMethod()));
        request.setPayItems(buildAlipayItems(req));
        request.setPayNotifyUrl(alipayProperties.getPayNotifyUrl());
        request.setTimeoutExpress(req.getPayTimeoutExpress());
        return request;
    }

    private List<PayItem> buildAlipayItems(PayReq req) {
        List<PayItem> items = new ArrayList<>();
        for (PayReq.PayItem item : req.getPayItems()) {
            PayItem row = new PayItem();
            row.setType(PayItem.FeeType.valueOf(item.getFeeType()));
            row.setInstallmentNo(item.getInstallmentNo());
            row.setPayAmount(item.getPayAmount());
            items.add(row);
        }
        return items;
    }

    private BigDecimal sumPayAmount(PayReq req) {
        BigDecimal total = BigDecimal.ZERO;
        for (PayReq.PayItem item : req.getPayItems()) {
            total = total.add(new BigDecimal(item.getPayAmount()));
        }
        return total;
    }

    /** 支付流水落库：同步返回trade_no且交易成功即置成功，否则置处理中等通知确认 */
    private RentPayRecord savePayRecord(RentOrder order, String outTradeNo, PayReq req, BigDecimal totalAmount,
                                        RentResponse<Map<String, Object>> resp) {
        RentPayRecord record = new RentPayRecord();
        record.setOrderId(order.getId());
        record.setOutTradeNo(outTradeNo);
        record.setPayMethod(req.getPayMethod());
        record.setPayAmount(totalAmount);
        record.setPayChannel(PAY_CHANNEL_ALIPAY);
        String tradeNo = resp.getStr(BizFields.TRADE_NO);
        String tradeStatus = resp.getStr(BizFields.TRADE_STATUS);
        boolean syncSuccess = StrUtil.isNotBlank(tradeNo)
                && (tradeStatus == null || TRADE_STATUS_SUCCESS.equals(tradeStatus) || TRADE_STATUS_FINISHED.equals(tradeStatus));
        // TODO 支付初始状态判定规则待联调时按order.pay同步响应确认
        record.setPayStatus(syncSuccess ? PAY_STATUS_SUCCESS : PAY_STATUS_PROCESSING);
        record.setTradeNo(tradeNo);
        if (syncSuccess) {
            record.setPayTime(LocalDateTime.now());
        }
        payRecordMapper.insert(record);
        Db.saveBatch(buildPayItems(record, order, req));
        return record;
    }

    /** 支付费项流水 */
    private List<RentPayItem> buildPayItems(RentPayRecord record, RentOrder order, PayReq req) {
        List<RentPayItem> items = new ArrayList<>();
        for (PayReq.PayItem item : req.getPayItems()) {
            RentPayItem pi = new RentPayItem();
            pi.setPayRecordId(record.getId());
            pi.setOrderId(order.getId());
            pi.setFeeType(item.getFeeType());
            pi.setInstallmentNo(item.getInstallmentNo());
            pi.setPayAmount(new BigDecimal(item.getPayAmount()));
            items.add(pi);
        }
        return items;
    }

    /** 订阅金费项对应期次置支付中（已支付期次不动） */
    private void updateInstallmentPaying(Long orderId, PayReq req) {
        List<RentOrderInstallment> updates = new ArrayList<>();
        for (PayReq.PayItem item : req.getPayItems()) {
            if (!PayItem.FeeType.RENT.name().equals(item.getFeeType())) {
                continue;
            }
            RentOrderInstallment inst = installmentMapper.selectByOrderIdAndNo(orderId, item.getInstallmentNo());
            if (inst != null && BILL_STATUS_WAIT_PAY.equals(inst.getBillStatus())) {
                inst.setBillStatus(BILL_STATUS_PAYING);
                updates.add(inst);
            }
        }
        installmentMapper.updateBatch(updates);
    }

}
