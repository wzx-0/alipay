package com.seehoo.rent.app.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/** A0102002 订单分页查询入参 */
@Data
public class OrderPageReq {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNo;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private Integer pageSize;

    @Valid
    @NotNull(message = "查询条件不能为空")
    private Query params;

    /** 查询条件（均非必填） */
    @Data
    public static class Query {
        private String outOrderId;
        private String alipayOrderId;
        /** 订单类型：RENT/RELET/BUYOUT */
        private String orderType;
        /** 订单状态：CREATED/SIGNED/APPROVED/DELIVERED/RECEIVED/RETURN_DELIVERED/RETURN_RECEIVED/FINISHED/CLOSED */
        private String orderStatus;
        /** 来源渠道：1-公域 0-私域 */
        private String sourceChannel;
        /** 买家支付宝userId */
        private String buyerId;
        /** 创建时间起 yyyy-MM-dd HH:mm:ss */
        private String createTimeBegin;
        /** 创建时间止 yyyy-MM-dd HH:mm:ss */
        private String createTimeEnd;
    }
}
