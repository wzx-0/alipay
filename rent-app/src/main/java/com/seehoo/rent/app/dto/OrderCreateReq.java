package com.seehoo.rent.app.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/** A0102001 创建租赁订单入参（小程序插件下单回调/私域确认订单页） */
@Data
public class OrderCreateReq {

    /** 订单类型：RENT/RELET/BUYOUT */
    @NotBlank(message = "订单类型不能为空")
    private String orderType;

    /** 前端my.checkBeforeAddOrder返回的sourceId */
    @NotBlank(message = "sourceId不能为空")
    private String sourceId;

    /** 买家openId */
    @NotBlank(message = "buyerOpenId不能为空")
    private String buyerOpenId;

    /** 商品ID，orderType=RENT时必填 */
    private String goodsId;

    /** SKU ID，orderType=RENT时必填 */
    private String skuId;

    /** 购买数量，orderType=RENT时必填 */
    private Integer quantity;

    /** 原订阅订单号，RELET/BUYOUT时必填 */
    private String originOrderId;

    /** 押金金额(元)，RENT时必填，芝麻免押预授权冻结金额 */
    private String depositPrice;

    /** 运费(元) */
    private String freight;

    /** 增值服务费(元) */
    private String additionalPrice;

    /** 订阅计划，RENT时必填 */
    @Valid
    private RentPlanInfo rentPlanInfo;

    /** 自提门店信息 */
    private ShopInfo shopInfo;

    /** 收货人信息 */
    private ReceiverInfo receiverInfo;

    /** 商家透传数据，原样透传支付宝 */
    private Object merchantExtInfo;

    /** 订阅计划：租期与期次计划 */
    @Data
    public static class RentPlanInfo {
        /** 租赁开始时间 yyyy-MM-dd HH:mm:ss */
        @NotBlank(message = "租赁开始时间不能为空")
        private String rentStartTime;
        /** 租赁结束时间 yyyy-MM-dd HH:mm:ss */
        @NotBlank(message = "租赁结束时间不能为空")
        private String rentEndTime;
        /** 期次计划，最长36期 */
        @Valid
        @NotNull(message = "期次计划不能为空")
        private List<Installment> installments;
    }

    /** 期次计划 */
    @Data
    public static class Installment {
        /** 期号，从1开始 */
        @NotNull(message = "期号不能为空")
        private Integer installmentNo;
        /** 当期订阅金额(元) */
        @NotBlank(message = "当期订阅金额不能为空")
        private String installmentPrice;
        /** 计划扣款时间 yyyy-MM-dd HH:mm:ss */
        @NotBlank(message = "计划扣款时间不能为空")
        private String planPayTime;
        /** 到期购买金额(元)，仅最后一期 */
        private String buyoutPrice;
    }

    /** 自提门店信息 */
    @Data
    public static class ShopInfo {
        private String shopName;
        private String shopAddress;
        private String shopTel;
    }

    /** 收货人信息 */
    @Data
    public static class ReceiverInfo {
        private String receiverName;
        private String receiverTel;
        private String detailedAddress;
    }
}
