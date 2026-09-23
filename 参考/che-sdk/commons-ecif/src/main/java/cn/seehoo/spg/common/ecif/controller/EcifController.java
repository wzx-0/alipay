package cn.seehoo.spg.common.ecif.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.seehoo.spg.common.ecif.model.PersonRelativesRes;
import cn.seehoo.spg.common.ecif.model.QueryRelativesReq;
import cn.seehoo.spg.common.ecif.vo.EcifPersonRelativesVo;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.common.ecif.client.EcifClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ecif控制器
 */
@RestController
public class EcifController {

	@Autowired
	private EcifClient cl;

    // 暴露HTTP接口

    /**
	 * 个人-关联方信息查询
	 * @param certId
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(path= {"/v1/ecif/queryPersonRelatives"})
	public EcifPersonRelativesVo addOrUpdateCompany(@RequestParam("certId") String certId) throws BusinessException {
		QueryRelativesReq req = new QueryRelativesReq();
		req.setCertId(certId);

		PersonRelativesRes relativesRes = cl.queryPersonRelatives(req);
		if (relativesRes == null) {
			return null;
		}
		EcifPersonRelativesVo relativesVo = BeanUtil.copyProperties(relativesRes, EcifPersonRelativesVo.class);
		return relativesVo;
	}

}