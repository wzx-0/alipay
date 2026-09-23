package com.seehoo.rent.sdk;

import lombok.Builder;
import lombok.Data;

/**
 * 支付宝汽车订阅SDK配置（公钥模式，非证书模式）
 * <p>密钥在支付宝开放平台「接口加签方式」选择公钥模式后获取</p>
 */
@Data
@Builder
public class AlipayRentConfig {

    /** 正式网关 */
    public static final String GATEWAY_PROD = "https://openapi.alipay.com/gateway.do";

    /** 开放平台应用APPID */
    private String appId;

    /** 收单主体APPID，异主体场景创单时传 trade_app_id，与appId一致则无需配置 */
    private String tradeAppId;

    /** 应用私钥（PKCS8，一行Base64，不含头尾标记） */
    private String appPrivateKey;

    /** 支付宝公钥（公钥模式） */
    private String alipayPublicKey;

    /** 网关地址，默认正式环境 */
    @Builder.Default
    private String gateway = GATEWAY_PROD;

    /** 签名类型，固定RSA2 */
    @Builder.Default
    private String signType = "RSA2";

    /** 编码 */
    @Builder.Default
    private String charset = "UTF-8";

    /** 报文格式 */
    @Builder.Default
    private String format = "json";

    /** 网关版本 */
    @Builder.Default
    private String apiVersion = "1.0";

    /** 是否验支付宝响应签名（联调期可关闭，生产建议开启） */
    @Builder.Default
    private boolean verifyResponseSign = false;
}
