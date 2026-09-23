package cn.seehoo.spg.common.bhrc.config;

import lombok.Data;

/**
 * @author caofei
 * @desc 华夏百行征信配置
 * @time 2025/9/24 17:10。
 */
@Data
public class HxBhrcPhoneConfig {
    /** true 开启、false 关闭*/
    private boolean mock;
    /** 模拟数据 */
    private String mockText;
    /**
     * 手机三要素核验接口地址
     */
    private String authUrl;
    /**
     * 银行卡四要素核验接口地址
     */
    private String authUrlBankCard;
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
     * 华夏网关参数
     */
    private String hxHost;
    private String reqSysId;
    private String transCode;
    private String scnNo;
    private String scnNoBankCard;
    private String svcNo;
}
