package cn.seehoo.spg.bizcom.order.model;

public class OrderExistResp {
    public static final String YES = "1";
    public static final String NO = "0";
    /** 客户是否存在在途订单，0: 否，1: 是 */
    private String customerFlag;
    private String customerMsg;
    /** 车架号是否存在在途订单，0: 否，1: 是 */
    private String vinFlag;
    private String vinMsg;
    /** 是否首次进件，0: 否，1: 是 */
    private String isFirstFlag;
    private String isFirstMsg;
    /**
     * 客户
     */
    private CustomerOrderInfo customerOrderInfo;
    /**
     * 车架号
     */
    private VinOrderInfo vinOrderInfo;

    public CustomerOrderInfo getCustomerOrderInfo() {
        return customerOrderInfo;
    }
    public void setCustomerOrderInfo(CustomerOrderInfo customerOrderInfo) {
        this.customerOrderInfo = customerOrderInfo;
    }
    public VinOrderInfo getVinOrderInfo() {
        return vinOrderInfo;
    }
    public void setVinOrderInfo(VinOrderInfo vinOrderInfo) {
        this.vinOrderInfo = vinOrderInfo;
    }
    public String getCustomerFlag() {
        return customerFlag;
    }
    public void setCustomerFlag(String customerFlag) {
        this.customerFlag = customerFlag;
    }
    public String getCustomerMsg() {
        return customerMsg;
    }
    public void setCustomerMsg(String customerMsg) {
        this.customerMsg = customerMsg;
    }
    public String getVinFlag() {
        return vinFlag;
    }
    public void setVinFlag(String vinFlag) {
        this.vinFlag = vinFlag;
    }
    public String getVinMsg() {
        return vinMsg;
    }
    public void setVinMsg(String vinMsg) {
        this.vinMsg = vinMsg;
    }
    public String getIsFirstFlag() {
        return isFirstFlag;
    }
    public void setIsFirstFlag(String isFirstFlag) {
        this.isFirstFlag = isFirstFlag;
    }
    public String getIsFirstMsg() {
        return isFirstMsg;
    }
    public void setIsFirstMsg(String isFirstMsg) {
        this.isFirstMsg = isFirstMsg;
    }

    // 判断客户是否存在在途订单
    public boolean checkCustomerExist() {
        return this.customerFlag.equals(YES);
    }

    // 判断车架号是否存在在途订单
    public boolean checkVimExist() {
        return this.vinFlag.equals(YES);
    }

    // 判断是否首次进件
    public boolean checkFirstReport() {
        return this.isFirstFlag.equals(YES);
    }
}
