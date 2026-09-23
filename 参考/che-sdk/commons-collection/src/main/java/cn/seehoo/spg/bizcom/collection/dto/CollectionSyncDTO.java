package cn.seehoo.spg.bizcom.collection.dto;

import cn.seehoo.spg.bizcom.collection.exception.CollectionBillException;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.List;

/**
 * @author zhangxx
 * @date 2026/1/8 9:58
 */
public class CollectionSyncDTO {
    /** 业务系统主键 */
    @NotNull(message = CollectionBillException.SOURCE_ID_NULL)
    @NotEmpty(message = CollectionBillException.SOURCE_ID_NULL)
    private String sourceId;
    /** 供应商名称 */
    @NotNull(message = CollectionBillException.COMPANY_NAME_NULL)
    @NotEmpty(message = CollectionBillException.COMPANY_NAME_NULL)
    private String companyName;
    /** 供应商编码 */
    @NotNull(message = CollectionBillException.COMPANY_CODE_NULL)
    @NotEmpty(message = CollectionBillException.COMPANY_CODE_NULL)
    private String companyCode;
    /** 供应商类型 */
    @NotNull(message = CollectionBillException.COMPANY_TYPE_NULL)
    @NotEmpty(message = CollectionBillException.COMPANY_TYPE_NULL)
    private String companyType;
    /** 供应商状态 */
    @NotNull(message = CollectionBillException.COMPANY_STATUS_NULL)
    @NotEmpty(message = CollectionBillException.COMPANY_STATUS_NULL)
    private String companyStatus;
    /** 法人名称 */
    private String legalName;
    /** 联系人名称 */
    private String contactName;
    /** 手机号 */
    private String contactPhone;
    /** 邮箱 */
    private String contactEmail;
    /** 详细地址 */
    private String contactAddress;
    /** 展业地址 */
    private List<CollectionRegionDTO> areas;

    public String getSourceId() {
        return sourceId;
    }
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getCompanyType() {
        return companyType;
    }
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    public String getCompanyStatus() {
        return companyStatus;
    }
    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }
    public String getLegalName() {
        return legalName;
    }
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    public String getContactEmail() {
        return contactEmail;
    }
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getContactAddress() {
        return contactAddress;
    }
    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
    public List<CollectionRegionDTO> getAreas() {
        return areas;
    }
    public void setAreas(List<CollectionRegionDTO> areas) {
        this.areas = areas;
    }
}
