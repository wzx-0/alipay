package cn.seehoo.spg.common.opl.model.dto.faf;

import lombok.Data;
import java.util.List;

/**
 * @author tangying
 * @version 1.0
 * @date 2026/8/28 10:02
 * @since 1.0
 */
@Data
public class JointLeaseSignResultDTO {
    /**
     * 业务编号
     */
    private String bizId;

    private List<JointLeaseSignResultFileDTO> fileList;

    @Data
    public static class JointLeaseSignResultFileDTO {
        /**
         * 小类编码,实际传输值: {小类编码}_C
         */
        private String subCategoryCode;

        /**
         * 文件标识ID
         */
        private String fileId;

    }

}
