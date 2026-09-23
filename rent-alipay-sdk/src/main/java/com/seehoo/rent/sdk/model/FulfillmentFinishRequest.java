package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * alipay.commerce.rent.order.fulfillment.finish 订单完结请求
 * <p>完结后支付宝自动解冻预授权并解除周期代扣签约</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FulfillmentFinishRequest extends BaseRentOrderRequest {

    /** 完结状态 */
    private FinishStatus finishStatus;

    /** 完结状态枚举 */
    public enum FinishStatus {

        /** 用户已归还 */
        USER_RETURNED,
        /** 用户提前归还 */
        USER_RETURNED_IN_ADVANCE,
        /** 其他 */
        OTHER
    }
}
