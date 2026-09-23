package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.Data;

/**
 * 鑫快链site类型操作结果同步 - 请求实体类
 * 接口：鑫快链site类型操作结果同步
 * 请求方式：HTTPS/POST
 */
@Data
public class SiteStatusSyncRequest {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 华夏订单号
     */
    private String orderNo;

    /**
     * 证件号
     */
    private String cardNo;

    /**
     * 流水code
     */
    private String siteCode;

    /**
     * 流水名称
     */
    private String siteName;

    /**
     * 流水状态：默认0：无状态（不展示状态）1：成功，2：失败，3：处理中
     */
    private String siteStatus;

    /**
     * 流水状态名称：默认0：无状态（不展示状态）1：成功，2：失败，3：处理中
     */
    private String siteStatusName;

    /**
     * 请求时间戳
     */
    private Long requestTime;

    /**
     * 检查是终态
     */
    public Boolean checkFinalStatus(){
        return XKLConstant.SITE_FINAL_STATUS.contains(this.siteStatus);
    }
}
