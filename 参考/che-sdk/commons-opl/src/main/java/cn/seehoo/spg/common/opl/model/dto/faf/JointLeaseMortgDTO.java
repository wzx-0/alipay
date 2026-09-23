package cn.seehoo.spg.common.opl.model.dto.faf;


/**
 * @author
 * @date 2026/8/25 17:13
 * @since 1.0
 */
public class JointLeaseMortgDTO {

    /**
     * 订单号
     */
    private String hxOrderNo;

    /**
     * 状态 01通过，02拒绝，03退回
     */
    private String reviewStatus;

    /**
     * 备注
     */
    private String remak;


    public String getHxOrderNo() {
        return hxOrderNo;
    }
    public void setHxOrderNo(String hxOrderNo) {
        this.hxOrderNo = hxOrderNo;
    }
    public String getReviewStatus() {
        return reviewStatus;
    }
    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
    public String getRemak() {
        return remak;
    }
    public void setRemak(String remak) {
        this.remak = remak;
    }
}
