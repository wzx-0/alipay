-- ============================================================================
-- 测试数据：商品 + SKU 示例（前端联调用）
-- 前置：car_subscription-DDL.sql 已执行
-- ============================================================================

-- 商品示例（out_item_id/out_sku_id 为空表示尚未提报支付宝商品库；
-- 联调创单前请先在支付宝开放平台完成商品提报，回填 out_item_id/out_sku_id）
INSERT INTO `tb_rent_goods`
(`id`, `goods_code`, `goods_name`, `out_item_id`, `item_type`, `sale_price`, `item_value`, `duration_days`, `report_status`, `goods_status`)
VALUES
(1001, 'CAR-TEST-001', '测试新能源汽车A-订阅', NULL, 'CAR_ITEM', 3600.00, 120000.00, 365, '0', '1');

INSERT INTO `tb_rent_goods_sku`
(`id`, `goods_id`, `out_sku_id`, `sku_name`, `duration_days`, `sale_price`, `sku_status`)
VALUES
(2001, 1001, NULL, '月租套餐-12期', 365, 300.00, '1'),
(2002, 1001, NULL, '季租套餐-4期', 365, 900.00, '1');
