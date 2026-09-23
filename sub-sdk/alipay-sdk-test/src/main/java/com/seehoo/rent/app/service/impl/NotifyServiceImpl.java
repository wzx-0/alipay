package com.seehoo.rent.app.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seehoo.rent.app.entity.AlipayNotifyRecord;
import com.seehoo.rent.app.entity.RentOrder;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import com.seehoo.rent.app.entity.RentOrderSign;
import com.seehoo.rent.app.entity.RentPayRecord;
import com.seehoo.rent.app.mapper.AlipayNotifyRecordMapper;
import com.seehoo.rent.app.mapper.RentOrderInstallmentMapper;
import com.seehoo.rent.app.mapper.RentOrderMapper;
import com.seehoo.rent.app.mapper.RentOrderSignMapper;
import com.seehoo.rent.app.mapper.RentPayItemMapper;
import com.seehoo.rent.app.mapper.RentPayRecordMapper;
import com.seehoo.rent.app.service.NotifyService;
import com.seehoo.rent.sdk.config.AlipayRentConfig;
import com.seehoo.rent.sdk.SignUtil;
import com.seehoo.rent.sdk.model.PayItem;
import com.seehoo.rent.sdk.model.RentOrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付宝异步通知服务实现：验签、notify_id幂等、报文落库、按类别分发更新业务表
 * <p>处理策略：验签失败应答fail（支付宝重试）；业务处理失败落库待处理并应答success防无限重试，
 * 补偿重试任务（J0108001）属后续阶段，TODO</p>
 */
@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    private static final String ACK_SUCCESS = "success";
    private static final String ACK_FAIL = "fail";
    private static final String KEY_BIZ_CONTENT = "biz_content";
    private static final String KEY_MSG_METHOD = "msg_method";
    private static final String KEY_NOTIFY_ID = "notify_id";
    private static final String KEY_APP_ID = "app_id";
    private static final String KEY_TRADE_STATUS = "trade_status";
    private static final String KEY_OUT_TRADE_NO = "out_trade_no";
    private static final String KEY_OUT_ORDER_ID = "out_order_id";
    private static final String KEY_TRADE_NO = "trade_no";
    private static final String KEY_OPERATION_STATUS = "operation_status";
    private static final String KEY_FUND_ORDER_NO = "fund_auth_order_no";
    /** 处理状态：待处理/成功/失败 */
    private static final String STATUS_PENDING = "0";
    private static final String STATUS_SUCCESS = "1";
    private static final String STATUS_FAILED = "2";
    /** 支付状态：成功/失败 */
    private static final String PAY_STATUS_SUCCESS = "1";
    private static final String PAY_STATUS_FAILED = "2";
    /** 账单/签约状态值 */
    private static final String BILL_STATUS_PAID = "2";
    private static final String FREEZE_STATUS_FROZEN = "1";
    private static final String SIGN_STATUS_SIGNED = "1";
    private static final String SIGN_PASS = "1";
    /** 通知类别 */
    private static final String CATEGORY_PAY = "PAY";
    private static final String CATEGORY_FREEZE = "FUND_AUTH_FREEZE";
    private static final String CATEGORY_MESSAGE = "MESSAGE";
    /** 通用通知支付成功状态 */
    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    private static final String TRADE_FINISHED = "TRADE_FINISHED";
    /** 冻结操作成功状态（TODO按沙箱实测值校正） */
    private static final String FREEZE_OPERATION_SUCCESS = "FREEZE_SUCCESS";

    @Resource
    private AlipayNotifyRecordMapper notifyRecordMapper;
    @Resource
    private RentOrderMapper orderMapper;
    @Resource
    private RentOrderSignMapper signMapper;
    @Resource
    private RentPayRecordMapper payRecordMapper;
    @Resource
    private RentPayItemMapper payItemMapper;
    @Resource
    private RentOrderInstallmentMapper installmentMapper;
    @Resource
    private AlipayRentConfig alipayRentConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleNotify(Map<String, String> params) {
        // 验签失败直接fail，支付宝会重试
        boolean signOk = SignUtil.rsaCheckNotify(params, alipayRentConfig.getAlipayPublicKey());
        if (!signOk) {
            log.error("支付宝通知验签失败：notify_id={}", params.get(KEY_NOTIFY_ID));
            return ACK_FAIL;
        }

        AlipayNotifyRecord record = saveNotifyRecord(params);
        try {
            dispatch(params, record);
            markProcess(record, STATUS_SUCCESS, null);
        } catch (Exception e) {
            log.error("支付宝通知处理失败：notify_id={}", record.getNotifyId(), e);
            markProcess(record, STATUS_FAILED, e.getMessage());
        }
        return ACK_SUCCESS;
    }

    /** 通知落库：notify_id幂等（重复通知返回已有记录，受理过即应答成功） */
    private AlipayNotifyRecord saveNotifyRecord(Map<String, String> params) {
        String notifyId = StrUtil.blankToDefault(params.get(KEY_NOTIFY_ID), digestOf(params));
        AlipayNotifyRecord exist = notifyRecordMapper.selectByNotifyId(notifyId);
        if (exist != null) {
            return exist;
        }
        AlipayNotifyRecord record = new AlipayNotifyRecord();
        record.setNotifyId(notifyId);
        record.setMsgMethod(params.get(KEY_MSG_METHOD));
        record.setMsgCategory(categorize(params));
        record.setAppId(params.get(KEY_APP_ID));
        record.setBizContent(params.get(KEY_BIZ_CONTENT));
        record.setSignResult(SIGN_PASS);
        record.setProcessStatus(STATUS_PENDING);
        try {
            notifyRecordMapper.insert(record);
        } catch (DuplicateKeyException e) {
            // 并发重复通知：忽略，走已存在记录
        }
        return notifyRecordMapper.selectByNotifyId(notifyId);
    }

    /** 无notify_id时按报文摘要生成幂等键 */
    private String digestOf(Map<String, String> params) {
        return DigestUtil.md5Hex(StrUtil.nullToEmpty(params.get(KEY_BIZ_CONTENT)));
    }

    /** 按报文特征归类通知 */
    private String categorize(Map<String, String> params) {
        if (StrUtil.isNotBlank(params.get(KEY_MSG_METHOD))) {
            return CATEGORY_MESSAGE;
        }
        String biz = StrUtil.nullToEmpty(params.get(KEY_BIZ_CONTENT));
        if (biz.contains(KEY_TRADE_STATUS)) {
            return CATEGORY_PAY;
        }
        if (biz.contains(KEY_OPERATION_STATUS)) {
            return CATEGORY_FREEZE;
        }
        return CATEGORY_MESSAGE;
    }

    /** 分发处理：支付/冻结通知更新业务表；消息类（售后/订单变更）落库待后续阶段 */
    private void dispatch(Map<String, String> params, AlipayNotifyRecord record) {
        Map<String, Object> biz = parseBiz(params.get(KEY_BIZ_CONTENT));
        if (CATEGORY_PAY.equals(record.getMsgCategory())) {
            handlePayNotify(biz);
            return;
        }
        if (CATEGORY_FREEZE.equals(record.getMsgCategory())) {
            handleFreezeNotify(biz);
            return;
        }
        // TODO 售后(aftersale.notify)/订单变更(order.changed)业务处理属后续阶段，已落库留痕
        log.info("通知已受理待后续处理：category={}, notifyId={}", record.getMsgCategory(), record.getNotifyId());
    }

    /** 支付结果通知：更新支付流水与期次账单状态 */
    private void handlePayNotify(Map<String, Object> biz) {
        String outTradeNo = str(biz.get(KEY_OUT_TRADE_NO));
        RentPayRecord payRecord = payRecordMapper.selectByOutTradeNo(outTradeNo);
        if (payRecord == null) {
            // 找不到流水按处理失败落库，等待补偿任务重试，避免通知被误标成功
            throw new IllegalStateException("支付通知无对应流水：outTradeNo=" + outTradeNo);
        }
        String tradeStatus = str(biz.get(KEY_TRADE_STATUS));
        boolean success = TRADE_SUCCESS.equals(tradeStatus) || TRADE_FINISHED.equals(tradeStatus);
        if (!success && payRecord.getPayTime() != null) {
            return;
        }
        payRecord.setTradeNo(str(biz.get(KEY_TRADE_NO)));
        payRecord.setNotifyTime(LocalDateTime.now());
        payRecord.setPayTime(LocalDateTime.now());
        payRecord.setPayStatus(success ? PAY_STATUS_SUCCESS : PAY_STATUS_FAILED);
        payRecordMapper.updateById(payRecord);

        if (success) {
            markInstallmentsPaid(payRecord);
        }
    }

    /** 支付成功后按费项更新期次账单 */
    private void markInstallmentsPaid(RentPayRecord payRecord) {
        List<com.seehoo.rent.app.entity.RentPayItem> items = payItemMapper.selectByPayRecordId(payRecord.getId());
        for (com.seehoo.rent.app.entity.RentPayItem item : items) {
            if (!PayItem.FeeType.RENT.name().equals(item.getFeeType()) || item.getInstallmentNo() == null) {
                continue;
            }
            RentOrderInstallment inst = installmentMapper.selectByOrderIdAndNo(payRecord.getOrderId(), item.getInstallmentNo());
            if (inst == null) {
                continue;
            }
            inst.setBillStatus(BILL_STATUS_PAID);
            BigDecimal paid = inst.getPaidAmount() == null ? BigDecimal.ZERO : inst.getPaidAmount();
            BigDecimal amount = item.getPayAmount() == null ? BigDecimal.ZERO : item.getPayAmount();
            inst.setPaidAmount(paid.add(amount));
            inst.setActualPayTime(payRecord.getPayTime());
            installmentMapper.updateById(inst);
        }
    }

    /** 预授权冻结结果通知：更新签约状态并推进订单至SIGNED */
    private void handleFreezeNotify(Map<String, Object> biz) {
        String outOrderId = str(biz.get(KEY_OUT_ORDER_ID));
        RentOrder order = orderMapper.selectByOutOrderId(outOrderId);
        if (order == null) {
            throw new IllegalStateException("冻结通知无对应订单：outOrderId=" + outOrderId);
        }
        String operationStatus = str(biz.get(KEY_OPERATION_STATUS));
        if (FREEZE_OPERATION_SUCCESS.equals(operationStatus)) {
            if (RentOrderStatus.CREATED.name().equals(order.getOrderStatus())) {
                order.setOrderStatus(RentOrderStatus.SIGNED.name());
                orderMapper.updateById(order);
            }
            updateSignFrozen(order.getId(), biz);
        } else {
            // TODO 解冻/转支付通知字段结构待联调确认
            log.info("冻结非成功状态通知：outOrderId={}, operationStatus={}", outOrderId, operationStatus);
        }
    }

    private void updateSignFrozen(Long orderId, Map<String, Object> biz) {
        RentOrderSign sign = signMapper.selectByOrderId(orderId);
        if (sign == null) {
            return;
        }
        sign.setFreezeStatus(FREEZE_STATUS_FROZEN);
        sign.setFreezeOrderNo(str(biz.get(KEY_FUND_ORDER_NO)));
        sign.setFreezeTime(LocalDateTime.now());
        sign.setSignStatus(SIGN_STATUS_SIGNED);
        sign.setSignTime(LocalDateTime.now());
        signMapper.updateById(sign);
    }

    private Map<String, Object> parseBiz(String bizContent) {
        if (StrUtil.isBlank(bizContent)) {
            return java.util.Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(bizContent, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new IllegalStateException("通知biz_content解析失败", e);
        }
    }

    private String str(Object val) {
        return val == null ? null : String.valueOf(val);
    }

    private void markProcess(AlipayNotifyRecord record, String status, String failReason) {
        record.setProcessStatus(status);
        record.setProcessTime(LocalDateTime.now());
        record.setFailReason(failReason);
        notifyRecordMapper.updateById(record);
    }
}
