package cn.seehoo.spg.common.bhrc.req;

import lombok.Data;

/**
 * 百行银行卡四要素请求参数类
 */
@Data
public class BankCardFourCheckReq {
    /**
     * 证件号
     */
    private String certNo;
    /**
     * 证件类型 默认身份证-00
     */
    private String certType;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 姓名
     */
    private String name;
    /**
     * 银行卡号
     */
    private String cardNo;
    /** 调用环节 */
    private String callStage;
    /** 合作方 */
    private String partnerName;
    /** 业务编号 */
    private String businessNo;
}
