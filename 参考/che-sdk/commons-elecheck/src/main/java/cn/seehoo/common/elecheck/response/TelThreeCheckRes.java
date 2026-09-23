package cn.seehoo.common.elecheck.response;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author sunyf
 * @date 2025/9/20 下午5:29
 * @since 1.0
 */
public class TelThreeCheckRes {
    public static final String RESULT_OK = "1";

    /**
     * 运营商
     * 1：电信2：联通3：移动4：其他，如170号段等
     */
    private String operation;
    /**
     * 验证结果
     * 0：查无此号；1：一致；2：不一致；空当Operation=4时，Result为空
     */
    private String result;

    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 判断是否本人手机号，返回ture为本人手机号
     * @return
     */
    public Boolean telCheckTrue() {
        return ObjectUtil.equal(result, RESULT_OK);
    }
}
