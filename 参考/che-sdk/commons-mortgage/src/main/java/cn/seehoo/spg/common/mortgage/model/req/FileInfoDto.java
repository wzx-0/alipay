package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

/**
 * @author zhaogq
 * @date 2026/4/16 17:32
 */
@Data
public class FileInfoDto {

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
