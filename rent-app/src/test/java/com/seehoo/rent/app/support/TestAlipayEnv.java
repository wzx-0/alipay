package com.seehoo.rent.app.support;

import org.springframework.test.context.DynamicPropertyRegistry;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * 测试环境共享资源：运行时生成RSA密钥对（密钥不硬编码进代码库）、启动本地支付宝网关，
 * 并把这些值通过@DynamicPropertySource注入Spring配置（覆盖application.yml的TODO占位值）。
 */
public final class TestAlipayEnv {

    /** 测试专用密钥对：应用私钥=本地网关公钥，构成完整互验签链路 */
    public static final KeyPair KEY_PAIR;
    /** 本地支付宝网关，整个测试JVM共享 */
    public static final AlipayGatewayStub GATEWAY;

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KEY_PAIR = generator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("测试RSA密钥对生成失败", e);
        }
        GATEWAY = AlipayGatewayStub.start(KEY_PAIR);
    }

    private TestAlipayEnv() {
    }

    /** 注册alipay.rent.*测试属性：真实密钥+真实本地网关，开启响应验签走最严格链路 */
    public static void register(DynamicPropertyRegistry registry) {
        registry.add("alipay.rent.app-id", () -> "2021000000000001");
        registry.add("alipay.rent.app-private-key",
                () -> Base64.getEncoder().encodeToString(KEY_PAIR.getPrivate().getEncoded()));
        registry.add("alipay.rent.alipay-public-key", GATEWAY::publicKeyBase64);
        registry.add("alipay.rent.gateway", GATEWAY::gatewayUrl);
        registry.add("alipay.rent.verify-response-sign", () -> "true");
        registry.add("alipay.rent.zm-service-id", () -> "ZM_TEST_001");
        registry.add("alipay.rent.category-id", () -> "CAT_TEST_001");
        registry.add("alipay.rent.freeze-notify-url", () -> "http://127.0.0.1:1/app-notify");
        registry.add("alipay.rent.pay-notify-url", () -> "http://127.0.0.1:1/app-notify");
    }
}
