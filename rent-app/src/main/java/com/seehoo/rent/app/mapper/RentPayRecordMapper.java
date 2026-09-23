package com.seehoo.rent.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seehoo.rent.app.entity.RentPayRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 支付记录Mapper */
@Mapper
public interface RentPayRecordMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<RentPayRecord> {

    /** 按订单查支付记录 */
    default List<RentPayRecord> selectByOrderId(Long orderId) {
        return selectList(new LambdaQueryWrapper<RentPayRecord>().eq(RentPayRecord::getOrderId, orderId));
    }

    /** 按商家侧支付单号查唯一记录 */
    default RentPayRecord selectByOutTradeNo(String outTradeNo) {
        return selectOne(new LambdaQueryWrapper<RentPayRecord>().eq(RentPayRecord::getOutTradeNo, outTradeNo));
    }

    /** 支付流水分页（保留扩展，当前业务查询走selectByOrderId） */
    default Page<RentPayRecord> selectPayRecordPage(Page<RentPayRecord> page, String outOrderId) {
        return selectPage(page, new LambdaQueryWrapper<RentPayRecord>()
                .eq(StringUtils.isNotBlank(outOrderId), RentPayRecord::getOutTradeNo, outOrderId)
                .orderByDesc(RentPayRecord::getId));
    }
}
