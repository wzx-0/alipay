package cn.seehoo.common.elecheck.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.common.elecheck.config.EleCheckConfig;
import cn.seehoo.common.elecheck.constant.EleCheckConstant;
import cn.seehoo.common.elecheck.request.TelThreeCheckReq;
import cn.seehoo.common.elecheck.response.TelThreeCheckRes;
import cn.seehoo.common.elecheck.util.AesAlgorithmUtil;
import cn.seehoo.common.elecheck.util.Md5Utils;
import cn.seehoo.spg.commons.core.util.HttpUtils;

/**
 * @author sunyf
 * 三要素校验
 * @date 2025/9/20 下午5:10
 * @since 1.0
 */
public class TelThreeCheckClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelThreeCheckClient.class);

    private EleCheckConfig eleCheckConfig;
	private RequestLogClient requestLogClient;

    public TelThreeCheckClient(EleCheckConfig eleCheckConfig, RequestLogClient requestLogClient) {
        this.eleCheckConfig = eleCheckConfig;
		this.requestLogClient = requestLogClient;
    }

    /**
     * 三要素校验
     */
	public TelThreeCheckRes checkTelTree(TelThreeCheckReq ttcdto) {
		if (eleCheckConfig.isMock()) {
			LOGGER.info(">>>>>>[三要素校验]，触发mock, 返回成功");
			TelThreeCheckRes telThreeCheckRes = new TelThreeCheckRes();
			telThreeCheckRes.setResult(TelThreeCheckRes.RESULT_OK);
			return telThreeCheckRes;
		}
		// 校验参数
		ttcdto.checkParams();
		TelThreeCheckRes telThreeCheckRes = new TelThreeCheckRes();
		String str = "";
		String response = "";
		try {
			String params = JSONUtil.toJsonStr(ttcdto);
			LOGGER.info(">>>>>>[三要素校验]入参：{}", params);
			JSONObject reqData = new JSONObject();
			JSONObject jsonData = new JSONObject();
			reqData.put(EleCheckConstant.CONF_ID, eleCheckConfig.getWorkFlowId(EleCheckConstant.TEL_CHECK_S));
			reqData.put(EleCheckConstant.ID, Md5Utils.genMd5(ttcdto.getId()));
			reqData.put(EleCheckConstant.CELL, Md5Utils.genMd5(ttcdto.getCell()));
			reqData.put(EleCheckConstant.NAME, ttcdto.getName());
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
			header.put(EleCheckConstant.REQ_SYS_ID_R, EleCheckConstant.REQ_SYS_ID);
			// 全局流水号
			header.put(EleCheckConstant.GLO_SEQ_NO, reqSeqNo);
			// 服务码
			header.put(EleCheckConstant.SVC_NO, EleCheckConstant.BR_SYSTEM);
			// 请求方流水号
			header.put(EleCheckConstant.REQ_SEQ_NO, reqSeqNo);
			// 场景版本号
			header.put(EleCheckConstant.SCN_VER_NO, EleCheckConstant.SCN_VERSION);
			str = JSONUtil.toJsonStr(paramMap);
			response = HttpUtils.request(eleCheckConfig.getDefaultDomain() + eleCheckConfig.getVerificationApi(),
					HttpUtils.METHOD_POST, header, str.getBytes(StandardCharsets.UTF_8), null, null);
			LOGGER.info(">>>>>>[三要素校验]返回：{}", response);
			JSONObject jsonObject = JSONObject.parseObject(response);
			JSONObject telCheck_s = jsonObject.getJSONObject("TelCheck_s");
			if (ObjectUtil.isNotEmpty(telCheck_s)) {
				telThreeCheckRes.setOperation(telCheck_s.getString("operation"));
				telThreeCheckRes.setResult(telCheck_s.getString("result"));
			}
			return telThreeCheckRes;
		} catch (Exception e) {
			LOGGER.error("三要素校验异常，e={}", e);
			telThreeCheckRes.setResult("-1");
			return telThreeCheckRes;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录百融三要素接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.BR);
				requestLogSaveDto.setInterfaceName("百融三要素");
				requestLogSaveDto.setUrl(eleCheckConfig.getDefaultDomain() + eleCheckConfig.getVerificationApi());
				requestLogSaveDto.setRequestMsg(str);
				requestLogSaveDto.setResponseMsg(response);
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录百融三要素接口调用日志失败:{}", e);
			}
		}
	}
}
