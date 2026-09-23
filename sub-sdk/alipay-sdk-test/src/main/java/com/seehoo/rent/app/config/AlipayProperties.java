package com.seehoo.rent.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝业务侧配置（application.yml alipay.rent 前缀）
 * <p>SDK配置绑定与客户端注册已由 rent-alipay-sdk 自动装配接管（alipay.rent.enabled=true），
 * 本类只承载业务侧属性</p>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay.rent")
public class AlipayProperties {

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
}
