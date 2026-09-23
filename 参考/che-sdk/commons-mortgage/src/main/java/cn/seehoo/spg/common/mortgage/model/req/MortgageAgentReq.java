package cn.seehoo.spg.common.mortgage.model.req;

import cn.seehoo.spg.common.mortgage.enums.MortgageApiConstants;
import lombok.Data;

/**
 * 获取代理人信息入参
 * @author zhangxx
 * @date 2026/3/26 15:10
 */
@Data
public class MortgageAgentReq {
    /**
     * 渠道编号
     */
    private String channelId;
    /**
     * 商户编号
     */
    private String merchantNo = MortgageApiConstants.CHANNEL_NO_MC;
    /**
     * 申请编号
     */
    private String bizNo;
    /**
     * 订单类型, 办押：PD_BANYA, 解押：PD_JIEYA
     */
    private String bizType;
    /**
     * 抵押权人名称
     */
    private String mortgageeName = MortgageApiConstants.BANK_NAME;
    /**
     * 上牌省CODE
     */
    private String provinceCode;
    /**
     * 上牌省Name
     */
    private String provinceName;
    /**
     * 上牌市CODE
     */
    private String cityCode;
    /**
     * 上牌市Name
     */
    private String cityName;
    /**
     * 上牌方区县代码
     */
    private String districtCode;
    /**
     * 上牌方区县名称
     */
    private String districtName;
}
