package cn.seehoo.common.unify.request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzeng
 * @date 2025/9/24 下午3:31
 * @since 1.0
 */
public class UserRoleOptRequest {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 解绑角色Id列表
     */
    private List<Long> oldRoleIds = new ArrayList<>();

    /**
     * 绑定角色Id列表
     */
    private List<Long> newRoleIds = new ArrayList<>();

    public UserRoleOptRequest(Long userId, List<Long> oldRoleIds, List<Long> newRoleIds) {
        this.oldRoleIds = oldRoleIds;
        this.newRoleIds = newRoleIds;
        this.userId = userId;
    }

    public UserRoleOptRequest() {
    }

    public List<Long> getOldRoleIds() {
        return oldRoleIds;
    }
    public void setOldRoleIds(List<Long> oldRoleIds) {
        this.oldRoleIds = oldRoleIds;
    }
    public List<Long> getNewRoleIds() {
        return newRoleIds;
    }
    public void setNewRoleIds(List<Long> newRoleIds) {
        this.newRoleIds = newRoleIds;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
