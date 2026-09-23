package cn.seehoo.common.unify.request;

import java.util.List;

/**
 * 角色管理菜单配置请求
 */
public class RoleFunctionConfigRequest {
    /** 角色ID */
    private Long roleId;

    /** 功能id列表 */
    private List<Long> functionIds;

    public RoleFunctionConfigRequest(Long roleId, List<Long> functionIds) {
        this.roleId = roleId;
        this.functionIds = functionIds;
    }

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public List<Long> getFunctionIds() {
        return functionIds;
    }
    public void setFunctionIds(List<Long> functionIds) {
        this.functionIds = functionIds;
    }
}
