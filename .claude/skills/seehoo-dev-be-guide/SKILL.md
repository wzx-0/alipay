---
name: seehoo-dev-be-guide
description: 后端开发规范指引，用于生成符合公司编码标准和技术基座要求的后端代码。当涉及 Java 后端开发、代码实现、技术选型、日志处理、异常设计、安全防护或性能优化时，必须使用此技能确保输出符合规范标准。
---

# 后端编码原则

## 优先级

**项目自有规范优先**。开始编码前，先检查工程目录下是否有自述规范文件（如 `readme.md`），如有则优先加载，并从中提取技术栈约束（如 Java 版本、ORM 框架等）作为编码基线。编码时必须使用与项目技术栈版本兼容的 API，不得使用高于项目指定版本的语言特性或标准库方法。项目规范与本通用规范冲突时，以项目规范为准；本规范仅在项目规范未覆盖时作为默认基线。

### 分层隔离

每一层有且只有一个职责。上层调用下层，禁止反向依赖或跳层。

### 工具方法归位

通用工具方法统一放入工具类；禁止在 Controller/Service/Entity 中放置可独立复用的工具方法。

### 实体操作归属，业务逻辑编排

所有 `*Wrapper`（QueryWrapper/LambdaQueryWrapper/UpdateWrapper 等）条件拼装必须在 Mapper 的 `default` 方法中完成，禁止在 Controller/Service/Util 等任何非 Mapper 层使用；Service 仅调用 Mapper 具名方法做流程编排和事务管理。

### 基础设施不可变

禁止修改、扩展或封装工具类、基础组件、防腐层等基础设施。确需改动或发现缺陷时，统一标记 TODO。

### 校验分离

格式不对（空值、长度、类型）在入口拒绝；格式对但业务不合规（状态不对、数量超限、无权操作）在业务层拒绝。

### 无实质不封装

只做参数转发、不做判断或组装的"透传方法"不需要存在。

### 操作有边界

查询带条件、批量有分页、写入考虑幂等。循环内的单条操作必须改为批量。禁止长事务——事务内不得包含外部 HTTP 调用、文件 IO 或耗时计算。

### 数据不重复查

同一方法内相同数据被查询超过一次时，缓存结果而非重复查询。

### 异常不吞

任何 catch 要么真正处理，要么明确向上抛。包装异常必须保留原始堆栈。异常信息不包含堆栈细节，堆栈由全局异常处理器统一输出。

### 日志只记决策点

记录关键分支和失败原因，不记流水账。敏感数据不进日志；循环内不打日志；同一件事不在多个层重复打。

### 安全是默认值

参数化查询防注入；用户传入的资源 ID 必须校验归属；外部数据反序列化前做类型白名单；配置不进源码；密钥不进代码和注释；资源用完必关。禁止使用弱加密算法，密码字段禁止明文存储。

### 常量定义
所有魔法值（数字、字符串、正则）必须定义为常量。常量使用UPPER_CASE_WITH_UNDERSCORES命名。

### 代码要简洁

方法短、参数少、不嵌套深。方法开头用卫语句提前 return，避免大段 if-else。去除冗余类型转换和不必要的中间变量。
样板代码用 Lombok：Entity/DTO/VO 使用 @Data/@Getter/@Setter，Controller/Service 使用 @Slf4j。禁止手写 getter/setter/toString/equals/hashCode 和手动创建 Logger。

### 注释规范

注释说明意图，不重复代码。类加用途，字段加含义，方法仅当 WHY 非显而易见时加注。

### 不写重复和不用的代码

相同逻辑出现两次就提取。没被调用的私有方法、没被使用的字段和导入、空方法体——统统删掉。

---

## 项目规范索引

本技能包含项目特定的框架约束，编码时必须同时遵守，详见 [references/framework-constraints.md](references/framework-constraints.md)。

其中：
- 实体类/Mapper/校验/定时任务/枚举/异常响应 → 见 [framework-constraints.md](references/framework-constraints.md)
- 通用工具类（Hutool/Json 转换/对象复制/JEXL/转换器/分页规范）→ 见 [common-utils.md](references/common-utils.md)
- 中台服务（影像/用户/流程/消息/基础服务-字典/省市区/序号生成）→ 见 `references/middle-services-client/` 下对应子文档
- 三方服务（三方服务对接）→ 见 [tpart-service-client-guide.md](references/tpart-service-client-guide.md)

> 新项目可替换此文件以适配自己的技术栈和组件库。
