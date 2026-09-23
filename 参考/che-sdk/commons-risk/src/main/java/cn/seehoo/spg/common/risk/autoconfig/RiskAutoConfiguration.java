package cn.seehoo.spg.common.risk.autoconfig;

import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.common.risk.client.RiskClient;
import cn.seehoo.spg.common.risk.client.DeaultRiskClient;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.seehoo.spg.common.risk.config.RiskConfiguration;

/**
 * 自动注入配置
 */
@Configuration
@ConditionalOnProperty(name= {"common.risk.enabled"}, havingValue = "true", matchIfMissing = false)
public class RiskAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(RiskAutoConfiguration.class);

	public RiskAutoConfiguration() {
		LOGGER.info("### RiskAutoConfiguration init success ###");
	}

	/**
	 * 注入 RiskClient
	 * @return
	 */
	@Bean
	public RiskClient configRiskClient(RiskConfiguration config, RedisOperateService redisOperateService, RequestLogClient requestLogClient) {
		LOGGER.info("### configRiskClient init  success ###");
		return new DeaultRiskClient(config, redisOperateService, requestLogClient);
	}

	/**
	 * 注入 RiskConfiguration
	 * @return
	 */
	@Bean
	public RiskConfiguration configRiskConfiguration() {
		LOGGER.info("### configRiskConfiguration init  success ###");
		return new RiskConfiguration();
	}

}