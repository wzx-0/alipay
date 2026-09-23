package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/** 支付宝异步通知记录表：统一接收冻结/支付/售后/订单变更通知，支撑验签、幂等与重试 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_alipay_notify_record")
public class AlipayNotifyRecord extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 通知ID(notify_id)，幂等去重 */
    private String notifyId;
    /** 消息方法名(msg_method) */
    private String msgMethod;
    /** 通知类别：FUND_AUTH_FREEZE/PAY/AFTERSALE/ORDER_CHANGED */
    private String msgCategory;
    /** 来源应用ID(app_id) */
    private String appId;
    /** 通知报文原文(biz_content)JSON */
    private String bizContent;
    /** 验签结果：1-通过 0-失败 */
    private String signResult;
    /** D10014处理状态：0-待处理 1-处理成功 2-处理失败 */
    private String processStatus;
    /** 业务处理完成时间 */
    private LocalDateTime processTime;
    /** 重试次数 */
    private Integer retryCount;
    /** 失败原因 */
    private String failReason;
}
