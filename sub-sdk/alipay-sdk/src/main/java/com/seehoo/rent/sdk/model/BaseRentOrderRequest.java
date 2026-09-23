package com.seehoo.rent.sdk.model;

import lombok.Data;

/**
 * 订单类接口公共入参：支付宝单号与商家单号至少传一个
 * <p>序列化说明：SDK统一使用SNAKE_CASE命名策略，camelCase字段自动映射为支付宝协议的snake_case字段</p>
 */
@Data
public class BaseRentOrderRequest {

    /** 支付宝订单ID（创单成功后返回，与outOrderId二选一） */
    private String orderId;

    /** 商家侧订单ID（创单时商家自行生成） */
    private String outOrderId;
}
