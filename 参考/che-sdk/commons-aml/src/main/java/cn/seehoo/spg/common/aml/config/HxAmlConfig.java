package cn.seehoo.spg.common.aml.config;
/**
 * @author chenzhuo
 */
public class HxAmlConfig {
    /** 是否开启 Mock   true 开启 false 关闭*/
    private boolean mock;
    /** 业务系统编号 */
    private String serviceId;
    /** 密钥 */
    private String channelSecret;
//    /** 服务地址 */
//    private String host;
    /** 自然人客户预评级实时接口地址 */
    private String naturalRatingRtUrl;

    /** 自然人监控名单查询结果接口(实时) */
    private String natPerMonitorRtUrl;

    /** 反洗钱系统正式评级结果查询接口 */
    private String officialRatingRtUrl;


    public boolean isMock() {
		return mock;
	}
	public void setMock(boolean mock) {
		this.mock = mock;
	}

	public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getChannelSecret() {
        return channelSecret;
    }

    public void setChannelSecret(String channelSecret) {
        this.channelSecret = channelSecret;
    }

    public String getNaturalRatingRtUrl() {
        return naturalRatingRtUrl;
    }

    public void setNaturalRatingRtUrl(String naturalRatingRtUrl) {
        this.naturalRatingRtUrl = naturalRatingRtUrl;
    }

    public String getNatPerMonitorRtUrl() {
        return natPerMonitorRtUrl;
    }

    public void setNatPerMonitorRtUrl(String natPerMonitorRtUrl) {
        this.natPerMonitorRtUrl = natPerMonitorRtUrl;
    }

    public String getOfficialRatingRtUrl() {
        return officialRatingRtUrl;
    }

    public void setOfficialRatingRtUrl(String officialRatingRtUrl) {
        this.officialRatingRtUrl = officialRatingRtUrl;
    }
}
