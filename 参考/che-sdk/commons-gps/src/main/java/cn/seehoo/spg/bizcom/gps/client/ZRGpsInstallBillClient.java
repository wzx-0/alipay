package cn.seehoo.spg.bizcom.gps.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import cn.seehoo.spg.bizcom.gps.dto.GpsBillGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.seehoo.spg.bizcom.gps.dto.GpsBillCreateDTO;
import cn.seehoo.spg.bizcom.gps.dto.GpsBillUpdateDTO;
import cn.seehoo.spg.bizcom.gps.exception.OrderBillException;
import cn.seehoo.spg.bizcom.gps.model.GpsInstallBill;
import cn.seehoo.spg.bizcom.gps.model.GpsInstallBillFile;
import cn.seehoo.spg.bizcom.gps.model.GpsLocation;
import cn.seehoo.spg.bizcom.utils.SeqUtil;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;

/**
 * @author chenjun
 * 
 * 状态机接口
 */
public class ZRGpsInstallBillClient extends ZRGpsBaseClient implements GpsInstallBillClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZRGpsInstallBillClient.class);
	/** GPS工单取消接口 */
	private static final String GPS_CANCEL_URL = "/api/ApiPlat/CancelVpsAddOrder";
	/** GPS工单更新接口 */
	private static final String GPS_UPDATE_URL = "/api/ApiPlat/EditVpsOrderExternal";
	/** GPS工单创建接口 */
	private static final String GPS_CREATE_URL = "/api/ApiPlat/AddOrderWithVpsInfos";
	/** GPS工单查询接口 */
	private static final String GPS_GET_URL = "/api/ApiPlat/GetBaseOrderInfoByAppCode";
	/** GPS工单附件查询 */
	private static final String GPSFILES_GET_URL = "/api/ApiPlat/GetImgByAppcode";
	/** GPS状态查询 */
	private static final String GPSINFO_GET_URL = "/api/ApiPlat/GetAllEqPositionsByAppCode";
	@Autowired
	private RedisOperateService redis;
	@Autowired
	private RequestLogClient requestLogClient;

	@Override
	public boolean updateGpsInstallBill(GpsBillUpdateDTO gbudto) throws BusinessException {
		//参数校验
		gbudto.checkParams();
		String request = "";
		String body = "";
		boolean status = false;
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，更新GPS安装工单，触发mock机制，申请编号={}", gbudto.getAppCode());
				return true;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，更新GPS安装工单，申请编号={}", gbudto.getAppCode());
			
			// 请求头
			conf.setScnNo("A04");
			conf.setTransCode(GPS_UPDATE_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
			conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			String url = conf.getHost() + GPS_UPDATE_URL;
			String proxy = conf.getProxy();
			request = JSON.toJSONString(gbudto);
			// body
			byte[] requestBody = JSON.toJSONString(gbudto).getBytes("UTF-8");
			LOGGER.info(">>>>>>[中瑞GPS]，更新GPS安装工单，入参={}", requestBody);
			body = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, requestBody, null, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，更新GPS安装工单，出参={}", body);
			status = this.checkResult(body);
		    return status;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，更新GPS安装工单异常，e={}", e);
			return false;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录根据申请编号修改工单基本信息接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.ZR);
				requestLogSaveDto.setInterfaceName("根据申请编号修改工单基本信息");
				requestLogSaveDto.setUrl(conf.getHost() + GPS_UPDATE_URL);
				requestLogSaveDto.setRequestMsg(request);
				requestLogSaveDto.setResponseMsg(body);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(body)) {
					requestLogSaveDto.setReason(JSON.parseObject(body).getString("AllMessages"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录根据申请编号修改工单基本信息接口调用日志失败:{}", e);
			}
		}
	}

	@Override
	public boolean createGpsInstallPlan(GpsBillCreateDTO gbcdto) throws BusinessException {
		//参数校验
		gbcdto.checkParams();
		String request = "";
		String body = "";
		boolean status = false;
		try {
			String appCode = gbcdto.getAppCode();
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，创建GPS安装工单，触发mock机制，申请编号={}", appCode);
				return true;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，创建GPS安装工单，申请编号={}", appCode);
			// 查询工单
			GpsBillGetDTO gbgdto = new GpsBillGetDTO(appCode,null);
			GpsInstallBill exist = getGpsInstallPlan(gbgdto);
			if (exist!= null) {
				return true;
			}
			// header
			conf.setScnNo("A01");
			conf.setTransCode(GPS_CREATE_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
			conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			request = JSON.toJSONString(gbcdto);
			// body
			String url = conf.getHost() + GPS_CREATE_URL;
			String proxy = conf.getProxy();
			byte[] requestBody = JSON.toJSONString(gbcdto).getBytes("UTF-8");
			LOGGER.info(">>>>>>[中瑞GPS]，创建GPS安装工单，入参={}", requestBody);
			body = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, requestBody, null, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，更新GPS安装工单，出参={}", body);
			status = this.checkResult(body);
		    return status;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，创建GPS安装工单异常，e={}", e);
			return false;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录工单新增接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.ZR);
				requestLogSaveDto.setInterfaceName("工单新增接口（派单）");
				requestLogSaveDto.setUrl(conf.getHost() + GPS_CREATE_URL);
				requestLogSaveDto.setRequestMsg(request);
				requestLogSaveDto.setResponseMsg(body);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(body)) {
					requestLogSaveDto.setReason(JSON.parseObject(body).getString("AllMessages"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录工单新增接口调用日志失败:{}", e);
			}
		}
	}

	@Override
	public GpsInstallBill getGpsInstallPlan(GpsBillGetDTO gbgdto) throws BusinessException {
		String appCode = gbgdto.getAppCode();
		if (StrUtil.isEmpty(appCode)) {
			throw new BusinessException(OrderBillException.APP_CODE_NULL);
		}
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，查询GPS安装工单，触发mock机制，申请编号={}", appCode);
				return null;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS安装工单，申请编号={}", appCode);
			// header
			conf.setScnNo("A07");
			conf.setTransCode(GPS_GET_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
			conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			// body
			String url = conf.getHost() + GPS_GET_URL;
			String proxy = conf.getProxy();
			Map<String, Object> form = new HashMap<>();
			form.put("appcode", appCode);
			String type = gbgdto.getType();
			if (null != type){
				form.put("ordertype", type);
			}
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS安装工单，入参={}", form);
			String body = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, form, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS安装工单，出参={}", body);
		    if (!this.checkResult(body)) {
		    	return null;
		    }
		    JSONObject data = JSON.parseObject(body).getJSONObject("Data");
		    String orderStatus = data.getString("OrderStatus");
		    String actualInstallName = data.getString("ActualInstallName");
		    Boolean deleted = data.getBoolean("Deleted");
		    GpsInstallBill gipb = new GpsInstallBill();
		    gipb.setAppCode(appCode);
		    gipb.setDeleted(deleted);
		    gipb.setOrderStatus(orderStatus);
		    gipb.setActualInstallName(actualInstallName);
		    return gipb;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，查询GPS安装工单异常，e={}", e);
			return null;
		}
	}

	@Override
	public boolean cancelGpsInstallPlan(String appCode) throws BusinessException {
		if (StrUtil.isEmpty(appCode)) {
			throw new BusinessException(OrderBillException.APP_CODE_NULL);
		}
		String request = "";
		String body = "";
		boolean status = false;
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，取消工单，触发mock机制，直接返回null, 申请编号={}", appCode);
				return true;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，取消工单，申请编号={}", appCode);
			// header
			conf.setScnNo("A03");
			conf.setTransCode(GPS_CANCEL_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(),
			conf.getScnNo(), conf.getTransCode(),conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			String url = conf.getHost() + GPS_CANCEL_URL;
			String proxy = conf.getProxy();
			// body
			Map<String, Object> map = new HashMap<>();
			map.put("appcode", appCode);
			request = JSON.toJSONString(map);
			byte[] requestBody = JSON.toJSONString(map).getBytes("UTF-8");
			LOGGER.info(">>>>>>[中瑞GPS]，取消工单，入参={}", requestBody);
			body = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, requestBody, null, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，取消工单，出参={}", body);
			status = checkResult(body);
			return status;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，取消中瑞GPS安装工单异常，e={}", e);
			return false;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录根据申请编号取消工单接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.ZR);
				requestLogSaveDto.setInterfaceName("根据申请编号取消工单");
				requestLogSaveDto.setUrl(conf.getHost() + GPS_CANCEL_URL);
				requestLogSaveDto.setRequestMsg(request);
				requestLogSaveDto.setResponseMsg(body);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(body)) {
					requestLogSaveDto.setReason(JSON.parseObject(body).getString("AllMessages"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录根据申请编号取消工单接口调用日志失败:{}", e);
			}
		}
	}
	
//	public static void main(String[] args) {
//		ZRGpsConfiguration conf = new ZRGpsConfiguration();
//		conf.setHost("http://gatewayapi.lunztech.cn");
//		conf.setAppKey("2C6074D0-C8E4-4BA4-A50A-E3A46EAD394A");
//		conf.setProxy("192.168.88.198:28080");
//		conf.setProductId("86645812-df32-1757-b364-2bf0e656a4be");
//		
//		ZRGpsInstallBillClient client = new ZRGpsInstallBillClient();
//		client.conf = conf;
//		// 创建工单
//		GpsBillCreateDTO gbcdto = new GpsBillCreateDTO();
//		gbcdto.setAppCode("test001006");
//		gbcdto.setDistrictCode("530800");
//		gbcdto.setInstallAdd("上海");
//		gbcdto.setInstallTime("2024-01-29 11:13");
//		gbcdto.setLinkMan("zhangsan");
//		gbcdto.setLinkPhone("lisi");
//		gbcdto.setShopName("dian001");
//		gbcdto.setUserName("wangwu");
//		gbcdto.setUserPhone("15212341234");
//		gbcdto.setVIN("12312312312312");
////		boolean flag = client.createGpsInstallPlan(gbcdto);
////		System.out.println(flag);
//		// 查询工单
////		GpsInstallBill gib = client.getGpsInstallPlan("test001006");
////		System.out.println(gib);
//		// 更新工单
////		GpsBillUpdateDTO gbudto = new GpsBillUpdateDTO();
////		gbudto.setAppCode("test001005");
////		gbudto.setDistrictCode("630100");
////		gbudto.setInstallAdd("西宁市");
////		gbudto.setInstallTime("2024-01-30 11:13");
////		gbudto.setLinkMan("cj");
////		gbudto.setLinkPhone("12512331231");
////		gbudto.setShopName("beijing");
////		boolean flag2 = client.updateGpsInstallBill(gbudto);
////		System.out.println(flag2);
//		// 取消工单
////		boolean result = client.cancelGpsInstallPlan("test001005");
////		System.out.println(result);
//		// 获取附件
////		client.getGpsInstallPlanFiles("230115669");
//		// 获取定位数据
//		List<GpsStatusGetDTO> gsgdtos = new ArrayList<>();
//		GpsStatusGetDTO g1 = new GpsStatusGetDTO();
//		g1.setImei("13710343916");
//		GpsStatusGetDTO g2 = new GpsStatusGetDTO();
//		g2.setImei("50964142348");
//		gsgdtos.add(g1);
//		gsgdtos.add(g2);
//		client.getGpsLocations("2019000193");
//	}

	@Override
	public List<GpsInstallBillFile> getGpsInstallPlanFiles(String appCode) throws BusinessException {
		if (StrUtil.isEmpty(appCode)) {
			throw new BusinessException(OrderBillException.APP_CODE_NULL);
		}
		String request = "";
		String body = "";
		boolean status = false;
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，查询GPS安装工单附件，触发mock机制，直接返回null,申请编号={}", appCode);
				return null;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS安装工单附件，申请编号={}", appCode);
			// header
			conf.setScnNo("A08");
			conf.setTransCode(GPSFILES_GET_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
			conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			// body
			String url = conf.getHost() + GPSFILES_GET_URL;
			String proxy = conf.getProxy();
			Map<String, Object> form = new HashMap<>();
			form.put("appcode", appCode);
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS安装工单附件，入参={}", form);
			body = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, form, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS安装工单附件，出参={}", body);
			status = this.checkResult(body);
		    if (!status) {
		         return null;
		    }
		    JSONArray data = JSON.parseObject(body).getJSONArray("Data");
		    List<GpsInstallBillFile> list = data.toJavaList(GpsInstallBillFile.class);
			return list;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，查询GPS安装工单附件异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录根据申请编号查询工单图片信息接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.ZR);
				requestLogSaveDto.setInterfaceName("根据申请编号查询工单图片信息");
				requestLogSaveDto.setUrl(conf.getHost() + GPSFILES_GET_URL);
				requestLogSaveDto.setResponseMsg(body);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(body)) {
					requestLogSaveDto.setReason(JSON.parseObject(body).getString("AllMessages"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录根据申请编号查询工单图片信息接口调用日志失败:{}", e);
			}
		}
	}

	@Override
	public List<GpsLocation> getGpsLocations(String appCode) throws BusinessException {
		if (StrUtil.isEmpty(appCode)) {
			throw new BusinessException(OrderBillException.APP_CODE_NULL);
		}
		String request = "";
		String rsp = "";
		boolean status = false;
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，查询GPS状态，触发mock机制，直接返回null, 申请编号={}", appCode);
				return null;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS状态，申请编号={}", appCode);
			// header
			conf.setScnNo("A05");
			conf.setTransCode(GPSINFO_GET_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
			conf.getScnNo(), conf.getTransCode(),conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			// body
			String url = conf.getHost() + GPSINFO_GET_URL;
			String proxy = conf.getProxy();
			Map<String, Object> form = new HashMap<>();
			form.put("appcode", appCode);
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS状态，入参={}", form);
			rsp = HttpUtils.request(url, HttpUtils.METHOD_GET, headers, null, form, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，查询GPS状态，出参={}", rsp);
			status = this.checkResult(rsp);
			if (!status) {
				return null;
			}
		    JSONArray data = JSON.parseObject(rsp).getJSONObject("Data").getJSONArray("postions");
		    List<GpsLocation> list = data.toJavaList(GpsLocation.class);
			return list;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，查询GPS安装工单状态异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录查询GPS状态接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.ZR);
				requestLogSaveDto.setInterfaceName("查询GPS状态");
				requestLogSaveDto.setUrl(conf.getHost() + GPSINFO_GET_URL);
				requestLogSaveDto.setResponseMsg(rsp);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(rsp)) {
					requestLogSaveDto.setReason(JSON.parseObject(rsp).getString("AllMessages"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录查询GPS状态接口调用日志失败:{}", e);
			}
		}
	}
}