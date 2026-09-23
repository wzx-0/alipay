package com.seehoo.rent.app.service;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.dto.OrderActionReq;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RiskAudit;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RiskAuditMapper;
import com.seehoo.rent.app.support.IntegrationTestBase;
import com.seehoo.rent.app.support.TestAlipayEnv;
import com.seehoo.rent.sdk.RentConstants;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 风控审核服务集成测试：咨询留痕/通过approve/拒绝close（真实网关联动+真实落库） */
class RiskServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private RiskService riskService;
    @Autowired
    private RentOrderMapper orderMapper;
    @Autowired
    private RiskAuditMapper riskAuditMapper;

    private RentOrder insertOrder() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("RSK" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.CREATED.name());
        order.setBuyerOpenId("OPENID_IT");
        orderMapper.insert(order);
        return order;
    }

    @Test
    void consultShouldRecordHistoryAndReturnLevel() {
        RentOrder order = insertOrder();

        OrderActionVo.RiskConsult vo = riskService.consult(order.getOutOrderId());

        // 网关返回T3，咨询记录落库留痕
        assertEquals("T3", vo.getRiskLevel());
        List<RiskAudit> audits = riskAuditMapper.selectByOrderId(order.getId());
        assertEquals(1, audits.size());
        assertEquals("T3", audits.get(0).getRiskLevel());
        assertEquals("0", audits.get(0).getSyncStatus());
        // 网关收到consult请求
        assertNotNull(TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_RISK_CONSULT));
    }

    @Test
    void auditShouldRejectWhenRejectReasonMissing() {
        OrderActionReq.RiskAudit req = new OrderActionReq.RiskAudit();
        req.setOutOrderId(insertOrder().getOutOrderId());
        req.setAuditResult("2");

        BusinessException e = assertThrows(BusinessException.class, () -> riskService.audit(req));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
    }

    @Test
    void auditPassShouldApproveAndSync() {
        RentOrder order = insertOrder();
        OrderActionReq.RiskAudit req = new OrderActionReq.RiskAudit();
        req.setOutOrderId(order.getOutOrderId());
        req.setAuditResult("1");

        riskService.audit(req);

        // 订单推进APPROVED，审核记录同步状态=已approve
        assertEquals(RentOrderStatus.APPROVED.name(),
                orderMapper.selectByOutOrderId(order.getOutOrderId()).getOrderStatus());
        List<RiskAudit> audits = riskAuditMapper.selectByOrderId(order.getId());
        assertEquals("1", audits.get(0).getSyncStatus());
        assertEquals("1", audits.get(0).getAuditResult());
        assertNotNull(TestAlipayEnv.GATEWAY.firstBizContent(
                RentConstants.METHOD_FULFILLMENT_APPROVE));
    }

    @Test
    void auditRejectShouldCloseOrderWithStandardReason() {
        RentOrder order = insertOrder();
        OrderActionReq.RiskAudit req = new OrderActionReq.RiskAudit();
        req.setOutOrderId(order.getOutOrderId());
        req.setAuditResult("2");
        req.setRejectReason("风险等级过高");

        riskService.audit(req);

        RentOrder saved = orderMapper.selectByOutOrderId(order.getOutOrderId());
        assertEquals(RentOrderStatus.CLOSED.name(), saved.getOrderStatus());
        assertEquals("3114", saved.getCloseReasonCode());
        List<RiskAudit> audits = riskAuditMapper.selectByOrderId(order.getId());
        assertEquals("2", audits.get(0).getSyncStatus());
        String bizContent = TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_CLOSE);
        assertTrue(bizContent.contains("\"reason_code\":\"3114\""));
        assertTrue(bizContent.contains("风险等级过高"));
    }

    @Test
    void consultShouldRejectUnknownOrder() {
        assertThrows(BusinessException.class, () -> riskService.consult("GHOST"));
    }
}
