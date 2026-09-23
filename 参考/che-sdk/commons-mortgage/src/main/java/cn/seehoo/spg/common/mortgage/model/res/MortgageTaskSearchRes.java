package cn.seehoo.spg.common.mortgage.model.res;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 抵押信息集合查询出参
 * @author zhangxx
 * @date 2026/3/26 15:09
 */
@Data
public class MortgageTaskSearchRes {
    /**
     * 主键
     */
    private String id;

    /**
     * 抵押任务编号
     */
    private String mortgageNo;

    /**
     * 抵押方式
     */
    private String type;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 客户姓名
     */
    private String customName;

    /**
     * 客户手机号
     */
    private String customMobile;

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
     * 上牌省
     */
    private String licenseProvinceCode;

    /**
     * 上牌省名称
     */
    private String licenseProvinceName;
    /**
     * 上牌城市
     */
    private String licenseCityCode;

    /**
     * 上牌市名称
     */
    private String licenseCityName;

    /**
     * 抵押期限
     */
    private LocalDate mortgageTerm;

    /**
     * 预计上牌方区县代码
     */
    private String licensingDistrictCode;

    /**
     * 预计上牌方区县名称
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
     * 车牌号
     */
    private String carNum;


    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 抵押任务状态
     */
    private String mortgageTaskStatus;

    /**
     * 抵押任务状态
     */
    private String mortgageTaskStatusName;

    /**
     * 合同状态
     */
    private String contractStatus;

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
     * 逻辑删除 0-未删除 1-已删除
     */
    private String logicDelete;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 材料下载通知状态
     */
    private String noticeStatus;

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
     * 登记日期
     */
    private LocalDate regDate;

    /**
     * 抵押登记日期 未使用
     */
    private LocalDate mortgageRegDate;

    /**
     * 收妥日期
     */
    private LocalDate receivedDate;

    /**
     * 存档日期
     */
    private LocalDate archiveDate;

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

    /**
     * 审批人员工号
     */
    private String userNo;

    /**
     *审批是否退回 1：是,0：否
     */
    private String approvalReturn;

    /**
     *审审批退回类型 退回初审,退回资管公司
     */
    private String approvalReturnType;
    /**
     * 资管公司任务编号
     */
    private String assetBn;

    /**
     * 系统来源 01-有担，02-无担 D98013
     */
    private String mortgageSystemType;

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
