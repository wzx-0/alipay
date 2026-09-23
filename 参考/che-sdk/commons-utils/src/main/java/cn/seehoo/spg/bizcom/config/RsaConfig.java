package cn.seehoo.spg.bizcom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "app.rsa")
public class RsaConfig {
    private String keypass;

	public String getKeypass() {
		return keypass;
	}

	public void setKeypass(String keypass) {
		this.keypass = keypass;
	}
}