package cn.seehoo.spg.common.opl.autoconfig;

import cn.seehoo.spg.common.opl.client.OplClient;
import cn.seehoo.spg.common.opl.config.OplConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name= {"common.opl.enabled"}, havingValue = "true", matchIfMissing = false)
public class OplAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(OplAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public OplClient configOplClient() {
		LOGGER.info("### OplClient INIT SUCCESS ###");
		return new OplClient();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConfigurationProperties(prefix = "common.opl")
	public OplConfiguration configOplConfiguration() {
		LOGGER.info("### OplConfiguration INIT SUCCESS ###");
		return new OplConfiguration();
	}
}