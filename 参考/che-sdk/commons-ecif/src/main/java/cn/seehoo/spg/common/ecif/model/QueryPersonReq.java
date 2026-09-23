package cn.seehoo.spg.common.ecif.model;

import java.util.List;

/**
 * 个人-精准查询入参
 * @author
 * @date 2025/9/23 下午6:51
 * @since 1.0
 */
public class QueryPersonReq extends CommonHeaderReq {

    /**
     * 证件类型
     */
    private String certType;

    /**
     * 证件号码
     */
    private String certId;

    /**
     * 客户号
     */
    private String custNo;

    /**
     * 查询模块
     */
    private List<String> selections;

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public List<String> getSelections() {
        return selections;
    }

    public void setSelections(List<String> selections) {
        this.selections = selections;
    }

}
