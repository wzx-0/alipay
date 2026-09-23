package cn.seehoo.spg.bizcom.qcc.client;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.*;
import cn.seehoo.spg.bizcom.qcc.model.*;
import cn.seehoo.spg.bizcom.qcc.vo.CompanyVO;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.seehoo.spg.bizcom.utils.SeqUtil;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;

/**
 * @author chenjun
 * 
 * 状态机接口
 */
public class QccCompanyClient extends QccBaseClient implements CompanyClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(QccCompanyClient.class);
	/** 企查查工商注册信息查询接口 */
	private static final String QCC_COMPANY_URL = "/ECIV4/GetBasicDetailsByName";
	/** 模糊查询公司信息 */
	private static final String QCC_COMPANY_FUZZYURL = "/FuzzySearch/GetList";
	/** 企业综合风险排查 */
	private static final String QCC_COMPANY_RISK_SCAN_URL = "/RiskControl/Scan";
	/** 经营异常核查 */
	private static final String QCC_EXCEPTION_CHECK_URL = "/ExceptionCheck/GetList";
	/** 失信核查 */
	private static final String QCC_SHIXIN_CHECK_URL = "/ShixinCheck/GetList";
	/** 严重违法核查 */
	private static final String QCC_SERIOUSILLEGAL_CHECK_URL = "/SeriousIllegalCheck/GetList";
	/** 惩戒名单核查 */
	private static final String QCC_DISCIPLINARY_CHECK_URL = "/DisciplinaryCheck/GetList";
	@Autowired
	private RedisOperateService redis;
	@Autowired
	private RequestLogClient requestLogClient;
	/**
	 * 有效经营状态列表
	 */
	public static final List<String> VALID_BUSINESS_STATUSES = Arrays.asList(
			"存续（在营、开业、在册）",
			"开业",
			"在营（开业）企业",
			"存续",
			"在业",
			"迁出",
			"在册",
			"在营（开业）",
			"在营",
			"登记成立",
			"存续（开业）",
			"正常在业",
			"迁往市外",
			"其他",
			"已开业",
			"正常",
			"存续(在营、开业、在册)",
			"开业/正常经营",
			"个体转企业",
			"正常执业",
			"存续(经营正常)",
			"存续（在营、开业 、在册）",
			"已迁出企业",
			"迁入",
			"经营期限届满",
			"存续（经营正常）",
			"异地迁入",
			"开业（存续）"
	);

	/**
	 * 无效经营状态列表
	 */
	public static final List<String> INVALID_BUSINESS_STATUSES = Arrays.asList(
			"个体暂时吊销",
			"迁移异地",
			"待迁入",
			"迁出注销",
			"设立登记中",
			"非正常户",
			"废止",
			"名称核准",
			"开业登记中",
			"证照管理登记中",
			"迁出  迁入地工商局",
			"迁出迁入地工商局",
			"变更登记中"
	);
	@Override
	public CompayInfo getCompanyInfo(String keyword) throws BusinessException {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，查询公司工商注册信息，触发mock，直接返回NULL，关键字={}", keyword);
			return mock();
		}
		if (StrUtil.isEmpty(keyword)) {
			throw new BusinessException("查询关键字为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try {
			String detailAppKey = conf.getDetailAppKey();
			String detailSecretkey = conf.getDetailSecretkey();
			LOGGER.info(">>>>>>[企查查]，查询公司工商注册信息，关键字={}", keyword);
			// header
			String[] autherHeader = RandomAuthentHeader(detailAppKey, detailSecretkey);
			conf.setTransCode(QCC_COMPANY_URL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_COMPANY_URL + "?key=" + detailAppKey + "&keyword=" + keyword;
			String proxy = conf.getProxy();
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			status = this.checkResult(rsp);
			if (!status) {
				CompayInfo compayInfo = new CompayInfo();
				compayInfo.setReqStatus("0");
				int statusCode = JSON.parseObject(rsp).getIntValue("Status");
				if (201 == statusCode) {
					compayInfo.setMsg("查询不到该企业信息");
				} else {
					String msg = JSON.parseObject(rsp).getString("Message");
					compayInfo.setMsg(msg);
				}
				return compayInfo;
			}
			JSONObject result = JSON.parseObject(rsp).getJSONObject("Result");
			CompayInfo ci = result.toJavaObject(CompayInfo.class);
			ci.setReqStatus("1");
			if (StrUtil.isNotBlank(rsp)) {
				String msg = JSON.parseObject(rsp).getString("Message");
				ci.setMsg(msg);
			}
			if (ObjectUtil.isEmpty(ci.getRegisteredCapital())) {
				ci.setRegisteredCapital("0");
			}
			JSONObject area = result.getJSONObject("Area");
			String province = area.getString("Province");
			String city = area.getString("City");
			String country = area.getString("County");
			ci.setProvince(province);
			ci.setCity(city);
			ci.setCounty(country);
			LOGGER.debug(">>>>>>[企查查]，查询到的公司工商注册信息，信息={}", ci);
			return ci;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，查询公司工商注册信息异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录企业工商信息查询接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("企业工商信息查询");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录企业工商信息查询接口调用日志失败:{}", e);
			}
		}
	}

	@Override
	public List<CompayInfo> searchCompanyInfos(String keyword) throws BusinessException {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，模糊查询公司信息，触发mock，直接返回NULL，关键字={}", keyword);
			return Collections.singletonList(mock());
		}
		if (StrUtil.isEmpty(keyword)) {
			throw new BusinessException("查询关键字为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try {
			String listAppKey = conf.getListAppKey();
			LOGGER.info(">>>>>>[企查查]，模糊查询公司信息，关键字={}", keyword);
			// header
			String[] autherHeader = RandomAuthentHeader(listAppKey, conf.getListSecretkey());
			conf.setTransCode(QCC_COMPANY_FUZZYURL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_COMPANY_FUZZYURL + "?key=" + listAppKey + "&searchKey=" + keyword;
			String proxy = conf.getProxy();
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			status = this.checkResult(rsp);
			if (!status) {
				return new ArrayList<>();
			}
			JSONArray result = JSON.parseObject(rsp).getJSONArray("Result");
			List<CompayInfo> cis = result.toJavaList(CompayInfo.class);
			LOGGER.debug(">>>>>>[企查查]，查询到的公司工商注册信息，信息={}", cis);
			return cis;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，查询公司工商注册信息异常，e={}", e);
			return new ArrayList<>();
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录模糊查询接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("模糊查询");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录模糊查询接口调用日志失败,", e);
			}
		}
	}

	/**
	 * 企业综合风险排查
	 * @param idNo  统一社会信用代码
	 * @return
	 */

	@Override
	public CompayInfo enterpriseCheck(String idNo) throws BusinessException {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，企业综合风险排查，触发mock，直接返回成功，统一社会信用代码={}", idNo);
			return mock();
		}
		if (StrUtil.isEmpty(idNo)) {
			throw new BusinessException("统一社会信用代码为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try {
			String detailAppKey = conf.getDetailAppKey();
			String detailSecretkey = conf.getDetailSecretkey();
			LOGGER.info(">>>>>>[企查查]，企业综合风险排查，统一社会信用代码={}", idNo);
			// header
			String[] autherHeader = RandomAuthentHeader(detailAppKey, detailSecretkey);
			conf.setTransCode(QCC_COMPANY_RISK_SCAN_URL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(),
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_COMPANY_RISK_SCAN_URL + "?key=" + detailAppKey + "&searchKey=" + idNo;
			String proxy = conf.getProxy();
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			LOGGER.debug(">>>>>>[企查查]，企业综合风险排查，信息={}", rsp);
			CommonsRes<CompanyOverViewInfoRes> commonsRes = JSON.parseObject(rsp, new TypeReference<CommonsRes<CompanyOverViewInfoRes>>() {
			});
			status = commonsRes.isSuccess() &&  ObjUtil.isNotEmpty(commonsRes.getResult()) && ObjUtil.equal(commonsRes.getResult().getVerifyResult(),1);
			if (!status){
				return null;
			}
			CompayInfo ci = BeanUtil.copyProperties(commonsRes.getResult().getData(),CompayInfo.class);
			if (ObjectUtil.isEmpty(ci.getRegisteredCapital())) {
				ci.setRegisteredCapital("0");
			}
			CompanyOverViewInfoRes.Area area = commonsRes.getResult().getData().getArea();
			ci.setProvince(area.getProvince());
			ci.setCity(area.getCity());
			ci.setCounty(area.getCounty());
			return ci;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，查询企业综合风险排查异常,", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录企业综合风险排查接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("企业综合风险排查");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录企业综合风险排查接口调用日志失败,", e);
			}
		}
	}

	@Override
	public List<ExceptionCheckInfo> exceptionCheck(String orderCode, String searchKey, ExceptionCheckHisSaveDto dto) {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，经营异常核查，触发mock，直接返回NULL，关键字={}", searchKey);
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			dto.setOrderNumber("mockOrderNumber"+System.currentTimeMillis());
			dto.setVerifyResult(1);
			return Collections.singletonList(ecmock());
		}
		if (StrUtil.isEmpty(searchKey)) {
			throw new BusinessException("查询关键字为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try{
			String detailAppKey = conf.getDetailAppKey();
			String detailSecretkey = conf.getDetailSecretkey();
			LOGGER.info(">>>>>>[企查查]，经营异常核查，搜索关键词={}", searchKey);
			// header
			String[] autherHeader = RandomAuthentHeader(detailAppKey, detailSecretkey);
			conf.setTransCode(QCC_EXCEPTION_CHECK_URL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(),
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_EXCEPTION_CHECK_URL + "?key=" + detailAppKey + "&searchKey=" + searchKey;
			String proxy = conf.getProxy();
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			LOGGER.debug(">>>>>>[企查查]，异常经营核查，信息={}", rsp);
			status = this.checkResult(rsp);
			if (!status) {
				throw new BusinessException(">>>>>>[企查查], 企查查调用结果失败");
			}
			String orderNumber = JSON.parseObject(rsp).getString("OrderNumber");
			dto.setOrderNumber(orderNumber);
			JSONObject result = JSON.parseObject(rsp).getJSONObject("Result");
			int verifyResult = result.getIntValue("VerifyResult");
			dto.setVerifyResult(verifyResult);
			if (verifyResult == 0) {
				List<ExceptionCheckInfo> eci = new ArrayList<>();
				ExceptionCheckInfo exceptionCheckInfo = new ExceptionCheckInfo();
				exceptionCheckInfo.setAddReason("");
				exceptionCheckInfo.setAddDate("");
				exceptionCheckInfo.setRomoveReason("");
				exceptionCheckInfo.setRemoveDate("");
				exceptionCheckInfo.setDecisionOffice("");
				exceptionCheckInfo.setRemoveDecisionOffice("");
				eci.add(exceptionCheckInfo);
				return eci;
			}
			JSONArray data = result.getJSONArray("Data");
			List<ExceptionCheckInfo> eci = new ArrayList<>();
			if(ObjectUtil.isNotEmpty(data)){
				for(int i = 0;i<data.size();i++){
					ExceptionCheckInfo exceptionCheckInfo = new ExceptionCheckInfo();
					JSONObject item = data.getJSONObject(i);
					exceptionCheckInfo.setAddReason(item.getString("AddReason"));
					exceptionCheckInfo.setAddDate(item.getString("AddDate"));
					exceptionCheckInfo.setRomoveReason(item.getString("RomoveReason"));
					exceptionCheckInfo.setRemoveDate(item.getString("RemoveDate"));
					exceptionCheckInfo.setDecisionOffice(item.getString("DecisionOffice"));
					exceptionCheckInfo.setRemoveDecisionOffice(item.getString("RemoveDecisionOffice"));
					eci.add(exceptionCheckInfo);
				}
			}
			LOGGER.debug(">>>>>>[企查查]，查询到的异常经营核查信息，信息={}", eci);
			return eci;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，异常经营核查异常,", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录异常经营核查接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("异常经营核查");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录异常经营核查接口调用日志失败,", e);
			}
		}
	}

	@Override
	public List<ShixinCheckInfo> shixinCheck(String orderCode, String searchKey, ShixinCheckHisSaveDto dto) {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，失信核查，触发mock，直接返回NULL，关键字={}", searchKey);
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			dto.setOrderNumber("mockOrderNumber"+System.currentTimeMillis());
			dto.setVerifyResult(1);
			return Collections.singletonList(shixinmock());
		}
		if (StrUtil.isEmpty(searchKey)) {
			throw new BusinessException("查询关键字为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try{
			String detailAppKey = conf.getDetailAppKey();
			String detailSecretkey = conf.getDetailSecretkey();
			LOGGER.info(">>>>>>[企查查]，失信核查，搜索关键词={}", searchKey);
			// header
			String[] autherHeader = RandomAuthentHeader(detailAppKey, detailSecretkey);
			conf.setTransCode(QCC_SHIXIN_CHECK_URL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(),
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_SHIXIN_CHECK_URL + "?key=" + detailAppKey + "&searchKey=" + searchKey;
			String proxy = conf.getProxy();
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			LOGGER.debug(">>>>>>[企查查]，失信核查，信息={}", rsp);
			status = this.checkResult(rsp);
			if (!status) {
				throw new BusinessException(">>>>>>[企查查], 企查查调用结果失败");
			}
			String orderNumber = JSON.parseObject(rsp).getString("OrderNumber");
			dto.setOrderNumber(orderNumber);
			JSONObject result = JSON.parseObject(rsp).getJSONObject("Result");
			int verifyResult = result.getIntValue("VerifyResult");
			dto.setVerifyResult(verifyResult);
			if (verifyResult == 0) {
				List<ShixinCheckInfo> eci = new ArrayList<>();
				ShixinCheckInfo shixinCheckInfo = new ShixinCheckInfo();
				shixinCheckInfo.setId("");
				shixinCheckInfo.setLianDate("");
				shixinCheckInfo.setAnno("");
				shixinCheckInfo.setExecuteGov("");
				shixinCheckInfo.setExecuteStatus("");
				shixinCheckInfo.setPublicDate("");
				shixinCheckInfo.setExecuteNo("");
				shixinCheckInfo.setActionRemark("");
				shixinCheckInfo.setAmount("");
				eci.add(shixinCheckInfo);
				return eci;
			}
			JSONArray data = result.getJSONArray("Data");
			List<ShixinCheckInfo> eci = new ArrayList<>();
			if(ObjectUtil.isNotEmpty(data)){
				for(int i = 0;i<data.size();i++){
					ShixinCheckInfo shixinCheckInfo = new ShixinCheckInfo();
					JSONObject item = data.getJSONObject(i);
					shixinCheckInfo.setId(item.getString("Id"));
					shixinCheckInfo.setLianDate(item.getString("Liandate"));
					shixinCheckInfo.setAnno(item.getString("Anno"));
					shixinCheckInfo.setExecuteGov(item.getString("Executegov"));
					shixinCheckInfo.setExecuteStatus(item.getString("Executestatus"));
					shixinCheckInfo.setPublicDate(item.getString("Publicdate"));
					shixinCheckInfo.setExecuteNo(item.getString("Executeno"));
					shixinCheckInfo.setActionRemark(item.getString("ActionRemark"));
					shixinCheckInfo.setAmount(item.getString("Amount"));
					eci.add(shixinCheckInfo);
				}
			}
			LOGGER.debug(">>>>>>[企查查]，查询到的失信核查信息，信息={}", eci);
			return eci;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，失信核查异常,", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录失信核查接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("失信核查");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录失信核查接口调用日志失败,", e);
			}
		}
	}

	@Override
	public List<SeriousIllegalCheckInfo> seriousIllegalCheck(String orderCode, String searchKey, SeriousIllegalCheckHisSaveDto dto) {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，严重违法核查，触发mock，直接返回NULL，关键字={}", searchKey);
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			dto.setOrderNumber("mockOrderNumber"+System.currentTimeMillis());
			dto.setVerifyResult(1);
			return Collections.singletonList(seriousillegalmock());
		}
		if (StrUtil.isEmpty(searchKey)) {
			throw new BusinessException("查询关键字为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try{
			String detailAppKey = conf.getDetailAppKey();
			String detailSecretkey = conf.getDetailSecretkey();
			LOGGER.info(">>>>>>[企查查]，严重违法核查，搜索关键词={}", searchKey);
			// header
			String[] autherHeader = RandomAuthentHeader(detailAppKey, detailSecretkey);
			conf.setTransCode(QCC_SERIOUSILLEGAL_CHECK_URL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(),
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_SERIOUSILLEGAL_CHECK_URL + "?key=" + detailAppKey + "&searchKey=" + searchKey;
			String proxy = conf.getProxy();
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			LOGGER.debug(">>>>>>[企查查]，严重违法核查，信息={}", rsp);
			status = this.checkResult(rsp);
			if (!status) {
				throw new BusinessException(">>>>>>[企查查], 企查查调用结果失败");
			}
			String orderNumber = JSON.parseObject(rsp).getString("OrderNumber");
			dto.setOrderNumber(orderNumber);
			JSONObject result = JSON.parseObject(rsp).getJSONObject("Result");
			int verifyResult = result.getIntValue("VerifyResult");
			dto.setVerifyResult(verifyResult);
			if (verifyResult == 0) {
				List<SeriousIllegalCheckInfo> eci = new ArrayList<>();
				SeriousIllegalCheckInfo seriousIllegalCheckInfo = new SeriousIllegalCheckInfo();
				seriousIllegalCheckInfo.setType("");
				seriousIllegalCheckInfo.setAddReason("");
				seriousIllegalCheckInfo.setAddDate("");
				seriousIllegalCheckInfo.setAddOffice("");
				seriousIllegalCheckInfo.setRemoveReason("");
				seriousIllegalCheckInfo.setRemoveDate("");
				seriousIllegalCheckInfo.setRemoveOffice("");
				eci.add(seriousIllegalCheckInfo);
				return eci;
			}
			JSONArray data = result.getJSONArray("Data");
			List<SeriousIllegalCheckInfo> eci = new ArrayList<>();
			if(ObjectUtil.isNotEmpty(data)){
				for(int i = 0;i<data.size();i++){
					SeriousIllegalCheckInfo seriousIllegalCheckInfo = new SeriousIllegalCheckInfo();
					JSONObject item = data.getJSONObject(i);
					seriousIllegalCheckInfo.setType(item.getString("Type"));
					seriousIllegalCheckInfo.setAddReason(item.getString("AddReason"));
					seriousIllegalCheckInfo.setAddDate(item.getString("AddDate"));
					seriousIllegalCheckInfo.setAddOffice(item.getString("AddOffice"));
					seriousIllegalCheckInfo.setRemoveReason(item.getString("RemoveReason"));
					seriousIllegalCheckInfo.setRemoveDate(item.getString("RemoveDate"));
					seriousIllegalCheckInfo.setRemoveOffice(item.getString("RemoveOffice"));
					eci.add(seriousIllegalCheckInfo);
				}
			}
			LOGGER.debug(">>>>>>[企查查]，查询到的严重违法核查信息，信息={}", eci);
			return eci;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，严重违法核查异常,", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录严重违法核查接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("严重违法核查");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录严重违法核查接口调用日志失败,", e);
			}
		}
	}

	@Override
	public List<DisciplinaryCheckInfo> disciplinaryCheck(String orderCode, String searchKey, DisciplinaryCheckHisSaveDto dto) {
		if (conf.checkMock()) {
			LOGGER.info(">>>>>>[企查查]，惩戒名单核查，触发mock，直接返回NULL，关键字={}", searchKey);
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			dto.setOrderNumber("mockOrderNumber"+System.currentTimeMillis());
			dto.setVerifyResult(1);
			return Collections.singletonList(disciplinarymock());
		}
		if (StrUtil.isEmpty(searchKey)) {
			throw new BusinessException("查询关键字为空");
		}
		String req = "";
		String rsp = "";
		boolean status = false;
		String url = "";
		try{
			String detailAppKey = conf.getDetailAppKey();
			String detailSecretkey = conf.getDetailSecretkey();
			LOGGER.info(">>>>>>[企查查]，惩戒名单核查，搜索关键词={}", searchKey);
			// header
			String[] autherHeader = RandomAuthentHeader(detailAppKey, detailSecretkey);
			conf.setTransCode(QCC_DISCIPLINARY_CHECK_URL);
			conf.setScnNo("A01");
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(),
					conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("Token", autherHeader[0]);
			headers.put("Timespan", autherHeader[1]);
			// body
			url = conf.getHost() + QCC_DISCIPLINARY_CHECK_URL + "?key=" + detailAppKey + "&searchKey=" + searchKey;
			String proxy = conf.getProxy();
			dto.setSearchTime(Timestamp.valueOf(LocalDateTime.now()));
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, null, proxy);
			LOGGER.debug(">>>>>>[企查查]，惩戒名单核查，信息={}", rsp);
			status = this.checkResult(rsp);
			if (!status) {
				throw new BusinessException(">>>>>>[企查查], 企查查调用结果失败");
			}
			String orderNumber = JSON.parseObject(rsp).getString("OrderNumber");
			dto.setOrderNumber(orderNumber);
			JSONObject result = JSON.parseObject(rsp).getJSONObject("Result");
			int verifyResult = result.getIntValue("VerifyResult");
			dto.setVerifyResult(verifyResult);
			if (verifyResult == 0) {
				List<DisciplinaryCheckInfo> eci = new ArrayList<>();
				DisciplinaryCheckInfo disciplinaryCheckInfo = new DisciplinaryCheckInfo();
				disciplinaryCheckInfo.setPunishType("");
				disciplinaryCheckInfo.setPunishFiled("");
				disciplinaryCheckInfo.setCaseReason("");
				disciplinaryCheckInfo.setDecisionOffice("");
				disciplinaryCheckInfo.setDecisionDate("");
				disciplinaryCheckInfo.setRemovedDate("");
				eci.add(disciplinaryCheckInfo);
				return eci;
			}
			JSONArray data = result.getJSONArray("Data");
			List<DisciplinaryCheckInfo> eci = new ArrayList<>();
			if(ObjectUtil.isNotEmpty(data)){
				for(int i = 0;i<data.size();i++){
					DisciplinaryCheckInfo disciplinaryCheckInfo = new DisciplinaryCheckInfo();
					JSONObject item = data.getJSONObject(i);
					disciplinaryCheckInfo.setPunishType(item.getString("PunishType"));
					disciplinaryCheckInfo.setPunishFiled(item.getString("PunishFiled"));
					disciplinaryCheckInfo.setCaseReason(item.getString("CaseReason"));
					disciplinaryCheckInfo.setDecisionOffice(item.getString("DecisionOffice"));
					disciplinaryCheckInfo.setDecisionDate(item.getString("DecisionDate"));
					disciplinaryCheckInfo.setRemovedDate(item.getString("RemovedDate"));
					eci.add(disciplinaryCheckInfo);
				}
			}
			LOGGER.debug(">>>>>>[企查查]，查询到的惩戒名单核查信息，信息={}", eci);
			return eci;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[企查查]，惩戒名单核查异常,", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录惩戒名单核查接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.QCC);
				requestLogSaveDto.setInterfaceName("惩戒名单核查");
				requestLogSaveDto.setUrl(url);
				requestLogSaveDto.setRequestMsg(req);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					String msg = JSON.parseObject(rsp).getString("Message");
					requestLogSaveDto.setReason(msg);
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录惩戒名单核查接口调用日志失败,", e);
			}
		}
	}

	private DisciplinaryCheckInfo disciplinarymock() {
		DisciplinaryCheckInfo disciplinaryCheckInfo = new DisciplinaryCheckInfo();
		disciplinaryCheckInfo.setPunishType("拖欠农民工工资失信联合惩戒对象名单");
		disciplinaryCheckInfo.setPunishFiled("劳动用工");
		disciplinaryCheckInfo.setCaseReason("拖欠XXX名农民工工资XXX万元");
		disciplinaryCheckInfo.setDecisionOffice("景德镇市人力资源和社会保障局");
		disciplinaryCheckInfo.setDecisionDate("2026-06-22");
		disciplinaryCheckInfo.setRemovedDate("");
		return disciplinaryCheckInfo;
	}

	public ExceptionCheckInfo ecmock(){
		ExceptionCheckInfo exceptionCheckInfo = new ExceptionCheckInfo();
		exceptionCheckInfo.setAddReason("未依照《企业信息公式暂行条例》第八条规定的期限公式年度报告");
		exceptionCheckInfo.setAddDate("2026-06-16");
		exceptionCheckInfo.setDecisionOffice("北京市场监督管理局");
		exceptionCheckInfo.setRomoveReason("");
		exceptionCheckInfo.setRemoveDate("");
		exceptionCheckInfo.setRemoveDecisionOffice("");
		return exceptionCheckInfo;
	}
	public ShixinCheckInfo shixinmock(){
		ShixinCheckInfo shixinCheckInfo = new ShixinCheckInfo();
		shixinCheckInfo.setId("c2**************432");
		shixinCheckInfo.setLianDate("2021-08-10");
		shixinCheckInfo.setAnno("(2021)京0105执34224号");
		shixinCheckInfo.setExecuteGov("北京市朝阳区人民法院");
		shixinCheckInfo.setExecuteStatus("全部未履行");
		shixinCheckInfo.setPublicDate("2022-01-07");
		shixinCheckInfo.setExecuteNo("(2020)京0105民初51653号");
		shixinCheckInfo.setActionRemark("有履行能力而拒不履行生效法律文书确定义务");
		shixinCheckInfo.setAmount("1098000");
		return shixinCheckInfo;
	}
	private SeriousIllegalCheckInfo seriousillegalmock() {
		SeriousIllegalCheckInfo seriousIllegalCheckInfo = new SeriousIllegalCheckInfo();
		seriousIllegalCheckInfo.setType("");
		seriousIllegalCheckInfo.setAddReason("被列入经营异常名录届满3年仍未履行相关义务的");
		seriousIllegalCheckInfo.setAddDate("2017-12-25");
		seriousIllegalCheckInfo.setAddOffice("山东省工商行政管理局");
		seriousIllegalCheckInfo.setRemoveReason("");
		seriousIllegalCheckInfo.setRemoveDate("");
		seriousIllegalCheckInfo.setRemoveOffice("");
		return seriousIllegalCheckInfo;
	}

	public CompayInfo mock(){
		CompayInfo compayInfo = new CompayInfo();
		compayInfo.setName("武汉市江岸区测试圣哈哈哈哈餐饮馆");
		compayInfo.setTermStart(new Date(System.currentTimeMillis()));
		compayInfo.setTermEnd(new Date(System.currentTimeMillis()));
		compayInfo.setStartDate(new Date(System.currentTimeMillis()));
		compayInfo.setOperName("成瑶");
		compayInfo.setRegisteredCapital("10000");
		compayInfo.setAddress("湖北省武汉市江岸区1号");
		compayInfo.setStatus("存续（在营、开业、在册）");
		compayInfo.setScope("一般项目：房屋拆迁服务");
		compayInfo.setCreditCode("92420102MACW0DXJ9C");
		compayInfo.setProvince("湖北省");
		compayInfo.setCity("武汉市");
		compayInfo.setCounty("江岸区");
		return compayInfo;
	}


//	public static void main(String[] args) {
//		QccConfiguration conf = new QccConfiguration();
//		conf.setAppKey("be37f18dfeb6461c836cf11f3c8d9919");
//		conf.setSecretkey("7D3CCC95E19B88382B3979F78DEAB645");
//		conf.setHost("https://api-sit.hxfl.com.cn:38080/out_qcc");
//		conf.setMock(false);
//		QccCompanyClient client = new QccCompanyClient();
//		client.conf = conf;
//		CompayInfo ci = client.getCompanyInfo("91310115MA1H82GC3T");
//		System.out.println(ci);
//	}
}