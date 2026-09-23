package com.seehoo.rent.sdk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * alipay.commerce.rent.order.pay.sync 端外支付同步请求
 * <p>商家自有渠道收款后向支付宝同步流水，payChannel固定OTHER</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPaySyncRequest extends BaseRentOrderRequest {

    /** 支付渠道 */
    private PayChannel payChannel;

    /** 同步费项列表 */
    private List<PayItem> payItems;

    /** 支付渠道枚举 */
    public enum PayChannel {

        /** 其他渠道（端外自有渠道） */
        OTHER
    }
}
