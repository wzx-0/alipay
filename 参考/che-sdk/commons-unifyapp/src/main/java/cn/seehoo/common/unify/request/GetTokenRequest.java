package cn.seehoo.common.unify.request;

import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;

/**
 * @author liuzeng
 * @date 2025/9/25 上午10:19
 * @since 1.0
 */
public class GetTokenRequest {

    /**
     * 用户名
     */
//    @NotNull(message = GetTokenException.USER_NAME_NULL)
    private String username;

    /**
     * 密码
     */
//    @NotNull(message = GetTokenException.PASS_WORD_NULL)
    private String password;


    /**
     * 应用id
     */
//    @NotNull(message = GetTokenException.APP_ID_NULL)
    private String appid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    // 校验参数
    public void checkParams() throws BusinessException {
        ExceptionUtil.check(this);
    }
}
