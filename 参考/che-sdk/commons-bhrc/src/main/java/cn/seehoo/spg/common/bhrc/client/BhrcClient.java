package cn.seehoo.spg.common.bhrc.client;

import cn.seehoo.spg.common.bhrc.model.BhrcFourElementAuthDTO;

/**
 * @author caofei
 * @desc BHRCClient基类
 * @time 2025/9/25 14:12。
 */
public interface BhrcClient {
    /**
     * 公安四要素认证
     * @param fourElementAuth 参数
     * @return 认证结果 true 通过 false 失败
     */
    public boolean fourElementAuth(BhrcFourElementAuthDTO fourElementAuth);
}
