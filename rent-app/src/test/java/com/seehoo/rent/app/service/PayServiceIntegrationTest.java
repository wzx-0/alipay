package com.seehoo.rent.app.service;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.common.BusinessException;
import com.seehoo.rent.app.common.RentErrorCode;
import com.seehoo.rent.app.dto.OrderActionVo;
import com.seehoo.rent.app.dto.PayReq;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import com.seehoo.rent.app.entity.RentPayRecord;
import com.seehoo.rent.app.mapper.RentOrderInstallmentMapper;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RentPayItemMapper;
import com.seehoo.rent.app.mapper.RentPayRecordMapper;
import com.seehoo.rent.app.support.IntegrationTestBase;
import com.seehoo.rent.app.support.TestAlipayEnv;
import com.seehoo.rent.sdk.RentConstants;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 支付服务集成测试：真实落库支付流水/费项/期次状态联动，网关侧验证pay请求要素 */
class PayServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private PayService payService;
    @Autowired
    private RentOrderMapper orderMapper;
    @Autowired
    private RentPayRecordMapper payRecordMapper;
    @Autowired
    private RentPayItemMapper payItemMapper;
    @Autowired
    private RentOrderInstallmentMapper installmentMapper;

    private RentOrder insertOrder() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("PAY" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.SIGNED.name());
        order.setBuyerOpenId("OPENID_IT");
        order.setDepositPrice(new BigDecimal("5000"));
        order.setOrderPrice(new BigDecimal("1000"));
        orderMapper.insert(order);
        return order;
    }

    private RentOrderInstallment insertInstallment(RentOrder order, int no, String billStatus) {
        RentOrderInstallment inst = new RentOrderInstallment();
        inst.setOrderId(order.getId());
        inst.setInstallmentNo(no);
        inst.setInstallmentPrice(new BigDecimal("1000"));
        inst.setBillStatus(billStatus);
        installmentMapper.insert(inst);
        return inst;
    }

    private PayReq payReq(RentOrder order, String feeType, Integer installmentNo, String amount) {
        PayReq req = new PayReq();
        req.setOutOrderId(order.getOutOrderId());
        req.setPayMethod("JSAPI");
        PayReq.PayItem item = new PayReq.PayItem();
        item.setFeeType(feeType);
        item.setInstallmentNo(installmentNo);
        item.setPayAmount(amount);
        req.setPayItems(Arrays.asList(item));
        return req;
    }

    @Test
    void payShouldPersistRecordItemsAndMarkInstallmentPaying() {
        RentOrder order = insertOrder();
        RentOrderInstallment inst = insertInstallment(order, 1, "0");

        OrderActionVo.Pay vo = payService.pay(payReq(order, "RENT", 1, "1000"));

        // 同步返回trade_no（网关真实返回）
        assertEquals(order.getOutOrderId() + "P0001", vo.getOutTradeNo());
        assertEquals("1000", vo.getPayAmount());

        // 流水落库且置成功
        RentPayRecord record = payRecordMapper.selectByOutTradeNo(vo.getOutTradeNo());
        assertEquals("1", record.getPayStatus());
        assertEquals(0, new BigDecimal("1000").compareTo(record.getPayAmount()));
        assertEquals("ALIPAY", record.getPayChannel());
        assertTrue(record.getPayTime() != null);

        // 费项流水落库
        assertEquals(1, payItemMapper.selectByPayRecordId(record.getId()).size());

        // 期次置支付中
        assertEquals("1", installmentMapper.selectByOrderIdAndNo(order.getId(), 1).getBillStatus());

        // 网关收到pay请求：费项/通知地址正确
        String bizContent = TestAlipayEnv.GATEWAY.firstBizContent(RentConstants.METHOD_ORDER_PAY);
        assertTrue(bizContent.contains("\"type\":\"RENT\""));
        assertTrue(bizContent.contains("\"installment_no\":1"));
        assertTrue(bizContent.contains("\"pay_notify_url\":\"http://127.0.0.1:1/app-notify\""));
    }

    @Test
    void outTradeNoShouldIncrementByExistingRecords() {
        RentOrder order = insertOrder();
        insertInstallment(order, 1, "0");

        payService.pay(payReq(order, "RENT", 1, "1000"));
        OrderActionVo.Pay second = payService.pay(payReq(order, "RENT", 1, "1000"));

        assertEquals(order.getOutOrderId() + "P0002", second.getOutTradeNo());
    }

    @Test
    void payShouldRejectUnknownOrder() {
        PayReq req = payReq(insertOrder(), "RENT", 1, "1000");
        req.setOutOrderId("GHOST");
        assertThrows(BusinessException.class, () -> payService.pay(req));
    }

    @Test
    void payShouldRejectRentItemWithoutInstallmentNo() {
        RentOrder order = insertOrder();
        BusinessException e = assertThrows(BusinessException.class,
                () -> payService.pay(payReq(order, "RENT", null, "1000")));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
    }

    @Test
    void paidInstallmentShouldNotBeOverwritten() {
        RentOrder order = insertOrder();
        insertInstallment(order, 1, "2");

        payService.pay(payReq(order, "RENT", 1, "1000"));

        assertEquals("2", installmentMapper.selectByOrderIdAndNo(order.getId(), 1).getBillStatus());
    }

    @Test
    void payAmountShouldSumAllItems() {
        RentOrder order = insertOrder();
        PayReq req = new PayReq();
        req.setOutOrderId(order.getOutOrderId());
        req.setPayMethod("JSAPI");
        PayReq.PayItem rent = new PayReq.PayItem();
        rent.setFeeType("RENT");
        rent.setInstallmentNo(1);
        rent.setPayAmount("1000.50");
        PayReq.PayItem indemnity = new PayReq.PayItem();
        indemnity.setFeeType("INDEMNITY");
        indemnity.setPayAmount("99.50");
        req.setPayItems(Arrays.asList(rent, indemnity));

        OrderActionVo.Pay vo = payService.pay(req);
        assertEquals("1100.00", vo.getPayAmount());
    }

    @Test
    void payShouldRejectInvalidFeeType() {
        RentOrder order = insertOrder();
        BusinessException e = assertThrows(BusinessException.class,
                () -> payService.pay(payReq(order, "BAD_TYPE", 1, "1000")));
        assertEquals(RentErrorCode.PARAM_ERROR.getCode(), e.getCode());
        List<RentPayRecord> records = payRecordMapper.selectByOrderId(order.getId());
        assertTrue(records.isEmpty());
    }
}
