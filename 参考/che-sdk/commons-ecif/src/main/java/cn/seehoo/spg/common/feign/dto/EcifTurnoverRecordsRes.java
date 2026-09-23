package cn.seehoo.spg.common.feign.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * ECIF对接流水表
 */
@Data
public class EcifTurnoverRecordsRes implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
//    private Long id;

    /**
     * 请求流水号
     */
    private String requestSerial;

    /**
     * 业务编号
     */
    private String businessCode;

    /**
     * 调用节点
     */
    private String invokeNode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 统一客户编码
     */
    private String ecifCustomerId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 请求方向(1代表上送。2代表回调)
     */
    private String requestDirection;

    /**
     * 调用结果(1代表成功。2代表失败)
     */
    private String callResult;

    /**
     * 重试标志(1代表不重试，2代表重试)
     */
    private String retryFlag;

    /**
     * 请求时间
     */
    private LocalDateTime requestTime;

    /**
     * 响应时间
     */
    private LocalDateTime responseTime;

    /**
     * 请求参数
     */
    private String requestParameters;

    /**
     * 响应参数
     */
    private String responseParameters;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志，0表示未删除，1表示删除
     */
    private String logicDelete;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
}
