package cn.seehoo.spg.common.risk.model;

import lombok.Data;

import java.util.List;

@Data
public class CreditCallbackReq {

    /**
     * 流水号
     */
    private String businessId;

    /**
     * 风控报告地址
     */
    private List<String> url;

}
