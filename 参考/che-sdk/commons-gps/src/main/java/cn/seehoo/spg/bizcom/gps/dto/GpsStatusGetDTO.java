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
public class GpsStatusGetDTO {
    /** 申请编号 */
	@NotNull(message = OrderBillException.IMEI_NULL)
	@NotEmpty(message = OrderBillException.IMEI_NULL)
    private String Imei;

	public String getImei() {
		return Imei;
	}

	public void setImei(String imei) {
		Imei = imei;
	}

	@Override
	public String toString() {
		return "GpsStatusGetDTO [Imei=" + Imei + "]";
	}

	//参数校验
	public void checkParams() throws BusinessException {
		ExceptionUtil.check(this);
	}
}