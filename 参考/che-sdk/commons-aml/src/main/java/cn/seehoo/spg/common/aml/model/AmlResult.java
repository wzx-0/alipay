package cn.seehoo.spg.common.aml.model;
import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.common.aml.constant.AmlPreRatingConstant;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author caofei
 * @desc 反洗钱预评级结果
 * @time 2025/9/23 14:52。
 */
public class AmlResult {
	private boolean highRisk;
    private String responseCode;
    private String responseText;
    private String timestamp;
    private String rateId;
    private AmlPreRatingResult custResult;

    public static class AmlPreRatingResult {
        /**
         * 客户预评级结果编号
         */
        private String rateId;

        /**
         * 客户预评级结果编号
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

        /**
         * 风险等级
         */
        @JsonProperty("cust_lvl")
        private String custLvl;

        /**
         * 说明
         */
        private String des;

        public String getRateId() {
            return rateId;
        }

        public void setRateId(String rateId) {
            this.rateId = rateId;
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

        public String getCustLvl() {
            return custLvl;
        }

        public void setCustLvl(String custLvl) {
            this.custLvl = custLvl;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
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
    public AmlPreRatingResult getCustResult() {
        return custResult;
    }
    public void setCustResult(AmlPreRatingResult custResult) {
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
    public static AmlResult defaultData(AmlDTO amlRtReq){
        AmlResult defaultAml = new AmlResult();
        defaultAml.setResponseCode(AmlPreRatingConstant.AML_RESPONSE_CODE);
        defaultAml.setHighRisk(false);
        defaultAml.setResponseText("成功");
        defaultAml.setRateId("a636db1d1fa846ffa6a1af8d28278401");
        //defaultAml.setTimestamp(String.valueOf(Timestamp.from(Instant.now())));
        AmlPreRatingResult amlPreRatingResult = new AmlPreRatingResult();
        amlPreRatingResult.setCustLvl("1001");
        amlPreRatingResult.setCustName(amlRtReq.getCust_name());
        amlPreRatingResult.setCertNo(amlRtReq.getCert_no());
        amlPreRatingResult.setId("a636db1d1fa846ffa6a1af8d28278401");
        amlPreRatingResult.setRateId("a636db1d1fa846ffa6a1af8d28278401");
        defaultAml.setCustResult(amlPreRatingResult);
        return defaultAml;
    }
}
