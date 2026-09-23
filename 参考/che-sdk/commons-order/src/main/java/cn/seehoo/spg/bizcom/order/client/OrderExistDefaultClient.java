package cn.seehoo.spg.bizcom.order.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.bizcom.order.config.OrderConfiguration;
import cn.seehoo.spg.bizcom.order.model.OrderExistCheckReq;
import cn.seehoo.spg.bizcom.order.model.OrderExistResp;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class OrderExistDefaultClient implements OrderExistClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderExistDefaultClient.class);
    private static final String API_EXIST_ORDER_CHECK = "/adminapi/v1/orders/efficientCheck";
    @Autowired
    private OrderConfiguration orderConfig;
    
    @Override
    public OrderExistResp checkExistOrder(OrderExistCheckReq oecr) throws RuntimeException {
        LOGGER.info(">>>>>> [在途订单判断] 有担在途订单判断，入参：{}", JSON.toJSONString(oecr));
        if (orderConfig.isMock()) {
            LOGGER.info(">>>>>>[在途订单判断]，触发mock, 返回成功");
            OrderExistResp res = new OrderExistResp();
            res.setCustomerFlag(OrderExistResp.NO);
            res.setIsFirstFlag(OrderExistResp.NO);
            res.setVinFlag(OrderExistResp.NO);
            return res;
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String response = HttpUtils.request(orderConfig.getHost() + API_EXIST_ORDER_CHECK, HttpUtils.METHOD_POST,
                headers, JSONUtil.toJsonStr(oecr).getBytes(StandardCharsets.UTF_8), null, "");
        if (StrUtil.isBlank(response)) {
            LOGGER.error(">>>>>> [在途订单判断] 有担在途订单判断 response body 为空");
        }
        JSONObject retData = JSONObject.parseObject(response);
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                OrderExistResp res = JSONUtil.toBean(retData.getString("data"), OrderExistResp.class);
                return res;
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        OrderConfiguration orderConfig = new OrderConfiguration();
//        orderConfig.setHost("https://retail-sit.hxfl.com.cn/baseUrl/order");
//        OrderExistDefaultClient client = new OrderExistDefaultClient();
//        client.orderConfig = orderConfig;
//        OrderExistCheckReq req = new OrderExistCheckReq();
//        req.setCustomerName("王小二");
//        req.setCustomerIdcard("13020219970304141X");
//        req.setVehicleTypeCode("2");
//        req.setVehicleVim("JTHBYLFF1N5022967");
//        OrderExistResp res = client.checkExistOrder(req);
//        System.out.println(JSON.toJSONString(res));
//    }
}
