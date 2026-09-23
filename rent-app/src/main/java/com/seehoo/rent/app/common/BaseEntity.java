package com.seehoo.rent.app.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实体审计字段基类：对应DDL公共列（is_deleted/create_time/update_time/create_id/update_id/dept_id）
 */
@Data
public class BaseEntity {

    /** 逻辑删除：0-否 1-是 */
    @TableLogic
    @TableField("is_deleted")
    private String isDeleted;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人ID */
    @TableField("create_id")
    private Long createId;

    /** 更新人ID */
    @TableField("update_id")
    private Long updateId;

    /** 部门ID */
    @TableField("dept_id")
    private Long deptId;
}
