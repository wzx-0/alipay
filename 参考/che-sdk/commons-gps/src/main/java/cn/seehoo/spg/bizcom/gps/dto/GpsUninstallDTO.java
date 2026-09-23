package cn.seehoo.spg.bizcom.gps.dto;

import cn.seehoo.spg.bizcom.gps.exception.OrderBillException;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

/**
 * @author fangcp
 * @date 2025/8/13 15:38
 * @since 1.0
 */
public class GpsUninstallDTO {
	/** 安装工单编号 */
	@NotNull(message = OrderBillException.NEW_APP_CODE_NULL)
	@NotEmpty(message = OrderBillException.NEW_APP_CODE_NULL)
	private String AppCode;
    /** 申请编号 */
	@NotNull(message = OrderBillException.NEW_APP_CODE_NULL)
	@NotEmpty(message = OrderBillException.NEW_APP_CODE_NULL)
    private String NewAppCode;
    /** 地区编码 */
	@NotNull(message = OrderBillException.DISTRICT_CODE_NULL)
	@NotEmpty(message = OrderBillException.DISTRICT_CODE_NULL)
    private String AddressCode;
    /** 安装地址 */
	@NotNull(message = OrderBillException.INSTALL_ADD_NULL)
	@NotEmpty(message = OrderBillException.INSTALL_TIME_NULL)
    private String InstallAddress;
    /** 预约时间 */
	@NotNull(message = OrderBillException.APPOINT_TIME_NULL)
	@NotEmpty(message = OrderBillException.APPOINT_TIME_NULL)
    private String AppointTime;
    /** 联系人 */
	@NotNull(message = OrderBillException.LINK_MAN_NULL)
	@NotEmpty(message = OrderBillException.LINK_MAN_NULL)
    private String ContactPerson;
    /** 联系电话 */
	@NotNull(message = OrderBillException.LINK_PHONE_NULL)
	@NotEmpty(message = OrderBillException.LINK_PHONE_NULL)
    private String ContactPhone;
    /** 拆机原因 */
	@NotNull(message = OrderBillException.REASON_NULL)
	@NotEmpty(message = OrderBillException.REASON_NULL)
    private String OrderReason;
    /** 车架号*/
	@NotNull(message = OrderBillException.VIN_NULL)
	@NotEmpty(message = OrderBillException.VIN_NULL)
    private String VinNumber;

	public String getAppCode() {
		return AppCode;
	}
	public void setAppCode(String appCode) {
		AppCode = appCode;
	}
	public String getNewAppCode() {
		return NewAppCode;
	}
	public void setNewAppCode(String newAppCode) {
		NewAppCode = newAppCode;
	}
	public String getAddressCode() {
		return AddressCode;
	}
	public void setAddressCode(String addressCode) {
		AddressCode = addressCode;
	}
	public String getInstallAddress() {
		return InstallAddress;
	}
	public void setInstallAddress(String installAddress) {
		InstallAddress = installAddress;
	}
	public String getAppointTime() {
		return AppointTime;
	}
	public void setAppointTime(String appointTime) {
		AppointTime = appointTime;
	}
	public String getContactPerson() {
		return ContactPerson;
	}
	public void setContactPerson(String contactPerson) {
		ContactPerson = contactPerson;
	}
	public String getContactPhone() {
		return ContactPhone;
	}
	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}
	public String getOrderReason() {
		return OrderReason;
	}
	public void setOrderReason(String orderReason) {
		OrderReason = orderReason;
	}
	public String getVinNumber() {
		return VinNumber;
	}
	public void setVinNumber(String vinNumber) {
		VinNumber = vinNumber;
	}

	//参数校验
	public void checkParams() throws BusinessException {
		ExceptionUtil.check(this);
	}
    
}
