package cn.seehoo.common.unify.request;

import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;

/**
 * @author liuzeng
 * @date 2025/9/27 下午3:19
 * @since 1.0
 */
public class SyncUserOrDeptRequest {
    /**
     * 部门id
     */
//    @NotNull(message = SyncUserDeptException.DEPT_ID_NULL)
    private String deptId;

    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    // 校验参数
    public void checkParams() throws BusinessException {
        ExceptionUtil.check(this);
    }
}
