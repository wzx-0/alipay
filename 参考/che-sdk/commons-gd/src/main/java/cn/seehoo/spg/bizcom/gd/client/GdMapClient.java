package cn.seehoo.spg.bizcom.gd.client;

import java.util.List;

import cn.seehoo.spg.base.dto.ExceptionCheckHisSaveDto;
import cn.seehoo.spg.bizcom.gd.model.*;

/**
 * 
 * 高德接口
 */
public interface GdMapClient {
	
	/** 高德地理编码 */
    List<GdMapCheckInfo> gdMapCheck(String address);
}