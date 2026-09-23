package com.seehoo.rent.sdk.model;

import lombok.Data;

/** alipay.trade.refund 统一收单退款请求（取消订单退已支付费项） */
@Data
public class TradeRefundRequest {

    /** 支付宝交易号（tradeNo与outTradeNo二选一） */
    private String tradeNo;

    /** 商家侧交易号（支付时的out_trade_no） */
    private String outTradeNo;

    /** 退款金额（元，2位小数，不能大于支付金额） */
    private String refundAmount;

    /** 商家侧退款请求号（同一笔部分退款多次发起时递增） */
    private String outRequestNo;

    /** 退款原因（选填） */
    private String refundReason;
}
