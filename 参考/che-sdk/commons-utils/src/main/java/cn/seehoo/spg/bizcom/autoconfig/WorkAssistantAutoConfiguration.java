package cn.seehoo.spg.bizcom.autoconfig;

import cn.seehoo.spg.bizcom.config.WorkAssistantConfig;
import cn.seehoo.spg.bizcom.utils.WorkFlowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name= {"common.wac.enabled"}, havingValue = "true", matchIfMissing = false)
public class WorkAssistantAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkAssistantAutoConfiguration.class);

    public WorkAssistantAutoConfiguration() {
        LOGGER.info("### WorkAssistantAutoConfiguration init success ###");
    }

    @Bean
    public WorkAssistantConfig configWorkAssistantConfig() {
        LOGGER.info("### configWorkAssistantConfig init  success ###");
        return new WorkAssistantConfig();
    }

    @Bean
    public WorkFlowUtil configWorkFlowUtil() {
        LOGGER.info("### configWorkFlowUtil init  success ###");
        return new WorkFlowUtil();
    }
}
