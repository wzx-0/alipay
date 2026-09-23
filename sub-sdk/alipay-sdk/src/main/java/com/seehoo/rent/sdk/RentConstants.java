package com.seehoo.rent.sdk;

/**
 * 汽车订阅协议常量：接口方法名与协议级固定值
 * <p>业务状态/操作类型等可枚举值已迁至 model 包各请求DTO的嵌套枚举（如 RentOrderStatus）；
 * 值来源：《汽车订阅解决方案-茂电》官方文档，与最新官方文档不符时以官方为准</p>
 */
public final class RentConstants {

    // ---------------- 接口方法名 ----------------

    /** 租赁订单创建 */
    public static final String METHOD_ORDER_CREATE = "alipay.commerce.rent.order.create";
    /** 租赁订单查询 */
    public static final String METHOD_ORDER_QUERY = "alipay.commerce.rent.order.query";
    /** 租赁订单签约（唤起受理台补偿） */
    public static final String METHOD_ORDER_SIGN = "alipay.commerce.rent.order.sign";
    /** 租赁风险咨询 */
    public static final String METHOD_RISK_CONSULT = "alipay.commerce.rent.risk.consult";
    /** 履约审核通过 */
    public static final String METHOD_FULFILLMENT_APPROVE = "alipay.commerce.rent.order.fulfillment.approve";
    /** 履约发货/用户寄回同步 */
    public static final String METHOD_FULFILLMENT_SEND = "alipay.commerce.rent.order.fulfillment.send";
    /** 履约确认收货 */
    public static final String METHOD_FULFILLMENT_RECEIVE = "alipay.commerce.rent.order.fulfillment.receive";
    /** 履约归还完结 */
    public static final String METHOD_FULFILLMENT_FINISH = "alipay.commerce.rent.order.fulfillment.finish";
    /** 订单支付（代扣/预授权转支付/JSAPI） */
    public static final String METHOD_ORDER_PAY = "alipay.commerce.rent.order.pay";
    /** 端外支付同步 */
    public static final String METHOD_ORDER_PAY_SYNC = "alipay.commerce.rent.order.pay.sync";
    /** 修改租赁起止时间 */
    public static final String METHOD_ORDER_MODIFY = "alipay.commerce.rent.order.modify";
    /** 订单关闭 */
    public static final String METHOD_ORDER_CLOSE = "alipay.commerce.rent.order.close";
    /** 统一收单退款 */
    public static final String METHOD_TRADE_REFUND = "alipay.trade.refund";
    /** 售后单创建 */
    public static final String METHOD_AFTERSALE_CREATE = "alipay.commerce.rent.order.aftersale.create";
    /** 售后单商户处理 */
    public static final String METHOD_AFTERSALE_CONFIRM = "alipay.commerce.rent.order.aftersale.confirm";

    // ---------------- 协议级固定值 ----------------

    /** 业务身份，汽车订阅固定值（需白名单），由orderCreate自动填充 */
    public static final String BIZ_IDENTITY = "CAR_SUBSCRIPTION";
    /** 商品类型，汽车订阅固定值 */
    public static final String ITEM_TYPE_CAR = "CAR_ITEM";

    private RentConstants() {
    }
}
