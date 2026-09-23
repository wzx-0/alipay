package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.Data;

/**
 * 鑫快链发起流程 - 请求实体类
 * 接口：鑫快链发起流程
 * 请求方式：HTTPS/POST
 */
@Data
public class StartRequest {

    public static final String START_URL = "/api/credit/v1/launch";

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户证件号
     */
    private String customerCardNo;

    /**
     * 客户手机号
     */
    private String customerPhone;

    /**
     * 华夏订单号
     */
    private String orderNo;

    /**
     * 鑫快链分配的flowId（华夏侧订单/客户ID，易鑫SAAS进件订单ID）
     */
    private String sysId;

    /**
     * 鑫快链流程类型固定值
     */
    private String processType;

    /**
     * flowId（这个客户之前的流程，传过后会把这个流程置为无效）
     */
    private String processId;
}
