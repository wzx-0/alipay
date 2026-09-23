package cn.seehoo.spg.bizcom.ocr.client;

import cn.seehoo.spg.bizcom.ocr.model.OcrBankCardInfo;
import cn.seehoo.spg.bizcom.ocr.model.OcrIdCardInfo;
import cn.seehoo.spg.bizcom.ocr.model.OcrVechileInvoiceInfo;
import cn.seehoo.spg.bizcom.ocr.model.OcrVechileLicenseInfo;

/**
 * @author chenjun
 * 
 * OCR客户端
 */
public interface OcrClient {

	/**
	 * 身份证扫描
	 * @param idCardSide font/back
	 * @param image base64
	 */
	public OcrIdCardInfo ocrIdCard(String idCardSide, String image) throws RuntimeException;
	
	/**
	 * 银行卡扫描
	 * @param image
	 * @return
	 * @throws RuntimeException
	 */
	public OcrBankCardInfo ocrBankCard(String image) throws RuntimeException;
	
	/**
	 * 车辆发票扫描
	 * @param image
	 * @return
	 * @throws RuntimeException
	 */
	public OcrVechileInvoiceInfo ocrVechileInvoice(String image) throws RuntimeException;
	
	
	/**
	 * 行驶证扫描
	 * @param image
	 * @return
	 * @throws RuntimeException
	 */
	public OcrVechileLicenseInfo ocrVechileLicense(String image) throws RuntimeException;
	
}
