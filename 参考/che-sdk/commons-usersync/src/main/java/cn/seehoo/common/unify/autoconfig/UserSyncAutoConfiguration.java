package cn.seehoo.common.unify.autoconfig;

import cn.seehoo.common.unify.client.UserSyncClient;
import cn.seehoo.common.unify.config.UserSyncConfig;
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
@Import(UserSyncConfig.class)
@ConditionalOnProperty(
        name = {"common.usersync.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class UserSyncAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncAutoConfiguration.class);

    public UserSyncAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public UserSyncClient configUserSyncClient() {
        LOGGER.info("### unify-user INIT SUCCESS ###");
        return new UserSyncClient();
    }
}
