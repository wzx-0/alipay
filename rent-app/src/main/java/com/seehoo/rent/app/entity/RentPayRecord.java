package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 支付记录表：每次订单支付(order.pay/pay.sync)流水 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_pay_record")
public class RentPayRecord extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 订单ID，关联tb_rent_order.id */
    private Long orderId;
    /** 商家侧支付单号(out_trade_no)，全局唯一 */
    private String outTradeNo;
    /** 支付宝交易号(trade_no) */
    private String tradeNo;
    /** 支付方式：RENT_DEDUCT/PRE_AUTH/JSAPI */
    private String payMethod;
    /** 支付金额(元) */
    private BigDecimal payAmount;
    /** D10008支付状态：0-处理中 1-成功 2-失败 3-已全额退款 */
    private String payStatus;
    /** 收款渠道：ALIPAY/OTHER */
    private String payChannel;
    /** 支付完成时间 */
    private LocalDateTime payTime;
    /** 支付结果通知到达时间 */
    private LocalDateTime notifyTime;
    /** 已退款金额(元) */
    private BigDecimal refundAmount;
}
