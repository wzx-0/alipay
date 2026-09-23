package cn.seehoo.spg.bizcom.dto;

import java.util.List;

/**
 * 
 * 任务通知DTO
 * */
public class TaskNoticeDTO {
    public static final String ACTION = "id=gogzzs|name=Jump|type=pushroot|node=offlinepackage|target=00000023/unsecuredPage.html|isneedlogin=true|param:ok=1&navbar=false&readTitle=false|passData:listName=prepareList&tenantId=1&procInstId=%s&taskInstId=%s";
    /** 系统标签 */
    private String label;
    /** 流程实例ID */
    private String procInstId;
    /** 需个推通知的任务 */
    private List<TaskDTO> noticeTaskList;
    /** 发起人或者审批人，用逗号分隔 */
    private String noticeUsersBefores;
    /** 流程状态 */
    private String procState;
    /** 流程是否删除 */
    private Integer isDelete = 0;

    /** 任务DTO */
    public static class TaskDTO{
        /** 任务主键 */
        private String id;
        /** 任务id */
        private String taskId;
        /** 标题 */
        private String title;
        /** 主体 */
        private String body;
        /** 打开方式 */
        private String action;
        /** 需个推通知的用户 */
        private String noticeUsers;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getNoticeUsers() {
            return noticeUsers;
        }

        public void setNoticeUsers(String noticeUsers) {
            this.noticeUsers = noticeUsers;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public List<TaskDTO> getNoticeTaskList() {
        return noticeTaskList;
    }

    public void setNoticeTaskList(List<TaskDTO> noticeTaskList) {
        this.noticeTaskList = noticeTaskList;
    }

    public String getNoticeUsersBefores() {
        return noticeUsersBefores;
    }

    public void setNoticeUsersBefores(String noticeUsersBefores) {
        this.noticeUsersBefores = noticeUsersBefores;
    }

    public String getProcState() {
        return procState;
    }

    public void setProcState(String procState) {
        this.procState = procState;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
