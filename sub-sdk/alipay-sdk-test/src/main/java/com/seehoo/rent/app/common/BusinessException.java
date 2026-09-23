package com.seehoo.rent.app.common;

import lombok.Getter;

/**
 * 业务异常：由全局异常处理器统一转换为统一响应结构
 */
@Getter
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /** 附加上下文信息（如支付宝sub_code/失败原因），不吞原始异常时用cause构造保留堆栈 */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
