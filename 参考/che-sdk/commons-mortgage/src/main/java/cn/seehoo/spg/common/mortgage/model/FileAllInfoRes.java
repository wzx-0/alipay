package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;

/**
 * 所有文件信息dto
 */
@Data
public class FileAllInfoRes {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 订单号
     */
    private String bussinessNo;

    /**
     * 附件小类编码
     */
    private String subCategoryCode;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件id
     */
    private String fileId;

    /**
     * 附件大小（单位：字节）
     */
    private Integer fileSize;

    /**
     * 文件下载路径
     */
    private String filePath;

}
