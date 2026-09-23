package com.seehoo.rent.app.controller;

import com.seehoo.rent.app.common.PageResult;
import com.seehoo.rent.app.common.Result;
import com.seehoo.rent.app.dto.GoodsPageReq;
import com.seehoo.rent.app.dto.GoodsVo;
import com.seehoo.rent.app.service.GoodsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/** 商品接口 */
@Validated
@RestController
@RequestMapping("/api/v1/rent/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /** A0101001 商品分页查询 */
    @PostMapping("/page")
    public Result<PageResult<GoodsVo>> page(@Valid @RequestBody GoodsPageReq req) {
        return Result.success(goodsService.page(req));
    }
}
