package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 合作机构配置查询入参
 * @author zhangxx
 * @date 2026/3/26 15:06
 */
@Data
public class MortgagePartnerReq {
    /**
     * 主键 （更新必传）
     */
    private Long id;

    /**
     * 项目模式 D98013
     */
    @NotBlank(message = "项目模式不能为空")
    private String mortgageSystemType;

    /**
     * 规则编码
     */
    private String configCode;

    /**
     * 合作机构编码
     */
    private String merchantCode;

    /**
     * 合作机构名称
     */
    private String merchantName;

    /**
     * 业务类型
     */
    private String leaseType;

    /**
     * 业务模式
     */
    private String businessModel;

    /**
     * 车辆类型
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

    /**
     * 线上抵解押是否开启 0-否、1-是
     */
    private String onlineMortgage;

    /**
     * 线下抵解押是否开启 0-否、1-是
     */
    private String offlineMortgage;

    /**
     * 优先抵押方式 1-线下、2-线上
     */
    private String priorityType;

    /**
     * 是否渠道自办
     */
    private String channelSelfOperated;

    /**
     * 办理车务公司 - 线上
     */
    private String onlineOperateCompany;

    /**
     * 办理车务公司名称 - 线上
     */
    private String onlineOperateCompanyName;

    /**
     * 办理车务公司 - 线下
     */
    private String offlineOperateCompany;

    /**
     * 办理车务公司名称 - 线下
     */
    private String offlineOperateCompanyName;

    /**
     * 代理人是否签署
     */
    private String workerSign;

    /**
     * 状态 D00178
     */
    private String status;

    /**
     * 抵押类型 1线下，2线上
     */
    private String type;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建时间 - start
     */
    private LocalDate createTimeStart;

    /**
     * 创建时间 - end
     */
    private LocalDate createTimeEnd;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新时间 - start
     */
    private LocalDate updateTimeStart;

    /**
     * 更新时间 - end
     */
    private LocalDate updateTimeEnd;

    /**
     * 逻辑删除0:未删除，1:已删除
     */
    private Integer logicDelete;

    /**
     * 线上抵押城市信息
     */
    private List<MortgageManageCityInfoDto> onlineMortgageManageCityInfoList;

    /**
     * 线下抵押城市信息
     */
    private List<MortgageManageCityInfoDto> offlineMortgageManageCityInfoList;
}
