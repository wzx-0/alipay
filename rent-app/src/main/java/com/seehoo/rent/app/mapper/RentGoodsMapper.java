package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehoo.rent.app.dto.GoodsPageReq;
import com.seehoo.rent.app.entity.RentGoods;
import org.apache.ibatis.annotations.Mapper;

/** 租赁商品Mapper */
@Mapper
public interface RentGoodsMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentGoods> {

    /** 按条件分页查询商品（Wrapper拼装收敛在Mapper层） */
    default Page<RentGoods> selectGoodsPage(Page<RentGoods> page, GoodsPageReq.Query q) {
        LambdaQueryWrapper<RentGoods> wrapper = new LambdaQueryWrapper<RentGoods>()
                .like(StringUtils.isNotBlank(q.getGoodsName()), RentGoods::getGoodsName, q.getGoodsName())
                .eq(StringUtils.isNotBlank(q.getGoodsStatus()), RentGoods::getGoodsStatus, q.getGoodsStatus())
                .eq(StringUtils.isNotBlank(q.getReportStatus()), RentGoods::getReportStatus, q.getReportStatus())
                .orderByDesc(RentGoods::getId);
        return selectPage(page, wrapper);
    }

    /** 按商品编码查唯一商品 */
    default RentGoods selectByGoodsCode(String goodsCode) {
        return selectOne(new LambdaQueryWrapper<RentGoods>().eq(RentGoods::getGoodsCode, goodsCode));
    }
}
