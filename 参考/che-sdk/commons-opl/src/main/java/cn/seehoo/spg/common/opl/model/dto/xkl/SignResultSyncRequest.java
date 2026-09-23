package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignResultSyncRequest {

    public static final String SYNC_URL = "/api/v1/channel/contracts/_result";

    /**
     * 业务编号
     */
    private String bizId;

    private String action = "CONTRACT_SIGN_SUC";

    private String actionName = "合同签署成功";

    public SignResultSyncRequest(String bizId){
        this.bizId = bizId;
    }

}
