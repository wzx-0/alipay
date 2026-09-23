package com.seehoo.rent.app;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.support.TestAlipayEnv;
import com.seehoo.rent.sdk.SignUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HTTP全链路集成测试：@SpringBootTest启动真实Tomcat（随机端口），
 * TestRestTemplate发起真实HTTP请求走Controller→Service→Mapper→本地支付宝网关。
 * 服务端事务独立提交，结束后按记录的订单号物理清理测试数据。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentAppHttpIntegrationTest {

    @DynamicPropertySource
    static void alipayEnv(DynamicPropertyRegistry registry) {
        TestAlipayEnv.register(registry);
    }

    @Autowired
    private TestRestTemplate rest;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** 本测试创建的订单号，清理用 */
    private final List<String> createdOutOrderIds = new ArrayList<>();
    private final List<String> createdNotifyIds = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (String outOrderId : createdOutOrderIds) {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT id FROM tb_rent_order WHERE out_order_id = ?", outOrderId);
            if (rows.isEmpty()) {
                continue;
            }
            Long orderId = ((Number) rows.get(0).get("id")).longValue();
            jdbcTemplate.update(
                    "DELETE pi FROM tb_rent_pay_item pi JOIN tb_rent_pay_record pr ON pi.pay_record_id = pr.id "
                            + "WHERE pr.order_id = ?", orderId);
            jdbcTemplate.update("DELETE FROM tb_rent_pay_record WHERE order_id = ?", orderId);
            jdbcTemplate.update("DELETE FROM tb_rent_order_installment WHERE order_id = ?", orderId);
            jdbcTemplate.update("DELETE FROM tb_rent_order_item WHERE order_id = ?", orderId);
            jdbcTemplate.update("DELETE FROM tb_rent_order_sign WHERE order_id = ?", orderId);
            jdbcTemplate.update("DELETE FROM tb_rent_order WHERE id = ?", orderId);
        }
        for (String notifyId : createdNotifyIds) {
            jdbcTemplate.update("DELETE FROM tb_alipay_notify_record WHERE notify_id = ?", notifyId);
        }
    }

    private ResponseEntity<Map> post(String path, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return rest.postForEntity(path, new HttpEntity<>(body, headers), Map.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> dataOf(ResponseEntity<Map> resp) {
        assertEquals(200, resp.getStatusCodeValue());
        Map<String, Object> body = resp.getBody();
        assertTrue((Boolean) body.get("success"), "接口应返回success，实际：" + body);
        return (Map<String, Object>) body.get("data");
    }

    @Test
    void goodsPageShouldWorkOverHttp() {
        Map<String, Object> req = new HashMap<>();
        req.put("pageNo", 1);
        req.put("pageSize", 10);
        req.put("params", new HashMap<>());

        ResponseEntity<Map> resp = post("/api/v1/rent/goods/page", req);
        Map<String, Object> data = dataOf(resp);
        assertNotNull(data.get("total"));
        assertNotNull(data.get("records"));
    }

    @Test
    void createOrderShouldWorkOverHttpEndToEnd() {
        // 前置：真实插入商品与SKU（表id无自增，须显式给雪花id）
        String goodsCode = "HIT" + IdUtil.getSnowflakeNextIdStr();
        long goodsId = IdUtil.getSnowflakeNextId();
        long skuId = IdUtil.getSnowflakeNextId();
        jdbcTemplate.update("INSERT INTO tb_rent_goods(id, goods_code, goods_name, item_type, sale_price, "
                        + "item_value, report_status, goods_status) VALUES(?, ?, ?, 'CAR_ITEM', 1000, 150000, '2', '1')",
                goodsId, goodsCode, "HTTP集成测试商品");
        String outSkuId = "HOS" + IdUtil.getSnowflakeNextIdStr();
        jdbcTemplate.update("INSERT INTO tb_rent_goods_sku(id, goods_id, out_sku_id, sku_name, duration_days, "
                        + "sale_price, sku_status) VALUES(?, ?, ?, '月租', 30, 1000, '1')",
                skuId, goodsId, outSkuId);

        // HTTP创单
        Map<String, Object> installment = new HashMap<>();
        installment.put("installmentNo", 1);
        installment.put("installmentPrice", "1000");
        installment.put("planPayTime", "2026-11-01 00:00:00");
        Map<String, Object> rentPlan = new HashMap<>();
        rentPlan.put("rentStartTime", "2026-10-01 00:00:00");
        rentPlan.put("rentEndTime", "2027-10-01 00:00:00");
        rentPlan.put("installments", java.util.Collections.singletonList(installment));

        Map<String, Object> createReq = new HashMap<>();
        createReq.put("orderType", "RENT");
        createReq.put("sourceId", "SRC-HIT");
        createReq.put("buyerOpenId", "OPENID_HTTP");
        createReq.put("goodsId", String.valueOf(goodsId));
        createReq.put("skuId", String.valueOf(skuId));
        createReq.put("quantity", 1);
        createReq.put("depositPrice", "5000");
        createReq.put("rentPlanInfo", rentPlan);

        Map<String, Object> createData = dataOf(post("/api/v1/rent/order/create", createReq));
        assertNotNull(createData.get("alipayOrderId"));
        assertEquals("/pages/detail", createData.get("path"));
        // 订单号由服务端生成，后续操作与清理都以此为准
        String outOrderId = (String) createData.get("outOrderId");
        assertNotNull(outOrderId);
        createdOutOrderIds.add(outOrderId);

        // HTTP支付
        Map<String, Object> payItem = new HashMap<>();
        payItem.put("feeType", "RENT");
        payItem.put("installmentNo", 1);
        payItem.put("payAmount", "1000");
        Map<String, Object> payReq = new HashMap<>();
        payReq.put("outOrderId", outOrderId);
        payReq.put("payMethod", "JSAPI");
        payReq.put("payItems", java.util.Collections.singletonList(payItem));
        Map<String, Object> payData = dataOf(post("/api/v1/rent/order/pay", payReq));
        String outTradeNo = (String) payData.get("outTradeNo");
        assertNotNull(payData.get("tradeNo"));

        // HTTP支付结果通知回调（测试私钥真实签名）
        Map<String, String> notifyParams = new HashMap<>();
        String notifyId = "NID_HTTP_" + IdUtil.getSnowflakeNextIdStr();
        notifyParams.put("notify_id", notifyId);
        notifyParams.put("biz_content", "{\"out_trade_no\":\"" + outTradeNo
                + "\",\"trade_status\":\"TRADE_SUCCESS\",\"trade_no\":\"TN_HTTP\"}");
        Map<String, String> toSign = new HashMap<>(notifyParams);
        notifyParams.put("sign", SignUtil.sign(toSign, Base64.getEncoder()
                .encodeToString(TestAlipayEnv.KEY_PAIR.getPrivate().getEncoded())));
        createdNotifyIds.add(notifyId);

        HttpHeaders formHeaders = new HttpHeaders();
        formHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        StringBuilder form = new StringBuilder();
        notifyParams.forEach((k, v) -> {
            try {
                form.append(k).append('=').append(java.net.URLEncoder.encode(v, "UTF-8")).append('&');
            } catch (java.io.UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        });
        ResponseEntity<String> notifyResp = rest.postForEntity("/api/v1/callback/alipay/notify",
                new HttpEntity<>(form.substring(0, form.length() - 1), formHeaders), String.class);
        assertEquals(200, notifyResp.getStatusCodeValue());
        assertEquals("success", notifyResp.getBody());

        // 服务端已真实提交：支付流水更新为成功
        Map<String, Object> payStatus = jdbcTemplate.queryForMap(
                "SELECT pay_status, trade_no FROM tb_rent_pay_record WHERE out_trade_no = ?", outTradeNo);
        assertEquals("1", String.valueOf(payStatus.get("pay_status")));
        assertEquals("TN_HTTP", String.valueOf(payStatus.get("trade_no")));
    }

    @Test
    void invalidRequestShouldReturnParamErrorOverHttp() {
        Map<String, Object> req = new HashMap<>();
        req.put("orderType", "");
        ResponseEntity<Map> resp = post("/api/v1/rent/order/create", req);
        Map<String, Object> body = resp.getBody();
        assertEquals(Boolean.FALSE, body.get("success"));
        assertEquals("RENT001", body.get("code"));
    }

    @Test
    void unknownOrderShouldReturnOrderNotFoundOverHttp() {
        Map<String, Object> req = new HashMap<>();
        req.put("outOrderId", "GHOST-" + IdUtil.getSnowflakeNextIdStr());
        ResponseEntity<Map> resp = post("/api/v1/rent/order/detail", req);
        Map<String, Object> body = resp.getBody();
        assertEquals(Boolean.FALSE, body.get("success"));
        assertEquals("RENT002", body.get("code"));
    }
}
