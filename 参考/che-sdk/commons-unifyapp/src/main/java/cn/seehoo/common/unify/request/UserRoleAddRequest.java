package cn.seehoo.common.unify.request;

import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;

/**
 * @author liuzeng
 * @date 2025/9/24 下午2:33
 * @since 1.0
 */
public class UserRoleAddRequest {
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 子应用字典编码（提前从统一平台获取）
     */
    private String appCode;

    /**
     * 业务域编码
     */
    private String appBizCode;

    /**
     * 业务域名称
     */
    private String appBizName;

    /**
     * 子应用名称
     */
    private String appName;

    /**
     * 子应用roleId，用于统一平台数据关联
     */
    private String sourceId;

    /**
     * 代理公司名称
     */
    private String companyName;

    /**
     * 代理公司社会通用编码
     */
    private String companyCode;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppBizCode() {
        return appBizCode;
    }

    public void setAppBizCode(String appBizCode) {
        this.appBizCode = appBizCode;
    }

    public String getAppBizName() {
        return appBizName;
    }

    public void setAppBizName(String appBizName) {
        this.appBizName = appBizName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

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

    public UserRoleAddRequest() {
    }

    public UserRoleAddRequest(String sourceId, String roleName, String companyCode, String companyName) {
        this.sourceId = sourceId;
        this.roleName = roleName;
        this.companyCode = companyCode;
        this.companyName = companyName;
    }

    // 校验参数
    public void checkParams() throws BusinessException {
        ExceptionUtil.check(this);
    }
}
