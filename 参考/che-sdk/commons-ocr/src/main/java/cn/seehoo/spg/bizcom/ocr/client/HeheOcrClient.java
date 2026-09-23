package cn.seehoo.spg.bizcom.ocr.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.bizcom.ocr.model.OcrBankCardInfo;
import cn.seehoo.spg.bizcom.ocr.model.OcrIdCardInfo;
import cn.seehoo.spg.bizcom.ocr.model.OcrVechileInvoiceInfo;
import cn.seehoo.spg.bizcom.ocr.model.OcrVechileLicenseInfo;
import cn.seehoo.spg.commons.core.util.HttpUtils;

/**
 * @author chenjun
 * 
 * 百度OCR客户端
 */
public class HeheOcrClient extends HeheBaseClient implements OcrClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeheOcrClient.class);
	
	@Override
	public OcrIdCardInfo ocrIdCard(String idCardSide, String idcardData) throws RuntimeException {
		if (StrUtil.isEmpty(idcardData)) {
			throw new RuntimeException("idcardData image is null");
		}
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[OCR识别], 身份证OCR识别, 触发mock机制，直接返回null, 触发mock");
				return null;
			}
			// 请求头
			Map<String, String> headers = super.setCommonHeaders();
			byte[] requestBytes = Base64.decode(idcardData);
			// 识别身份证
			String proxy = conf.getProxy();
			String response = HttpUtils.request(conf.getIdcardUrl(), HttpUtils.METHOD_POST, headers, requestBytes, null, proxy);
		    boolean result = super.checkResult(response);
		    if (!result) {
		    	LOGGER.warn(">>>>>>[OCR识别], 身份证OCR识别失败");
		    	return null;
		    }
		    // 解析字段
		    OcrIdCardInfo idCardInfo = new OcrIdCardInfo();
			JSONObject json = JSON.parseObject(response);
			JSONObject idcResult = json.getJSONObject("result");
			JSONArray itemList = idcResult.getJSONArray("item_list");
			for (int i=0; i<itemList.size();i++) {
				JSONObject item = itemList.getJSONObject(i);
				String key = item.getString("key");
				String value = item.getString("value");
				if (key.equals("name")) {
					idCardInfo.setName(value);
				}
				if (key.equals("sex")) {
					idCardInfo.setSexual(value);
				}
				if (key.equals("nationality")) {
					idCardInfo.setNation(value);
				}
				if (key.equals("birth")) {
					idCardInfo.setBorn(value);
				}
				if (key.equals("address")) {
					idCardInfo.setAddress(value);
				}
				if (key.equals("id_number")) {
					idCardInfo.setIdcard(value);
				}
				if (key.equals("issue_authority")) {
					idCardInfo.setIssueAuthority(value);
				}
				if (key.equals("validate_date")) {
					idCardInfo.setExpireDate(value);
				}
			}
			return idCardInfo;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[OCR识别], 身份证OCR识别异常， e={}", e);
			return null; 
		}
		
	}
	
	@Override
	public OcrBankCardInfo ocrBankCard(String image) throws RuntimeException {
		if (StrUtil.isEmpty(image)) {
			throw new RuntimeException("bankcard image is null");
		}
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[OCR识别], 银行卡OCR识别, 触发mock机制，直接返回null");
				return null;
			}
			// 请求头
			LOGGER.info(">>>>>>[OCR识别], 银行卡OCR识别开始");
			Map<String, String> headers = super.setCommonHeaders();
			byte[] requestBytes = Base64.decode(image);
			// 识别银行卡
			String proxy = conf.getProxy();
			String response = HttpUtils.request(conf.getBankcardUrl(), HttpUtils.METHOD_POST, headers, requestBytes, null, proxy);
		    boolean result = super.checkResult(response);
		    if (!result) {
		    	LOGGER.warn(">>>>>>[OCR识别], 银行卡OCR识别失败");
		    	return null;
		    }
		    OcrBankCardInfo bank = new OcrBankCardInfo();
		    JSONObject json = JSON.parseObject(response);
			JSONObject idcResult = json.getJSONObject("result");
			JSONArray itemList = idcResult.getJSONArray("item_list");
			for (int i=0; i<itemList.size();i++) {
				JSONObject item = itemList.getJSONObject(i);
				String key = item.getString("key");
				String value = item.getString("value");
				if (key.equals("type")) {
					bank.setBankCardType(value);
				}
				if (key.equals("card_number")) {
					bank.setBankCardNumber(value);
				}
				if (key.equals("validate")) {
					bank.setValidDate(value);
				}
				if (key.equals("issuer")) {
					bank.setBankName(value);
				}
			}
			return bank;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[OCR识别], 银行卡OCR识别异常， e={}", e);
			return null;
		}
	} 
	
	@Override
	public OcrVechileInvoiceInfo ocrVechileInvoice(String image) throws RuntimeException {
		if (StrUtil.isEmpty(image)) {
			throw new RuntimeException("vehicle invoice image is null");
		}
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[OCR识别], 发票OCR识别, 触发mock机制，直接返回null");
				return null;
			}
			// 请求头
			Map<String, String> headers = super.setCommonHeaders();
			// body
			byte[] bankcardBytes = Base64.decode(image);
			// 识别身份证
			String proxy = conf.getProxy();
			String response = HttpUtils.request(conf.getInvoiceUrl(), 
			HttpUtils.METHOD_POST, headers, bankcardBytes, null, proxy);
		    boolean result = super.checkResult(response);
		    if (!result) {
		    	LOGGER.warn(">>>>>>[OCR识别], 发票OCR识别失败");
		    	return null;
		    }
		    LOGGER.info(">>>>发票返回={}", response);
		    OcrVechileInvoiceInfo invo = new OcrVechileInvoiceInfo();
		    JSONObject json = JSON.parseObject(response);
		    JSONArray items = json.getJSONArray("pages").getJSONObject(0).getJSONObject("result")
		    .getJSONArray("object_list").getJSONObject(0).getJSONArray("item_list");
		    for (int i=0;i<items.size();i++) {
		    	JSONObject item = items.getJSONObject(i);
		    	String key = item.getString("key");
		    	String value = item.getString("value");
		    	if (key.equals("vehicle_invoice_total_price_char")) {
		    		invo.setPriceTax(value);
		    	}
		    	if (key.equals("vehicle_invoice_total_price_digits")) {
		    		invo.setPriceTaxLow(value);
		    	}
		    	if (key.equals("vehicle_invoice_seller")) {
		    		invo.setSaler(value);
		    	}
		    	if (key.equals("vehicle_invoice_daima")) {
		    		invo.setInvoiceCode(value);
		    	}
		    	if (key.equals("vehicle_invoice_issue_date")) {
		    		invo.setInvoiceDate(value);
		    	}
		    	if (key.equals("vehicle_invoice_tax_rate")) {
		    		invo.setTaxRate(value);
		    	}
		    }
			return invo;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[OCR识别]，发票OCR识别异常， e={}", e);
			return null;
		}
	}

	@Override
	public OcrVechileLicenseInfo ocrVechileLicense(String image) throws RuntimeException {
		if (StrUtil.isEmpty(image)) {
			throw new RuntimeException("vehicle license image is null");
		}
		try {
			// mock
			if (conf.checkMock()) {
				LOGGER.warn(">>>>>>[OCR识别], 行驶证OCR识别, 触发mock机制，直接返回null");
				return null;
			}
			// 请求头
			Map<String, String> headers = super.setCommonHeaders();
			// body
			byte[] bankcardBytes = Base64.decode(image);
			// 识别身份证
			String proxy = conf.getProxy();
			String response = HttpUtils.request(conf.getVehicleLicenseUrl(), 
			HttpUtils.METHOD_POST, headers, bankcardBytes, null, proxy);
		    boolean result = super.checkResult(response);
		    if (!result) {
		    	LOGGER.warn(">>>>>>[OCR识别], 行驶证OCR识别失败");
		    	return null;
		    }
		    JSONObject json = JSON.parseObject(response);
			JSONObject idcResult = json.getJSONObject("result");
			JSONObject details = idcResult.getJSONObject("details");
			String address = details.getJSONObject("address").getString("value");
			String vin =  details.getJSONObject("vin").getString("value");
			OcrVechileLicenseInfo ovlinfo = new OcrVechileLicenseInfo();
			ovlinfo.setAddress(address);
			ovlinfo.setVin(vin);
			return ovlinfo;
		} catch (Exception e) {
			LOGGER.error(">>>>>>[OCR识别]，行驶证OCR识别异常， e={}", e);
			return null;
		}
	}
}