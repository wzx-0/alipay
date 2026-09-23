package cn.seehoo.spg.common.signpl.constant;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class SignPlConstant {
    /**
     * 签署人类型
     * 上游字典： 1-企业 2-个人
     * 签约平台： 1-个人 2-企业
     */
    public static final String SIGNER_PERSON = "1";
    public static final String SIGNER_ENTERPRISE = "2";
    /**
     * 客户角色
     * 上游字典:14-出租方（资金方）
     * 签约平台：3-资金方
     */
    public static final String UP_STREAM_ROLE_FUND = "14";
    public static final String SIGN_ROLE_FUND = "3";

    /**
     * 业务场景
     * 上游(业务场景) 字典D_CONT_036
     * 1-征信授权 2-合同签约 3-提前结清
     * 签约平台（场景主题） 字典D00371
     * 1征信授权、2合同签约 3合同签约（挂靠） 4.提前结清 11.资产生成结清材料-抵押
     */

    public static String sceneMapping(String scene){
        if(StrUtil.isBlank(scene)){
            return null;
        }
        Map<String, String> scenesMap = new HashMap<>();
        scenesMap.put("1","1");
        scenesMap.put("2","2");
        return scenesMap.getOrDefault(scenesMap.get(scene),scene);
    }
}
