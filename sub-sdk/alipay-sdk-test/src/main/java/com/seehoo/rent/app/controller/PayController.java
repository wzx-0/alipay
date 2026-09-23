package com.seehoo.rent.app.controller;

import com.seehoo.rent.app.common.Result;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.dto.PayReq;
import com.seehoo.rent.app.service.PayService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/** 支付接口：发起订单支付（JSAPI返回tradeNo供前端my.tradePay） */
@Validated
@RestController
@RequestMapping("/api/v1/rent/order")
public class PayController {

    @Resource
    private PayService payService;

    /** A0104001 订单支付 */
    @PostMapping("/pay")
    public Result<OrderActionVo.Pay> pay(@Valid @RequestBody PayReq req) {
        return Result.success(payService.pay(req));
    }
}
