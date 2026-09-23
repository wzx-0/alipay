package cn.seehoo.spg.common.signpl.model;

/**
 * @author caofei
 * @desc 取消合同参数
 * @time 2025/9/25 17:33。
 */
public class CancelContractDTO {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 取消备注
     */
    private String remark;
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
