package cn.seehoo.spg.bizcom.intel.autoconfig;

import cn.seehoo.spg.bizcom.intel.client.QwIntelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name= {"common.qwintel.enabled"}, havingValue = "true", matchIfMissing = false)
public class QWIntelAutoConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(QWIntelAutoConfiguration.class);

	@Bean
	@ConditionalOnMissingBean
	public QwIntelClient intelClient() {
		LOGGER.info("### IntelClient INIT SUCCESS ###");
		return new QwIntelClient();
	}
}