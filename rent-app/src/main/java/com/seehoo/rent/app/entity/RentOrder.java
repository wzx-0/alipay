package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 租赁订单主表：承载订单状态机(与支付宝订单双向映射)及创单要素 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_order")
public class RentOrder extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 商家侧订单号(out_order_id)，全局唯一 */
    private String outOrderId;
    /** 支付宝订单ID(order_id) */
    private String alipayOrderId;
    /** 原订阅订单号，续订/买断溯源 */
    private String originOrderId;
    /** 订单类型：RENT/RELET/BUYOUT */
    private String orderType;
    /** 订单状态：CREATED/SIGNED/APPROVED/DELIVERED/RECEIVED/RETURN_DELIVERED/RETURN_RECEIVED/FINISHED/CLOSED */
    private String orderStatus;
    /** 业务身份，固定CAR_SUBSCRIPTION */
    private String bizIdentity;
    /** D10001来源渠道：1-公域 0-私域 */
    private String sourceChannel;
    /** 订单标题 */
    private String title;
    /** 买家支付宝userId */
    private String buyerId;
    /** 买家支付宝openId */
    private String buyerOpenId;
    /** 下单前置判断sourceId */
    private String sourceId;
    /** 订单总价(元) */
    private BigDecimal orderPrice;
    /** 押金金额(元)，芝麻免押预授权冻结金额 */
    private BigDecimal depositPrice;
    /** 运费(元) */
    private BigDecimal freight;
    /** 增值服务费(元) */
    private BigDecimal additionalPrice;
    /** 租赁开始时间 */
    private LocalDateTime rentStartTime;
    /** 租赁结束时间 */
    private LocalDateTime rentEndTime;
    /** 配送方式，固定SELFPICK */
    private String deliveryType;
    /** 自提门店名称 */
    private String shopName;
    /** 自提门店地址 */
    private String shopAddress;
    /** 自提门店电话 */
    private String shopTel;
    /** 默认收货人姓名 */
    private String receiverName;
    /** 默认收货人电话 */
    private String receiverTel;
    /** 默认收货详细地址 */
    private String receiverAddress;
    /** 租赁协议名称 */
    private String protocolName;
    /** 租赁协议页面路径 */
    private String protocolPath;
    /** 商家订单详情页路径 */
    private String detailPath;
    /** 商家透传数据(merchantExtInfo)JSON */
    private String merchantExtInfo;
    /** 发货(交车)时间 */
    private LocalDateTime deliveryTime;
    /** 用户确认收车时间 */
    private LocalDateTime receiveTime;
    /** 用户发起还车时间 */
    private LocalDateTime returnSendTime;
    /** 商家验收收车时间 */
    private LocalDateTime returnReceiveTime;
    /** 订单完结时间 */
    private LocalDateTime finishTime;
    /** 完结方式：USER_RETURNED/USER_RETURNED_IN_ADVANCE/OTHER */
    private String finishStatus;
    /** 关单原因编码 */
    private String closeReasonCode;
    /** 关单原因描述 */
    private String closeReasonDesc;
    /** 关单时间 */
    private LocalDateTime closeTime;
}
