package cn.seehoo.spg.common.pboc.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.seehoo.spg.base.client.RequestLogClient;
import cn.seehoo.spg.base.constant.SystemNameConstant;
import cn.seehoo.spg.base.dto.RequestLogSaveDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.pboc.config.PbocConfiguration;
import cn.seehoo.spg.common.pboc.model.CreditReportReq;
import cn.seehoo.spg.common.pboc.model.CreditReportRes;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 人行客户端默认实现
 */
public class PbocDeaultClient implements PbocClient {


	private static final Logger LOGGER = LoggerFactory.getLogger(PbocDeaultClient.class);

	/**
	 * 征信报告查询URL
	 */
	private static final String API_QUERY_REPORT_URL = "/hbp/pcInterface/asyncResult.action";

	@Autowired
	public PbocConfiguration conf;
	@Autowired
	public RequestLogClient requestLogClient;

	public static void main(String[] args) {

		PbocConfiguration configuration = new PbocConfiguration();
		configuration.setCreditIpHost("http://103.164.30.84:6009");
		configuration.setUsername("xx");
		configuration.setPassword("xx");
		configuration.setCreditKey("xx");

		// 加密标识：测试环境-false 生产环境-true
		configuration.setSignFlag(false);

		PbocDeaultClient client = new PbocDeaultClient();
		client.conf = configuration;

		CreditReportReq req = new CreditReportReq();
		req.setFlowId("2025082514584128985157");
		CreditReportRes reportRes = client.queryCreditReport(req);
		System.out.println(JSON.toJSONString(reportRes));

	}

	/**
	 * 征信报告查询
	 * @param req
	 * @return
	 */
	@Override
	public CreditReportRes queryCreditReport(CreditReportReq req) throws BusinessException {

		if (ObjectUtil.isEmpty(req)) {
			throw new BusinessException("查询关键字为空");
		}
		String body = "";
		String response = "";
		boolean status = false;
		try {
			LOGGER.info(">>>>>>[PBOC]，征信报告查询接口，入参={}", JSON.toJSON(req));

			CreditReportRes reportRes = null;
			if(conf.checkMock()){
				reportRes = JSONUtil.toBean(new ClassPathResource("pdf.txt").readUtf8Str(),
							CreditReportRes.class);
				return reportRes;
			}

			req.setUsername(conf.getUsername());
			req.setPassword(conf.getPassword());

			body = JSON.toJSONString(req);

			// 加密后报文，上线后报文为加密
			SM4 pbocSm4 = null;
			if(conf.checkSign()){
				pbocSm4 = SmUtil.sm4(conf.getCreditKey().getBytes());
				body = pbocSm4.encryptHex(body);
			}

			Map<String, String> headers = new HashMap<>();
			headers.put("Content-Type", ContentType.JSON.toString());
			response = HttpUtils.request(conf.getCreditIpHost() + API_QUERY_REPORT_URL, HttpUtils.METHOD_POST,
					headers, body.getBytes(), null, conf.getProxy());
			if(conf.checkSign()){
				response = pbocSm4.decryptStr(response);
			}

			reportRes = JSON.parseObject(response, CreditReportRes.class);
			status = this.checkResult(reportRes);
			if (!status) {
				return null;
			}

			return reportRes;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[PBOC]，征信报告查询接口异常，e={}", e);
			return null;
		} finally {
			try {
				LOGGER.info(">>>>>>>>>>开始记录征信报告查询接口调用日志");
				RequestLogSaveDto requestLogSaveDto = new RequestLogSaveDto();
				requestLogSaveDto.setSystemName(SystemNameConstant.CREDIT);
				requestLogSaveDto.setInterfaceName("个人征信报告查看");
				requestLogSaveDto.setUrl(conf.getCreditIpHost() + API_QUERY_REPORT_URL);
				requestLogSaveDto.setRequestMsg(body);
				//处理返回结果，不保存pdf
				CreditReportRes creditReportRes = JSON.parseObject(response, CreditReportRes.class);
				if (null != creditReportRes) {
					creditReportRes.setPdf("");
				}
				response = JSON.toJSONString(creditReportRes);
				requestLogSaveDto.setResponseMsg(response);
				requestLogSaveDto.setStatus(status ? "1" : "0");
				if (StrUtil.isNotBlank(response)) {
					requestLogSaveDto.setReason(JSON.parseObject(response).getString("msg"));
				}
				requestLogClient.saveRequestLog(requestLogSaveDto);
			} catch (Exception e) {
				LOGGER.error(">>>>>>>>>>记录征信报告查询接口调用日志失败:{}", e);
			}
		}
	}

	protected boolean checkResult(CreditReportRes resData) {
		if (ObjectUtil.notEqual(200, resData.getCode())) {
			LOGGER.warn(">>>>>>[PBOC], 调用结果失败，code={} msg={}", resData.getCode(), resData.getMsg());
			return false;
		}
		return true;
	}

}