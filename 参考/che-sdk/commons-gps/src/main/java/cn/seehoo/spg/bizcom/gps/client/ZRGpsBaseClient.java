package cn.seehoo.spg.bizcom.gps.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.seehoo.spg.bizcom.gps.config.ZRGpsConfiguration;

/**
 * @author chenjun
 * 
 * 中瑞抽象
 */
public abstract class ZRGpsBaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZRGpsBaseClient.class);
	@Autowired
	public ZRGpsConfiguration conf;

	// 检查中瑞响应数据
	protected boolean checkResult(String result) {
		boolean success = JSON.parseObject(result).getBooleanValue("Success");
		String msg = JSON.parseObject(result).getString("AllMessages");
		if (!success) {
			LOGGER.warn(">>>>>>[中瑞GPS], 中瑞调用结果失败，msg={}", msg);
			return false;
		}
		return true;
	}
}