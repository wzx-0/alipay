package cn.seehoo.spg.bizcom.gps.client;

import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.hutool.http.ContentType;
import cn.seehoo.spg.bizcom.gps.dto.GpsUninstallDTO;
import cn.seehoo.spg.bizcom.utils.SeqUtil;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;

/**
 * @author chenjun
 * 
 * 拆机接口实现
 */
public class ZRGpsUninstallBillClient extends ZRGpsBaseClient implements GpsUninstallBillClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZRGpsUninstallBillClient.class);
	/** GPS拆机工单URL */
	private static final String GPS_UNINSTALL_URL = "/api/ApiPlat/AddDismantleByCustomer";
	@Autowired
	private RedisOperateService redis;
	@Autowired
	private RequestLogClient requestLogClient;

	@Override
	public boolean createGpsInstallPlan(GpsUninstallDTO gbcdto) throws BusinessException {
		//参数校验
		gbcdto.checkParams();
		String request = "";
		String body = "";
		boolean status = false;
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[中瑞GPS]，取消工单，触发mock机制，直接返回null, 申请编号={}", gbcdto.getNewAppCode());
				return true;
			}
			LOGGER.info(">>>>>>[中瑞GPS]，创建GPS拆机工单，申请编号={}", gbcdto.getNewAppCode());
			// header
			conf.setScnNo("A09");
			conf.setTransCode(GPS_UNINSTALL_URL);
			Map<String, String> headers = SeqUtil.buildReqHeader(conf.getSvcNo(), 
			conf.getScnNo(), conf.getTransCode(), conf.getReqSysId(), redis);
			headers.put("Content-Type", ContentType.JSON.toString());
			headers.put("appKey", conf.getAppKey());
			request = JSON.toJSONString(gbcdto);
			// body
			String url = conf.getHost() + GPS_UNINSTALL_URL;
			String proxy = conf.getProxy();
			byte[] requestBody = JSON.toJSONString(gbcdto).getBytes("UTF-8");
			LOGGER.info(">>>>>>[中瑞GPS]，创建GPS拆机工单，入参={}", requestBody);
			body = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, requestBody, null, proxy);
			LOGGER.info(">>>>>>[中瑞GPS]，创建GPS拆机工单，出参={}", body);
			status = this.checkResult(body);
			return status;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[中瑞GPS]，创建GPS拆机工单异常，e={}", e);
			return false;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录拆机新增接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.ZR);
				requestLogSaveDto.setInterfaceName("拆机新增接口");
				requestLogSaveDto.setUrl(conf.getHost() + GPS_UNINSTALL_URL);
				requestLogSaveDto.setRequestMsg(request);
				requestLogSaveDto.setResponseMsg(body);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(body)) {
					requestLogSaveDto.setReason((String) JSONUtil.parseObj(body).get("AllMessages"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录拆机新增接口调用日志失败:{}", e);
			}
		}
	}
}