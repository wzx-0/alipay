package com.seehoo.rent.app.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/** A0105001 订单支付入参（代扣/预授权转支付/JSAPI主动支付） */
@Data
public class PayReq {

    @NotBlank(message = "商家侧订单号不能为空")
    private String outOrderId;

    /** RENT_DEDUCT/PRE_AUTH/JSAPI */
    @NotBlank(message = "支付方式不能为空")
    private String payMethod;

    /** 费项明细 */
    @Valid
    @NotEmpty(message = "费项明细不能为空")
    private List<PayItem> payItems;

    /** 支付超时时间（如30m），JSAPI场景有效 */
    private String payTimeoutExpress;

    /** 费项 */
    @Data
    public static class PayItem {
        /** 费项类型：RENT-订阅金 INDEMNITY-赔付违约金 */
        @NotBlank(message = "费项类型不能为空")
        private String feeType;
        /** 期号，feeType=RENT时必填 */
        private Integer installmentNo;
        /** 费项金额(元) */
        @NotBlank(message = "费项金额不能为空")
        private String payAmount;
    }
}
