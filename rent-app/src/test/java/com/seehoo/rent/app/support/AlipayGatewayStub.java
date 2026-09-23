package com.seehoo.rent.app.support;

import com.seehoo.rent.sdk.SignUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地支付宝网关模拟（测试夹具，非mock框架）：JDK HttpServer提供的真实HTTP服务。
 * <p>处理逻辑与真实网关一致：解析form→验请求签名→按method组装业务响应→私钥签响应节点→返回；
 * 响应值含唯一序号，避免测试数据撞库表唯一索引。</p>
 */
public class AlipayGatewayStub {

    private final HttpServer server;
    private final KeyPair keyPair;
    private final List<Map<String, String>> requests = new CopyOnWriteArrayList<>();
    private final Map<String, String> failOnce = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong();

    private AlipayGatewayStub(KeyPair keyPair) throws IOException {
        this.keyPair = keyPair;
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/gateway.do", this::handle);
        server.start();
    }

    public static AlipayGatewayStub start(KeyPair keyPair) {
        try {
            return new AlipayGatewayStub(keyPair);
        } catch (IOException e) {
            throw new IllegalStateException("本地支付宝网关启动失败", e);
        }
    }

    public String gatewayUrl() {
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/gateway.do";
    }

    public String publicKeyBase64() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /** 每个测试前清空请求记录与一次性失败配置 */
    public void reset() {
        requests.clear();
        failOnce.clear();
    }

    /** 网关收到的全部请求（form参数），按到达顺序 */
    public List<Map<String, String>> getRequests() {
        return requests;
    }

    /** 网关收到的指定method首个请求的biz_content原文 */
    public String firstBizContent(String method) {
        return requests.stream()
                .filter(r -> method.equals(r.get("method")))
                .map(r -> r.get("biz_content"))
                .findFirst()
                .orElse(null);
    }

    /** 下一次请求返回error_response（模拟支付宝业务拒绝） */
    public void failNextWith(String subCode, String subMsg) {
        failOnce.put("error", subCode + "|" + subMsg);
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
            responseBody = "{\"" + node + "\":" + rawNode + ",\"sign\":\"" + sign + "\"}";
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
        if ("alipay.commerce.rent.order.create".equals(method)) {
            node.append(",\"order_id\":\"OID").append(n).append("\"");
            node.append(",\"path\":\"/pages/detail\"");
        } else if ("alipay.commerce.rent.order.pay".equals(method)) {
            node.append(",\"trade_no\":\"TN").append(n).append("\"");
            node.append(",\"trade_status\":\"TRADE_SUCCESS\"");
        } else if ("alipay.commerce.rent.risk.consult".equals(method)) {
            node.append(",\"risk_level\":\"T3\"");
        } else if ("alipay.commerce.rent.order.sign".equals(method)) {
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
        return SignUtil.rsaCheckContent(content.toString(), sign, publicKeyBase64());
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
