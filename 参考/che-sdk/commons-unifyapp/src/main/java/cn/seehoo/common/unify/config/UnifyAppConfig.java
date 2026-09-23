package cn.seehoo.common.unify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sunyf
 * 统一app配置
 * @date 2025/9/24 上午11:29
 * @since 1.0
 */
@RefreshScope
@Component
@ConfigurationProperties(prefix = "common.unifyapp")
public class UnifyAppConfig {
    /** 是否mock */
    private boolean mock;
    /** host */
    private String host;
    /** host */
    private String authHost;
    /** 功能新增路径 */
    private String resourceAddPath;
    /** 功能修改路径 */
    private String resourceUpdatePath;
    /** 功能查询路径 */
    private String resourceSelectPath;
    /** 功能删除路径 */
    private String resourceDeletePath;
    /** 角色基本信息创建同步路径 */
    private String userRoleAddPath;
    /** 角色基本信息修改同步路径 */
    private String userRoleUpdatePath;
    /** 角色基本信息删除同步路径 */
    private String userRoleDeletePath;
    /** 角色基本信息查询路径 */
    private String userRoleSelectPath;
    /** 角色权限信息查询路径 */
    private String userRolePermissionSelectPath;
    /** 角色关联权限同步路径 */
    private String userRolePermissionGrantPath;
    /** 用户解绑角色同步路径 */
    private String userRoleUnbindPath;
    /** 用户绑定角色同步路径 */
    private String userRoleBindPath;
    /** 车辆用户信息同步路径 */
    private String carUserInfoSyncPath;
    /** 子应用用户移除路径 */
    private String subAppUserRemovePath;
    /** 获取当前登录用户信息路径 */
    private String getCurUserPath;
    /** 获取当前登录用户信息设备列表 */
    private String queryDevicesPath;

    /**
     * 立即发送消息
     */
    private String immediatelySendPath;

    /** 获取token */
    private String getTokenPath;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 应用id */
    private String appid;
    /** 用户名 */
    private String clientId;
    /** 密码 */
    private String clientSecrent;
    /** 应用id */
    private String grantType;
    /** appCode */
    private String appCode;
    /** appName */
    private String appName;
    /** appBizCode */
    private String appBizCode;
    /** appBizName */
    private String appBizName;
    /** 最大设备数量 */
    private int maxDeviceNum;
    /** 调整路径前缀 */
    private String jumpUrlPrefix;
    /** 同步的菜单Id列表 */
    private List<Long> functionIds;

    public String getImmediatelySendPath() {
        return immediatelySendPath;
    }

    public void setImmediatelySendPath(String immediatelySendPath) {
        this.immediatelySendPath = immediatelySendPath;
    }

    public List<Long> getFunctionIds() {
        return functionIds;
    }
    public void setFunctionIds(List<Long> functionIds) {
        this.functionIds = functionIds;
    }
    public String getJumpUrlPrefix() {
        return jumpUrlPrefix;
    }
    public void setJumpUrlPrefix(String jumpUrlPrefix) {
        this.jumpUrlPrefix = jumpUrlPrefix;
    }
    public int getMaxDeviceNum() {
        return maxDeviceNum;
    }
    public void setMaxDeviceNum(int maxDeviceNum) {
        this.maxDeviceNum = maxDeviceNum;
    }
    public String getQueryDevicesPath() {
        return queryDevicesPath;
    }
    public void setQueryDevicesPath(String queryDevicesPath) {
        this.queryDevicesPath = queryDevicesPath;
    }
    public String getAuthHost() {
        return authHost;
    }
    public void setAuthHost(String authHost) {
        this.authHost = authHost;
    }
    public String getGetCurUserPath() {
        return getCurUserPath;
    }
    public void setGetCurUserPath(String getCurUserPath) {
        this.getCurUserPath = getCurUserPath;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getClientSecrent() {
        return clientSecrent;
    }
    public void setClientSecrent(String clientSecrent) {
        this.clientSecrent = clientSecrent;
    }
    public String getGrantType() {
        return grantType;
    }
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
    public boolean isMock() {
        return mock;
    }
    public void setMock(boolean mock) {
        this.mock = mock;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getResourceAddPath() {
        return resourceAddPath;
    }
    public void setResourceAddPath(String resourceAddPath) {
        this.resourceAddPath = resourceAddPath;
    }
    public String getResourceUpdatePath() {
        return resourceUpdatePath;
    }
    public void setResourceUpdatePath(String resourceUpdatePath) {
        this.resourceUpdatePath = resourceUpdatePath;
    }
    public String getResourceSelectPath() {
        return resourceSelectPath;
    }
    public void setResourceSelectPath(String resourceSelectPath) {
        this.resourceSelectPath = resourceSelectPath;
    }
    public String getResourceDeletePath() {
        return resourceDeletePath;
    }
    public void setResourceDeletePath(String resourceDeletePath) {
        this.resourceDeletePath = resourceDeletePath;
    }
    public String getUserRoleAddPath() {
        return userRoleAddPath;
    }
    public void setUserRoleAddPath(String userRoleAddPath) {
        this.userRoleAddPath = userRoleAddPath;
    }
    public String getUserRoleUpdatePath() {
        return userRoleUpdatePath;
    }
    public void setUserRoleUpdatePath(String userRoleUpdatePath) {
        this.userRoleUpdatePath = userRoleUpdatePath;
    }
    public String getUserRoleDeletePath() {
        return userRoleDeletePath;
    }
    public void setUserRoleDeletePath(String userRoleDeletePath) {
        this.userRoleDeletePath = userRoleDeletePath;
    }
    public String getUserRoleSelectPath() {
        return userRoleSelectPath;
    }
    public void setUserRoleSelectPath(String userRoleSelectPath) {
        this.userRoleSelectPath = userRoleSelectPath;
    }
    public String getUserRolePermissionSelectPath() {
        return userRolePermissionSelectPath;
    }
    public void setUserRolePermissionSelectPath(String userRolePermissionSelectPath) {
        this.userRolePermissionSelectPath = userRolePermissionSelectPath;
    }
    public String getUserRolePermissionGrantPath() {
        return userRolePermissionGrantPath;
    }
    public void setUserRolePermissionGrantPath(String userRolePermissionGrantPath) {
        this.userRolePermissionGrantPath = userRolePermissionGrantPath;
    }
    public String getUserRoleUnbindPath() {
        return userRoleUnbindPath;
    }
    public void setUserRoleUnbindPath(String userRoleUnbindPath) {
        this.userRoleUnbindPath = userRoleUnbindPath;
    }
    public String getUserRoleBindPath() {
        return userRoleBindPath;
    }
    public void setUserRoleBindPath(String userRoleBindPath) {
        this.userRoleBindPath = userRoleBindPath;
    }
    public String getCarUserInfoSyncPath() {
        return carUserInfoSyncPath;
    }
    public void setCarUserInfoSyncPath(String carUserInfoSyncPath) {
        this.carUserInfoSyncPath = carUserInfoSyncPath;
    }
    public String getSubAppUserRemovePath() {
        return subAppUserRemovePath;
    }
    public void setSubAppUserRemovePath(String subAppUserRemovePath) {
        this.subAppUserRemovePath = subAppUserRemovePath;
    }
    public String getGetTokenPath() {
        return getTokenPath;
    }
    public void setGetTokenPath(String getTokenPath) {
        this.getTokenPath = getTokenPath;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getAppCode() {
        return appCode;
    }
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
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

    // 判断是否mock
    public boolean checkMock() {
        return mock;
    }
}
