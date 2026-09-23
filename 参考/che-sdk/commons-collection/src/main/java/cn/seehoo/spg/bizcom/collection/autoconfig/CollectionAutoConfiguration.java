package cn.seehoo.spg.bizcom.collection.autoconfig;

import cn.seehoo.spg.bizcom.collection.client.PartnerSynchronizationClient;
import cn.seehoo.spg.bizcom.collection.config.CollectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangxx
 * @date 2026/1/8 9:14
 */
@Configuration
@ConditionalOnProperty(name= {"common.collection.enabled"}, havingValue = "true", matchIfMissing = false)
public class CollectionAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionAutoConfiguration.class);

    public CollectionAutoConfiguration() {
        LOGGER.info("### CollectionAutoConfiguration init success ###");
    }

    @Bean
    public PartnerSynchronizationClient configZRGpsInstallBillClient() {
        LOGGER.info("### PartnerSynchronizationClient init  success ###");
        return new PartnerSynchronizationClient();
    }

    @Bean
    public CollectionConfiguration configCollectionConfiguration() {
        LOGGER.info("### CollectionConfiguration init  success ###");
        return new CollectionConfiguration();
    }

}
