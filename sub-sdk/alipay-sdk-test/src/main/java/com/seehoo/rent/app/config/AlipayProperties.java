package com.seehoo.rent.app.config;

import com.seehoo.rent.sdk.AlipayRentClient;
import com.seehoo.rent.sdk.AlipayRentConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝对接配置（application.yml alipay.rent 前缀），并装配SDK客户端
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay.rent")
public class AlipayProperties {

    /** 开放平台应用APPID */
    private String appId;
    /** 应用私钥（PKCS8一行Base64） */
    private String appPrivateKey;
    /** 支付宝公钥 */
    private String alipayPublicKey;
    /** 网关地址，空则用SDK默认正式网关 */
    private String gateway;
    /** 异主体收单APPID，与appId一致无需配置 */
    private String tradeAppId;
    /** 预授权冻结结果通知地址（创单时传给支付宝，须外网可达） */
    private String freezeNotifyUrl;
    /** 支付结果通知地址（支付时传给支付宝，须外网可达） */
    private String payNotifyUrl;
    /** 芝麻信用服务ID（BD提供） */
    private String zmServiceId;
    /** 芝麻信用品类ID（BD提供） */
    private String categoryId;
    /** 预授权类目，私域传DEPOSIT_CAR_LEASING_PRI，公域留空 */
    private String authCategory;
    /** 是否验支付宝响应签名，联调期可false */
    private boolean verifyResponseSign = false;

    @Bean
    public AlipayRentConfig alipayRentConfig() {
        AlipayRentConfig.AlipayRentConfigBuilder builder = AlipayRentConfig.builder()
                .appId(appId)
                .appPrivateKey(appPrivateKey)
                .alipayPublicKey(alipayPublicKey)
                .tradeAppId(tradeAppId)
                .verifyResponseSign(verifyResponseSign);
        if (gateway != null && !gateway.isEmpty()) {
            builder.gateway(gateway);
        }
        return builder.build();
    }

    @Bean
    public AlipayRentClient alipayRentClient(AlipayRentConfig alipayRentConfig) {
        return new AlipayRentClient(alipayRentConfig);
    }
}
