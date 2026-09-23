package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehoo.rent.app.entity.RentOrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 订单商品明细Mapper */
@Mapper
public interface RentOrderItemMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentOrderItem> {

    /** 查订单全部商品明细 */
    default List<RentOrderItem> selectByOrderId(Long orderId) {
        return selectList(new LambdaQueryWrapper<RentOrderItem>().eq(RentOrderItem::getOrderId, orderId));
    }
}
