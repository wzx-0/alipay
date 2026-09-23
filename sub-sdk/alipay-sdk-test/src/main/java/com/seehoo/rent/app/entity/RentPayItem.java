package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 支付费项明细表：pay_items逐费项金额，关联支付记录与订阅期次 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_pay_item")
public class RentPayItem extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 支付记录ID，关联tb_rent_pay_record.id */
    private Long payRecordId;
    /** 订单ID，冗余便于按期次核对 */
    private Long orderId;
    /** 费项类型：RENT-订阅金 INDEMNITY-赔付违约金 */
    private String feeType;
    /** 期号，费项为订阅金时必填 */
    private Integer installmentNo;
    /** 费项支付金额(元) */
    private BigDecimal payAmount;
}
