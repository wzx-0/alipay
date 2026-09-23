package cn.seehoo.spg.bizcom.qcc.model;

import lombok.Data;

//严重违法核查VO
@Data
public class SeriousIllegalCheckInfo {
	//类型
	private String type;
	//列入原因
	private String addReason;
	//列入日期
	private String addDate;
	//列入决定机关
	private String addOffice;
	//移除原因(保留字段)
	private String removeReason;
	//移除日期(保留字段)
	private String removeDate;
	//移除决定机关
	private String removeOffice;
}