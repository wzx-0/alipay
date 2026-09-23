---
name: seehoo-dev-design-data-model
description: |
  数据模型设计规范。触发场景:数据库设计、表结构设计、ER图、DDL、索引设计、建表。务必在用户提及任何数据库相关需求时使用此技能。
  
  ⚠️ 重要提示：必须首先阅读 references/mandatory-constraints.md 中的强制性约束！
  ⚠️ 强制性约束为最高优先级，无论 PRD/FRD 如何描述都必须严格遵守！
---

# 数据模型设计规范

## 工作流

1. **阅读强制性约束** → 先读 references/mandatory-constraints.md（必须先读）
2. **需求分析** → 识别实体、字段、关系
3. **设计 ER 图** → 按 DBML 语法编写
4. **生成 DDL** → 基于 ER 图转换为 SQL
5. **质量检查** → 使用各阶段清单验证，特别注意强制性约束检查

> 🔴 【重要】任何设计都必须满足 mandatory-constraints.md 中的所有约束
> 违反强制性约束的设计将被视为无效！

> ?? **何时读参考文档:**
> - 强制性约束 → [references/mandatory-constraints.md](references/mandatory-constraints.md) 🔴 必须先读！
> - DBML 语法 → [references/dbml-template.md](references/dbml-template.md)
> - DDL 模板 → [references/ddl-template.sql](references/ddl-template.sql)
> - 数据类型、字段类型、索引设计、设计原则、公共字段 → [references/advanced-rules.md](references/advanced-rules.md)

---

## 一、ER 图

### 产物

- **文件**:`{需求编号}-ER.dbml`，格式 DBML，数据库 MySQL 8

### 命名规范

- 表名：`tb_` 开头，`_` 分隔（如 `tb_order_item`）
- 主键：固定 `id bigint PK`，**不自增**，由应用层自行设置（如雪花算法）
- 字段：英文简称，`_` 分隔（如 `user_name`）
- 唯一索引：`uk_` 前缀（如 `uk_user_email`）
- 普通索引：`idx_` 前缀（如 `idx_order_status`）

### 表结构

- 所有表和字段必须有注释
- 每张表必含公共审计字段（类型和说明见 [references/advanced-rules.md](references/advanced-rules.md)）

### 检查清单

#### 🔴 强制性约束检查（必须 100% 通过，否则设计无效）
- [ ] 已完整阅读 references/mandatory-constraints.md
- [ ] 未生成任何包含 "approval"、"审批" 字样的表
- [ ] 未生成任何包含 "file"、"attachment"、"附件" 字样的表
- [ ] 未生成任何字典、省市区、行政区划相关表（如 tb_*_dict、tb_*_province 等）
- [ ] 即使 PRD/FRD 中有审批历史/附件管理/基础数据维护需求，也确认通过中心接口实现，不单独建表
- [ ] 业务表中仅存储 approval_id、file_ids、dict_code、province_code 等关联编码字段
- [ ] approval_id 字段类型为 varchar(64)
- [ ] file_ids 字段类型为 varchar(500)
- [ ] 字典/区划关联字段类型为 varchar(32) 或 varchar(64)

#### 常规检查
- [ ] 表和字段均有注释
- [ ] 公共审计字段完整
- [ ] 命名符合规范
- [ ] 数据类型正确
- [ ] 参考 [references/advanced-rules.md](references/advanced-rules.md) 和 [references/mandatory-constraints.md](references/mandatory-constraints.md) 

---

## 二、DDL

### 产物

- **文件**:`{需求编号}-DDL.sql`，格式 SQL，数据库 MySQL 8

### 生成规则

- 必须与 ER 图完全一致
- 不生成外键级联语句
- 字符集统一 `utf8mb4`，排序 `utf8mb4_general_ci`
- 时间字段禁止使用 `ON UPDATE CURRENT_TIMESTAMP`

> ?? **完整模板**:[references/ddl-template.sql](references/ddl-template.sql)

### 检查清单

- [ ] 与 ER 图一致
- [ ] 字符集 `utf8mb4`，排序 `utf8mb4_general_ci`
- [ ] 无外键级联
- [ ] 无 `ON UPDATE CURRENT_TIMESTAMP`
- [ ] 索引用 `KEY`/`UNIQUE KEY`(非 `INDEX`)
- [ ] 表和字段均有 `COMMENT`
