package cn.seehoo.spg.common.signpl.autoconfig;

import cn.seehoo.spg.common.signpl.client.SingPlClient;
import cn.seehoo.spg.common.signpl.config.SignPlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caofei
 * @desc 签约平台自动配置
 * @time 2025/9/25 17:03。
 */
@RefreshScope
@Configuration
@ConditionalOnProperty(name= {"common.signpl.enabled"}, havingValue = "true", matchIfMissing = false)
public class SignPlAutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignPlAutoConfig.class);
    
    @Bean
    public SingPlClient singPlClient() {
        LOGGER.info("### SingPlClient INIT SUCCESS ###");
        return new SingPlClient();
    }

    @Bean
    @ConfigurationProperties(prefix = "common.signpl")
    public SignPlConfig signPlConfig() {
        LOGGER.info("### SignPlConfig INIT SUCCESS ###");
        return new SignPlConfig();
    }
}
