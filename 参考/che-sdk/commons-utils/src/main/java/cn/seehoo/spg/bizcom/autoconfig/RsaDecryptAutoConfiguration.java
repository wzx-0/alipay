package cn.seehoo.spg.bizcom.autoconfig;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.support.StandardServletEnvironment;

import com.lyzdfintech.loongeasy.cloud.encryption.manager.KeystoreManager;
import com.lyzdfintech.loongeasy.cloud.encryption.manager.LoongeasyCloudKeystore;

import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.bizcom.config.RsaConfig;

// aes解密初始化
@Configuration
public class RsaDecryptAutoConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
	private static final Logger LOGGER = LoggerFactory.getLogger(RsaDecryptAutoConfiguration.class);
	private static final String VALUE_PREFIX_ENX = "LEC:";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		Environment env = applicationContext.getEnvironment();
		String envName = env.getClass().getName();
		if (!envName.equals("org.springframework.boot.ApplicationServletEnvironment")) {
			return;
		}
		StandardServletEnvironment senv = (StandardServletEnvironment)env;
		MutablePropertySources mps = senv.getPropertySources();
		// 读取RSA密钥
		String keystorePass = senv.getProperty("app.rsa.keypass");
		if (StrUtil.isEmpty(keystorePass)) {
			LOGGER.warn(">>>>>>[配置解密]，没有读取到RSA KEY, 不进行解密");
			return;
		}
		// RSA解密
		LoongeasyCloudKeystore km =  KeystoreManager.getLecKeystore(keystorePass);
		MapPropertySource etcmps = decrypt(mps, km);
	    mps.addFirst(etcmps);
	}
	
	// RSA解密
	private MapPropertySource decrypt(MutablePropertySources mps, LoongeasyCloudKeystore km) {
		Map<String, Object> etc = new HashMap<>();
		for (PropertySource<?> ps : mps) {
			if (!(ps instanceof EnumerablePropertySource<?>)) {
				continue;
			}
			EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>)ps;
			String[] names = eps.getPropertyNames();
			for (String name : names) {
				// 读取配置
				Object value = eps.getProperty(name);
				LOGGER.debug(">>>>>>[配置解密], 读取配置文件配置，NAME={}, VALUE={}", name, value);
				// AES解密
				if (value.getClass() == String.class 
				&& ((String)value).indexOf(VALUE_PREFIX_ENX) != -1) {
					String tmp = ((String)value).replaceAll(VALUE_PREFIX_ENX, "");
					tmp = km.decrypt(tmp);
					LOGGER.debug(">>>>>>[配置解密], 读取配置文件配置，NAME={}, PLAIN={}", name, tmp);
					etc.put(name, tmp);
				}
			}
		}
		MapPropertySource etcmps =  new MapPropertySource("etc", etc);
		return etcmps;
	}

	@Override
	public int getOrder() {
		return 100;
	}
	
	@Bean
	public RsaConfig configRsaConfig() {
		return new RsaConfig();
	}
}
