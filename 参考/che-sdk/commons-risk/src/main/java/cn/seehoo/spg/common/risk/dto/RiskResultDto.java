package cn.seehoo.spg.common.risk.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 风控回调结果DTO
 */
public class RiskResultDto {

    /**
     * 报告结果
     * HIGH:拒绝， MID:审慎审核  LOW:通过
     */
    private String approvalResult;
    /**
     * 得分
     */
    private String score;
    /**
     * 单号
     */
    private String orderId;
    private String modelId;
    /**
     * 申请唯一标识
     */
    private String businessId;
    /**
     * 证件号码
     */
    private String certNo;
    /**
     * 手机号
     */
    private String mobile;
    private List<RiskInfo> riskInfo;
    /**
     * 业务场景类型
     * 1:预审;2：提报;3;资审
     */
    private String businessType;
    /**
     * 附件id
     */
    private String filePath;
    /**
     * 流水ID，用于报告查看
     */
    private String flowid;
    /**
     * 个人征信报告编号
     */
    private String reportNo;
    /**
     * 征信查询状态码 D98002
     */
    private String creditCode;
    /**
     * 征信查询描述
     */
    private String creditMessage;
    /**
     * 业务查询ID
     */
    private String businessRequestId;
    /**
     * 规则级别
     */
    private String ruleLevel;
    /**
     * 客户级别
     */
    private String custLevel;
    /**
     * 征信月负债
     * */
    private String monthlyLiabilities;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 征信白户标签
     */
    private String creditBlankTag;
    /**
     * 二手单标签 	 D_PREAUDIT_002
     */
    private String secondOrderTag;
    /**
     *是否降级标识 	 D_PREAUDIT_004
     */
    private String downFlag;
    /**
     *快审条件（审批提示信息）
     */
    private String prompt;
    /**
     *快审标识 D_ORDR_004
     */
    private String fastAppFlag;

    /**
     *是否秒批 D_ORDR_005
     */
    private String secondPassFlag;
    /**
     * 秒批信息
     */
    private String secondPassAmount;

    /**
     * 是否重新测算
     */
    private String recountFlag;
    /**
     * 风控是否通过判断
     * @param scoreMin 风控最低分数
     * @return 通过返回true
     */
    public boolean checkScore(BigDecimal scoreMin) {
        //小于最低分不通过
        if (BigDecimal.valueOf(Double.valueOf(this.score)).compareTo(scoreMin) == -1) {
            return false;
        }
        return true;
    }

    public String getRecountFlag() {
        return recountFlag;
    }

    public void setRecountFlag(String recountFlag) {
        this.recountFlag = recountFlag;
    }

    public String getSecondPassAmount() {
        return secondPassAmount;
    }

    public void setSecondPassAmount(String secondPassAmount) {
        this.secondPassAmount = secondPassAmount;
    }

    public String getSecondPassFlag() {
        return secondPassFlag;
    }

    public void setSecondPassFlag(String secondPassFlag) {
        this.secondPassFlag = secondPassFlag;
    }

    public String getDownFlag() {
        return downFlag;
    }

    public void setDownFlag(String downFlag) {
        this.downFlag = downFlag;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getFastAppFlag() {
        return fastAppFlag;
    }

    public void setFastAppFlag(String fastAppFlag) {
        this.fastAppFlag = fastAppFlag;
    }

    public String getCreditBlankTag() {
        return creditBlankTag;
    }

    public void setCreditBlankTag(String creditBlankTag) {
        this.creditBlankTag = creditBlankTag;
    }

    public String getSecondOrderTag() {
        return secondOrderTag;
    }

    public void setSecondOrderTag(String secondOrderTag) {
        this.secondOrderTag = secondOrderTag;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBusinessRequestId() {
        return businessRequestId;
    }

    public void setBusinessRequestId(String businessRequestId) {
        this.businessRequestId = businessRequestId;
    }

    public String getRuleLevel() {
        return ruleLevel;
    }

    public void setRuleLevel(String ruleLevel) {
        this.ruleLevel = ruleLevel;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getMonthlyLiabilities() {
        return monthlyLiabilities;
    }

    public void setMonthlyLiabilities(String monthlyLiabilities) {
        this.monthlyLiabilities = monthlyLiabilities;
    }

    public String getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(String approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<RiskInfo> getRiskInfo() {
        return riskInfo;
    }

    public void setRiskInfo(List<RiskInfo> riskInfo) {
        this.riskInfo = riskInfo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFlowid() {
        return flowid;
    }

    public void setFlowid(String flowid) {
        this.flowid = flowid;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getCreditMessage() {
        return creditMessage;
    }

    public void setCreditMessage(String creditMessage) {
        this.creditMessage = creditMessage;
    }

    class RiskInfo {

        private Boolean hit;
        private String code;
        private String desc;
        private Integer score;
        private String result;
        private String channelDesc;

        public Boolean getHit() {
            return hit;
        }

        public void setHit(Boolean hit) {
            this.hit = hit;
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

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getChannelDesc() {
            return channelDesc;
        }

        public void setChannelDesc(String channelDesc) {
            this.channelDesc = channelDesc;
        }

    }

}
