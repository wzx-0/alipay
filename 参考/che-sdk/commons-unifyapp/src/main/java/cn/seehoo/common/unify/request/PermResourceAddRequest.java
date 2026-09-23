package cn.seehoo.common.unify.request;

import cn.seehoo.common.unify.config.UnifyAppConfig;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;


/**
 * @author sunyf
 * 功能新增同步
 * @date 2025/9/24 下午12:17
 * @since 1.0
 */
public class PermResourceAddRequest {
    public static final String MENU_TYPE="1";
    public static final String BUTTON_TYPE="1";
    /**
     * 功能名称
     */
    private String resourceName;

    /**
     * 功能Id
     */
    private String resourceCode;

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
    private Integer isNeedLogin = 0;

    /**
     * 是否启用
     * 0:未启 1:启用
     */
    private Integer isEnable;

    /**
     * icon文件编码
     */
    private String bgImgFileCode;

    /**
     * icon文件地址
     */
    private String bgImg;

    /**
     * 需求方数据集中生成的id
     */
    private Long sourceId;

    public PermResourceAddRequest() {

    }

    public PermResourceAddRequest(String resourceName, String resourceType, String jumpUrl, String bgImg, Long sourceId, UnifyAppConfig unitAppConfig) {
        this.resourceName = resourceName;
        this.resourceCode = resourceName;
        this.resourceType = resourceType.equals("2") ? PermResourceAddRequest.MENU_TYPE : PermResourceAddRequest.BUTTON_TYPE;
        this.bgImg = bgImg;
        this.sourceId = sourceId;
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

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
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

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
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

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    // 校验参数
    public void checkParams() throws BusinessException {
        ExceptionUtil.check(this);
    }

}
