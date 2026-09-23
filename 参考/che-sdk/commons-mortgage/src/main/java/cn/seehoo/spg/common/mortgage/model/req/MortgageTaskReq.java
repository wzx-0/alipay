package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

/**
 * 抵押任务下发入参
 * @author zhangxx
 * @date 2026/3/26 15:20
 */
@Data
public class MortgageTaskReq {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

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
     * 车牌号
     */
    private String carNum;

    /**
     * 业务联系人
     */
    private String businessPeople;

    /**
     * 业务联系人手机号
     */
    private String businessPhone;

    /**
     * 抵押方式 1线下，2线上
     */
    private String type;

    /**
     * 代理人姓名
     */
    private String workerName;

    /**
     * 代理人电话
     */
    private String workerMobile;

    /**
     * 代理人证件类型
     */
    private String workerIdType;

    /**
     * 代理人证件号码
     */
    private String workerIdNumber;

    /**
     * 代理人邮政编码
     */
    private String workerPostalCode;

    /**
     * 代理人邮寄地址
     */
    private String workerMailAddress;

    /**
     * 代办人办理渠道 1专网 2公安网
     */
    private String workerHandleChannel;

    /**
     * 备注-其他说明
     */
    private String remaks;
}
