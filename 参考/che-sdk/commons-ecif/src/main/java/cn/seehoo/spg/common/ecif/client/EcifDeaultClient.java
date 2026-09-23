package cn.seehoo.spg.common.ecif.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.ecif.config.EcifConfiguration;
import cn.seehoo.spg.common.ecif.model.*;
import cn.seehoo.spg.common.feign.CustomerClient;
import cn.seehoo.spg.common.feign.dto.EcifTurnoverRecordsRes;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Ecif默认实现客户端
 */
@SuppressWarnings("rawtypes")
public class EcifDeaultClient implements EcifClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(EcifDeaultClient.class);

	/**
	 * 精准查询对公客户信息
	 */
	private static final String QUERY_COMPANY_INFO_BY_CERT = "queryCustomer/getCompanyInfoByCertId";

	/**
	 * 新增对公客户
	 */
	private static final String ADD_COMPANY = "addCustomer/company";

	/**
	 * 更新对公客户
	 */
	private static final String UPDATE_COMPANY = "updateCustomer/company";

	/**
	 * 更新对公账户信息
	 */
	private static final String UPDATE_COMPANY_ACCOUNT = "updateCompany/account";

	/**
	 * 精准查询个人客户信息
	 */
	private static final String QUERY_PERSON_INFO_BY_CERT = "queryCustomer/getPersonInfoByCertId";

	/**
	 * 新增个人客户
	 */
	private static final String ADD_PERSON = "addCustomer/person";

	/**
	 * 更新个人客户
	 */
	private static final String UPDATE_PERSON = "updateCustomer/person";

	/**
	 * 个人客户校验接口
	 */
	private static final String CHECK_CUSTOMER_PERSON = "checkCustomer/person";

	/**
	 * 关联方查询
	 */
	private static final String QUERY_PERSON_RELATIVES = "query/person/relatedParties";

	/**
	 * 请求头KEY
	 */
	private static final String REQ_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String REQ_HEADER_CHANNEL = "channel";
	private static final String REQ_HEADER_TIMESTAMP = "timestamp";
	private static final String REQ_HEADER_SIGN = "sign";
	private static final String REQ_HEADER_REQUEST_ID = "requestId";
	private static final String REQ_HEADER_REQUEST_USERNAME = "requestUserName";

	/**
	 * 客户请求默认值 证件类型-E_01：统一社会信用代码
	 */
	private static final String COMPANY_CERT_TYPE_DEFAULT_VALUE = "E_01";
	/**
	 * 客户请求默认值 正式客户
	 */
	private static final String CUST_STATUS_OFFICIAL = "1";
	/**
	 * 客户请求默认值 正常
	 */
	private static final String ENTERPRISE_STATUS_NORMAL = "00";
	/**
	 * 客户请求默认值 人民币
	 */
	private static final String CURRENCY_CNY = "CNY";

	@Autowired
	public EcifConfiguration conf;
	@Autowired
	private CustomerClient customerClient;

	public static void main(String[] args) {

		// Mock Config
		EcifConfiguration conf = new EcifConfiguration();
		conf.setBaseUrl("https://hxcif-sit.hxfl.com.cn/baseUrl/hxcifbase/ecif/openapi/");
		conf.setRequestUserName("admin");
		conf.setSm4Key("xx");
		conf.setChannel("04024");
		conf.setChannelSecret("xx");

		EcifDeaultClient client = new EcifDeaultClient();
		client.conf = conf;

		// 0.个人客户精准查询
		QueryPersonReq personQueryReq = new QueryPersonReq();
		personQueryReq.setCertId("441403199712012849");
		personQueryReq.setCertType("P_02");
		PersonRes personQueryRes = client.queryPersonInfoByCertId(personQueryReq);
		System.out.println(JSON.toJSONString(personQueryRes));
		System.out.println(personQueryRes.getRevision());

		// 1.新增/修改个人客户
		PersonReq personUpdateReq = BeanUtil.copyProperties(personQueryRes, PersonReq.class);
		personUpdateReq.getBasic().setCustName(personUpdateReq.getBasic().getCustName()+"0929");
		PersonRes personUpdateRes = client.addOrUpdatePerson(personUpdateReq);
		System.out.println(JSON.toJSONString(personUpdateRes));
		System.out.println(personUpdateRes.getRevision());

		// 2.个人客户校验
		PersonReq checkReq = BeanUtil.copyProperties(personQueryRes, PersonReq.class);
		PersonRes personCheckRes = client.checkPerson(checkReq);
		System.out.println(JSON.toJSONString(personCheckRes));

		// 3.个人关联方查询
		QueryRelativesReq relaReq = new QueryRelativesReq();
		relaReq.setCertId("441403199712012849");
		PersonRelativesRes personRelativesRes = client.queryPersonRelatives(relaReq);
		System.out.println(JSON.toJSONString(personRelativesRes));

		// 4.对公客户精准查询
		QueryCompanyReq companyReq = new QueryCompanyReq();
		companyReq.setCertId("91321012MA1WYYHR55");
		companyReq.setCertType("E_01");
		CompanyRes queryCompanyRes = client.queryCompanyInfoByCertId(companyReq);
		System.out.println(JSON.toJSONString(queryCompanyRes));
		System.out.println(queryCompanyRes.getRevision());

		// 5.新增/修改对公客户
		CompanyReq addReq = new CompanyReq();
		CompanyBaiscReq basic = new CompanyBaiscReq();
		addReq.setCertType("E_01");
		addReq.setBasic(basic);

		// 统一社会信用代码
		addReq.setCertId("91321012MA1WYYHR55");
		// 合作方/挂靠方/车商编码
		addReq.setCompanyCode("CAXS01");

		basic.setCustName(basic.getCustName()+"0929");
		// merchantType
		basic.setCustCategories("03");
		basic.setRegDistrict("440307");
		basic.setRegAddress("注册详细地址");
		// 注册资本
		basic.setRegCapital(new BigDecimal("10000"));
		// 注册日期
		basic.setRegDate(LocalDate.now());
		// 证件过期日期
		basic.setCertEndDate(LocalDateTimeUtil.parseDate("2099-12-30"));

		CompanyRes addCompanyRes = client.addOrUpdateCompany(addReq);
		System.out.println(JSON.toJSONString(addCompanyRes));
		System.out.println(addCompanyRes.getRevision());

	}

	/**
	 * 对公信息精准查询接口
	 * @param req
	 * @return
	 */
	@Override
	public CompanyRes queryCompanyInfoByCertId(QueryCompanyReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		CompanyRes companyRes = null;
		EcifTurnoverRecordsRes ecifTurnoverRecordsRes = new EcifTurnoverRecordsRes();
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(CompanyRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				companyRes = BeanUtil.copyProperties(resData.getData(), CompanyRes.class);
			}
			return companyRes;
		}

		// 默认参数填充
		if(StrUtil.isEmpty(req.getCertType())){
			req.setCertType(COMPANY_CERT_TYPE_DEFAULT_VALUE);
		}

		String body = null;
		try {
			LOGGER.info(">>>>>>[Ecif]，对公信息精准查询接口，入参={}", JSON.toJSON(req));

			body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(HexUtil.decodeHex(conf.getSm4Key()));
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			ecifTurnoverRecordsRes.setInterfaceName("对公信息精准查询接口");
			ecifTurnoverRecordsRes.setBusinessCode(Optional.ofNullable(req.getCompanyCode()).orElse(req.getCertId()));
			ecifTurnoverRecordsRes.setCustomerName(req.getCustName());
			ecifTurnoverRecordsRes.setOperationType(QUERY_COMPANY_INFO_BY_CERT);
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setRequestTime(LocalDateTime.now());
			String response = HttpUtils.request(conf.getBaseUrl() + QUERY_COMPANY_INFO_BY_CERT, HttpUtils.METHOD_POST,
					headers, encrpt.getBytes(), null, conf.getProxy());
			ecifTurnoverRecordsRes.setResponseTime(LocalDateTime.now());
			String decrpt = cifSm4.decryptStr(response);
			ecifTurnoverRecordsRes.setResponseParameters(decrpt);
			LOGGER.info(">>>>>>[Ecif]，对公信息精准查询接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				ecifTurnoverRecordsRes.setCallResult("2");
				return null;
			}

			companyRes = BeanUtil.copyProperties(
					resData.getData(), CompanyRes.class);
			ecifTurnoverRecordsRes.setEcifCustomerId(companyRes.getCustNo());
			ecifTurnoverRecordsRes.setCallResult("1");
			return companyRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，对公信息精准查询接口异常，e={}", e);
			ecifTurnoverRecordsRes.setCallResult("2");
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setResponseParameters(e.toString());
			return null;
		} finally {
			ecifTurnoverRecordsRes.setRequestDirection("1");
			ecifTurnoverRecordsRes.setRetryFlag("1");
			ecifTurnoverRecordsRes.setLogicDelete("0");
			ecifTurnoverRecordsRes.setCreateBy("admin");
			ecifTurnoverRecordsRes.setUpdateBy("admin");
			ecifTurnoverRecordsRes.setCreateTime(LocalDateTime.now());
			ecifTurnoverRecordsRes.setUpdateTime(LocalDateTime.now());
			customerClient.insertEcifTurnoverRecords(ecifTurnoverRecordsRes);
		}
	}

	/**
	 * 个人-信息精准查询接口
	 *
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public PersonRes queryPersonInfoByCertId(QueryPersonReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		PersonRes personRes = null;
		EcifTurnoverRecordsRes ecifTurnoverRecordsRes = new EcifTurnoverRecordsRes();
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(PersonRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				personRes = BeanUtil.copyProperties(resData.getData(), PersonRes.class);
			}
			return personRes;
		}
		String body = null;
		try {
			LOGGER.info(">>>>>>[Ecif]，个人信息精准查询接口，入参={}", JSON.toJSON(req));
			ecifTurnoverRecordsRes.setInterfaceName("个人信息精准查询接口");

			body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(HexUtil.decodeHex(conf.getSm4Key()));
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			ecifTurnoverRecordsRes.setBusinessCode("");
			ecifTurnoverRecordsRes.setOperationType("getPersonInfoByCertId");
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setRequestTime(LocalDateTime.now());
			String response = HttpUtils.request(conf.getBaseUrl() + QUERY_PERSON_INFO_BY_CERT, HttpUtils.METHOD_POST,
					headers, encrpt.getBytes(), null, conf.getProxy());
			ecifTurnoverRecordsRes.setResponseTime(LocalDateTime.now());
			String decrpt = cifSm4.decryptStr(response);
			ecifTurnoverRecordsRes.setResponseParameters(decrpt);
			LOGGER.info(">>>>>>[Ecif]，个人信息精准查询接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				ecifTurnoverRecordsRes.setCallResult("2");
				return null;
			}

			personRes = BeanUtil.copyProperties(
					resData.getData(), PersonRes.class);
			ecifTurnoverRecordsRes.setCustomerName(personRes.getBasic().getCustName());
			ecifTurnoverRecordsRes.setEcifCustomerId(personRes.getCustNo());
			ecifTurnoverRecordsRes.setCallResult("1");
			return personRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，个人信息精准查询接口异常，e={}", e);
			ecifTurnoverRecordsRes.setCallResult("2");
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setResponseParameters(e.toString());
			return null;
		} finally {
			ecifTurnoverRecordsRes.setRequestDirection("1");
			ecifTurnoverRecordsRes.setRetryFlag("1");
			ecifTurnoverRecordsRes.setLogicDelete("0");
			ecifTurnoverRecordsRes.setCreateBy("admin");
			ecifTurnoverRecordsRes.setUpdateBy("admin");
			ecifTurnoverRecordsRes.setCreateTime(LocalDateTime.now());
			ecifTurnoverRecordsRes.setUpdateTime(LocalDateTime.now());
			customerClient.insertEcifTurnoverRecords(ecifTurnoverRecordsRes);
		}
	}

	/**
	 * 新增/更新对公客户接口
	 *
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public CompanyRes addOrUpdateCompany(CompanyReq req) throws BusinessException {
		LOGGER.info("新增/更新对公客户接口 入参：{}", JSONUtil.toJsonStr(req));
		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		CompanyRes companyRes = null;
		EcifTurnoverRecordsRes ecifTurnoverRecordsRes = new EcifTurnoverRecordsRes();
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(CompanyRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				companyRes = BeanUtil.copyProperties(resData.getData(), CompanyRes.class);
			}
			return companyRes;
		}

		// 默认参数填充
		if(StrUtil.isEmpty(req.getCertType())){
			req.setCertType(COMPANY_CERT_TYPE_DEFAULT_VALUE);
		}
		if(ObjectUtil.isNotNull(req.getBasic())){
			CompanyBaiscReq basic = req.getBasic();
			if(StrUtil.isEmpty(basic.getCustStatus())){
				// 正式客户
				basic.setCustStatus(CUST_STATUS_OFFICIAL);
			}
			if(StrUtil.isEmpty(basic.getEnterpriseStatus())){
				// 正常
				basic.setEnterpriseStatus(ENTERPRISE_STATUS_NORMAL);
			}
			if(StrUtil.isEmpty(basic.getRegCountry())){
				basic.setRegCountry(Locale.CHINA.getCountry());
			}
			if(StrUtil.isEmpty(basic.getRegCapitalCurrency())){
				basic.setRegCapitalCurrency(CURRENCY_CNY);
			}
		}

		// 新增或修改之前精准查询
		QueryCompanyReq query = new QueryCompanyReq();
		query.setCertId(req.getCertId());
		query.setCustName(req.getBasic().getCustName());
		query.setCompanyCode(req.getCompanyCode());
		try{
			CompanyRes queryRes = queryCompanyInfoByCertId(query);
			if(ObjectUtil.isNull(queryRes) || StrUtil.isEmpty(queryRes.getCustNo())){
				// 新增
				req.setRevision(BigDecimal.ONE.intValue());
				req.setDealType(CompanyReq.ADD);
				ecifTurnoverRecordsRes.setInterfaceName("新增对公客户接口");
			}else {
				// 修改
				req.setRevision(queryRes.getRevision());
				req.setDealType(CompanyReq.UPDATE);
				ecifTurnoverRecordsRes.setInterfaceName("修改对公客户接口");
			}
//			if (ObjectUtil.isNotNull(queryRes)){
//				CompanyBaiscRes companyBaiscRes = queryRes.getBasic();
//				if (ObjectUtil.isNotNull(companyBaiscRes)){
//					req.getBasic().setIndustry(companyBaiscRes.getIndustry());
//				}
//			}
		}catch (Exception e){
			LOGGER.error(">>>>>>[Ecif]，新增/修改对公客户精准查询失败", e);
		}

		String body = null;
		try {

			String reqUrl = null;
			if(CompanyReq.ADD.equals(req.getDealType())){
				reqUrl = conf.getBaseUrl() + ADD_COMPANY;
				LOGGER.info(">>>>>>[Ecif]，新增对公客户接口，入参={}", JSON.toJSON(req));
			}else {
				reqUrl = conf.getBaseUrl() + UPDATE_COMPANY;
				LOGGER.info(">>>>>>[Ecif]，更新对公客户接口，入参={}", JSON.toJSON(req));
			}

			body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(HexUtil.decodeHex(conf.getSm4Key()));
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			ecifTurnoverRecordsRes.setBusinessCode(Optional.ofNullable(req.getCompanyCode()).orElse(req.getCertId()));
			ecifTurnoverRecordsRes.setCustomerName(req.getBasic().getCustName());
			ecifTurnoverRecordsRes.setOperationType("addOrUpdateCompany");
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setRequestTime(LocalDateTime.now());
			String response = HttpUtils.request(reqUrl, HttpUtils.METHOD_POST,
					headers, encrpt.getBytes(), null, conf.getProxy());
			ecifTurnoverRecordsRes.setResponseTime(LocalDateTime.now());
			String decrpt = cifSm4.decryptStr(response);
			ecifTurnoverRecordsRes.setResponseParameters(decrpt);
			LOGGER.info(">>>>>>[Ecif]，新增/更新对公客户接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				companyRes = new CompanyRes();
				companyRes.setErrorMsg(resData.getMessage());
				ecifTurnoverRecordsRes.setCallResult("2");
				ecifTurnoverRecordsRes.setRetryFlag("2");
				return companyRes;
			}

			companyRes = BeanUtil.copyProperties(
					resData.getData(), CompanyRes.class);
			ecifTurnoverRecordsRes.setEcifCustomerId(companyRes.getCustNo());
			ecifTurnoverRecordsRes.setCallResult("1");

			return companyRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，新增/更新对公客户接口异常，e={}", e);
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setResponseParameters(e.toString());
			ecifTurnoverRecordsRes.setCallResult("2");
			CompanyRes ompanyRes = new CompanyRes();
			ompanyRes.setErrorMsg("ecif异常");
			return companyRes;
		} finally {
			ecifTurnoverRecordsRes.setRequestDirection("1");
			ecifTurnoverRecordsRes.setRetryFlag("1");
			ecifTurnoverRecordsRes.setLogicDelete("0");
			ecifTurnoverRecordsRes.setCreateBy("admin");
			ecifTurnoverRecordsRes.setUpdateBy("admin");
			ecifTurnoverRecordsRes.setCreateTime(LocalDateTime.now());
			ecifTurnoverRecordsRes.setUpdateTime(LocalDateTime.now());
			customerClient.insertEcifTurnoverRecords(ecifTurnoverRecordsRes);
		}
	}

	/**
	 * 新增/更新个人客户接口
	 *
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public PersonRes addOrUpdatePerson(PersonReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		PersonRes personRes = null;
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(PersonRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				personRes = BeanUtil.copyProperties(resData.getData(), PersonRes.class);
			}
			return personRes;
		}

		// 新增或修改之前精准查询
		QueryPersonReq query = new QueryPersonReq();
		query.setCertId(req.getCertId());
		query.setCertType(req.getCertType());
		query.setCustNo(req.getCustomerEcifNo());
		try{
			PersonRes queryRes = queryPersonInfoByCertId(query);
			if(ObjectUtil.isNull(queryRes)){
				// 新增
				req.setRevision(BigDecimal.ONE.intValue());
				req.setDealType(CompanyReq.ADD);
			}else {
				// 修改
				req.setRevision(queryRes.getRevision());
				req.setDealType(CompanyReq.UPDATE);
			}
		}catch (Exception e){
			LOGGER.error(">>>>>>[Ecif]，新增/修改个人客户精准查询失败", e);
		}

		try {

			String reqUrl = null;
			if(CompanyReq.ADD.equals(req.getDealType())){
				reqUrl = conf.getBaseUrl() + ADD_PERSON;
				LOGGER.info(">>>>>>[Ecif]，新增个人客户接口，入参={}", JSON.toJSON(req));
			}else {
				reqUrl = conf.getBaseUrl() + UPDATE_PERSON;
				LOGGER.info(">>>>>>[Ecif]，更新个人客户接口，入参={}", JSON.toJSON(req));
			}

			String body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(HexUtil.decodeHex(conf.getSm4Key()));
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			String response = HttpUtils.request(reqUrl, HttpUtils.METHOD_POST,
					headers, encrpt.getBytes(), null, conf.getProxy());
			String decrpt = cifSm4.decryptStr(response);
			LOGGER.info(">>>>>>[Ecif]，新增/更新个人客户接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				return null;
			}

			personRes = BeanUtil.copyProperties(
					resData.getData(), PersonRes.class);
			return personRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，新增/更新个人客户接口异常，e={}", e);
			return null;
		}
	}

	/**
	 * 更新对公账户信息
	 *
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public CompanyRes updateCompanyAccount(UpdateCompanyAccountReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		CompanyRes companyRes = null;
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(CompanyRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				companyRes = BeanUtil.copyProperties(resData.getData(), CompanyRes.class);
			}
			return companyRes;
		}

		try {
			LOGGER.info(">>>>>>[Ecif]，更新对公账户信息，入参={}", JSON.toJSON(req));

			String body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(conf.getSm4Key().getBytes());
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			String response = HttpUtils.request(conf.getBaseUrl() + UPDATE_COMPANY_ACCOUNT, HttpUtils.METHOD_POST,
					null, encrpt.getBytes(), null, conf.getProxy());
			String decrpt = cifSm4.decryptStr(response);
			LOGGER.info(">>>>>>[Ecif]，更新对公账户信息接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				return null;
			}

			companyRes = BeanUtil.copyProperties(
					resData.getData(), CompanyRes.class);
			return companyRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，更新对公账户信息接口异常，e={}", e);
			return null;
		}
	}

	/**
	 * 个人客户校验
	 *
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public PersonRes checkPerson(PersonReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		PersonRes personRes = null;
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(PersonRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				personRes = BeanUtil.copyProperties(resData.getData(), PersonRes.class);
			}
			return personRes;
		}

		try {
			LOGGER.info(">>>>>>[Ecif]，个人客户校验接口，入参={}", JSON.toJSON(req));

			String body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(HexUtil.decodeHex(conf.getSm4Key()));
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			String response = HttpUtils.request(conf.getBaseUrl() + CHECK_CUSTOMER_PERSON, HttpUtils.METHOD_POST,
					headers, encrpt.getBytes(), null, conf.getProxy());
			String decrpt = cifSm4.decryptStr(response);
			LOGGER.info(">>>>>>[Ecif]，个人客户校验接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				return null;
			}

			personRes = BeanUtil.copyProperties(
					resData.getData(), PersonRes.class);
			return personRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，个人客户校验接口异常，e={}", e);
			return null;
		}
	}

	/**
	 * 个人客户关联方信息查询
	 *
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public PersonRelativesRes queryPersonRelatives(QueryRelativesReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}

		PersonRelativesRes personRes = null;
		EcifTurnoverRecordsRes ecifTurnoverRecordsRes = new EcifTurnoverRecordsRes();
		if(conf.checkMock()){
			ResData resData = JSON.parseObject(PersonRelativesRes.getMockObj(), ResData.class);
			if(ObjectUtil.isNotNull(resData)){
				personRes = BeanUtil.copyProperties(resData.getData(), PersonRelativesRes.class);
			}
			return personRes;
		}
		String body = null;
		try {
			LOGGER.info(">>>>>>[Ecif]，个人客户关联方信息查询接口，入参={}", JSON.toJSON(req));
			ecifTurnoverRecordsRes.setInterfaceName("个人客户关联方信息查询接口");
			body = JSON.toJSONString(req);
			if(StrUtil.isEmpty(req.getRequestUserName())){
				req.setRequestUserName(conf.getRequestUserName());
			}

			SM4 cifSm4 = SmUtil.sm4(HexUtil.decodeHex(conf.getSm4Key()));
			String encrpt = cifSm4.encryptHex(body);

			long timestamp = Calendar.getInstance().getTimeInMillis();

			// 请求头
			Map<String, String> headers = new HashMap<>();
			// 签名
			String signRaw = SmUtil.sm4(HexUtil.decodeHex(conf.getChannelSecret())).encryptHex(
					String.format("%s%d", conf.getChannel(), timestamp));

			headers.put(REQ_HEADER_CONTENT_TYPE, ContentType.JSON.toString());
			headers.put(REQ_HEADER_CHANNEL, conf.getChannel());
			headers.put(REQ_HEADER_TIMESTAMP, String.valueOf(timestamp));
			headers.put(REQ_HEADER_SIGN, DigestUtil.md5Hex(signRaw).toUpperCase());
			headers.put(REQ_HEADER_REQUEST_ID, UUID.randomUUID().toString());
			headers.put(REQ_HEADER_REQUEST_USERNAME, req.getRequestUserName());

			ecifTurnoverRecordsRes.setBusinessCode(req.getCertId());
			ecifTurnoverRecordsRes.setOperationType("query/person/relatedParties");
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setRequestTime(LocalDateTime.now());
			String response = HttpUtils.request(conf.getBaseUrl() + QUERY_PERSON_RELATIVES, HttpUtils.METHOD_POST,
					headers, encrpt.getBytes(), null, conf.getProxy());
			ecifTurnoverRecordsRes.setResponseTime(LocalDateTime.now());
			String decrpt = cifSm4.decryptStr(response);
			ecifTurnoverRecordsRes.setResponseParameters(decrpt);
			LOGGER.info(">>>>>>[Ecif]，个人客户关联方信息查询接口，响应={}", decrpt);

			ResData resData = JSON.parseObject(decrpt, ResData.class);
			if (!this.checkResult(resData)) {
				ecifTurnoverRecordsRes.setCallResult("2");
				ecifTurnoverRecordsRes.setRetryFlag("2");
				return null;
			}

			personRes = BeanUtil.copyProperties(
					resData.getData(), PersonRelativesRes.class);
			ecifTurnoverRecordsRes.setCustomerName(req.getCustName());
			ecifTurnoverRecordsRes.setCallResult("1");
			ecifTurnoverRecordsRes.setRetryFlag("1");
			return personRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[Ecif]，个人客户关联方信息查询接口异常，e={}", e);
			ecifTurnoverRecordsRes.setCallResult("2");
			ecifTurnoverRecordsRes.setRetryFlag("2");
			ecifTurnoverRecordsRes.setRequestParameters(body);
			ecifTurnoverRecordsRes.setResponseParameters(e.toString());
			return null;
		} finally {
			ecifTurnoverRecordsRes.setRequestDirection("1");
			ecifTurnoverRecordsRes.setLogicDelete("0");
			ecifTurnoverRecordsRes.setCreateBy("admin");
			ecifTurnoverRecordsRes.setUpdateBy("admin");
			ecifTurnoverRecordsRes.setCreateTime(LocalDateTime.now());
			ecifTurnoverRecordsRes.setUpdateTime(LocalDateTime.now());
			customerClient.insertEcifTurnoverRecords(ecifTurnoverRecordsRes);
		}
	}

	protected boolean checkResult(ResData resData) {
		if (!"000000".equals(resData.getReturnCode())) {
			LOGGER.warn(">>>>>>[Ecif], 调用结果失败，msg={}", resData.getMessage());
			return false;
		}
		return true;
	}

}