package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * alipay.commerce.rent.order.fulfillment.receive 收货确认请求
 * <p>DELIVERED状态传MERCHANT_DELIVERY_RECEIVED（用户确认收货）；
 * RETURN_DELIVERED状态传USER_DELIVERY_RECEIVED（商家确认收到还车）</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FulfillmentReceiveRequest extends BaseRentOrderRequest {

    /** 收货类型 */
    private ReceiveType receiveType;

    /** 收货类型枚举 */
    public enum ReceiveType {

        /** 商家发货的用户收货确认 */
        MERCHANT_DELIVERY_RECEIVED,
        /** 用户寄回的商家收货确认 */
        USER_DELIVERY_RECEIVED
    }
}
