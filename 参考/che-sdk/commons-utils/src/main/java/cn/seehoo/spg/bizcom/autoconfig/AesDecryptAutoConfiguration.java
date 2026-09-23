package cn.seehoo.spg.bizcom.autoconfig;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.support.StandardServletEnvironment;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

// aes解密初始化
public class AesDecryptAutoConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
	private static final Logger LOGGER = LoggerFactory.getLogger(AesDecryptAutoConfiguration.class);
	private static final String VALUE_PREFIX_ENX = "ENX-";
	private static final String LCE_KEY = "LEC.Key";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		Environment env = applicationContext.getEnvironment();
		String envName = env.getClass().getName();
		if (!envName.equals("org.springframework.boot.ApplicationServletEnvironment")) {
			return;
		}
		StandardServletEnvironment senv = (StandardServletEnvironment)env;
		MutablePropertySources mps = senv.getPropertySources();
		// 读取AES密钥
		String aesKey = getAesKey(mps);
		if (StrUtil.isEmpty(aesKey)) {
			LOGGER.warn(">>>>>>[配置解密]，没有读取到AES KEY, 不进行解密");
			return;
		}
		// AES解密
		MapPropertySource etcmps = decrypt(mps, aesKey);
	    mps.addFirst(etcmps);
		System.out.println("======<<<<<<"+env.getProperty("password"));
	}
	
	// 从配置中获取AES KEY
	private String getAesKey(MutablePropertySources mps) {
		for (PropertySource<?> ps : mps) {
			if (!(ps instanceof EnumerablePropertySource<?>)) {
				continue;
			}
			EnumerablePropertySource<?> eps = (EnumerablePropertySource<?>) ps;
			String[] names = eps.getPropertyNames();
			for (String name : names) {
				Object value =  eps.getProperty(name);
				// 匹配AES密钥
				if (value.getClass() != String.class 
				|| !LCE_KEY.equals((String)name)) {
					continue;
				}
				LOGGER.info(">>>>>>匹配到AES密钥，NAME={}, VALUE={}", name, value);
				return (String)value;
			}
		}
		return null;
	}
	
	// AES解密
	private MapPropertySource decrypt(MutablePropertySources mps, String aesKey) {
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
				LOGGER.debug(">>>>>>读取配置文件配置，NAME={}, VALUE={}", name, value);
				// AES解密
				if (value.getClass() == String.class 
				&& ((String)value).indexOf(VALUE_PREFIX_ENX) != -1) {
					String tmp = ((String)value).replaceAll(VALUE_PREFIX_ENX, "");
					tmp = SecureUtil.aes(aesKey.getBytes()).decryptStr(tmp);
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
	
//	public static void main(String[] args) {
//		SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//		String key = "f7244837c4701df4f47e549682495e5e";
//		String value = SecureUtil.aes(key.getBytes()).encryptBase64("cjh");
//		String plaun = SecureUtil.aes(key.getBytes()).decryptStr(value);
//		System.out.println(value+"--"+plaun);
//	}
}
