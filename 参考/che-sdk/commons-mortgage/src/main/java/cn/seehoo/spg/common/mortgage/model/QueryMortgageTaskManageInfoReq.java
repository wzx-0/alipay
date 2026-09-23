package cn.seehoo.spg.common.mortgage.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 抵押结果回传
 */
@Getter
@Setter
@ToString
public class QueryMortgageTaskManageInfoReq {
    /**
     * id
     */
    private String id;

    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 车架号
     */
    private String carVin;

    /**
     * 订单类型 办押: PD_BANYA , 解押: PD_JIEYA
     */
    private String orderType;

    /**
     * 订单编号集合
     */
    private List<String> orderNos;

    /**
     * 要的抵押状态
     */
    private List<String> inStatus;

    /**
     * 不要的抵押状态
     */
    private List<String> notInStatus;

    /**
     * 办理进度同步 bizId
     */
    private List<String> bizIds;
}
