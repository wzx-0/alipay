package com.seehoo.rent.sdk.model;

import lombok.Data;

import java.util.List;

/**
 * alipay.commerce.rent.order.create 创单请求
 * <p>RENT新租须传itemInfos/priceInfo/rentPlanInfo/rentSignInfo/deliveryInfo；
 * RELET续订/BUYOUT买断须传originOrderId及对应reletInfo/buyoutInfo</p>
 */
@Data
public class OrderCreateRequest {

    /** 业务身份（SDK按协议固定值CAR_SUBSCRIPTION自动填充，无需设置） */
    private String bizIdentity;
    /** 订单类型 */
    private OrderType orderType;
    /** 商家侧订单ID（全局唯一） */
    private String outOrderId;

    /** 买家openId */
    private String openId;

    /** 买家userId（openId与userId二选一） */
    private String userId;

    /** 交易应用APPID（异主体收单时传，与app_id一致可不传） */
    private String tradeAppId;

    /** 外部业务号（商家自定义溯源） */
    private String sourceId;

    /** 商品明细列表 */
    private List<ItemInfo> itemInfos;

    /** 价格信息 */
    private PriceInfo priceInfo;

    /** 订阅计划 */
    private RentPlanInfo rentPlanInfo;

    /** 签约要素（芝麻免押+代扣） */
    private RentSignInfo rentSignInfo;

    /** 配送信息 */
    private DeliveryInfo deliveryInfo;

    /** 续订溯源（orderType=RELET时必传） */
    private ReletInfo reletInfo;

    /** 买断溯源（orderType=BUYOUT时必传） */
    private BuyoutInfo buyoutInfo;

    /** 订单类型枚举 */
    public enum OrderType {

        /** 新租 */
        RENT,
        /** 续订 */
        RELET,
        /** 买断 */
        BUYOUT
    }

    /** 商品明细 */
    @Data
    public static class ItemInfo {

        /** 商品类型：汽车订阅固定CAR_ITEM */
        private String itemType;

        /** 商家侧商品ID（商品库提报后的out_item_id） */
        private String outItemId;

        /** 商家侧SKU ID（未分SKU可不传） */
        private String outSkuId;

        /** 商品名称 */
        private String itemName;

        /** 商品描述 */
        private String itemDescription;

        /** 商品售价（元，2位小数） */
        private String salePrice;

        /** 商品价值（元，押金评估基准） */
        private String itemValue;

        /** 商品数量 */
        private Integer itemCnt;
    }

    /** 价格信息 */
    @Data
    public static class PriceInfo {

        /** 订单总价（元，=运费+增值服务费+Σ期次金额） */
        private String orderPrice;

        /** 押金金额（元，免押模式下为信用评估参考） */
        private String depositPrice;

        /** 运费（元，选填） */
        private String freight;

        /** 增值服务费（元，选填） */
        private String additionalPrice;
    }

    /** 订阅计划 */
    @Data
    public static class RentPlanInfo {

        /** 租期开始时间（yyyy-MM-dd HH:mm:ss） */
        private String rentStartTime;

        /** 租期结束时间 */
        private String rentEndTime;

        /** 买断价（元，选填） */
        private String buyoutPrice;

        /** 期次列表 */
        private List<Installment> installments;
    }

    /** 订阅期次 */
    @Data
    public static class Installment {

        /** 期次号（从1开始） */
        private Integer installmentNo;

        /** 本期金额（元） */
        private String installmentPrice;

        /** 计划支付时间（yyyy-MM-dd HH:mm:ss） */
        private String planPayTime;

        /** 本期买断价（元，选填） */
        private String buyoutPrice;
    }

    /** 配送信息 */
    @Data
    public static class DeliveryInfo {

        /** 配送方式 */
        private DeliveryType deliveryType;

        /** 门店信息（自提时建议传） */
        private ShopInfo shopInfo;

        /** 收货人信息 */
        private ReceiverInfo receiverInfo;
    }

    /** 配送方式枚举 */
    public enum DeliveryType {

        /** 用户自提 */
        SELFPICK
    }

    /** 门店信息 */
    @Data
    public static class ShopInfo {

        /** 门店名称 */
        private String shopName;

        /** 门店地址 */
        private String shopAddress;

        /** 门店电话 */
        private String shopTel;
    }

    /** 收货人信息 */
    @Data
    public static class ReceiverInfo {

        /** 收货人姓名 */
        private String receiverName;

        /** 收货人电话 */
        private String receiverTel;

        /** 详细地址 */
        private String detailedAddress;
    }

    /** 续订溯源 */
    @Data
    public static class ReletInfo {

        /** 原订单ID（商家侧） */
        private String originOrderId;
    }

    /** 买断溯源 */
    @Data
    public static class BuyoutInfo {

        /** 原订单ID（商家侧） */
        private String originOrderId;
    }
}
