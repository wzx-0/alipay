package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest {

    public static final String CANCEL_URL = "/api/credit/v1/close";

    /** 流程Id */
    private String processId;

    /** 系统Id */
    private String sysId;

}
