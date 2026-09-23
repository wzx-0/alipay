package com.seehoo.rent.sdk.model;

import lombok.Data;

/**
 * 支付费项（order.pay的pay_items / order.pay.sync / aftersale.create赔付费项复用）
 */
@Data
public class PayItem {

    /** 费项类型 */
    private FeeType type;

    /** 期次号（type=RENT时必传） */
    private Integer installmentNo;

    /** 本次支付金额（元，2位小数） */
    private String payAmount;

    /** 费项类型枚举 */
    public enum FeeType {

        /** 订阅租金 */
        RENT,
        /** 赔偿金（售后赔付场景） */
        INDEMNITY
    }
}
