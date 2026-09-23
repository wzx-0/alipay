package cn.seehoo.common.unify.exception;

/**
 * @author liuzeng
 * @date 2025/9/25 上午10:25
 * @since 1.0
 */
public class UnifyAppException {
    public static final String APP_TOKEN_NULL = "100100001:app token为空！";
    public static final String DEVICE_NUM_LIMIT = "100100002:当前手机号绑定的设备数量超过最大值！";
    public static final String QUERY_DEVICE_ERROR = "100100003:获取统一APP设备数量错误！";
    public static final String GET_APP_USER_ERROR = "100100004:获取app用户信息失败！";
    public static final String GET_TOKEN_ERROR = "100100005:获取统一APP token异常！";
    public static final String ADD_RESOURCE_ERROR = "100100006:统一APP新增资源异常！";
    public static final String UPDATE_RESOURCE_ERROR = "100100007:统一APP修改资源异常！";
    public static final String ADD_ROLE_ERROR = "100100008:统一APP新增角色异常！";
    public static final String UPDATE_ROLE_ERROR = "100100009:统一APP更新角色异常！";
    public static final String GRANT_ROLE_RESOURCE_ERROR = "100100010:统一APP角色关联资源异常！";
    public static final String DELETE_USER_ROLE_REL_ERROR = "100100011:统一APP用户解绑角色异常！";
    public static final String BIND_USER_ROLE_REL_ERROR = "100100012:统一APP用户綁定角色异常！";
    public static final String CREATE_USER_ERROR = "100100013:统一APP用户创建用户异常！";
    public static final String UPDATE_USER_ERROR = "100100014:统一APP用户更新用户异常！";
}
