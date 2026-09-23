package cn.seehoo.spg.common.aml.model;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.aml.constant.AmlPreRatingConstant;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caofei
 * @desc 反洗钱系统客户正式评级结果
 * @time 2025/9/23 14:52。
 */
public class AmlOfficialRatingResult {
    private boolean highRisk;
    private String responseCode;
    private String responseText;
    private String timestamp;

    private CustRlstDTO custRlstDTO;

    public static class CustRlstDTO {


        @JsonProperty("rslt_id")
        private String rsltId;


        @JsonProperty("cust_id")
        private String custId;

        /**
         * 评级岗位
         */
        @JsonProperty("post_id")
        private String postId;

        /**
         * 评级状态
         */
        @JsonProperty("rslt_sts")
        private String rsltSts;

        /**
         * 最终登记
         */
        @JsonProperty("curr_lvl")
        private String currLvl;


        public String getCurrLvl() {
            return currLvl;
        }

        public void setCurrLvl(String currLvl) {
            this.currLvl = currLvl;
        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getRsltId() {
            return rsltId;
        }

        public void setRsltId(String rsltId) {
            this.rsltId = rsltId;
        }

        public String getRsltSts() {
            return rsltSts;
        }

        public void setRsltSts(String rsltSts) {
            this.rsltSts = rsltSts;
        }
    }
    public boolean isHighRisk() {
        return highRisk;
    }
    public void setHighRisk(boolean highRisk) {
        this.highRisk = highRisk;
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
    public CustRlstDTO getCustRlstDTO() {
        return custRlstDTO;
    }

    public void setCustRlstDTO(CustRlstDTO custRlstDTO) {
        this.custRlstDTO = custRlstDTO;
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
    public static AmlOfficialRatingResult defaultData(){
        AmlOfficialRatingResult defaultAml = new AmlOfficialRatingResult();
        defaultAml.setResponseCode(AmlPreRatingConstant.AML_RESPONSE_CODE);
        defaultAml.setResponseText("成功");
        //defaultAml.setTimestamp(String.valueOf(Timestamp.from(Instant.now())));
        CustRlstDTO a = new CustRlstDTO();
        a.setRsltId("2021111");
        a.setCustId("000000005");
        a.setPostId("P9999");
        a.setRsltSts("6");
        a.setCurrLvl("1001");
        defaultAml.setCustRlstDTO(a);
        return defaultAml;
    }

}
