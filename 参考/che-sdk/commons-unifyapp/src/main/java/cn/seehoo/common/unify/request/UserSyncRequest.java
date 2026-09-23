package cn.seehoo.common.unify.request;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author liuzeng
 * @date 2025/9/24 下午4:01
 * @since 1.0
 */
public class UserSyncRequest {
    public static final int ADD = 1;
    public static final int UPDATE = 2;
    public static final int STOP_USE = 6;
    /** 操作类型：1.新增 2.更新 */
    private Integer operatorType;
    /** 用户id */
    private String id;
    /** 用户名称 */
    private String name;
    /** 手机号 */
    private String phone;
    /** 证件号 */
    private String idNo;
    /** 代理公司社会通用编码 */
    private String companyCode;
    /** 代理公司社会通用名称 */
    private String companyName;
    /** 用户状态，1待提交、2审批中、3启用中、4变更待提交、5变更审批中、6已停用 */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // 判断停用
    public boolean checkStopUse() {
        return ObjectUtil.equal(this.status, STOP_USE);
    }
}
