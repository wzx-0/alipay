package cn.seehoo.spg.common.bhrc.res;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liusijia
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class TelThreeCheckRes {
    public static final String RESULT_OK = "0";

    /**
     * 运营商
     * 1：电信2：联通3：移动4：其他，如170号段等
     */
    private String operator;
    /**
     * 验证结果
     * 0：一致；1：姓名不一致；2：证件号不一致；3：均不一致 -1：已查无数据 -2：查询异常
     */
    private String isIdNameMatchSimp;

    /**
     * 判断是否本人手机号，返回ture为本人手机号
     * @return
     */
    public Boolean telCheckTrue() {
        return ObjectUtil.equal(isIdNameMatchSimp, RESULT_OK);
    }
}
