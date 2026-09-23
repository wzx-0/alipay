package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 抵押合作方机构规则配置
 * </p>
 *
 * @author czh
 * @since 2025-10-27 19:50:32
 */
@Data
public class QueryMortgageTypeReq {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 项目模式 D98013
     */
    @NotBlank(message = "项目模式不能为空")
    private String mortgageSystemType;

    /**
     * 合作机构编码
     */
    private String merchantCode;

    /**
     * 合作机构名称
     */
    private String merchantName;

    /**
     * 业务类型 	D00102   1-直租 、2-回租
     */
    private String leaseType;

    /**
     * 业务模式 D00104 01-消费融、02-车主融
     */
    private String businessModel;

    /**
     * 车辆类型 D00075 1-新车、2-二手车
     */
    private String vehicleType;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

}
