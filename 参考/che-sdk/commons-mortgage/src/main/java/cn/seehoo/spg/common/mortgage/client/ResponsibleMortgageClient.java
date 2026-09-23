package cn.seehoo.spg.common.mortgage.client;

import cn.seehoo.spg.common.mortgage.model.req.*;
import cn.seehoo.spg.common.mortgage.model.res.*;
import cn.seehoo.spg.commons.core.page.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 调用有担-抵押中台台客户端
 * @author zhangxx
 * @date 2026/3/26 10:49
 */
public interface ResponsibleMortgageClient {
    /**
     * 合作机构配置查询
     * @param req
     * @return res
     */
    MortgagePartnerRes queryMortgagePartnerConfigInfo(MortgagePartnerReq req);

    /**
     * 创建或更新抵押任务
     * @param req
     * @return res
     */
    MortgageTaskCreateRes createOrUpdateMortgageTaskManage(MortgageTaskCreateReq req);

    /**
     * 抵押信息修改
     * @param req
     * @return res
     */
    MortgageTaskUpdateRes updateMortgageTaskManageInfo(MortgageTaskUpdateReq req);

    /**
     * 抵押任务下发
     * @param req
     * @return res
     */
    MortgageTaskRes saveMortgageTaskManageInfo(MortgageTaskReq req);

    /**
     * 抵押信息集合查询
     * @param req
     * @return res
     */
    List<MortgageTaskSearchRes> queryMortgageTaskManageInfos(MortgageTaskSearchReq req);

    /**
     * 获取代理人信息
     * @param req
     * @return res
     */
    MortgageAgentRes getAgentInfo(MortgageAgentReq req);

    /**
     * 合同结清处理
     * @param req
     * @return res
     */
    void settlementOrOrderCancel(MortgageSettlementReq req) throws Exception;

    /**
     * 抵押签署完成通知
     * @param req
     * @return res
     */
    void mortgageSignContractPush(MortgageSignContractReq req) throws Exception;

    /**
     * 附件查询
     * @param req
     * @return res
     */
    List<MortgageFileRes> queryFileInfos(MortgageFileReq req);

    /**
     * 查询抵押管理城市信息
     * @param req
     * @return res
     */
    MortgageManageCityInfoRes queryMortgageManageCityInfo(MortgageManageCityInfoReq req);

    /**
     * 分页查询
     * @param pageDto
     * @return
     */
    Page<MortgagePageRes> queryPageMortgage(PageDto<MortgagePageReq> pageDto);

    /**
     * 抵押任务回传
     * @param req
     * @return
     */
    MortgageManageResultRes mortgageTaskManageResult(MortgageManageResultReq req);
}
