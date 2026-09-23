package cn.seehoo.spg.bizcom.gd.exception;

/**
 * mentianyu
 * 错误码
 */
public enum GdErrorCode {

    /**
     * 请求正常
     */
    OK(10000, "OK", "请求正常"),

    /**
     * key不正确或过期
     */
    INVALID_USER_KEY(10001, "INVALID_USER_KEY", "key不正确或过期"),

    /**
     * 没有权限使用相应的服务或者请求接口的路径拼写错误
     */
    SERVICE_NOT_AVAILABLE(10002, "SERVICE_NOT_AVAILABLE", "没有权限使用相应的服务或者请求接口的路径拼写错误"),

    /**
     * 访问已超出日访问量
     */
    DAILY_QUERY_OVER_LIMIT(10003, "DAILY_QUERY_OVER_LIMIT", "访问已超出日访问量"),

    /**
     * 单位时间内访问过于频繁
     */
    ACCESS_TOO_FREQUENT(10004, "ACCESS_TOO_FREQUENT", "单位时间内访问过于频繁"),

    /**
     * IP白名单出错
     */
    INVALID_USER_IP(10005, "INVALID_USER_IP", "IP白名单出错，发送请求的服务器IP不在IP白名单内"),

    /**
     * 绑定域名无效
     */
    INVALID_USER_DOMAIN(10006, "INVALID_USER_DOMAIN", "绑定域名无效"),

    /**
     * 数字签名未通过验证
     */
    INVALID_USER_SIGNATURE(10007, "INVALID_USER_SIGNATURE", "数字签名未通过验证"),

    /**
     * MD5安全码未通过验证
     */
    INVALID_USER_SCODE(10008, "INVALID_USER_SCODE", "MD5安全码未通过验证"),

    /**
     * 请求key与绑定平台不符
     */
    USERKEY_PLAT_NOMATCH(10009, "USERKEY_PLAT_NOMATCH", "请求key与绑定平台不符"),

    /**
     * IP访问超限
     */
    IP_QUERY_OVER_LIMIT(10010, "IP_QUERY_OVER_LIMIT", "IP访问超限"),

    /**
     * 服务不支持https请求
     */
    NOT_SUPPORT_HTTPS(10011, "NOT_SUPPORT_HTTPS", "服务不支持https请求"),

    /**
     * 权限不足，服务请求被拒绝
     */
    INSUFFICIENT_PRIVILEGES(10012, "INSUFFICIENT_PRIVILEGES", "权限不足，服务请求被拒绝"),

    /**
     * Key被删除
     */
    USER_KEY_RECYCLED(10013, "USER_KEY_RECYCLED", "Key被删除"),

    /**
     * 云图服务QPS超限
     */
    QPS_HAS_EXCEEDED_THE_LIMIT(10014, "QPS_HAS_EXCEEDED_THE_LIMIT", "云图服务QPS超限"),

    /**
     * 受单机QPS限流限制
     */
    GATEWAY_TIMEOUT(10015, "GATEWAY_TIMEOUT", "受单机QPS限流限制"),

    /**
     * 服务器负载过高
     */
    SERVER_IS_BUSY(10016, "SERVER_IS_BUSY", "服务器负载过高"),

    /**
     * 所请求的资源不可用
     */
    RESOURCE_UNAVAILABLE(10017, "RESOURCE_UNAVAILABLE", "所请求的资源不可用"),

    /**
     * 使用的某个服务总QPS超限
     */
    CQPS_HAS_EXCEEDED_THE_LIMIT(10019, "CQPS_HAS_EXCEEDED_THE_LIMIT", "使用的某个服务总QPS超限"),

    /**
     * 某个Key使用某个服务接口QPS超出限制
     */
    CKQPS_HAS_EXCEEDED_THE_LIMIT(10020, "CKQPS_HAS_EXCEEDED_THE_LIMIT", "某个Key使用某个服务接口QPS超出限制"),

    /**
     * 账号使用某个服务接口QPS超出限制
     */
    CUQPS_HAS_EXCEEDED_THE_LIMIT(10021, "CUQPS_HAS_EXCEEDED_THE_LIMIT", "账号使用某个服务接口QPS超出限制"),

    /**
     * 账号处于被封禁状态
     */
    INVALID_REQUEST(10026, "INVALID_REQUEST", "账号处于被封禁状态"),

    /**
     * 某个Key的QPS超出限制 (注：原文描述为QPS超限，通常对应海外或特定场景)
     */
    ABROAD_DAILY_QUERY_OVER_LIMIT(10029, "ABROAD_DAILY_QUERY_OVER_LIMIT", "某个Key的QPS超出限制"),

    /**
     * 请求的接口权限过期
     */
    NO_EFFECTIVE_INTERFACE(10041, "NO_EFFECTIVE_INTERFACE", "请求的接口权限过期"),

    /**
     * 账号维度日调用量超出限制
     */
    USER_DAILY_QUERY_OVER_LIMIT(10044, "USER_DAILY_QUERY_OVER_LIMIT", "账号维度日调用量超出限制"),

    /**
     * 账号维度海外服务日调用量超出限制
     */
    USER_ABROAD_DAILY_QUERY_OVER_LIMIT(10045, "USER_ABROAD_DAILY_QUERY_OVER_LIMIT", "账号维度海外服务日调用量超出限制"),

    /**
     * 请求参数非法
     */
    INVALID_PARAMS(20000, "INVALID_PARAMS", "请求参数非法"),

    /**
     * 缺少必填参数
     */
    MISSING_REQUIRED_PARAMS(20001, "MISSING_REQUIRED_PARAMS", "缺少必填参数"),

    /**
     * 请求协议非法
     */
    ILLEGAL_REQUEST(20002, "ILLEGAL_REQUEST", "请求协议非法"),

    /**
     * 其他未知错误
     */
    UNKNOWN_ERROR(20003, "UNKNOWN_ERROR", "其他未知错误"),

    /**
     * 查询坐标或规划点在海外，但没有海外地图权限
     */
    INSUFFICIENT_ABROAD_PRIVILEGES(20011, "INSUFFICIENT_ABROAD_PRIVILEGES", "查询坐标或规划点在海外，但没有海外地图权限"),

    /**
     * 查询信息存在非法内容
     */
    ILLEGAL_CONTENT(20012, "ILLEGAL_CONTENT", "查询信息存在非法内容"),

    /**
     * 规划点不在中国陆地范围内 (路径规划)
     */
    OUT_OF_SERVICE(20800, "OUT_OF_SERVICE", "规划点（包括起点、终点、途经点）不在中国陆地范围内"),

    /**
     * 划点附近搜不到路
     */
    NO_ROADS_NEARBY(20801, "NO_ROADS_NEARBY", "划点（起点、终点、途经点）附近搜不到路"),

    /**
     * 路线计算失败
     */
    ROUTE_FAIL(20802, "ROUTE_FAIL", "路线计算失败，通常是由于道路连通关系导致"),

    /**
     * 起点终点距离过长
     */
    OVER_DIRECTION_RANGE(20803, "OVER_DIRECTION_RANGE", "起点终点距离过长"),

    /**
     * 服务响应失败 (通用引擎错误)
     */
    ENGINE_RESPONSE_DATA_ERROR(30000, "ENGINE_RESPONSE_DATA_ERROR", "服务响应失败"),

    /**
     * 余额耗尽
     */
    QUOTA_PLAN_RUN_OUT(40000, "QUOTA_PLAN_RUN_OUT", "余额耗尽"),

    /**
     * 围栏个数达到上限
     */
    GEOFENCE_MAX_COUNT_REACHED(40001, "GEOFENCE_MAX_COUNT_REACHED", "围栏个数达到上限"),

    /**
     * 购买服务到期
     */
    SERVICE_EXPIRED(40002, "SERVICE_EXPIRED", "购买服务到期"),

    /**
     * 海外服务余额耗尽
     */
    ABROAD_QUOTA_PLAN_RUN_OUT(40003, "ABROAD_QUOTA_PLAN_RUN_OUT", "海外服务余额耗尽");

    private final int code;
    private final String info;
    private final String message;

    GdErrorCode(int code, String info, String message) {
        this.code = code;
        this.info = info;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据错误码获取枚举对象
     *
     * @param code 错误码
     * @return 对应的枚举对象，如果未找到则返回 null
     */
    public static GdErrorCode getByCode(int code) {
        for (GdErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }

    /**
     * 根据 infocode 字符串获取枚举对象
     *
     * @param info infocode 字符串
     * @return 对应的枚举对象，如果未找到则返回 null
     */
    public static GdErrorCode getByInfo(String info) {
        if (info == null) {
            return null;
        }
        for (GdErrorCode errorCode : values()) {
            if (errorCode.getInfo().equals(info)) {
                return errorCode;
            }
        }
        return null;
    }
}
