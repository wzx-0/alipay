package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.seehoo.rent.app.entity.RentOrderInstallment;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/** 订阅计划期次Mapper */
@Mapper
public interface RentOrderInstallmentMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentOrderInstallment> {

    /** 按期号升序查订单全部期次 */
    default List<RentOrderInstallment> selectByOrderId(Long orderId) {
        return selectList(new LambdaQueryWrapper<RentOrderInstallment>()
                .eq(RentOrderInstallment::getOrderId, orderId)
                .orderByAsc(RentOrderInstallment::getInstallmentNo));
    }

    /** 按订单与期号查唯一期次 */
    default RentOrderInstallment selectByOrderIdAndNo(Long orderId, Integer installmentNo) {
        return selectOne(new LambdaQueryWrapper<RentOrderInstallment>()
                .eq(RentOrderInstallment::getOrderId, orderId)
                .eq(RentOrderInstallment::getInstallmentNo, installmentNo));
    }

    /** 批量插入期次（Db工具批量，避免循环单条） */
    default boolean insertBatch(Collection<RentOrderInstallment> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return Db.saveBatch(list);
    }

    /** 批量更新期次 */
    default boolean updateBatch(Collection<RentOrderInstallment> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return Db.updateBatchById(list);
    }
}
