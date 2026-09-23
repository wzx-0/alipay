package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehoo.rent.app.dto.OrderPageReq;
import com.seehoo.rent.app.entity.RentOrder;
import org.apache.ibatis.annotations.Mapper;

/** 租赁订单Mapper */
@Mapper
public interface RentOrderMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentOrder> {

    /** 按条件分页查询订单（Wrapper拼装收敛在Mapper层） */
    default Page<RentOrder> selectOrderPage(Page<RentOrder> page, OrderPageReq.Query q) {
        LambdaQueryWrapper<RentOrder> wrapper = new LambdaQueryWrapper<RentOrder>()
                .eq(StringUtils.isNotBlank(q.getOutOrderId()), RentOrder::getOutOrderId, q.getOutOrderId())
                .eq(StringUtils.isNotBlank(q.getAlipayOrderId()), RentOrder::getAlipayOrderId, q.getAlipayOrderId())
                .eq(StringUtils.isNotBlank(q.getOrderType()), RentOrder::getOrderType, q.getOrderType())
                .eq(StringUtils.isNotBlank(q.getOrderStatus()), RentOrder::getOrderStatus, q.getOrderStatus())
                .eq(StringUtils.isNotBlank(q.getSourceChannel()), RentOrder::getSourceChannel, q.getSourceChannel())
                .eq(StringUtils.isNotBlank(q.getBuyerId()), RentOrder::getBuyerId, q.getBuyerId())
                .ge(StringUtils.isNotBlank(q.getCreateTimeBegin()), RentOrder::getCreateTime, q.getCreateTimeBegin())
                .le(StringUtils.isNotBlank(q.getCreateTimeEnd()), RentOrder::getCreateTime, q.getCreateTimeEnd())
                .orderByDesc(RentOrder::getId);
        return selectPage(page, wrapper);
    }

    /** 按商家侧订单号查唯一订单 */
    default RentOrder selectByOutOrderId(String outOrderId) {
        return selectOne(new LambdaQueryWrapper<RentOrder>().eq(RentOrder::getOutOrderId, outOrderId));
    }
}
