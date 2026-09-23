package com.seehoo.rent.app.controller;

import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.common.Result;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderCreateReq;
import com.seehoo.rent.app.dto.OrderCreateVo;
import com.seehoo.rent.app.dto.OrderDetailReq;
import com.seehoo.rent.app.dto.OrderDetailVo;
import com.seehoo.rent.app.dto.OrderInfoVo;
import com.seehoo.rent.app.dto.OrderPageReq;
import com.seehoo.rent.app.service.OrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/** 租赁订单接口：创单/查询/履约/关单/改租期 */
@Validated
@RestController
@RequestMapping("/api/v1/rent/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /** A0102001 创建租赁订单（小程序插件下单回调） */
    @PostMapping("/create")
    public Result<OrderCreateVo> create(@Valid @RequestBody OrderCreateReq req) {
        return Result.success(orderService.create(req));
    }

    /** A0102002 订单分页查询 */
    @PostMapping("/page")
    public Result<PageResult<OrderInfoVo>> page(@Valid @RequestBody OrderPageReq req) {
        return Result.success(orderService.page(req));
    }

    /** A0102003 订单详情查询 */
    @PostMapping("/detail")
    public Result<OrderDetailVo> detail(@Valid @RequestBody OrderDetailReq req) {
        return Result.success(orderService.detail(req.getOutOrderId()));
    }

    /** A0102004 订单履约同步（商家交车/用户还车） */
    @PostMapping("/fulfillment/send")
    public Result<Void> fulfillmentSend(@Valid @RequestBody OrderActionReq.FulfillmentSend req) {
        orderService.fulfillmentSend(req);
        return Result.success(null);
    }

    /** A0102005 订单收货确认 */
    @PostMapping("/fulfillment/receive")
    public Result<Void> receive(@Valid @RequestBody OrderActionReq.ReceiveConfirm req) {
        orderService.receiveConfirm(req);
        return Result.success(null);
    }

    /** A0102006 订单完结 */
    @PostMapping("/fulfillment/finish")
    public Result<Void> finish(@Valid @RequestBody OrderActionReq.Finish req) {
        orderService.finish(req);
        return Result.success(null);
    }

    /** A0102007 订单关闭 */
    @PostMapping("/close")
    public Result<Void> close(@Valid @RequestBody OrderActionReq.Close req) {
        orderService.close(req);
        return Result.success(null);
    }

    /** A0102008 修改租期 */
    @PostMapping("/modify")
    public Result<Void> modify(@Valid @RequestBody OrderActionReq.Modify req) {
        orderService.modify(req);
        return Result.success(null);
    }
}
