package cn.seehoo.spg.common.ecif.model;

import java.util.List;

/**
 * 新增更新对公客户接口
 * @author
 * @date 2025/9/23 下午6:51
 * @since 1.0
 */
public class CompanyReq extends CommonHeaderReq {

    public static final String ADD = "1";
    public static final String UPDATE = "0";

    /**
     * 处理类型：1-新增 0-更新
     */
    private String dealType;

    /**
     * 合作方/挂靠方/车商编码
     */
    private String companyCode;

    /**
     * 版本号
     */
    private Integer revision;

    /**
     * 证件类型
     */
    private String certType;

    /**
     * 证件号码
     */
    private String certId;

    /**
     * 基本信息
     */
    private CompanyBaiscReq basic;

    /**
     * 关联方信息
     */
    private List<CompanyRelationReq> relations;

    /**
     * 账户信息
     */
    private List<CompanyAccountReq> accounts;

    /**
     * 开票信息
     */
    private CompanyInvoiceReq invoice;

    /**
     * 客户负责人
     */
    private List<CompanyDirectorReq> directors;

    /**
     * 营业执照
     */
    private List<CompanyCertPhotoReq> certPhotos;


    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
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

    public CompanyBaiscReq getBasic() {
        return basic;
    }

    public void setBasic(CompanyBaiscReq basic) {
        this.basic = basic;
    }

    public List<CompanyRelationReq> getRelations() {
        return relations;
    }

    public void setRelations(List<CompanyRelationReq> relations) {
        this.relations = relations;
    }

    public List<CompanyAccountReq> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<CompanyAccountReq> accounts) {
        this.accounts = accounts;
    }

    public CompanyInvoiceReq getInvoice() {
        return invoice;
    }

    public void setInvoice(CompanyInvoiceReq invoice) {
        this.invoice = invoice;
    }

    public List<CompanyDirectorReq> getDirectors() {
        return directors;
    }

    public void setDirectors(List<CompanyDirectorReq> directors) {
        this.directors = directors;
    }

    public List<CompanyCertPhotoReq> getCertPhotos() {
        return certPhotos;
    }

    public void setCertPhotos(List<CompanyCertPhotoReq> certPhotos) {
        this.certPhotos = certPhotos;
    }

}
