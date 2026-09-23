package cn.seehoo.common.unify.request;

import java.util.List;

/**
 * @author liuzeng
 * @date 2025/9/24 下午3:31
 * @since 1.0
 */
//@Data
public class UserRoleBindOptRequest {

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 权限列表
     */
    private List<Long> userIdList;

    /**
     * 子应用字典编码（提前从统一平台获取）
     */
    private String appCode;

    private String companyCode;

    public UserRoleBindOptRequest(List<Long> userIdList, String appCode, String companyCode) {
        this.userIdList = userIdList;
        this.appCode = appCode;
        this.companyCode = companyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
