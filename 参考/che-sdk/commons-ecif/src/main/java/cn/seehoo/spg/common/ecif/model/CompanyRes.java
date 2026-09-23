package cn.seehoo.spg.common.ecif.model;

import cn.hutool.core.date.DateUtil;

import java.util.List;

/**
 * 客户对接公用响应
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class CompanyRes extends CommonRes {
	private String errorMsg;
    /**
     * 财务编码
     */
    private String financeCustomerNo;

    /**
     * 基本信息
     */
    private CompanyBaiscRes basic;

    /**
     * 关联方信息
     */
    private List<CompanyRelationRes> relations;

    /**
     * 账户信息
     */
    private List<CompanyAccountRes> accounts;

    /**
     * 开票信息
     */
    private CompanyInvoiceRes invoice;

    /**
     * 客户负责人
     */
    private List<CompanyDirectorRes> directors;

    /**
     * 营业执照
     */
    private List<CompanyCertPhotoRes> certPhotos;

    public String getFinanceCustomerNo() {
        return financeCustomerNo;
    }

    public void setFinanceCustomerNo(String financeCustomerNo) {
        this.financeCustomerNo = financeCustomerNo;
    }

    public CompanyBaiscRes getBasic() {
        return basic;
    }

    public void setBasic(CompanyBaiscRes basic) {
        this.basic = basic;
    }

    public List<CompanyRelationRes> getRelations() {
        return relations;
    }

    public void setRelations(List<CompanyRelationRes> relations) {
        this.relations = relations;
    }

    public List<CompanyAccountRes> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<CompanyAccountRes> accounts) {
        this.accounts = accounts;
    }

    public CompanyInvoiceRes getInvoice() {
        return invoice;
    }

    public void setInvoice(CompanyInvoiceRes invoice) {
        this.invoice = invoice;
    }

    public List<CompanyDirectorRes> getDirectors() {
        return directors;
    }

    public void setDirectors(List<CompanyDirectorRes> directors) {
        this.directors = directors;
    }

    public List<CompanyCertPhotoRes> getCertPhotos() {
        return certPhotos;
    }

    public void setCertPhotos(List<CompanyCertPhotoRes> certPhotos) {
        this.certPhotos = certPhotos;
    }

    public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static String getMockObj(){
        String custNo = "E" + DateUtil.format(DateUtil.date(), "yyyyMMdd") + String.valueOf(System.currentTimeMillis()).substring(9);
        String financeNo = "caiwubianma" + custNo.substring(6);
        String respBody = String.format("{\"returnCode\":\"000000\",\"message\":\"请求处理成功\",\"data\":{\"custNo\":\"%s\",\"financeNo\":\"%s\",\"supplierNo\":\"gongyingshangbianma001\",\"certType\":\"E_01\",\"revision\":5,\"revisionChannel\":\"04001\",\"updateTime\":\"2024-10-10 14:14:14\",\"certId\":\"91130129391284177A\",\"basic\":{\"custName\":\"山西晋瑞科技有限公司\",\"custStatus\":\"1\",\"custCategories\":\"01\",\"custNature\":\"01\",\"enterpriseStatus\":\"0\",\"industry\":\"A0111\",\"regCountry\":\"CHN\",\"overseas\":\"N\",\"regDistrict\":\"140101\",\"regAddress\":\"山西省 太原市 市辖区 建路16号 6号房间\",\"regCapital\":54658.25,\"regCapitalCurrency\":\"CNY\",\"regDate\":\"2024-02-12\",\"certEndDate\":\"2025-01-07\",\"businessScope\":\"经营范围\",\"enterpriseScale\":\"01\",\"contactPhone\":\"-057 3794 0877\",\"economicSector\":\"A0101\",\"economicType\":\"100\",\"netAssets\":40782.25,\"netAssetsCurrency\":\"CNY\",\"operationCountry\":\"CHN\",\"operationDistrict\":\"140101\",\"operationAddress\":\"山西省 太原市 市辖区 建路16号 6号房间\",\"listedSectors\":\"A\",\"manufacturingType\":\"01\",\"strategicEmergingType\":\"00\",\"madeInChina2025\":\"Y\",\"goingOutProject\":\"Y\",\"industrialTransProject\":\"Y\",\"specialtyNew\":\"Y\",\"littleGiant\":\"N\",\"creditCode\":\"91370102717833152X\",\"creditLevel\":\"00\",\"creditStartDate\":\"2025-09-10\",\"creditEndDate\":\"2025-04-02\",\"website\":\"www.examplecorporation.com\",\"employeeNumber\":76,\"paidinCapital\":85462.55,\"paidinCapitalCurrency\":\"CNY\",\"relatedTypes\":\"0\"},\"relations\":[{\"id\":\"l5uh3mWCdnWytVNlj3bKx\",\"relation\":\"1\",\"type\":\"1\",\"name\":\"幸鹏\",\"certType\":\"P_01\",\"certId\":\"511423199412260051\",\"birthdate\":\"1999-12-31\",\"gender\":\"1\",\"certStartDate\":\"2024-06-08\",\"certEndDate\":\"2025-07-10\",\"country\":\"CHN\",\"contactPhone\":\"-23747934859\",\"contactFixedPhone\":\"097 8065 9361\",\"contactEmail\":\"gs2k34.j2h27@gmail.com\",\"district\":\"140101\",\"address\":\"山西省 太原市 市辖区 建路16号 6号房间\",\"jobPosition\":\"1\",\"shareholdingRatio\":98.23,\"shareholdingControlling\":\"Y\",\"beneficiaryOwnType\":\"et\",\"beneficiaryOwnStartDate\":\"2025-03-05\",\"beneficiaryOwnEndDate\":\"2025-10-10\",\"beneficiaryPath\":\"XXXX->XXXX\",\"beneficiaryLevel\":\"6\",\"remark\":\"XXXXXX\"}],\"accounts\":[{\"id\":\"d_lVbcayHHCfR-7M4-K_A\",\"accountNo\":\"1234 5678 9012 3456\",\"accountName\":\"投资 Account\",\"accountBank\":\"XX银行\",\"accountBankNo\":\"ullamco dolore dolor eu fugiat\",\"accountBankProvince\":\"北京市\",\"accountBankCity\":\"北京市\"}],\"invoice\":{\"id\":\"FS_PYCfSdWA9jCHloru1K\",\"taxNo\":\"nostrud est in\",\"taxAddress\":\"辽宁省 吉林市 武宁县 尤侬101号 73号门牌\",\"taxPhone\":\"-92437684686\",\"taxBank\":\"ut nisi qui\",\"taxAccountNo\":\"in adipisicing\",\"taxpayerType\":\"1\",\"taxEmail\":\"o97sbt_tw2@21cn.com\",\"taxRemark\":\"exercitation cupidatat ut sint\"},\"directors\":[{\"directorType\":\"01\",\"directorUserName\":\"zhangsan\",\"isHost\":\"Y\"}],\"certPhotos\":[{\"id\":\"QaNiFL1ZD8IUj3VxUbLRI\",\"fileDisplayName\":\"仍雪\",\"fileFullPath\":\"/etc/mail/test.jpg\",\"fileDownloadUrl\":\"https://tender-apparatus.com/\",\"fileSize\":45654654,\"fileType\":\"image/jpeg\",\"fileExtension\":\"pdf\",\"s3FullPath\":\"hxcif/etc/mail/test.jpg\"}]}}",
                custNo, financeNo);
        return respBody;
    }

}
