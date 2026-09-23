package cn.seehoo.spg.common.mortgage.client;

import cn.seehoo.spg.common.mortgage.model.*;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 抵押中台台客户端
 */
public interface MortgageClient {

    /**
     * 抵押方式查询: 抵押类型 1线下 2线上 获取代理人前，获取抵押方式, 线上才需要获取代理人, 签署抵押相关合同
     * @param req
     * @return
     */
    public String queryMortgageType(QueryMortgageTypeReq req);

    /**
     * 获取代理人信息
     * @param req
     * @return
     */
    public GetAgentInfoRes getAgentInfo(GetAgentInfoReq req);

    /**
     * 创建抵押任务
     * @param req
     * @return
     */
    public JSONObject createOrUpdateMortgageTaskManage(MortgageTaskManageReq req);

    /**
     * 发起工单取消
     * @param req
     * @return
     */
    public String settlementOrOrderCancel(SettlementInfoReq req);

    /**
     * 抵押任务下发
     * @param req
     * @return
     */
    public String saveMortgageTaskManageInfo(MortgageTaskManageInfoOpenApiReq req);

    /**
     * 抵押签署完成通知
     * @param req
     * @return
     */
    public String mortgageSignContractPush(PushOrderFileReq req);

    /**
     * 附件查询
     * @param req
     * @return
     */
    public List<FileAllInfoRes> queryFileInfos(QueryFileInfoReq req);

    /**
     * 抵押信息集合查询
     * @param req
     * @return
     */
    public List<MortgageTaskManageInfoRes> queryMortgageTaskManageInfos(QueryMortgageTaskManageInfoReq req);


    /**
     * 请求抵押中台-接口授权
     * @return
     */
    public String createToken();

    /**
     * 抵押中台-请求转发
     * @return
     */
    public Object doRequestMortgage(HttpServletRequest request, String requestBody);

}
