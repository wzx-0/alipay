package cn.seehoo.spg.common.ecif.model;

/**
 * 营业执照请求躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class CompanyCertPhotoReq {

    /**
     * 营业执照主键ID
     */
    private String id;

    private String fileDisplayName;

    private String fileFullPath;

    private String s3FullPath;

    private String fileDownloadUrl;

    private String fileSize;

    private String fileType;

    private String fileExtension;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public void setFileDisplayName(String fileDisplayName) {
        this.fileDisplayName = fileDisplayName;
    }

    public String getFileFullPath() {
        return fileFullPath;
    }

    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getS3FullPath() {
        return s3FullPath;
    }

    public void setS3FullPath(String s3FullPath) {
        this.s3FullPath = s3FullPath;
    }

}
