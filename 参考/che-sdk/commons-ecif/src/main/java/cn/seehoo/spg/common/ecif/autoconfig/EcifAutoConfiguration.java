package cn.seehoo.spg.common.ecif.autoconfig;

import cn.seehoo.spg.common.ecif.controller.EcifController;
import cn.seehoo.spg.common.ecif.client.EcifClient;
import cn.seehoo.spg.common.ecif.client.EcifDeaultClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import  cn.seehoo.spg.common.ecif.config.EcifConfiguration;

/**
 * 自动注入配置
 */
@Configuration
@ConditionalOnProperty(name= {"common.ecif.enabled"}, havingValue = "true", matchIfMissing = false)
public class EcifAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(EcifAutoConfiguration.class);

	public EcifAutoConfiguration() {
		LOGGER.info("### EcifAutoConfiguration init success ###");
	}

	/**
	 * 注入 EcifClient
	 * @return
	 */
	@Bean
	public EcifClient configEcifClient() {
		LOGGER.info("### configEcifClient init  success ###");
		return new EcifDeaultClient();
	}

	/**
	 * 注入 EcifConfiguration
	 * @return
	 */
	@Bean
	public EcifConfiguration configEcifConfiguration() {
		LOGGER.info("### configEcifConfiguration init  success ###");
		return new EcifConfiguration();
	}

	/**
	 * 注入 EcifController
	 * @return
	 */
	@Bean
	public EcifController configEcifController() {
		LOGGER.info("### configEcifController init  success ###");
		return new EcifController();
	}

}