package cn.seehoo.spg.bizcom.ocr.autoconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.seehoo.spg.bizcom.ocr.client.HeheOcrClient;
import cn.seehoo.spg.bizcom.ocr.config.HeheOcrConfiguration;

@Configuration
@ConditionalOnProperty(name= {"common.heheocr.enabled"}, havingValue = "true", matchIfMissing = false)
public class HeheOCRAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeheOCRAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public HeheOcrClient configHeheOcrClient() {
		LOGGER.info("### HeheOcrClient INIT SUCCESS ###");
		return new HeheOcrClient();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties(prefix = "common.heheocr")
	public HeheOcrConfiguration configHeheOcrConfiguration() {
		LOGGER.info("### HeheOcrConfiguration INIT SUCCESS ###");
		return new HeheOcrConfiguration();
	}
}