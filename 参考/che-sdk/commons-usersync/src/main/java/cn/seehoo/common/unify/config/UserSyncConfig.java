package cn.seehoo.common.unify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author sunyf
 * 统一app配置
 * @date 2025/9/24 上午11:29
 * @since 1.0
 */
@RefreshScope
@Component
@ConfigurationProperties(prefix = "common.usersync")
public class UserSyncConfig {
    /** 是否mock */
    private boolean mock;
    /** host */
    private String host;
    /** 获取token */
    private String getTokenPath;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 应用id */
    private String appid;
    /** 用户信息同步路径 */
    private String syncUserInfoPath;
    /** 部门信息同步路径 */
    private String syncDepartmentPath;

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

    public String getSyncUserInfoPath() {
        return syncUserInfoPath;
    }

    public void setSyncUserInfoPath(String syncUserInfoPath) {
        this.syncUserInfoPath = syncUserInfoPath;
    }

    public String getSyncDepartmentPath() {
        return syncDepartmentPath;
    }

    public void setSyncDepartmentPath(String syncDepartmentPath) {
        this.syncDepartmentPath = syncDepartmentPath;
    }
}
