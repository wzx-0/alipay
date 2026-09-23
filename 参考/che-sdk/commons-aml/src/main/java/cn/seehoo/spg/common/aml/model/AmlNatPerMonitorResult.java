package cn.seehoo.spg.common.aml.model;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.aml.constant.AmlPreRatingConstant;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caofei
 * @desc 自然人监控名单查询结果
 * @time 2025/9/23 14:52。
 */
public class AmlNatPerMonitorResult {

    private boolean highRisk;
    private String responseCode;
    private String responseText;
    private String timestamp;
    private String rateId;
    private CustResult custResult;

    public static class CustResult {

        /**
         * 流水号
         */
        private String id;

        /**
         * 客户名称
         */
        @JsonProperty("cust_name")
        private String custName;

        /**
         * 证件号码
         */
        @JsonProperty("cert_no")
        private String certNo;

        private List<ResResult> resList;

        public static class ResResult {
            /**
             * 监控名单类型 1反洗钱和反恐怖融资监控名单 2其他监测关注名单
             */
            private String restype;

            /**
             * 描述
             */
            private String resdes;

            /**
             * 监控名单是否命中 0未匹配 1匹配
             */
            private String rescode;


            public String getRestype() {
                return restype;
            }

            public void setRestype(String restype) {
                this.restype = restype;
            }

            public String getResdes() {
                return resdes;
            }

            public void setResdes(String resdes) {
                this.resdes = resdes;
            }

            public String getRescode() {
                return rescode;
            }

            public void setRescode(String rescode) {
                this.rescode = rescode;
            }

        }



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getCertNo() {
            return certNo;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public List<ResResult> getResList() {
            return resList;
        }

        public void setResList(List<ResResult> resList) {
            this.resList = resList;
        }
    }


    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public String getResponseText() {
        return responseText;
    }
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getRateId() {
        return rateId;
    }
    public void setRateId(String rateId) {
        this.rateId = rateId;
    }

    public CustResult getCustResult() {
        return custResult;
    }

    public void setCustResult(CustResult custResult) {
        this.custResult = custResult;
    }
    public boolean isHighRisk() {
        return highRisk;
    }
    public void setHighRisk(boolean highRisk) {
        this.highRisk = highRisk;
    }

    // 判断是否成功
    public boolean isSuccess() {
    	// 响应码为空，则失败
    	if (StrUtil.isEmpty(responseCode)) {
    		return false;
    	}
        return AmlPreRatingConstant.AML_RESPONSE_CODE.equals(responseCode);
    }
    // 缺省数据
    public static AmlNatPerMonitorResult defaultData(AmlDTO amlRtReq){
        AmlNatPerMonitorResult defaultAml = new AmlNatPerMonitorResult();
        defaultAml.setResponseCode(AmlPreRatingConstant.AML_RESPONSE_CODE);
        defaultAml.setResponseText("成功");
        defaultAml.setRateId("a636db1d1fa846ffa6a1af8d28278401");
        //sdefaultAml.setTimestamp(String.valueOf(Timestamp.from(Instant.now())));
        CustResult custResult = new CustResult();
        custResult.setCustName(amlRtReq.getCust_name());
        custResult.setCertNo(amlRtReq.getCert_no());
        custResult.setId(String.valueOf(Timestamp.from(Instant.now())));
        List<CustResult.ResResult> resList = new ArrayList<>();
        CustResult.ResResult resResult = new CustResult.ResResult();
        resResult.setRescode("0");
        resResult.setResdes("其他监测关注名单");
        resResult.setRestype("2");
        resList.add(resResult);
        custResult.setResList(resList);
        defaultAml.setCustResult(custResult);
        return defaultAml;
    }


}
