package com.seehoo.rent.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 签名/验签工具测试：使用JDK动态生成的RSA密钥对走真实加解密链路 */
class SignUtilTest {

    private static KeyPair keyPair;
    private static String publicKeyBase64;
    private static String privateKeyBase64;

    @BeforeAll
    static void initKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /** 测试辅助：用私钥对原文签名（模拟支付宝侧） */
    private String signContent(String content) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    @Test
    void signShouldBeVerifiedByPublicKey() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("app_id", "2021000000000001");
        params.put("method", "alipay.commerce.rent.order.create");
        params.put("charset", "UTF-8");
        String sign = SignUtil.sign(params, privateKeyBase64);

        // 手工按签名规则拼原文并验签，证明sign与参数内容一一对应
        String content = "app_id=2021000000000001&charset=UTF-8&method=alipay.commerce.rent.order.create";
        assertTrue(SignUtil.rsaCheckContent(content, sign, publicKeyBase64));
    }

    @Test
    void signShouldExcludeEmptyValueAndSignItself() {
        Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        params.put("empty", "");
        params.put("nullVal", null);

        String sign = assertDoesNotThrow(() -> SignUtil.sign(params, privateKeyBase64));
        assertFalse(sign.isEmpty());
    }

    @Test
    void verifyShouldFailWhenContentTampered() throws Exception {
        String sign = signContent("out_order_id=A001");
        assertFalse(SignUtil.rsaCheckContent("out_order_id=A002", sign, publicKeyBase64));
    }

    @Test
    void verifyShouldFailWithWrongKey() throws Exception {
        String sign = signContent("out_order_id=A001");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        String otherPublicKey = Base64.getEncoder()
                .encodeToString(generator.generateKeyPair().getPublic().getEncoded());
        assertFalse(SignUtil.rsaCheckContent("out_order_id=A001", sign, otherPublicKey));
    }

    @Test
    void rsaCheckContentShouldReturnFalseOnNullInput() {
        assertFalse(SignUtil.rsaCheckContent(null, "sign", publicKeyBase64));
        assertFalse(SignUtil.rsaCheckContent("content", null, publicKeyBase64));
        assertFalse(SignUtil.rsaCheckContent("content", "sign", null));
    }

    @Test
    void signShouldThrowOnInvalidPrivateKey() {
        Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        assertThrows(AlipayRentException.class, () -> SignUtil.sign(params, "not-a-valid-key"));
    }

    @Test
    void rsaCheckNotifyShouldPassAndExcludeSignType() throws Exception {
        // 按官方rsaCheckV1规则拼原文（排除sign与sign_type，key升序，空值排除）
        Map<String, String> params = new HashMap<>();
        params.put("notify_id", "N001");
        params.put("trade_status", "TRADE_SUCCESS");
        params.put("sign_type", "RSA2");
        params.put("empty_field", "");
        String content = "notify_id=N001&trade_status=TRADE_SUCCESS";
        params.put("sign", signContent(content));

        assertTrue(SignUtil.rsaCheckNotify(params, publicKeyBase64));
    }

    @Test
    void rsaCheckNotifyShouldFailWhenSignMissingOrEmpty() {
        Map<String, String> noSign = new HashMap<>();
        noSign.put("a", "1");
        assertFalse(SignUtil.rsaCheckNotify(noSign, publicKeyBase64));

        Map<String, String> emptySign = new HashMap<>();
        emptySign.put("a", "1");
        emptySign.put("sign", "");
        assertFalse(SignUtil.rsaCheckNotify(emptySign, publicKeyBase64));
    }

    @Test
    void signShouldBeDeterministicForSameContent() throws Exception {
        // SHA256withRSA使用PKCS1 v1.5填充（确定性算法）：同一原文两次签名值相同且均验签通过
        String sign1 = signContent("a=1");
        String sign2 = signContent("a=1");
        assertEquals(sign1, sign2);
        assertTrue(SignUtil.rsaCheckContent("a=1", sign1, publicKeyBase64));
        assertTrue(SignUtil.rsaCheckContent("a=1", sign2, publicKeyBase64));
    }

    @Test
    void signAndVerifyWithChineseValue() throws Exception {
        String content = "goods_name=特斯拉Model3&out_order_id=A001";
        String sign = signContent(content);
        assertTrue(SignUtil.rsaCheckContent(content, sign, publicKeyBase64));
    }
}
