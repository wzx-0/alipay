package cn.seehoo.spg.common.bhrc.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import cn.seehoo.spg.common.bhrc.config.HxBhrcPhoneConfig;
import cn.seehoo.spg.common.bhrc.model.BhrcResDTO;
import cn.seehoo.spg.common.bhrc.req.TelThreeCheckReq;
import cn.seehoo.spg.common.bhrc.res.TelThreeCheckRes;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

@AllArgsConstructor
@Slf4j
public class BhPhoneClient extends BhBaseClient{
    private final HxBhrcPhoneConfig config;
    private final RedisOperateService redisOperateService;
    private RequestLogClient requestLogClient;

    public TelThreeCheckRes check(TelThreeCheckReq req) {
        TelThreeCheckRes res = new TelThreeCheckRes(null, null);
        // 是否模拟
        if (config.isMock()) {
            log.info(">>>>>> [百行] 开启 Mock");
            String mockText = config.getMockText();
            if (StrUtil.isNotBlank(mockText)) {
                TelThreeCheckRes telThreeCheckRes = JSONUtil.toBean(mockText, TelThreeCheckRes.class);
                log.info("模拟结果：{}", JSONUtil.toJsonStr(telThreeCheckRes));
                return telThreeCheckRes;
            } else {
                res.setIsIdNameMatchSimp(TelThreeCheckRes.RESULT_OK);
                return res;
            }
        }
        String body = null;
        String responseStr = null;
        boolean success = false;
        String responseCode = null;
        try {
            String secretId = config.getSecretId();
            String secretKey = config.getSecretKey();
            // 构建华夏请求头
            Map<String, String> header = buildReqHeader(config.getSvcNo(), config.getScnNo(), redisOperateService);
            // 构建百行请求体
            Map<String, String> paramMap = BeanUtils.describe(req);
            paramMap.remove("class");
            body = genRequestStr(secretKey, secretId, paramMap);

            //发起请求
            responseStr = HttpUtils.request(config.getHxHost() + config.getAuthUrl(), HttpUtils.METHOD_POST, header, body.getBytes(), null, "");
            log.info("【百行】手机三要素校验结果：{}", responseStr);

            // 结果判断
            if (StrUtil.isBlank(responseStr)) {
                return res;
            }
            BhrcResDTO bhVo = JSONUtil.toBean(responseStr, BhrcResDTO.class);
            responseCode = bhVo.getHead().getResponseCode();
            success = bhVo.authSuccess();
            if (!success) {
                return res;
            }
            // 验签 解密
            String decryptData = BhrcResDTO.decrypt(bhVo.getResponse(), "3des", secretKey);
            log.info("解密结果：{}", decryptData);
            // 装填返回
            res = JSONUtil.toBean(decryptData, TelThreeCheckRes.class);
        } catch (Exception e) {
            res.setIsIdNameMatchSimp(null);
            log.error(">>>>>> [百行] 调用接口失败：", e);
        } finally {
            RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
            requestLogSaveDto.setSystemName(SystemNameConstant.BH);
            requestLogSaveDto.setInterfaceName("手机三要素");
            requestLogSaveDto.setUrl(config.getHxHost() + config.getAuthUrl());
            requestLogSaveDto.setRequestMsg(body);
            requestLogSaveDto.setResponseMsg(responseStr);
            requestLogSaveDto.setResponseCode(responseCode);
            requestLogSaveDto.setStatus(success ? "1" : "0");
            requestLogSaveDto.setCallStage(req.getCallStage());
            requestLogSaveDto.setPartnerName(req.getPartnerName());
            requestLogSaveDto.setMode("2");
            requestLogSaveDto.setBusinessNo(req.getBusinessNo());
            requestLogSaveDto.setCustomerName(req.getName());
            requestLogClient.saveRequestLog(requestLogSaveDto);
        }
        return res;
    }
}
