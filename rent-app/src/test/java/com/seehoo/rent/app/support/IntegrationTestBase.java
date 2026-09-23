package com.seehoo.rent.app.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 集成测试基类：@SpringBootTest启动真实Spring上下文（真实MySQL、真实本地支付宝网关），
 * 测试加@Transactional，结束后自动回滚不留脏数据。
 */
@SpringBootTest
@Transactional
public abstract class IntegrationTestBase {

    @org.springframework.test.context.DynamicPropertySource
    static void alipayEnv(org.springframework.test.context.DynamicPropertyRegistry registry) {
        TestAlipayEnv.register(registry);
    }

    @BeforeEach
    void resetGatewayStub() {
        TestAlipayEnv.GATEWAY.reset();
    }
}
