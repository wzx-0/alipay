package cn.seehoo.spg.common.ecif.client;

import cn.seehoo.spg.common.ecif.model.*;
import cn.seehoo.spg.commons.core.exception.BusinessException;

/**
 * Ecif客户端
 */
public interface EcifClient {

	/**
	 * 对公-信息精准查询接口
	 * @param req
	 * @return
	 */
	public CompanyRes queryCompanyInfoByCertId(QueryCompanyReq req) throws BusinessException;

	/**
	 * 个人-信息精准查询接口
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public PersonRes queryPersonInfoByCertId(QueryPersonReq req) throws BusinessException;

	/**
	 * 新增/更新对公客户接口
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public CompanyRes addOrUpdateCompany(CompanyReq req) throws BusinessException;

	/**
	 * 新增/更新个人客户接口
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public PersonRes addOrUpdatePerson(PersonReq req) throws BusinessException;

	/**
	 * 更新对公账户信息
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public CompanyRes updateCompanyAccount(UpdateCompanyAccountReq req) throws BusinessException;

	/**
	 * 个人客户校验
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public PersonRes checkPerson(PersonReq req) throws BusinessException;

	/**
	 * 个人客户关联方信息查询
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	public PersonRelativesRes queryPersonRelatives(QueryRelativesReq req) throws BusinessException;

}