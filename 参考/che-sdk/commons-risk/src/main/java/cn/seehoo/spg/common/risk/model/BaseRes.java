package cn.seehoo.spg.common.risk.model;

import lombok.Data;

@Data
public class BaseRes {

    private String code;

    private String errorCode;

    private String message;

    public Boolean success() {
        return "200".equals(code);
    }

}
