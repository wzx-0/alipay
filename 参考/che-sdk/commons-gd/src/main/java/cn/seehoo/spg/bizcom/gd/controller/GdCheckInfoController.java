package cn.seehoo.spg.bizcom.gd.controller;

import java.util.ArrayList;
import java.util.List;

import cn.seehoo.spg.base.client.RequestHisClient;
import cn.seehoo.spg.bizcom.gd.model.*;
import cn.seehoo.spg.bizcom.gd.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollectionUtil;
import cn.seehoo.spg.bizcom.gd.client.GdMapClient;
import cn.seehoo.spg.commons.core.exception.BusinessException;

@RestController
public class GdCheckInfoController {
	@Autowired
	private GdMapClient gdMapCheck;


	/**
	 * 高德地理编码
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(path = {"/v1/gd/geo/geocode"})
	public List<GdMapCheckVO> geoCode(@RequestParam("address") String address) throws BusinessException {
		List<GdMapCheckInfo> gmi = gdMapCheck.gdMapCheck(address);
		if (CollectionUtil.isEmpty(gmi)) {
			return new ArrayList<>();
		}
		List<GdMapCheckVO> gmvos = new ArrayList<>();
		for (GdMapCheckInfo gm : gmi) {
			GdMapCheckVO gmvo = new GdMapCheckVO();
			gmvo.setProvince(gm.getProvince());
			gmvo.setCity(gm.getCity());
			gmvo.setDistrict(gm.getDistrict());
			gmvo.setLevel(gm.getLevel());
			gmvos.add(gmvo);
		}
		return gmvos;
	}


}