package cn.seehoo.spg.bizcom.gps.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common.zrgps")
public class ZRGpsConfiguration {
//	private static final int MOCK_ENABLE = 1;
	/** 车信息域名 */
	private String host;
	/** 代理 */
	private String proxy;
	/** 中瑞产品ID */
	private String productId;
	/** 中瑞产品ID */
	private String appKey;
	/** mock开关 */
	private boolean mock;
	/** 服务码 */
	private String svcNo = "PU1200ZR";
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
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
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