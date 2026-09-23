# 流程中心 — ApprovalHelper

## 基本信息

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.ApprovalHelper` |
| 源码 | `business-service-plugins/middle-service-api-client` |
| 用途 | 审批记录查询、流程启动/取消、任务提交 |

## 注入方式

```java
@Autowired
private ApprovalHelper approvalHelper;
```

## 使用示例

### 启动流程

```java
import cn.seehoo.infra.plugin.dto.ProcessStartDto;

ProcessStartDto dto = new ProcessStartDto();
dto.setProcessKey("contract_approval");
dto.setBizId("CONTRACT20240001");

// 返回流程实例 ID
String piId = approvalHelper.startProcess(dto);
```

### 取消流程

```java
import cn.seehoo.infra.plugin.dto.ProcessCancelDto;

ProcessCancelDto dto = new ProcessCancelDto();
dto.setProcessId("PI-xxx-xxx");
approvalHelper.cancelProcess(dto);
```

### 提交任务

```java
import cn.seehoo.infra.plugin.dto.TaskSubmitDto;

TaskSubmitDto dto = new TaskSubmitDto();
dto.setTaskId("12345");
approvalHelper.submitTask(dto);
```

## DTO 说明

| DTO 类 | 包路径 | 用途 |
|--------|--------|------|
| `ProcessStartDto` | `cn.seehoo.infra.plugin.dto` | 流程启动 |
| `ProcessCancelDto` | `cn.seehoo.infra.plugin.dto` | 流程取消 |
| `TaskSubmitDto` | `cn.seehoo.infra.plugin.dto` | 任务提交 |
| `ApproveRecordParam` | `cn.seehoo.infra.plugin.dto` | 审批记录查询 |

> **更多方法**：查阅 `business-service-plugins/middle-service-api-client` 源码，已存在的方法直接调用，不存在的标记 TODO。

## ⚠️ 注意

禁止自建审批表/审批状态字段，审批流程统一通过 `ApprovalHelper` 调用。
