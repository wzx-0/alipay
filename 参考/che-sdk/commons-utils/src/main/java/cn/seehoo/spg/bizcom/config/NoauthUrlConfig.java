package cn.seehoo.spg.bizcom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "app")
public class NoauthUrlConfig {
    private List<String> noauthUrls;
    
    public List<String> getNoauthUrls() {
        return noauthUrls;
    }
    public void setNoauthUrls(List<String> noauthUrls) {
        this.noauthUrls = noauthUrls;
    }
}
