package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 合同结清处理入参
 * @author zhangxx
 * @date 2026/3/26 15:12
 */
@Data
public class MortgageSettlementReq {
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
