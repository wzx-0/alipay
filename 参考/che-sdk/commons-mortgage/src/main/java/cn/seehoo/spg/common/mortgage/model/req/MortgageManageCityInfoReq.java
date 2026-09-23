package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

/**
 * @author zhangxx
 * @date 2026/4/8 9:29
 */
@Data
public class MortgageManageCityInfoReq {
    private static final long serialVersionUID = 1L;
    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 上级城市编码
     */
    private String parentCityCode;

    /**
     * 发证机关
     */
    private String licenseOffice;

    /**
     * 车牌前缀
     */
    private String licensePrefix;

    /**
     * 状态
     */
    private String status;

    /**
     * 抵押方式
     */
    private String mortgageType;
}
