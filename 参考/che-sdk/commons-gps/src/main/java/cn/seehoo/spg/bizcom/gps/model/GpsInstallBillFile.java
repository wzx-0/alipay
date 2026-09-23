package cn.seehoo.spg.bizcom.gps.model;

// 工单附件
public class GpsInstallBillFile {
	 /** 申请编号 */
    private String appcode;
    /** 文件名称 */
    private String filename;
    /** 图片协议地址 */
    private String url;
    /** 图片描述 */
    private String remark;
    /** 图片类型 */
    private String imgtype;
    
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getImgtype() {
		return imgtype;
	}
	public void setImgtype(String imgtype) {
		this.imgtype = imgtype;
	}
	
	@Override
	public String toString() {
		return "GpsInstallBillFile [appcode=" + appcode + ", filename=" + filename + ", url=" + url + ", remark="
				+ remark + ", imgtype=" + imgtype + "]";
	}
}
