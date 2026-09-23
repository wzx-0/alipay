package com.seehoo.rent.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RiskAudit;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RiskAuditMapper;
import com.seehoo.rent.app.service.RiskService;
import com.seehoo.rent.sdk.AlipayRentClient;
import com.seehoo.rent.sdk.BizFields;
import com.seehoo.rent.sdk.RentResponse;
import com.seehoo.rent.sdk.model.FulfillmentApproveRequest;
import com.seehoo.rent.sdk.model.OrderCloseRequest;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import com.seehoo.rent.sdk.model.RiskConsultRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/** 风控审核服务实现：T1-T4自动通过/T5-T6人工/T7-T10拒绝（策略由管理端执行，接口提供查询与结论提交） */
@Slf4j
@Service
public class RiskServiceImpl implements RiskService {

    /** 审核结论：通过 */
    private static final String AUDIT_PASS = "1";
    /** 审核结论：拒绝 */
    private static final String AUDIT_REJECT = "2";
    /** 同步状态：待同步/已approve/已close */
    private static final String SYNC_WAIT = "0";
    private static final String SYNC_APPROVED = "1";
    private static final String SYNC_CLOSED = "2";
    /** 拒绝关单原因编码（文档示例值，TODO与支付宝确认拒绝场景标准编码） */
    private static final String REJECT_CLOSE_REASON = "3114";

    @Resource
    private RentOrderMapper orderMapper;
    @Resource
    private RiskAuditMapper riskAuditMapper;
    @Resource
    private AlipayRentClient alipayRentClient;

    @Override
    public OrderActionVo.RiskConsult consult(String outOrderId) {
        RentOrder order = mustGetOrder(outOrderId);
        RiskConsultRequest request = new RiskConsultRequest();
        request.setOrderId(order.getAlipayOrderId());
        request.setOutOrderId(outOrderId);
        RentResponse<Map<String, Object>> resp = alipayRentClient.riskConsult(request);

        String riskLevel = resp.getStr(BizFields.RISK_LEVEL);
        String consultResult = String.valueOf(resp.getData());
        // 咨询留痕（每次咨询记录一条，审核结论另由audit落库）
        RiskAudit record = new RiskAudit();
        record.setOrderId(order.getId());
        record.setRiskLevel(riskLevel);
        record.setConsultResult(consultResult);
        record.setSyncStatus(SYNC_WAIT);
        riskAuditMapper.insert(record);

        OrderActionVo.RiskConsult vo = new OrderActionVo.RiskConsult();
        vo.setRiskLevel(riskLevel);
        vo.setConsultResult(consultResult);
        // 已有审核结论时一并返回
        RiskAudit audited = riskAuditMapper.selectByOrderId(order.getId()).stream()
                .filter(r -> StrUtil.isNotBlank(r.getAuditResult()))
                .findFirst().orElse(null);
        if (audited != null) {
            vo.setAuditResult(audited.getAuditResult());
        }
        return vo;
    }

    @Override
    public void audit(OrderActionReq.RiskAudit req) {
        if (AUDIT_REJECT.equals(req.getAuditResult()) && StrUtil.isBlank(req.getRejectReason())) {
            throw new BusinessException(RentErrorCode.PARAM_ERROR, "拒绝时必须填写拒绝原因");
        }
        RentOrder order = mustGetOrder(req.getOutOrderId());

        RiskAudit record = new RiskAudit();
        record.setOrderId(order.getId());
        record.setAuditResult(req.getAuditResult());
        record.setAuditUser("MANUAL");
        record.setAuditTime(LocalDateTime.now());
        record.setRejectReason(req.getRejectReason());
        if (AUDIT_PASS.equals(req.getAuditResult())) {
            FulfillmentApproveRequest request = new FulfillmentApproveRequest();
            request.setOrderId(order.getAlipayOrderId());
            request.setOutOrderId(req.getOutOrderId());
            request.setOpenId(order.getBuyerOpenId());
            alipayRentClient.fulfillmentApprove(request);
            order.setOrderStatus(RentOrderStatus.APPROVED.name());
            record.setSyncStatus(SYNC_APPROVED);
        } else {
            OrderCloseRequest request = new OrderCloseRequest();
            request.setOrderId(order.getAlipayOrderId());
            request.setOutOrderId(req.getOutOrderId());
            request.setReasonCode(REJECT_CLOSE_REASON);
            request.setReasonDesc(req.getRejectReason());
            alipayRentClient.orderClose(request);
            order.setOrderStatus(RentOrderStatus.CLOSED.name());
            order.setCloseReasonCode(REJECT_CLOSE_REASON);
            order.setCloseReasonDesc(req.getRejectReason());
            order.setCloseTime(LocalDateTime.now());
            record.setSyncStatus(SYNC_CLOSED);
        }
        riskAuditMapper.insert(record);
        orderMapper.updateById(order);
    }

    private RentOrder mustGetOrder(String outOrderId) {
        RentOrder order = orderMapper.selectByOutOrderId(outOrderId);
        if (order == null) {
            throw new BusinessException(RentErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }
}
