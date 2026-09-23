package cn.seehoo.spg.common.signpl.model;

/**
 * @author caofei
 * @desc 签约平台响应基类
 * @time 2025/9/25 17:43。
 */
public  class SignPlBaseRes {
    public static final String SUCCESS_CODE = "000000";
    private String code;
    private String message;
    private String timeStamp;
    private String data;
    private String msg;
    /**
     *  业务操作是否成功
     * @return true:成功，false:失败
     */
    public boolean bizSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
