# 聚合类 API 模板

聚合类接口（编排多个服务/接口的 API）需体现：并行调用策略、结果聚合、部分失败降级、超时兜底。

```mermaid
flowchart TD
    Start([接收请求]) --> Validate{参数校验}
    Validate -->|失败| ParamError[返回参数错误]
    Validate -->|通过| CacheCheck{查询缓存}

    %% 缓存策略
    CacheCheck -->|命中| HitCache[返回缓存数据]
    CacheCheck -->|未命中| BuildTasks[组装并行调用任务]

    %% 并行调用多个服务
    BuildTasks --> CallA["调用：用户服务-API001|获取基础信息"]
    BuildTasks --> CallB["调用：订单服务-API002|获取订单详情"]
    BuildTasks --> CallC["调用：商品服务-API003|获取关联商品"]

    %% 各调用结果处理
    CallA -->|成功| ResultA[获取A数据]
    CallA -->|失败| FallbackA[降级: 使用默认值或跳过]

    CallB -->|成功| ResultB[获取B数据]
    CallB -->|失败| FallbackB[降级: 标记部分不可用]

    CallC -->|成功| ResultC[获取C数据]
    CallC -->|失败| FallbackC[降级: 返回空集合]

    %% 聚合结果
    ResultA --> Aggregate[聚合数据]
    ResultB --> Aggregate
    ResultC --> Aggregate
    FallbackA --> Aggregate
    FallbackB --> Aggregate
    FallbackC --> Aggregate

    %% 聚合后处理
    Aggregate --> ComposeCheck{数据完整性校验}
    ComposeCheck -->|核心数据缺失| CoreError[返回核心数据不可用错误]
    ComposeCheck -->|通过| CacheWrite[写入缓存]
    CacheWrite --> BuildResp[构建响应]
    BuildResp --> Success[返回成功]

    %% 异常统一结束
    ParamError --> End([结束])
    HitCache --> End
    CoreError --> End
    Success --> End
```

## 要点说明

| 要点 | 模板体现位置 |
|------|------------|
| 并行调用 | `BuildTasks` 分支到多个 `NodeID["调用：服务名-API编号|API名称"]` |
| 降级策略 | 每个调用都有 `失败→Fallback` 分支 |
| 核心/非核心区分 | `ComposeCheck` 校验核心数据是否缺失 |
| 缓存策略 | `CacheCheck` → 命中返回 / `CacheWrite` → 聚合后写入 |
| 超时兜底 | 每个调用节点标注超时时间（如 `timeout: 3s`） |
