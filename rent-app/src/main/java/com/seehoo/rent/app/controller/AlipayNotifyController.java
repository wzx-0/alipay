package com.seehoo.rent.app.controller;

import com.seehoo.rent.app.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 支付宝异步通知入口：支付结果/预授权冻结/售后/消息
 * <p>支付宝协议要求：处理完返回纯文本 success/fail，不能走统一Result包装，也不能抛异常</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/callback/alipay")
public class AlipayNotifyController {

    @Resource
    private NotifyService notifyService;

    /** 统一通知入口（freeze_notify_url/pay_notify_url均指向此地址） */
    @PostMapping("/notify")
    public String notify(@RequestParam Map<String, String> params) {
        return notifyService.handleNotify(params);
    }
}
