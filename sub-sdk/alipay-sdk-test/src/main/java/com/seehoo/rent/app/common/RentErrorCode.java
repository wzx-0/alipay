package com.seehoo.rent.app.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 汽车订阅模块错误码：编码规则 = 模块缩写RENT + 3位序号
 */
@Getter
@AllArgsConstructor
public enum RentErrorCode implements ErrorCode {

    /** 通用参数错误 */
    PARAM_ERROR("RENT001", "请求参数不合法"),
    /** 订单不存在 */
    ORDER_NOT_FOUND("RENT002", "订单不存在"),
    /** 订单状态不允许当前操作 */
    ORDER_STATUS_NOT_ALLOW("RENT003", "订单当前状态不允许该操作"),
    /** 商品或SKU不存在 */
    GOODS_NOT_FOUND("RENT004", "商品或SKU不存在"),
    /** 支付宝接口调用失败 */
    ALIPAY_CALL_FAILED("RENT005", "支付宝接口调用失败"),
    /** 支付宝异步通知验签失败 */
    NOTIFY_SIGN_INVALID("RENT006", "支付宝通知验签失败"),
    /** 售后单不存在（预留） */
    AFTERSALE_NOT_FOUND("RENT007", "售后单不存在"),
    /** 系统内部异常 */
    SYSTEM_ERROR("SYS000", "系统繁忙，请稍后重试");

    private final String code;
    private final String message;
}
