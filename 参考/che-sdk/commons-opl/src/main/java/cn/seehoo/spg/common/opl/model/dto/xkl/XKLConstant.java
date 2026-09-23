package cn.seehoo.spg.common.opl.model.dto.xkl;


import java.util.Arrays;
import java.util.List;

public interface XKLConstant {

    /**
     * 无状态
     */
    String NONE = "0";

    /**
     * 成功
     */
    String SUCCESS = "1";

     /**
     * 失败
     */
    String FAIL = "2";

    /**
     * 处理中
     */
    String PROCESSING = "3";

    /**
     * 增信site终态集合
     */
    List<String> SITE_FINAL_STATUS = Arrays.asList(SUCCESS, FAIL);

}
