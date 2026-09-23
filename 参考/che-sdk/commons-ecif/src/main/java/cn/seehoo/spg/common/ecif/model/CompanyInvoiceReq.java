package cn.seehoo.spg.common.ecif.model;

/**
 * 开票信息请求躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class CompanyInvoiceReq {

    /**
     * 开票信息主键ID
     */
    private String id;

    private String taxNo;

    private String taxAddress;

    private String taxPhone;

    private String taxBank;

    private String taxAccountNo;

    private String taxpayerType;

    private String taxEmail;

    private String taxRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getTaxAddress() {
        return taxAddress;
    }

    public void setTaxAddress(String taxAddress) {
        this.taxAddress = taxAddress;
    }

    public String getTaxPhone() {
        return taxPhone;
    }

    public void setTaxPhone(String taxPhone) {
        this.taxPhone = taxPhone;
    }

    public String getTaxBank() {
        return taxBank;
    }

    public void setTaxBank(String taxBank) {
        this.taxBank = taxBank;
    }

    public String getTaxAccountNo() {
        return taxAccountNo;
    }

    public void setTaxAccountNo(String taxAccountNo) {
        this.taxAccountNo = taxAccountNo;
    }

    public String getTaxpayerType() {
        return taxpayerType;
    }

    public void setTaxpayerType(String taxpayerType) {
        this.taxpayerType = taxpayerType;
    }

    public String getTaxEmail() {
        return taxEmail;
    }

    public void setTaxEmail(String taxEmail) {
        this.taxEmail = taxEmail;
    }

    public String getTaxRemark() {
        return taxRemark;
    }

    public void setTaxRemark(String taxRemark) {
        this.taxRemark = taxRemark;
    }

}
