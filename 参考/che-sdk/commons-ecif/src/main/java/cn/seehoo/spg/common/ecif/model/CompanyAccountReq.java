package cn.seehoo.spg.common.ecif.model;

/**
 * 账号信息请求躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class CompanyAccountReq {

    /**
     * 账号信息主键ID
     */
    private String id;

    private String accountNo;

    private String accountName;

    private String accountBank;

    private String accountBankNo;

    private String accountBankProvince;

    private String accountBankCity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountBankNo() {
        return accountBankNo;
    }

    public void setAccountBankNo(String accountBankNo) {
        this.accountBankNo = accountBankNo;
    }

    public String getAccountBankProvince() {
        return accountBankProvince;
    }

    public void setAccountBankProvince(String accountBankProvince) {
        this.accountBankProvince = accountBankProvince;
    }

    public String getAccountBankCity() {
        return accountBankCity;
    }

    public void setAccountBankCity(String accountBankCity) {
        this.accountBankCity = accountBankCity;
    }

}
