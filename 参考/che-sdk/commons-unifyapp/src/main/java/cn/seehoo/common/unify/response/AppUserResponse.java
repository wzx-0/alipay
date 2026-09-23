package cn.seehoo.common.unify.response;

/**
 * app用户
 */
public class AppUserResponse {
    /** 手机号 */
    private String phoneNo;
    /** 过期时间 */
    private Long empireTime;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Long getEmpireTime() {
        return empireTime;
    }

    public void setEmpireTime(Long empireTime) {
        this.empireTime = empireTime;
    }
}
