package cn.seehoo.spg.common.pboc.model;

/**
 * 征信报告查询 请求类
 * @author HeCG
 * @date 2025/9/26 下午6:00
 * @since 1.0
 */
public class CreditReportReq {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 流水ID
     */
    private String flowId;

    /**
     * 1-xml 2-html 默认格式 3-html和xml 4-pdf 5-json 6-pdf和xml
     */
    private String reporttype = "4";

    /**
     * 来源系统
     * 默认 3 - 车辆零售融资租赁系统
     */
    private String systemresource = "3";

    /**
     * 业务场景
     */
    private String systemsituation = "13";

    private String businessRequestId;
    private String reportno;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getReporttype() {
        return reporttype;
    }

    public void setReporttype(String reporttype) {
        this.reporttype = reporttype;
    }

    public String getSystemresource() {
        return systemresource;
    }

    public void setSystemresource(String systemresource) {
        this.systemresource = systemresource;
    }

    public String getSystemsituation() {
        return systemsituation;
    }

    public void setSystemsituation(String systemsituation) {
        this.systemsituation = systemsituation;
    }

    public String getBusinessRequestId() {
        return businessRequestId;
    }

    public void setBusinessRequestId(String businessRequestId) {
        this.businessRequestId = businessRequestId;
    }

    public String getReportno() {
        return reportno;
    }

    public void setReportno(String reportno) {
        this.reportno = reportno;
    }
}
