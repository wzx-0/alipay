package cn.seehoo.spg.common.aml.autoconfig;

import cn.seehoo.spg.common.aml.client.AmlClient;
import cn.seehoo.spg.common.aml.config.HxAmlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author caofei
 * @desc 反洗钱自动配置
 * @time 2025/9/25 15:03。
 */
@RefreshScope
@Configuration
@ConditionalOnProperty(name= {"common.aml.enabled"}, havingValue = "true", matchIfMissing = false)
public class AmlAutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmlAutoConfig.class);
    
    @Bean
    public AmlClient amlClient() {
        LOGGER.info("### AmlClient INIT SUCCESS ###");
        return new AmlClient();
    }

    @Bean
    @ConfigurationProperties(prefix = "common.aml")
    public HxAmlConfig hxAmlConfig() {
        LOGGER.info("### HxAmlConfig INIT SUCCESS ###");
        return new HxAmlConfig();
    }
}
