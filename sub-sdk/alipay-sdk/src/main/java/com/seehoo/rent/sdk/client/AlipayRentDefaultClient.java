package com.seehoo.rent.sdk.client;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.seehoo.rent.sdk.AlipayRentException;
import com.seehoo.rent.sdk.RentConstants;
import com.seehoo.rent.sdk.RentResponse;
import com.seehoo.rent.sdk.SignUtil;
import com.seehoo.rent.sdk.config.AlipayRentConfig;
import com.seehoo.rent.sdk.model.AftersaleConfirmRequest;
import com.seehoo.rent.sdk.model.AftersaleCreateRequest;
import com.seehoo.rent.sdk.model.FulfillmentApproveRequest;
import com.seehoo.rent.sdk.model.FulfillmentFinishRequest;
import com.seehoo.rent.sdk.model.FulfillmentReceiveRequest;
import com.seehoo.rent.sdk.model.FulfillmentSendRequest;
import com.seehoo.rent.sdk.model.OrderCloseRequest;
import com.seehoo.rent.sdk.model.OrderCreateRequest;
import com.seehoo.rent.sdk.model.OrderModifyRequest;
import com.seehoo.rent.sdk.model.OrderPayRequest;
import com.seehoo.rent.sdk.model.OrderPaySyncRequest;
import com.seehoo.rent.sdk.model.OrderQueryRequest;
import com.seehoo.rent.sdk.model.OrderSignRequest;
import com.seehoo.rent.sdk.model.RiskConsultRequest;
import com.seehoo.rent.sdk.model.TradeRefundRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付宝汽车订阅服务端接口默认客户端
 * <p>统一协议层：公参组装 -> RSA2签名 -> 网关form提交 -> 响应节点解析（可选验签）</p>
 * <p>序列化说明：bizMapper为SNAKE_CASE命名策略，请求DTO的camelCase字段
 * 自动映射为支付宝协议snake_case字段，枚举按name()原样输出，null字段不参与序列化（与官方非空才传规则一致）</p>
 */
@Slf4j
public class AlipayRentDefaultClient implements AlipayRentClient {

    private static final String KEY_APP_ID = "app_id";
    private static final String KEY_METHOD = "method";
    private static final String KEY_FORMAT = "format";
    private static final String KEY_CHARSET = "charset";
    private static final String KEY_SIGN_TYPE = "sign_type";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_VERSION = "version";
    private static final String KEY_BIZ_CONTENT = "biz_content";
    private static final String KEY_NOTIFY_URL = "notify_url";
    private static final String NODE_ERROR_RESPONSE = "error_response";
    private static final String NODE_CODE = "code";
    private static final String NODE_MSG = "msg";
    private static final String NODE_SUB_CODE = "sub_code";
    private static final String NODE_SUB_MSG = "sub_msg";
    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int HTTP_TIMEOUT_MS = 15000;
    /** 综合风险类型默认值 */
    private static final String RISK_TYPE_COMPREHENSIVE = "COMPREHENSIVE_RISK";

    private final AlipayRentConfig config;
    /** bizContent专用：SNAKE_CASE策略映射协议字段名 */
    private final ObjectMapper bizMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    private final ObjectMapper plainMapper = new ObjectMapper();

    public AlipayRentDefaultClient(AlipayRentConfig config) {
        this.config = config;
    }

    @Override
    public RentResponse<Map<String, Object>> execute(String method, Object bizContent, String notifyUrl) {
        Map<String, Object> params = buildPublicParams(method, notifyUrl);
        params.put(KEY_BIZ_CONTENT, toJson(bizContent));
        params.put(RentSdkHelper.KEY_SIGN, SignUtil.sign(params, config.getAppPrivateKey()));

        String body = HttpUtil.post(config.getGateway(), params, HTTP_TIMEOUT_MS);
        return parseResponse(method, body);
    }

    /** 组装公参（不含sign） */
    private Map<String, Object> buildPublicParams(String method, String notifyUrl) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(KEY_APP_ID, config.getAppId());
        params.put(KEY_METHOD, method);
        params.put(KEY_FORMAT, config.getFormat());
        params.put(KEY_CHARSET, config.getCharset());
        params.put(KEY_SIGN_TYPE, config.getSignType());
        params.put(KEY_TIMESTAMP, LocalDateTime.now().format(TIMESTAMP_FMT));
        params.put(KEY_VERSION, config.getApiVersion());
        if (notifyUrl != null && !notifyUrl.isEmpty()) {
            params.put(KEY_NOTIFY_URL, notifyUrl);
        }
        return params;
    }

    /** 解析网关响应：error_response 抛异常；否则提取响应节点，可选验签 */
    private RentResponse<Map<String, Object>> parseResponse(String method, String body) {
        Map<String, Object> rootNode = fromJson(body);
        if (rootNode.containsKey(NODE_ERROR_RESPONSE)) {
            Map<?, ?> err = (Map<?, ?>) rootNode.get(NODE_ERROR_RESPONSE);
            throw new AlipayRentException(String.format("支付宝网关错误：%s %s",
                    err.get(NODE_MSG), str(err.get(NODE_SUB_CODE))),
                    str(err.get(NODE_CODE)), str(err.get(NODE_SUB_CODE)));
        }
        String node = method.replace('.', '_') + "_response";
        Object respNode = rootNode.get(node);
        if (respNode == null) {
            throw new AlipayRentException("支付宝响应缺少节点：" + node + "，原文：" + body);
        }
        if (config.isVerifyResponseSign()) {
            String rawNode = RentSdkHelper.extractRawNode(body, node);
            String sign = str(rootNode.get(RentSdkHelper.KEY_SIGN));
            if (!SignUtil.rsaCheckContent(rawNode, sign, config.getAlipayPublicKey())) {
                throw new AlipayRentException("支付宝响应验签失败");
            }
        }
        RentResponse<Map<String, Object>> result = new RentResponse<>();
        result.setRawBody(body);
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) respNode;
        result.setData(data);
        return result;
    }

    @Override
    public RentResponse<Map<String, Object>> orderCreate(OrderCreateRequest request) {
        request.setBizIdentity(RentConstants.BIZ_IDENTITY);
        return execute(RentConstants.METHOD_ORDER_CREATE, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> orderQuery(OrderQueryRequest request) {
        return execute(RentConstants.METHOD_ORDER_QUERY, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> orderSign(OrderSignRequest request) {
        return execute(RentConstants.METHOD_ORDER_SIGN, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> riskConsult(RiskConsultRequest request) {
        if (request.getConsultRiskTypes() == null || request.getConsultRiskTypes().isEmpty()) {
            request.setConsultRiskTypes(RISK_TYPE_COMPREHENSIVE);
        }
        return execute(RentConstants.METHOD_RISK_CONSULT, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> fulfillmentApprove(FulfillmentApproveRequest request) {
        return execute(RentConstants.METHOD_FULFILLMENT_APPROVE, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> fulfillmentSend(FulfillmentSendRequest request) {
        return execute(RentConstants.METHOD_FULFILLMENT_SEND, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> fulfillmentReceive(FulfillmentReceiveRequest request) {
        return execute(RentConstants.METHOD_FULFILLMENT_RECEIVE, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> fulfillmentFinish(FulfillmentFinishRequest request) {
        return execute(RentConstants.METHOD_FULFILLMENT_FINISH, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> orderPay(OrderPayRequest request) {
        return execute(RentConstants.METHOD_ORDER_PAY, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> orderPaySync(OrderPaySyncRequest request) {
        return execute(RentConstants.METHOD_ORDER_PAY_SYNC, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> orderModify(OrderModifyRequest request) {
        return execute(RentConstants.METHOD_ORDER_MODIFY, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> orderClose(OrderCloseRequest request) {
        return execute(RentConstants.METHOD_ORDER_CLOSE, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> tradeRefund(TradeRefundRequest request) {
        return execute(RentConstants.METHOD_TRADE_REFUND, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> aftersaleCreate(AftersaleCreateRequest request) {
        return execute(RentConstants.METHOD_AFTERSALE_CREATE, request, null);
    }

    @Override
    public RentResponse<Map<String, Object>> aftersaleConfirm(AftersaleConfirmRequest request) {
        return execute(RentConstants.METHOD_AFTERSALE_CONFIRM, request, null);
    }

    private String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return bizMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new AlipayRentException("bizContent序列化失败", e);
        }
    }

    private Map<String, Object> fromJson(String json) {
        try {
            return plainMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new AlipayRentException("支付宝响应解析失败：" + json, e);
        }
    }

    private String str(Object val) {
        return val == null ? null : String.valueOf(val);
    }
}
