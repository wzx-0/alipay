package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 租赁商品表：维护本地商品与支付宝商品库(out_item_id)映射及提报状态 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_goods")
public class RentGoods extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 商品编码，商家侧唯一 */
    private String goodsCode;
    /** 商品名称 */
    private String goodsName;
    /** 支付宝商品库商品ID */
    private String outItemId;
    /** 商品类型，汽车订阅固定CAR_ITEM */
    private String itemType;
    /** 商品售价(元) */
    private BigDecimal salePrice;
    /** 商品价值(元) */
    private BigDecimal itemValue;
    /** 标准租期(天) */
    private Integer durationDays;
    /** 提报素材附件ID，多个逗号分隔 */
    private String fileIds;
    /** D10012提报状态：0-未提报 1-提报中 2-已提报 3-提报失败 */
    private String reportStatus;
    /** D10013商品状态：0-下架 1-上架 */
    private String goodsStatus;
}
