package cn.seehoo.spg.common.risk.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import cn.seehoo.spg.common.risk.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.risk.config.RiskConfiguration;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;

/**
 * Risk默认实现客户端
 */
@SuppressWarnings("rawtypes")
public class DeaultRiskClient implements RiskClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeaultRiskClient.class);

	private RedisOperateService redisOperateService;

	public RiskConfiguration conf;

	private RequestLogClient requestLogClient;

	public DeaultRiskClient(RiskConfiguration conf, RedisOperateService redisOperateService, RequestLogClient requestLogClient) {
		this.conf = conf;
		this.redisOperateService = redisOperateService;
		this.requestLogClient = requestLogClient;
	}

	/**
	 * Mock-决策提交
	 */
	private static final String URL_MOCK_DECISION = "/fkyq-front/app/decision";

	/**
	 * Mock-报告查询
	 */
	private static final String URL_MOCK_QUERY = "/fkyq-front/app/query";

	/**
	 * 风控-决策引擎接口-承租人
	 */
	private static final String URL_RISK_DECISION = "/fkyq-front/wdapp/decision";

	/**
	 * 风控-决策引擎接口-担保人
	 */
	private static final String URL_RISK_DECISION_GUARANTOR = "/fkyq-front/wdapp/guarantor";

	/**
	 * 风控-决策引擎接口-提报人员
	 */
	private static final String URL_RISK_DECISION_REPORTER = "/fkyq-front/wdapp/reporter";

	/**
	 * 风控-报告查询
	 */
	private static final String URL_RISK_QUERY = "/fkyq-front/wdapp/query";

	/**
	 * 风控-法海案件报告查询
	 */
	private static final String URL_RISK_FH = "/fkyq-front/wdapp/premium";

	/**
	 * 风控-接受业务系统（流水指标）
	 */
	private static final String URL_RISK_CREDIT = "/fkyq-front/wdapp/bill";

	/**
	 * 风控-通用提报接口（车辆系统 -> 风控决策引擎）
	 * bizScene 驱动多场景，同步仅返回收妥确认，决策结果走异步回调
	 */
	private static final String URL_RISK_COMMON_SUBMIT = "/fkyq-front/chebiz/decision";

	public static void main(String[] args) {

		RiskConfiguration config = new RiskConfiguration();
		config.setHost("https://fkyq-sit.hxfl.com.cn");
		config.setTenantId("hxjz");
		config.setKey("cheliang");
		config.setSecret("f5c885d2faa72f70e3d91a6ab9364b5e");
		config.setEngineAesKey("2flaRO8quMHdoUgPwws6xUktFpNXgelm");
		config.setUserId("hxjz");
		config.setUsername("fqykInterface00");
		config.setPassword("777480e4977794d352ffadd6cb601e9d");
		config.setMock(true);

		DeaultRiskClient client = new DeaultRiskClient(config, null, null);
		client.conf = config;

		RiskReq req = null;

		// API1 预审/资审-承租人-风控调用
		req = preAuditCallRisk();

		// API2 资审-担保人
		req = orderGarantorCallRisk();

		// API3 提报人员
		req = reporterCallRisk();

		client.submitRisk(req);


		// API4 - 风控报告查询
		RiskReportReq request = new RiskReportReq();
		request.setBusinessId(UUID.fastUUID().toString());
		request.setCertNo("34082619900102065X");
//		request.setFlowId("28c2737dcf154202b62308d3b352c4a1");
		request.setReportType("B");

		// 提报人员不传
		request.setOrderId("251018001");
		request.setPreOrderId("YSCDD20250929004778");

		client.riskReport(request);

	}

	/**
	 * 风控提交
	 *
	 * @param req
	 * @return
	 */
	@Override
	public String submitRisk(RiskReq req) throws BusinessException {
		String reqUrl = "";
		String body = "";
		String response = "";
		try {
			LOGGER.info(">>>>>>[Risk]，风控接口，入参={}", JSON.toJSON(req));

			String reqApi = null;
			if(conf.checkMock()){
				LOGGER.info(">>>>>>[Risk]，风控接口，Mock调用");
				return "{\"code\":\"200\",\"errorCode\":\"true\",\"message\":\"调用成功\"}";
			}else {
				// RiskDecision01-预审资审接口
				if(RiskReq.BUSINESS_TYPE_1.equals(req.getBusinessType()) ||
						RiskReq.BUSINESS_TYPE_3.equals(req.getBusinessType())){
					reqApi = URL_RISK_DECISION;
					req.setChannelId("WDCZR");
				}
				// RiskDecision04-担保人接口
				else if(RiskReq.BUSINESS_TYPE_4.equals(req.getBusinessType())){
					reqApi = URL_RISK_DECISION_GUARANTOR;
					req.setChannelId("WDDBR");
				}
				// RiskDecision07-提报人员
				else if(RiskReq.BUSINESS_TYPE_5.equals(req.getBusinessType())){
					reqApi = URL_RISK_DECISION_REPORTER;
					req.setChannelId("WDJRZY");
				}
				// RiskDecision06-易鑫资审接口
				else if(RiskReq.BUSINESS_TYPE_6.equals(req.getBusinessType())){
					reqApi = URL_RISK_DECISION;
					req.setChannelId("WDYXX");
					req.setBusinessType(RiskReq.BUSINESS_TYPE_3);
				}
				// RiskDecision01-易鑫预审接口
				else if(RiskReq.BUSINESS_TYPE_7.equals(req.getBusinessType())){
					reqApi = URL_RISK_DECISION;
					req.setChannelId("WDYXX");
					req.setBusinessType(RiskReq.BUSINESS_TYPE_1);
				}
				else if(RiskReq.BUSINESS_TYPE_8.equals(req.getBusinessType())){
					reqApi = URL_RISK_DECISION;
					req.setChannelId("WDRFD");
					req.setBusinessType(RiskReq.BUSINESS_TYPE_3);
				}
			}

			req.setUserId(conf.getUserId());

			InnerRiskReq riskReq = BeanUtil.copyProperties(req, InnerRiskReq.class);
			riskReq.setOrderCreateTime(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_PATTERN));
			if(ObjUtil.isNotEmpty(req.getCreditReq()) && StrUtil.isNotEmpty(req.getCreditReq().getAuthstartdate())){
				CsCreditReq creditReq = req.getCreditReq();
				creditReq.setUsername(conf.getUsername());
				creditReq.setPassword(conf.getPassword());
				creditReq.setName(req.getMainLoan().getNAME());
				creditReq.setCertno(req.getMainLoan().getCUST_ID_NO());
				creditReq.setBusinessRequestId(buildBusineeRequestId(creditReq.getCertno()));
				riskReq.setCsCreditReq(JSONUtil.toJsonStr(creditReq));
			}
			LOGGER.info(">>>>>>[Risk]，风控接口，实际入参={}", JSONUtil.toJsonStr(riskReq));

			String reqTime = System.currentTimeMillis()/1000 + "";
			String source = conf.getTenantId() + reqTime + req.getOrderId();

			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(conf.getEngineAesKey().getBytes(), "HmacMD5"));
			byte[] result = mac.doFinal(source.getBytes("UTF-8"));
			String sign = bs2s(result);

			String keyId = conf.getKey() + "@" + SecureUtil.aes(HexUtil.decodeHex(conf.getSecret()))
					.encryptBase64(conf.getKey() + reqTime);
			reqUrl = conf.getHost() + reqApi + "?tenantId=" + conf.getTenantId() + "&reqTime=" + reqTime
					+ "&sign=" + sign + "&keyId=" + keyId;

			body = JSONUtil.toJsonStr(riskReq);

			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", ContentType.JSON.toString());

			response = HttpUtils.request(reqUrl, HttpUtils.METHOD_POST,
					headers, body.getBytes(), null, conf.getProxy());
			LOGGER.info(">>>>>>[Risk]，风控接口，响应={}", response);
			return response;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Risk]，风控接口异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录风控提交接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.RISK);
				requestLogSaveDto.setInterfaceName("风控提交接口");
				requestLogSaveDto.setUrl(reqUrl);
				requestLogSaveDto.setRequestMsg(body);
				requestLogSaveDto.setResponseMsg(response);
				String code = (String) JSONUtil.parseObj(response).get("code");
				String message = (String) JSONUtil.parseObj(response).get("message");
				requestLogSaveDto.setStatus("200".equals(code) ? "1" : "0");
				requestLogSaveDto.setReason(message);
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录风控提交接口调用日志失败:{}", e);
			}
		}

	}

	/**
	 * 报告查询
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String riskReport(RiskReportReq req) throws BusinessException {
		String reqUrl = "";
		String body = "";
		String response = "";
		try {
			LOGGER.info(">>>>>>[Risk]，风控报告查询接口，入参={}", JSON.toJSON(req));

			String reqApi = null;
			if(conf.checkMock()){
				LOGGER.info(">>>>>>[Risk]，风控报告查询接口，Mock调用");
				return "{\"code\":\"200\",\"errorCode\":\"true\",\"message\":\"调用成功\"}";
			}else {
				// RiskDecision03-风险报告查询接口
				reqApi = URL_RISK_QUERY;
			}

			LOGGER.info(">>>>>>[Risk]，风控报告查询接口，实际入参={}", JSONUtil.toJsonStr(req));

			String reqTime = System.currentTimeMillis()/1000 + "";
			String source = conf.getTenantId() + reqTime + req.getOrderId();

			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(conf.getEngineAesKey().getBytes(), "HmacMD5"));
			byte[] result = mac.doFinal(source.getBytes("UTF-8"));
			String sign = bs2s(result);

			String keyId = conf.getKey() + "@" + SecureUtil.aes(HexUtil.decodeHex(conf.getSecret()))
					.encryptBase64(conf.getKey() + reqTime);
			reqUrl = conf.getHost() + reqApi + "?tenantId=" + conf.getTenantId() + "&reqTime=" + reqTime
					+ "&sign=" + sign + "&keyId=" + keyId;

			body = JSONUtil.toJsonStr(req);

			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", ContentType.JSON.toString());

			response = HttpUtils.request(reqUrl, HttpUtils.METHOD_POST,
					headers, body.getBytes(), null, conf.getProxy());
			LOGGER.info(">>>>>>[Risk]，风控报告查询接口，响应={}", response);
			return response;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Risk]，风控报告查询接口异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录风控报告查询接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.RISK);
				requestLogSaveDto.setInterfaceName("风控报告查询接口");
				requestLogSaveDto.setUrl(reqUrl);
				requestLogSaveDto.setRequestMsg(body);
				requestLogSaveDto.setResponseMsg(response);
				String code = (String) JSONUtil.parseObj(response).get("code");
				String message = (String) JSONUtil.parseObj(response).get("message");
				requestLogSaveDto.setStatus("200".equals(code) ? "1" : "0");
				requestLogSaveDto.setReason(message);
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录风控报告查询接口调用日志失败:{}", e);
			}
		}
	}

	/**
	 * 法海案件报告查询
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String fhRiskReport(RiskReportReq req) throws BusinessException {
		String reqUrl = "";
		String body = "";
		String response = "";
		try {
			LOGGER.info(">>>>>>[Risk]，法海案件报告查询接口，入参={}", JSON.toJSON(req));

			String reqApi = null;
			if(conf.checkMock()){
				LOGGER.info(">>>>>>[Risk]，法海案件报告查询接口，Mock调用");
				return "{\"code\":\"200\",\"message\":\"查询成功\",\"responseData\":[{\"type\":\"FH\",\"json\":\"{\\\"code\\\":\\\"201\\\",\\\"message\\\":\\\"暂无数据，请稍后再试\\\"}\"}]}";
			}else {
				// 法海案件报告查询接口
				reqApi = URL_RISK_FH;
			}

			LOGGER.info(">>>>>>[Risk]，法海案件报告查询接口，实际入参={}", JSONUtil.toJsonStr(req));

			String reqTime = System.currentTimeMillis()/1000 + "";
			String source = conf.getTenantId() + reqTime + req.getOrderId();

			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(conf.getEngineAesKey().getBytes(), "HmacMD5"));
			byte[] result = mac.doFinal(source.getBytes("UTF-8"));
			String sign = bs2s(result);

			String keyId = conf.getKey() + "@" + SecureUtil.aes(HexUtil.decodeHex(conf.getSecret()))
					.encryptBase64(conf.getKey() + reqTime);
			reqUrl = conf.getHost() + reqApi + "?keyId=" + keyId;

			body = JSONUtil.toJsonStr(req);

			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", ContentType.JSON.toString());

			response = HttpUtils.request(reqUrl, HttpUtils.METHOD_POST,
					headers, body.getBytes(), null, conf.getProxy());
			LOGGER.info(">>>>>>[Risk]，法海案件报告查询接口，响应={}", response);
			return response;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Risk]，法海案件报告查询接口异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录法海案件报告查询接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.RISK);
				requestLogSaveDto.setInterfaceName("法海案件报告查询接口");
				requestLogSaveDto.setUrl(reqUrl);
				requestLogSaveDto.setRequestMsg(body);
				requestLogSaveDto.setResponseMsg(response);
				String code = (String) JSONUtil.parseObj(response).get("code");
				String message = (String) JSONUtil.parseObj(response).get("message");
				requestLogSaveDto.setStatus("200".equals(code) ? "1" : "0");
				requestLogSaveDto.setReason(message);
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录法海案件报告查询接口调用日志失败:{}", e);
			}
		}
	}

	/**
	 * 发起增信风控模型
	 */
	public String submitCreditRisk(CreditRiskReq req){
		return this.sendRequest(req, URL_RISK_CREDIT,"发起增信风控模型接口");
	}

	/**
	 * 通用提报接口（车辆系统 -> 风控决策引擎）
	 */
	@Override
	public String submitCommonRisk(CommonRiskSubmitReq req){
		return this.sendRequest(req, URL_RISK_COMMON_SUBMIT, "通用风控提报接口");
	}

	/**
	 * 通用请求方法，keyId 鉴权 + HTTP POST + 请求日志 + 业务异常统一处理
	 */
	private String sendRequest(BaseReq req, String reqApi, String interfaceName){
		try {
			String body = JSONUtil.toJsonStr(req);
			LOGGER.info(">>>>>>[Risk]，请求风控接口，入参={}", body);
			String reqTime = System.currentTimeMillis()/1000 + "";

			Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(conf.getEngineAesKey().getBytes(), "HmacMD5"));

			String keyId = conf.getKey() + "@" + SecureUtil.aes(HexUtil.decodeHex(conf.getSecret()))
					.encryptBase64(conf.getKey() + reqTime);
			String reqUrl = conf.getHost() + reqApi + "?keyId=" + keyId;

			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", ContentType.JSON.toString());

			String response = HttpUtils.request(reqUrl, HttpUtils.METHOD_POST, headers, body.getBytes(), null, conf.getProxy());
			LOGGER.info(">>>>>>[Risk]，请求风控接口，响应={}", response);
			BaseRes baseRes = JSONUtil.toBean(response, BaseRes.class);
			try {
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.RISK);
				requestLogSaveDto.setInterfaceName(interfaceName);
				requestLogSaveDto.setUrl(reqUrl);
				requestLogSaveDto.setRequestMsg(body);
				requestLogSaveDto.setResponseMsg(response);
				requestLogSaveDto.setStatus(baseRes.success() ? "1" : "0");
				requestLogSaveDto.setReason(baseRes.getMessage());
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录请求风控接口调用日志失败:", e);
			}
			if (!baseRes.success()){
				throw new BusinessException(baseRes.getCode(), baseRes.getMessage());
			}
			return response;
		}catch (Exception e) {
			LOGGER.error(">>>>>>[Risk]，请求风控接口异常，e=", e);
			throw new BusinessException(e.getMessage());
		}
	}

	private static String bs2s(byte[] src){
		StringBuilder buffer=new StringBuilder();
		for(byte b:src){
			int i=b&0xFF;
			if(i<0x10) buffer.append('0');
			String s=Integer.toHexString(i);
			buffer.append(s);
		}
		return buffer.toString();
	}

	/**
	 * 预审/资审承租人-参数构造示例
	 * @return
	 */
	private static RiskReq preAuditCallRisk() {
		RiskReq req = new RiskReq();
		// 批次号
		req.setBatchNo("xxx");
		// 流水号：申请唯一标识
		req.setBusinessId(UUID.fastUUID().toString());
		// 1-预审 3-资审-承租人
		req.setBusinessType(RiskReq.BUSINESS_TYPE_1);
//		req.setBusinessType(RiskReq.BUSINESS_TYPE_3);

		// 预审都传预审订单号
		req.setPreOrderId("ys202510180006");
		req.setOrderId("251018001");
		// 从产品取-业务模式: XFR-消费融 CZR-车主融
		req.setModelType("XFR");

		MainLoanReq mainLoan = new MainLoanReq();
		req.setMainLoan(mainLoan);
		mainLoan.setNAME("朱涵途");
		mainLoan.setCUST_ID_NO("210102198905177358");

		CsCreditReq creditReq = new CsCreditReq();
		String idCardNo = mainLoan.getCUST_ID_NO();
		// 610124200110025695 身份证后四位 5695
		// CL-3-13-5695-20251013-00038
		String busiReqId = String.format("CL-3-13-%s-%s-%s",
				// 身份证后四位
				StrUtil.subSuf(idCardNo, idCardNo.length()-4),
				DateUtil.format(DateUtil.date(), "yyyyMMdd"),
				// 5位自增流水号 需要Seq工具类
				"00001");
		creditReq.setBusinessRequestId(busiReqId);
		req.setCreditReq(creditReq);
		// 征信授权通过日期 yyyy-MM-dd
		creditReq.setAuthstartdate("2025-10-13");
		// 征信授权文件路径
		creditReq.setAuthfilepath("retail_sit/2025/05/order/e06e810e15f37597f0a5d5630659a4ce/applicantComprehensiveAuth/09581c4077a54b919c048412f7c4328c.jpg");

		return req;
	}

	/**
	 * 资审担保人-参数构造示例
	 * @return
	 */
	private static RiskReq orderGarantorCallRisk() {
		RiskReq req = new RiskReq();
		// 批次号
		req.setBatchNo("xxx");
		// 流水号：申请唯一标识
		req.setBusinessId(UUID.fastUUID().toString());
		// 4-资审-担保人
		req.setBusinessType(RiskReq.BUSINESS_TYPE_4);

		// 预审都传预审订单号
		req.setPreOrderId("ys2510120050");
		req.setOrderId("ys2510120050");
		// 从产品取-业务模式: XFR-消费融 CZR-车主融
		req.setModelType("XFR");

		MainLoanReq mainLoan = new MainLoanReq();
		req.setMainLoan(mainLoan);
		mainLoan.setNAME("张涛001");
		mainLoan.setCUST_ID_NO("610124200110025695");

		CsCreditReq creditReq = new CsCreditReq();
		String idCardNo = mainLoan.getCUST_ID_NO();
		// 610124200110025695 身份证后四位 5695
		// CL-3-13-5695-20251013-00038
		String busiReqId = String.format("CL-3-13-%s-%s-%s",
				// 身份证后四位
				StrUtil.subSuf(idCardNo, idCardNo.length()-4),
				DateUtil.format(DateUtil.date(), "yyyyMMdd"),
				// 5位自增流水号 需要Seq工具类
				"00001");
		creditReq.setBusinessRequestId(busiReqId);
		req.setCreditReq(creditReq);
		// 征信授权通过日期 yyyy-MM-dd
		creditReq.setAuthstartdate("2025-10-13");
		// 征信授权文件路径
		creditReq.setAuthfilepath("retail_sit/2025/05/order/e06e810e15f37597f0a5d5630659a4ce/applicantComprehensiveAuth/09581c4077a54b919c048412f7c4328c.jpg");

		return req;
	}

	/**
	 * 提报人员-参数构造示例
	 * @return
	 */
	private static RiskReq reporterCallRisk() {
		RiskReq req = new RiskReq();
		// 批次号
		req.setBatchNo("xxx");
		// 流水号：申请唯一标识
		req.setBusinessId(UUID.fastUUID().toString());
		// 5-提报人员
		req.setBusinessType(RiskReq.BUSINESS_TYPE_5);


		// 从产品取-业务模式: XFR-消费融 CZR-车主融
		req.setModelType("XFR");

		MainLoanReq mainLoan = new MainLoanReq();
		req.setMainLoan(mainLoan);
		mainLoan.setNAME("张涛001");
		mainLoan.setCUST_ID_NO("610124200110025695");

		CsCreditReq creditReq = new CsCreditReq();
		String idCardNo = mainLoan.getCUST_ID_NO();
		// 610124200110025695 身份证后四位 5695
		// CL-3-13-5695-20251013-00038
		String busiReqId = String.format("CL-3-13-%s-%s-%s",
				// 身份证后四位
				StrUtil.subSuf(idCardNo, idCardNo.length()-4),
				DateUtil.format(DateUtil.date(), "yyyyMMdd"),
				// 5位自增流水号 需要Seq工具类
				"00001");
		creditReq.setBusinessRequestId(busiReqId);
		req.setCreditReq(creditReq);
		// 征信授权通过日期 yyyy-MM-dd
		creditReq.setAuthstartdate("2025-10-13");
		// 征信授权文件路径
		creditReq.setAuthfilepath("retail_sit/2025/05/order/e06e810e15f37597f0a5d5630659a4ce/applicantComprehensiveAuth/09581c4077a54b919c048412f7c4328c.jpg");

		return req;
	}

	private String buildBusineeRequestId(String idCardNo) throws BusinessException {
		String today = DateUtil.format(DateUtil.date(), "yyyyMMdd");
		String redisKey = "SEQ_ZXQUERY:"+today;
		Long currentNum = redisOperateService.getStr().incr(redisKey, 1L);
		if(currentNum==null){
			throw new RuntimeException("生成流水号失败, Redis操作异常");
		}
		if(currentNum==1){
			long expireMillis = DateUtil.endOfDay(new Date()).getTime() - System.currentTimeMillis() + 1000;
			redisOperateService.getStr().expire(redisKey, expireMillis, TimeUnit.MILLISECONDS);
		}
		return String.format("CL-3-13-%s-%s-%s", StrUtil.subSuf(idCardNo, idCardNo.length()-4),
				today, StrUtil.padPre(currentNum.toString(), 5, '0'));
	}

}