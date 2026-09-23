package cn.seehoo.spg.common.opl.model;

import cn.hutool.core.util.ObjUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OplCommonDTO {

    public static final String CHANNEL_ID = "channelId";

    public static final String TOPIC = "TP_OPL_Request";
    /**
     * 请求体
     */
    private Object body;
    /**
     * 请求path
     */
    private String apiIdent;

    /**
     * 请求头
     */
    private Map<String,String> header = new HashMap<>();


    public void setHeader(String key,String value){
        if (ObjUtil.isNull(header)){
            header = new HashMap<>();
        }
        header.put(key,value);
    }

    public String getChannelId(){
        if (ObjUtil.isNull(header)){
            return null;
        }
        return header.get(CHANNEL_ID);
    }

}
