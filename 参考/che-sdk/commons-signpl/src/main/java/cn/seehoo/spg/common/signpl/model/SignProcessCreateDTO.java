package cn.seehoo.spg.common.signpl.model;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.common.signpl.constant.SignPlConstant;

import java.util.List;

/**
 * @author caofei
 * @desc 创建签约流程参数
 * @time 2025/9/25 17:29。
 */
public class SignProcessCreateDTO {
    /**
     * 业务编号（必选）
     */
    private String businessNo;

    /**
     * 签署场景（必选）
     * 字典值：D00176（合同生成环节）
     */
    private String businessScene;

    /**
     * 来源系统代码（可选）
     */
    private String sourceCode;

    /**
     * 接收签署事件回调的URL（可选）
     * 说明：请求类型为POST，请求体为JSON格式
     */
    private String pushUrl;

    /**
     * 签署方信息列表（必选）
     */
    private List<Signer> signers;

    /**
     * 待签署文件信息列表（必选）
     */
    private List<SignFile> files;

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessScene() {
        return businessScene;
    }

    public void setBusinessScene(String businessScene) {
        this.businessScene = businessScene;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public List<Signer> getSigners() {
        return signers;
    }

    public void setSigners(List<Signer> signers) {
        this.signers = signers;
    }

    public List<SignFile> getFiles() {
        return files;
    }

    public void setFiles(List<SignFile> files) {
        this.files = files;
    }

    /**
     * 签署方信息内部类
     */
    public static class Signer {
        /**
         * 签署方名称（必选）
         */
        private String name;

        /**
         * 外部签署人唯一ID（必选）
         */
        private String signerId;

        /**
         * 证件号码（可选，个人签署时必传）
         */
        private String idNo;

        /**
         * 证件类型（可选，个人签署时必传，目前仅支持身份证）
         */
        private String idType;

        /**
         * 是否需要手写签名
         */
        private String handWrittenFlag;

        /**
         * 手机号（可选，个人签署时必传，11位）
         */
        private String phone;

        /**
         * 是否开启视频双录（必选，0否1是）
         */
        private String vdrFlag;

        /**
         * 视频双录参数JSON字符串（可选，vdrFlag=1时必填）
         */
        private String vdrParams;

        /**
         * 签署人类型（必选，1-个人；2-企业）
         */
        private String signerType;

        /**
         * 签署人角色（必选，1-承租人；2-担保人；3-资金方）
         */
        private String signerRole;

        /**
         * 是否自动签署（必选，0否1是）
         */
        private String autoSignFlag = "0";

        /**
         * 企业账号（可选，自动签署时必传）
         */
        private String account;

        /**
         * 签署顺序（必选）
         */
        private int signOrder;

        /**
         * 是否需要上传半身照（可选，0否1是）
         */
        private String halfBodyFlag = "0";

        /**
         * 企业名称(企业认证必传)
         */
        private String enterpriseName;
        /**
         * 统一社会信用代码(企业认证必传)
         */
        private String unifiedSocialCreditCode;
        /**
         * 企业签章fileId
         */
        private String enterpriseSeal;
        /**
         * 实名方式 认证类型 1个人三要素 2银行卡四要素 3刷脸 4双录
         */
        private String authType;

        public String getHandWrittenFlag() {
            return handWrittenFlag;
        }

        public void setHandWrittenFlag(String handWrittenFlag) {
            this.handWrittenFlag = handWrittenFlag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSignerId() {
            return signerId;
        }

        public void setSignerId(String signerId) {
            this.signerId = signerId;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getIdType() {
            return idType;
        }

        public void setIdType(String idType) {
            this.idType = idType;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getVdrFlag() {
            return vdrFlag;
        }

        public void setVdrFlag(String vdrFlag) {
            this.vdrFlag = vdrFlag;
        }

        public String getVdrParams() {
            return vdrParams;
        }

        public void setVdrParams(String vdrParams) {
            this.vdrParams = vdrParams;
        }

        public String getSignerType() {
            return signerType;
        }

        public void setSignerType(String signerType) {
            this.signerType = signerType;
        }

        public String getSignerRole() {
            return signerRole;
        }

        public void setSignerRole(String signerRole) {
            this.signerRole = signerRole;
        }

        public String getAutoSignFlag() {
            return autoSignFlag;
        }

        public void setAutoSignFlag(String autoSignFlag) {
            this.autoSignFlag = autoSignFlag;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getSignOrder() {
            return signOrder;
        }

        public void setSignOrder(int signOrder) {
            this.signOrder = signOrder;
        }

        public String getHalfBodyFlag() {
            return halfBodyFlag;
        }

        public void setHalfBodyFlag(String halfBodyFlag) {
            this.halfBodyFlag = halfBodyFlag;
        }

        public String getEnterpriseName() {
            return enterpriseName;
        }

        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }

        public String getUnifiedSocialCreditCode() {
            return unifiedSocialCreditCode;
        }

        public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
            this.unifiedSocialCreditCode = unifiedSocialCreditCode;
        }

        public String getEnterpriseSeal() {
            return enterpriseSeal;
        }

        public void setEnterpriseSeal(String enterpriseSeal) {
            this.enterpriseSeal = enterpriseSeal;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        /**
         *签署人类型
         * 上游-》映射-》签约平台
         */
        public void signerTypeMappingSign(){
            this.signerType = SignPlConstant.SIGNER_ENTERPRISE.equals(this.signerType)? SignPlConstant.SIGNER_PERSON: SignPlConstant.SIGNER_ENTERPRISE;
        }

        /**
         *签署角色
         * 上游-》映射-》签约平台
         */
        public void roleMappingSign(){
            this.signerRole = SignPlConstant.UP_STREAM_ROLE_FUND.equals(this.signerRole)? SignPlConstant.SIGN_ROLE_FUND:this.signerRole;
        }

        public void signRole() {
            this.idType = "1";
        }
    }

    /**
     * 待签署文件信息内部类
     */
    public static class SignFile {

        /**
         * 文件类型（必选，固定值PDF）
         */
        private String fileType;

        /**
         * 文件顺序（必选）
         */
        private String fileOrder;

        /**
         * 文件唯一标识（必选，用于影像中心获取文件）
         */
        private String fileKey;

        /**
         * 自定义id（可选）
         */
        private String customId;

        /**
         * 文件标题（必选）
         */
        private String fileTitle;

        /**
         * 文件页数（必选）
         */
        private int filePages;
        /**
         * 是否前置确认
         */
        private String isPreConfirmationRequired;

        /**
         * 签署信息列表（必选）
         */
        private List<SignInfo> signInfos;

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileOrder() {
            return fileOrder;
        }

        public void setFileOrder(String fileOrder) {
            this.fileOrder = fileOrder;
        }

        public String getFileKey() {
            return fileKey;
        }

        public void setFileKey(String fileKey) {
            this.fileKey = fileKey;
        }

        public String getCustomId() {
            return customId;
        }

        public void setCustomId(String customId) {
            this.customId = customId;
        }

        public String getFileTitle() {
            return fileTitle;
        }

        public void setFileTitle(String fileTitle) {
            this.fileTitle = fileTitle;
        }

        public int getFilePages() {
            return filePages;
        }

        public void setFilePages(int filePages) {
            this.filePages = filePages;
        }

        public String getIsPreConfirmationRequired() {
            return isPreConfirmationRequired;
        }

        public void setIsPreConfirmationRequired(String isPreConfirmationRequired) {
            this.isPreConfirmationRequired = isPreConfirmationRequired;
        }

        public List<SignInfo> getSignInfos() {
            return signInfos;
        }

        public void setSignInfos(List<SignInfo> signInfos) {
            this.signInfos = signInfos;
        }
    }

    /**
     * 签署位置信息内部类
     */
    public static class SignInfo {
        /**
         * 文件用途（必选，1查看2签署）
         */
        private String filePurpose;

        /**
         * 签署位置关键字（可选，文件用途为签署时必填）
         */
        private String keyword;

        /**
         * 签署人id（客户id）
         */
        private String signerId;

        // 以下为get/set方法
        public String getFilePurpose() {
            return filePurpose;
        }

        public void setFilePurpose(String filePurpose) {
            this.filePurpose = filePurpose;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getSignerId() {
            return signerId;
        }

        public void setSignerId(String signerId) {
            this.signerId = signerId;
        }
    }

    /**
     * 签约平台字典映射
     */
    public void signDictMapping(){
        if(null == this.signers|| CollUtil.isEmpty(this.signers) || StrUtil.isBlank(this.businessScene)){
            return;
        }
        this.businessScene = SignPlConstant.sceneMapping(this.businessScene);
        for (Signer signer : this.signers) {
            signer.signerTypeMappingSign();
            signer.roleMappingSign();
            signer.signRole();
        }
    }
}
