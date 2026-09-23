package cn.seehoo.spg.common.ecif.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 个人新增响应躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class PersonBaiscRes {

    /**
     * 客户名称
     */
    private String custName;

    /**
     * 客户状态
     */
    private String custStatus;

    /**
     * 客户分类
     */
    private String custCategories;

    private LocalDate certStartDate;

    /**
     * 证件到期日期
     */
    private LocalDate certEndDate;

    private String gender;

    /**
     * 国家
     */
    private String country;

    /**
     * 职业
     */
    private String profession;

    /**
     * 联系电话
     */
    private String contactPhones;

    /**
     * 出生日期
     */
    private LocalDate birthdate;

    /**
     * 民族
     */
    private String ethnicity;

    private String maritalStatus;

    private String education;

    private String domicileProvince;

    private String domicileCity;

    private String domicileDistrict;

    private String domicileAddress;

    private String resideProvince;

    private String resideCity;

    private String resideDistrict;

    private String resideAddress;

    private String workUnit;

    private String workUnitAddress;

    private String workUnitPhone;

    private BigDecimal personalAnnualIncome;

    private String personalAnnualIncomeCurrency;

    private BigDecimal familyAnnualIncome;

    private String familyAnnualIncomeCurrency;

    private String academicDegree;

    private String workUnitNature;

    private String workUnitPostalCode;

    private String workUnitProvince;

    private String workUnitCity;

    private String workUnitDistrict;

    private String jobPosition;

    private String jobTitle;

    private String workingStartYear;


    private String resideStatus;

    private String certIssuedAuthorityName;

    private String certIssuedAuthorityProvince;

    private String certIssuedAuthorityCity;

    private String certIssuedAuthorityDistrict;

    private String relatedTypes;

    private String taxEmail;

    private String postalCode;

    private String overseas;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    public String getCustCategories() {
        return custCategories;
    }

    public void setCustCategories(String custCategories) {
        this.custCategories = custCategories;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getContactPhones() {
        return contactPhones;
    }

    public void setContactPhones(String contactPhones) {
        this.contactPhones = contactPhones;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDomicileProvince() {
        return domicileProvince;
    }

    public void setDomicileProvince(String domicileProvince) {
        this.domicileProvince = domicileProvince;
    }

    public String getDomicileCity() {
        return domicileCity;
    }

    public void setDomicileCity(String domicileCity) {
        this.domicileCity = domicileCity;
    }

    public String getDomicileDistrict() {
        return domicileDistrict;
    }

    public void setDomicileDistrict(String domicileDistrict) {
        this.domicileDistrict = domicileDistrict;
    }

    public String getDomicileAddress() {
        return domicileAddress;
    }

    public void setDomicileAddress(String domicileAddress) {
        this.domicileAddress = domicileAddress;
    }

    public String getResideProvince() {
        return resideProvince;
    }

    public void setResideProvince(String resideProvince) {
        this.resideProvince = resideProvince;
    }

    public String getResideCity() {
        return resideCity;
    }

    public void setResideCity(String resideCity) {
        this.resideCity = resideCity;
    }

    public String getResideDistrict() {
        return resideDistrict;
    }

    public void setResideDistrict(String resideDistrict) {
        this.resideDistrict = resideDistrict;
    }

    public String getResideAddress() {
        return resideAddress;
    }

    public void setResideAddress(String resideAddress) {
        this.resideAddress = resideAddress;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkUnitAddress() {
        return workUnitAddress;
    }

    public void setWorkUnitAddress(String workUnitAddress) {
        this.workUnitAddress = workUnitAddress;
    }

    public String getWorkUnitPhone() {
        return workUnitPhone;
    }

    public void setWorkUnitPhone(String workUnitPhone) {
        this.workUnitPhone = workUnitPhone;
    }

    public BigDecimal getPersonalAnnualIncome() {
        return personalAnnualIncome;
    }

    public void setPersonalAnnualIncome(BigDecimal personalAnnualIncome) {
        this.personalAnnualIncome = personalAnnualIncome;
    }

    public String getPersonalAnnualIncomeCurrency() {
        return personalAnnualIncomeCurrency;
    }

    public void setPersonalAnnualIncomeCurrency(String personalAnnualIncomeCurrency) {
        this.personalAnnualIncomeCurrency = personalAnnualIncomeCurrency;
    }

    public BigDecimal getFamilyAnnualIncome() {
        return familyAnnualIncome;
    }

    public void setFamilyAnnualIncome(BigDecimal familyAnnualIncome) {
        this.familyAnnualIncome = familyAnnualIncome;
    }

    public String getFamilyAnnualIncomeCurrency() {
        return familyAnnualIncomeCurrency;
    }

    public void setFamilyAnnualIncomeCurrency(String familyAnnualIncomeCurrency) {
        this.familyAnnualIncomeCurrency = familyAnnualIncomeCurrency;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    public String getWorkUnitNature() {
        return workUnitNature;
    }

    public void setWorkUnitNature(String workUnitNature) {
        this.workUnitNature = workUnitNature;
    }

    public String getWorkUnitPostalCode() {
        return workUnitPostalCode;
    }

    public void setWorkUnitPostalCode(String workUnitPostalCode) {
        this.workUnitPostalCode = workUnitPostalCode;
    }

    public String getWorkUnitProvince() {
        return workUnitProvince;
    }

    public void setWorkUnitProvince(String workUnitProvince) {
        this.workUnitProvince = workUnitProvince;
    }

    public String getWorkUnitCity() {
        return workUnitCity;
    }

    public void setWorkUnitCity(String workUnitCity) {
        this.workUnitCity = workUnitCity;
    }

    public String getWorkUnitDistrict() {
        return workUnitDistrict;
    }

    public void setWorkUnitDistrict(String workUnitDistrict) {
        this.workUnitDistrict = workUnitDistrict;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWorkingStartYear() {
        return workingStartYear;
    }

    public void setWorkingStartYear(String workingStartYear) {
        this.workingStartYear = workingStartYear;
    }

    public String getResideStatus() {
        return resideStatus;
    }

    public void setResideStatus(String resideStatus) {
        this.resideStatus = resideStatus;
    }

    public String getCertIssuedAuthorityName() {
        return certIssuedAuthorityName;
    }

    public void setCertIssuedAuthorityName(String certIssuedAuthorityName) {
        this.certIssuedAuthorityName = certIssuedAuthorityName;
    }

    public String getCertIssuedAuthorityProvince() {
        return certIssuedAuthorityProvince;
    }

    public void setCertIssuedAuthorityProvince(String certIssuedAuthorityProvince) {
        this.certIssuedAuthorityProvince = certIssuedAuthorityProvince;
    }

    public String getCertIssuedAuthorityCity() {
        return certIssuedAuthorityCity;
    }

    public void setCertIssuedAuthorityCity(String certIssuedAuthorityCity) {
        this.certIssuedAuthorityCity = certIssuedAuthorityCity;
    }

    public String getCertIssuedAuthorityDistrict() {
        return certIssuedAuthorityDistrict;
    }

    public void setCertIssuedAuthorityDistrict(String certIssuedAuthorityDistrict) {
        this.certIssuedAuthorityDistrict = certIssuedAuthorityDistrict;
    }

    public String getRelatedTypes() {
        return relatedTypes;
    }

    public void setRelatedTypes(String relatedTypes) {
        this.relatedTypes = relatedTypes;
    }

    public String getTaxEmail() {
        return taxEmail;
    }

    public void setTaxEmail(String taxEmail) {
        this.taxEmail = taxEmail;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getOverseas() {
        return overseas;
    }

    public void setOverseas(String overseas) {
        this.overseas = overseas;
    }
}
