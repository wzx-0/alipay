package cn.seehoo.spg.bizcom.collection.dto;

/**
 * @author zhangxx
 * @date 2026/1/8 10:11
 */
public class CollectionRegionDTO {
    /** 省编码 */
    private String provinceKey;
    /** 省名称 */
    private String provinceName;
    /** 城市编码 */
    private String codeKey;
    /** 城市名称 */
    private String codeName;

    public String getProvinceKey() {
        return provinceKey;
    }
    public void setProvinceKey(String provinceKey) {
        this.provinceKey = provinceKey;
    }
    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public String getCodeKey() {
        return codeKey;
    }
    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }
    public String getCodeName() {
        return codeName;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
