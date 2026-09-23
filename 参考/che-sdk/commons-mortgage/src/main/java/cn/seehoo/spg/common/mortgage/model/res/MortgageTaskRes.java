package cn.seehoo.spg.common.mortgage.model.res;

import lombok.Data;

/**
 * 抵押任务下发出参
 * @author zhangxx
 * @date 2026/3/26 15:21
 */
@Data
public class MortgageTaskRes {
    private static final long serialVersionUID = 1L;

    /**
     * 抵押任务Id
     */
    private String mortgageId;

    /**
     * 签约链接
     */
    private String signUrl;

    /**
     * 代理人签约链接
     */
    private String dlrSignUrl;
}
