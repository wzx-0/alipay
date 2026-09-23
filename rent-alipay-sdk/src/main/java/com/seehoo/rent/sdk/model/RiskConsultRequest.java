package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * alipay.commerce.rent.risk.consult 风险咨询请求
 * <p>返回综合风险等级riskLevel（T1~T4低风险/T5~T6中风险/T7~T10高风险，策略口径以芝麻文档为准）</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskConsultRequest extends BaseRentOrderRequest {

    /** 咨询风险类型，综合风险固定COMPREHENSIVE */
    private String consultRiskTypes;
}
