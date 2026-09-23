package com.seehoo.rent.app.service;

import cn.hutool.core.util.IdUtil;
import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.dto.GoodsPageReq;
import com.seehoo.rent.app.dto.GoodsVo;
import com.seehoo.rent.app.entity.RentGoods;
import com.seehoo.rent.app.entity.RentGoodsSku;
import com.seehoo.rent.app.mapper.RentGoodsMapper;
import com.seehoo.rent.app.mapper.RentGoodsSkuMapper;
import com.seehoo.rent.app.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 商品服务集成测试：真实落库商品+SKU后分页组装 */
class GoodsServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RentGoodsMapper goodsMapper;
    @Autowired
    private RentGoodsSkuMapper skuMapper;

    @Test
    void pageShouldAssembleGoodsWithSkuList() {
        RentGoods goods = new RentGoods();
        goods.setGoodsCode("GC" + IdUtil.getSnowflakeNextIdStr());
        goods.setGoodsName("集成测试商品");
        goods.setOutItemId("OI" + IdUtil.getSnowflakeNextIdStr());
        goods.setItemType("CAR_ITEM");
        goods.setSalePrice(new BigDecimal("1000"));
        goods.setItemValue(new BigDecimal("150000"));
        goods.setReportStatus("2");
        goods.setGoodsStatus("1");
        goodsMapper.insert(goods);

        RentGoodsSku sku = new RentGoodsSku();
        sku.setGoodsId(goods.getId());
        sku.setOutSkuId("OS" + IdUtil.getSnowflakeNextIdStr());
        sku.setSkuName("月租套餐");
        sku.setDurationDays(30);
        sku.setSalePrice(new BigDecimal("1000"));
        sku.setSkuStatus("1");
        skuMapper.insert(sku);

        GoodsPageReq req = new GoodsPageReq();
        req.setPageNo(1);
        req.setPageSize(10);
        GoodsPageReq.Query query = new GoodsPageReq.Query();
        query.setGoodsName("集成测试商品");
        req.setParams(query);

        PageResult<GoodsVo> result = goodsService.page(req);

        assertTrue(result.getTotal() >= 1);
        GoodsVo vo = result.getRecords().stream()
                .filter(g -> goods.getId().toString().equals(g.getId()))
                .findFirst().orElseThrow(AssertionError::new);
        assertEquals("集成测试商品", vo.getGoodsName());
        assertEquals("1000.00", vo.getSalePrice());
        assertEquals(1, vo.getSkuList().size());
        assertEquals("月租套餐", vo.getSkuList().get(0).getSkuName());
        assertEquals("1000.00", vo.getSkuList().get(0).getSalePrice());
    }

    @Test
    void pageShouldFilterByGoodsName() {
        GoodsPageReq req = new GoodsPageReq();
        req.setPageNo(1);
        req.setPageSize(10);
        GoodsPageReq.Query query = new GoodsPageReq.Query();
        query.setGoodsName("不存在的商品名称XYZ");
        req.setParams(query);

        PageResult<GoodsVo> result = goodsService.page(req);
        assertEquals(0, result.getTotal());
    }
}
