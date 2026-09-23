package cn.seehoo.spg.common.aml.constant;

import cn.hutool.core.collection.ListUtil;

import java.util.List;

/**
 * @author caofei
 * @desc 风险等级常量
 * @time 2025/9/23 13:44。
 */
public class AmlRiskLevelTypeConstant {
    /**
     * 低风险
     */
    public static final String LOW_RISK = "1001";
    /**
     * 较低风险
     */
    public static final String LOWER_RISK = "1002";
    /**
     * 中风险
     */
    public static final String MID_RISK = "1003";
    /**
     * 较高风险
     */
    public static final String HIGHER_RISK = "1004";
    /**
     * 高风险
     */
    public static final String HIGH_RISK = "1005";
    /**
     * 高风险等级 需拦截进件流程
     */
    public static final List<String> HIGH_RISK_LEVELS = ListUtil.toList(HIGHER_RISK, HIGH_RISK);
}
