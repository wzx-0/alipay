# 设计规范

## 公共审计字段

每张表必含以下公共字段。

| 字段名           | 类型 | 默认值 | 说明 |
|---------------|------|--------|------|
| `is_deleted`  | varchar(1) | '0' | 逻辑删除：0-否，1-是 |
| `create_time` | datetime | CURRENT_TIMESTAMP(3) | 创建时间 |
| `update_time` | datetime | CURRENT_TIMESTAMP(3) | 更新时间|
| `create_id`   | bigint | NULL | 创建人ID |
| `update_id`   | bigint | NULL | 更新人ID |
| `dept_id`     | bigint | NULL | 部门ID |


## 数据类型

### MySQL 数据类型参考

| 类型 | 示例 | 说明 |
|------|------|------|
| bigint | `id bigint PK` | 大整数，主键常用 |
| int | `age int` | 整数 |
| smallint | `count smallint` | 小整数 |
| tinyint | `level tinyint` | 微整数 |
| decimal(p,s) | `amount decimal(15,2)` | 精确小数，p=总位数，s=小数位 |
| varchar(n) | `name varchar(100)` | 可变长度字符串 |
| char(n) | `code char(6)` | 定长字符串 |
| text | `content text` | 文本，最大 64KB |
| mediumtext | `article mediumtext` | 中长文本，最大 16MB |
| longtext | `bigdata longtext` | 超长文本，最大 4GB |
| date | `birth_date date` | 日期（YYYY-MM-DD） |
| datetime | `create_time datetime` | 日期时间 |
| datetime(3) | `create_time_ms datetime(3)` | 日期时间（毫秒） |
| datetime(6) | `create_time_us datetime(6)` | 日期时间（微秒） |

## 字段类型选择指南

| 场景 | 推荐类型 | 说明 |
|------|----------|------|
| 金额 | `decimal(15,2)` | 禁止使用 `float`/`double`(精度问题) |
| 利率/费率 | `decimal(5,2)` | 存储百分比值，备注描述需加 `%`(如"年利率 %") |
| 枚举/状态 | `varchar(3)` / `char(1)` | 多值枚举用 `varchar(3)`，两面性枚举用 `char(1)` |
| JSON 数据 | `text` | 在应用层解析，最大 64KB |
| 长文本 | `mediumtext` | 超过 64KB 使用，最大 16MB |

## 索引设计原则

- **最左前缀原则**:联合索引 `(a,b,c)` 可支持查询条件为 `a`、`a,b`、`a,b,c` 的场景
- **覆盖索引**:当 SELECT 的字段全部在索引中时，可避免回表查询
- **避免过度索引**:单表索引数建议 ≤ 5 个，过多索引会影响写入性能
- **索引字段选择**:区分度高、常用于 WHERE/ORDER BY/GROUP BY 的字段优先建立索引

## 禁止项

- **禁止**使用 `ON UPDATE CURRENT_TIMESTAMP`，更新必须由应用层控制

## 设计原则

- **范式基准**:满足第三范式(3NF)为起点
- **反范式设计**:为提升查询速度可保留冗余字段(如订单表冗余商品名称)
- **性能平衡**:范式越高表越碎，查询慢时，针对性增加冗余
- **大字段分离**:含有大字段(如 `text`、`mediumtext`)的明细表，应将大字段单独拆分为扩展表，与主业务字段分离，避免查询业务数据时加载不必要的大字段内容
- **附件存储**:**禁止**使用 `blob`、`clob` 或 `text`(Base64)将附件内容直接存储在数据库表中。附件应存储至文件系统或对象存储(如 OSS)，数据库表中仅保存文件路径或访问地址
- **附件/影像中心集成**: **禁止**设计独立的附件表、文件表、影像表。项目已有独立的附件影像中心提供统一的附件管理能力，业务表仅需存储附件关联标识（如 `file_ids varchar(500)` 存储逗号分隔的附件ID列表）
- **审批中心集成**: **禁止**设计独立的审批流程表、审批节点表、审批记录表。项目已有独立的审批中心提供统一的工作流审批能力，业务表仅需存储审批关联标识（如 `approval_id varchar(64)` 存储审批实例ID、`approval_status char(1)` 存储审批状态）
- **主从表设计**:对于涉及多条明细数据的业务操作(如订单、合同、审批单等)，必须采用"主表 + 明细表"的设计模式。主表存储业务操作的公共信息(如单号、批次号、状态、总金额、创建人等)，明细表存储每条业务数据的详细内容，通过外键或业务键关联主表
- **变更记录表**:对于支持多次发起变更、生效/取消操作导致字段更新行为不一致的业务场景，建议新增独立变更记录表（即使无需界面展示）。按实际需求设计字段，**需包含业务实体的不可变字段**，可变字段通过JSON存储变更前后值，支持字段级追溯和数据回滚
- **枚举字段**:字段值存字典小类编码（两面性枚举存 `'0'`/`'1'`，统一 `1` 正面，`0` 反面）；字段注释存字典标识-字典描述（如 `D10001-订单状态`）
- **主键类型**: 推荐使用 `bigint` 作为主键，**不自增**，由应用层自行设置（如雪花算法）
- **中间表设计**: N:N 关系必须通过中间表拆解为两个 1:N 关系。中间表命名 `tb_A_B`（如 `tb_user_role`）；使用独立 `id bigint PK`；含公共审计字段；对两个外键字段分别建索引以支持双向查询
- **日期时间类型**:**禁止**使用 `timestamp`，统一使用 `datetime`；根据具体功能逻辑，如果需要毫秒则用 `datetime(3)`

---

## 六、强制性约束

⚠️ **重要提示：** 本章节仅为快速索引，完整的强制性约束请务必阅读：
👉 **[mandatory-constraints.md](mandatory-constraints.md)**

### 6.1 审批中心集成（快速索引）
- ❌ 禁止生成任何审批相关表
- ✅ 业务表仅存 approval_id varchar(64)
- 🔴 详细规则见 mandatory-constraints.md

### 6.2 附件中心集成（快速索引）
- ❌ 禁止生成任何附件相关表
- ✅ 业务表仅存 file_ids varchar(500)
- 🔴 详细规则见 mandatory-constraints.md