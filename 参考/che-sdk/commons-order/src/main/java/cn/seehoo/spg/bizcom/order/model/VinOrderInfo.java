package cn.seehoo.spg.bizcom.order.model;

/**
 * @author
 * @date 2026/8/10 15:34
 * @since 1.0
 */
public class VinOrderInfo {
    /**
     * 渠道
     */
    private String channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 渠道
     */
    private String orderStatus;
    /**
     * 渠道渠道名称
     */
    private String orderStatusName;
    /**
     * 金融专员
     */
    private String reporterName;

    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderStatusName() {
        return orderStatusName;
    }
    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }
    public String getReporterName() {
        return reporterName;
    }
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
}
