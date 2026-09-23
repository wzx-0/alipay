package cn.seehoo.spg.common.risk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Risk配置类
 */
@ConfigurationProperties(prefix = "common.risk")
public class RiskConfiguration {
	/** mock开关 */
	private boolean mock;

	/** 代理 */
	private String proxy;

	/** 请求地址 */
	private String host;

	/** key */
	private String key;

	/** 分配秘钥 */
	private String secret;
	private String engineAesKey;

	/** 租户ID */
	private String tenantId;

	/** 前置系统的查询员用户名 */
	private String username;

	/** 前置用户密码，SM4加密 */
	private String password;

	/** 用户ID-默认传 hxjz */
	private String userId;

	public boolean isMock() {
		return mock;
	}

	public void setMock(boolean mock) {
		this.mock = mock;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	
	public boolean checkMock() {
		return this.mock;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getEngineAesKey() {
		return engineAesKey;
	}

	public void setEngineAesKey(String engineAesKey) {
		this.engineAesKey = engineAesKey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}