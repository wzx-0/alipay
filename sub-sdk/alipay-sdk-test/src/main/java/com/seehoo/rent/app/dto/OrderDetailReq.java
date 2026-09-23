package com.seehoo.rent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/** A0102003 订单详情入参 */
@Data
public class OrderDetailReq {

    @NotBlank(message = "商家侧订单号不能为空")
    private String outOrderId;
}
