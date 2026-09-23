package cn.seehoo.spg.common.bhrc.autoconfig;

import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.common.bhrc.client.HxBhrcClient;
import cn.seehoo.spg.common.bhrc.config.HxBhrcConfig;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caofei
 * @desc 华夏百行征信配置
 * @time 2025/9/24 17:10。
 */
@RefreshScope
@Configuration
@ConditionalOnProperty(name= {"common.baihang.enabled"}, havingValue = "true", matchIfMissing = false)
public class HxBhrcAutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(HxBhrcAutoConfig.class);
    @Bean
    public HxBhrcClient bhrcClient(HxBhrcConfig hxBhrcConfig, RedisOperateService redisOperateService, RequestLogClient requestLogClient) {
        LOGGER.info("### HxBhrcClient INIT SUCCESS ###");
        return new HxBhrcClient(hxBhrcConfig,redisOperateService,requestLogClient);
    }

    @Bean
    @ConfigurationProperties(prefix = "common.baihang")
    public HxBhrcConfig hxBhrcConfig() {
        LOGGER.info("### HxBhrcConfig INIT SUCCESS ###");
        return new HxBhrcConfig();
    }
}
