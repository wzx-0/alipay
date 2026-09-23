package cn.seehoo.spg.common.signpl.client;

import java.util.HashMap;
import java.util.Map;

/**
 * 无担业务系统合同签署状态
 */
public class BizContStatusConstant {
    /** 1未签署 */
    public static final int UNSIGNED = 1;
    /** 3签署中 */
    public static final int SIGNING = 3;
    /** 2已签署 */
    public static final int SIGNED = 2;
    /** 5 待承租人签署 */
    public static final int LESSEE_SIGNING = 4;
    /** 6 待担保人签署 */
    public static final int GUARANTOR_SIGNING = 5;
    /** 7 待挂靠人签署 */
    public static final int AFFILIATE_SIGNING = 6;
    /** 8 待企业签署 */
    public static final int ENTERPRISE_SIGNING = 7;

    public static final Map<Integer,String> CONT_STATUS_MAP= new HashMap<Integer,String>();
    static {
        CONT_STATUS_MAP.put(UNSIGNED,"合同未签署");
        CONT_STATUS_MAP.put(SIGNING,"合同签署中");
        CONT_STATUS_MAP.put(SIGNED,"已签署");
        CONT_STATUS_MAP.put(LESSEE_SIGNING,"待承租人签署");
        CONT_STATUS_MAP.put(GUARANTOR_SIGNING,"待担保人签署");
        CONT_STATUS_MAP.put(AFFILIATE_SIGNING,"待挂靠人签署");
        CONT_STATUS_MAP.put(ENTERPRISE_SIGNING,"待企业签署");
    }
}
