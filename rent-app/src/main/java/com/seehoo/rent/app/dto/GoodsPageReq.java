package com.seehoo.rent.app.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/** A0101001 商品分页查询入参 */
@Data
public class GoodsPageReq {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNo;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private Integer pageSize;

    @Valid
    @NotNull(message = "查询条件不能为空")
    private Query params;

    /** 查询条件（均非必填） */
    @Data
    public static class Query {
        /** 商品名称，模糊匹配 */
        private String goodsName;
        /** 商品状态：0-下架 1-上架 */
        private String goodsStatus;
        /** 提报状态：0-未提报 1-提报中 2-已提报 3-提报失败 */
        private String reportStatus;
    }
}
