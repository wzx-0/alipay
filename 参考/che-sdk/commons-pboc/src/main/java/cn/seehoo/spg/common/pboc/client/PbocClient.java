package cn.seehoo.spg.common.pboc.client;

import cn.seehoo.spg.common.pboc.model.CreditReportReq;
import cn.seehoo.spg.common.pboc.model.CreditReportRes;
import cn.seehoo.spg.commons.core.exception.BusinessException;

/**
 * 人行客户端
 */
public interface PbocClient {

	/**
	 * 征信报告查询
	 * @param req
	 * @return
	 */
	public CreditReportRes queryCreditReport(CreditReportReq req) throws BusinessException;

}