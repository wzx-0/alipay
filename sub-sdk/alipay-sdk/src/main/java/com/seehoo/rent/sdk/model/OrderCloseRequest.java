package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * alipay.commerce.rent.order.close 订单关闭请求
 * <p>仅确认收货前可关单；已支付费项须先全额退款，否则支付宝侧拒绝</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCloseRequest extends BaseRentOrderRequest {

    /** 关闭原因编码（枚举值TODO联调时与文档核对，风控拒绝参考值3114） */
    private String reasonCode;

    /** 关闭原因描述（选填） */
    private String reasonDesc;
}
