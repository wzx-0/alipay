# 查询类 API 模板

查询类接口需体现：缓存策略、特殊查询条件、流式查询。

```mermaid
flowchart TD
    Start([接收请求]) --> Validate{参数校验}
    Validate -->|失败| ParamError[返回参数错误]
    Validate -->|通过| QueryCondCheck{特殊查询条件校验}

    %% 特殊查询条件(如固定查询条件、权限过滤)
    QueryCondCheck -->|非法条件| CondError[返回条件错误]
    QueryCondCheck -->|通过| CacheCheck{查询缓存}

    %% 缓存策略
    CacheCheck -->|命中| HitCache[返回缓存数据]
    CacheCheck -->|未命中| DataSizeCheck{数据量评估}

    %% 大数据量处理
    DataSizeCheck -->|数据量小| NormalQuery[普通查询: SELECT * FROM 表 WHERE 条件]
    DataSizeCheck -->|数据量大| StreamQuery[流式查询: 开启游标/分批读取]

    %% 数据构建
    NormalQuery --> Transform[数据转换/过滤]
    StreamQuery --> Transform
    Transform --> BuildResult[构建响应]
    BuildResult --> Success[返回成功]

    %% 异常统一处理
    ParamError --> End([结束])
    CondError --> End
    HitCache --> End
    Success --> End
```

## 要点说明

| 要点 | 模板体现位置 |
|------|------------|
| 特殊查询条件 | `QueryCondCheck`(如固定查询条件、权限过滤) |
| 缓存策略 | `CacheCheck` → 命中直接返回 |
| 流式查询 | `DataSizeCheck` → `StreamQuery`(大数据量场景) |
| 字段明确 | 有过滤/排序/关联查询时需注明涉及的表 |
| 批量IO | `StreamQuery` 为流式批量读取，禁止逐条查询节点 |
