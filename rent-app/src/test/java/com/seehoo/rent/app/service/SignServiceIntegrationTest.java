package com.seehoo.rent.app.service;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.support.IntegrationTestBase;
import com.seehoo.rent.app.support.TestAlipayEnv;
import com.seehoo.rent.sdk.RentConstants;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 签约服务集成测试：签约串获取（真实网关）与要素组装 */
class SignServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private SignService signService;
    @Autowired
    private RentOrderMapper orderMapper;

    @Test
    void shouldReturnSignStrWithConsistentElements() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("SGN" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.CREATED.name());
        orderMapper.insert(order);

        OrderActionVo.SignStr vo = signService.getSignStr(order.getOutOrderId());

        // 网关真实返回签约串
        assertTrue(vo.getSignStr() != null && !vo.getSignStr().isEmpty());
        assertEquals("LAUNCH_1", vo.getSignLaunchMethod());

        // 签约要素与创单一致：芝麻免押+代扣场景
        String bizContent = TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_SIGN);
        assertNotNull(bizContent);
        assertTrue(bizContent.contains("\"zm_service_id\":\"ZM_TEST_001\""));
        assertTrue(bizContent.contains("RENT_DEDUCT"));
    }

    @Test
    void shouldRejectUnknownOrder() {
        assertThrows(BusinessException.class, () -> signService.getSignStr("GHOST"));
    }
}
