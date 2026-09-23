package com.seehoo.rent.sdk;

import lombok.Getter;

/**
 * 支付宝对接异常：网关调用失败、签名失败、支付宝返回业务错误时抛出
 */
@Getter
public class AlipayRentException extends RuntimeException {

    /** 支付宝网关错误码（error_response的code），本地错误时为null */
    private final String code;

    /** 支付宝子错误码（sub_code） */
    private final String subCode;

    public AlipayRentException(String message) {
        super(message);
        this.code = null;
        this.subCode = null;
    }

    public AlipayRentException(String message, Throwable cause) {
        super(message, cause);
        this.code = null;
        this.subCode = null;
    }

    /** 支付宝业务错误（含网关错误码） */
    public AlipayRentException(String message, String code, String subCode) {
        super(message);
        this.code = code;
        this.subCode = subCode;
    }
}
