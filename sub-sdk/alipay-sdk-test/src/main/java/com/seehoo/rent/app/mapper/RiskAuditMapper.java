package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehoo.rent.app.entity.RiskAudit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 风控审核记录Mapper */
@Mapper
public interface RiskAuditMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RiskAudit> {

    /** 按订单倒序查审核记录（最新在前） */
    default List<RiskAudit> selectByOrderId(Long orderId) {
        return selectList(new LambdaQueryWrapper<RiskAudit>()
                .eq(RiskAudit::getOrderId, orderId)
                .orderByDesc(RiskAudit::getId));
    }
}
