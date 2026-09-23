package cn.seehoo.common.elecheck.response;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author sunyf
 * @date 2025/9/20 下午5:29
 * @since 1.0
 */
public class BankCardFourCheckRes {
    public static final String SUCCESS = "00";
    /**
     * 查询结果返回码
     * 00：验证匹配 01：验证不匹配（卡状态异常，例睡眠卡、挂失卡、受限制卡等） 02：验证异常（当日验证次数超限等） 20：银行卡不支持验证
     */
    private String respCode ;

    /**
     * 查询结果
     */
    private String respDesc ;

    public String getRespCode() {
        return respCode;
    }
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
    public String getRespDesc() {
        return respDesc;
    }
    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    // 校验匹配成功
    public Boolean checkCheckSuccess() {
        return ObjectUtil.equal(SUCCESS, respCode);
    }
}
