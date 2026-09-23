package com.seehoo.rent.sdk;

import com.seehoo.rent.sdk.client.AlipayRentClient;
import com.seehoo.rent.sdk.client.AlipayRentDefaultClient;
import com.seehoo.rent.sdk.config.AlipayRentConfig;
import com.seehoo.rent.sdk.model.OrderCreateRequest;
import com.seehoo.rent.sdk.model.OrderQueryRequest;
import com.seehoo.rent.sdk.model.RiskConsultRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 网关客户端集成测试：不使用mock框架。
 * 用JDK内置HttpServer在本机模拟支付宝网关（真实HTTP、真实RSA签名/验签、真实表单协议），
 * 客户端经真实网络调用走完整链路：组装公参→签名→POST→解析响应→（响应验签）。
 */
class AlipayRentClientTest {

    private static LocalAlipayGateway gateway;

    @BeforeAll
    static void startGateway() throws IOException {
        gateway = new LocalAlipayGateway();
        gateway.start();
    }

    @AfterAll
    static void stopGateway() {
        gateway.stop();
    }

    private AlipayRentClient newClient(boolean verifySign) {
        return newClient(verifySign,
                Base64.getEncoder().encodeToString(gateway.keyPair.getPrivate().getEncoded()));
    }

    private AlipayRentClient newClient(boolean verifySign, String privateKey) {
        AlipayRentConfig config = new AlipayRentConfig();
        config.setAppId("2021000000000001");
        config.setAppPrivateKey(privateKey);
        config.setAlipayPublicKey(Base64.getEncoder().encodeToString(gateway.keyPair.getPublic().getEncoded()));
        config.setGateway(gateway.gatewayUrl());
        config.setVerifyResponseSign(verifySign);
        return new AlipayRentDefaultClient(config);
    }

    @Test
    void executeShouldAssembleSignedParamsAndParseResponse() {
        gateway.reset();
        OrderQueryRequest request = new OrderQueryRequest();
        request.setOutOrderId("A001");

        RentResponse<Map<String, Object>> resp = newClient(false).orderQuery(request);

        assertTrue(resp.isSuccess());
        assertEquals("10000", resp.getStr("code"));

        // 网关侧收到的请求：公参齐全、签名可被公钥验证、biz_content为snake_case JSON
        Map<String, String> received = gateway.requests.get(0);
        assertEquals("2021000000000001", received.get("app_id"));
        assertEquals(RentConstants.METHOD_ORDER_QUERY, received.get("method"));
        assertEquals("RSA2", received.get("sign_type"));
        assertEquals("1.0", received.get("version"));
        assertNotNull(received.get("timestamp"));
        assertNotNull(received.get("sign"));
        assertTrue(received.get("biz_content").contains("\"out_order_id\":\"A001\""));

        Map<String, String> withoutSign = new TreeMap<>(received);
        String sign = withoutSign.remove("sign");
        StringBuilder content = new StringBuilder();
        withoutSign.forEach((k, v) -> {
            if (content.length() > 0) {
                content.append('&');
            }
            content.append(k).append('=').append(v);
        });
        assertTrue(SignUtil.rsaCheckContent(content.toString(), sign,
                Base64.getEncoder().encodeToString(gateway.keyPair.getPublic().getEncoded())));
    }

    @Test
    void executeShouldThrowOnErrorReponse() {
        gateway.reset();
        gateway.failNextWith("ISV_PERMISSION_DENIED", "权限不足");

        AlipayRentException e = assertThrows(AlipayRentException.class,
                () -> newClient(false).orderQuery(new OrderQueryRequest()));
        assertEquals("40004", e.getCode());
        assertEquals("ISV_PERMISSION_DENIED", e.getSubCode());
    }

    @Test
    void verifyResponseSignShouldPassWithGatewaySignedResponse() {
        gateway.reset();
        // 开启响应验签：本地网关用私钥签响应节点，客户端用公钥验——完整密钥链路
        RentResponse<Map<String, Object>> resp = newClient(true).orderQuery(new OrderQueryRequest());
        assertTrue(resp.isSuccess());
    }

    @Test
    void verifyResponseSignShouldFailWhenNodeTampered() {
        gateway.reset();
        gateway.tamperNextResponse();

        assertThrows(AlipayRentException.class, () -> newClient(true).orderQuery(new OrderQueryRequest()));
    }

    @Test
    void orderCreateShouldFillBizIdentityAndSkipNullFields() {
        gateway.reset();
        OrderCreateRequest request = new OrderCreateRequest();
        request.setOrderType(OrderCreateRequest.OrderType.RENT);
        request.setOutOrderId("A001");

        RentResponse<Map<String, Object>> resp = newClient(false).orderCreate(request);

        assertNotNull(resp.getStr(BizFields.ORDER_ID));
        assertEquals("/pages/detail", resp.getStr(BizFields.PATH));

        String bizContent = gateway.requests.get(0).get("biz_content");
        assertTrue(bizContent.contains("\"biz_identity\":\"CAR_SUBSCRIPTION\""));
        // null字段不参与序列化（与官方非空才传规则一致）
        assertFalse(bizContent.contains("trade_app_id"));
    }

    @Test
    void riskConsultShouldDefaultToComprehensiveRisk() {
        gateway.reset();
        RentResponse<Map<String, Object>> resp = newClient(false).riskConsult(
                new RiskConsultRequest());

        assertEquals("T3", resp.getStr(BizFields.RISK_LEVEL));
        assertTrue(gateway.requests.get(0).get("biz_content").contains("COMPREHENSIVE_RISK"));
    }

    @Test
    void notifyUrlShouldBeIncludedWhenProvided() {
        gateway.reset();
        newClient(false).execute(RentConstants.METHOD_ORDER_QUERY, null, "https://shop.example.com/notify");
        assertEquals("https://shop.example.com/notify", gateway.requests.get(0).get("notify_url"));
    }

    @Test
    void gatewayShouldRejectRequestSignedByWrongKey() {
        gateway.reset();
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            String wrongPrivateKey = Base64.getEncoder()
                    .encodeToString(generator.generateKeyPair().getPrivate().getEncoded());
            AlipayRentException e = assertThrows(AlipayRentException.class,
                    () -> newClient(false, wrongPrivateKey).orderQuery(new OrderQueryRequest()));
            assertEquals("SIGN_INVALID", e.getSubCode());
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * 本地支付宝网关模拟（测试夹具，非mock）：JDK HttpServer提供的真实HTTP服务。
     * 处理逻辑：解析form→验请求签名→按method组装业务响应→私钥签响应节点→返回。
     */
    static class LocalAlipayGateway {

        final KeyPair keyPair;
        HttpServer server;
        final List<Map<String, String>> requests = new CopyOnWriteArrayList<>();
        final Map<String, String> failOnce = new ConcurrentHashMap<>();
        volatile boolean tamperOnce = false;
        final AtomicLong seq = new AtomicLong();

        LocalAlipayGateway() {
            try {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                keyPair = generator.generateKeyPair();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        void start() throws IOException {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext("/gateway.do", this::handle);
            server.start();
        }

        void stop() {
            if (server != null) {
                server.stop(0);
            }
        }

        String gatewayUrl() {
            return "http://127.0.0.1:" + server.getAddress().getPort() + "/gateway.do";
        }

        void reset() {
            requests.clear();
            failOnce.clear();
            tamperOnce = false;
        }

        /** 下一次请求返回error_response（模拟支付宝业务拒绝） */
        void failNextWith(String subCode, String subMsg) {
            failOnce.put("error", subCode + "|" + subMsg);
        }

        /** 下一次响应内容与签名不匹配（模拟报文被篡改） */
        void tamperNextResponse() {
            tamperOnce = true;
        }

        private void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseForm(
                    new String(readAll(exchange.getRequestBody()), StandardCharsets.UTF_8));
            requests.add(params);

            String method = params.get("method");
            String node = method.replace('.', '_') + "_response";
            String responseBody;
            String error = failOnce.remove("error");
            // 网关侧请求验签（与支付宝一致）：签名错误返回error_response
            if (error == null && !verifyRequest(params)) {
                error = "SIGN_INVALID|验签失败";
            }
            if (error != null) {
                String[] parts = error.split("\\|", 2);
                responseBody = "{\"error_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\""
                        + ",\"sub_code\":\"" + parts[0] + "\",\"sub_msg\":\"" + parts[1] + "\"}}";
            } else {
                String rawNode = buildRawNode(method);
                String sign = signRaw(rawNode);
                String nodeInBody = rawNode;
                if (tamperOnce) {
                    // 先对原文签名，再篡改正文（模拟响应被中间人篡改）
                    tamperOnce = false;
                    nodeInBody = rawNode.replace("10000", "10001");
                }
                responseBody = "{\"" + node + "\":" + nodeInBody + ",\"sign\":\"" + sign + "\"}";
            }
            byte[] out = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
            exchange.sendResponseHeaders(200, out.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(out);
            }
        }

        /** 按method组装业务响应节点（值含唯一序号，避免唯一索引冲突） */
        private String buildRawNode(String method) {
            long n = seq.incrementAndGet();
            StringBuilder node = new StringBuilder("{\"code\":\"10000\",\"msg\":\"Success\"");
            if (RentConstants.METHOD_ORDER_CREATE.equals(method)) {
                node.append(",\"order_id\":\"OID").append(n).append("\"");
                node.append(",\"path\":\"/pages/detail\"");
            } else if (RentConstants.METHOD_ORDER_PAY.equals(method)) {
                node.append(",\"trade_no\":\"TN").append(n).append("\"");
                node.append(",\"trade_status\":\"TRADE_SUCCESS\"");
            } else if (RentConstants.METHOD_RISK_CONSULT.equals(method)) {
                node.append(",\"risk_level\":\"T3\"");
            } else if (RentConstants.METHOD_ORDER_SIGN.equals(method)) {
                node.append(",\"sign_str\":\"SS").append(n).append("\"");
                node.append(",\"sign_launch_method\":\"LAUNCH_1\"");
            }
            node.append("}");
            return node.toString();
        }

        /** 网关侧请求验签：排除sign按key升序拼原文，RSA2公钥验证 */
        private boolean verifyRequest(Map<String, String> params) {
            String sign = params.get("sign");
            if (sign == null) {
                return false;
            }
            StringBuilder content = new StringBuilder();
            new TreeMap<>(params).forEach((k, v) -> {
                if ("sign".equals(k) || v == null || v.isEmpty()) {
                    return;
                }
                if (content.length() > 0) {
                    content.append('&');
                }
                content.append(k).append('=').append(v);
            });
            return SignUtil.rsaCheckContent(content.toString(), sign,
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        }

        private String signRaw(String content) {
            try {
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initSign(keyPair.getPrivate());
                signature.update(content.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(signature.sign());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private Map<String, String> parseForm(String form) throws IOException {
            Map<String, String> params = new HashMap<>();
            for (String pair : form.split("&")) {
                if (pair.isEmpty()) {
                    continue;
                }
                int eq = pair.indexOf('=');
                String key = eq < 0 ? pair : pair.substring(0, eq);
                String value = eq < 0 ? "" : pair.substring(eq + 1);
                params.put(URLDecoder.decode(key, "UTF-8"),
                        URLDecoder.decode(value, "UTF-8"));
            }
            return params;
        }

        private byte[] readAll(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        }
    }
}
