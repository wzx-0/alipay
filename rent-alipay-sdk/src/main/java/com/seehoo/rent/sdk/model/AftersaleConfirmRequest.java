package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** alipay.commerce.rent.order.aftersale.confirm 售后处理请求 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AftersaleConfirmRequest extends BaseRentOrderRequest {

    /** 支付宝售后单ID */
    private String aftersaleId;

    /** 处理操作 */
    private OperationType operationType;

    /** 处理操作枚举 */
    public enum OperationType {

        /** 用户取消售后申请 */
        USER_CANCEL_APPLY,
        /** 商家完成售后处理 */
        AFTERSALE_FINISH
    }
}
