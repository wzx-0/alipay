package cn.seehoo.spg.common.signpl.config;

/**
 * @author caofei
 * @desc
 * @time 2025/9/25 17:06。
 */
public class SignPlConfig {
    private boolean mock;
    private String host;
    private String authType;
    private String pushUrl;

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

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }
}
