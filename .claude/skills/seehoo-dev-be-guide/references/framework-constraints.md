# 框架约束

> 本文件定义项目特定的框架约束，编码时必须同时遵守。
> 新项目可整体替换此文件以适配自己的技术栈和组件库。

---

## 一、依赖引入

```xml
<!-- 必选 -->
<dependency><groupId>cn.seehoo.infra</groupId><artifactId>common-infra-extension</artifactId><version>1.0-SNAPSHOT</version></dependency>
<dependency><groupId>cn.seehoo.spg.commons</groupId><artifactId>commons-core</artifactId><version>2.0.5-SNAPSHOT</version></dependency>
<!-- 按需 -->
<dependency><groupId>cn.seehoo.infra</groupId><artifactId>middle-service-api-client</artifactId><version>1.0-SNAPSHOT</version></dependency>
<dependency><groupId>cn.seehoo.infra</groupId><artifactId>tpart-service-api-client</artifactId><version>1.0-SNAPSHOT</version></dependency>
```

---

## 二、实体类规范

- 必须继承 `cn.seehoo.infra.plugin.common.BaseEntity`（含 isDeleted、createTime、updateTime、createId、updateId、deptId）
- 禁止手动定义审计字段、禁止自建逻辑删除字段

```java
@Data
@TableName("tb_contract")
public class Contract extends BaseEntity {
    private String contractNo;
    private BigDecimal amount;
}
```

---

## 二补、DTO/VO 规范

- 接口入参/反参中的数据库表主键 ID 字段必须使用 `String` 类型。

---

## 三、Mapper 规范

- 必须继承 `cn.seehoo.infra.plugin.common.ExtendBaseMapper<T>`
- 批量操作使用 `saveBatch()` / `updateBatchById()`，**禁止循环单条操作**

```java
@Mapper
public interface ContractMapper extends ExtendBaseMapper<Contract> {}

// 批量：contractMapper.saveBatch(list); / contractMapper.updateBatchById(list);
```

---

## 四、中台服务

中台服务拆分为独立文档，代码生成时按需读取：

| 服务 | 文档路径 |
|------|---------|
| 影像中心 | `middle-services-client/files-center.md` |
| 用户中心 | `middle-services-client/user-center.md` |
| 流程中心 | `middle-services-client/approval-center.md` |
| 消息中心 | `middle-services-client/message-center.md` |
| 基础服务（字典/省市区/序号生成） | `middle-services-client/base-service.md` |

> **规则**：文档或源码中已有的方法直接生成调用；确实不存在的才标记 TODO，禁止全部标记 TODO；不编造 API。

---

## 五、三方服务

目前项目暂无三方服务对接，详见 [tpart-service-client-guide.md](tpart-service-client-guide.md)。

---

## 六、参数校验规范

| 注解 | 包路径 | 用途 |
|------|--------|------|
| `@DateTimeVal` | `cn.seehoo.infra.plugin.validation` | 日期格式 |
| `@DecimalVal` | `cn.seehoo.infra.plugin.validation` | 小数精度 |
| `@EnumVal` | `cn.seehoo.infra.plugin.validation` | 枚举值 |
| `@IdCardNo` | `cn.seehoo.infra.plugin.validation` | 身份证号 |

```java
@DecimalVal(precision = 2, message = "最多2位小数")
private BigDecimal amount;
```

---

## 七、定时任务规范

- 使用 `@Component` + `@XxlJob` 注解，**不需要继承** `XxlJobExecCommonHandler`
- Handler **不写具体业务逻辑**，仅编排调用 Service 层方法
- **不需要**打印日志、**不需要** try-catch —— 开始/结束/异常均由组件切面统一处理
- 配置：`xxl.job.enabled=true`，`adminAddresses` 填调度中心地址

```java
@Component
public class ContractJobHandler {
    @Resource
    private ContractService contractService;

    @XxlJob("contractExpireNotifyJob")
    public void execute() {
        contractService.handleExpiredContractNotify();
    }
}
```

---

## 八、枚举规范

- 统一枚举位置：`cn.seehoo.infra.plugin.enums.*`
- 常用枚举：`YesOrNoEnum`、`VehicleTypeEnum`、`VehicleStatusEnum`、`ApproveStatusEnum`
- 禁止自建已存在的枚举

---

## 九、通用工具类

详见 [common-utils.md](common-utils.md)，含 Hutool（字符串/日期）、Json 转换/对象复制/JEXL、分页规范、项目自有工具类、类型转换器。

---

## 十、统一异常与响应规范

### 10.1 错误码枚举

- 接口：`cn.seehoo.spg.commons.core.exception.BaseErrorCode`
- 命名：类名 `{模块}ErrorCode`，错误码 `{缩写}{3位序号}`（如 `EMP001`）
- **禁止**字符串常量、多模块共用、错误码重复

### 10.2 业务异常

- 包路径：`cn.seehoo.spg.commons.core.exception.BusinessException`
- 推荐：`throw new BusinessException(ModuleErrorCode.XXX);`
- 包装异常保留原始堆栈：`throw new BusinessException(code, msg, e);`
- **禁止**吞异常、循环内打异常日志

### 10.3 统一响应

- 包路径：`cn.seehoo.spg.commons.base.BaseResponse`
- 成功返回：`return BaseResponse.success(data);`
- **禁止** Controller 手动 catch 返回失败状态（全局异常处理器统一处理）

### 10.4 全局异常处理

项目通过 `ExceptionHandlerResolver` 统一处理，Controller 层**不需要**手动捕获异常。

---

## 十一、包扫描要求

启动类必须包含：`@SpringBootApplication(scanBasePackages = {"cn.seehoo.infra.plugin", "com.your.package"})`

---

## 十二、代码生成检查清单

| # | 检查项 | 状态 |
|---|--------|------|
| 1 | 实体类继承 BaseEntity | ☐ |
| 2 | Mapper 继承 ExtendBaseMapper | ☐ |
| 3 | 批量操作使用 saveBatch / updateBatchById | ☐ |
| 4 | 中台 Helper 调用正确（影像/用户/流程/消息/基础服务） | ☐ |
| 5 | 未硬编码字典值 | ☐ |
| 6 | 错误码实现 BaseErrorCode 接口 | ☐ |
| 7 | 异常使用 BusinessException + 错误码枚举 | ☐ |
| 8 | 响应使用 BaseResponse | ☐ |
| 9 | Controller 不手动 catch | ☐ |
| 10 | 异常不吞，保留堆栈 | ☐ |
| 11 | 对象复制使用 BeanUtils.mapper/mapperCols | ☐ |
| 12 | 分页入参 PageDto / 反参 CommonPage | ☐ |
| 13 | *Wrapper 条件拼装仅在 Mapper default 方法中，其他层无使用 | ☐ |
| 14 | 接口入参/反参主键 ID 使用 String 类型 | ☐ |
| 15 | 定时任务 Handler 不继承基类、不写业务逻辑、不打印日志、不 try-catch | ☐ |
