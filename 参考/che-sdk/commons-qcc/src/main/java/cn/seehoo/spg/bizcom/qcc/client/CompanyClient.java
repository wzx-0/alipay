package cn.seehoo.spg.bizcom.qcc.client;

import java.util.List;

import cn.seehoo.spg.base.dto.DisciplinaryCheckHisSaveDto;
import cn.seehoo.spg.base.dto.ExceptionCheckHisSaveDto;
import cn.seehoo.spg.base.dto.SeriousIllegalCheckHisSaveDto;
import cn.seehoo.spg.base.dto.ShixinCheckHisSaveDto;
import cn.seehoo.spg.bizcom.qcc.model.*;
import cn.seehoo.spg.commons.core.exception.BusinessException;

/**
 * @author chenjun
 * 
 * 企查查工商注册信息接口
 */
public interface CompanyClient {
	
	/** 查询公司注册信息 */
	public CompayInfo getCompanyInfo(String keyword) throws BusinessException;
	
	/** 模糊查询公司信息 */
	public List<CompayInfo> searchCompanyInfos(String keyword) throws BusinessException;

	/**
	 * 	企业综合风险排查
	 * @param idNo
	 * @return
	 * @throws BusinessException
	 */
	CompayInfo enterpriseCheck(String idNo) throws BusinessException;
	/** 经营异常核查 */
    List<ExceptionCheckInfo> exceptionCheck(String orderCode, String searchKey, ExceptionCheckHisSaveDto dto);
	/** 失信核查 */
	List<ShixinCheckInfo> shixinCheck(String orderCode, String searchKey, ShixinCheckHisSaveDto dto);
	/** 严重违法核查 */
	List<SeriousIllegalCheckInfo> seriousIllegalCheck(String orderCode, String searchKey, SeriousIllegalCheckHisSaveDto dto);
	/** 惩戒名单核查 */
	List<DisciplinaryCheckInfo> disciplinaryCheck(String orderCode, String searchKey, DisciplinaryCheckHisSaveDto dto);
}