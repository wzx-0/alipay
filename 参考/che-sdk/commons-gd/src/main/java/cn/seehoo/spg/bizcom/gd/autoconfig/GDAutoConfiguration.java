package cn.seehoo.spg.bizcom.gd.autoconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.seehoo.spg.bizcom.gd.client.GdMapClient;
import cn.seehoo.spg.bizcom.gd.client.GdCheckClient;
import cn.seehoo.spg.bizcom.gd.config.GdConfiguration;
import cn.seehoo.spg.bizcom.gd.controller.GdCheckInfoController;

@Configuration
@ConditionalOnProperty(name= {"common.gd.enabled"}, havingValue = "true", matchIfMissing = false)
public class GDAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(GDAutoConfiguration.class);

	public GDAutoConfiguration() {
		LOGGER.info("### GDAutoConfiguration init success ###");
	}

	@Bean
	public GdMapClient configGdClient() {
		LOGGER.info("### configGdClient init  success ###");
		return new GdCheckClient();
	}
	
	@Bean
	public GdConfiguration configGdConfiguration() {
		LOGGER.info("### configGdConfiguration init  success ###");
		return new GdConfiguration();
	}
	
	@Bean
	public GdCheckInfoController gdCheckInfoController() {
		LOGGER.info("### gdCheckInfoController init  success ###");
		return new GdCheckInfoController();
	}
	
}