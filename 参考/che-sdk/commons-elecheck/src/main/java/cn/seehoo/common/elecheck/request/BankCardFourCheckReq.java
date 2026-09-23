package cn.seehoo.common.elecheck.request;

import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;

/**
 * @author sunyf
 * @date 2025/9/20 下午5:16
 * @since 1.0
 */
public class BankCardFourCheckReq {
    /** 身份证号 */
//    @NotNull(message = "身份证号不能为空")
//    @NotEmpty(message = "身份证号不能为空")
    private String id;
    /** 手机号 */
//    @NotNull(message = "手机号不能为空")
//    @NotEmpty(message = "手机号不能为空")
    private String cell;
    /** 姓名 */
//    @NotNull(message = "姓名不能为空")
//    @NotEmpty(message = "姓名不能为空")
    private String name;
    /** 银行卡号 */
//    @NotNull(message = "银行卡号不能为空")
//    @NotEmpty(message = "银行卡号不能为空")
    private String bankId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCell() {
        return cell;
    }
    public void setCell(String cell) {
        this.cell = cell;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    // 校验参数
    public void checkParams() throws BusinessException {
        ExceptionUtil.check(this);
    }
}
