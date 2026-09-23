package cn.seehoo.spg.common.mortgage.model.res;

import lombok.Data;

/**
 * 抵押信息修改出参
 * @author zhangxx
 * @date 2026/3/26 15:08
 */
@Data
public class MortgageTaskUpdateRes {
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
