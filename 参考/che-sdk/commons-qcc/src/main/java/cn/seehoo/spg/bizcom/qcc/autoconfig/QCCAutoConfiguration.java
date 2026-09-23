package cn.seehoo.spg.bizcom.qcc.autoconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.seehoo.spg.bizcom.qcc.client.CompanyClient;
import cn.seehoo.spg.bizcom.qcc.client.QccCompanyClient;
import cn.seehoo.spg.bizcom.qcc.config.QccConfiguration;
import cn.seehoo.spg.bizcom.qcc.controller.CompanyInfoController;

@Configuration
@ConditionalOnProperty(name= {"common.qcc.enabled"}, havingValue = "true", matchIfMissing = false)
public class QCCAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(QCCAutoConfiguration.class);
	
	public QCCAutoConfiguration() {
		LOGGER.info("### QCCAutoConfiguration init success ###");
	}

	@Bean
	public CompanyClient configCompanyClient() {
		LOGGER.info("### configZRGpsInstallBillClient init  success ###");
		return new QccCompanyClient();
	}
	
	@Bean
	public QccConfiguration configQccConfiguration() {
		LOGGER.info("### configQccConfiguration init  success ###");
		return new QccConfiguration();
	}
	
	@Bean
	public CompanyInfoController configCompanyController() {
		LOGGER.info("### configCompanyController init  success ###");
		return new CompanyInfoController();
	}
	
}