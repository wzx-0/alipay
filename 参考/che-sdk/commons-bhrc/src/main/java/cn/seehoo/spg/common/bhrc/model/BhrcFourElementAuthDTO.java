package cn.seehoo.spg.common.bhrc.model;

/**
 * @author caofei
 * @desc 百行公安四要素请求数据
 * @time 2025/9/25 14:19。
 */
public class BhrcFourElementAuthDTO {
    /**
     * 姓名
     */
    private String name;
    /**
     * 证件类型
     */
    private String certType;
    /**
     * 证件号码
     */
    private String certNo;
    /**
     * 有效期起
     * 客户居民身份证有效期起始日期，格式：“YYYYMMDD”
     */
    private String validStartDate;
    /**
     * 有效期止
     * 1. 客户居民身份证有效期结束时间时间，格式：“YYYYMMDD”
     * 2. “长期”需转换为“00000000”；
     */
    private String validEndDate;
    /**
     * 加密方式
     * 敏感字段（身份证号）加密方式：
     * 0 或不填：明文
     * 1：MD5
     * 2：SHA256
     * 3：SM3
     * 身份证中如有 X，应采用大写
     */
    private String encryptType;
    /** 调用环节 */
    private String callStage;
    /** 合作方 */
    private String partnerName;
    /** 业务编号 */
    private String businessNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getValidStartDate() {
        return validStartDate;
    }

    public void setValidStartDate(String validStartDate) {
        this.validStartDate = validStartDate;
    }

    public String getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(String validEndDate) {
        this.validEndDate = validEndDate;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    public String getCallStage() {
        return callStage;
    }

    public void setCallStage(String callStage) {
        this.callStage = callStage;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }
}
