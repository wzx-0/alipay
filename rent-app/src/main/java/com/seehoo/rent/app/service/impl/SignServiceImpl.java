package com.seehoo.rent.app.service.impl;

import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.config.AlipayProperties;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.service.SignService;
import com.seehoo.rent.sdk.AlipayRentClient;
import com.seehoo.rent.sdk.BizFields;
import com.seehoo.rent.sdk.RentResponse;
import com.seehoo.rent.sdk.model.OrderSignRequest;
import com.seehoo.rent.sdk.model.RentSignInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import javax.annotation.Resource;

/** 签约服务实现：受理台退出后补偿唤起 */
@Service
public class SignServiceImpl implements SignService {

    @Resource
    private RentOrderMapper orderMapper;
    @Resource
    private AlipayRentClient alipayRentClient;
    @Resource
    private AlipayProperties alipayProperties;

    @Override
    public OrderActionVo.SignStr getSignStr(String outOrderId) {
        RentOrder order = orderMapper.selectByOutOrderId(outOrderId);
        if (order == null) {
            throw new BusinessException(RentErrorCode.ORDER_NOT_FOUND);
        }
        // rent_sign_info与创单保持一致：芝麻免押credit_info + 代扣场景
        RentSignInfo signInfo = new RentSignInfo();
        RentSignInfo.CreditInfo credit = new RentSignInfo.CreditInfo();
        credit.setZmServiceId(alipayProperties.getZmServiceId());
        credit.setCategoryId(alipayProperties.getCategoryId());
        signInfo.setCreditInfo(credit);
        RentSignInfo.RentDeductInfo deduct = new RentSignInfo.RentDeductInfo();
        deduct.setSignScene(RentSignInfo.SignScene.RENT_DEDUCT);
        signInfo.setRentDeductInfo(deduct);

        OrderSignRequest request = new OrderSignRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(outOrderId);
        request.setRentSignInfo(signInfo);
        RentResponse<Map<String, Object>> resp = alipayRentClient.orderSign(request);

        OrderActionVo.SignStr vo = new OrderActionVo.SignStr();
        vo.setSignStr(resp.getStr(BizFields.SIGN_STR));
        vo.setSignLaunchMethod(resp.getStr(BizFields.SIGN_LAUNCH_METHOD));
        return vo;
    }
}
