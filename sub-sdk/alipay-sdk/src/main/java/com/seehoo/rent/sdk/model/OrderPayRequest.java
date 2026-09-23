package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * alipay.commerce.rent.order.pay 订单支付请求
 * <p>JSAPI方式同步返回trade_no供前端my.tradePay唤起收银台；
 * payNotifyUrl由应用从配置填充，支付宝异步回调支付结果</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPayRequest extends BaseRentOrderRequest {

    /** 支付方式 */
    private PayMethod payMethod;

    /** 本次支付费项列表 */
    private List<PayItem> payItems;

    /** 支付结果异步通知地址（应用配置填充） */
    private String payNotifyUrl;

    /** 相对超时时间（选填，如"15m"，TODO联调确认格式） */
    private String timeoutExpress;

    /** 支付方式枚举（枚举值TODO联调时与文档核对） */
    public enum PayMethod {

        /** 小程序收银台主动支付 */
        JSAPI,
        /** 周期代扣 */
        RENT_DEDUCT,
        /** 预授权转支付 */
        PRE_AUTH
    }
}
