package cn.seehoo.spg.common.ecif.model;

/**
 * 个人-关联方查询
 * @author
 * @date 2025/9/23 下午6:51
 * @since 1.0
 */
public class QueryRelativesReq extends CommonHeaderReq {

    /**
     * 证件号码
     */
    private String certId;
    /** 客户姓名 */
    private String custName;

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}
