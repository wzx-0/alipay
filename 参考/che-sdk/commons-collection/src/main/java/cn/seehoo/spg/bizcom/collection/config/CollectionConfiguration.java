package cn.seehoo.spg.bizcom.collection.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangxx
 * @date 2026/1/8 9:16
 */
@ConfigurationProperties(prefix = "common.collection")
public class CollectionConfiguration {
    /** mock开关 */
    private boolean mock;
    /** 请求地址 */
    private String transCode;
    /** 车信息域名 */
    private String host;

    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public boolean isMock() {
        return mock;
    }
    public void setMock(boolean mock) {
        this.mock = mock;
    }
    public String getTransCode() {
        return transCode;
    }
    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }
    public boolean checkMock() {
        return mock;
    }
}
