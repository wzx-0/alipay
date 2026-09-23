package cn.seehoo.spg.common.signpl.constant;

/**
 * @author caofei
 * @desc 签约平台 Api 常量
 * @time 2025/9/25 17:12。
 */
public class SignApiConstant {
    /**
     * 创建签约流程
     */
    public static final String CREATE_SIGN_PROCESS = "/innerapi/sign/order/create";
    /**
     * 获取签约链接
     */
    public static final String GET_SIGN_LINK = "/innerapi/sign/getSigningLink";
    /**
     * 获取签约订单结果
     */
    public static final String GET_SIGN_RESULT = "/innerapi/sign/order/getSignResult";
    /**
     * 取消合同
     */
    public static final String CANCEL_CONTRACT = "/innerapi/sign/order/cancel";
    /**
     * 合同下载
     */
    public static final String CONTRACT_DOWNLOAD = "/innerapi/sign/order/downloadContract";

    /**
     * 获取签署方信息
     */
    public static final String GET_SIGN_LINK_DATA = "/innerapi/sign/getSigningLinkData";
}
