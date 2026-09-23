package com.seehoo.rent.sdk;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** SDK统一响应包装测试 */
class RentResponseTest {

    @Test
    void isSuccessShouldDependOnDataPresence() {
        RentResponse<Map<String, Object>> resp = new RentResponse<>();
        assertFalse(resp.isSuccess());

        Map<String, Object> data = new HashMap<>();
        data.put(BizFields.ORDER_ID, "OID1");
        resp.setData(data);
        assertTrue(resp.isSuccess());
    }

    @Test
    void getShouldBeSafeOnNullData() {
        RentResponse<Map<String, Object>> resp = new RentResponse<>();
        assertNull(resp.get(BizFields.ORDER_ID));
        assertNull(resp.getStr(BizFields.ORDER_ID));
    }

    @Test
    void getStrShouldConvertValueToString() {
        RentResponse<Map<String, Object>> resp = new RentResponse<>();
        Map<String, Object> data = new HashMap<>();
        data.put("num", 12345);
        resp.setData(data);
        assertEquals("12345", resp.getStr("num"));
    }
}
