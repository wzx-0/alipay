package cn.seehoo.spg.common.bhrc.config;
/**
 * @author caofei
 * @desc 华夏百行征信配置
 * @time 2025/9/24 17:10。
 */
public class HxBhrcConfig {
    /** true 开启、false 关闭*/
    private boolean mock;
    /** 域名/ip */
    private String host;
    /**
     * 公安四要素认证接口地址
     * 公安四要素认证的英文是 Public Security Four-Element Authentication 或 PSFEA。
     */
    private String psfeAuthUrl;
    /**
     * 密钥（百行提供）：
     * 加密方式-对称 3DES
     * requestRefId说明：请求唯一标识id (客户端自己生成)
     * 签名源：requestRefId={requestRefId}&secretKey={secretKey}
     * 加签方式: HMAC-SHA1 使用Base64 对 secretKey 解码得到密钥，再使用HMAC-SHA1 算法对签名源进行签名，得到签名结果。
     */
    private String secretKey;
    /** 客户端身份标识（百行提供）*/
    private String secretId;
    /**
     * 加密方式
     */
    private String encryptType;
    /**
     * 华夏网关参数
     */
    private String huxHost;
    private String reqSysId;
    private String transCode;
    private String scnNo;
    private String svcNo;

    public String getReqSysId() {
        return reqSysId;
    }

    public void setReqSysId(String reqSysId) {
        this.reqSysId = reqSysId;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getScnNo() {
        return scnNo;
    }

    public void setScnNo(String scnNo) {
        this.scnNo = scnNo;
    }

    public String getSvcNo() {
        return svcNo;
    }

    public void setSvcNo(String svcNo) {
        this.svcNo = svcNo;
    }

    public String getHuxHost() {
        return huxHost;
    }

    public void setHuxHost(String huxHost) {
        this.huxHost = huxHost;
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

    public String getPsfeAuthUrl() {
        return psfeAuthUrl;
    }

    public void setPsfeAuthUrl(String psfeAuthUrl) {
        this.psfeAuthUrl = psfeAuthUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }
}
