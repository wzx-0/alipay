package cn.seehoo.common.unify.response;

/**
 * @author liuzeng
 * @date 2025/9/27 下午3:36
 * @since 1.0
 */
public class SyncDepartmentResponse {


    /**
     * 部门负责人ID
     */
    private Long leaderId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 上层部门id
     */
    private String ancestors;

    /**
     * 显示顺序/排序号
     */
    private Integer orderNum;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门状态（通常：0-禁用，1-启用）
     */
    private String status;

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }
}
