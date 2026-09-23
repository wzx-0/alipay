package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * alipay.commerce.rent.order.modify 修改租期请求（type=RENT_PLAN_TIME）
 * <p>调整整体租期时间传rentStartTime/rentEndTime；调整单期计划支付时间传rentPlanInfo.installments</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderModifyRequest extends BaseRentOrderRequest {

    /** 修改类型 */
    private ModifyType type;

    /** 新租期开始时间（yyyy-MM-dd HH:mm:ss） */
    private String rentStartTime;

    /** 新租期结束时间 */
    private String rentEndTime;

    /** 调整的期次计划（只传需调整的期次） */
    private RentPlanInfo rentPlanInfo;

    /** 修改类型枚举 */
    public enum ModifyType {

        /** 修改租期时间 */
        RENT_PLAN_TIME
    }

    /** 期次计划容器 */
    @Data
    public static class RentPlanInfo {

        /** 调整的期次列表 */
        private List<Installment> installments;
    }

    /** 单期调整项 */
    @Data
    public static class Installment {

        /** 期次号 */
        private Integer installmentNo;

        /** 新计划支付时间（yyyy-MM-dd HH:mm:ss） */
        private String planPayTime;
    }
}
