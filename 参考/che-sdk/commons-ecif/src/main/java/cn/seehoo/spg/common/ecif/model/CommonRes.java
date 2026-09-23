package cn.seehoo.spg.common.ecif.model;

import java.time.LocalDateTime;

/**
 * 新增/更新个人、对公信息/账户通用返回
 * @author HeCG
 * @date 2025/9/23 下午3:36
 * @since 1.0
 */
public class CommonRes {

    /**
     * 版本号
     */
    private Integer revision;

    /**
     * 最后一次修改系统编号
     * 04001：户用光伏业务系统
     * 04006：融资性租赁业务系统
     * 04006：融资性租赁业务系统
     * 04024：零售融资租赁业务系统
     *
     */
    private String revisionChannel;

    /**
     * 最后一次修改时间
     * 格式：yyyy-MM-DD HH:mm:ss
     */
    private LocalDateTime updateTime;

    /**
     * 客户号
     */
    private String custNo;

    /**
     * 客户财务编码
     */
    private String financeNo;

    /**
     * 客户供应商编码
     */
    private String supplierNo;

    /**
     * 证件类型
     */
    private String certType;

    /**
     * 证件号码
     */
    private String certId;

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getRevisionChannel() {
        return revisionChannel;
    }

    public void setRevisionChannel(String revisionChannel) {
        this.revisionChannel = revisionChannel;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getFinanceNo() {
        return financeNo;
    }

    public void setFinanceNo(String financeNo) {
        this.financeNo = financeNo;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
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

}
