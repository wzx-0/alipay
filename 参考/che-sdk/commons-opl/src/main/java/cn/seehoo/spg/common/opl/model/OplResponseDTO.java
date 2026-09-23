package cn.seehoo.spg.common.opl.model;

import lombok.Data;

@Data
public class OplResponseDTO<T> {

    private String code;

    /**
     * 响应消息
     */
    private String msg;

    private T data;

    public boolean isSuccess() {
        return "000000000".equals(code);
    }

}
