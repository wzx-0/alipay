-- ============================================================================
-- 汽车订阅支付宝对接 — DDL
-- 需求编号: REQ001 (PRD-REQ001-汽车订阅支付宝对接)
-- 数据库: MySQL 8
-- 字符集: utf8mb4 / utf8mb4_general_ci
-- 规范: 主键不自增由应用层生成; 无外键级联; 禁止 ON UPDATE CURRENT_TIMESTAMP
-- 对应 ER 图: REQ001-ER.dbml
-- ============================================================================

-- ----------------------------------------------------------------------------
-- M1 商品与商品库
-- ----------------------------------------------------------------------------

-- 租赁商品表，维护本地商品与支付宝商品库(out_item_id)映射及提报状态
CREATE TABLE `tb_rent_goods` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `goods_code` varchar(64) DEFAULT NULL COMMENT '商品编码，商家侧唯一',
  `goods_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `out_item_id` varchar(64) DEFAULT NULL COMMENT '支付宝商品库商品ID(out_item_id)',
  `item_type` varchar(32) DEFAULT NULL COMMENT '商品类型，汽车订阅固定 CAR_ITEM',
  `sale_price` decimal(15,2) DEFAULT NULL COMMENT '商品售价(元)',
  `item_value` decimal(15,2) DEFAULT NULL COMMENT '商品价值(元)',
  `duration_days` int DEFAULT NULL COMMENT '标准租期(天)',
  `file_ids` varchar(500) DEFAULT NULL COMMENT '提报素材附件ID，多个逗号分隔，关联附件影像中心（商品提报封面图/详情长图）',
  `report_status` varchar(3) DEFAULT NULL COMMENT 'D10012-商品库提报状态：0-未提报 1-提报中 2-已提报 3-提报失败',
  `goods_status` varchar(3) DEFAULT NULL COMMENT 'D10013-商品状态：0-下架 1-上架',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_goods_code` (`goods_code`),
  UNIQUE KEY `uk_out_item_id` (`out_item_id`),
  KEY `idx_goods_status` (`goods_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租赁商品表，维护本地商品与支付宝商品库(out_item_id)映射及提报状态';

-- 租赁商品SKU表，维护订阅套餐与支付宝商品库(out_sku_id)映射
CREATE TABLE `tb_rent_goods_sku` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `goods_id` bigint DEFAULT NULL COMMENT '商品ID，关联tb_rent_goods.id',
  `out_sku_id` varchar(64) DEFAULT NULL COMMENT '支付宝商品库SKU ID(out_sku_id)',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'SKU规格名称（订阅套餐描述）',
  `duration_days` int DEFAULT NULL COMMENT '租期时长(天)',
  `sale_price` decimal(15,2) DEFAULT NULL COMMENT 'SKU售价(元)',
  `sku_status` varchar(3) DEFAULT NULL COMMENT 'D10013-SKU状态：0-下架 1-上架',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_sku_id` (`out_sku_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租赁商品SKU表，维护订阅套餐与支付宝商品库(out_sku_id)映射';

-- ----------------------------------------------------------------------------
-- M2 订单模块（主表 + 商品明细 + 订阅计划期次）
-- ----------------------------------------------------------------------------

-- 租赁订单主表，承载订单状态机(与支付宝订单双向映射)及创单要素
CREATE TABLE `tb_rent_order` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `out_order_id` varchar(64) DEFAULT NULL COMMENT '商家侧订单号(out_order_id)，全局唯一',
  `alipay_order_id` varchar(64) DEFAULT NULL COMMENT '支付宝订单ID(order_id)',
  `origin_order_id` varchar(64) DEFAULT NULL COMMENT '原订阅订单号，续订(relet_info)/到期购买(buyout_info.origin_order_id)时传入',
  `order_type` varchar(32) DEFAULT NULL COMMENT '订单类型：RENT-首次订阅 RELET-续订 BUYOUT-到期购买',
  `order_status` varchar(32) DEFAULT NULL COMMENT '订单状态：CREATED-申请中 SIGNED-已签约 APPROVED-审核通过待发货 DELIVERED-已发货 RECEIVED-已收车(订阅中) RETURN_DELIVERED-已寄回 RETURN_RECEIVED-已收车验收 FINISHED-已完结 CLOSED-已关闭',
  `biz_identity` varchar(32) DEFAULT NULL COMMENT '订单业务身份，固定 CAR_SUBSCRIPTION',
  `source_channel` char(1) DEFAULT NULL COMMENT 'D10001-订单来源渠道：1-公域 0-私域',
  `title` varchar(200) DEFAULT NULL COMMENT '订单标题',
  `buyer_id` varchar(32) DEFAULT NULL COMMENT '买家支付宝userId',
  `buyer_open_id` varchar(64) DEFAULT NULL COMMENT '买家支付宝openId',
  `source_id` varchar(128) DEFAULT NULL COMMENT '下单前置判断(my.checkBeforeAddOrder)返回的sourceId',
  `order_price` decimal(15,2) DEFAULT NULL COMMENT '订单总价(元)=运费+增值服务费+Σ每期订阅金额',
  `deposit_price` decimal(15,2) DEFAULT NULL COMMENT '押金金额(元)，作为芝麻免押预授权冻结金额，需大于0',
  `freight` decimal(15,2) DEFAULT NULL COMMENT '运费(元)，无则不存',
  `additional_price` decimal(15,2) DEFAULT NULL COMMENT '增值服务费(元)，无则不存',
  `rent_start_time` datetime DEFAULT NULL COMMENT '租赁开始时间',
  `rent_end_time` datetime DEFAULT NULL COMMENT '租赁结束时间',
  `delivery_type` varchar(32) DEFAULT NULL COMMENT '配送方式，汽车场景固定 SELFPICK-自提',
  `shop_name` varchar(100) DEFAULT NULL COMMENT '自提门店名称',
  `shop_address` varchar(300) DEFAULT NULL COMMENT '自提门店地址',
  `shop_tel` varchar(32) DEFAULT NULL COMMENT '自提门店电话',
  `receiver_name` varchar(64) DEFAULT NULL COMMENT '默认收货人姓名',
  `receiver_tel` varchar(32) DEFAULT NULL COMMENT '默认收货人电话',
  `receiver_address` varchar(300) DEFAULT NULL COMMENT '默认收货详细地址',
  `protocol_name` varchar(100) DEFAULT NULL COMMENT '租赁协议名称',
  `protocol_path` varchar(300) DEFAULT NULL COMMENT '租赁协议页面路径',
  `detail_path` varchar(300) DEFAULT NULL COMMENT '商家订单详情页路径',
  `merchant_ext_info` varchar(2000) DEFAULT NULL COMMENT '商家自定义透传数据(创单入参merchantExtInfo)，JSON格式',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货(交车)时间',
  `receive_time` datetime DEFAULT NULL COMMENT '用户确认收车时间',
  `return_send_time` datetime DEFAULT NULL COMMENT '用户发起还车时间',
  `return_receive_time` datetime DEFAULT NULL COMMENT '商家验收收车时间',
  `finish_time` datetime DEFAULT NULL COMMENT '订单完结时间',
  `finish_status` varchar(32) DEFAULT NULL COMMENT '完结方式：USER_RETURNED-到期归还 USER_RETURNED_IN_ADVANCE-提前归还 OTHER-其他',
  `close_reason_code` varchar(32) DEFAULT NULL COMMENT '关单原因编码',
  `close_reason_desc` varchar(200) DEFAULT NULL COMMENT '关单原因描述',
  `close_time` datetime DEFAULT NULL COMMENT '关单时间',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_order_id` (`out_order_id`),
  UNIQUE KEY `uk_alipay_order_id` (`alipay_order_id`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_origin_order_id` (`origin_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租赁订单主表，承载订单状态机(与支付宝订单双向映射)及创单要素';

-- 订单商品明细表，存创单时商品快照(item_infos)，汽车订阅限定1项
CREATE TABLE `tb_rent_order_item` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id',
  `item_type` varchar(32) DEFAULT NULL COMMENT '商品类型，汽车订阅固定 CAR_ITEM',
  `out_item_id` varchar(64) DEFAULT NULL COMMENT '商家侧商品ID',
  `out_sku_id` varchar(64) DEFAULT NULL COMMENT '商家侧SKU ID',
  `item_name` varchar(200) DEFAULT NULL COMMENT '商品名称（创单快照冗余）',
  `item_description` varchar(500) DEFAULT NULL COMMENT '商品描述',
  `sale_price` decimal(15,2) DEFAULT NULL COMMENT '商品售价(元)',
  `item_value` decimal(15,2) DEFAULT NULL COMMENT '商品价值(元)',
  `item_cnt` int DEFAULT NULL COMMENT '购买数量',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_out_item_id` (`out_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单商品明细表，存创单时商品快照(item_infos)，汽车订阅限定1项';

-- 订阅计划期次表，存rent_plan_info.installments逐期计划，作为代扣调度与账单核对的依据
CREATE TABLE `tb_rent_order_installment` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id',
  `installment_no` int DEFAULT NULL COMMENT '期号，从1开始递增',
  `installment_price` decimal(15,2) DEFAULT NULL COMMENT '当期订阅金额(元)，除首期尾期外金额需一致',
  `plan_pay_time` datetime DEFAULT NULL COMMENT '当期计划扣款时间，第2期及之后需到达此时间方可发起代扣',
  `buyout_price` decimal(15,2) DEFAULT NULL COMMENT '到期购买金额(元)，仅最后一期传入',
  `bill_status` varchar(3) DEFAULT NULL COMMENT 'D10002-期次账单状态：0-待支付 1-支付中 2-已支付 3-支付失败 4-已关闭',
  `paid_amount` decimal(15,2) DEFAULT NULL COMMENT '已付金额(元)',
  `actual_pay_time` datetime DEFAULT NULL COMMENT '实际支付完成时间',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_installment` (`order_id`, `installment_no`),
  KEY `idx_plan_pay_time` (`plan_pay_time`),
  KEY `idx_bill_status` (`bill_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订阅计划期次表，存rent_plan_info.installments逐期计划，作为代扣调度与账单核对的依据';

-- ----------------------------------------------------------------------------
-- M3 签约与芝麻免押
-- ----------------------------------------------------------------------------

-- 订单签约信息表，存芝麻免押预授权冻结与代扣协议签约信息，与订单1:1
CREATE TABLE `tb_rent_order_sign` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id，1:1',
  `sign_status` varchar(3) DEFAULT NULL COMMENT 'D10003-签约状态：0-未签约 1-已签约 2-已解约',
  `zm_service_id` varchar(64) DEFAULT NULL COMMENT '芝麻信用服务ID(zm_service_id)，由BD提供',
  `category_id` varchar(64) DEFAULT NULL COMMENT '芝麻信用品类ID(category_id)，风控用',
  `sign_scene` varchar(32) DEFAULT NULL COMMENT '代扣签约场景(sign_scene)，需要代扣时固定 RENT_DEDUCT',
  `auth_category` varchar(64) DEFAULT NULL COMMENT '预授权类目，私域传 DEPOSIT_CAR_LEASING_PRI，公域按默认',
  `freeze_status` varchar(3) DEFAULT NULL COMMENT 'D10004-预授权冻结状态：0-未冻结 1-已冻结 2-已解冻 3-已转支付',
  `freeze_amount` decimal(15,2) DEFAULT NULL COMMENT '预授权冻结金额(元)',
  `freeze_order_no` varchar(64) DEFAULT NULL COMMENT '预授权冻结单号',
  `freeze_time` datetime DEFAULT NULL COMMENT '预授权冻结完成时间',
  `unfreeze_time` datetime DEFAULT NULL COMMENT '预授权解冻时间',
  `deduct_status` varchar(3) DEFAULT NULL COMMENT 'D10005-代扣协议状态：0-未签约 1-已签约 2-已解除',
  `deduct_agreement_no` varchar(64) DEFAULT NULL COMMENT '代扣协议编号',
  `sign_time` datetime DEFAULT NULL COMMENT '签约完成时间',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单签约信息表，存芝麻免押预授权冻结与代扣协议签约信息，与订单1:1';

-- ----------------------------------------------------------------------------
-- M4 风控审核（业务风控决策留痕，非OA审批流，审批中心不涉及）
-- ----------------------------------------------------------------------------

-- 风控审核记录表，商家风控审核决策留痕(风险等级T1~T10、审核结论)，审核动作通过支付宝接口(approve/close)同步
CREATE TABLE `tb_risk_audit` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id',
  `risk_level` varchar(3) DEFAULT NULL COMMENT '综合风险等级：T1~T10(T1-T4低风险 T5-T6中风险 T7-T8高风险 T9-T10极高风险)',
  `consult_result` varchar(2000) DEFAULT NULL COMMENT '风控咨询接口(risk.consult)返回关键信息，JSON格式',
  `audit_result` varchar(3) DEFAULT NULL COMMENT 'D10006-风控审核结论：1-通过 2-拒绝 3-转人工',
  `audit_user` varchar(50) DEFAULT NULL COMMENT '审核人，系统自动审核记SYSTEM',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `reject_reason` varchar(200) DEFAULT NULL COMMENT '拒绝原因',
  `sync_status` varchar(3) DEFAULT NULL COMMENT 'D10007-审核结果同步状态：0-待同步 1-已同步审核通过(approve) 2-已同步关单(close)',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_audit_result` (`audit_result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='风控审核记录表，商家风控审核决策留痕(风险等级T1~T10、审核结论)，审核动作通过支付宝接口(approve/close)同步';

-- ----------------------------------------------------------------------------
-- M5 支付与扣款（支付记录 + 费项明细 + 退款）
-- ----------------------------------------------------------------------------

-- 支付记录表，存每次订单支付(order.pay/pay.sync)流水，含代扣/预授权转支付/主动支付
CREATE TABLE `tb_rent_pay_record` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id',
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '商家侧支付单号(out_trade_no)，全局唯一',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '支付宝交易号(trade_no)',
  `pay_method` varchar(32) DEFAULT NULL COMMENT '支付方式：RENT_DEDUCT-代扣 PRE_AUTH-预授权转支付 JSAPI-主动支付',
  `pay_amount` decimal(15,2) DEFAULT NULL COMMENT '支付金额(元)',
  `pay_status` varchar(3) DEFAULT NULL COMMENT 'D10008-支付状态：0-处理中 1-成功 2-失败 3-已全额退款',
  `pay_channel` varchar(32) DEFAULT NULL COMMENT '收款渠道：ALIPAY-支付宝 OTHER-端外(微信/线下转账，经pay.sync同步)',
  `pay_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `notify_time` datetime DEFAULT NULL COMMENT '支付结果通知到达时间',
  `refund_amount` decimal(15,2) DEFAULT NULL COMMENT '已退款金额(元)，冗余便于余额核对',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_trade_no` (`out_trade_no`),
  UNIQUE KEY `uk_trade_no` (`trade_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_pay_status` (`pay_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付记录表，存每次订单支付(order.pay/pay.sync)流水，含代扣/预授权转支付/主动支付';

-- 支付费项明细表，存pay_items逐费项金额，关联支付记录与订阅期次
CREATE TABLE `tb_rent_pay_item` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `pay_record_id` bigint DEFAULT NULL COMMENT '支付记录ID，关联tb_rent_pay_record.id',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，冗余存储便于按期次核对',
  `fee_type` varchar(32) DEFAULT NULL COMMENT '费项类型：RENT-订阅金 INDEMNITY-赔付/违约金',
  `installment_no` int DEFAULT NULL COMMENT '期号，费项为订阅金时必传',
  `pay_amount` decimal(15,2) DEFAULT NULL COMMENT '费项支付金额(元)',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  KEY `idx_pay_record_id` (`pay_record_id`),
  KEY `idx_order_installment` (`order_id`, `installment_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付费项明细表，存pay_items逐费项金额，关联支付记录与订阅期次';

-- 退款记录表，存统一收单退款(alipay.trade.refund)流水，用于取消订单退已支付费项
CREATE TABLE `tb_rent_refund_record` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id',
  `pay_record_id` bigint DEFAULT NULL COMMENT '原支付记录ID，关联tb_rent_pay_record.id',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '原支付宝交易号(trade_no)',
  `out_request_no` varchar(64) DEFAULT NULL COMMENT '退款请求号(out_request_no)，幂等唯一',
  `refund_amount` decimal(15,2) DEFAULT NULL COMMENT '退款金额(元)',
  `refund_reason` varchar(200) DEFAULT NULL COMMENT '退款原因',
  `refund_status` varchar(3) DEFAULT NULL COMMENT 'D10009-退款状态：0-处理中 1-成功 2-失败',
  `refund_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_request_no` (`out_request_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_pay_record_id` (`pay_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='退款记录表，存统一收单退款(alipay.trade.refund)流水，用于取消订单退已支付费项';

-- ----------------------------------------------------------------------------
-- M7 售后
-- ----------------------------------------------------------------------------

-- 售后单表，存取消订单/赔付两类售后(aftersale.create/confirm)，赔付收取须在订单完结前
CREATE TABLE `tb_rent_aftersale` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `out_aftersale_id` varchar(64) DEFAULT NULL COMMENT '商家侧售后单号(out_aftersale_id)',
  `alipay_aftersale_id` varchar(64) DEFAULT NULL COMMENT '支付宝售后单号(aftersale_id)',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID，关联tb_rent_order.id',
  `aftersale_type` varchar(32) DEFAULT NULL COMMENT '售后类型：ORDER_CANCEL-取消订单 COMPENSATION-赔付',
  `reason_code` varchar(32) DEFAULT NULL COMMENT '售后原因编码：NO_NEED-不想要了 ITEM_DAMAGED-物损等，以支付宝文档为准',
  `reason_desc` varchar(200) DEFAULT NULL COMMENT '售后原因描述',
  `additional_desc` varchar(500) DEFAULT NULL COMMENT '补充说明(additional_description)',
  `file_ids` varchar(500) DEFAULT NULL COMMENT '凭证附件ID，多个逗号分隔，关联附件影像中心（售后凭证additional_media_list）',
  `pay_amount` decimal(15,2) DEFAULT NULL COMMENT '赔付金额(元)，赔付类售后收取的赔付金/违约金',
  `pay_status` varchar(3) DEFAULT NULL COMMENT 'D10010-赔付收取状态：0-待收取 1-已收取 2-免于收取',
  `aftersale_status` varchar(3) DEFAULT NULL COMMENT 'D10011-售后状态：0-处理中 1-已完结 2-已取消',
  `operation_type` varchar(32) DEFAULT NULL COMMENT '最近处理操作：USER_CANCEL_APPLY-用户取消申请 AFTERSALE_FINISH-售后完结',
  `finish_time` datetime DEFAULT NULL COMMENT '售后完结时间',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_out_aftersale_id` (`out_aftersale_id`),
  UNIQUE KEY `uk_alipay_aftersale_id` (`alipay_aftersale_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_aftersale_status` (`aftersale_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='售后单表，存取消订单/赔付两类售后(aftersale.create/confirm)，赔付收取须在订单完结前';

-- ----------------------------------------------------------------------------
-- 横切：统一异步通知网关
-- ----------------------------------------------------------------------------

-- 支付宝异步通知记录表，统一接收冻结/支付/售后/订单变更通知，支撑验签、notify_id幂等去重与失败重试
CREATE TABLE `tb_alipay_notify_record` (
  `id` bigint NOT NULL COMMENT '主键ID，应用层设置（如雪花算法）',
  `notify_id` varchar(64) DEFAULT NULL COMMENT '支付宝通知ID(notify_id)，幂等去重；无通知ID时按业务报文摘要生成',
  `msg_method` varchar(100) DEFAULT NULL COMMENT '消息方法名(msg_method)，如 alipay.commerce.rent.order.aftersale.notify',
  `msg_category` varchar(32) DEFAULT NULL COMMENT '通知类别：FUND_AUTH_FREEZE-预授权冻结结果 PAY-支付结果 AFTERSALE-售后消息 ORDER_CHANGED-订单变更',
  `app_id` varchar(64) DEFAULT NULL COMMENT '来源应用ID(app_id)',
  `biz_content` text COMMENT '通知报文原文(biz_content)，JSON格式',
  `sign_result` char(1) DEFAULT NULL COMMENT '验签结果：1-通过 0-失败',
  `process_status` varchar(3) DEFAULT NULL COMMENT 'D10014-处理状态：0-待处理 1-处理成功 2-处理失败',
  `process_time` datetime DEFAULT NULL COMMENT '业务处理完成时间',
  `retry_count` int DEFAULT NULL COMMENT '重试次数',
  `fail_reason` varchar(500) DEFAULT NULL COMMENT '失败原因',
  `is_deleted` varchar(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notify_id` (`notify_id`),
  KEY `idx_msg_method` (`msg_method`),
  KEY `idx_process_status` (`process_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付宝异步通知记录表，统一接收冻结/支付/售后/订单变更通知，支撑验签、notify_id幂等去重与失败重试';
