package cn.seehoo.common.elecheck.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import cn.seehoo.common.elecheck.config.EleCheckConfig;
import cn.seehoo.common.elecheck.constant.EleCheckConstant;
import cn.seehoo.common.elecheck.request.BankCardFourCheckReq;
import cn.seehoo.common.elecheck.response.BankCardFourCheckRes;
import cn.seehoo.common.elecheck.util.AesAlgorithmUtil;
import cn.seehoo.common.elecheck.util.Md5Utils;
import cn.seehoo.spg.commons.core.util.HttpUtils;

/**
 * @author sunyf
 * 银行四要素校验
 * @date 2025/9/20 下午5:10
 * @since 1.0
 */
public class BankCardFourCheckClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankCardFourCheckClient.class);
    private EleCheckConfig eleCheckConfig;

    public BankCardFourCheckClient(EleCheckConfig eleCheckConfig) {
        this.eleCheckConfig = eleCheckConfig;
    }

    /**
     * 银行四要素校验
     */
	public BankCardFourCheckRes checkBankCardFour(BankCardFourCheckReq bcfcdto) {
		if (eleCheckConfig.isMock()) {
			LOGGER.info(">>>>>>[银行四要素校验]模拟返回");
			BankCardFourCheckRes fourCheckRes = new BankCardFourCheckRes();
			fourCheckRes.setRespCode(BankCardFourCheckRes.SUCCESS);
			return fourCheckRes;
		}
		// 校验参数
		bcfcdto.checkParams();
		// 四要素校验
		BankCardFourCheckRes cardFourCheckRes = new BankCardFourCheckRes();
		try {
			String params = JSONUtil.toJsonStr(bcfcdto);
			LOGGER.info(">>>>>>[银行四要素要素校验]入参：{}", params);
			JSONObject reqData = new JSONObject();
			JSONObject jsonData = new JSONObject();
			reqData.put(EleCheckConstant.CONF_ID, eleCheckConfig.getWorkFlowId(EleCheckConstant.BANK_FOUR_S));
			reqData.put(EleCheckConstant.ID, Md5Utils.genMd5(bcfcdto.getId()));
			reqData.put(EleCheckConstant.CELL, Md5Utils.genMd5(bcfcdto.getCell()));
			reqData.put(EleCheckConstant.NAME, bcfcdto.getName());
			reqData.put(EleCheckConstant.BANK_ID, bcfcdto.getBankId());
			jsonData.put(EleCheckConstant.REQ_DATA, reqData);
			jsonData.put(EleCheckConstant.API_NAME, EleCheckConstant.API_NAME_TEL);
			String apiCode = eleCheckConfig.getApiCode();
			String appKey = eleCheckConfig.getAppKey();
			String reqDataStr = JSON.toJSONString(reqData);
			String checkCode = Md5Utils.genMd5(reqDataStr + apiCode + appKey);
			LOGGER.debug("生成的checkCode是{}", checkCode);
			String encryptJsonData = AesAlgorithmUtil
					.encrypt(URLEncoder.encode(reqDataStr, StandardCharsets.UTF_8.name()), appKey);
			LOGGER.debug("生成的jsonData是{}", encryptJsonData);
			Map<String, Object> paramMap = new HashMap<>();
			String appKeyEncrypt = Md5Utils.genMd5(appKey);
			paramMap.put(EleCheckConstant.JSON_DATA, encryptJsonData);
			paramMap.put(EleCheckConstant.APP_KEY, appKeyEncrypt);
			paramMap.put(EleCheckConstant.API_CODE, apiCode);
			paramMap.put(EleCheckConstant.CHECK_CODE, checkCode);
			Map<String, String> header = new HashMap<>();
			String reqSeqNo = IdUtil.getSnowflakeNextIdStr();
			// 内容体格式
			// header.put("Content-Type", CONTENT_TYPE);
			// 请求方渠道标示
			header.put("ReqSysId", EleCheckConstant.REQ_SYS_ID);
			// 全局流水号
			header.put("GloSeqNo", reqSeqNo);
			// 服务码
			header.put("SvcNo", EleCheckConstant.BR_SYSTEM);
			// 请求方流水号
			header.put("ReqSeqNo", reqSeqNo);
			// 场景版本号
			header.put("ScnVerNo", EleCheckConstant.SCN_VERSION);
			String str = JSONUtil.toJsonStr(paramMap);
			String response = HttpUtils.request(eleCheckConfig.getDefaultDomain() + eleCheckConfig.getVerificationApi(),
					HttpUtils.METHOD_POST, header, str.getBytes(StandardCharsets.UTF_8), null, null);
			LOGGER.info(">>>>>>[银行四要素要素校验]返回：{}", response);
			JSONObject jsonObject = JSONObject.parseObject(response);
			JSONObject telCheck_s = jsonObject.getJSONObject("BankFour_s");
			if (ObjectUtil.isNotEmpty(telCheck_s)) {
				cardFourCheckRes.setRespCode((String) telCheck_s.get("respCode"));
				cardFourCheckRes.setRespDesc((String) telCheck_s.get("respDesc"));
			}
			return cardFourCheckRes;
		} catch (Exception e) {
			LOGGER.error("四要素校验异常，e={}", e);
			cardFourCheckRes.setRespCode("001");
			cardFourCheckRes.setRespDesc("四要素校验失败");
			return cardFourCheckRes;
		}
	}

}
