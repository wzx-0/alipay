package com.seehoo.rent.sdk.model;

import lombok.Data;

/**
 * 订单状态机枚举（支付宝侧状态流转）
 * <p>CREATED→SIGNED→APPROVED→DELIVERED→RECEIVED→FINISHED 为正向主链路；
 * RECEIVED→RETURN_DELIVERED→RETURN_RECEIVED→FINISHED 为归还链路；任意确认收货前状态可CLOSED</p>
 */
public enum RentOrderStatus {

    /** 已创建 */
    CREATED,
    /** 已签约（免押冻结成功） */
    SIGNED,
    /** 商家审核通过 */
    APPROVED,
    /** 已交付（商家交车） */
    DELIVERED,
    /** 已收货（用户确认收货） */
    RECEIVED,
    /** 归还已寄出（用户寄回） */
    RETURN_DELIVERED,
    /** 归还已收货（商家确认收到归还车辆） */
    RETURN_RECEIVED,
    /** 已完结（自动解冻+解除代扣） */
    FINISHED,
    /** 已关闭 */
    CLOSED
}
