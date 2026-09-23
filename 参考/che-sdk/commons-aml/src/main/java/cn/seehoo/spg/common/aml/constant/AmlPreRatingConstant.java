package cn.seehoo.spg.common.aml.constant;

/**
 * @author caofei
 * @desc  反洗钱模块常量
 * @time 2025/9/23 14:43。
 */
public class AmlPreRatingConstant {
    public static final String AML_RESPONSE_CODE = "SUCCESS";

    public static final String MOCK_ON = "on";
    /**
     * 自然人客户预评级实时接口地址
     */
    public static final String CUST_RSLT_PRE_I_RT_URL = "/aml-prel/openapi/rrp/cust_rslt_pre_i_real";

    /**
     * 业务系统编号
     */
    public static final  String SERVICE_ID = "serviceId";

    /**
     * 时间戳
     */
    public static final String TIME_STAMP = "timestamp";

    /**
     * 签名
     */
    public static final  String SIGN = "sign";
}
