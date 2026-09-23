package cn.seehoo.spg.common.bhrc.req;

import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.exception.ExceptionUtil;
import lombok.Data;

/**
 * @author liusijia
 * @since 1.0
 */
@Data
public class TelThreeCheckReq {
    /** 身份证号 */
    private String certNo;
    /** 证件类型 */
    private String certType;
    /** 手机号 */
    private String mobile;
    /** 姓名 */
    private String name;
    /** 调用环节 */
    private String callStage;
    /** 合作方 */
    private String partnerName;
    /** 业务编号 */
    private String businessNo;
}
