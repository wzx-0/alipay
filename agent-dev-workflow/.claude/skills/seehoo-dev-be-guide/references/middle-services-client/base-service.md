# 基础服务（字典 / 省市区 / 序号生成）

> 所属依赖：`cn.seehoo.infra:middle-service-api-client`（通过 `CommonHelperAutoConfigure` 自动注入 Spring Bean）。中台底层依赖 `spg-base-api` 等模块，由 `commons-core` 间接引入。无需额外配置 `@EnableFeignClients`，`CommonHelperAutoConfigure` 已自动处理。

### 注入方式

本文件涉及的所有 Helper 注入方式如下：

```java
@Autowired
private DictExternalHelper dictExternalHelper;

@Autowired
private BizSeqNoHelper bizSeqNoHelper;
```

---

## 一、字典服务

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.DictExternalHelper` |
| 用途 | 字典数据查询 |
| 底层 Client | `DictionaryClient`（cn.seehoo.spg.base.feign） |

### 可用方法

```java
// 查询单个字典类型的字典 map，key=dictCode, value=dictName
Map<String, String> dictMap = dictExternalHelper.queryDictListMap("contract_type");

// 查询多个字典类型的字典 map，外层 key=dictTypeCode, 内层 key=dictCode, value=dictName
Map<String, Map<String, String>> dictMaps = dictExternalHelper.queryDictListMap(Arrays.asList("contract_type", "approve_status"));
```

### 使用示例

```java
// 单个字典
Map<String, String> statusMap = dictExternalHelper.queryDictListMap("approve_status");
String statusName = statusMap.get("2");  // "审批通过"

// 批量查询
Map<String, Map<String, String>> allDicts = dictExternalHelper.queryDictListMap(
    Arrays.asList("contract_type", "approve_status", "payment_status"));
```

### ⚠️ 注意

- 禁止硬编码字典值，所有字典查询统一通过 `DictExternalHelper` 调用

---

## 二、序号生成

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.BizSeqNoHelper` |
| 用途 | 业务流水号生成（基于序号规则引擎） |
| 底层 Client | `CodeRuleClient`（cn.seehoo.spg.base.feign） |
| 配置依赖 | `biz-common.biz-seq-no-mapping`（业务 key → 模板编号映射） |

### 可用方法

```java
// 生成序列号。code: 业务 key（需在 application.yml 中映射到中台配置的规则编码）
String gen(String code, Map<String, String> bizParam);
```

### 使用示例

```yaml
biz-common:
  biz-seq-no-mapping:
    your_biz_key: "TPL_XXX_001"    # 示例：需求规则为 JTGZ+3位自然数递增(JTGZ001)，TPL_XXX_001需在中台基础中心页面按实际规则创建
```

```java
String seqNo = bizSeqNoHelper.gen("your_biz_key", null);
// 带业务参数
Map<String, String> params = Map.of("date", "202405");
String seqNoWithParam = bizSeqNoHelper.gen("your_biz_key", params);
```

### ⚠️ 注意

- **必须在 `application.yml` 中配置映射**，否则 `gen()` 查不到模板编号，静默返回 `null`（不抛异常）
- 配置值需在中台基础中心页面预先创建对应序号规则，按实际编码填写

---

## 三、省市区服务

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.DictExternalHelper` |
| 用途 | 省市区数据查询 |
| 底层 Client | `AreaClient`（cn.seehoo.spg.base.feign） |

### 可用方法

```java
// 查询地区信息（注意：当前源码实现被注释，返回 null，待中台恢复后可用）
List<AreaVO> areas = dictExternalHelper.queryAreaByCodes(Arrays.asList("110000", "310000"));
```

### ⚠️ 注意

- 需要省市区查询时，优先使用 `queryAreaByCodes()` 方法
- 如该方法仍不可用或有其他省市区相关需求 → **先标记 TODO**，不编造 API

---

## 四、统一使用约束

| 约束 | 说明 |
|------|------|
| 禁止硬编码 | 字典值、流水号生成规则禁止写死在代码中 |
| 禁止重复造轮子 | 基础中心已提供的能力，业务模块不得自建表或自研逻辑 |
| 未定义方法 | 用到但本文档未列出的方法 → 查阅 `business-service-plugins/middle-service-api-client` 源码，已存在的方法直接调用，不存在的标记 TODO |
