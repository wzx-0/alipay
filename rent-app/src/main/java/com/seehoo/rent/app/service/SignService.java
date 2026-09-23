package com.seehoo.rent.app.service;

import com.seehoo.rent.app.dto.OrderActionVo;

/** 签约服务：芝麻免押受理台补偿唤起 */
public interface SignService {

    /** 获取签约串sign_str供前端唤起受理台 */
    OrderActionVo.SignStr getSignStr(String outOrderId);
}
