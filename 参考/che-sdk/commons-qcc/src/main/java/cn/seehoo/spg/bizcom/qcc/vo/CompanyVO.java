package cn.seehoo.spg.bizcom.qcc.vo;

import java.sql.Date;

// 公司工商注册信息
public class CompanyVO {
	/** 公司名称 */
	private String Name;
	/** 注册时间 */
    private Date registeDate;
    /** 注册省名称 */
    private String registeProvinceName;
    /** 注册市名称 */
    private String registeCityName;
    /** 注册区名称 */
    private String registeDistrictName;
    /** 注册详细地址 */
    private String registeAddress;
    /** 证件有效期起 */
    private Date idBeginDate;
    /** 证件有效期止 */
    private Date idEndDate;
    /** 注册资本 */
    private String regiestAmount;
    /** 法人姓名 */
    private String legalPersonName;
    /** 经营状况 */
    private String optCondition;
    /** 证件号 */
    private String idNo;
    /** 经营范围 */
    private String scope;
	/** 企业类型 */
	private String EconKind;
	/** 吊销日期 */
	private String EndDate;
	/** 核准日期 */
	private String CheckDate;
	/** 登记机关 */
	private String BelongOrg;
	/** 请求状态 */
	private String reqStatus;
	/** 请求结果消息 */
	private String msg;
	/** 法人ID */
	private String OperId;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getRegisteDate() {
		return registeDate;
	}
	public void setRegisteDate(Date registeDate) {
		this.registeDate = registeDate;
	}
	public String getRegisteProvinceName() {
		return registeProvinceName;
	}
	public void setRegisteProvinceName(String registeProvinceName) {
		this.registeProvinceName = registeProvinceName;
	}
	public String getRegisteCityName() {
		return registeCityName;
	}
	public void setRegisteCityName(String registeCityName) {
		this.registeCityName = registeCityName;
	}
	public String getRegisteDistrictName() {
		return registeDistrictName;
	}
	public void setRegisteDistrictName(String registeDistrictName) {
		this.registeDistrictName = registeDistrictName;
	}
	public String getRegisteAddress() {
		return registeAddress;
	}
	public void setRegisteAddress(String registeAddress) {
		this.registeAddress = registeAddress;
	}
	public Date getIdBeginDate() {
		return idBeginDate;
	}
	public void setIdBeginDate(Date idBeginDate) {
		this.idBeginDate = idBeginDate;
	}
	public Date getIdEndDate() {
		return idEndDate;
	}
	public void setIdEndDate(Date idEndDate) {
		this.idEndDate = idEndDate;
	}
	public String getRegiestAmount() {
		return regiestAmount;
	}
	public void setRegiestAmount(String regiestAmount) {
		this.regiestAmount = regiestAmount;
	}
	public String getLegalPersonName() {
		return legalPersonName;
	}
	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}
	public String getOptCondition() {
		return optCondition;
	}
	public void setOptCondition(String optCondition) {
		this.optCondition = optCondition;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getEconKind() {
		return EconKind;
	}

	public void setEconKind(String econKind) {
		EconKind = econKind;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}

	public String getBelongOrg() {
		return BelongOrg;
	}

	public void setBelongOrg(String belongOrg) {
		BelongOrg = belongOrg;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOperId() {
		return OperId;
	}

	public void setOperId(String operId) {
		OperId = operId;
	}
}