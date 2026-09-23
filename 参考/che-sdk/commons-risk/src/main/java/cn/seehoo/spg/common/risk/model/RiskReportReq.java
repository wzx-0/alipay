package cn.seehoo.spg.common.risk.model;

/**
 * 风控报告查询入参
 */
public class RiskReportReq {

    /**
     * 资审环节流水号
     */
    private String businessId;

    /**
     * 资审单号
     */
    private String orderId;

    /**
     * 预审单号
     */
    private String preOrderId;

    /**
     * 身份证号
     */
    private String certNo;

    /**
     * 业务查询征信ID
     */
    private String businessRequestId;

    /**
     * 征信流水ID
     */
    private String flowId;

    /**
     * 报告类型
     */
    private String reportType;

    /**
     * 渠道编号如大搜车 传DST
     */
    private String channelId;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPreOrderId() {
        return preOrderId;
    }

    public void setPreOrderId(String preOrderId) {
        this.preOrderId = preOrderId;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getBusinessRequestId() {
        return businessRequestId;
    }

    public void setBusinessRequestId(String businessRequestId) {
        this.businessRequestId = businessRequestId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
