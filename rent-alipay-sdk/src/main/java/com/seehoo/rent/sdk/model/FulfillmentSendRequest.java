package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * alipay.commerce.rent.order.fulfillment.send 发货/寄回请求
 * <p>APPROVED状态传MERCHANT_DELIVERY_SEND交车；RECEIVED状态传USER_DELIVERY_SEND归还寄出</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FulfillmentSendRequest extends BaseRentOrderRequest {

    /** 操作类型 */
    private SendType operationType;

    /** 操作类型枚举 */
    public enum SendType {

        /** 商家发货（交车） */
        MERCHANT_DELIVERY_SEND,
        /** 用户寄回（还车寄出） */
        USER_DELIVERY_SEND
    }
}
