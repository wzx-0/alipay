-- DDL 模板
-- 数据库: MySQL 8
-- 字符集: utf8mb4 / utf8mb4_general_ci
-- 规范: 详见 SKILL.md

CREATE TABLE `tb_xxx` (
  `id` bigint NOT NULL COMMENT '主键 ID，应用层设置（如雪花算法）',
  `field_name` varchar(100) DEFAULT NULL COMMENT '字段注释',
  -- 公共字段（每张表必含）
  -- 优先从 项目 `<global文档>/数据模型公共字段.md` 中读取
  -- 如项目未定义，使用 Skill 内置默认：
  --   create_by       varchar(50)    DEFAULT NULL
  --   last_update_by  varchar(50)    DEFAULT NULL
  --   create_time     datetime(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
  --   last_update_time datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
  --   version         int           NOT NULL DEFAULT 0
  --   org_id          varchar(32)   DEFAULT NULL
  --   tenant_id       varchar(32)   DEFAULT NULL
  --   is_deleted      tinyint       NOT NULL DEFAULT 0

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_xxx` (`field_name`),
  KEY `idx_xxx` (`field1`, `field2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表注释';
