package cn.seehoo.common.unify.request;

import cn.seehoo.common.unify.config.UnifyAppConfig;

/**
 * @author liuzeng
 * @date 2025/9/24 下午1:51
 * @since 1.0
 */
public class PermResourceUpdateRequest {

    /**
     * 功能名称
     */
    private String resourceName;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 资源类型
     * 1.菜单 2.按钮
     */
    private String resourceType;

    /**
     * 子应用字典编码
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
     * 跳转类型
     * 1.H5 2.子应用 3.DeepLink(内部) 4.DeepLink(外部) 5.微信小程序
     */
    private Integer jumpType = 2;

    /**
     * 跳转地址
     */
    private String jumpUrl;

    /**
     * 是否登录限制
     * 0:不限 1:限制
     */
    private Integer isNeedLogin;

    /**
     * 是否启用
     * 0:失效 1:启用
     */
    private Integer isEnabled;

    /**
     * 文件编码（背景图/图标）
     */
    private String bgImgFileCode;

    /**
     * icon图标地址
     */
    private String bgImg;

    public PermResourceUpdateRequest(String resourceName, Long id, String jumpUrl, String bgImg, UnifyAppConfig unitAppConfig) {
        this.resourceName = resourceName;
        this.id = id;
        this.bgImg = bgImg;
        this.jumpUrl = unitAppConfig.getJumpUrlPrefix() + jumpUrl;
        this.appCode = unitAppConfig.getAppCode();
        this.appName = unitAppConfig.getAppName();
        this.appBizCode = unitAppConfig.getAppBizCode();
        this.appBizName = unitAppConfig.getAppBizName();
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

    public Integer getJumpType() {
        return jumpType;
    }

    public void setJumpType(Integer jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public Integer getIsNeedLogin() {
        return isNeedLogin;
    }

    public void setIsNeedLogin(Integer isNeedLogin) {
        this.isNeedLogin = isNeedLogin;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getBgImgFileCode() {
        return bgImgFileCode;
    }

    public void setBgImgFileCode(String bgImgFileCode) {
        this.bgImgFileCode = bgImgFileCode;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }
}
