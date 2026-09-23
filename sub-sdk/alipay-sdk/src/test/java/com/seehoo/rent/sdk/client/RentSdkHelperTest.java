package com.seehoo.rent.sdk.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** 响应节点原文提取测试 */
class RentSdkHelperTest {

    @Test
    void shouldExtractSimpleNode() {
        String body = "{\"alipay_commerce_rent_order_query_response\":{\"code\":\"10000\"},\"sign\":\"abc\"}";
        assertEquals("{\"code\":\"10000\"}",
                RentSdkHelper.extractRawNode(body, "alipay_commerce_rent_order_query_response"));
    }

    @Test
    void shouldExtractNestedNode() {
        String body = "{\"resp\":{\"code\":\"10000\",\"rent_plan_info\":{\"installments\":[{\"no\":1}]}}}";
        assertEquals("{\"code\":\"10000\",\"rent_plan_info\":{\"installments\":[{\"no\":1}]}}",
                RentSdkHelper.extractRawNode(body, "resp"));
    }

    @Test
    void shouldKeepBracesInsideStringValues() {
        // 字符串值中包含大括号时不参与配对计数
        String body = "{\"resp\":{\"msg\":\"json样例{与}\",\"code\":\"10000\"}}";
        assertEquals("{\"msg\":\"json样例{与}\",\"code\":\"10000\"}",
                RentSdkHelper.extractRawNode(body, "resp"));
    }

    @Test
    void shouldKeepQuotesInsideStringValues() {
        String body = "{\"resp\":{\"msg\":\"say \\\"hi\\\"\"}}";
        assertEquals("{\"msg\":\"say \\\"hi\\\"\"}", RentSdkHelper.extractRawNode(body, "resp"));
    }

    @Test
    void shouldReturnNullWhenNodeMissing() {
        assertNull(RentSdkHelper.extractRawNode("{\"other\":{}}", "resp"));
    }

    @Test
    void shouldReturnNullWhenNodeValueNotObject() {
        assertNull(RentSdkHelper.extractRawNode("{\"resp\":123}", "resp"));
    }

    @Test
    void shouldReturnNullWhenUnbalanced() {
        assertNull(RentSdkHelper.extractRawNode("{\"resp\":{\"code\":", "resp"));
    }
}
