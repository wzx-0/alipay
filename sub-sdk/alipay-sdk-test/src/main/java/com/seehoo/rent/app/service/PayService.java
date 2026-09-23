package com.seehoo.rent.app.service;

import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.dto.PayReq;

/** 支付服务 */
public interface PayService {

    /** 发起订单支付，JSAPI返回tradeNo供前端唤起收银台 */
    OrderActionVo.Pay pay(PayReq req);
}
