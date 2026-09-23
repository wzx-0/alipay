package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seehoo.rent.app.entity.AlipayNotifyRecord;
import org.apache.ibatis.annotations.Mapper;

/** 支付宝异步通知记录Mapper */
@Mapper
public interface AlipayNotifyRecordMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<AlipayNotifyRecord> {

    /** 按通知ID查（幂等去重） */
    default AlipayNotifyRecord selectByNotifyId(String notifyId) {
        return selectOne(new LambdaQueryWrapper<AlipayNotifyRecord>().eq(AlipayNotifyRecord::getNotifyId, notifyId));
    }
}
