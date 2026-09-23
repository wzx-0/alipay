package cn.seehoo.spg.bizcom.qcc.vo;

import lombok.Data;

import java.util.Date;

//惩戒名单核查VO
@Data
public class DisciplinaryCheckVO {
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
	//是否存在
	private String verifyResult;
	//查询时间
	private Date searchTime;
	//企查查订单号
	private String orderNumber;
}