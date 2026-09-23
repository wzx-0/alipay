package cn.seehoo.common.unify.request;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class ImmediatelySendRequest {

    /**
     * 流水号
     */
    private String seqNo;

    /**
     * 子应用名称
     */
    private String appCode;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 消息类型 1:个推;2:站内信;3:短信;4:邮件
     */
    private Integer type;

    private Integer isAppPush;

    /**
     * 执行策略
     */
    private Integer way;

    /**
     * 消息名单集合
     */
    private List<MessageCMD> messageCMDList;

    /**
     * 消息名单
     */
    @Data
    public static class MessageCMD{

        /**
         * 用户编号
         */
        private String userId;

        /**
         * 消息类型 1:个推;2:站内信;3:短信;4:邮件
         */
        private String type;

        /**
         * 用户编号
         */
        private String appCode;

        /**
         * 消息标题
         */
        private String msgTitle;

        /**
         * 消息内容
         */
        private String content;

        /**
         * 内容摘要
         */
        private String contentDesc;

        /**
         * 跳转链接类型
         */
        private Integer jumpType;

        /**
         * 跳转链接
         */
        private String jumpLink;

        /**
         * 填充参数
         */
        private Map<String, Object> fillParams = Collections.emptyMap();

    }



}
