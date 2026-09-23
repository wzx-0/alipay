package cn.seehoo.spg.common.ecif.model;

/**
 * 客户负责人响应躰
 * @ HeCG
 * @date 2025/9/23 下午3:47
 * @since 1.0
 */
public class PersonDirectorRes {

    /**
     * 客户负责人类型
     */
    private String directorType;

    /**
     * 负责人登录账号
     */
    private String directorUserName;

    /**
     * 主负责人
     * 渠道客户经理为主负责人
     */
    private String isHost;

    public String getDirectorType() {
        return directorType;
    }

    public void setDirectorType(String directorType) {
        this.directorType = directorType;
    }

    public String getDirectorUserName() {
        return directorUserName;
    }

    public void setDirectorUserName(String directorUserName) {
        this.directorUserName = directorUserName;
    }

    public String getIsHost() {
        return isHost;
    }

    public void setIsHost(String isHost) {
        this.isHost = isHost;
    }

}
