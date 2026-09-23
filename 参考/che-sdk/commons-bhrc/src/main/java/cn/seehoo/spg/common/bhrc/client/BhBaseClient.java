package cn.seehoo.spg.common.bhrc.client;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.bhrc.config.HxBhrcConfig;
import cn.seehoo.spg.common.bhrc.config.HxBhrcPhoneConfig;
import cn.seehoo.spg.common.bhrc.constant.BhrcConstant;
import cn.seehoo.spg.common.bhrc.model.BhrcReqDTO;
import cn.seehoo.spg.common.bhrc.res.TelThreeCheckRes;
import cn.seehoo.spg.common.bhrc.utils.SeqNoUtil;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BhBaseClient {

    String HmacSHA1Encrypt(byte[] secretKeyBytes, String signSrc) throws Exception {
        SecretKeySpec signKey = new SecretKeySpec(secretKeyBytes, "HmacSHA1");
        Mac hmacSHA1 = Mac.getInstance("HmacSHA1");
        hmacSHA1.init(signKey);
        byte[] rawHmac = hmacSHA1.doFinal(signSrc.getBytes());
        return new BASE64Encoder().encode(rawHmac);
    }
    String genRequestStr(String secretKey, String secretId, Map<String, String> paramMap) throws Exception {
        byte[] secretKeyBytes = Base64.decode(secretKey);
        String requestRefId = RandomUtil.randomString(32);
        String signSrc = BhrcConstant.REQUEST_REF_ID + "=" + requestRefId + "&" + BhrcConstant.SECRET_ID + "=" + secretId;
        String sign = HmacSHA1Encrypt(secretKeyBytes, signSrc);

        BhrcReqDTO appBhrcReq = new BhrcReqDTO();
        BhrcReqDTO.RequestHead head = new BhrcReqDTO.RequestHead(requestRefId, secretId, sign);
        appBhrcReq.setHead(head);
        BhrcReqDTO.RequestParam request = new BhrcReqDTO.RequestParam(paramMap);
        appBhrcReq.setRequest(request);
        return appBhrcReq.toEncryptString("3DES", secretKey);
    }

    /**
     * 构建请求头
     * @return 请求头
     */
    Map<String, String> buildReqHeader(String svcNo, String scnNo, RedisOperateService redisOperateService) {
        Map<String, String> requestHead = new HashMap<>(16);
        Date date = new Date(System.currentTimeMillis());
        requestHead.put("reqDt", DateUtil.format(date, "YYYYMMdd"));
        requestHead.put("reqTm", DateUtil.format(date, "HHmmss"));
        requestHead.put("SvcNo", svcNo);
        requestHead.put("ScnNo",scnNo);
        requestHead.put("ReqSysId", "04024");
        requestHead.put("gloSeqNo", SeqNoUtil.getGlobalSeqNo(redisOperateService));
        requestHead.put("ReqSeqNo",SeqNoUtil.getReqSeqNo(redisOperateService));
        requestHead.put("gloEndTm",String.valueOf(System.currentTimeMillis()));
        requestHead.put("rspDt", DateUtil.format(date, "YYYYMMdd"));
        requestHead.put("rspSt","S");
        requestHead.put("rspCd", "000000");
        requestHead.put("rspMsg", "SUCCESS");
        return requestHead;
    }
}
