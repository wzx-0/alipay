package cn.seehoo.spg.bizcom.intel.client;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.ContentType;
import cn.seehoo.spg.bizcom.intel.model.IntelRecognizeInfo;
import cn.seehoo.spg.bizcom.intel.model.IntelRecognizeResponse;
import cn.seehoo.spg.bizcom.intel.model.IntelModelConfig;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @author chenjun
 * <p>
 * qw客户端
 */
public class QwIntelClient implements IntelClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(QwIntelClient.class);

    @Override
    public IntelRecognizeResponse intelRecognize(IntelRecognizeInfo intelRecognizeInfo){
        String jsonString = JSON.toJSONString(intelRecognizeInfo);
        LOGGER.info(">>>>>>[intel]， 入参 [{}]", jsonString);
        IntelModelConfig modelConfig = intelRecognizeInfo.getModelConfig();
        if (ObjectUtil.isEmpty(modelConfig)) {
            throw new BusinessException("智能识别模型配置为空");
        }
        String url = modelConfig.getUrl();
        String apiKey = modelConfig.getApiKey();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", ContentType.JSON.toString());
        headers.put("Authorization", apiKey);
        String response = HttpUtils.request(url, HttpUtils.METHOD_POST,
                headers, jsonString.getBytes(StandardCharsets.UTF_8), null, null);
        LOGGER.info(">>>>>>[intel]，智能识别接口，响应={}", response);
        return JSON.parseObject(response, IntelRecognizeResponse.class);
    }


}