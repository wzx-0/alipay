package cn.seehoo.spg.bizcom.gd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common.gd")
public class GdConfiguration {
	/** url */
	private String host;
	/** mock开关 */
	private boolean mock;
	/** 高德key */
	private String key;
	/** 代理 */
	private String proxy;
	/** 服务码 */
	private String svcNo = "PU1200GD";
	/** 场景码 */
	private String scnNo;
	/**  */
	private String transCode;
	/**  */
	private String reqSysId="04024";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isMock() {
		return mock;
	}

	public void setMock(boolean mock) {
		this.mock = mock;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean checkMock() {
		return mock;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getSvcNo() {
		return svcNo;
	}

	public void setSvcNo(String svcNo) {
		this.svcNo = svcNo;
	}

	public String getScnNo() {
		return scnNo;
	}

	public void setScnNo(String scnNo) {
		this.scnNo = scnNo;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getReqSysId() {
		return reqSysId;
	}

	public void setReqSysId(String reqSysId) {
		this.reqSysId = reqSysId;
	}
}