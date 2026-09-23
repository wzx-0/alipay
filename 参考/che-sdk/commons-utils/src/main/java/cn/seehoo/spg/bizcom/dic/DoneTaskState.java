package cn.seehoo.spg.bizcom.dic;

import cn.hutool.core.util.ObjectUtil;

/**
 * 已办任务状态
 * */
public enum DoneTaskState {
    APPROVAL("approval", "同意"),
    REJECT("reject", "驳回"),
    REFUSE("refuse", "拒绝"),
    DRAWBACK("drawback", "收回"),
    REVOKED("revoke", "撤销"),
    CANCEL("cancel", "取消"),
    STOP("stop", "终止"),
    FINISH("finish", "结束"),
    ADDAUDIT("addaudit", "加签"),
    TRANSFER("transfer", "转办");

    private final String code;
    private final String desc;

    DoneTaskState(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String transWdStatus(String status){
        if (ObjectUtil.isEmpty(status)){
            return null;
        }
        if ("3".equals(status)){
            return APPROVAL.getCode();
        }
        if ("4".equals(status)){
            return REJECT.getCode();
        }
        if ("5".equals(status)){
            return REJECT.getCode();
        }
        if ("6".equals(status)){
            return REFUSE.getCode();
        }
        if ("7".equals(status)){
            return CANCEL.getCode();
        }
        if ("9".equals(status)){
            return CANCEL.getCode();
        }
        if ("10".equals(status)){
            return STOP.getCode();
        }
        return null;
    }
}
