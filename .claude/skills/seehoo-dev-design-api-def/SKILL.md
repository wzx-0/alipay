---
name: seehoo-dev-design-api-def
description: |
  API 定义设计技能。用于创建 API 定义文档（Markdown + OpenAPI 3.0 JSON）。
  触发：用户提到"创建 API"、"设计接口"、"API 文档"、"接口定义"、"REST API"、"接口设计"、"出个接口文档"等。
---

# API 定义规范

## 工作流程

1. 确定需求 → 2. 生成 API 编号 → 3. 设计接口 → 4. 定义参数 → 5. 生成 OpenAPI → 6. 质量检查

**详细示例参考**：[references/api-definition.md](references/api-definition.md)

---

## 核心规范

### 基础信息
- **文件命名**：`需求编号-API-Definition.md`
- **API 编号**：`API类型简称 + 业务领域编码（2位）+ 业务模块编码（2位）+ API序号（3位）`
  - `A` — HTTP/RPC 接口、`M` — MQ 消费者、`J` — 定时 Job
  - 示例：`A0101001`、`J1011001`
  - **业务领域编码**和**各业务领域的模块编码**：参考上下文中提供的 `业务领域清单`
  - **API 序号起始值**：参考上下文中提供的 `API 定义清单`，查阅该业务领域下该类型已有的编号，确定序号起始值

### 请求规范
- 原则上遵循 RESTful 风格
- 考虑到我司客户（金融机构）特殊要求（安全加固、非 MVC 技术栈等），有以下特殊要求：
  - 仅允许 `GET` 和 `POST` 请求
  - 不使用路径传参方式（如 `/v1/api/10001/query`）
  - 入参 > 3 个 → 必须使用 `POST`
  - 含敏感信息 → 必须使用 `POST`
- 使用 `Body` 或 `Query（URL 参数）` 传参，禁止混合使用；注：`token`、`sign`、密钥不属于业务参数
- 文件导入接口使用 `POST + FormData`，文件字段统一为 `file`；文件导出接口使用 `POST`，按查询条件导出。

### 字段规范
- **类型**：`String`、`Number`、`Bool`、`Array`、`Object`
- **必填**：`Y`（必填）、`N`（非必填）、`C`（条件必填，需备注说明）
- **值约束**（按需）：长度规则、格式规则、字典标识
- 接口入参/反参中的数据库表主键 ID 字段统一定义为 `String`，禁止定义为 `Number`。
- 文件导入/导出接口必须定义 Excel 字段清单。

### 响应规范
- 统一结构：`code`、`message`、`data`、`success`、`timestamp`
- 涉及分页查询时，入参统一结构：`pageNo`、`pageSize`、`params`；响应 `data` 统一结构：`total`、`current`、`size`、`pages`、`records`
- 文件导入接口返回 `data` 为空的统一成功结构；文件导出接口返回文件流，不定义业务 `data`。
- `success=true（此时code=000000000/SY000000）` 表示成功，其他表示失败

```json
{"code": "SY000000", "message": "交易成功", "data": {}, "success": true, "timestamp": "1772775292343"}
```

### 输出文件
- Markdown 文档：`需求编号-API-Definition.md`
- OpenAPI 3.0：`需求编号-OpenAPI.json`（含 `openapi`、`info`、`paths`）

---

## 质量检查清单

输出前逐项检查：

- [ ] 文件命名正确（`需求编号-API-Definition.md`）
- [ ] API 编号格式正确
- [ ] 请求方法选择正确（GET/POST）
- [ ] 参数传递方式正确（Body/Query），未混合使用
- [ ] 响应结构符合统一规范
- [ ] 分页接口已定义 `pageNo`、`pageSize`、`params` 入参和 `total`、`current`、`size`、`pages`、`records` 出参
- [ ] 文件导入接口已定义 `POST + FormData`、`file` 字段和支持格式
- [ ] 文件导出接口已定义查询入参、文件流响应和文件格式
- [ ] 文件导入/导出接口已定义 Excel 字段清单
- [ ] 接口入参/反参中的数据库表主键 ID 字段已定义为 `String`，未定义为 `Number`
- [ ] 字段类型使用规范类型（String/Number/Bool/Array/Object）
- [ ] 是否必填使用 Y/N/C 标识
- [ ] 值约束填写完整
- [ ] OpenAPI 3.0 JSON 文件完整（`需求编号-OpenAPI.json`）
