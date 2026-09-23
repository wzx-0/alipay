package cn.seehoo.spg.common.pboc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * PBOC配置类
 */
@ConfigurationProperties(prefix = "common.pboc")
public class PbocConfiguration {

	/** mock开关 */
	private boolean mock;

	/** 接口加密开关：false-明文 true-密文 */
	private boolean signFlag;

	/** 征信前置机IP端口 */
	private String creditIpHost;

	/** 代理 */
	private String proxy;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 征信报告密钥
	 */
	private String creditKey;

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

	public String getCreditIpHost() {
		return creditIpHost;
	}

	public void setCreditIpHost(String creditIpHost) {
		this.creditIpHost = creditIpHost;
	}

	public String getCreditKey() {
		return creditKey;
	}

	public void setCreditKey(String creditKey) {
		this.creditKey = creditKey;
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

	public boolean isSignFlag() {
		return signFlag;
	}

	public void setSignFlag(boolean signFlag) {
		this.signFlag = signFlag;
	}

	public boolean checkMock() {
		return this.mock;
	}

	public boolean checkSign() {
		return this.signFlag;
	}

}