package cn.seehoo.spg.bizcom.collection.client;

import cn.seehoo.spg.bizcom.collection.config.CollectionConfiguration;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenjun
 * 
 * 催收抽象
 */
public abstract class CollectionBaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionBaseClient.class);
	@Autowired
	public CollectionConfiguration conf;

	// 检查催收响应数据
	protected boolean checkResult(String result) {
		boolean success = JSON.parseObject(result).getBooleanValue("success");
		String msg = JSON.parseObject(result).getString("msg");
		if (!success) {
			LOGGER.warn(">>>>>>[催收系统], 催收调用结果失败，msg={}", msg);
			return false;
		}
		return true;
	}
}