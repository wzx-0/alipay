package com.seehoo.rent.app.service;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.entity.AlipayNotifyRecord;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import com.seehoo.rent.app.entity.RentOrderSign;
import com.seehoo.rent.app.entity.RentPayItem;
import com.seehoo.rent.app.entity.RentPayRecord;
import com.seehoo.rent.app.mapper.AlipayNotifyRecordMapper;
import com.seehoo.rent.app.mapper.RentOrderInstallmentMapper;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RentOrderSignMapper;
import com.seehoo.rent.app.mapper.RentPayItemMapper;
import com.seehoo.rent.app.mapper.RentPayRecordMapper;
import com.seehoo.rent.app.support.IntegrationTestBase;
import com.seehoo.rent.app.support.TestAlipayEnv;
import com.seehoo.rent.sdk.SignUtil;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 支付宝异步通知集成测试：应用配置的支付宝公钥=测试密钥对公钥，
 * 通知参数用对应私钥真实签名，走完整验签→落库→分发→业务更新链路。
 */
class NotifyServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private NotifyService notifyService;
    @Autowired
    private AlipayNotifyRecordMapper notifyRecordMapper;
    @Autowired
    private RentOrderMapper orderMapper;
    @Autowired
    private RentOrderSignMapper signMapper;
    @Autowired
    private RentPayRecordMapper payRecordMapper;
    @Autowired
    private RentPayItemMapper payItemMapper;
    @Autowired
    private RentOrderInstallmentMapper installmentMapper;

    /** 用测试私钥签通知参数（sign与sign_type按官方规则处理） */
    private Map<String, String> signedParams(Map<String, String> bizParams) {
        Map<String, String> params = new HashMap<>(bizParams);
        params.put("sign_type", "RSA2");
        Map<String, String> toSign = new HashMap<>(params);
        toSign.remove("sign_type");
        params.put("sign", SignUtil.sign(toSign,
                Base64.getEncoder().encodeToString(TestAlipayEnv.KEY_PAIR.getPrivate().getEncoded())));
        return params;
    }

    private Map<String, String> payNotify(String notifyId, String outTradeNo) {
        Map<String, String> biz = new HashMap<>();
        biz.put("notify_id", notifyId);
        biz.put("biz_content", "{\"out_trade_no\":\"" + outTradeNo
                + "\",\"trade_status\":\"TRADE_SUCCESS\",\"trade_no\":\"TN" + notifyId + "\"}");
        return signedParams(biz);
    }

    private RentPayRecord insertPayRecord(Long orderId, String outTradeNo) {
        RentPayRecord record = new RentPayRecord();
        record.setOrderId(orderId);
        record.setOutTradeNo(outTradeNo);
        record.setPayMethod("JSAPI");
        record.setPayAmount(new BigDecimal("1000"));
        record.setPayChannel("ALIPAY");
        record.setPayStatus("0");
        payRecordMapper.insert(record);

        RentPayItem item = new RentPayItem();
        item.setPayRecordId(record.getId());
        item.setOrderId(orderId);
        item.setFeeType("RENT");
        item.setInstallmentNo(1);
        item.setPayAmount(new BigDecimal("1000"));
        payItemMapper.insert(item);
        return record;
    }

    @Test
    void shouldReturnFailWhenSignInvalid() {
        Map<String, String> params = payNotify("NID_A1", "NO_TRADE");
        params.put("sign", "tampered");

        String ack = notifyService.handleNotify(params);

        assertEquals("fail", ack);
        assertNull(notifyRecordMapper.selectByNotifyId("NID_A1"));
    }

    @Test
    void shouldUpdatePayRecordAndInstallmentsOnPaySuccess() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("NTF" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.SIGNED.name());
        orderMapper.insert(order);

        RentOrderInstallment inst = new RentOrderInstallment();
        inst.setOrderId(order.getId());
        inst.setInstallmentNo(1);
        inst.setInstallmentPrice(new BigDecimal("1000"));
        inst.setBillStatus("1");
        installmentMapper.insert(inst);

        RentPayRecord record = insertPayRecord(order.getId(), "NTF" + IdUtil.getSnowflakeNextIdStr());
        String ack = notifyService.handleNotify(payNotify("NID_B1", record.getOutTradeNo()));

        assertEquals("success", ack);
        // 流水已置成功
        RentPayRecord saved = payRecordMapper.selectByOutTradeNo(record.getOutTradeNo());
        assertEquals("1", saved.getPayStatus());
        assertEquals("TNNID_B1", saved.getTradeNo());
        assertNotNull(saved.getPayTime());
        // 期次置已支付并累计金额
        RentOrderInstallment savedInst = installmentMapper.selectByOrderIdAndNo(order.getId(), 1);
        assertEquals("2", savedInst.getBillStatus());
        assertEquals(0, new BigDecimal("1000").compareTo(savedInst.getPaidAmount()));
        // 通知记录处理成功
        assertEquals("1", notifyRecordMapper.selectByNotifyId("NID_B1").getProcessStatus());
    }

    @Test
    void shouldKeepSuccessRecordWhenFailureNotifyArrivesAfterPaid() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("NTF" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.SIGNED.name());
        orderMapper.insert(order);
        RentPayRecord record = insertPayRecord(order.getId(), "NTF" + IdUtil.getSnowflakeNextIdStr());
        record.setPayStatus("1");
        record.setPayTime(java.time.LocalDateTime.now());
        payRecordMapper.updateById(record);

        Map<String, String> biz = new HashMap<>();
        biz.put("notify_id", "NID_C1");
        biz.put("biz_content", "{\"out_trade_no\":\"" + record.getOutTradeNo()
                + "\",\"trade_status\":\"TRADE_CLOSED\"}");
        String ack = notifyService.handleNotify(signedParams(biz));

        assertEquals("success", ack);
        assertEquals("1", payRecordMapper.selectByOutTradeNo(record.getOutTradeNo()).getPayStatus());
    }

    @Test
    void shouldMarkFailedWhenPayRecordNotFound() {
        String ack = notifyService.handleNotify(payNotify("NID_D1", "GHOST_TRADE"));

        assertEquals("success", ack);
        assertEquals("2", notifyRecordMapper.selectByNotifyId("NID_D1").getProcessStatus());
    }

    @Test
    void shouldAdvanceOrderToSignedOnFreezeSuccess() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("NTF" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.CREATED.name());
        orderMapper.insert(order);
        RentOrderSign sign = new RentOrderSign();
        sign.setOrderId(order.getId());
        sign.setSignStatus("0");
        signMapper.insert(sign);

        Map<String, String> biz = new HashMap<>();
        biz.put("notify_id", "NID_E1");
        biz.put("biz_content", "{\"operation_status\":\"FREEZE_SUCCESS\",\"out_order_id\":\""
                + order.getOutOrderId() + "\",\"fund_auth_order_no\":\"FROZEN001\"}");
        String ack = notifyService.handleNotify(signedParams(biz));

        assertEquals("success", ack);
        assertEquals(RentOrderStatus.SIGNED.name(),
                orderMapper.selectByOutOrderId(order.getOutOrderId()).getOrderStatus());
        RentOrderSign savedSign = signMapper.selectByOrderId(order.getId());
        assertEquals("1", savedSign.getFreezeStatus());
        assertEquals("FROZEN001", savedSign.getFreezeOrderNo());
        assertEquals("1", savedSign.getSignStatus());
    }

    @Test
    void shouldBeIdempotentOnRepeatedNotifyId() {
        RentOrder order = new RentOrder();
        order.setOutOrderId("NTF" + IdUtil.getSnowflakeNextIdStr());
        order.setOrderType("RENT");
        order.setOrderStatus(RentOrderStatus.SIGNED.name());
        orderMapper.insert(order);
        RentPayRecord record = insertPayRecord(order.getId(), "NTF" + IdUtil.getSnowflakeNextIdStr());

        notifyService.handleNotify(payNotify("NID_F1", record.getOutTradeNo()));
        notifyService.handleNotify(payNotify("NID_F1", record.getOutTradeNo()));

        // 幂等：同一notify_id只落一条记录
        assertEquals(1, countNotify("NID_F1"));
    }

    private int countNotify(String notifyId) {
        return notifyRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AlipayNotifyRecord>()
                        .eq(AlipayNotifyRecord::getNotifyId, notifyId)).size();
    }

    @Test
    void shouldStoreMessageNotifyForLaterProcessing() {
        Map<String, String> biz = new HashMap<>();
        biz.put("notify_id", "NID_G1");
        biz.put("msg_method", "alipay.commerce.rent.order.aftersale.notify");
        biz.put("biz_content", "{\"aftersale_id\":\"AS001\"}");
        String ack = notifyService.handleNotify(signedParams(biz));

        assertEquals("success", ack);
        AlipayNotifyRecord record = notifyRecordMapper.selectByNotifyId("NID_G1");
        assertEquals("MESSAGE", record.getMsgCategory());
        assertEquals("1", record.getProcessStatus());
    }
}
