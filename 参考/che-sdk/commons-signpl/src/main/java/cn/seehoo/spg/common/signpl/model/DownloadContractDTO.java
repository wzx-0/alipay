package cn.seehoo.spg.common.signpl.model;

import java.util.List;

/**
 * @author caofei
 * @desc 下载合同参数
 * @time 2025/9/25 17:33。
 */
public class DownloadContractDTO {
    /**
     * 签约订单号
     */
    private String orderNo;
    /**
     * 合同文件类型
     * 1-合同(已签署)
     * 2-存证
     * 3-双录视频
     * 4-半身照
     */
    private List<String> fileTypes;
    /**
     * 签署人ID(客户id)
     */
    private String signerId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }
}
