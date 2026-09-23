package cn.seehoo.spg.common.bhrc.autoconfig;

import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.common.bhrc.client.BhBankCardClient;
import cn.seehoo.spg.common.bhrc.client.BhPhoneClient;
import cn.seehoo.spg.common.bhrc.config.HxBhrcPhoneConfig;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liusijia
 * @desc 华夏百行手机三要素校验配置
 */
@RefreshScope
@Configuration
@ConditionalOnProperty(name= {"common.baihangphone.enabled"}, havingValue = "true", matchIfMissing = false)
public class HxBhrcBankCardAutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(HxBhrcBankCardAutoConfig.class);
    @Bean
    public BhBankCardClient bhBankCardClient(HxBhrcPhoneConfig config, RedisOperateService redisOperateService, RequestLogClient requestLogClient) {
        LOGGER.info("### HxBhrcClient INIT SUCCESS ###");
        return new BhBankCardClient(config, redisOperateService, requestLogClient);
    }
}
