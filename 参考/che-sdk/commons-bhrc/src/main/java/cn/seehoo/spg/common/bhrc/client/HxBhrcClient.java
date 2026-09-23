package cn.seehoo.spg.common.bhrc.client;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.util.ObjectUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.bhrc.config.HxBhrcConfig;
import cn.seehoo.spg.common.bhrc.constant.BhrcConstant;
import cn.seehoo.spg.common.bhrc.exception.BhrcException;
import cn.seehoo.spg.common.bhrc.model.BhrcAuthResultDTO;
import cn.seehoo.spg.common.bhrc.model.BhrcFourElementAuthDTO;
import cn.seehoo.spg.common.bhrc.model.BhrcReqDTO;
import cn.seehoo.spg.common.bhrc.model.BhrcResDTO;
import cn.seehoo.spg.common.bhrc.utils.SeqNoUtil;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author caofei
 * @desc
 * @time 2025/9/25 13:43。
 */
public class HxBhrcClient implements BhrcClient{
    private static final Logger LOGGER = LoggerFactory.getLogger(HxBhrcClient.class);
    private  HxBhrcConfig hxBhrcConfig;
    private RedisOperateService redisOperateService;
    private RequestLogClient requestLogClient;
    public HxBhrcClient(HxBhrcConfig hxBhrcConfig, RedisOperateService redisOperateService, RequestLogClient requestLogClient) {
        this.hxBhrcConfig = hxBhrcConfig;
        this.redisOperateService = redisOperateService;
        this.requestLogClient = requestLogClient;
    }

    @Override
    public boolean fourElementAuth(BhrcFourElementAuthDTO auth) {
        // 请求体数据
        if (hxBhrcConfig.isMock()) {
            LOGGER.info(">>>>>> [百行公安四要素认证] 开启 Mock");
            return true;
        }
        // 业务参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name", auth.getName());
        paramMap.put("certType", "00");
        paramMap.put("certNo", auth.getCertNo());
        paramMap.put("validStartDate", auth.getValidStartDate());
        paramMap.put("validEndDate", auth.getValidEndDate());
        //构建请求头
        String requestRefId = RandomUtil.randomString(32);
        Map<String, String> requestHead = buildReqHeader(hxBhrcConfig);
        String secretKey = hxBhrcConfig.getSecretKey();
        String secretId = hxBhrcConfig.getSecretId();
        //加签
        String signSrc = BhrcConstant.REQUEST_REF_ID + "=" + requestRefId + "&" + BhrcConstant.SECRET_ID + "=" + secretId;
        byte[] secretKeyBytes = Base64.decode(secretKey);
        String reqStr = "";
        String httpResponse = "";
        boolean status = false;
        BhrcResDTO bhrcResDTO = new BhrcResDTO();
        String responseCode = null;
        try {
            SecretKeySpec signKey = new SecretKeySpec(secretKeyBytes, "HmacSHA1");
            Mac hmacSHA1 = Mac.getInstance("HmacSHA1");
            hmacSHA1.init(signKey);
            byte[] keyBytes = new BASE64Decoder().decodeBuffer(secretKey);
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = hmacSHA1.doFinal(signSrc.getBytes());
            String sign = new BASE64Encoder().encode(rawHmac);
            BhrcReqDTO appBhrcReq = new BhrcReqDTO();
            BhrcReqDTO.RequestHead head = new BhrcReqDTO.RequestHead(requestRefId, secretId, sign);
            appBhrcReq.setHead(head);
            BhrcReqDTO.RequestParam request = new BhrcReqDTO.RequestParam(paramMap);
            appBhrcReq.setRequest(request);
            reqStr = appBhrcReq.toEncryptString("3DES", secretKey);
            //发起请求
            httpResponse = HttpUtils.request(hxBhrcConfig.getHuxHost() + hxBhrcConfig.getPsfeAuthUrl(), HttpUtils.METHOD_POST, requestHead, reqStr.getBytes(), null, "");
            if (StrUtil.isBlank(httpResponse)) {
                throw new BusinessException(BhrcException.AUTH_FAILED);
            }
            bhrcResDTO = JSONUtil.toBean(httpResponse, BhrcResDTO.class);
            responseCode = bhrcResDTO.getHead().getResponseCode();
            status = bhrcResDTO.authSuccess();
            if (!status) {
                LOGGER.error(">>>>>> [百行公安四要素认证] 请求失败");
                throw new BusinessException(bhrcResDTO.getHead().getResponseCode(), bhrcResDTO.getHead().getResponseMsg());
            }
            // 验签 解密
            String decryptData = BhrcResDTO.decrypt(bhrcResDTO.getResponse(), "3des", secretKey);
            BhrcAuthResultDTO authInfo = JSONUtil.toBean(decryptData, BhrcAuthResultDTO.class);
            String verificationCode = authInfo.getVerificationCode();
            if (!authInfo.pass()) {
                LOGGER.info(">>>>>> [百行公安四要素认证] 认证未通过, 结果：{}", verificationCode);
                return false;
            }
            LOGGER.info(">>>>>> [百行公安四要素认证] 认证通过, 结果：{}", verificationCode);
        } catch (Exception e) {
            LOGGER.error(">>>>>>>> [百行公安四要素认证] 认证失败", e);
            return false;
        } finally {
            try {
                LOGGER.info(">>>>>>>>>>开始记录百行公安信息四要素接口调用日志");
                RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
                requestLogSaveDto.setSystemName(SystemNameConstant.BH);
                requestLogSaveDto.setInterfaceName("公安信息四要素");
                requestLogSaveDto.setUrl(hxBhrcConfig.getHuxHost() + hxBhrcConfig.getPsfeAuthUrl());
                requestLogSaveDto.setRequestMsg(reqStr);
                requestLogSaveDto.setResponseMsg(httpResponse);
                requestLogSaveDto.setResponseCode(responseCode);
                requestLogSaveDto.setStatus(status ? "1" : "0");
                requestLogSaveDto.setCallStage(auth.getCallStage());
                requestLogSaveDto.setPartnerName(auth.getPartnerName());
                requestLogSaveDto.setMode("2");
                requestLogSaveDto.setBusinessNo(auth.getBusinessNo());
                requestLogSaveDto.setCustomerName(auth.getName());
                BhrcResDTO.ResponseHead head = bhrcResDTO.getHead();
                if (ObjectUtil.isNotEmpty(head)) {
                    requestLogSaveDto.setReason(head.getResponseMsg());
                }
                requestLogClient.saveRequestLog(requestLogSaveDto);
            } catch (Exception e) {
                LOGGER.error(">>>>>>>>>>记录百行公安信息四要素接口调用日志失败:{}", e);
            }
        }
        return true;
    }

    /**
     * 构建请求头
     * @return 请求头
     */
    private Map<String, String> buildReqHeader(HxBhrcConfig hxBhrcConfig) {
        Map<String, String> requestHead = new HashMap<>(16);
        Date date = new Date(System.currentTimeMillis());
        requestHead.put("reqDt", DateUtil.format(date, "YYYYMMdd"));
        requestHead.put("reqTm", DateUtil.format(date, "HHmmss"));
        requestHead.put("SvcNo",hxBhrcConfig.getSvcNo());
        requestHead.put("ScnNo",hxBhrcConfig.getScnNo());
        requestHead.put("ReqSysId", "04024");
        requestHead.put("gloSeqNo", SeqNoUtil.getGlobalSeqNo(redisOperateService));
        requestHead.put("ReqSeqNo",SeqNoUtil.getReqSeqNo(redisOperateService));
        requestHead.put("gloEndTm",String.valueOf(System.currentTimeMillis()));
        requestHead.put("rspDt", DateUtil.format(date, "YYYYMMdd"));
        requestHead.put("rspSt","S");
        requestHead.put("rspCd", "000000");
        requestHead.put("rspMsg", "SUCCESS");
        return  requestHead;
    }
}
