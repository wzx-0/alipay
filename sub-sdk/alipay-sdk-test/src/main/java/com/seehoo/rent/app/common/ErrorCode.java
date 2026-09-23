package com.seehoo.rent.app.common;

/**
 * 错误码接口：统一响应code/message来源
 */
public interface ErrorCode {

    String getCode();

    String getMessage();
}
