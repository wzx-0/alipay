package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 租赁商品SKU表：订阅套餐与支付宝商品库(out_sku_id)映射 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_goods_sku")
public class RentGoodsSku extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 商品ID，关联tb_rent_goods.id */
    private Long goodsId;
    /** 支付宝商品库SKU ID */
    private String outSkuId;
    /** SKU规格名称（订阅套餐描述） */
    private String skuName;
    /** 租期时长(天) */
    private Integer durationDays;
    /** SKU售价(元) */
    private BigDecimal salePrice;
    /** D10013状态：0-下架 1-上架 */
    private String skuStatus;
}
