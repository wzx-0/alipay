package cn.seehoo.spg.common.signpl.model;

import cn.hutool.core.collection.CollUtil;
import cn.seehoo.spg.common.signpl.client.BizContStatusConstant;
import cn.seehoo.spg.common.signpl.client.SignPlRoleConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author caofei
 * @desc 签约结果信息对象
 * @time 2025/9/25 19:16。
 */
public class SignResultResDTO{
    public static final String FINISH = "T5";
    public static final String TASK_COMPLETED = "1";
    private static final Logger LOGGER = LoggerFactory.getLogger(SignResultResDTO.class);
    private String status;
    private String statusName;
    private LocalDateTime completeTime;
    private boolean downloadFinishFlag;
    /**
     * 签署人信息
     */
    private List<SignerData> singers;

    public static  class SignerData {
        /**
         * 签署人角色：1-承租人；2-担保人；3-资金方
         */
        private String signerRole;
        /**
         * 任务状态 0-未完成 1-已完成 2-失败
         */
        private String signCompleteResult;
        /**
         * 签约链接
         */
        private String signLink;
        /**
         * 企业账号
         */
        private String account;

        public String getSignerRole() {
            return signerRole;
        }

        public void setSignerRole(String signerRole) {
            this.signerRole = signerRole;
        }

        public String getSignCompleteResult() {
            return signCompleteResult;
        }

        public void setSignCompleteResult(String signCompleteResult) {
            this.signCompleteResult = signCompleteResult;
        }

        public String getSignLink() {
            return signLink;
        }

        public void setSignLink(String signLink) {
            this.signLink = signLink;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        /**
         * 是否签署完成
         * @return true -false
         */
        public boolean taskCompleted(){
            return TASK_COMPLETED.equals(this.signCompleteResult);
        }
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public boolean isDownloadFinishFlag() {
        return downloadFinishFlag;
    }

    public void setDownloadFinishFlag(boolean downloadFinishFlag) {
        this.downloadFinishFlag = downloadFinishFlag;
    }

    public static String getFINISH() {
        return FINISH;
    }

    public static String getTaskCompleted() {
        return TASK_COMPLETED;
    }

    public List<SignerData> getSingers() {
        return singers;
    }

    public void setSingers(List<SignerData> singers) {
        this.singers = singers;
    }

    /**
     * 签约完成
     * @return true 完成 false 未完成
     */
    public Integer signCompleted(){
        if(FINISH.equals(this.status)&&this.downloadFinishFlag){
            return BizContStatusConstant.SIGNED;
        }
        List<SignerData> singers = this.singers;
        if(CollUtil.isEmpty(singers)){
            LOGGER.info(">>>>>>>>>>>>> 签约平台返回的签署人信息为空！");
            return null;
        }
        boolean leaseFinishSign = false;
        boolean guarantorFinishSign = false;
        boolean hasGuarantor = false;
        boolean affiliationFinishSign = false;
        boolean hasAffiliation = false;
        boolean enterpriseFinishSign = false;
        for (SignerData singer : singers) {
            if(SignPlRoleConstant.PL_LESSEE.equals(singer.getSignerRole())){
                if(singer.taskCompleted()){
                    leaseFinishSign =true;
                }
                continue;
            }
            if(SignPlRoleConstant.PL_GUARANTOR.equals(singer.getSignerRole())){
                hasGuarantor =true;
                if(singer.taskCompleted()){
                    guarantorFinishSign =true;
                }
                continue;
            }
            if (SignPlRoleConstant.PL_AFFILIATION.equals(singer.getSignerRole())) {
                hasAffiliation = true;
                if(singer.taskCompleted()){
                    affiliationFinishSign = true;
                }
            }
            if (SignPlRoleConstant.PL_ENTERPRISE.equals(singer.getSignerRole())) {
                if(singer.taskCompleted()){
                    enterpriseFinishSign = true;
                }
            }
        }
        if (singers.size() == 1 && SignPlRoleConstant.PL_ENTERPRISE.equals(singers.get(0).getSignerRole())) {
            if (!enterpriseFinishSign) {
                return BizContStatusConstant.ENTERPRISE_SIGNING;
            }
        }
        if (!leaseFinishSign) {
            return BizContStatusConstant.LESSEE_SIGNING;
        }
        if (hasGuarantor && !guarantorFinishSign) {
            return BizContStatusConstant.GUARANTOR_SIGNING;
        }
        if (hasAffiliation && !affiliationFinishSign) {
            return BizContStatusConstant.AFFILIATE_SIGNING;
        }
        return BizContStatusConstant.SIGNING;
    }
}
