package cn.seehoo.common.elecheck.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sunyf
 * @date 2025/9/20 下午5:11
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "common.elecheck")
public class EleCheckConfig {

    /** 是否mock */
    private boolean mock;
    /**
     * 生产服务地址
     */
    private String defaultDomain;
    /**
     * url路径
     */
    private Map<String, String> remoteUrls;
    /**
     * 流程编号
     */
    private Map<String, String> workFlowIds;
    /** 路径 */
    private String verificationApi;

    private String apiCode;
    private String appKey;

    public String getWorkFlowId(String apiName) {
        return workFlowIds.get(apiName);
    }

    public String getVerificationApi() {
        return verificationApi;
    }
    public void setVerificationApi(String verificationApi) {
        this.verificationApi = verificationApi;
    }
    public boolean isMock() {
        return mock;
    }
    public void setMock(boolean mock) {
        this.mock = mock;
    }
    public String getDefaultDomain() {
        return defaultDomain;
    }
    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }
    public Map<String, String> getRemoteUrls() {
        return remoteUrls;
    }
    public void setRemoteUrls(Map<String, String> remoteUrls) {
        this.remoteUrls = remoteUrls;
    }
    public Map<String, String> getWorkFlowIds() {
        return workFlowIds;
    }
    public void setWorkFlowIds(Map<String, String> workFlowIds) {
        this.workFlowIds = workFlowIds;
    }
    public String getApiCode() {
        return apiCode;
    }
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }
    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
