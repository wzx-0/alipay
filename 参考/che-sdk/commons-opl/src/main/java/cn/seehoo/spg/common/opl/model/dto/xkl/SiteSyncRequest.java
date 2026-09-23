package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.Data;

import java.util.List;

/**
 * 鑫快链客户选择的site类型同步 - 请求实体类
 * 接口：鑫快链客户选择的site类型同步
 * 请求方式：HTTPS/POST
 */
@Data
public class SiteSyncRequest {

    /**
     * 之前发起的时候返回给发起方的流程id
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
     * 客户选择的流程列表（多次选择，推送全量）
     */
    private List<ProtocolItem> protocolList;

    /**
     * 协议项（site流水信息）
     */
    @Data
    public static class ProtocolItem {

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
         * 流程类型
         */
        private String processType;
    }
}
