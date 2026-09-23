package cn.seehoo.spg.bizcom.gd.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.seehoo.spg.bizcom.gd.config.GdConfiguration;

import static cn.seehoo.spg.bizcom.gd.exception.GdErrorCode.getByInfo;

/**
 * 
 * 高德抽象
 */
public abstract class GdBaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(GdBaseClient.class);
	@Autowired
	public GdConfiguration conf;

	// 检查中瑞响应数据
	protected boolean checkResult(String result) {
		String status = JSON.parseObject(result).getString("status");
		String info = JSON.parseObject(result).getString("info");
		if (!"1".equals(status)) {
			LOGGER.warn(">>>>>>[高德], 高德调用结果失败，info={}", info+":"+getByInfo(info).getMessage());
			return false;
		}
		return true;
	}

}