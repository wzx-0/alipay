package cn.seehoo.common.unify.request;

import cn.hutool.core.util.ObjectUtil;
import cn.seehoo.common.unify.config.UnifyAppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzeng
 * @date 2025/9/24 下午3:22
 * @since 1.0
 */
public class RoleGrantPermissionRequest {

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 权限列表
     */
    private List<PermissionItem> permissionList;

    /**
     * 子应用字典编码（提前从统一平台获取）
     */
    private String appCode;

    public RoleGrantPermissionRequest() {
    }

    public RoleGrantPermissionRequest(Long roleId, List<Long> functionIds, UnifyAppConfig unifyAppConfig) {
        this.roleId = roleId;
        permissionList = new ArrayList<>();
        appCode = unifyAppConfig.getAppCode();
        List<Long> configIds = unifyAppConfig.getFunctionIds();
        for (int i = 0; i < configIds.size(); i++) {
            Long id = configIds.get(i);
            if (functionIds.contains(id)) {
                permissionList.add(new PermissionItem(id, i + 1));
            }
        }
    }

    /**
     * 权限项内部类
     */
    public static class PermissionItem {

        /**
         * 功能清单id
         */
        private Long resourceId;

        /**
         * 排序值
         */
        private Integer sort;

        public PermissionItem() {
        }

        public PermissionItem(Long resourceId, Integer sort) {
            this.resourceId = resourceId;
            this.sort = sort;
        }

        public Long getResourceId() {
            return resourceId;
        }

        public void setResourceId(Long resourceId) {
            this.resourceId = resourceId;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<PermissionItem> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<PermissionItem> permissionList) {
        this.permissionList = permissionList;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void addPermission(PermissionItem item) {
        permissionList.add(item);
    }

    // 判断待同步菜单是否为空
    public boolean checkPermissionItemEmpty() {
        return ObjectUtil.isEmpty(this.permissionList);
    }

}
