package com.seehoo.rent.app.dto;

import lombok.Data;

import java.util.List;

/** A0101001 商品分页查询单条数据 */
@Data
public class GoodsVo {

    /** 商品ID */
    private String id;
    /** 商品编码 */
    private String goodsCode;
    /** 商品名称 */
    private String goodsName;
    /** 支付宝商品库ID */
    private String outItemId;
    /** 商品售价(元) */
    private String salePrice;
    /** 商品价值(元) */
    private String itemValue;
    /** 标准租期(天) */
    private Integer durationDays;
    /** 提报状态：0-未提报 1-提报中 2-已提报 3-提报失败 */
    private String reportStatus;
    /** 商品状态：0-下架 1-上架 */
    private String goodsStatus;
    /** SKU列表 */
    private List<SkuVo> skuList;

    /** SKU数据 */
    @Data
    public static class SkuVo {
        /** SKU ID */
        private String id;
        /** 支付宝商品库SKU ID */
        private String outSkuId;
        /** SKU名称 */
        private String skuName;
        /** 租期时长(天) */
        private Integer durationDays;
        /** SKU售价(元) */
        private String salePrice;
        /** SKU状态：0-下架 1-上架 */
        private String skuStatus;
    }
}
