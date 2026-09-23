package cn.seehoo.spg.common.pboc.autoconfig;

import cn.seehoo.spg.common.pboc.client.PbocClient;
import cn.seehoo.spg.common.pboc.client.PbocDeaultClient;
import cn.seehoo.spg.common.pboc.config.PbocConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动注入配置
 */
@Configuration
@ConditionalOnProperty(name= {"common.pboc.enabled"}, havingValue = "true", matchIfMissing = false)
public class PbocAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(PbocAutoConfiguration.class);

	public PbocAutoConfiguration() {
		LOGGER.info("### PbocAutoConfiguration init success ###");
	}

	/**
	 * 注入 PbocClient
	 * @return
	 */
	@Bean
	public PbocClient configPbocClient() {
		LOGGER.info("### configPbocClient init  success ###");
		return new PbocDeaultClient();
	}

	/**
	 * 注入 PbocConfiguration
	 * @return
	 */
	@Bean
	public PbocConfiguration configPbocConfiguration() {
		LOGGER.info("### configPbocConfiguration init  success ###");
		return new PbocConfiguration();
	}

}