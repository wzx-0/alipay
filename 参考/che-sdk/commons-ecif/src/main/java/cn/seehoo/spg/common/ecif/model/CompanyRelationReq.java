package cn.seehoo.spg.common.ecif.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 关联方信息请求躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class CompanyRelationReq {

    /**
     * 关联方信息主键ID
     */
    private String id;

    /**
     * 关联方分类
     */
    private String relation;

    /**
     * 关联方类型
     */
    private String type;

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
    private String certId;

    /**
     * 性别
     */
    private String gender;

    /**
     * 证件有效期起
     */
    private LocalDate certStartDate;

    /**
     * 证件有效期止
     */
    private LocalDate certEndDate;

    /**
     * 国籍
     */
    private String country;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 固定电话
     */
    private String contactFixedPhone;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    private String province;

    private String city;

    private String district;

    private String address;

    private String jobPosition;

    private BigDecimal shareholdingRatio;

    private String shareholdingControlling;

    private String beneficiaryOwnType;

    private LocalDate beneficiaryOwnStartDate;

    private LocalDate beneficiaryOwnEndDate;

    private String beneficiaryPath;

    private String beneficiaryLevel;

    private String remark;

    private LocalDate birthdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getCertStartDate() {
        return certStartDate;
    }

    public void setCertStartDate(LocalDate certStartDate) {
        this.certStartDate = certStartDate;
    }

    public LocalDate getCertEndDate() {
        return certEndDate;
    }

    public void setCertEndDate(LocalDate certEndDate) {
        this.certEndDate = certEndDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactFixedPhone() {
        return contactFixedPhone;
    }

    public void setContactFixedPhone(String contactFixedPhone) {
        this.contactFixedPhone = contactFixedPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public BigDecimal getShareholdingRatio() {
        return shareholdingRatio;
    }

    public void setShareholdingRatio(BigDecimal shareholdingRatio) {
        this.shareholdingRatio = shareholdingRatio;
    }

    public String getShareholdingControlling() {
        return shareholdingControlling;
    }

    public void setShareholdingControlling(String shareholdingControlling) {
        this.shareholdingControlling = shareholdingControlling;
    }

    public String getBeneficiaryOwnType() {
        return beneficiaryOwnType;
    }

    public void setBeneficiaryOwnType(String beneficiaryOwnType) {
        this.beneficiaryOwnType = beneficiaryOwnType;
    }

    public LocalDate getBeneficiaryOwnStartDate() {
        return beneficiaryOwnStartDate;
    }

    public void setBeneficiaryOwnStartDate(LocalDate beneficiaryOwnStartDate) {
        this.beneficiaryOwnStartDate = beneficiaryOwnStartDate;
    }

    public LocalDate getBeneficiaryOwnEndDate() {
        return beneficiaryOwnEndDate;
    }

    public void setBeneficiaryOwnEndDate(LocalDate beneficiaryOwnEndDate) {
        this.beneficiaryOwnEndDate = beneficiaryOwnEndDate;
    }

    public String getBeneficiaryPath() {
        return beneficiaryPath;
    }

    public void setBeneficiaryPath(String beneficiaryPath) {
        this.beneficiaryPath = beneficiaryPath;
    }

    public String getBeneficiaryLevel() {
        return beneficiaryLevel;
    }

    public void setBeneficiaryLevel(String beneficiaryLevel) {
        this.beneficiaryLevel = beneficiaryLevel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

}
