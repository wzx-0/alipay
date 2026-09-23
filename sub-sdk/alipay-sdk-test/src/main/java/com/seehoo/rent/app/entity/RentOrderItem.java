package com.seehoo.rent.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seehoo.rent.app.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 订单商品明细表：创单时商品快照(item_infos)，汽车订阅限定1项 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_rent_order_item")
public class RentOrderItem extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /** 订单ID，关联tb_rent_order.id */
    private Long orderId;
    /** 商品类型，固定CAR_ITEM */
    private String itemType;
    /** 商家侧商品ID */
    private String outItemId;
    /** 商家侧SKU ID */
    private String outSkuId;
    /** 商品名称（创单快照） */
    private String itemName;
    /** 商品描述 */
    private String itemDescription;
    /** 商品售价(元) */
    private BigDecimal salePrice;
    /** 商品价值(元) */
    private BigDecimal itemValue;
    /** 购买数量 */
    private Integer itemCnt;
}
