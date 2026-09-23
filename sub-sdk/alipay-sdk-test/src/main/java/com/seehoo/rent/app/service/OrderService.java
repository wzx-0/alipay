package com.seehoo.rent.app.service;

import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderCreateReq;
import com.seehoo.rent.app.dto.OrderCreateVo;
import com.seehoo.rent.app.dto.OrderDetailVo;
import com.seehoo.rent.app.dto.OrderInfoVo;
import com.seehoo.rent.app.dto.OrderPageReq;

/** 租赁订单服务：创单/查询/履约/关单/改租期 */
public interface OrderService {

    /** 创建租赁订单：本地落库后调支付宝order.create（RENT/RELET/BUYOUT共用） */
    OrderCreateVo create(OrderCreateReq req);

    /** 订单分页查询 */
    PageResult<OrderInfoVo> page(OrderPageReq req);

    /** 订单详情（含明细/期次/签约） */
    OrderDetailVo detail(String outOrderId);

    /** 履约同步：商家交车/用户还车 */
    void fulfillmentSend(OrderActionReq.FulfillmentSend req);

    /** 收货确认：用户收车/商家验收 */
    void receiveConfirm(OrderActionReq.ReceiveConfirm req);

    /** 订单完结（自动解冻+解除代扣） */
    void finish(OrderActionReq.Finish req);

    /** 订单关闭（确认收货前） */
    void close(OrderActionReq.Close req);

    /** 修改租期与计划扣款时间 */
    void modify(OrderActionReq.Modify req);
}
