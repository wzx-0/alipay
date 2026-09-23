package com.seehoo.rent.app.dto;

import lombok.Data;

/** A0102001 创建租赁订单返回 */
@Data
public class OrderCreateVo {

    /** 支付宝订单ID */
    private String alipayOrderId;
    /** 商家侧订单号 */
    private String outOrderId;
    /** 订单详情页路径（支付完成后前端跳转） */
    private String path;
}
