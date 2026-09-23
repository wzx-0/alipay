package cn.seehoo.spg.common.risk.client;

import cn.seehoo.spg.common.risk.model.*;
import cn.seehoo.spg.commons.core.exception.BusinessException;

/**
 * Risk客户端
 */
public interface RiskClient {

	/**
	 * 风控提交
	 * @param req
	 * @return
	 */
	public String submitRisk(RiskReq req) throws BusinessException;


	/**
	 * 查询风控个人审批报告
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public String riskReport(RiskReportReq req) throws BusinessException;

	/**
	 * 查询法海案件报告
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public String fhRiskReport(RiskReportReq req) throws BusinessException;

	/**
	 * 发起增信风控模型
	 */
	String submitCreditRisk(CreditRiskReq req);

	/**
	 * 通用提报接口（车辆系统 -> 风控决策引擎）
	 * 真正的决策结果走异步回调（依据 orderId / taskId 关联）</p>
	 * <p>bizData 声明为 Object 以保证扩展性，调用方按所选 bizScene 自由组装结构即可</p>
	 *
	 * @param req 通用提报请求
	 * @return 响应 JSON 字符串
	 */
	String submitCommonRisk(CommonRiskSubmitReq req);

}