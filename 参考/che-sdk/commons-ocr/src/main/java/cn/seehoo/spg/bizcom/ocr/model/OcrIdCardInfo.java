package cn.seehoo.spg.bizcom.ocr.model;

/**
 * @author chenjun
 * 
 * 身份证信息
 */
public class OcrIdCardInfo {
	/** 身份证正面 */
	public static final String IDCARD_FRONT = "front";
	/** 身份证反面 */
	public static final String IDCARD_BACK = "back";
	/** 签发机关 */
	private String issueAuthority;
	/** 失效日期 */
	private String expireDate;
	/** 发证日期 */
	private String issueDate;
	/** 姓名 */
	private String name;
	/** 民族 */
	private String nation;
	/** 地址 */
	private String address;
	/** 公民身份证 */
	private String idcard;
	/** 出生 */
	private String born;
	/** 性别 */
	private String sexual;
	
	public OcrIdCardInfo(String name, String nation, String address, String idcard, String born, String sexual) {
		super();
		this.name = name;
		this.nation = nation;
		this.address = address;
		this.idcard = idcard;
		this.born = born;
		this.sexual = sexual;
	}

	public OcrIdCardInfo() {
		super();
	}

	public String getIssueAuthority() {
		return issueAuthority;
	}
	public void setIssueAuthority(String issueAuthority) {
		this.issueAuthority = issueAuthority;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBorn() {
		return born;
	}
	public void setBorn(String born) {
		this.born = born;
	}
	public String getSexual() {
		return sexual;
	}
	public void setSexual(String sexual) {
		this.sexual = sexual;
	}
	
	@Override
	public String toString() {
		return "OcrIdCardInfo [issueAuthority=" + issueAuthority + ", expireDate=" + expireDate + ", issueDate="
				+ issueDate + ", name=" + name + ", nation=" + nation + ", address=" + address + ", idcard=" + idcard
				+ ", born=" + born + ", sexual=" + sexual + "]";
	}
}