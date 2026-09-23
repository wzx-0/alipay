package cn.seehoo.spg.bizcom.qcc.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.base.client.RequestHisClient;
import cn.seehoo.spg.base.dto.DisciplinaryCheckHisSaveDto;
import cn.seehoo.spg.base.dto.ExceptionCheckHisSaveDto;
import cn.seehoo.spg.base.dto.SeriousIllegalCheckHisSaveDto;
import cn.seehoo.spg.base.dto.ShixinCheckHisSaveDto;
import cn.seehoo.spg.bizcom.qcc.client.QccCompanyClient;
import cn.seehoo.spg.bizcom.qcc.model.*;
import cn.seehoo.spg.bizcom.qcc.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollectionUtil;
import cn.seehoo.spg.bizcom.qcc.client.CompanyClient;
import cn.seehoo.spg.commons.core.exception.BusinessException;

@RestController
public class CompanyInfoController {
	@Autowired
	private CompanyClient cl;
	@Autowired
	private RequestHisClient requestHisClient;
	
	@PostMapping(path= {"/v1/qcc/company/info"})
	public CompanyVO getCompanyInfo(@RequestParam("keyword") String keyword) throws BusinessException {
		CompayInfo ci = cl.getCompanyInfo(keyword);
		if (ci == null) {
			return null;
		}
		return copyProperties(ci);
	}

	public CompanyVO copyProperties(CompayInfo ci){
		CompanyVO civo = BeanUtil.copyProperties(ci, CompanyVO.class);
		civo.setName(ci.getName());
		civo.setIdBeginDate(ci.getTermStart());
		civo.setIdEndDate(ci.getTermEnd());
		civo.setLegalPersonName(ci.getOperName());
		civo.setOptCondition(ci.getStatus());
		civo.setRegiestAmount(ci.getRegisteredCapital());
		civo.setRegisteDate(ci.getStartDate());
		civo.setRegisteProvinceName(ci.getProvince());
		civo.setRegisteCityName(ci.getCity());
		civo.setRegisteDistrictName(ci.getCounty());
		civo.setRegisteAddress(ci.getAddress());
		civo.setScope(ci.getScope());
		civo.setIdNo(ci.getCreditCode());
		return civo;
	}
	
	@PostMapping(path= {"/v1/qcc/company/_search"})
	public List<CompanyVO> searchCompanyInfo(@RequestParam("keyword") String keyword) throws BusinessException {
		List<CompayInfo> cis = cl.searchCompanyInfos(keyword);
		if (CollectionUtil.isEmpty(cis)) {
			return new ArrayList<>();
		}
		List<CompanyVO> civos = new ArrayList<>();
		for(CompayInfo ci : cis) {
			CompanyVO civo = new CompanyVO();
			civo.setName(ci.getName());
			civo.setIdNo(ci.getCreditCode());
			civos.add(civo);
		}
		return civos;
	}
	@PostMapping(path= {"/v1/qcc/company/overview/info"})
	public CompanyVO enterpriseCheck(@RequestParam("keyword") String keyword) throws BusinessException{
		CompayInfo ci = cl.getCompanyInfo(keyword);
		if (ci == null) {
			return null;
		}
		if (StrUtil.isEmpty(ci.getStatus()) || !QccCompanyClient.VALID_BUSINESS_STATUSES.contains(ci.getStatus())){
			throw new BusinessException("-1","企业状态无效，请检查");
		}
		return copyProperties(ci);
	}

	/**
	 * 经营异常核查
	 * @param searchKey
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(path = {"/v1/qcc/company/exceptionCheck"})
	public List<ExceptionCheckVO> exceptionCheck(@RequestParam("orderCode") String orderCode,@RequestParam("searchKey") String searchKey) throws BusinessException {
		ExceptionCheckHisSaveDto dto = new ExceptionCheckHisSaveDto();
		dto.setOrderCode(orderCode);
		dto.setSearchKey(searchKey);
		List<ExceptionCheckInfo> eci = cl.exceptionCheck(orderCode,searchKey,dto);
		if (CollectionUtil.isEmpty(eci)) {
			return new ArrayList<>();
		}else {
			for (ExceptionCheckInfo exceptionCheckInfo : eci) {
				dto.setAddReason(exceptionCheckInfo.getAddReason());
				dto.setAddDate(exceptionCheckInfo.getAddDate());
				dto.setRomoveReason(exceptionCheckInfo.getRomoveReason());
				dto.setRemoveDate(exceptionCheckInfo.getRemoveDate());
				dto.setDecisionOffice(exceptionCheckInfo.getDecisionOffice());
				dto.setRemoveDecisionOffice(exceptionCheckInfo.getRemoveDecisionOffice());
				requestHisClient.saveExceptionCheckHis(dto);
			}
		}
		List<ExceptionCheckVO> ecvos = new ArrayList<>();
		for (ExceptionCheckInfo ec : eci) {
			ExceptionCheckVO ecvo = new ExceptionCheckVO();
			ecvo.setAddReason(ec.getAddReason());
			ecvo.setAddDate(ec.getAddDate());
			ecvo.setRomoveReason(ec.getRomoveReason());
			ecvo.setRemoveDate(ec.getRemoveDate());
			ecvo.setDecisionOffice(ec.getDecisionOffice());
			ecvo.setRemoveDecisionOffice(ec.getRemoveDecisionOffice());
			ecvo.setVerifyResult(String.valueOf(dto.getVerifyResult()));
			ecvo.setSearchTime(dto.getSearchTime());
			ecvo.setOrderNumber(dto.getOrderNumber());
			ecvos.add(ecvo);
		}
		return ecvos;
	}

	/**
	 * 失信核查
	 * @param searchKey
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(path = {"/v1/qcc/company/shixinCheck"})
	public List<ShixinCheckVO> shixinCheck(@RequestParam("orderCode") String orderCode, @RequestParam("searchKey") String searchKey) throws BusinessException {
		ShixinCheckHisSaveDto dto = new ShixinCheckHisSaveDto();
		dto.setOrderCode(orderCode);
		dto.setSearchKey(searchKey);
		List<ShixinCheckInfo> eci = cl.shixinCheck(orderCode,searchKey,dto);
		if (CollectionUtil.isEmpty(eci)) {
			return new ArrayList<>();
		}else {
			for (ShixinCheckInfo shixinCheckInfo : eci) {
				dto.setLianDate(shixinCheckInfo.getLianDate());
				dto.setAnno(shixinCheckInfo.getAnno());
				dto.setExecuteGov(shixinCheckInfo.getExecuteGov());
				dto.setExecuteStatus(shixinCheckInfo.getExecuteStatus());
				dto.setPublicDate(shixinCheckInfo.getPublicDate());
				dto.setExecuteNo(shixinCheckInfo.getExecuteNo());
				dto.setActionRemark(shixinCheckInfo.getActionRemark());
				dto.setAmount(shixinCheckInfo.getAmount());
				requestHisClient.saveShixinCheckHis(dto);
			}
		}
		List<ShixinCheckVO> ecvos = new ArrayList<>();
		for (ShixinCheckInfo ec : eci) {
			ShixinCheckVO ecvo = new ShixinCheckVO();
			ecvo.setLianDate(ec.getLianDate());
			ecvo.setAnno(ec.getAnno());
			ecvo.setExecuteGov(ec.getExecuteGov());
			ecvo.setExecuteStatus(ec.getExecuteStatus());
			ecvo.setPublicDate(ec.getPublicDate());
			ecvo.setExecuteNo(ec.getExecuteNo());
			ecvo.setActionRemark(ec.getActionRemark());
			ecvo.setAmount(ec.getAmount());
			ecvo.setVerifyResult(String.valueOf(dto.getVerifyResult()));
			ecvo.setSearchTime(dto.getSearchTime());
			ecvo.setOrderNumber(dto.getOrderNumber());
			ecvos.add(ecvo);
		}
		return ecvos;
	}

	/**
	 * 严重违法核查
	 * @param searchKey
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(path = {"/v1/qcc/company/seriousIllegalCheck"})
	public List<SeriousIllegalCheckVO> seriousIllegalCheck(@RequestParam("orderCode") String orderCode, @RequestParam("searchKey") String searchKey) throws BusinessException {
		SeriousIllegalCheckHisSaveDto dto = new SeriousIllegalCheckHisSaveDto();
		dto.setOrderCode(orderCode);
		dto.setSearchKey(searchKey);
		List<SeriousIllegalCheckInfo> eci = cl.seriousIllegalCheck(orderCode,searchKey,dto);
		if (CollectionUtil.isEmpty(eci)) {
			return new ArrayList<>();
		}else {
			for (SeriousIllegalCheckInfo seriousIllegalCheckInfo : eci) {
				dto.setType(seriousIllegalCheckInfo.getType());
				dto.setAddReason(seriousIllegalCheckInfo.getAddReason());
				dto.setAddDate(seriousIllegalCheckInfo.getAddDate());
				dto.setAddOffice(seriousIllegalCheckInfo.getAddOffice());
				dto.setRemoveReason(seriousIllegalCheckInfo.getRemoveReason());
				dto.setRemoveDate(seriousIllegalCheckInfo.getRemoveDate());
				dto.setRemoveOffice(seriousIllegalCheckInfo.getRemoveOffice());
				requestHisClient.saveSeriousIllegalCheckHis(dto);
			}
		}
		List<SeriousIllegalCheckVO> ecvos = new ArrayList<>();
		for (SeriousIllegalCheckInfo ec : eci) {
			SeriousIllegalCheckVO ecvo = new SeriousIllegalCheckVO();
			ecvo.setType(ec.getType());
			ecvo.setAddReason(ec.getAddReason());
			ecvo.setAddDate(ec.getAddDate());
			ecvo.setAddOffice(ec.getAddOffice());
			ecvo.setRemoveReason(ec.getRemoveReason());
			ecvo.setRemoveDate(ec.getRemoveDate());
			ecvo.setRemoveOffice(ec.getRemoveOffice());
			ecvo.setVerifyResult(String.valueOf(dto.getVerifyResult()));
			ecvo.setSearchTime(dto.getSearchTime());
			ecvo.setOrderNumber(dto.getOrderNumber());
			ecvos.add(ecvo);
		}
		return ecvos;
	}
	/**
	 * 惩戒名单核查
	 * @param searchKey
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping(path = {"/v1/qcc/company/disciplinaryCheck"})
	public List<DisciplinaryCheckVO> disciplinaryCheck(@RequestParam("orderCode") String orderCode, @RequestParam("searchKey") String searchKey) throws BusinessException {
		DisciplinaryCheckHisSaveDto dto = new DisciplinaryCheckHisSaveDto();
		dto.setOrderCode(orderCode);
		dto.setSearchKey(searchKey);
		List<DisciplinaryCheckInfo> eci = cl.disciplinaryCheck(orderCode,searchKey,dto);
		if (CollectionUtil.isEmpty(eci)) {
			return new ArrayList<>();
		}else {
			for (DisciplinaryCheckInfo disciplinaryCheckInfo : eci) {
				dto.setPunishType(disciplinaryCheckInfo.getPunishType());
				dto.setPunishFiled(disciplinaryCheckInfo.getPunishFiled());
				dto.setCaseReason(disciplinaryCheckInfo.getCaseReason());
				dto.setDecisionOffice(disciplinaryCheckInfo.getDecisionOffice());
				dto.setDecisionDate(disciplinaryCheckInfo.getDecisionDate());
				dto.setRemovedDate(disciplinaryCheckInfo.getRemovedDate());
				requestHisClient.saveDisciplinaryCheckHis(dto);
			}
		}
		List<DisciplinaryCheckVO> ecvos = new ArrayList<>();
		for (DisciplinaryCheckInfo ec : eci) {
			DisciplinaryCheckVO ecvo = new DisciplinaryCheckVO();
			ecvo.setPunishType(ec.getPunishType());
			ecvo.setPunishFiled(ec.getPunishFiled());
			ecvo.setCaseReason(ec.getCaseReason());
			ecvo.setDecisionOffice(ec.getDecisionOffice());
			ecvo.setDecisionDate(ec.getDecisionDate());
			ecvo.setRemovedDate(ec.getRemovedDate());
			ecvo.setVerifyResult(String.valueOf(dto.getVerifyResult()));
			ecvo.setSearchTime(dto.getSearchTime());
			ecvo.setOrderNumber(dto.getOrderNumber());
			ecvos.add(ecvo);
		}
		return ecvos;
	}
}