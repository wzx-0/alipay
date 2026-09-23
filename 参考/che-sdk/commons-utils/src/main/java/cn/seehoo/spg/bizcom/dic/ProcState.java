package cn.seehoo.spg.bizcom.dic;

import cn.hutool.core.util.ObjectUtil;

/**
 * 流程状态
 * */
public enum ProcState {
    DRAFT("draft","草稿"),
    BACK_EDIT("back_edit","退回修改"),
    APPROVING("approving","审批中"),
    CANCEL("cancel","已取消"),
    SUSPEND("suspend","挂起中"),
    STOP("stop","已终止"),
    REJECT("reject","已拒绝"),
    FINISH("finish","已完成");

    private final String code;
    private final String desc;

    ProcState(String code, String desc) {
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
        if ("2".equals(status)){
            return APPROVING.getCode();
        }
        if ("3".equals(status)){
            return FINISH.getCode();
        }
        if ("4".equals(status)){
            return REJECT.getCode();
        }
        if ("5".equals(status)){
            return CANCEL.getCode();
        }
        if ("6".equals(status)){
            return SUSPEND.getCode();
        }
        if ("8".equals(status)){
            return BACK_EDIT.getCode();
        }
        return null;
    }
}
