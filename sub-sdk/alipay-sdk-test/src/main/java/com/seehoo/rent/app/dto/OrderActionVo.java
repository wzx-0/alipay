package com.seehoo.rent.app.dto;

import lombok.Data;

/**
 * 订单操作类返回数据集合
 */
public class OrderActionVo {

    private OrderActionVo() {
    }

    /** A0103001 签约串返回 */
    @Data
    public static class SignStr {
        /** 签约字符串，供前端唤起芝麻受理台 */
        private String signStr;
        /** 唤起方式，以支付宝返回为准 */
        private String signLaunchMethod;
    }

    /** A0104001 风控咨询返回 */
    @Data
    public static class RiskConsult {
        /** 综合风险等级T1~T10 */
        private String riskLevel;
        /** 审核结论：1-通过 2-拒绝 3-转人工（已有审核记录时返回） */
        private String auditResult;
        /** 风控咨询详情JSON */
        private String consultResult;
    }

    /** A0105001 订单支付返回 */
    @Data
    public static class Pay {
        /** 支付宝交易号，JSAPI场景返回供前端my.tradePay */
        private String tradeNo;
        /** 商家侧支付单号 */
        private String outTradeNo;
        /** 支付总金额(元) */
        private String payAmount;
    }
}
