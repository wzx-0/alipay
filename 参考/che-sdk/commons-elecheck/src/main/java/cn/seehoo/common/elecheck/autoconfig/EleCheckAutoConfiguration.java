package cn.seehoo.common.elecheck.autoconfig;

import cn.seehoo.common.elecheck.client.BankCardFourCheckClient;
import cn.seehoo.common.elecheck.client.TelThreeCheckClient;
import cn.seehoo.common.elecheck.config.EleCheckConfig;
import cn.seehoo.spg.base.client.RequestLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sunyf
 * @date 2025/9/25 上午11:43
 * @since 1.0
 */
@Configuration
@Import(EleCheckConfig.class)
@ConditionalOnProperty(
        name = {"common.elecheck.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class EleCheckAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(EleCheckAutoConfiguration.class);

    public EleCheckAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public BankCardFourCheckClient bankCardFourCheckClient(EleCheckConfig eleCheckConfig) {
        LOGGER.info("### bankCardFourCheckClient INIT SUCCESS ###");
        return new BankCardFourCheckClient(eleCheckConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelThreeCheckClient telThreeCheckClient(EleCheckConfig eleCheckConfig, RequestLogClient requestLogClient) {
        LOGGER.info("### telThreeCheckClient INIT SUCCESS ###");
        return new TelThreeCheckClient(eleCheckConfig, requestLogClient);
    }
}
