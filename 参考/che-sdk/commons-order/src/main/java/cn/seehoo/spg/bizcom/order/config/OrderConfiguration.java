package cn.seehoo.spg.bizcom.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common.order")
public class OrderConfiguration {
    /** mock开关 */
    private boolean mock;
    /** 请求地址 */
    private String host;

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
}
