package cn.seehoo.spg.bizcom.gps.client;

import java.util.List;

import cn.seehoo.spg.bizcom.gps.dto.GpsBillCreateDTO;
import cn.seehoo.spg.bizcom.gps.dto.GpsBillGetDTO;
import cn.seehoo.spg.bizcom.gps.dto.GpsBillUpdateDTO;
import cn.seehoo.spg.bizcom.gps.model.GpsInstallBill;
import cn.seehoo.spg.bizcom.gps.model.GpsInstallBillFile;
import cn.seehoo.spg.bizcom.gps.model.GpsLocation;
import cn.seehoo.spg.commons.core.exception.BusinessException;

/**
 * @author chenjun
 * 
 * 状态机接口
 */
public interface GpsInstallBillClient {
	
	/** 更新gps安装工单 */
	public boolean updateGpsInstallBill(GpsBillUpdateDTO gbudto) throws BusinessException;
	
	/** 创建gps工单 */
	public boolean createGpsInstallPlan(GpsBillCreateDTO gbcdto) throws BusinessException;
	
	/** 查询gps工单 */
	public GpsInstallBill getGpsInstallPlan(GpsBillGetDTO gbgdto) throws BusinessException;
	
	/** 取消gps工单 */
	public boolean cancelGpsInstallPlan(String appCode) throws BusinessException;
	
	/** 查询gps工工单附件 */
	public List<GpsInstallBillFile> getGpsInstallPlanFiles(String appCode) throws BusinessException;
	
	/** 查询gps定位信息 */
	public List<GpsLocation> getGpsLocations(String appCode) throws BusinessException;
	
}