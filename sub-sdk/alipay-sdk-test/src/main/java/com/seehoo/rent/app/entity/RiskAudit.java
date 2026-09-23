package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/** 风控审核记录表：审核决策留痕(T1~T10、结论)，动作经approve/close同步支付宝 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_risk_audit")
public class RiskAudit extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 订单ID，关联tb_rent_order.id */
    private Long orderId;
    /** 综合风险等级T1~T10 */
    private String riskLevel;
    /** 风控咨询返回关键信息JSON */
    private String consultResult;
    /** D10006审核结论：1-通过 2-拒绝 3-转人工 */
    private String auditResult;
    /** 审核人，系统自动审核记SYSTEM */
    private String auditUser;
    /** 审核时间 */
    private LocalDateTime auditTime;
    /** 拒绝原因 */
    private String rejectReason;
    /** D10007同步状态：0-待同步 1-已同步approve 2-已同步close */
    private String syncStatus;
}
