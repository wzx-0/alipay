package cn.seehoo.spg.common.mortgage.model.req;

import cn.seehoo.spg.common.mortgage.enums.MortgageApiConstants;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MortgagePageReq {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 系统来源
     */
    private String source;

    /**
     * 抵押任务编号
     */
    private String mortgageNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 类型 D00550 1线下，2线上
     */
    private String type;

    /**
     * 变更类型 D00552 1线下转线上，2线上转线下
     */
    private String changeType;

    /**
     * 交科所序号
     */
    private String jksXh;

    /**
     * 交科所状态
     */
    private String jksStatus;

    /**
     * 客户姓名
     */
    private String customName;

    /**
     * 业务模式
     */
    private String businessModel;
    /**
     * 预计上牌省
     */
    private String licenseProvinceCode;

    /**
     * 预计上牌省名称
     */
    private String licenseProvinceName;
    /**
     * 预计上牌城市
     */
    private String licenseCityCode;

    /**
     * 预计上牌市名称
     */
    private String licenseCityName;

    /**
     * 预计上牌区
     */
    private String licensingDistrictCode;

    /**
     * 预计上牌区名称
     */
    private String licensingDistrictName;


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
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单状态名称 H5查询返回
     */
    private String orderStatusName;

    /**
     * 抵押任务状态
     */
    private String mortgageTaskStatus;
    /**
     * 抵押任务状态
     */
    private List<String> statusList;

    /**
     * 抵押任务状态
     */
    private String mortgageTaskStatusName;

    /**
     * 合同状态
     */
    private String contractStatus;

    /**
     * 合同状态
     */
    private List<String> contractStatusList;

    /**
     * 放款日期
     */
    private LocalDate loanDate;

    /**
     * 结清日期
     */
    private String settleDate;

    /**
     * 办理地址类别 1-车管所 2-警邮
     */
    private String handleAddressCategory;
    /**
     * 办理地址类别 1-车管所 2-警邮
     */
    private String handleAddressCategoryName;

    /**
     * 资管公司
     */
    private String assetCompany;

    /**
     * 资管公司id
     */
    private String assetCompanyId;

    /**
     * 业务联系人
     */
    private String businessPeople;

    /**
     * 业务联系人手机号
     */
    private String businessPhone;

    /**
     * 抵押日期
     */
    private LocalDate mortgageDate;
    /**
     * 备注-其他说明
     */
    private String remaks;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 渠道编号
     */
    private String channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 抵押权利证书编号
     */
    private String certificateNo;
    /**
     * 抵押登记机关
     */
    private String regOffice;

    /**
     * 抵押登记日期
     */
    private LocalDate regDate;

    /**
     * 登记证书状态
     */
    private String certificateStatus;
    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 业务主键
     */
    private String businessKey;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 是否可以审批
     */
    private String approveFlag;
    /**
     *流程ID
     */
    private String processInstanceId;

    /**
     * 流程定义id
     */
    private String ProcessDefinitionId;
    /**
     * 流程节点KEY
     */
    private String nodeKey;
    /**
     * 代办人姓名
     */
    private String workerName;
    /**
     * 代办人电话
     */
    private String workerMobile;
    /**
     * 代办人证件类型
     */
    private String workerIdType;
    /**
     * 代办人证件号码
     */
    private String workerIdNumber;
    /**
     * 代办人邮政编码
     */
    private String workerPostalCode;
    /**
     * 代办人邮寄地址
     */
    private String workerMailAddress;
    /**
     * 代办人办理渠道 1专网 2公安网
     */
    private String workerHandleChannel;


    /**
     * 抵押登记日期
     */
    private LocalDate mortgageRegDate;

    /**
     * 新增详情返回车牌号
     */
    private String carNum;

    /**
     * 抵押任务创建时间
     */
    private LocalDateTime createTime;

    //2023年12月18日新增字段
    /**
     * 是否挂靠
     */
    private String isAttachment;
    /**
     * 融资租赁合同号
     */
    private String contractNo;

    /**
     * 融资期限
     */
    private String productTerm;

    /**放款日期 */
    private LocalDate paymentTime;
    /**
     * 合同到期日
     */
    private LocalDate contractDate;

    /**
     * 经销商名
     */
    private String agentName;

    /**
     * 抵押人类型
     */
    private String mortgageType;

    /**
     * 抵押人名称
     */
    private String mortgageName;

    /**
     * 证件类型名称
     */
    private String certificationTypeName;

    /**
     * 抵押人证件号码
     */
    private String certification;

    /**
     * 抵押合同编号
     */
    private String contractMortgageNo;

    /**
     * 抵押合同签署日期
     */
    private LocalDate signDateYmd;

    /**
     * 抵押合同到期日
     */
    private LocalDate deadLineYmd;

    /**
     * 客户证件号码
     */
    private String customerNo;

    /**
     * 合同下载状态
     */
    private String contractDownloadStatus;

    /**
     * 合同首次下载时间
     */
    private LocalDateTime contractFirstDownloadDate;

    //放款日期开始时间
    private String loanDateStart;

    //放款日期结算时间
    private String loanDateEnd;

    //抵押日期开始时间
    private String mortgageDateStart;
    //抵押日期结算时间
    private String mortgageDateEnd;

    /**
     * 超期天数最小天数
     */
    private Long startDays;

    /**
     * 超期天数最多天数
     */
    private Long endDays;

    /***
     * 是否审批退回 1：是 0：否
     */
    private String approvalReturn;

    /**
     * 抵押权人
     */
    private String mortgagee;

    /**
     * 发动机号
     */
    private String vehicleEngineNo;

    /**
     * 发动机号可修改标识
     */
    private String vehicleEngineNoUpdateFlag = MortgageApiConstants.YES_STR;

    /**
     * 驱动电机号
     */
    private String driveMotorNumber;

    /**
     * 驱动电机号可修改标识
     */
    private String driveMotorNumberUpdateFlag = MortgageApiConstants.YES_STR;

    /**
     * 购车目的 D00067
     */
    private String carPurchasePurpose;

    /**
     * 车辆颜色
     */
    private String carColor;

    /**
     * 车辆使用性质 D10018
     */
    private String vehicleUsageNature;

    /**
     * 是否重签合同
     */
    private String retrySignFlag;
    /**
     * 重签合同列表
     */
    private List<String> contractList;

    /**
     * 是否放款
     */
    private String loanFlag;

    /**
     * 订单号
     */
    private List<String> orderNoList;

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
