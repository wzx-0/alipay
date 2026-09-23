package com.seehoo.rent.sdk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Map;

/**
 * SDK统一响应：包装支付宝网关响应节点，保留原始报文便于排查
 *
 * @param <T> 业务数据类型，通常为Map
 */
@Data
public class RentResponse<T> {

    /** 支付宝响应原始JSON（response节点原文） */
    private String rawBody;

    /** 响应节点内容 */
    private Map<String, Object> data;

    @JsonIgnore
    public boolean isSuccess() {
        return data != null;
    }

    /** 取业务字段，避免调用方强转 */
    public Object get(String key) {
        return data == null ? null : data.get(key);
    }

    public String getStr(String key) {
        Object val = get(key);
        return val == null ? null : String.valueOf(val);
    }
}
