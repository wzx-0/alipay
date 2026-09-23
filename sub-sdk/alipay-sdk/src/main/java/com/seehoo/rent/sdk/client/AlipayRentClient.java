package com.seehoo.rent.sdk.client;

import com.seehoo.rent.sdk.RentResponse;
import com.seehoo.rent.sdk.model.AftersaleConfirmRequest;
import com.seehoo.rent.sdk.model.AftersaleCreateRequest;
import com.seehoo.rent.sdk.model.FulfillmentApproveRequest;
import com.seehoo.rent.sdk.model.FulfillmentFinishRequest;
import com.seehoo.rent.sdk.model.FulfillmentReceiveRequest;
import com.seehoo.rent.sdk.model.FulfillmentSendRequest;
import com.seehoo.rent.sdk.model.OrderCloseRequest;
import com.seehoo.rent.sdk.model.OrderCreateRequest;
import com.seehoo.rent.sdk.model.OrderModifyRequest;
import com.seehoo.rent.sdk.model.OrderPayRequest;
import com.seehoo.rent.sdk.model.OrderPaySyncRequest;
import com.seehoo.rent.sdk.model.OrderQueryRequest;
import com.seehoo.rent.sdk.model.OrderSignRequest;
import com.seehoo.rent.sdk.model.RiskConsultRequest;
import com.seehoo.rent.sdk.model.TradeRefundRequest;

import java.util.Map;

/**
 * 支付宝汽车订阅服务端接口客户端
 * <p>业务方法与支付宝 openapi 一一对应，入参为 model 包下的强类型请求对象</p>
 */
public interface AlipayRentClient {

    /**
     * 网关通用调用：组装公参、签名、POST、解析响应节点
     *
     * @param method     接口方法名，如 alipay.commerce.rent.order.create
     * @param bizContent 业务参数对象（SDK以SNAKE_CASE序列化为JSON），null表示无业务参数
     * @param notifyUrl  异步通知地址，无则传null
     */
    RentResponse<Map<String, Object>> execute(String method, Object bizContent, String notifyUrl);

    /** alipay.commerce.rent.order.create 租赁订单创建（bizIdentity由SDK按协议固定值填充） */
    RentResponse<Map<String, Object>> orderCreate(OrderCreateRequest request);

    /** alipay.commerce.rent.order.query 租赁订单查询 */
    RentResponse<Map<String, Object>> orderQuery(OrderQueryRequest request);

    /** alipay.commerce.rent.order.sign 租赁订单签约：返回sign_str供前端唤起受理台 */
    RentResponse<Map<String, Object>> orderSign(OrderSignRequest request);

    /** alipay.commerce.rent.risk.consult 租赁风险咨询：consultRiskTypes为空时默认查综合风险 */
    RentResponse<Map<String, Object>> riskConsult(RiskConsultRequest request);

    /** alipay.commerce.rent.order.fulfillment.approve 商家审核通过 */
    RentResponse<Map<String, Object>> fulfillmentApprove(FulfillmentApproveRequest request);

    /** alipay.commerce.rent.order.fulfillment.send 发货/用户寄回 */
    RentResponse<Map<String, Object>> fulfillmentSend(FulfillmentSendRequest request);

    /** alipay.commerce.rent.order.fulfillment.receive 确认收货 */
    RentResponse<Map<String, Object>> fulfillmentReceive(FulfillmentReceiveRequest request);

    /** alipay.commerce.rent.order.fulfillment.finish 订单完结（自动解冻+解除代扣） */
    RentResponse<Map<String, Object>> fulfillmentFinish(FulfillmentFinishRequest request);

    /** alipay.commerce.rent.order.pay 订单支付：JSAPI返回trade_no供前端my.tradePay */
    RentResponse<Map<String, Object>> orderPay(OrderPayRequest request);

    /** alipay.commerce.rent.order.pay.sync 端外支付同步：payChannel固定OTHER */
    RentResponse<Map<String, Object>> orderPaySync(OrderPaySyncRequest request);

    /** alipay.commerce.rent.order.modify 修改租期（type=RENT_PLAN_TIME） */
    RentResponse<Map<String, Object>> orderModify(OrderModifyRequest request);

    /** alipay.commerce.rent.order.close 订单关闭（确认收货前；已支付费项须先全额退款） */
    RentResponse<Map<String, Object>> orderClose(OrderCloseRequest request);

    /** alipay.trade.refund 统一收单退款（取消订单退已支付费项） */
    RentResponse<Map<String, Object>> tradeRefund(TradeRefundRequest request);

    /** alipay.commerce.rent.order.aftersale.create 售后单创建 */
    RentResponse<Map<String, Object>> aftersaleCreate(AftersaleCreateRequest request);

    /** alipay.commerce.rent.order.aftersale.confirm 售后处理 */
    RentResponse<Map<String, Object>> aftersaleConfirm(AftersaleConfirmRequest request);
}
