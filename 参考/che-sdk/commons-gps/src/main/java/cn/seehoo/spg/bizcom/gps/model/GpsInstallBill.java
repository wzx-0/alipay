package cn.seehoo.spg.bizcom.gps.model;

public class GpsInstallBill {
	 /** 申请编号 */
    private String AppCode;
    /** 工单状态，等待派单、等待加装、加装完成 */
    private String OrderStatus;
    /** 是否删除 */
    private Boolean Deleted;
    /** 实际安装人员 */
    private String ActualInstallName;
    
	public String getAppCode() {
		return AppCode;
	}
	public void setAppCode(String appCode) {
		AppCode = appCode;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public Boolean getDeleted() {
		return Deleted;
	}
	public void setDeleted(Boolean deleted) {
		Deleted = deleted;
	}
	public String getActualInstallName() {
		return ActualInstallName;
	}
	public void setActualInstallName(String actualInstallName) {
		ActualInstallName = actualInstallName;
	}
	
	@Override
	public String toString() {
		return "GpsInstallBill [AppCode=" + AppCode + ", OrderStatus=" + OrderStatus + ", Deleted=" + Deleted
				+ ", ActualInstallName=" + ActualInstallName + "]";
	}
}
