package cn.seehoo.spg.common.mortgage.autoconfig;

import cn.seehoo.spg.common.mortgage.client.MortgageClient;
import cn.seehoo.spg.common.mortgage.client.MortgageDefaultClient;
import cn.seehoo.spg.common.mortgage.client.ResponsibleMortgageDefaultClient;
import cn.seehoo.spg.common.mortgage.config.MortgageConfig;
import cn.seehoo.spg.common.mortgage.config.ResponsibleMortgageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 抵押中台自动配置
 */
@Configuration
@ConditionalOnProperty(name= {"common.mortgage.enabled"}, havingValue = "true", matchIfMissing = false)
public class MortgageAutoConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MortgageAutoConfig.class);

    MortgageAutoConfig(){
        LOGGER.info("### MortgageAutoConfig init success ###");
    }

    /**
     * 注入 MortgageClient
     * @return
     */
    @Bean
    public MortgageClient configMortgageClient() {
        LOGGER.info("### configMortgageClient init SUCCESS ###");
        return new MortgageDefaultClient();
    }

    /**
     * 注入 MortgageConfig
     * @return
     */
    @Bean
    public MortgageConfig configMortgageConfig() {
        LOGGER.info("### configMortgageConfig init SUCCESS ###");
        return new MortgageConfig();
    }

    /**
     * 注入 ResponsibleMortgageDefaultClient
     * @return
     */
    @Bean
    public ResponsibleMortgageDefaultClient ResponsibleMortgageDefaultClient() {
        LOGGER.info("### ResponsibleMortgageDefaultClient init SUCCESS ###");
        return new ResponsibleMortgageDefaultClient();
    }

    /**
     * 注入 ResponsibleMortgageConfig
     * @return
     */
    @Bean
    public ResponsibleMortgageConfig ResponsibleMortgageConfig() {
        LOGGER.info("### ResponsibleMortgageConfig init SUCCESS ###");
        return new ResponsibleMortgageConfig();
    }
}
