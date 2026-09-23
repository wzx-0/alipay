package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 华夏订单附件推送
 *
 */
@Data
@ToString
public class PushOrderFileReq {

    /**
     * 渠道编号
     */
    private String channelId;
    /**
     * 业务编号
     */
    private String bizNo;
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 抵押合同任务编号
     */
    private String mortgageContractNo;
    /**
     * 附件列表
     */
    private List<FileInfoDto> files;


    @Data
    public static class FileInfoDto {

        /**
         * 附件名称
         */
        private String fileName;
        /**
         * 附件类型(附件小类)
         */
        private String fileType;
        /**
         * 附件地址
         */
        private String fileUrl;
    }
}
