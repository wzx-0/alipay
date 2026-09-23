package cn.seehoo.spg.bizcom.qcc.model;

import lombok.Data;

//惩戒名单核查VO
@Data
public class DisciplinaryCheckInfo {
	//惩戒名单类型
	private String punishType;
	//惩戒名单领域
	private String punishFiled;
	//列入原因
	private String caseReason;
	//列入机关
	private String decisionOffice;
	//列入日期
	private String decisionDate;
	//移除日期
	private String removedDate;
}