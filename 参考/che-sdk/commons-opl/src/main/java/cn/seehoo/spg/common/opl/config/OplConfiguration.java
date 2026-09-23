package cn.seehoo.spg.common.opl.config;

import lombok.Data;

@Data
public class OplConfiguration {

    private String serverUrl;

    private Integer timeout = 10000;
}
