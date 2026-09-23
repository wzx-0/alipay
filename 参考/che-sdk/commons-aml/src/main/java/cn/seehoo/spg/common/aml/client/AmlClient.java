package cn.seehoo.spg.common.aml.client;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import cn.seehoo.spg.common.aml.model.AmlNatPerMonitorResult;
import cn.seehoo.spg.common.aml.model.AmlOfficialRatingResult;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.aml.config.HxAmlConfig;
import cn.seehoo.spg.common.aml.constant.AmlPreRatingConstant;
import cn.seehoo.spg.common.aml.constant.AmlRiskLevelTypeConstant;
import cn.seehoo.spg.common.aml.model.AmlDTO;
import cn.seehoo.spg.common.aml.model.AmlResult;
import cn.seehoo.spg.common.aml.utils.SecurityUtil;
import cn.seehoo.spg.commons.core.util.HttpUtils;

/**
 * @author caofei
 * @desc
 * @time 2025/9/25 15:05。
 */
public class AmlClient implements AmlBaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(AmlClient.class);
	@Autowired
	private HxAmlConfig amlConfig;
	@Autowired
	private RequestLogClient requestLogClient;

	@Override
	public AmlResult preRatingRt(AmlDTO amlRtReq) {
		try {
			if (amlConfig.isMock()) {
				LOGGER.info(">>>>>>> [反洗钱] 自然人反洗钱预评级（实时） 开启 Mock");
				return AmlResult.defaultData(amlRtReq);
			}
			// 生成签名
			long timeInMillis = Calendar.getInstance().getTimeInMillis();
			String serviceId = amlConfig.getServiceId();
			String sign = SecurityUtil.generateSignature(serviceId, timeInMillis, amlConfig.getChannelSecret());
			// 构建请求头
			Map<String, String> requestHead = new HashMap<>(10);
			requestHead.put(AmlPreRatingConstant.SERVICE_ID, serviceId);
			requestHead.put(AmlPreRatingConstant.TIME_STAMP, String.valueOf(timeInMillis));
			requestHead.put(AmlPreRatingConstant.SIGN, sign);
			requestHead.put("Content-Type", "application/json");
			// 发起请求
			String requestBody = JSON.toJSONString(amlRtReq);
			LOGGER.info(">>>>>>> [反洗钱] 自然人反洗钱预评级（实时） 入参={}", requestBody);
			String response = HttpUtils.request(amlConfig.getNaturalRatingRtUrl(), HttpUtils.METHOD_POST, requestHead,
			requestBody.getBytes(StandardCharsets.UTF_8), null, null);
			LOGGER.info(">>>>>>> [反洗钱] 自然人反洗钱预评级（实时） 出参={}", response);
			AmlResult result = JSONUtil.toBean(response, AmlResult.class);
			if (!result.isSuccess()) {
				LOGGER.error("[反洗钱] 自然人反洗钱预评级（实时） 失败,code={},msg={}", 
				result.getResponseCode(), result.getResponseText());
			}
			// 高风险
			AmlResult.AmlPreRatingResult custResult = result.getCustResult();
			if (custResult == null || custResult.getCustLvl() == null || AmlRiskLevelTypeConstant.HIGH_RISK_LEVELS.contains(custResult.getCustLvl())) {
				result.setHighRisk(true);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("[反洗钱] 自然人反洗钱预评级（实时）异常， ", e);
			throw new BusinessException("-1", "反洗钱调用异常");
		}
	}

	@Override
	public AmlNatPerMonitorResult natPerMonitorRt(AmlDTO amlRtReq) {
		String requestBody = "";
		String response = "";
		boolean status = false;
		AmlNatPerMonitorResult result = new AmlNatPerMonitorResult();
		try {
			if (amlConfig.isMock()) {
				LOGGER.info(">>>>>>> [反洗钱] 自然人监控名单查询结果接口(实时) 开启 Mock");
				return AmlNatPerMonitorResult.defaultData(amlRtReq);
			}
			// 生成签名
			long timeInMillis = Calendar.getInstance().getTimeInMillis();
			String serviceId = amlConfig.getServiceId();
			String sign = SecurityUtil.generateSignature(serviceId, timeInMillis, amlConfig.getChannelSecret());
			// 构建请求头
			Map<String, String> requestHead = new HashMap<>(10);
			requestHead.put(AmlPreRatingConstant.SERVICE_ID, serviceId);
			requestHead.put(AmlPreRatingConstant.TIME_STAMP, String.valueOf(timeInMillis));
			requestHead.put(AmlPreRatingConstant.SIGN, sign);
			requestHead.put("Content-Type", "application/json");
			// 发起请求
			requestBody = JSON.toJSONString(amlRtReq);
			LOGGER.info(">>>>>>> [反洗钱] 自然人监控名单查询结果接口(实时) 入参={}", requestBody);
			response = HttpUtils.request(amlConfig.getNatPerMonitorRtUrl(), HttpUtils.METHOD_POST, requestHead,
					requestBody.getBytes(StandardCharsets.UTF_8), null, null);
			LOGGER.info(">>>>>>> [反洗钱] 自然人监控名单查询结果接口(实时) 出参={}", response);
			result = JSONUtil.toBean(response, AmlNatPerMonitorResult.class);
			status = result.isSuccess();
			if (!status) {
				LOGGER.error("[反洗钱] 自然人监控名单查询结果接口(实时) 失败,code={},msg={}",
						result.getResponseCode(), result.getResponseText());
			}
			// 高风险
			AmlNatPerMonitorResult.CustResult custResult = result.getCustResult();
			if (ObjUtil.isEmpty(custResult) || CollUtil.isEmpty(custResult.getResList())) {
				result.setHighRisk(true);
			}else{
				for (AmlNatPerMonitorResult.CustResult.ResResult resResult:custResult.getResList()) {
					//匹配成功 - 反洗钱和反恐怖融资监控名单
					if (ObjUtil.equal(resResult.getRescode(),"1") && ObjUtil.equal(resResult.getRestype(),"1")){
						result.setHighRisk(true);
					}
				}
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("[反洗钱] 自然人监控名单查询结果接口(实时)异常， ", e);
			throw new BusinessException("-1", "反洗钱调用异常");
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录自然人监控名单查询录接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.AML);
				requestLogSaveDto.setInterfaceName("监控名单查询");
				requestLogSaveDto.setUrl(amlConfig.getNatPerMonitorRtUrl());
				requestLogSaveDto.setRequestMsg(requestBody);
				requestLogSaveDto.setResponseMsg(response);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (null != result) {
					requestLogSaveDto.setReason(result.getResponseText());
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.info(">>>>>>>>>>记录自然人监控名单查询录接口调用日志失败:{}", e);
			}
		}
	}

	@Override
	public AmlOfficialRatingResult officialRatingRt(String custId) {
		String requestBody = "";
		String response = "";
		boolean status = false;
		AmlOfficialRatingResult result = new AmlOfficialRatingResult();
		try {
			if (amlConfig.isMock()) {
				LOGGER.info(">>>>>>> [反洗钱] 客户正式评级结果查询接口 开启 Mock");
				return AmlOfficialRatingResult.defaultData();
			}
			// 生成签名
			long timeInMillis = Calendar.getInstance().getTimeInMillis();
			String serviceId = amlConfig.getServiceId();
			String sign = SecurityUtil.generateSignature(serviceId, timeInMillis, amlConfig.getChannelSecret());
			// 构建请求头
			Map<String, String> requestHead = new HashMap<>(10);
			requestHead.put(AmlPreRatingConstant.SERVICE_ID, serviceId);
			requestHead.put(AmlPreRatingConstant.TIME_STAMP, String.valueOf(timeInMillis));
			requestHead.put(AmlPreRatingConstant.SIGN, sign);
			requestHead.put("Content-Type", "application/json");
			// 发起请求
			requestBody = JSON.toJSONString(new HashMap<String,String >(){{put("cust_id",custId);}});
			LOGGER.info(">>>>>>> [反洗钱] 客户正式评级结果查询接口 入参={}", requestBody);
			response = HttpUtils.request(amlConfig.getOfficialRatingRtUrl(), HttpUtils.METHOD_POST, requestHead,
					requestBody.getBytes(StandardCharsets.UTF_8), null, null);
			LOGGER.info(">>>>>>> [反洗钱] 客户正式评级结果查询接口 出参={}", response);
			result = JSONUtil.toBean(response, AmlOfficialRatingResult.class);
			status = result.isSuccess();
			if (!status) {
				LOGGER.error("[反洗钱] 客户正式评级结果查询接口 失败,code={},msg={}",
						result.getResponseCode(), result.getResponseText());
			}
			// 高风险
			AmlOfficialRatingResult.CustRlstDTO custResult = result.getCustRlstDTO();
			if (ObjUtil.isEmpty(custResult) || StrUtil.equals(custResult.getCurrLvl(),"1004") || StrUtil.equals(custResult.getCurrLvl(),"1005")) {
				result.setHighRisk(true);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error("[反洗钱] 客户正式评级结果查询接口异常， ", e);
			throw new BusinessException("-1", "反洗钱调用异常");
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录客户正式评级接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.AML);
				requestLogSaveDto.setInterfaceName("客户正式评级接口");
				requestLogSaveDto.setUrl(amlConfig.getOfficialRatingRtUrl());
				requestLogSaveDto.setRequestMsg(requestBody);
				requestLogSaveDto.setResponseMsg(response);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (null != result) {
					requestLogSaveDto.setReason(result.getResponseText());
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.info(">>>>>>>>>>记录客户正式评级接口调用日志失败:{}", e);
			}
		}
	}
}
