package cn.seehoo.common.unify.request;

/**
 * @author liuzeng
 * @date 2025/9/24 下午2:35
 * @since 1.0
 */
public class UserRoleUpdateRequest {

    /**
     * 角色来源id
     */
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 子应用字典编码（提前从统一平台获取）
     */
    private String appCode;

    /**
     * 代理公司名称
     */
    private String companyName;

    /**
     * 代理公司社会通用编码
     */
    private String companyCode;

    public UserRoleUpdateRequest() {
    }

    public UserRoleUpdateRequest(String id, String roleName, String companyCode, String companyName) {
        this.id = id;
        this.roleName = roleName;
        this.companyCode = companyCode;
        this.companyName = companyName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
