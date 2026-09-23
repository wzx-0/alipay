package cn.seehoo.spg.common.mortgage.model.res;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhangxx
 * @date 2026/4/8 9:30
 */
@Data
public class MortgageManageCityInfoRes {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 上级城市编码
     */
    private String parentCityCode;

    /**
     * 上级城市名称
     */
    private String parentCityName;

    /**
     * 发证机关
     */
    private String licenseOffice;

    /**
     * 车牌前缀
     */
    private String licensePrefix;

    /**
     * 是否启用 0:否，1是
     */
    private String status;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    private String logicDelete;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private String updateTime;
}
