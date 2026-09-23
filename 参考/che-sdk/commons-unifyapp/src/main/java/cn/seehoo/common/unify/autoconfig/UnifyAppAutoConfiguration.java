package cn.seehoo.common.unify.autoconfig;

import cn.seehoo.common.unify.client.UnifyAppClient;
import cn.seehoo.common.unify.config.UnifyAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sunyf
 * @date 2025/9/25 上午11:43
 * @since 1.0
 */
@Configuration
@Import(UnifyAppConfig.class)
@ConditionalOnProperty(
        name = {"common.unifyapp.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class UnifyAppAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnifyAppAutoConfiguration.class);

    public UnifyAppAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public UnifyAppClient unifyAppClient() {
        LOGGER.info("### unify-app INIT SUCCESS ###");
        return new UnifyAppClient();
    }
}
