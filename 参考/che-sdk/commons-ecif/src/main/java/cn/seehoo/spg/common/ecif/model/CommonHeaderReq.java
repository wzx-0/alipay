package cn.seehoo.spg.common.ecif.model;

/**
 * 公共请求入参
 * @author HeCG
 * @date 2025/9/23 下午3:36
 * @since 1.0
 */
public class CommonHeaderReq {

    /**
     * 当前登录账号
     */
    private String requestUserName;

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

}
