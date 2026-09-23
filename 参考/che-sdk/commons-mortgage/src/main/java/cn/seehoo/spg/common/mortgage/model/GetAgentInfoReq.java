package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;
import lombok.ToString;

/**
 * 代理人信息查询Dto
 */
@Data
@ToString
public class GetAgentInfoReq {

    /**
     * 渠道编号
     */
    private String channelId;
    /**
     * 商户编号
     */
    private String merchantNo = "mc001";
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
    private String mortgageeName = "华夏金融租赁有限公司";
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
