package cn.seehoo.spg.common.ecif.model;

import java.util.List;

/**
 * 个人客户响应
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class PersonRes extends CommonRes {

    /**
     * 财务编码
     */
    private String financeCustomerNo;

    /**
     * 基本信息
     */
    private PersonBaiscRes basic;

    /**
     * 关联方信息
     */
    private List<PersonRelationRes> relations;

    /**
     * 账户信息
     */
    private List<PersonAccountRes> accounts;

    /**
     * 客户负责人
     */
    private List<PersonDirectorRes> directors;

    /**
     * 营业执照
     */
    private List<PersonCertPhotoRes> certPhotos;

    public PersonBaiscRes getBasic() {
        return basic;
    }

    public void setBasic(PersonBaiscRes basic) {
        this.basic = basic;
    }

    public List<PersonRelationRes> getRelations() {
        return relations;
    }

    public void setRelations(List<PersonRelationRes> relations) {
        this.relations = relations;
    }

    public List<PersonAccountRes> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<PersonAccountRes> accounts) {
        this.accounts = accounts;
    }

    public List<PersonDirectorRes> getDirectors() {
        return directors;
    }

    public void setDirectors(List<PersonDirectorRes> directors) {
        this.directors = directors;
    }

    public List<PersonCertPhotoRes> getCertPhotos() {
        return certPhotos;
    }

    public void setCertPhotos(List<PersonCertPhotoRes> certPhotos) {
        this.certPhotos = certPhotos;
    }

    public String getFinanceCustomerNo() {
        return financeCustomerNo;
    }

    public void setFinanceCustomerNo(String financeCustomerNo) {
        this.financeCustomerNo = financeCustomerNo;
    }

    public static String getMockObj(){
        String respBody = "{\"returnCode\":\"000000\",\"message\":\"请求处理成功\",\"data\":{\"custNo\":\"P20241230020000002\",\"financeNo\":\"caiwubianma001\",\"supplierNo\":\"gongyingshangbianma001\",\"certType\":\"P_01\",\"revision\":5,\"revisionChannel\":\"04001\",\"updateTime\":\"2024-10-10 14:14:14\",\"certId\":\"511423199412260051\",\"basic\":{\"custName\":\"闽玉兰\",\"custStatus\":\"0\",\"industry\":\"M7590\",\"custCategories\":\"01,02\",\"certStartDate\":\"2024-11-21\",\"certEndDate\":\"2025-01-22\",\"gender\":\"2\",\"country\":\"CHN\",\"overseas\":\"N\",\"profession\":\"20227\",\"contactPhones\":\"45864714001\",\"birthdate\":\"1994-12-26\",\"ethnicity\":\"01\",\"maritalStatus\":\"10\",\"education\":\"1\",\"domicileProvince\":\"140000\",\"domicileCity\":\"140100\",\"domicileDistrict\":\"140101\",\"domicileAddress\":\"山西省 太原市 市辖区 庆旁764号 74单元\",\"resideProvince\":\"140000\",\"resideCity\":\"140100\",\"resideDistrict\":\"140101\",\"resideAddress\":\"山西省 太原市 市辖区 过侬51号 17层\",\"workUnit\":\"山西省 太原市 市辖区\",\"workUnitAddress\":\"山西省 太原市 市辖区 景桥4868号 28层\",\"workUnitPhone\":\"-12104878606\",\"personalAnnualIncome\":98870.25,\"personalAnnualIncomeCurrency\":\"CNY\",\"familyAnnualIncome\":75480.26,\"familyAnnualIncomeCurrency\":\"CNY\",\"academicDegree\":\"1\",\"workUnitNature\":\"01\",\"workUnitPostalCode\":\"605741\",\"workUnitProvince\":\"140000\",\"workUnitCity\":\"140100\",\"workUnitDistrict\":\"140101\",\"jobTitle\":\"未来应用程序开发员\",\"jobPosition\":\"1\",\"workingStartYear\":2025,\"resideStatus\":\"1\",\"certIssuedAuthorityName\":\"山西省人力资源和社会保障局\",\"certIssuedAuthorityProvince\":\"140000\",\"certIssuedAuthorityCity\":\"140100\",\"certIssuedAuthorityDistrict\":\"140101\",\"relatedTypes\":\"0\",\"taxEmail\":\"fozmy274@21cn.com\",\"postalCode\":\"179075\"},\"relations\":[{\"id\":\"yNsmOTWWL7lA60wAw4Rp2\",\"type\":\"1\",\"name\":\"学国英\",\"certType\":\"P_01\",\"certId\":\"511423199412260051\",\"contactPhone\":\"-11037903477\",\"workUnit\":\"山西省人力资源和社会保障局\"}],\"accounts\":[{\"id\":\"CwLT84Jug\",\"accountBank\":\"示例开户行\",\"accountNo\":\"1234 5678 9012 3456\"}],\"directors\":[{\"directorType\":\"01\",\"directorUserName\":\"zhangsan\",\"isHost\":\"Y\"}],\"certPhotos\":[{\"id\":\"QaNiFL1ZD8IUj3VxUbLRI\",\"type\":\"est commodo anim irure mollit\",\"fileDisplayName\":\"仍雪\",\"fileFullPath\":\"/etc/mail/test.jpg\",\"fileDownloadUrl\":\"https://tender-apparatus.com/\",\"fileSize\":45654654,\"fileType\":\"image/jpeg\",\"fileExtension\":\"pdf\",\"s3FullPath\":\"hxcif/etc/mail/test.jpg\"},{\"id\":\"WrkiQ-NFFwSPKLN4_3Gnc\",\"type\":\"consequat ullamco sed occaecat\",\"fileDisplayName\":\"欧阳宇航\",\"fileFullPath\":\"/var/spool/test.jpg\",\"fileDownloadUrl\":\"https://unripe-secrecy.biz/\",\"fileSize\":45654654,\"fileType\":\"image/jpeg\",\"fileExtension\":\"pdf\",\"s3FullPath\":\"hxcif/etc/mail/test.jpg\"}]}}";
        return respBody;
    }

}
