package cn.seehoo.spg.bizcom.intel.client;

import cn.seehoo.spg.bizcom.intel.model.IntelRecognizeInfo;
import cn.seehoo.spg.bizcom.intel.model.IntelRecognizeResponse;

/**
 * @author chenjun
 * 
 * OCR客户端
 */
public interface IntelClient {

	/**
	 * 智能体识别
	 * @param intelRecognizeInfo
	 * @return
	 * @throws RuntimeException
	 */
	public IntelRecognizeResponse intelRecognize(IntelRecognizeInfo intelRecognizeInfo);

}
