package com.seehoo.rent.sdk.autoconfig;

import com.seehoo.rent.sdk.client.AlipayRentClient;
import com.seehoo.rent.sdk.client.AlipayRentDefaultClient;
import com.seehoo.rent.sdk.config.AlipayRentConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配入口：业务应用引入SDK依赖并配置 alipay.rent.enabled=true 后，
 * 自动注册配置属性与客户端，业务侧直接注入 AlipayRentClient 使用
 */
@Configuration
@ConditionalOnProperty(name = {"alipay.rent.enabled"}, havingValue = "true", matchIfMissing = false)
public class AlipayRentAutoConfiguration {

    /**
     * 注册配置属性（alipay.rent前缀绑定）
     */
    @Bean
    public AlipayRentConfig alipayRentConfig() {
        return new AlipayRentConfig();
    }

    /**
     * 注册支付宝汽车订阅客户端
     */
    @Bean
    public AlipayRentClient alipayRentClient(AlipayRentConfig alipayRentConfig) {
        return new AlipayRentDefaultClient(alipayRentConfig);
    }
}
