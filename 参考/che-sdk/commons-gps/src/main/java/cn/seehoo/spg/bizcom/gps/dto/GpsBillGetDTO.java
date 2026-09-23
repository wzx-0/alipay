package cn.seehoo.spg.bizcom.gps.dto;

public class GpsBillGetDTO {
    /** 申请编号 */
    private String AppCode;
    /** 申请编号 */
    private String type;

    public String getAppCode() {
        return AppCode;
    }
    public void setAppCode(String appCode) {
        AppCode = appCode;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public GpsBillGetDTO() {
    }

    public GpsBillGetDTO(String appCode, String type) {
        AppCode = appCode;
        this.type = type;
    }
}
