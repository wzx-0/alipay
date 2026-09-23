package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehoo.rent.app.entity.RentGoodsSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 租赁商品SKU Mapper */
@Mapper
public interface RentGoodsSkuMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentGoodsSku> {

    /** 查商品下全部SKU */
    default List<RentGoodsSku> selectByGoodsId(Long goodsId) {
        return selectList(new LambdaQueryWrapper<RentGoodsSku>().eq(RentGoodsSku::getGoodsId, goodsId));
    }
}
