package cn.seehoo.spg.common.bhrc.model;

import cn.seehoo.spg.common.bhrc.constant.BhrcConstant;

/**
 * 百行征信公安四要素认证结果
 */
public class BhrcAuthResultDTO {
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    /**
     * 是否认证通过
     * @return true 通过 false 失败
     */
    public boolean pass(){
        return BhrcConstant.AUTH_A001.equals(this.getVerificationCode());
    }
}
