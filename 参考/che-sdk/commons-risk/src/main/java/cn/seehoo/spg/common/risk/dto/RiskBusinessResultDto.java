package cn.seehoo.spg.common.risk.dto;

import java.util.List;

/**
 * @author tangying
 * @version 1.0
 * @date 2026/8/3 15:02
 * @since 1.0
 */
public class RiskBusinessResultDto {
    /**
     * 业务订单唯一标识
     */
    private String orderId;
    /**
     * 业务订单类型
     */
    private String orderType;
    /**
     * 风控异步任务ID
     */
    private String taskId;
    /**
     * PASS 通过 REJECT 拒绝
     */
    private String approvalResult;
    /**
     * 命中规则集合
     */
    private List<HitRule> hitRules;
    /**
     * 备注
     */
    private String remark;
    /**
     * 回调时间
     */
    private String callbackTime;

    class HitRule {
        /**
         * 业务场景标识 01 跨渠道拒绝拦截 02 金融专员黑名单
         */
        private String bizScene;
        /**
         * 命中规则编码
         */
        private String code;
        /**
         * 命中规则名称
         */
        private String desc;
        /**
         * 拒绝原因描述
         */
        private String channelDesc;
        /**
         * 是否触发规则
         */
        private String hit;
        /**
         * 规则结果
         */
        private String result;

        public String getBizScene() {
            return bizScene;
        }

        public void setBizScene(String bizScene) {
            this.bizScene = bizScene;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getChannelDesc() {
            return channelDesc;
        }

        public void setChannelDesc(String channelDesc) {
            this.channelDesc = channelDesc;
        }

        public String getHit() {
            return hit;
        }

        public void setHit(String hit) {
            this.hit = hit;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public List<HitRule> getHitRules() {
        return hitRules;
    }

    public void setHitRules(List<HitRule> hitRules) {
        this.hitRules = hitRules;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(String callbackTime) {
        this.callbackTime = callbackTime;
    }
}
