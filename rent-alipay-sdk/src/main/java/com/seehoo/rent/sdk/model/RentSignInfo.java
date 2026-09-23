package com.seehoo.rent.sdk.model;

import lombok.Data;

/**
 * 签约要素：芝麻免押预授权 + 周期代扣（order.create的rent_sign_info / order.sign复用）
 */
@Data
public class RentSignInfo {

    /** 芝麻信用信息 */
    private CreditInfo creditInfo;

    /** 周期代扣签约信息 */
    private RentDeductInfo rentDeductInfo;

    /** 预授权冻结结果异步通知地址（仅order.create使用） */
    private String freezeNotifyUrl;

    /** 芝麻信用服务实现 */
    @Data
    public static class CreditInfo {

        /** 芝麻服务ID（由支付宝BD提供） */
        private String zmServiceId;

        /** 品类ID（由支付宝BD提供） */
        private String categoryId;
    }

    /** 周期代扣签约实现 */
    @Data
    public static class RentDeductInfo {

        /** 签约场景：租赁扣款固定RENT_DEDUCT */
        private SignScene signScene;

        /** 预授权类目：私域DEPOSIT_CAR_LEASING_PRI/公域DEPOSIT_CAR_LEASING_PUB（TODO联调确认） */
        private String authCategory;
    }

    /** 代扣签约场景枚举 */
    public enum SignScene {

        /** 租赁周期扣款 */
        RENT_DEDUCT
    }
}
