package cn.seehoo.common.unify.response;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author liuzeng
 * @date 2025/9/27 下午3:36
 * @since 1.0
 */
public class SyncUserInfoResponse {
    /** 启用 */
    public static final int STATUS_ON = 1;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 外部邮箱地址
     */
    private String outerEmail;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户状态（通常：0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 用户密码
     */
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOuterEmail() {
        return outerEmail;
    }

    public void setOuterEmail(String outerEmail) {
        this.outerEmail = outerEmail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 判断启用
    public boolean checkStatusOn() {
        return ObjectUtil.equal(status, STATUS_ON);
    }
}
