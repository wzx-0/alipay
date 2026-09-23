package cn.seehoo.spg.common.risk.model;

import java.io.Serializable;

/**
 * 主貸人信息
 */
public class MainLoanReq implements Serializable {

    private static final long serialVersionUID = 3475425945182237654L;

    //主贷人姓名
    private String NAME;

    //主贷人手机号
    private String PHONE;

    //主贷人证件号码(如最后一位为X请使用大写字母)
    private String CUST_ID_NO;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getCUST_ID_NO() {
        return CUST_ID_NO;
    }

    public void setCUST_ID_NO(String CUST_ID_NO) {
        this.CUST_ID_NO = CUST_ID_NO;
    }

}
