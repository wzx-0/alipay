package cn.seehoo.spg.common.signpl.model;

import java.util.List;

/**
 * @author caofei
 * @desc 合同下载响应结果报文
 * @time 2025/9/25 17:35。
 */
public class DownloadContractResDTO {
    /**
     * 合同文件类型
     * 1-合同(已签署)
     * 2-存证
     * 3-双录视频
     * 4-半身照
     */
    private String fileType;
    /**
     * 文件列表
     */
    private List<FileItem> files;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public void setFiles(List<FileItem> files) {
        this.files = files;
    }

    /**
     * 文件列表项内部类
     * 表示单个文件的详细信息
     */
    public static class FileItem {
        /**
         * 文件标题
         */
        private String fileTitle;

        /**
         * 创建签约时的文件唯一标识
         */
        private String origFileKey;

        /**
         * 签署人Id
         */
        private String signerId;

        /**
         * 文件唯一标识
         */
        private String fileKey;

        /**
         * 附件下载链接
         */
        private String link;
        /** 业务自定义KEY */
        private String customId;

        public String getFileTitle() {
            return fileTitle;
        }

        public void setFileTitle(String fileTitle) {
            this.fileTitle = fileTitle;
        }

        public String getOrigFileKey() {
            return origFileKey;
        }

        public void setOrigFileKey(String origFileKey) {
            this.origFileKey = origFileKey;
        }

        public String getSignerId() {
            return signerId;
        }

        public void setSignerId(String signerId) {
            this.signerId = signerId;
        }

        public String getFileKey() {
            return fileKey;
        }

        public void setFileKey(String fileKey) {
            this.fileKey = fileKey;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

		public String getCustomId() {
			return customId;
		}

		public void setCustomId(String customId) {
			this.customId = customId;
		}
    }

}
