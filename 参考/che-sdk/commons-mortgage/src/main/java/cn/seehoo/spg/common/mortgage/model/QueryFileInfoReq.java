package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryFileInfoReq {

    /**
     * 业务主键集合
     */
    private List<String> bussinessNoList;
    /**
     * 附件小类集合
     */
    private List<String> subCategoryCodeList;
}
