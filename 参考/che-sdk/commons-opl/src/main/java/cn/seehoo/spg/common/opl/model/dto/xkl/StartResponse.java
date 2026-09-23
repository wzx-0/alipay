package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.Data;

/**
 * 鑫快链发起流程 - 响应实体类
 * 只包含data下的业务字段
 */
@Data
public class StartResponse {

    /**
     * 本次发起的流程的flowId
     */
    private String processId;

    /**
     * H5链接
     */
    private String mobileLink;

    /**
     * 链接过期时间，精确到时分秒
     */
    private String linkExpirationTime;

}
