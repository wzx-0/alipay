package cn.seehoo.spg.bizcom.qcc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.hutool.crypto.digest.MD5;
import cn.seehoo.spg.bizcom.qcc.config.QccConfiguration;

/**
 * @author chenjun
 * 
 * 企查查抽象
 */
public abstract class QccBaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(QccBaseClient.class);
	@Autowired
	public QccConfiguration conf;

	// 检查中瑞响应数据
	protected boolean checkResult(String result) {
		int success = JSON.parseObject(result).getIntValue("Status");
		String msg = JSON.parseObject(result).getString("Message");
		if (success != 200) {
			LOGGER.warn(">>>>>>[企查查], 企查查调用结果失败，msg={}", msg);
			return false;
		}
		return true;
	}
	
	// 获取Token/Timespan
	protected String[] RandomAuthentHeader(String appKey, String secretKey) {
		String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
		String[] authentHeaders = new String[] {
		MD5.create().digestHex(appKey.concat(timeSpan).concat(secretKey)).toUpperCase(),
		timeSpan };
		return authentHeaders;
	}
}