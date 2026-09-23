package com.seehoo.rent.app.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehoo.rent.app.common.BeanCopyUtil;
import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.dto.GoodsPageReq;
import com.seehoo.rent.app.dto.GoodsVo;
import com.seehoo.rent.app.entity.RentGoods;
import com.seehoo.rent.app.mapper.RentGoodsMapper;
import com.seehoo.rent.app.mapper.RentGoodsSkuMapper;
import com.seehoo.rent.app.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/** 商品服务实现 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private RentGoodsMapper goodsMapper;
    @Resource
    private RentGoodsSkuMapper skuMapper;

    @Override
    public PageResult<GoodsVo> page(GoodsPageReq req) {
        Page<RentGoods> page = goodsMapper.selectGoodsPage(
                new Page<>(req.getPageNo(), req.getPageSize()), req.getParams());
        List<GoodsVo> records = page.getRecords().stream()
                .map(this::toVo).collect(Collectors.toList());
        return PageResult.of(page, records);
    }

    /** 商品+SKU组装VO（Long/BigDecimal由拷贝工具转String） */
    private GoodsVo toVo(RentGoods goods) {
        GoodsVo vo = BeanCopyUtil.mapper(goods, GoodsVo.class);
        vo.setSkuList(BeanCopyUtil.mapperCols(
                skuMapper.selectByGoodsId(goods.getId()), GoodsVo.SkuVo.class));
        return vo;
    }
}
