package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** alipay.commerce.rent.order.fulfillment.approve 商家审核通过请求（风控通过后调用） */
@Data
@EqualsAndHashCode(callSuper = true)
public class FulfillmentApproveRequest extends BaseRentOrderRequest {

    /** 买家openId */
    private String openId;
}
