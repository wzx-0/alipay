package cn.seehoo.spg.common.aml.client;

import cn.seehoo.spg.common.aml.model.AmlDTO;
import cn.seehoo.spg.common.aml.model.AmlNatPerMonitorResult;
import cn.seehoo.spg.common.aml.model.AmlOfficialRatingResult;
import cn.seehoo.spg.common.aml.model.AmlResult;

/**
 * @desc: AML客户端几类
 * @author: caofei
 * @time: 2025/9/25 15:06
 */
public interface AmlBaseClient {
    /**
     * 反洗钱预评级（实时）
     * @param appAmlPreRateRt 参数
     * @return 预评级结果
     */
    public AmlResult preRatingRt(AmlDTO appAmlPreRateRt);

    /**
     * 自然人监控名单查询结果接口(实时)
     * @param appAmlMonitorRt 参数
     * @return 监控名单查询结果
     */
    public AmlNatPerMonitorResult natPerMonitorRt(AmlDTO appAmlMonitorRt);

    /**
     * 反洗钱系统正式评级结果查询接口
     * @param custId 统一客户编号
     * @return 正式评级结果查询结果
     */
    public AmlOfficialRatingResult officialRatingRt(String custId);

}
