package cn.seehoo.common.unify.request;

/**
 * @author liuzeng
 * @date 2025/9/24 下午4:01
 * @since 1.0
 */
public class OutsideUserUpdateRequest {
    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 用户id(子应用生成的id,用于跟统一平台绑定数据关系)
     */
    private String sourceId;

    /**
     * 代理公司社会通用编码
     */
    private String companyCode;
    /**
     * 代理公司社会通用名称
     */
    private String companyName;

    public OutsideUserUpdateRequest() {
    }

    public OutsideUserUpdateRequest(String userName, String phoneNo, String idCardNo, String sourceId, String companyCode, String companyName) {
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.idCardNo = idCardNo;
        this.sourceId = sourceId;
        this.companyCode = companyCode;
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
