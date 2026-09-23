package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * alipay.commerce.rent.order.sign 签约请求：返回sign_str供前端唤起芝麻免押受理台
 * <p>rentSignInfo与创单保持一致，受理台退出后补偿唤起时传入</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderSignRequest extends BaseRentOrderRequest {

    /** 签约要素（选填，缺省沿用创单要素） */
    private RentSignInfo rentSignInfo;
}
