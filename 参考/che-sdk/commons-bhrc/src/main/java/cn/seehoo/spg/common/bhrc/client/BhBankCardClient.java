package cn.seehoo.spg.common.bhrc.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import cn.seehoo.spg.common.bhrc.config.HxBhrcPhoneConfig;
import cn.seehoo.spg.common.bhrc.model.BhrcResDTO;
import cn.seehoo.spg.common.bhrc.req.BankCardFourCheckReq;
import cn.seehoo.spg.common.bhrc.req.TelThreeCheckReq;
import cn.seehoo.spg.common.bhrc.res.BankCardFourCheckRes;
import cn.seehoo.spg.common.bhrc.res.TelThreeCheckRes;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

@AllArgsConstructor
@Slf4j
public class BhBankCardClient extends BhBaseClient{
    private final HxBhrcPhoneConfig config;
    private final RedisOperateService redisOperateService;
    private RequestLogClient requestLogClient;

    public BankCardFourCheckRes check(BankCardFourCheckReq req) {
        BankCardFourCheckRes res = new BankCardFourCheckRes(null);
        // 是否模拟
        if (config.isMock()) {
            res.setVerifyStatus(BankCardFourCheckRes.STR_OK);
            return res;
        }
        String body = null;
        String responseStr = null;
        String responseCode = null;
        boolean success = false;
        try {
            String secretId = config.getSecretId();
            String secretKey = config.getSecretKey();
            // 构建华夏请求头
            Map<String, String> header = buildReqHeader(config.getSvcNo(), config.getScnNoBankCard(), redisOperateService);
            // 构建百行请求体
            Map<String, String> paramMap = BeanUtils.describe(req);
            paramMap.remove("class");
            body = genRequestStr(secretKey, secretId, paramMap);

            //发起请求
            responseStr = HttpUtils.request(config.getHxHost() + config.getAuthUrlBankCard(), HttpUtils.METHOD_POST, header, body.getBytes(), null, "");
            log.info("【百行】银行卡四要素 校验结果：{}", responseStr);

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
            res = JSONUtil.toBean(decryptData, BankCardFourCheckRes.class);
        } catch (Exception e) {
            res.setVerifyStatus(null);
            log.error(">>>>>> [百行] 调用接口失败：", e);
        } finally {
            RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
            requestLogSaveDto.setSystemName(SystemNameConstant.BH);
            requestLogSaveDto.setInterfaceName("银行卡四要素");
            requestLogSaveDto.setUrl(config.getHxHost() + config.getAuthUrlBankCard());
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
