package cn.seehoo.spg.common.mortgage.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合同结清信息
 *
 */
@Data
@ToString
public class SettlementInfoReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderNo;
    /**
     * 合同结清类型：D00276
     */
    @NotEmpty(message = "合同结清类型不能为空")
    private String settlementType;
    /**
     * 合同结束日期
     */
    @NotNull(message = "合同结束日期不能为空")
    private LocalDateTime contractEndDate;

}
