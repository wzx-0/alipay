package cn.seehoo.spg.bizcom.order.autoconfig;

import cn.seehoo.spg.bizcom.order.client.OrderExistClient;
import cn.seehoo.spg.bizcom.order.client.OrderExistDefaultClient;
import cn.seehoo.spg.bizcom.order.config.OrderConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name= {"common.order.enabled"}, havingValue = "true", matchIfMissing = false)
public class OrderAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public OrderExistClient orderClient() {
        LOGGER.info("### OrderExistClient INIT SUCCESS ###");
        return new OrderExistDefaultClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public OrderConfiguration qwIntelConfiguration() {
        LOGGER.info("### OrderConfiguration INIT SUCCESS ###");
        return new OrderConfiguration();
    }
}
