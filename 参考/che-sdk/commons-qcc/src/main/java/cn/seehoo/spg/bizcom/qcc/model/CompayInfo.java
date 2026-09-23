package cn.seehoo.spg.bizcom.qcc.model;

import java.sql.Date;

// 公司工商注册信息
public class CompayInfo {
	/** 公司名称 */
	private String Name;
	 /** 证件有效期起 */
    private Date TermStart;
    /** 证件有效期止 */
    private Date TermEnd;
    /** 注册资本（万元） */
    private String RegisteredCapital;
    /** 成立日期 */
    private Date StartDate;
    /** 法人姓名 */
    private String OperName;
	/** 法人ID */
	private String OperId;
    /** 经营状况 */
    private String Status;
    /** 注册地省 */
    private String Province;
    /** 注册地市 */
    private String City;
    /** 注册地区 */
    private String County;
    /** 注册地址 */
    private String Address;
    /** 统一社会信用代码 */
    private String CreditCode;
    /** 经营范围 */
    private String Scope;
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
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Date getTermStart() {
		return TermStart;
	}
	public void setTermStart(Date termStart) {
		TermStart = termStart;
	}
	public Date getTermEnd() {
		return TermEnd;
	}
	public void setTermEnd(Date termEnd) {
		TermEnd = termEnd;
	}
	public String getRegisteredCapital() {
		return RegisteredCapital;
	}
	public void setRegisteredCapital(String registeredCapital) {
		RegisteredCapital = registeredCapital;
	}
	public Date getStartDate() {
		return StartDate;
	}
	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}
	public String getOperName() {
		return OperName;
	}
	public void setOperName(String operName) {
		OperName = operName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getCounty() {
		return County;
	}
	public void setCounty(String county) {
		County = county;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCreditCode() {
		return CreditCode;
	}
	public void setCreditCode(String creditCode) {
		CreditCode = creditCode;
	}
	public String getScope() {
		return Scope;
	}
	public void setScope(String scope) {
		Scope = scope;
	}
	
	@Override
	public String toString() {
		return "CompayInfo [TermStart=" + TermStart + ", TermEnd=" + TermEnd + ", RegisteredCapital="
				+ RegisteredCapital + ", StartDate=" + StartDate + ", OperName=" + OperName + ", Status=" + Status
				+ ", Province=" + Province + ", City=" + City + ", County=" + County + ", Address=" + Address + "]";
	}

	public String getOperId() {
		return OperId;
	}

	public void setOperId(String operId) {
		OperId = operId;
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


}