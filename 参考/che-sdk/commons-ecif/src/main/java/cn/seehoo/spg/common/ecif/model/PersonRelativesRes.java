package cn.seehoo.spg.common.ecif.model;

/**
 * 个人关联人响应躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class PersonRelativesRes {

    private String certId;

    private String certType;

    private String contactPhone;

    private String custName;

    private String relatedTypes;

    /**
     * 是否命中
     */
    private String hit;

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getRelatedTypes() {
        return relatedTypes;
    }

    public void setRelatedTypes(String relatedTypes) {
        this.relatedTypes = relatedTypes;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public static String getMockObj(){
        String respBody = "{\"returnCode\":\"000000\",\"message\":\"请求处理成功\",\"data\":{\"hit\":\"Y\"}}";
        return respBody;
    }

}
