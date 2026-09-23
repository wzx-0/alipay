package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建或更新抵押任务入参
 * @author zhangxx
 * @date 2026/3/26 15:07
 */
@Data
public class MortgageTaskCreateReq {
    /**
     * 系统来源 D00600
     */
    @NotBlank(message = "系统来源不能为空")
    private String source;
    /**
     * 是否退回订单，默认为否(1：是,0：否)
     */
    private String isReturnOrder;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 办理类型 1线下，2线上
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
     * 业务模式
     */
    private String businessModel;
    /**
     * 客户姓名
     */
    private String customName;
    /**
     * 客户手机号
     */
    private String customMobile;

    /**
     * 预计上牌省CODE
     */
    private String licenseProvinceCode;
    /**
     * 预计上牌省名称
     */
    private String licenseProvinceName;

    /**
     * 预计上牌市CODE
     */
    private String licenseCityCode;

    /**
     * 预计上牌市名称
     */
    private String licenseCityName;

    /**
     * 预计上牌方区县代码
     */
    private String licensingDistrictCode;

    /**
     * 预计上牌方区县名称
     */
    private String licensingDistrictName;

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
     * 合同状态
     */
    private String contractStatus;

    /**
     * 放款日期
     */
    private LocalDate loanDate;

    /**
     * 抵押期限
     */
    private LocalDate mortgageTerm;

    /**
     * 结清日期
     */
    private LocalDate settleDate;

    /**
     * 是否挂靠
     */
    private String isAttachment;

    /**
     * 渠道编号
     */
    private String channelId;
    /**
     * 渠道名称
     */
    private String channelName;

    /******************2023年12月18日后新增字段**********/

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
     * 抵押人类型 2：企业 1：个人
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
     * 发动机号
     */
    private String vehicleEngineNo;

    /**
     * 驱动电机号
     */
    private String driveMotorNumber;

    /**
     * 车辆类型 D00075
     */
    private String vehicleType;
    /**
     * 附件信息
     * applicantIDPositive	    承租人身份证正面
     * applicantIDReverse	    承租人身份证反面
     * orderContractEvidence	合同存证
     * workerHalfLengthPhoto	代理人现场照
     * workerIDPositiveReverse	代理人身份证正反面
     * leaseContract            融资租赁合同小类编码
     * mortgageContract         抵押合同小类编码
     * mortgageConstractParty   抵押合同-挂靠小类编码
     */
    private List<FileInfoDto> files;

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
