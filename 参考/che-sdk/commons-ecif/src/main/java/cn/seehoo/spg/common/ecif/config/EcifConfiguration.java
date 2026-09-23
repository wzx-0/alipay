package cn.seehoo.spg.common.ecif.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ecif配置类
 */
@ConfigurationProperties(prefix = "common.ecif")
public class EcifConfiguration {

	/** mock开关 */
	private boolean mock;

	/** 代理 */
	private String proxy;

	/** 各业务系统编号（由平台分配） */
	private String channel;

	/** 渠道密码 */
	private String channelSecret;

	/** 当前登录账号 */
	private String requestUserName;

	/** 基础访问路径 */
	private String baseUrl;

	/**
	 * 客商SM4密钥
	 */
	private String sm4Key;

	public boolean isMock() {
		return mock;
	}

	public void setMock(boolean mock) {
		this.mock = mock;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannelSecret() {
		return channelSecret;
	}

	public void setChannelSecret(String channelSecret) {
		this.channelSecret = channelSecret;
	}

	public String getRequestUserName() {
		return requestUserName;
	}

	public void setRequestUserName(String requestUserName) {
		this.requestUserName = requestUserName;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getSm4Key() {
		return sm4Key;
	}

	public void setSm4Key(String sm4Key) {
		this.sm4Key = sm4Key;
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

}