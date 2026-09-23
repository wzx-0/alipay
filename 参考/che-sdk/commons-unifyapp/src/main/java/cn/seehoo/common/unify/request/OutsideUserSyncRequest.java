package cn.seehoo.common.unify.request;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author liuzeng
 * @date 2025/9/24 下午4:01
 * @since 1.0
 */
public class OutsideUserSyncRequest {
    public static final int ENABLE_USE = 1;
    public static final int STOP_USE = 0;
    public static final String BUSINESS_USER = "1";
    public static final int ADD = 1;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 用户id(子应用生成的id,用于跟统一平台绑定数据关系)
     */
    private String sourceId;


    /**
     * 账户类型码值：运营人员、销售人员、管理人员
     */
    private Integer accountType;

    /**
     * 性别 0-女, 1-男, 9-未知
     */
    private String gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 是否注销 (0: 否, 1: 是)
     */
    private Integer isLogOff = 0;

    /**
     * 子应用名称
     */
    private String appCode;

    /**
     * 启用状态: 1-启用, 0-禁用
     */
    private Integer isEnable = ENABLE_USE;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 民族
     */
    private String nation;

    /**
     * 婚姻状况
     */
    private String marriageStatus;

    /**
     * 操作类型：1.新增 2.更新
     */
    private Integer operatorType;

    /**
     * appBizCodes
     */
    private String appBizCodes;
    /**
     * 代理公司社会通用编码
     */
    private String companyCode;
    /**
     * 代理公司社会通用名称
     */
    private String companyName;

    /**
     * 1.B端用户 2.C端用户
     */
    private String userPlatformType = BUSINESS_USER;

    public String getUserPlatformType() {
        return userPlatformType;
    }

    public void setUserPlatformType(String userPlatformType) {
        this.userPlatformType = userPlatformType;
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
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getIsLogOff() {
        return isLogOff;
    }

    public void setIsLogOff(Integer isLogOff) {
        this.isLogOff = isLogOff;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    public String getAppBizCodes() {
        return appBizCodes;
    }

    public void setAppBizCodes(String appBizCodes) {
        this.appBizCodes = appBizCodes;
    }

    // 判断新增
    public boolean checkOperatorAdd() {
        return ObjectUtil.equal(this.operatorType, ADD);
    }
}
