package cn.seehoo.spg.common.mortgage.model;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 抵押任务管理表
 * </p>
 *
 */
@Getter
@Setter
public class MortgageTaskManageInfoOpenApiReq {

    private static final long serialVersionUID = 1L;


    /**
     * 订单编号
     */
    private String orderNo;


    /**
     * 抵押省
     */
    private String mortgageProvinceCode;

    /**
     * 抵押省名称
     */
    private String mortgageProvinceName;
    /**
     * 抵押城市
     */
    private String mortgageCityCode;

    /**
     * 抵押城市名称
     */
    private String mortgageCityName;

    /**
     * 车架号
     */
    private String carVin;


    /**
     * 业务联系人
     */
    private String businessPeople;

    /**
     * 业务联系人手机号
     */
    private String businessPhone;

    /**
     * 备注-其他说明
     */
    private String remaks;

}
