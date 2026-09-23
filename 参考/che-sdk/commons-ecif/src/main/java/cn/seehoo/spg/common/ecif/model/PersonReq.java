package cn.seehoo.spg.common.ecif.model;

import java.util.List;

/**
 * 客户查询参数
 * @author
 * @date 2025/9/23 下午6:51
 * @since 1.0
 */
public class PersonReq extends CommonHeaderReq {

    public static final String ADD = "1";
    public static final String UPDATE = "0";

    /**
     * 客户编码
     */
    private String customerId;

    /**
     * 客商系统客户编号
     */
    private String customerEcifNo;

    /**
     * 处理类型：1-新增 0-更新
     */
    private String dealType;

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
    private PersonBaiscReq basic;

    /**
     * 关联方信息
     */
    private List<PersonRelationReq> relations;

    /**
     * 账户信息
     */
    private List<PersonAccountReq> accounts;


    /**
     * 客户负责人
     */
    private List<PersonDirectorReq> directors;

    /**
     * 营业执照
     */
    private List<PersonCertPhotoReq> certPhotos;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
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

    public PersonBaiscReq getBasic() {
        return basic;
    }

    public void setBasic(PersonBaiscReq basic) {
        this.basic = basic;
    }

    public List<PersonRelationReq> getRelations() {
        return relations;
    }

    public void setRelations(List<PersonRelationReq> relations) {
        this.relations = relations;
    }

    public List<PersonAccountReq> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<PersonAccountReq> accounts) {
        this.accounts = accounts;
    }

    public List<PersonDirectorReq> getDirectors() {
        return directors;
    }

    public void setDirectors(List<PersonDirectorReq> directors) {
        this.directors = directors;
    }

    public List<PersonCertPhotoReq> getCertPhotos() {
        return certPhotos;
    }

    public void setCertPhotos(List<PersonCertPhotoReq> certPhotos) {
        this.certPhotos = certPhotos;
    }

    public String getCustomerEcifNo() {
        return customerEcifNo;
    }

    public void setCustomerEcifNo(String customerEcifNo) {
        this.customerEcifNo = customerEcifNo;
    }

}
