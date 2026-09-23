package cn.seehoo.spg.common.opl.client;


import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.opl.config.OplConfiguration;
import cn.seehoo.spg.common.opl.model.OplCommonDTO;
import cn.seehoo.spg.commons.mq.producer.MQProducer;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.nio.charset.StandardCharsets;

public class OplClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OplClient.class);

    @Autowired
    private OplConfiguration oplConfiguration;

    @Autowired
    private MQProducer mqp;
    //风控结果同步路径
    public static final String RISK_RESULT_SYNC_URL = "/api/riskResult/sync";
    //征信授权结果通知
    public static final String CREDIT_AUTH_CALLBACK_URL = "/api/creditAuth/result/callback";
    //订单状态同步
    public static final String ORDER_STATUS_SYNC_URL = "/api/order/status/sync";
    //放款结果通知接口
    public static final String API_HFL_LOAN_RESULT = "/ums/openapi/loan/postHflLoanResultNote";
    //资审审核通知
    public static final String API_HFL_APPLY_RESULT = "/ums/openapi/loan/postHflApplyResultNote";
    //签放材料审核结果通知
    public static final String API_HFL_DOC_RESULT = "/ums/openapi/loan/postHflDocResultNote";
    //抵押结果回传
    public static final String API_HFL_MORTG_RESULT = "/ums/openapi/loan/postHflMortgResultNote";
    //faf签署完成通知
    public static final String API_FAF_SIGN_NOTICE = "/wx/hxfl/signResultNotice";


    /**
     * 同步 HTTP 请求
     *
     * @param dto   请求参数
     * @param typeRef 返回类型,例如：new TypeReference<BaseResponse<String>>()
     * @param <T>   泛型类型
     * @return 响应结果
     */
    public <T> T syncRequest(OplCommonDTO dto, TypeReference<T> typeRef) {
        // 1. 参数校验
        validateRequestParams(dto);

        String channelId = dto.getChannelId();
        String url = buildRequestUrl(dto);
        String requestBody = JSONUtil.toJsonStr(dto.getBody());

        LOGGER.info("发起同步请求，apiIdent: {}, url: {}, channelId: {}",
                dto.getApiIdent(), url, channelId);

        try {
            // 2. 构建并发送请求
            HttpResponse response = buildHttpRequest(url, channelId, requestBody)
                    .execute();

            // 3. 处理响应
            return handleResponse(response, typeRef, dto);

        } catch (Exception e) {
            LOGGER.error("请求开放平台失败，apiIdent: {}, channelId: {}, url: {}",
                    dto.getApiIdent(), channelId, url, e);
            throw new RuntimeException("请求异常: " + e.getMessage(), e);
        }
    }


    /**
     * 异步请求
     * @param dto
     */
    public void asyncRequest(OplCommonDTO dto){
        // 1. 参数校验
        validateRequestParams(dto);
        //判断如果有事务，等事务提交后再发送消息
        afterTransactionCommit(() -> {
            LOGGER.info("发送MQ-TOPIC:{},消息：{}",OplCommonDTO.TOPIC,JSONUtil.toJsonStr(dto));
            mqp.sendMsg(OplCommonDTO.TOPIC, dto, null);
        });

    }

    /**
     * 事务提交后执行工具方法（通用，复制到你的项目里）
     */
    public static void afterTransactionCommit(Runnable runnable) {
        // 判断当前是否存在事务
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 注册事务同步器
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // 事务提交成功后执行
                    runnable.run();
                }
            });
        } else {
            // 没有事务，直接执行
            runnable.run();
        }
    }


    /**
     * 参数校验
     */
    private void validateRequestParams(OplCommonDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("请求参数不能为空");
        }
        if (StrUtil.isBlank(dto.getChannelId())) {
            throw new IllegalArgumentException("channelId 不能为空");
        }
        if (StrUtil.isBlank(dto.getApiIdent())) {
            throw new IllegalArgumentException("apiIdent 不能为空");
        }
    }

    /**
     * 构建请求 URL
     */
    private String buildRequestUrl(OplCommonDTO dto) {
        String baseUrl = oplConfiguration.getServerUrl();
        if (StrUtil.isBlank(baseUrl)) {
            throw new IllegalStateException("服务器地址未配置");
        }
        return baseUrl + dto.getApiIdent();
    }

    /**
     * 构建 HTTP 请求
     */
    private HttpRequest buildHttpRequest(String url, String channelId, String requestBody) {
        return HttpRequest.post(url)
                .timeout(oplConfiguration.getTimeout())
                .body(requestBody)
                .header(OplCommonDTO.CHANNEL_ID, channelId)
                .contentType(ContentType.JSON.toString(StandardCharsets.UTF_8));

    }

    /**
     * 处理 HTTP 响应
     */
    private <T> T handleResponse(HttpResponse response, TypeReference<T> typeRef, OplCommonDTO dto) {
        if (!response.isOk()) {
            String errorMsg = String.format("HTTP请求失败，状态码: %d, apiIdent: %s",
                    response.getStatus(), dto.getApiIdent());
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        String responseBody = response.body();
        if (StrUtil.isBlank(responseBody)) {
           return null;
        }
        LOGGER.info("响应结果，apiIdent: {}, response: {}", dto.getApiIdent(), responseBody);
        return convertResponse(responseBody, typeRef);
    }

    /**
     * 响应类型转换
     */
    private <T> T convertResponse(String responseBody, TypeReference<T> typeRef) {
        if (typeRef == null) {
            return null;
        }
        // 场景 4：JSON 反序列化
        try {
            return JSONUtil.toBean(responseBody,typeRef,true);
        } catch (Exception e) {
            LOGGER.error("JSON 反序列化失败，clazz: {}, response: {}",typeRef.getTypeName(), responseBody, e);
            throw new RuntimeException("响应解析失败: " + e.getMessage(), e);
        }
    }

}
