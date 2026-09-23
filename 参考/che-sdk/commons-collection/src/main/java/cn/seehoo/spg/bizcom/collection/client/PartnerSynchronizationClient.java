package cn.seehoo.spg.bizcom.collection.client;

import cn.hutool.http.ContentType;
import cn.seehoo.spg.bizcom.collection.dto.CollectionSyncDTO;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxx
 * @date 2026/1/8 9:30
 */
public class PartnerSynchronizationClient extends CollectionBaseClient implements PartnerUpdateOrSaveClient{
    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerSynchronizationClient.class);
    /**同步其他合作方信息接口 */
    private static final String SYNC_PARTNER_URL = "/api/outSource/syncOutSourceCompany";

    @Override
    public boolean syncPartnerInfo(CollectionSyncDTO csdto) throws BusinessException {
        try {
            // mock
            if (conf.checkMock()) {
                LOGGER.warn(">>>>>>[催收系统]，同步供应商信息，触发mock机制，供应商编号={}", csdto.getCompanyCode());
                return true;
            }
            LOGGER.info(">>>>>>[催收系统]，同步供应商信息，供应商编号={}", csdto.getCompanyCode());

            // 请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", ContentType.JSON.toString());
            String url = conf.getHost() + SYNC_PARTNER_URL;
            // body
            byte[] requestBody = JSON.toJSONString(csdto).getBytes("UTF-8");
            LOGGER.info(">>>>>>[催收系统]，同步供应商信息，入参={}", JSON.toJSONString(csdto));
            String body = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, requestBody, null, null);
            LOGGER.info(">>>>>>[催收系统]，同步供应商信息，出参={}", body);
            return this.checkResult(body);
        } catch (Exception e) {
            LOGGER.error(">>>>>>[催收系统]，同步供应商信息，e={}", e);
            return false;
        }
    }
}
