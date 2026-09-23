# REQ001 API Logic 逻辑图说明

> 需求编号: REQ001
> 业务模块: 汽车订阅支付宝对接
> 文档版本: v1.0
> 生成日期: 2026-09-22
> 上游产物: REQ001-API-Definition.md、REQ001-ER.dbml

---

## 一、产物清单

| 文件 | 对象 | 类型 |
| --- | --- | --- |
| api-logics/REQ001-A0101001-商品分页查询-Logic.mmd | A0101001 | 查询类 |
| api-logics/REQ001-A0102001-创建租赁订单-Logic.mmd | A0102001 | 操作类 |
| api-logics/REQ001-A0102002-订单分页查询-Logic.mmd | A0102002 | 查询类 |
| api-logics/REQ001-A0102003-订单详情查询-Logic.mmd | A0102003 | 查询类 |
| api-logics/REQ001-A0102004-订单履约同步-Logic.mmd | A0102004 | 操作类 |
| api-logics/REQ001-A0102005-订单收货确认-Logic.mmd | A0102005 | 操作类 |
| api-logics/REQ001-A0102006-订单完结-Logic.mmd | A0102006 | 操作类 |
| api-logics/REQ001-A0102007-订单关闭-Logic.mmd | A0102007 | 操作类 |
| api-logics/REQ001-A0102008-修改租期-Logic.mmd | A0102008 | 操作类 |
| api-logics/REQ001-A0103001-获取签约串-Logic.mmd | A0103001 | 操作类（只读代理，无本地写事务） |
| api-logics/REQ001-A0104001-风控咨询查询-Logic.mmd | A0104001 | 操作类（查询留痕） |
| api-logics/REQ001-A0104002-提交风控审核结论-Logic.mmd | A0104002 | 操作类 |
| api-logics/REQ001-A0105001-订单支付-Logic.mmd | A0105001 | 操作类 |
| api-logics/REQ001-A0105002-端外支付同步-Logic.mmd | A0105002 | 操作类 |
| api-logics/REQ001-A0105003-交易退款-Logic.mmd | A0105003 | 操作类 |
| api-logics/REQ001-A0105004-支付流水分页查询-Logic.mmd | A0105004 | 查询类 |
| api-logics/REQ001-A0107001-创建售后单-Logic.mmd | A0107001 | 操作类 |
| api-logics/REQ001-A0107002-售后单分页查询-Logic.mmd | A0107002 | 查询类 |
| api-logics/REQ001-A0107003-售后处理-Logic.mmd | A0107003 | 操作类 |
| api-logics/REQ001-A0108001-支付宝异步通知接收-Logic.mmd | A0108001 | 操作类（异步分发） |
| api-logics/REQ001-J0102001-订阅计划代扣任务-Logic.mmd | J0102001 | 定时任务（批处理） |
| api-logics/REQ001-J0108001-异步通知补偿重试任务-Logic.mmd | J0108001 | 定时任务（批处理） |

文件共 22 个 = 20 个 API + 2 个 Job（Job 为 API 定义中明确的后端批处理逻辑，一并设计）。

---

## 二、通用设计约定

1. **事务边界**：遵循"先落本地事实、后调支付宝、失败即补偿"模式。事务（TxBegin→TxCommit）内仅保留本地 DB 操作，所有支付宝外部调用一律放在事务提交之后；外部调用失败通过补偿事务恢复本地状态，不存在跨事务的长事务。
2. **幂等保障**：操作类接口统一使用订单/售后单维度分布式锁（AcquireLock）防并发重复提交；A0105003 退款另以后端生成的 `out_request_no` 唯一索引兜底幂等；A0108001 依赖 `tb_alipay_notify_record.notify_id` 唯一索引去重；A0104002 通过"已同步审核结论"状态校验防重复审核。
3. **补偿策略**：支付宝调用失败时，按业务语义分三类补偿——(a) 状态回滚类（履约/租期/售后同步：恢复本地原状态）；(b) 状态终止类（创单失败：本地订单置 CLOSED）；(c) 事实保留类（端外支付同步：本地收款事实保留，仅告警转人工/任务重试同步）。
4. **异步机制**：支付/代扣的最终结果以支付宝异步通知为准，由 A0108001 落库后经 MQ/线程池异步分发更新（pay_status、期次账单状态、签约冻结状态）；通知处理失败由 J0108001 每 5 分钟扫描重试，超过 5 次告警人工介入。
5. **缓存策略**：管理端分页列表（订单/支付流水/售后单）使用 30 秒短缓存；商品分页使用 60 秒缓存；订单详情为运营操作依据，实时性优先，不启用缓存。
6. **批量与流式**：所有列表查询的关联明细（SKU、费项）均按本页 ID 集合 BatchQuery 组装，无循环内逐条 DB 操作；J0102001/J0108001 均采用游标流式扫描 + 分批处理。
7. **异常处理**：图中仅绘制关键业务分支；DB 操作的技术性异常由全局异常处理器统一捕获响应（堆栈由全局处理器输出），不逐一绘制。

---

## 三、逐 API 事务边界与补偿说明

| API | 事务范围 | 外部调用（事务外） | 失败补偿 |
| --- | --- | --- | --- |
| A0102001 创建租赁订单 | Insert tb_rent_order + BatchInsert item/installment | rent.order.create | 本地订单置 CLOSED 并记录失败原因 |
| A0102004 订单履约同步 | Update tb_rent_order 状态与时间 | fulfillment.send | 恢复订单原状态，记录失败待重试 |
| A0102005 订单收货确认 | Update tb_rent_order 状态与时间 | fulfillment.receive | 恢复订单原状态，记录失败待重试 |
| A0102006 订单完结 | Update tb_rent_order 置 FINISHED + BatchUpdate 期次关闭 | fulfillment.finish | 恢复订单与期次状态；解冻/解约由支付宝自动执行并经 A0108001 同步 |
| A0102007 订单关闭 | Update tb_rent_order 置 CLOSED + BatchUpdate 期次关闭 | order.close | 恢复订单与期次状态；前置校验已支付费项全额退款完成 |
| A0102008 修改租期 | Update 租期时间 + BatchUpdate plan_pay_time | order.modify | 恢复原租期与原计划扣款时间 |
| A0103001 获取签约串 | 无本地写事务（只读） | order.sign | 直接返回失败，无补偿需求 |
| A0104001 风控咨询查询 | Insert tb_risk_audit 留痕 | risk.consult | 咨询失败直接返回；留痕失败返回错误 |
| A0104002 提交风控审核结论 | 事务1 Insert 审核记录（待同步）；事务2 Update 同步状态+订单状态 | fulfillment.approve 或 order.close | 审核记录保持待同步，告警转人工/任务重试 |
| A0105001 订单支付 | Insert 支付记录（处理中）+ BatchInsert 费项 + BatchUpdate 期次支付中 | order.pay | 支付记录置失败，期次恢复待支付；终态由 A0108001 确认 |
| A0105002 端外支付同步 | Insert 支付记录（成功）+ BatchInsert 费项 + BatchUpdate 期次已支付 | pay.sync | 端外收款事实保留，记录告警转人工/任务重试 |
| A0105003 交易退款 | 事务1 BatchInsert 退款记录（处理中）；事务2 Update 退款成功+累加已退金额 | alipay.trade.refund | 退款记录置失败；幂等键 out_request_no |
| A0107001 创建售后单 | Insert tb_rent_aftersale（处理中） | aftersale.create | 售后单置已取消并记录失败 |
| A0107003 售后处理 | Update tb_rent_aftersale 终态 | aftersale.confirm；取消类完结后内部调用 A0102007 关单 | confirm 失败回滚为处理中；关单失败保留售后终态并告警 |
| A0108001 异步通知接收 | Insert 通知记录（先落库快速应答） | 无（分发为异步内部处理） | 处理失败置 process_status=2，由 J0108001 重试 |

---

## 四、质量检查清单自检

**必检项（所有 API）**：

- [x] 语法正确可渲染；文件数（22）与 API 数（20）+ Job 数（2）一致
- [x] 含入参校验（Validate）、DB 操作（SELECT/Insert/Update/BatchInsert/BatchUpdate 均标明类型）、异常处理
- [x] 流程完整无孤立节点；每个判断节点均有失败/分流分支
- [x] 所有异常节点均连接 End；DB 操作均标明类型
- [x] 节点 ID 在同一文件内唯一

**按类型**：

- [x] 查询类：均含缓存策略节点（CacheCheck/CachePolicy）；分页查询以页容量上限 100 控制，关联明细 BatchQuery 组装，无逐条查询
- [x] 操作类：均含幂等检查（AcquireLock 分布式锁 / StatusCheck 状态校验 / notify_id 唯一索引）、事务边界（TxBegin→TxCommit/TxRollback）、字段生命周期（状态流转校验后更新）
- [x] 聚合场景（A0108001 分发、J0102001/J0108001 批处理）：并行分发分支、失败降级（记录失败转重试）、核心数据校验均已体现

**跨服务及通用**：

- [x] 跨系统/跨领域调用均按 `调用：服务名-API编号|API名称` 标记（双引号包裹），均有失败分支与补偿
- [x] 事务内无外部服务调用（支付宝调用均在 TxCommit 之后）
- [x] 无逐条处理：关联明细批量组装，Job 采用流式扫描 + 分批处理
- [x] 实时/异步/批处理方式明确：支付终态异步（A0108001 通知分发），A0108001 先落库快速应答后异步处理，代扣与通知重试为定时批处理
