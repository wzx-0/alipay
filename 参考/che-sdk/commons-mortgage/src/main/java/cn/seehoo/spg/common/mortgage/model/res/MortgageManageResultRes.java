package cn.seehoo.spg.common.mortgage.model.res;

import lombok.Data;

@Data
public class MortgageManageResultRes {
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
