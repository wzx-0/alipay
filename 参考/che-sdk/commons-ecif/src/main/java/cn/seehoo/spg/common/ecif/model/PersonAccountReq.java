package cn.seehoo.spg.common.ecif.model;

/**
 * 个人账号信息请求躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class PersonAccountReq {

    /**
     * 账号信息主键ID
     */
    private String id;

    private String accountDeleteFlag;

    private String accountNo;

    private String accountBank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountDeleteFlag() {
        return accountDeleteFlag;
    }

    public void setAccountDeleteFlag(String accountDeleteFlag) {
        this.accountDeleteFlag = accountDeleteFlag;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

}
