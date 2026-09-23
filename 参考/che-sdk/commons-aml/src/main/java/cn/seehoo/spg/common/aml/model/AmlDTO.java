package cn.seehoo.spg.common.aml.model;

/**
 * @author chenzhuo
 */
public class AmlDTO {
    /**
     * 客户名称
     */
    private String cust_name;

    /**
     * 证件类型
     */
    private String cert_type;

    /**
     * 证件号码
     */
    private String cert_no;

    /**
     * 出生日期
     */
    private String birth_dt;

    /**
     * 国籍
     */
    private String cust_nat;

    /**
     * 证件到期日
     */
    private String cert_invalid_dt;

    /**
     * 地区代码
     */
    private String cust_area;

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCert_type() {
        return cert_type;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getBirth_dt() {
        return birth_dt;
    }

    public void setBirth_dt(String birth_dt) {
        this.birth_dt = birth_dt;
    }

    public String getCust_nat() {
        return cust_nat;
    }

    public void setCust_nat(String cust_nat) {
        this.cust_nat = cust_nat;
    }

    public String getCert_invalid_dt() {
        return cert_invalid_dt;
    }

    public void setCert_invalid_dt(String cert_invalid_dt) {
        this.cert_invalid_dt = cert_invalid_dt;
    }

    public String getCust_area() {
        return cust_area;
    }

    public void setCust_area(String cust_area) {
        this.cust_area = cust_area;
    }
}
