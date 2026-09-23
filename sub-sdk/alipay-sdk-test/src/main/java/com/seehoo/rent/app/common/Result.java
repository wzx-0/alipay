package com.seehoo.rent.app.common;

import lombok.Data;

/**
 * 统一响应结构：{code, message, data, success, timestamp}，与REQ001-API-Definition约定一致
 */
@Data
public class Result<T> {

    private static final String SUCCESS_CODE = "SY000000";
    private static final String SUCCESS_MESSAGE = "交易成功";

    private String code;
    private String message;
    private T data;
    private boolean success;
    private String timestamp;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = SUCCESS_CODE;
        r.message = SUCCESS_MESSAGE;
        r.data = data;
        r.success = true;
        r.timestamp = String.valueOf(System.currentTimeMillis());
        return r;
    }

    public static <T> Result<T> fail(String code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        r.success = false;
        r.timestamp = String.valueOf(System.currentTimeMillis());
        return r;
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String message) {
        return fail(errorCode.getCode(), message);
    }
}
