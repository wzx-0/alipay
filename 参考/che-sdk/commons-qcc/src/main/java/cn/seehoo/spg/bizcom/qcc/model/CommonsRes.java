package cn.seehoo.spg.bizcom.qcc.model;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonsRes<T> {

    public static final String CODE_SUCC = "200";
    public static final String MESSAGE_SUCC = "【有效请求】查询成功";

    /**
     * 状态 VARCHAR2(10)
     */
    private String Status;

    /**
     * 消息 VARCHAR2(200)
     */
    private String Message;

    /**
     * 订单编号 VARCHAR2(50)
     */
    private String OrderNumber;

    /**
     * 返回结果 Result
     */
    private T Result;

    /**
     * 相应时间
     */
    private LocalDateTime resultTime = LocalDateTime.now();

    /**
     * 判断是否请求成功
     *
     * @return true为成功
     */
    public Boolean isSuccess() {
        if (CODE_SUCC.equals(this.Status)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
