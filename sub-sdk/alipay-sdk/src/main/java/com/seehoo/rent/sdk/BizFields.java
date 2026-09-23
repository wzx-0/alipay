package com.seehoo.rent.sdk;

/**
 * 支付宝响应报文字段名常量（snake_case，用于从响应Map中取值）
 * <p>请求侧字段名已由 model 包请求DTO + SNAKE_CASE序列化策略承担，不再需要手写字段名</p>
 */
public final class BizFields {

    // ---------------- 订单响应 ----------------
    /** 支付宝订单ID */
    public static final String ORDER_ID = "order_id";
    /** 订单详情页路径 */
    public static final String PATH = "path";

    // ---------------- 签约响应 ----------------
    /** 签约串（前端唤起受理台） */
    public static final String SIGN_STR = "sign_str";
    /** 受理台唤起方式 */
    public static final String SIGN_LAUNCH_METHOD = "sign_launch_method";

    // ---------------- 风控响应 ----------------
    /** 综合风险等级T1~T10 */
    public static final String RISK_LEVEL = "risk_level";

    // ---------------- 支付响应/通知 ----------------
    /** 支付宝交易号 */
    public static final String TRADE_NO = "trade_no";
    /** 交易状态（TRADE_SUCCESS/TRADE_FINISHED等） */
    public static final String TRADE_STATUS = "trade_status";

    private BizFields() {
    }
}
