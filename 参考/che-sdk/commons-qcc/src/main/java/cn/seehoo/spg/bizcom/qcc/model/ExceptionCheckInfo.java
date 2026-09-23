package cn.seehoo.spg.bizcom.qcc.model;

import lombok.Data;

//经营异常核查VO
@Data
public class ExceptionCheckInfo {
	//列入经营异常名录原因
	private String addReason;
	//列入日期
	private String addDate;
	//移除经营异常名录原因(保留字段)
	private String romoveReason;
	//移除日期(保留字段)
	private String removeDate;
	//做出决定机关
	private String decisionOffice;
	//移除决定机关(保留字段)
	private String removeDecisionOffice;
}