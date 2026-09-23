package cn.seehoo.spg.common.ecif.model;

/**
 * @author HTTP响应
 * @date 2025/9/23 下午7:53
 * @since 1.0
 */
public class ResData<T> {

    /**
     * 返回码
     */
    private String returnCode;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
