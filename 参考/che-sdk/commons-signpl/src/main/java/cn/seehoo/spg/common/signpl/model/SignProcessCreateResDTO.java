package cn.seehoo.spg.common.signpl.model;

/**
 * @author caofei
 * @desc
 * @time 2025/9/25 17:47。
 */
public class SignProcessCreateResDTO{
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 签约链接
     */
    private String signLink;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSignLink() {
        return signLink;
    }

    public void setSignLink(String signLink) {
        this.signLink = signLink;
    }
}
