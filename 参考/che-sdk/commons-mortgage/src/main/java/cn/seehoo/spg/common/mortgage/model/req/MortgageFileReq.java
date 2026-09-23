package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import java.util.List;

/**
 * 附件查询入参
 * @author zhangxx
 * @date 2026/3/26 15:14
 */
@Data
public class MortgageFileReq {
    /**
     * 业务主键集合
     */
    private List<String> bussinessNoList;
    /**
     * 附件小类集合
     */
    private List<String> subCategoryCodeList;
}
