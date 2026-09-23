package cn.seehoo.spg.bizcom.gps.autoconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.seehoo.spg.bizcom.gps.client.GpsInstallBillClient;
import cn.seehoo.spg.bizcom.gps.client.GpsUninstallBillClient;
import cn.seehoo.spg.bizcom.gps.client.ZRGpsInstallBillClient;
import cn.seehoo.spg.bizcom.gps.client.ZRGpsUninstallBillClient;
import cn.seehoo.spg.bizcom.gps.config.ZRGpsConfiguration;

@Configuration
@ConditionalOnProperty(name= {"common.zrgps.enabled"}, havingValue = "true", matchIfMissing = false)
public class ZhongRuiGpsAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZhongRuiGpsAutoConfiguration.class);
	
	public ZhongRuiGpsAutoConfiguration() {
		LOGGER.info("### ZhongRuiGpsAutoConfiguration init success ###");
	}

	@Bean
	public GpsInstallBillClient configZRGpsInstallBillClient() {
		LOGGER.info("### configZRGpsInstallBillClient init  success ###");
		return new ZRGpsInstallBillClient();
	}
	
	@Bean
	public GpsUninstallBillClient configGpsUninstallBillClient() {
		LOGGER.info("### configGpsUninstallBillClient init  success ###");
		return new ZRGpsUninstallBillClient();
	}
	
	@Bean
	public ZRGpsConfiguration configZRGpsConfiguration() {
		LOGGER.info("### configZRGpsConfiguration init  success ###");
		return new ZRGpsConfiguration();
	}
	
}