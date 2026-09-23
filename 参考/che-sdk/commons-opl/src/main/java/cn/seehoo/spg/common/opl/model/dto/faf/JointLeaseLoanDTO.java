package cn.seehoo.spg.common.opl.model.dto.faf;

/**
 * @author
 * @date 2026/8/21 14:27
 * @since 1.0
 */
public class JointLeaseLoanDTO {
    /**
     * 订单编号
     */
    private String hxOrderNo;

    /**
     * 状态 01成功，02失败
     */
    private String loanStatus;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 银行放款金额
     */
    private String moneyBank;

    public String getHxOrderNo() {
        return hxOrderNo;
    }
    public void setHxOrderNo(String hxOrderNo) {
        this.hxOrderNo = hxOrderNo;
    }
    public String getLoanStatus() {
        return loanStatus;
    }
    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
    public String getFailReason() {
        return failReason;
    }
    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
    public String getMoneyBank() {
        return moneyBank;
    }
    public void setMoneyBank(String moneyBank) {
        this.moneyBank = moneyBank;
    }
}
