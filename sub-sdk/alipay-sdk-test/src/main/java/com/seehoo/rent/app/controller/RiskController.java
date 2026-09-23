package com.seehoo.rent.app.controller;

import com.seehoo.rent.app.common.Result;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.service.RiskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/** 风控审核接口：咨询综合风险等级、提交人工审核结论 */
@Validated
@RestController
@RequestMapping("/api/v1/rent/risk")
public class RiskController {

    @Resource
    private RiskService riskService;

    /** A0103001 风险咨询 */
    @PostMapping("/consult")
    public Result<OrderActionVo.RiskConsult> consult(@Valid @RequestBody OrderActionReq.RiskConsult req) {
        return Result.success(riskService.consult(req.getOutOrderId()));
    }

    /** A0103002 审核结论提交（1通过/2拒绝） */
    @PostMapping("/audit")
    public Result<Void> audit(@Valid @RequestBody OrderActionReq.RiskAudit req) {
        riskService.audit(req);
        return Result.success(null);
    }
}
