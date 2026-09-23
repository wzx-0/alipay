package cn.seehoo.spg.bizcom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 工作助手配置
 *  xiaolin
 * */
@ConfigurationProperties(prefix = "common.wac")
public class WorkAssistantConfig {
    /** private key */
    private String privateKey;
    /** appKey */
    private String appKey;
    /** label */
    private String label;
    /** url */
    private String url;
    /** app打开方式 */
    private String action;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
