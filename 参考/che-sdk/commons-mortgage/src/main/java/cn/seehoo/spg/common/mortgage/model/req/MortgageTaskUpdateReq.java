package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import java.util.List;

/**
 * 抵押信息修改入参
 * @author zhangxx
 * @date 2026/3/26 15:08
 */
@Data
public class MortgageTaskUpdateReq {

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
     * 是否需要手写签名
     */
    private String handWrittenFlag;

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
     * mortgageArchiveFile	    抵押归档材料
     */
    private List<MortgageFileInfoDto> files;

    /**
     * 备注-其他说明
     */
    private String remaks;
}
