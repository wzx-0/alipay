package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 订单签约信息表：芝麻免押预授权冻结与代扣协议签约信息，与订单1:1 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_order_sign")
public class RentOrderSign extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 订单ID，关联tb_rent_order.id */
    private Long orderId;
    /** D10003签约状态：0-未签约 1-已签约 2-已解约 */
    private String signStatus;
    /** 芝麻信用服务ID */
    private String zmServiceId;
    /** 芝麻信用品类ID */
    private String categoryId;
    /** 代扣签约场景，固定RENT_DEDUCT */
    private String signScene;
    /** 预授权类目，私域DEPOSIT_CAR_LEASING_PRI */
    private String authCategory;
    /** D10004冻结状态：0-未冻结 1-已冻结 2-已解冻 3-已转支付 */
    private String freezeStatus;
    /** 预授权冻结金额(元) */
    private BigDecimal freezeAmount;
    /** 预授权冻结单号 */
    private String freezeOrderNo;
    /** 预授权冻结完成时间 */
    private LocalDateTime freezeTime;
    /** 预授权解冻时间 */
    private LocalDateTime unfreezeTime;
    /** D10005代扣协议状态：0-未签约 1-已签约 2-已解除 */
    private String deductStatus;
    /** 代扣协议编号 */
    private String deductAgreementNo;
    /** 签约完成时间 */
    private LocalDateTime signTime;
}
