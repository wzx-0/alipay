# 消息中心 — MesExternalHelper

## 基本信息

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.MesExternalHelper` |
| 源码 | `business-service-plugins/middle-service-api-client` |
| 用途 | 站内消息发送 |

## 注入方式

```java
@Autowired
private MesExternalHelper mesExternalHelper;
```

## 使用示例

```java
import cn.seehoo.infra.plugin.dto.SendMsgDto;
import java.util.Arrays;

SendMsgDto dto = new SendMsgDto();
dto.setTemplateCode("approval_notice");
dto.setSerialNo("MSG20240001");
dto.setReceivers(Arrays.asList("1001", "1002"));
mesExternalHelper.sendMsg(dto);
```

## DTO 说明

| DTO 类 | 包路径 | 用途 |
|--------|--------|------|
| `SendMsgDto` | `cn.seehoo.infra.plugin.dto` | 消息发送请求（字段：templateCode、serialNo、params、receivers、from、async） |

> **更多方法**：查阅 `business-service-plugins/middle-service-api-client` 源码，已存在的方法直接调用，不存在的标记 TODO。
