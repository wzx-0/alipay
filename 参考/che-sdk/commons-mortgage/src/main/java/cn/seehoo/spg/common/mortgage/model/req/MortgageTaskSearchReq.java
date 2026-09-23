package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import java.util.List;

/**
 * 抵押信息集合查询入参
 * @author zhangxx
 * @date 2026/3/26 15:09
 */
@Data
public class MortgageTaskSearchReq {
    /**
     * id
     */
    private String id;

    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 车架号
     */
    private String carVin;

    /**
     * 订单类型 办押: PD_BANYA , 解押: PD_JIEYA
     */
    private String orderType;

    /**
     * 订单编号集合
     */
    private List<String> orderNos;

    /**
     * 要的抵押状态
     */
    private List<String> inStatus;

    /**
     * 不要的抵押状态
     */
    private List<String> notInStatus;

    /**
     * 办理进度同步 bizId
     */
    private List<String> bizIds;

    /**
     * 代办人证件号码
     */
    private String workerIdNumber;

    /**
     * 渠道编码
     */
    private String channelId;

    /**
     * 代理人id
     */
    private String agentId;

    /**
     * 根部门id
     */
    private String rootDeptId;

    /**
     * 部门id
     */
    private String currentDeptId;

    /**
     * 金融专员id
     */
    private String financialSpecialistId;
}
