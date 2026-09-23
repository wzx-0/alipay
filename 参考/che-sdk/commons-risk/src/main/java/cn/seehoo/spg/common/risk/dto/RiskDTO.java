package cn.seehoo.spg.common.risk.dto;

public class RiskDTO {

    private String status;
    private String code;
    private String msg;
    private RiskResultDto data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RiskResultDto getData() {
        return data;
    }

    public void setData(RiskResultDto data) {
        this.data = data;
    }
}
