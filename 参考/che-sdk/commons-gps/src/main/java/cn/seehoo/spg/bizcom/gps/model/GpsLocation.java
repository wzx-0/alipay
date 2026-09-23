package cn.seehoo.spg.bizcom.gps.model;

// GPS定位结果
public class GpsLocation {
	 /** 申请编号 */
    private String appcode;
    /** 定位时间 */
    private String locationTime;
    /** 定位地址 */
    private String locationAdd;
    /** 设备号 */
    private String imei;
    /** sim卡号 */
    private String sim;
    /** 设备运行状态，-1未启用、1离线、2静止、3行驶中 */
    private String sbcstatus;
    /** 纬度 */
    private String lat;
    /** 经度 */
    private String lng;
	/** 0无线 1有线 */
	private Integer flag;
    
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getLocationTime() {
		return locationTime;
	}
	public void setLocationTime(String locationTime) {
		this.locationTime = locationTime;
	}
	public String getLocationAdd() {
		return locationAdd;
	}
	public void setLocationAdd(String locationAdd) {
		this.locationAdd = locationAdd;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public String getSbcstatus() {
		return sbcstatus;
	}
	public void setSbcstatus(String sbcstatus) {
		this.sbcstatus = sbcstatus;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	// 判断GPS是否激活
	public boolean checkactive() {
		return !(this.sbcstatus.equals("0")||this.sbcstatus.equals("1"));
	}

	@Override
	public String toString() {
		return "GpsLocation [appcode=" + appcode + ", locationTime=" + locationTime + ", locationAdd=" + locationAdd
				+ ", imei=" + imei + ", sim=" + sim + ", sbcstatus=" + sbcstatus + ", lat=" + lat + ", lng=" + lng
				+ ", flag=" + flag
				+ "]";
	}

}