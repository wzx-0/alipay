package cn.seehoo.spg.bizcom.ocr.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.seehoo.spg.bizcom.ocr.config.HeheOcrConfiguration;

/**
 * @author chenjun
 * 
 * 百度客户端抽象
 */
public abstract class HeheBaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeheBaseClient.class);
	@Autowired
	public HeheOcrConfiguration conf;
	protected static List<String> fields = new ArrayList<>();
	
	static {
		fields.add("name");
		fields.add("sex");
		fields.add("nationality");
		fields.add("birth");
		fields.add("address");
		fields.add("id_number");
		fields.add("issue_authority");
		fields.add("validate_date");
		fields.add("validate_date");
	}
	
	// 设置通用请求头
	protected Map<String, String> setCommonHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("connection", "Keep-Alive");
		headers.put("Content-Type", "application/octet-stream");
		headers.put("contentEncoding", "base64");
//		String xtiappId = conf.getX_ti_app_id();
//		if (StrUtil.isNotEmpty(xtiappId)) {
//			headers.put("x-ti-app-id", xtiappId);
//		}
//		String xtisecretCode = conf.getX_ti_secret_code();
//		if (StrUtil.isNotEmpty(xtisecretCode)) {
//			headers.put("x-ti-secret-code", xtisecretCode);
//		}
		return headers;
	}
	
	// 判断结果
	protected boolean checkResult(String result) {
		int success = JSON.parseObject(result).getIntValue("code");
		String msg = JSON.parseObject(result).getString("Message");
		if (success != 200) {
			LOGGER.warn(">>>>>>[合合OCR], 合合OCR调用结果失败，msg={}", msg);
			return false;
		}
		return true;
	}
}
