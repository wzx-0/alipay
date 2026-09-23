package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 订阅计划期次表：rent_plan_info逐期计划，代扣调度与账单核对依据 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_order_installment")
public class RentOrderInstallment extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 订单ID，关联tb_rent_order.id */
    private Long orderId;
    /** 期号，从1开始递增 */
    private Integer installmentNo;
    /** 当期订阅金额(元) */
    private BigDecimal installmentPrice;
    /** 计划扣款时间，第2期起到达此时间方可代扣 */
    private LocalDateTime planPayTime;
    /** 到期购买金额(元)，仅最后一期 */
    private BigDecimal buyoutPrice;
    /** D10002账单状态：0-待支付 1-支付中 2-已支付 3-支付失败 4-已关闭 */
    private String billStatus;
    /** 已付金额(元) */
    private BigDecimal paidAmount;
    /** 实际支付完成时间 */
    private LocalDateTime actualPayTime;
}
