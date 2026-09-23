package com.seehoo.rent.app.service;

import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.dto.GoodsPageReq;
import com.seehoo.rent.app.dto.GoodsVo;

/** 商品服务 */
public interface GoodsService {

    /** 商品分页查询（含SKU与提报状态） */
    PageResult<GoodsVo> page(GoodsPageReq req);
}
