package com.seehoo.rent.app.service;

import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderActionVo;

/** 风控审核服务 */
public interface RiskService {

    /** 风控咨询：查询综合风险等级T1~T10并留痕 */
    OrderActionVo.RiskConsult consult(String outOrderId);

    /** 提交审核结论：通过则approve推进，拒绝则关单 */
    void audit(OrderActionReq.RiskAudit req);
}
