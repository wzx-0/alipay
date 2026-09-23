package com.seehoo.rent.app.service;

import java.util.Map;

/** 支付宝异步通知服务：验签/幂等/落库/分发 */
public interface NotifyService {

    /**
     * 处理支付宝异步通知
     *
     * @return 支付宝协议应答文本 success/fail
     */
    String handleNotify(Map<String, String> params);
}
