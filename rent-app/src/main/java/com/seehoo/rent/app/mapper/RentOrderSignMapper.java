package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehoo.rent.app.entity.RentOrderSign;
import org.apache.ibatis.annotations.Mapper;

/** 订单签约信息Mapper */
@Mapper
public interface RentOrderSignMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentOrderSign> {

    /** 按订单查签约信息（1:1） */
    default RentOrderSign selectByOrderId(Long orderId) {
        return selectOne(new LambdaQueryWrapper<RentOrderSign>().eq(RentOrderSign::getOrderId, orderId));
    }
}
