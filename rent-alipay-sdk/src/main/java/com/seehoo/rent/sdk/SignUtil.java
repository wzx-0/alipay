package com.seehoo.rent.sdk;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * 支付宝签名/验签工具
 * <p>签名规则（RSA2）：请求参数不含sign且非空，按key升序拼 key=value 用 & 连接，值不做URL转义，
 * SHA256withRSA 私钥签名后 Base64；异步通知验签额外排除 sign_type。</p>
 */
public final class SignUtil {

    private static final String KEY_SIGN = "sign";
    private static final String KEY_SIGN_TYPE = "sign_type";
    private static final String RSA2 = "SHA256withRSA";
    private static final String KEY_FACTORY_ALG = "RSA";
    private static final char AMP = '&';
    private static final char EQ = '=';

    private SignUtil() {
    }

    /**
     * 请求签名：对参数表生成sign值
     */
    public static String sign(Map<String, ?> params, String privateKey) {
        String content = buildSignContent(params, false);
        try {
            PrivateKey priKey = KeyFactory.getInstance(KEY_FACTORY_ALG)
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
            java.security.Signature signature = java.security.Signature.getInstance(RSA2);
            signature.initSign(priKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new AlipayRentException("支付宝请求签名失败", e);
        }
    }

    /**
     * 异步通知验签（规则同官方rsaCheckV1：排除sign与sign_type）
     *
     * @param params 通知全部form参数
     * @return true=验签通过
     */
    public static boolean rsaCheckNotify(Map<String, ?> params, String alipayPublicKey) {
        Object signVal = params.get(KEY_SIGN);
        String sign = signVal == null ? null : String.valueOf(signVal);
        if (sign == null || sign.isEmpty()) {
            return false;
        }
        return rsaCheckContent(buildSignContent(params, true), sign, alipayPublicKey);
    }

    /**
     * 内容原文验签：异步通知验签复用；响应验签时原文即响应节点JSON串本身
     */
    public static boolean rsaCheckContent(String content, String sign, String alipayPublicKey) {
        if (content == null || sign == null || alipayPublicKey == null) {
            return false;
        }
        try {
            PublicKey pubKey = KeyFactory.getInstance(KEY_FACTORY_ALG)
                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(alipayPublicKey)));
            java.security.Signature signature = java.security.Signature.getInstance(RSA2);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 拼签名原文：key升序、排除sign及空值；验签场景再排除sign_type
     */
    private static String buildSignContent(Map<String, ?> params, boolean excludeSignType) {
        StringBuilder sb = new StringBuilder();
        new TreeMap<>(params).forEach((key, value) -> {
            String val = value == null ? null : String.valueOf(value);
            if (KEY_SIGN.equals(key) || val == null || val.isEmpty()) {
                return;
            }
            if (excludeSignType && KEY_SIGN_TYPE.equals(key)) {
                return;
            }
            if (sb.length() > 0) {
                sb.append(AMP);
            }
            sb.append(key).append(EQ).append(val);
        });
        return sb.toString();
    }
}
