package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;
import lombok.ToString;

/**
 * 代理人信息查询Dto
 */
@Data
@ToString
public class GetAgentInfoRes {

    /**
     * 代理人姓名
     */
    private String agentName;

    /**
     * 代理人电话
     */
    private String agentPhone;

    /**
     * 代理人证件类型
     */
    private String agentIdType;

    /**
     * 代理人证件号码
     */
    private String agentIdNumber;

    /**
     * 代理人邮政编码
     */
    private String postalCode;

    /**
     * 代理人邮寄地址
     */
    private String address;

    /**
     * 办理渠道 1专网 2公安网
     */
    private String handleChannel;

}
