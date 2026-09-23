package cn.seehoo.spg.common.opl.model.dto.faf;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @date 2026/8/21 14:27
 * @since 1.0
 */
@Data
public class JointLeaseDocDTO {
    /**
     * 订单编号
     */
    private String hxOrderNo;

    /**
     * 审核状态 01 通过  03 退回
     */
    private String reviewStatus;
    /**
     * 备注
     */
    private String remark;

    List<DocTypeList> docTypeList;


    @Data
    private class DocTypeList {
        private String fileCode;
        private String fileMsg;
    }


}
