package cn.seehoo.spg.bizcom.ocr.config;

// 百度OCR配置
public class HeheOcrConfiguration {
	/** mock */
	private boolean mock;
	/** x_ti_app_id */
	private String x_ti_app_id;
	/** x_ti_secret_code */
	private String x_ti_secret_code;
	/** 代理 */
	private String proxy;
	/** accessToken链接 */
	private String idcardUrl;
	/** 合合OCR通用发票识别链接 */
	private String invoiceUrl;
	/** 合合OCR银行卡识别链接 */
	private String bankcardUrl;
	/** 合合OCR行驶证识别链接 */
	private String vehicleLicenseUrl;
	
	public boolean isMock() {
		return mock;
	}
	public void setMock(boolean mock) {
		this.mock = mock;
	}
	public String getX_ti_app_id() {
		return x_ti_app_id;
	}
	public void setX_ti_app_id(String x_ti_app_id) {
		this.x_ti_app_id = x_ti_app_id;
	}
	public String getX_ti_secret_code() {
		return x_ti_secret_code;
	}
	public void setX_ti_secret_code(String x_ti_secret_code) {
		this.x_ti_secret_code = x_ti_secret_code;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getIdcardUrl() {
		return idcardUrl;
	}
	public void setIdcardUrl(String idcardUrl) {
		this.idcardUrl = idcardUrl;
	}
	public String getBankcardUrl() {
		return bankcardUrl;
	}
	public void setBankcardUrl(String bankcardUrl) {
		this.bankcardUrl = bankcardUrl;
	}
	public String getVehicleLicenseUrl() {
		return vehicleLicenseUrl;
	}
	public void setVehicleLicenseUrl(String vehicleLicenseUrl) {
		this.vehicleLicenseUrl = vehicleLicenseUrl;
	}
	public String getInvoiceUrl() {
		return invoiceUrl;
	}
	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}
	
	public boolean checkMock() {
		return this.mock;
	}
}