package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehoo.rent.app.entity.RentPayItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 支付费项明细Mapper */
@Mapper
public interface RentPayItemMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentPayItem> {

    /** 查支付记录全部费项 */
    default List<RentPayItem> selectByPayRecordId(Long payRecordId) {
        return selectList(new LambdaQueryWrapper<RentPayItem>().eq(RentPayItem::getPayRecordId, payRecordId));
    }
}
