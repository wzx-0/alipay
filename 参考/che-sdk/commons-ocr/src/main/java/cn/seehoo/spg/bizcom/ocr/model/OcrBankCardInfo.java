package cn.seehoo.spg.bizcom.ocr.model;

/**
 * @author chenjun
 * 
 * 银行信息
 */
public class OcrBankCardInfo {
	/** 到期日 */
	private String validDate;
	/** 银行卡号 */
	private String bankCardNumber;
	/** 银行名 */
	private String bankName;
	/** 银行类型 */
	private String bankCardType;
	
	public OcrBankCardInfo() {
		super();
	}
	
	public OcrBankCardInfo(String validDate, String bankCardNumber, String bankName, String bankCardType) {
		super();
		this.validDate = validDate;
		this.bankCardNumber = bankCardNumber;
		this.bankName = bankName;
		this.bankCardType = bankCardType;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getBankCardNumber() {
		return bankCardNumber;
	}

	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardType() {
		return bankCardType;
	}

	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
	}
}