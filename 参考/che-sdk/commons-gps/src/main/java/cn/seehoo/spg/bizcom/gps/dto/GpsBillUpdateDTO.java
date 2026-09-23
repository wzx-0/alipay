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
public class GpsBillUpdateDTO {
    /** 申请编号 */
	@NotNull(message = OrderBillException.APP_CODE_NULL)
	@NotEmpty(message = OrderBillException.APP_CODE_NULL)
    private String AppCode;
    /** 地区编码 */
	@NotNull(message = OrderBillException.DISTRICT_CODE_NULL)
	@NotEmpty(message = OrderBillException.DISTRICT_CODE_NULL)
    private String DistrictCode;
    /** 安装地址 */
	@NotNull(message = OrderBillException.INSTALL_ADD_NULL)
	@NotEmpty(message = OrderBillException.INSTALL_TIME_NULL)
    private String InstallAdd;
    /** 安装时间 */
	@NotNull(message = OrderBillException.INSTALL_TIME_NULL)
	@NotEmpty(message = OrderBillException.INSTALL_TIME_NULL)
    private String InstallTime;
    /** 安装店面 */
	@NotNull(message = OrderBillException.SHOP_NAME_NULL)
	@NotEmpty(message = OrderBillException.SHOP_NAME_NULL)
    private String ShopName;
    /** 店面联系人 */
	@NotNull(message = OrderBillException.LINK_MAN_NULL)
	@NotEmpty(message = OrderBillException.LINK_MAN_NULL)
    private String LinkMan;
    /** 店面联系电话 */
	@NotNull(message = OrderBillException.LINK_PHONE_NULL)
	@NotEmpty(message = OrderBillException.LINK_PHONE_NULL)
    private String LinkPhone;
    
	public String getAppCode() {
		return AppCode;
	}
	public void setAppCode(String appCode) {
		AppCode = appCode;
	}
	public String getDistrictCode() {
		return DistrictCode;
	}
	public void setDistrictCode(String districtCode) {
		DistrictCode = districtCode;
	}
	public String getInstallAdd() {
		return InstallAdd;
	}
	public void setInstallAdd(String installAdd) {
		InstallAdd = installAdd;
	}
	public String getInstallTime() {
		return InstallTime;
	}
	public void setInstallTime(String installTime) {
		InstallTime = installTime;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getLinkMan() {
		return LinkMan;
	}
	public void setLinkMan(String linkMan) {
		LinkMan = linkMan;
	}
	public String getLinkPhone() {
		return LinkPhone;
	}
	public void setLinkPhone(String linkPhone) {
		LinkPhone = linkPhone;
	}

	//参数校验
	public void checkParams() throws BusinessException {
		ExceptionUtil.check(this);
	}
    
}
