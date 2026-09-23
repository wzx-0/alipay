package cn.seehoo.spg.bizcom.utils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.commons.redis.service.RedisOperateService;

public final class SeqUtil {
	/** 源请求方系统标识 */
	private static final String SRC_SYS_ID = "04024";
    /** 全局流水號KEY */
    private static final String GLO_SEQ_NO_KEY = "COMM/GLO_SIGN_SEQ_NO_KEY";
    /** 請求流水號KEY */
    private static final String REQ_SEQ_NO_KEY = "COMM/REQ_SIGN_SEQ_NO_KEY";
    /** 請求流水號KEY */
    private static final String CURRENT_DATE_KEY = "COMM/DATE";

	// 獲取全局流水號
	public static String getGlobalSeqNo(RedisOperateService redisOperate) {
		StringBuilder appender = new StringBuilder();
		// 5位系統編碼
		appender.append(SRC_SYS_ID);
		// 2位保留域
		appender.append("bl");
		// 14位時間序列
		String datetime = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
		appender.append(datetime);
		// 8位標識為
		String dateKey = getCurrentDate(redisOperate, GLO_SEQ_NO_KEY);
		long gloabSeqNo = redisOperate.getStr().incr(GLO_SEQ_NO_KEY+"-"+dateKey, 1);
		String tailStr = StringUtils.leftPad(""+gloabSeqNo, 9, '0');
		appender.append(tailStr);
		return appender.toString();
	}

	// 獲取請求流水號
	public static String getReqSeqNo(RedisOperateService redisOperate) {
		StringBuilder appender = new StringBuilder();
		// 5位系統編碼
		appender.append(SRC_SYS_ID);
		// 2位保留域
		appender.append("bl");
		// 8位時間
		String date = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
		appender.append(date);
		// 9位標識為
		String dateKey = getCurrentDate(redisOperate, REQ_SEQ_NO_KEY);
		long reqSeqNo = redisOperate.getStr().incr(REQ_SEQ_NO_KEY+"-"+dateKey, 1);
		String tailStr = StringUtils.leftPad(""+reqSeqNo, 9, '0');
		appender.append(tailStr);
		return appender.toString();
	}

	// 緩存日期
	private static String getCurrentDate(RedisOperateService redisOperate, String seqNoKey) {
		// 當前日期
		String currentDate = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
		// 判斷緩存中是否存在當前日期
		String redisDate = (String) redisOperate.getStr().get(CURRENT_DATE_KEY,String.class);
		if (StrUtil.isEmpty(redisDate)) {
			return currentDate;
		}
		// 存在的情況下判斷是否是同一天
		if (currentDate.equals(redisDate)) {
			return redisDate;
		}
		// 刪除序列號KEY，緩存當前日期
		redisOperate.getStr().del(Collections.singleton(seqNoKey + "-" + redisDate));
		redisOperate.getStr().set(CURRENT_DATE_KEY, currentDate,String.class);
		return currentDate;
	}

	// 获取网关通用请求头
	public static Map<String, String> buildReqHeader(String SvcNo, String ScnNo, 
		String transCode, String reqSysId, RedisOperateService redisOperateService) {
		Map<String, String> requestHead = new HashMap<>(16);
		Date date = new Date(System.currentTimeMillis());
		requestHead.put("ReqDt", DateUtil.format(date, "YYYYMMdd"));
		requestHead.put("ReqTm", DateUtil.format(date, "HHmmss"));
		requestHead.put("SrcSysId", "");
		requestHead.put("ScnVerNo", "");
		requestHead.put("SrcCalCod", "");
		requestHead.put("SrcVerNo", "1.0.0");
		requestHead.put("SvcVerNo", "1.0.0");
		requestHead.put("SvcNo", SvcNo);
		requestHead.put("ScnNo", ScnNo);
		requestHead.put("TransCode", transCode);
		requestHead.put("ReqSysId", reqSysId);
		requestHead.put("GloSeqNo", SeqUtil.getGlobalSeqNo(redisOperateService));
		requestHead.put("ReqSeqNo", SeqUtil.getReqSeqNo(redisOperateService));
		requestHead.put("GloEndTm", String.valueOf(System.currentTimeMillis()));
		requestHead.put("RspDt", DateUtil.format(date, "YYYYMMdd"));
		requestHead.put("RspSt", "S");
		requestHead.put("RspCd", "000000");
		requestHead.put("RspMsg", "SUCCESS");
		requestHead.put("RspSeqNo", "S");
		return requestHead;
	}

	public static Map<String, String> gdBuildReqHeader(String SvcNo, String ScnNo,
													 String transCode, String reqSysId, RedisOperateService redisOperateService) {
		Map<String, String> requestHead = new HashMap<>(16);
		Date date = new Date(System.currentTimeMillis());
		requestHead.put("ReqDt", DateUtil.format(date, "YYYYMMdd"));
		requestHead.put("ReqTm", DateUtil.format(date, "HHmmss"));
		requestHead.put("SrcSysId", "");
		requestHead.put("ScnVerNo", "1.0");
		requestHead.put("SrcCalCod", "");
		requestHead.put("SrcVerNo", "1.0.0");
		requestHead.put("SvcVerNo", "1.0.0");
		requestHead.put("SvcNo", SvcNo);
		requestHead.put("ScnNo", ScnNo);
		requestHead.put("TransCode", transCode);
		requestHead.put("ReqSysId", reqSysId);
		requestHead.put("GloSeqNo", SeqUtil.getGlobalSeqNo(redisOperateService));
		requestHead.put("ReqSeqNo", SeqUtil.getReqSeqNo(redisOperateService));
		requestHead.put("GloEndTm", String.valueOf(System.currentTimeMillis()));
		requestHead.put("RspDt", DateUtil.format(date, "YYYYMMdd"));
		requestHead.put("RspSt", "S");
		requestHead.put("RspCd", "000000");
		requestHead.put("RspMsg", "SUCCESS");
		requestHead.put("RspSeqNo", "S");
		return requestHead;
	}
}
