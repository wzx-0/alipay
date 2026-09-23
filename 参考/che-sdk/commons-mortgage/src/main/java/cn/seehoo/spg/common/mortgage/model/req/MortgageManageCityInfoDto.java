package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxx
 * @date 2026/4/9 15:11
 */
@Data
public class MortgageManageCityInfoDto {
    private static final long serialVersionUID = 1L;
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
     * 上级城市编码
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
     * 下级信息
     */
    private List<MortgageManageCityInfoDto> mortgageManageCityInfoDtoList;

    public MortgageManageCityInfoDto(String cityCode, String cityName, String parentCityCode, String licensePrefix, String parentCityName) {
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.parentCityCode = parentCityCode;
        this.parentCityName = parentCityName;
        this.licensePrefix = licensePrefix;
        this.mortgageManageCityInfoDtoList = new ArrayList<>();
    }
}
