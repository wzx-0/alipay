package com.seehoo.rent.app.common;

import com.seehoo.rent.sdk.AlipayRentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：Controller不手动catch，统一转统一响应结构
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        log.warn("业务异常：{} {}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /** 支付宝调用失败：透出sub_code便于联调排查 */
    @ExceptionHandler(AlipayRentException.class)
    public Result<Void> handleAlipay(AlipayRentException e) {
        log.error("支付宝调用失败：code={}, subCode={}", e.getCode(), e.getSubCode(), e);
        String msg = e.getSubCode() == null ? e.getMessage() : e.getMessage() + "（sub_code=" + e.getSubCode() + "）";
        return Result.fail(RentErrorCode.ALIPAY_CALL_FAILED, msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValid(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError == null ? RentErrorCode.PARAM_ERROR.getMessage()
                : fieldError.getField() + " " + fieldError.getDefaultMessage();
        return Result.fail(RentErrorCode.PARAM_ERROR, msg);
    }

    /** 枚举valueOf等非法取值：按参数错误返回，避免落到系统异常 */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("参数取值非法：{}", e.getMessage());
        return Result.fail(RentErrorCode.PARAM_ERROR, "参数取值非法");
    }
    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception e) {
        log.error("系统异常", e);
        return Result.fail(RentErrorCode.SYSTEM_ERROR.getCode(), RentErrorCode.SYSTEM_ERROR.getMessage());
    }
}
