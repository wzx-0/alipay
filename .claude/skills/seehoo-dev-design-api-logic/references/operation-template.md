# 操作类 API 模板

操作类接口(创建/更新/删除)需体现：字段生命周期、事务边界、幂等保障、跨服务调用补偿。

## 节点 ID 规范

模板中的节点 ID（如 `Validate`、`BizCheck`）仅为示例占位符，**实际使用时必须替换为业务相关的唯一 ID**，且同一 ID 不得重复定义。

## 模板

```mermaid
flowchart TD
    Start([接收请求]) --> Validate{参数校验}
    Validate -->|失败| ParamError[返回参数错误]
    Validate -->|通过| IdempotentCheck{幂等检查}

    %% 幂等保障
    IdempotentCheck -->|已处理| ReturnCached[返回缓存结果]
    IdempotentCheck -->|未处理| BizCheck{业务校验}

    %% 业务校验与卡控
    BizCheck -->|失败| BizError[返回业务错误]
    BizCheck -->|通过| AcquireLock[获取分布式锁或数据库锁]
    AcquireLock -->|失败| LockError[返回锁定错误]
    AcquireLock -->|成功| TxBegin[开启事务]

    %% 事务内操作
    TxBegin --> QueryField{查询当前字段状态}
    QueryField --> StatusCheck{状态流转校验}
    StatusCheck -->|非法流转| StatusError[返回状态错误]
    StatusCheck -->|合法| FieldOp[更新字段]

    %% 领域间调用
    FieldOp --> NeedExtCall{需要调用其他领域?}
    NeedExtCall -->|是| CallExt["调用：订单服务-API001|创建订单API"]
    CallExt -->|失败| Compensate{执行补偿或回滚}
    Compensate --> RollbackField[撤销字段更新]
    RollbackField --> TxRollback[事务回滚]
    CallExt -->|成功| ContinueFlow[继续流程]
    NeedExtCall -->|否| ContinueFlow

    %% 事务提交
    ContinueFlow --> TxCommit[提交事务]
    TxCommit --> ReleaseLock[释放锁]
    ReleaseLock --> CacheResult[缓存幂等结果]
    CacheResult --> Success[返回成功]

    %% 异常统一结束
    ParamError --> EndNode([结束])
    BizError --> EndNode
    StatusError --> EndNode
    LockError --> EndNode
    TxRollback --> EndNode
    TxCommit --> EndNode
    ReturnCached --> EndNode
    Success --> EndNode
```

## 要点说明

| 要点 | 模板体现位置 |
|------|------------|
| 字段生命周期 | `QueryField`(来源) → `StatusCheck`(处理) → `FieldOp`(去向) |
| 事务边界 | `TxBegin` → `TxCommit` / `TxRollback`，避免大事务 |
| 幂等保障 | `IdempotentCheck` → 缓存结果 + `AcquireLock` 分布式锁 |
| 跨服务补偿 | `Compensate` → `RollbackField` → `TxRollback` |
| 领域间调用 | `NodeID["调用：服务名-API编号|API名称"]` 标准格式 |
| 批量IO | `FieldOp` 为批量操作节点，禁止循环内逐条IO |
