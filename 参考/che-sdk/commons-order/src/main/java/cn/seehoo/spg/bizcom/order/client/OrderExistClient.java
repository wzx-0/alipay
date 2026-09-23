package cn.seehoo.spg.bizcom.order.client;

import cn.seehoo.spg.bizcom.order.model.OrderExistCheckReq;
import cn.seehoo.spg.bizcom.order.model.OrderExistResp;

public interface OrderExistClient {

    /**
     * 在途订单校验接口
     * @param oecr 
     * @return
     * @throws RuntimeException
     */
    public OrderExistResp checkExistOrder(OrderExistCheckReq oecr) throws RuntimeException;
}
