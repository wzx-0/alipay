package cn.seehoo.spg.bizcom.qcc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common.qcc")
public class QccConfiguration {
	/** 车信息域名 */
	private String host;
	/** 代理 */
	private String proxy;
	/** 详情secret */
	private String detailSecretkey;
	/** 详情appKey */
	private String detailAppKey;
	/** secretkey */
	private String listSecretkey;
	/** appKey */
	private String listAppKey;
	/** mock开关 */
	private boolean mock;
	/** 服务码 */
	private String svcNo = "PU120QCC";
	/** 场景码 */
	private String scnNo;
	/**  */
	private String transCode;
	/**  */
	private String reqSysId="04024";

	public String getListSecretkey() {
		return listSecretkey;
	}
	public void setListSecretkey(String listSecretkey) {
		this.listSecretkey = listSecretkey;
	}
	public String getListAppKey() {
		return listAppKey;
	}
	public void setListAppKey(String listAppKey) {
		this.listAppKey = listAppKey;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getDetailSecretkey() {
		return detailSecretkey;
	}
	public void setDetailSecretkey(String detailSecretkey) {
		this.detailSecretkey = detailSecretkey;
	}
	public String getDetailAppKey() {
		return detailAppKey;
	}
	public void setDetailAppKey(String detailAppKey) {
		this.detailAppKey = detailAppKey;
	}
	public boolean isMock() {
		return mock;
	}
	public void setMock(boolean mock) {
		this.mock = mock;
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
	
	public boolean checkMock() {
		return mock;
	}
	
}