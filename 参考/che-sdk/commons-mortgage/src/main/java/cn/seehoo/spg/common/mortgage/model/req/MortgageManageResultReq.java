package cn.seehoo.spg.common.mortgage.model.req;

import lombok.Data;

import java.util.List;

@Data
public class MortgageManageResultReq {
    /**
     * 业务编码
     */
    private String bizId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 抵押日期 YYYYMMDD
     */
    private String mortgageDate;
    /**
     * 抵押地址类别 1-车管所、2-警邮
     */
    private String mortgageAddType;

    // 需求变动增加的字段

    /***
     * 订单号
     */
    /*private String orderNo;
     */
    /**
     * 车架号
     *//*
    private String carVin;*/
    /**
     * 抵押省
     */
    private String mortgageProvinceCode;

    /**
     * 抵押省名称
     */
    private String mortgageProvinceName;
    /**
     * 抵押城市
     */
    private String mortgageCityCode;

    /**
     * 抵押城市名称
     */
    private String mortgageCityName;
    /**
     * 抵押权利证书编号
     */
    private String certificateNo;
    /**
     * 抵押登记机关
     */
    private String regOffice;

    /**
     * 登记日期
     */
    private String regDate;

    /**
     * 抵押登记日期
     */
    private String mortgageRegDate;

    /**
     * 附件列表
     */
    private List<FileInfoDto> files;

    @Data
    public static class FileInfoDto {

        /**
         * 附件名称
         */
        private String fileName;
        /**
         * 附件类型(附件小类)
         */
        private String fileType;
        /**
         * 附件地址
         */
        private String fileUrl;
    }
}
