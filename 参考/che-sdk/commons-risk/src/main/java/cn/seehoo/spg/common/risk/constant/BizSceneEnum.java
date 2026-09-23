package cn.seehoo.spg.common.risk.constant;

/**
 * 通用提报接口业务场景枚举
 * <p>对应接口文档 bizScene 字段，Array 类型，支持多场景并行提报</p>
 */
public enum BizSceneEnum {

    /**
     * 跨渠道拒绝拦截
     * <p>必传 channelCode（字典 D00510 渠道）、projectName（字典 D98013 项目模式）；
     * bizData 可选 tenantIdNo / guarantorIdNo</p>
     */
    CROSS_CHANNEL_REJECT("01", "跨渠道拒绝拦截"),

    /**
     * 金融专员黑名单
     * <p>bizData 必传 staffName / staffIdNo / staffIdType（后两者最大20字符，可能为脱敏数据）</p>
     */
    STAFF_BLACKLIST("02", "金融专员黑名单");

    private final String code;
    private final String desc;

    BizSceneEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
