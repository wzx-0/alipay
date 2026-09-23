# DBML 语法模板参考

## 完整示例

```dbml
// 表定义 - 表注释使用 [note: 'xxx']
Table tb_xxx [note: '表注释'] {
  id bigint PK [note: '主键 ID，应用层设置（如雪花算法）']
  field_name varchar(100) [note: '字段注释']

  // 公共字段（必须包含）
  // 优先从 <后端代码库根>/README.md 的 ## 数据库规范 章节读取
  // 如项目未定义，使用 Skill 内置默认字段：
  //   create_by         varchar(50)
  //   last_update_by    varchar(50)
  //   create_time       datetime(3) [default: `CURRENT_TIMESTAMP(3)`]
  //   last_update_time  datetime(3) [default: `CURRENT_TIMESTAMP(3)`]
  //   version           int [default: 0]
  //   org_id            varchar(32)
  //   tenant_id         varchar(32)
  //   is_deleted        tinyint [default: 0]

  // 索引定义 - 使用 indexes 块
  indexes {
    field_name [name: 'idx_field_name', note: '字段索引']
    unique_field [name: 'uk_unique_field', unique, note: '唯一索引']
    (field1, field2) [name: 'idx_field1_field2', note: '联合索引']
  }
}

// 表关系定义 - 使用 ref: 语法，行首 // 仅用于关系注释
// 1:N 关系
ref: tb_parent.id > tb_child.parent_id
// N:1 关系
ref: tb_child.parent_id > tb_parent.id
// 1:1 关系
ref: tb_a.id - tb_b.a_id
```

## 语法要点

### 表定义

```dbml
// 正确 ?? - 表注释使用 [note: 'xxx']
Table tb_xxx [note: '表注释'] { }

// 错误 ??
Table tb_xxx { }  // 缺少表注释

// 错误 ?? - note 不能作为独立语句
[note: '独立 note 语法']
```

### 字段定义

```dbml
// 正确 ?? - 字段注释使用 [note: 'xxx']
field_name varchar(100) [note: '字段注释']
id bigint PK [note: '主键 ID，应用层设置（如雪花算法）']

// 错误 ??
field_name varchar(100)  // 缺少字段注释
```

### 索引定义

```dbml
// 正确 ?? - 使用 indexes 块，必须放在 Table 内
Table tb_xxx [note: '表注释'] {
  id bigint PK [note: '主键 ID，应用层设置（如雪花算法）']
  name varchar(100) [note: '姓名']
  email varchar(200) [note: '邮箱']

  indexes {
    name [name: 'idx_name']
    email [name: 'uk_email', unique]           // ?? 唯一索引：unique 是 flag，无冒号无 true
    email [name: 'uk_email', unique, note: '邮箱唯一索引']  // ?? 带 note 的唯一索引
    (field1, field2) [name: 'idx_union']
  }
}

// 错误 ?? - 使用 SQL 语法
INDEX idx_name (field_name)
KEY idx_name (field_name)

// 错误 ?? - indexes 块单独定义在 Table 外
indexes {
  name [name: 'idx_name']
}

// 错误 ?? - unique 不是 key-value，不写 unique: true
email [name: 'uk_email', unique: true]
```

### 表关系定义

表之间的关系需标注关系类型（1:N、N:1、1:1、N:N），使用行首 `//` 注释。

```dbml
// 正确 ?? - 使用 ref: 语法，关系注释用行首 //
// 1:N 关系
ref: tb_a.id > tb_b.a_id
// N:1 关系
ref: tb_b.a_id > tb_a.id
// 1:1 关系
ref: tb_a.id - tb_b.a_id
// N:N 关系（通过中间表）
ref: tb_a.id > tb_a_b.a_id
ref: tb_b.id > tb_a_b.b_id

// 错误 ?? - 使用 SQL 语法
FOREIGN KEY (a_id) REFERENCES tb_a(id)

// 错误 ?? - ref 语句不支持 [note] 语法
ref: tb_a.id > tb_b.a_id [note: '关系注释']
```

