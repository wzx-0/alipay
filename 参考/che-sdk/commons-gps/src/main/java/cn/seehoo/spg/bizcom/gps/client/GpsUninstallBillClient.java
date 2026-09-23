package cn.seehoo.spg.bizcom.gps.client;

import cn.seehoo.spg.bizcom.gps.dto.GpsUninstallDTO;
import cn.seehoo.spg.commons.core.exception.BusinessException;

/**
 * @author chenjun
 * 
 * 拆机接口
 */
public interface GpsUninstallBillClient {
	
	/** 创建gps拆机工单 */
	public boolean createGpsInstallPlan(GpsUninstallDTO gbcdto) throws BusinessException;
	
}