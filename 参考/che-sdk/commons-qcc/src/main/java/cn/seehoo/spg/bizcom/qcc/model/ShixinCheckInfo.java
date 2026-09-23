package cn.seehoo.spg.bizcom.qcc.model;

import lombok.Data;

//失信核查VO
@Data
public class ShixinCheckInfo {
	//主键
	private String id;
	//立案日期
	private String lianDate;
	//案号
	private String anno;
	//执行法院
	private String executeGov;
	//被执行人的履行情况
	private String executeStatus;
	//发布日期
	private String publicDate;
	//执行依据文号
	private String executeNo;
	//失信行为
	private String actionRemark;
	//涉案金额
	private String amount;
}