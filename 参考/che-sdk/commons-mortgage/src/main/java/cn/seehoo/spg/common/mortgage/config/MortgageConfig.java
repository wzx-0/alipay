package cn.seehoo.spg.common.mortgage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 抵押中台配置
 */
//@ConfigurationProperties(prefix = "common.mortgage")
public class MortgageConfig {
    private boolean mock;
    private String host;
    private String appid;
    private String appKey;
    private String appSecret;
    private String userName;

    public boolean getMock() {
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
