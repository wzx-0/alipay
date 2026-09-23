package cn.seehoo.spg.common.risk.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseReq {

    /**
     * 订单号
     */
    private String orderId;

}
