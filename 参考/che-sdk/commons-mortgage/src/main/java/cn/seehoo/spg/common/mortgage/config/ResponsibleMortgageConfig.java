package cn.seehoo.spg.common.mortgage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 调用有担-抵押中台台客户端配置
 * @author zhangxx
 * @date 2026/3/26 10:53
 */
@ConfigurationProperties(prefix = "common.mortgage")
public class ResponsibleMortgageConfig {
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
