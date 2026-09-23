package com.seehoo.rent.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/** A0102003 订单详情返回：订单主体+商品明细+订阅计划期次+签约信息 */
@Data
public class OrderDetailVo {

    /** 订单主信息 */
    private OrderInfoVo order;
    /** 商品明细 */
    private List<ItemVo> items;
    /** 订阅计划期次，RENT订单返回 */
    private List<InstallmentVo> installments;
    /** 签约信息，未签约时为空 */
    private SignVo sign;

    /** 商品明细 */
    @Data
    public static class ItemVo {
        private String id;
        /** 商品类型，固定CAR_ITEM */
        private String itemType;
        private String outItemId;
        private String outSkuId;
        private String itemName;
        private String itemDescription;
        private String salePrice;
        private String itemValue;
        private Integer itemCnt;
    }

    /** 订阅计划期次 */
    @Data
    public static class InstallmentVo {
        private String id;
        private Integer installmentNo;
        private String installmentPrice;
        private LocalDateTime planPayTime;
        /** 到期购买金额(元)，仅最后一期 */
        private String buyoutPrice;
        /** 账单状态：0-待支付 1-支付中 2-已支付 3-支付失败 4-已关闭 */
        private String billStatus;
        private String paidAmount;
        private LocalDateTime actualPayTime;
    }

    /** 签约信息 */
    @Data
    public static class SignVo {
        /** 签约状态：0-未签约 1-已签约 2-已解约 */
        private String signStatus;
        /** 预授权冻结状态：0-未冻结 1-已冻结 2-已解冻 3-已转支付 */
        private String freezeStatus;
        private String freezeAmount;
        /** 代扣协议状态：0-未签约 1-已签约 2-已解除 */
        private String deductStatus;
        private LocalDateTime signTime;
        private LocalDateTime freezeTime;
        private LocalDateTime unfreezeTime;
    }
}
