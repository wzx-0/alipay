package com.seehoo.rent.app.dto;

import lombok.Data;

import java.time.LocalDateTime;

/** 订单信息VO：订单分页与订单详情的订单主体（时间字段由Jackson统一序列化为yyyy-MM-dd HH:mm:ss） */
@Data
public class OrderInfoVo {

    private String id;
    private String outOrderId;
    private String alipayOrderId;
    /** 原订阅订单号（续订/买断溯源） */
    private String originOrderId;
    /** 订单类型：RENT/RELET/BUYOUT */
    private String orderType;
    /** 订单状态 */
    private String orderStatus;
    /** 来源渠道：1-公域 0-私域 */
    private String sourceChannel;
    /** 订单标题 */
    private String title;
    /** 买家支付宝userId */
    private String buyerId;
    private String buyerOpenId;
    /** 订单总价(元) */
    private String orderPrice;
    /** 押金金额(元) */
    private String depositPrice;
    private String freight;
    private String additionalPrice;
    private LocalDateTime rentStartTime;
    private LocalDateTime rentEndTime;
    /** 配送方式，固定SELFPICK */
    private String deliveryType;
    private String shopName;
    private String shopAddress;
    private String shopTel;
    private String receiverName;
    private String receiverTel;
    private String receiverAddress;
    private String protocolName;
    private String protocolPath;
    /** 订单详情页路径 */
    private String detailPath;
    /** 完结方式 */
    private String finishStatus;
    private String closeReasonCode;
    private String closeReasonDesc;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;
    private LocalDateTime returnSendTime;
    private LocalDateTime returnReceiveTime;
    private LocalDateTime finishTime;
    private LocalDateTime closeTime;
    private LocalDateTime createTime;
}
