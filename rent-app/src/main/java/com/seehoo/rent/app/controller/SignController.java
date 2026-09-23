package com.seehoo.rent.app.controller;

import com.seehoo.rent.app.common.Result;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.service.SignService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/** 签约接口：获取sign_str供前端唤起芝麻免押受理台 */
@Validated
@RestController
@RequestMapping("/api/v1/rent/order")
public class SignController {

    @Resource
    private SignService signService;

    /** A0102009 获取签约串 */
    @PostMapping("/sign")
    public Result<OrderActionVo.SignStr> sign(@Valid @RequestBody OrderActionReq.Sign req) {
        return Result.success(signService.getSignStr(req.getOutOrderId()));
    }
}
