package com.seehoo.rent.app.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 订单操作类入参集合：履约/收货/完结/关单/改租期/签约串，均以商家侧订单号定位
 */
public class OrderActionReq {

    private OrderActionReq() {
    }

    /** A0102004 履约同步：MERCHANT_DELIVERY_SEND商家交车 / USER_DELIVERY_SEND用户还车 */
    @Data
    public static class FulfillmentSend {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
        @NotBlank(message = "履约操作类型不能为空")
        private String operationType;
    }

    /** A0102005 收货确认：MERCHANT_DELIVERY_RECEIVED用户收车 / USER_DELIVERY_RECEIVED商家验收 */
    @Data
    public static class ReceiveConfirm {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
        @NotBlank(message = "收货确认类型不能为空")
        private String receiveType;
    }

    /** A0102006 订单完结 */
    @Data
    public static class Finish {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
        /** USER_RETURNED/USER_RETURNED_IN_ADVANCE/OTHER */
        @NotBlank(message = "完结方式不能为空")
        private String finishStatus;
    }

    /** A0102007 订单关闭 */
    @Data
    public static class Close {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
        @NotBlank(message = "关单原因编码不能为空")
        private String reasonCode;
        private String reasonDesc;
    }

    /** A0102008 修改租期 */
    @Data
    public static class Modify {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
        @NotBlank(message = "租赁开始时间不能为空")
        private String rentStartTime;
        @NotBlank(message = "租赁结束时间不能为空")
        private String rentEndTime;
        /** 仅传需要调整计划扣款时间的期次 */
        @Valid
        private List<ModifyInstallment> installments;
    }

    /** 期次调整项 */
    @Data
    public static class ModifyInstallment {
        @javax.validation.constraints.NotNull(message = "期号不能为空")
        private Integer installmentNo;
        @NotBlank(message = "新计划扣款时间不能为空")
        private String planPayTime;
    }

    /** A0103001 获取签约串 */
    @Data
    public static class Sign {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
    }

    /** A0104001 风控咨询 */
    @Data
    public static class RiskConsult {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
    }

    /** A0104002 提交风控审核结论 */
    @Data
    public static class RiskAudit {
        @NotBlank(message = "商家侧订单号不能为空")
        private String outOrderId;
        /** 1-通过 2-拒绝 */
        @NotBlank(message = "审核结论不能为空")
        private String auditResult;
        /** 拒绝原因，auditResult=2时必填（业务校验） */
        private String rejectReason;
    }
}
