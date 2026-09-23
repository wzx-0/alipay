package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * alipay.commerce.rent.order.aftersale.create 售后单创建请求
 * <p>取消类售后传aftersaleType=ORDER_CANCEL；赔付类传COMPENSATION及赔付费项</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AftersaleCreateRequest extends BaseRentOrderRequest {

    /** 售后类型 */
    private AftersaleType aftersaleType;

    /** 售后原因编码（TODO联调确认枚举表） */
    private String reasonCode;

    /** 售后原因描述（选填） */
    private String reasonDesc;

    /** 赔付费项列表（type=INDEMNITY，赔付类售后必传） */
    private List<PayItem> payItems;

    /** 售后类型枚举 */
    public enum AftersaleType {

        /** 订单取消 */
        ORDER_CANCEL,
        /** 赔付 */
        COMPENSATION
    }
}
