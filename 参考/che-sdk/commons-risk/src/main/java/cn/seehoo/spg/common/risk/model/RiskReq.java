package cn.seehoo.spg.common.risk.model;

import java.io.Serializable;

/**
 * 风控请求入参
 * @author HeCG
 * @date 2025/9/23 下午3:36
 * @since 1.0
 */
public class RiskReq implements Serializable {

    private static final long serialVersionUID = 1075232088529301388L;

    /**
     * 业务场景类型  1:预审-承租人
     */
    public final static String BUSINESS_TYPE_1 = "1";

    /**
     * 业务场景类型  3：资审承租人
     */
    public final static String BUSINESS_TYPE_3 = "3";

    /**
     * 业务场景类型  4：资审-担保人
     */
    public final static String BUSINESS_TYPE_4 = "4";

    /**
     * 业务场景类型  5：入围-提报人员
     */
    public final static String BUSINESS_TYPE_5 = "5";
    /**
     * 业务场景类型  6：易鑫资审承租人
     */
    public final static String BUSINESS_TYPE_6 = "6";
    /**
     * 业务场景类型  7：易鑫预审
     */
    public final static String BUSINESS_TYPE_7 = "7";
    /**
     * 业务场景类型  6：易鑫资审承租人
     */
    public final static String BUSINESS_TYPE_8 = "8";

    /**
     * 预审订单号
     */
    private String preOrderId;

    /**
     * 渠道ID
     */
    private String channelId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 业务id(由接口方提供)
     */
    private String businessId;

    /**
     * 业务场景类型  1:预审;2：提报;3;资审
     * @return
     */
    private String businessType;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 模型分类
     */
    private String modelType;

    /**
     * 查询个人征信请求
     */
    private CsCreditReq creditReq;

    /**
     * 承租人信息
     */
    private MainLoanReq mainLoan;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public MainLoanReq getMainLoan() {
        return mainLoan;
    }

    public void setMainLoan(MainLoanReq mainLoan) {
        this.mainLoan = mainLoan;
    }

    public CsCreditReq getCreditReq() {
        return creditReq;
    }

    public void setCreditReq(CsCreditReq creditReq) {
        this.creditReq = creditReq;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
