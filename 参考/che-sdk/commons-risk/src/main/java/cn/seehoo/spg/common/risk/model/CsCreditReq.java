package cn.seehoo.spg.common.risk.model;

import java.io.Serializable;

/**
 * 查询个人征信请求
 */
public class CsCreditReq implements Serializable {

    private static final long serialVersionUID = 3075232088529301388L;


    /**
     * 前置系统的查询员用户名(必填)
     */
    private String username;
    /**
     * 前置用户密码（必填）
     */
    private String password;
    /**
     * 前置系统的审核员用户名
     */
    private String recheckuser;
    /**
     * 被查客户姓名（必填）
     */
    private String name;
    /**
     * 客户证件号码（必填）
     */
    private String certno;
    /**
     * 客户证件类型码（必填）
     * 预审时，默认{10-居民身份证及其他以公民身份证号为标识的证件}
     */
    private String certype = "10";
    /**
     * 查询原因码（必填）
     * 默认{24-融资审批}
     */
    private String queryreason = "24";
    /**
     * 返回报告格式（必填）
     * 1xml、2 html、3html和xml、4 pdf、5 json、6 pdf和xml
     * 默认{pdf格式}
     */
    private String reporttype = "5";
    /**
     * 来源系统（必填）
     * 默认：3-车辆零售融资租赁系统
     */
    private String systemresource = "3";
    /**
     * 业务场景（必填）
     */
    private String systemsituation = "13";
    /**
     * 1-本地优先、2-仅查询本地、3-仅查询征信中心
     * 默认{1-本地优先}
     */
    private String localflag = "1";
    /**
     * 本地报告缓存期（数字天数）
     * 数字必须大于0，当需要查询本地时（localflag不为3），如果不传则使用系统全局的时效
     */
    private Integer localvalidity;
    /**
     * 授权书开始时间（必填）
     * 精确到天(YYYY-MM-DD)
     * 签署完成的授权书创建时间  1.在华夏签署时，取征信授权书签署完成时间；2.由渠道上传时，取附件上传成功时间。
     */
    private String authstartdate;
    /**
     * 授权书结束时间
     */
    private String authenddate;
    /**
     * 授权书文件路径（必填）
     * 举例： /shareFile/authFile.pdf
     * “个人征信授权书（承租人）”附件路径；使用桶路径。
     */
    private String authfilepath;
    /**
     * 证件正面路径
     */
    private String certfile1;
    /**
     * 证件反面路径
     */
    private String certfile2;
    /**
     * 证件有效期起始日
     * 精确到天(YYYY-MM-DD)
     */
    private String certstartdate;
    /**
     * 证件有效期结束日
     * 精确到天(YYYY-MM-DD)
     * 不填代表长期
     */
    private String certenddate;
    /**
     * 业务请求id
     */
    private String businessRequestId;

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

    public String getRecheckuser() {
        return recheckuser;
    }

    public void setRecheckuser(String recheckuser) {
        this.recheckuser = recheckuser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getCertype() {
        return certype;
    }

    public void setCertype(String certype) {
        this.certype = certype;
    }

    public String getQueryreason() {
        return queryreason;
    }

    public void setQueryreason(String queryreason) {
        this.queryreason = queryreason;
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

    public String getLocalflag() {
        return localflag;
    }

    public void setLocalflag(String localflag) {
        this.localflag = localflag;
    }

    public Integer getLocalvalidity() {
        return localvalidity;
    }

    public void setLocalvalidity(Integer localvalidity) {
        this.localvalidity = localvalidity;
    }

    public String getAuthstartdate() {
        return authstartdate;
    }

    public void setAuthstartdate(String authstartdate) {
        this.authstartdate = authstartdate;
    }

    public String getAuthenddate() {
        return authenddate;
    }

    public void setAuthenddate(String authenddate) {
        this.authenddate = authenddate;
    }

    public String getAuthfilepath() {
        return authfilepath;
    }

    public void setAuthfilepath(String authfilepath) {
        this.authfilepath = authfilepath;
    }

    public String getCertfile1() {
        return certfile1;
    }

    public void setCertfile1(String certfile1) {
        this.certfile1 = certfile1;
    }

    public String getCertfile2() {
        return certfile2;
    }

    public void setCertfile2(String certfile2) {
        this.certfile2 = certfile2;
    }

    public String getCertstartdate() {
        return certstartdate;
    }

    public void setCertstartdate(String certstartdate) {
        this.certstartdate = certstartdate;
    }

    public String getCertenddate() {
        return certenddate;
    }

    public void setCertenddate(String certenddate) {
        this.certenddate = certenddate;
    }

    public String getBusinessRequestId() {
        return businessRequestId;
    }

    public void setBusinessRequestId(String businessRequestId) {
        this.businessRequestId = businessRequestId;
    }
}
