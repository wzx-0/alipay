package cn.seehoo.spg.common.ecif.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 對公客戶请求躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class CompanyBaiscReq {

    /**
     * 客戶號
     */
    private String custNo;

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

    /**
     * 客户性质
     */
    private String custNature;

    /**
     * 存续状态
     */
    private String enterpriseStatus;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 注册地国家
     */
    private String regCountry;

    /**
     * 是否境外
     */
    private String overseas;

    /**
     * 注册地所在省
     */
    private String regProvince;

    /**
     * 注册地所在市
     */
    private String regCity;

    /**
     * 注册地所在区
     */
    private String regDistrict;

    /**
     * 注册地-详细地址
     */
    private String regAddress;

    /**
     * 注册资本（元）
     */
    private BigDecimal regCapital;

    /**
     * 注册资本币种
     */
    private String regCapitalCurrency;

    /**
     * 注册成立日期
     */
    private LocalDate regDate;

    /**
     * 证件到期日期
     */
    private LocalDate certEndDate;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 企业规模
     */
    private String enterpriseScale;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 经济成分
     */
    private String economicSector;

    /**
     * 经济类型
     */
    private String economicType;

    /**
     * 净资产（元）
     */
    private BigDecimal netAssets;

    /**
     * 净资产币种
     */
    private String netAssetsCurrency;

    /**
     * 经营地国家
     */
    private String operationCountry;

    /**
     * 经营地所在省
     */
    private String operationProvince;

    /**
     * 经营地所在市
     */
    private String operationCity;

    /**
     * 经营地所在区
     */
    private String operationDistrict;

    /**
     * 经营地-详细地址
     */
    private String operationAddress;

    /**
     * 上市板块
     */
    private String listedSectors;

    /**
     * 高技术制造业分类
     */
    private String manufacturingType;

    /**
     * 战略性新兴产业分类
     */
    private String strategicEmergingType;

    /**
     * 是否为“中国制造2025”
     */
    private String madeInChina2025;

    /**
     * 是否为“走出去项目”
     */
    private String goingOutProject;

    /**
     * 是否为工业转型项目
     */
    private String industrialTransProject;

    /**
     * 是否专精特新
     */
    private String specialtyNew;

    /**
     * 是否专精特新小巨人
     */
    private String littleGiant;

    /**
     * 中征码
     */
    private String creditCode;

    /**
     * 内部评级-评级结果
     */
    private String creditLevel;

    /**
     * 内部评级-评级有效期始
     */
    private LocalDate creditStartDate;

    /**
     * 内部评级-评级有效期止
     */
    private LocalDate creditEndDate;

    /**
     * 企业网址
     */
    private String website;

    /**
     * 职工人数
     */
    private Integer employeeNumber;

    /**
     * 实收资本(元)
     */
    private String paidinCapital;

    /**
     * 实收资本币种
     */
    private String paidinCapitalCurrency;

    /**
     * 关联类型
     */
    private String relatedTypes;

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

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

    public String getCustNature() {
        return custNature;
    }

    public void setCustNature(String custNature) {
        this.custNature = custNature;
    }

    public String getEnterpriseStatus() {
        return enterpriseStatus;
    }

    public void setEnterpriseStatus(String enterpriseStatus) {
        this.enterpriseStatus = enterpriseStatus;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegCountry() {
        return regCountry;
    }

    public void setRegCountry(String regCountry) {
        this.regCountry = regCountry;
    }

    public String getOverseas() {
        return overseas;
    }

    public void setOverseas(String overseas) {
        this.overseas = overseas;
    }

    public String getRegProvince() {
        return regProvince;
    }

    public void setRegProvince(String regProvince) {
        this.regProvince = regProvince;
    }

    public String getRegCity() {
        return regCity;
    }

    public void setRegCity(String regCity) {
        this.regCity = regCity;
    }

    public String getRegDistrict() {
        return regDistrict;
    }

    public void setRegDistrict(String regDistrict) {
        this.regDistrict = regDistrict;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public BigDecimal getRegCapital() {
        return regCapital;
    }

    public void setRegCapital(BigDecimal regCapital) {
        this.regCapital = regCapital;
    }

    public String getRegCapitalCurrency() {
        return regCapitalCurrency;
    }

    public void setRegCapitalCurrency(String regCapitalCurrency) {
        this.regCapitalCurrency = regCapitalCurrency;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public LocalDate getCertEndDate() {
        return certEndDate;
    }

    public void setCertEndDate(LocalDate certEndDate) {
        this.certEndDate = certEndDate;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getEnterpriseScale() {
        return enterpriseScale;
    }

    public void setEnterpriseScale(String enterpriseScale) {
        this.enterpriseScale = enterpriseScale;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getEconomicSector() {
        return economicSector;
    }

    public void setEconomicSector(String economicSector) {
        this.economicSector = economicSector;
    }

    public String getEconomicType() {
        return economicType;
    }

    public void setEconomicType(String economicType) {
        this.economicType = economicType;
    }

    public BigDecimal getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(BigDecimal netAssets) {
        this.netAssets = netAssets;
    }

    public String getNetAssetsCurrency() {
        return netAssetsCurrency;
    }

    public void setNetAssetsCurrency(String netAssetsCurrency) {
        this.netAssetsCurrency = netAssetsCurrency;
    }

    public String getOperationCountry() {
        return operationCountry;
    }

    public void setOperationCountry(String operationCountry) {
        this.operationCountry = operationCountry;
    }

    public String getOperationProvince() {
        return operationProvince;
    }

    public void setOperationProvince(String operationProvince) {
        this.operationProvince = operationProvince;
    }

    public String getOperationCity() {
        return operationCity;
    }

    public void setOperationCity(String operationCity) {
        this.operationCity = operationCity;
    }

    public String getOperationDistrict() {
        return operationDistrict;
    }

    public void setOperationDistrict(String operationDistrict) {
        this.operationDistrict = operationDistrict;
    }

    public String getOperationAddress() {
        return operationAddress;
    }

    public void setOperationAddress(String operationAddress) {
        this.operationAddress = operationAddress;
    }

    public String getListedSectors() {
        return listedSectors;
    }

    public void setListedSectors(String listedSectors) {
        this.listedSectors = listedSectors;
    }

    public String getManufacturingType() {
        return manufacturingType;
    }

    public void setManufacturingType(String manufacturingType) {
        this.manufacturingType = manufacturingType;
    }

    public String getStrategicEmergingType() {
        return strategicEmergingType;
    }

    public void setStrategicEmergingType(String strategicEmergingType) {
        this.strategicEmergingType = strategicEmergingType;
    }

    public String getMadeInChina2025() {
        return madeInChina2025;
    }

    public void setMadeInChina2025(String madeInChina2025) {
        this.madeInChina2025 = madeInChina2025;
    }

    public String getGoingOutProject() {
        return goingOutProject;
    }

    public void setGoingOutProject(String goingOutProject) {
        this.goingOutProject = goingOutProject;
    }

    public String getIndustrialTransProject() {
        return industrialTransProject;
    }

    public void setIndustrialTransProject(String industrialTransProject) {
        this.industrialTransProject = industrialTransProject;
    }

    public String getSpecialtyNew() {
        return specialtyNew;
    }

    public void setSpecialtyNew(String specialtyNew) {
        this.specialtyNew = specialtyNew;
    }

    public String getLittleGiant() {
        return littleGiant;
    }

    public void setLittleGiant(String littleGiant) {
        this.littleGiant = littleGiant;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public LocalDate getCreditStartDate() {
        return creditStartDate;
    }

    public void setCreditStartDate(LocalDate creditStartDate) {
        this.creditStartDate = creditStartDate;
    }

    public LocalDate getCreditEndDate() {
        return creditEndDate;
    }

    public void setCreditEndDate(LocalDate creditEndDate) {
        this.creditEndDate = creditEndDate;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getPaidinCapital() {
        return paidinCapital;
    }

    public void setPaidinCapital(String paidinCapital) {
        this.paidinCapital = paidinCapital;
    }

    public String getPaidinCapitalCurrency() {
        return paidinCapitalCurrency;
    }

    public void setPaidinCapitalCurrency(String paidinCapitalCurrency) {
        this.paidinCapitalCurrency = paidinCapitalCurrency;
    }

    public String getRelatedTypes() {
        return relatedTypes;
    }

    public void setRelatedTypes(String relatedTypes) {
        this.relatedTypes = relatedTypes;
    }

}
