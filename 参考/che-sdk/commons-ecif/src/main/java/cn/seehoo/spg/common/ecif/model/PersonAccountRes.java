package cn.seehoo.spg.common.ecif.model;

/**
 * 个人账号信息响应躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class PersonAccountRes {

    private String id;
    private String accountNo;

    private String accountBank;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
