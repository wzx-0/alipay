package cn.seehoo.spg.common.bhrc.res;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 百行 银行卡四要素校验 结果类
 */
@Data
@AllArgsConstructor
public class BankCardFourCheckRes {
    public static final String STR_OK = "0";
    /**
     * 校验结果 验证一致:0 验证不一致:1 不支持验证或验证失败:-1 查询异常:-2
     */
    private String verifyStatus;

    public boolean checkCheckSuccess() {
        return STR_OK.equals(this.verifyStatus);
    }
}
