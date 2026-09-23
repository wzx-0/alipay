# REQ001 API Definition

> 需求编号: REQ001
> 业务模块: 汽车订阅支付宝对接
> 文档版本: v1.0
> 生成日期: 2026-09-22

**编号规则说明（前提假设，落地时需确认）**：

1. 项目暂未提供《业务领域清单》与既有《API 定义清单》，本文档约定：**业务领域编码 `01` = 汽车订阅（租赁）**，后续需在《业务领域清单》正式注册；API 序号自 `001` 起编。
2. 业务模块编码与 ER 图（`REQ001-ER.dbml`）模块编号保持一致：`01`-商品(M1)、`02`-订单(M2)、`03`-签约(M3)、`04`-风控(M4)、`05`-支付(M5)、`07`-售后(M7)、`08`-通知网关(横切)。
3. 除 `A0108001`（支付宝异步通知回调，响应遵循支付宝协议返回文本 `success`）外，所有接口响应均为统一结构 `{code, message, data, success, timestamp}`；各接口"响应参数"表仅描述 `data` 内容。
4. 金额字段类型统一为 `String`（单位元，保留 2 位小数，如 `"300.00"`），与支付宝接口金额格式零转译；数据库表主键 ID 字段在接口入参/反参中统一定义为 `String`。
5. 按安全规范，所有接口仅使用 `POST` + `Body` 传参（含敏感信息或入参 > 3 个）。

---

## API 编号分配

| API编号 | API名称 | 请求方式 | 用途 |
|---------|--------|---------|------|
| A0101001 | 商品分页查询 | POST | 分页查询租赁商品及其SKU（含支付宝商品库映射与提报状态） |
| A0102001 | 创建租赁订单 | POST | 小程序下单回调创建租赁订单（支持首次订阅/续订/到期购买） |
| A0102002 | 订单分页查询 | POST | 管理端分页查询租赁订单列表 |
| A0102003 | 订单详情查询 | POST | 查询订单详情（含商品明细、订阅计划期次、签约信息） |
| A0102004 | 订单履约同步 | POST | 同步交车/用户还车履约状态（fulfillment.send） |
| A0102005 | 订单收货确认 | POST | 确认用户收车/商家验收收车（fulfillment.receive） |
| A0102006 | 订单完结 | POST | 订单归还完结，触发预授权解冻与代扣协议解除（fulfillment.finish） |
| A0102007 | 订单关闭 | POST | 关闭未确认收货的订单（order.close） |
| A0102008 | 修改租期 | POST | 修改租赁开始结束时间及计划扣款时间（order.modify） |
| A0103001 | 获取签约串 | POST | 获取签约字符串，供前端再次唤起芝麻免押受理台 |
| A0104001 | 风控咨询查询 | POST | 查询综合风险等级（risk.consult，T1~T10） |
| A0104002 | 提交风控审核结论 | POST | 提交商家风控审核结论，同步approve/关单至支付宝 |
| A0105001 | 订单支付 | POST | 发起订单支付（代扣/预授权转支付/JSAPI），JSAPI返回tradeNo |
| A0105002 | 端外支付同步 | POST | 同步微信/线下转账等端外已支付费项（pay.sync） |
| A0105003 | 交易退款 | POST | 对已支付费项发起退款（alipay.trade.refund） |
| A0105004 | 支付流水分页查询 | POST | 分页查询支付流水及费项明细 |
| A0107001 | 创建售后单 | POST | 创建取消订单/赔付类售后单（aftersale.create） |
| A0107002 | 售后单分页查询 | POST | 管理端分页查询售后单列表 |
| A0107003 | 售后处理 | POST | 同步售后终态：用户取消申请/售后完结（aftersale.confirm） |
| A0108001 | 支付宝异步通知接收 | POST | 统一接收支付宝冻结/支付/售后/订单变更异步通知 |

## Job 编号分配

| Job编号 | Job名称 | 触发方式 | 用途 |
|---------|--------|---------|------|
| J0102001 | 订阅计划代扣任务 | Cron：每小时整点 | 扫描到达计划扣款时间且待支付的期次，自动发起代扣 |
| J0108001 | 异步通知补偿重试任务 | Cron：每5分钟 | 扫描处理失败的支付宝异步通知记录并重新处理 |

---

## 接口定义

### A0101001 商品分页查询

**业务概述**: 分页查询本地租赁商品及其SKU列表，含支付宝商品库映射ID与提报状态，供管理端与私域自建商品页使用。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/goods/page` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| pageNo | 页码 | Number | Y | 最小值：1 | |
| pageSize | 每页条数 | Number | Y | 长度：1-100 | |
| params | 查询条件 | Object | Y | | 条件均为非必填 |
| ··goodsName | 商品名称 | String | N | 长度：1-200 | 模糊匹配 |
| ··goodsStatus | 商品状态 | String | N | 枚举：0/1 | 字典：D10013，0-下架 1-上架 |
| ··reportStatus | 提报状态 | String | N | 枚举：0/1/2/3 | 字典：D10012，0-未提报 1-提报中 2-已提报 3-提报失败 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| total | 总条数 | Number | Y | | |
| current | 当前页码 | Number | Y | | |
| size | 每页条数 | Number | Y | | |
| pages | 总页数 | Number | Y | | |
| records | 商品列表 | Array | Y | | 可为空数组 |
| ··id | 商品ID | String | Y | 格式：雪花ID | |
| ··goodsCode | 商品编码 | String | Y | 长度：1-64 | 商家侧唯一 |
| ··goodsName | 商品名称 | String | Y | 长度：1-200 | |
| ··outItemId | 支付宝商品库ID | String | N | 长度：1-64 | 未提报时为空 |
| ··salePrice | 商品售价 | String | Y | 精度：2位小数 | 单位：元 |
| ··itemValue | 商品价值 | String | N | 精度：2位小数 | 单位：元 |
| ··durationDays | 标准租期 | Number | N | 最小值：1 | 单位：天 |
| ··reportStatus | 提报状态 | String | Y | 枚举：0/1/2/3 | 字典：D10012 |
| ··goodsStatus | 商品状态 | String | Y | 枚举：0/1 | 字典：D10013 |
| ··skuList | SKU列表 | Array | Y | | |
| ····id | SKU ID | String | Y | 格式：雪花ID | |
| ····outSkuId | 支付宝商品库SKU ID | String | N | 长度：1-64 | |
| ····skuName | SKU名称 | String | Y | 长度：1-200 | 订阅套餐描述 |
| ····durationDays | 租期时长 | Number | Y | 最小值：1 | 单位：天 |
| ····salePrice | SKU售价 | String | Y | 精度：2位小数 | 单位：元 |
| ····skuStatus | SKU状态 | String | Y | 枚举：0/1 | 字典：D10013 |

**请求示例**:

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "params": {
    "goodsStatus": "1"
  }
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "total": 1,
    "current": 1,
    "size": 10,
    "pages": 1,
    "records": [
      {
        "id": "1823456789012345678",
        "goodsCode": "CAR_SUB_001",
        "goodsName": "某品牌Model Y 订阅",
        "outItemId": "12345",
        "salePrice": "400.00",
        "itemValue": "260000.00",
        "durationDays": 365,
        "reportStatus": "2",
        "goodsStatus": "1",
        "skuList": [
          {
            "id": "1823456789012345679",
            "outSkuId": "34567576565656",
            "skuName": "年租套餐",
            "durationDays": 365,
            "salePrice": "400.00",
            "skuStatus": "1"
          }
        ]
      }
    ]
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102001 创建租赁订单

**业务概述**: 小程序下单回调（插件提交订单/私域确认订单页）调用，完成订单前置校验后调用支付宝创建租赁订单，返回双边订单号及订单详情页路径；续订（RELET）与到期购买（BUYOUT）复用本接口。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/create` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| orderType | 订单类型 | String | Y | 枚举：RENT/RELET/BUYOUT | 首次订阅/续订/到期购买 |
| sourceId | 下单前置凭证 | String | Y | 长度：1-128 | 前端 my.checkBeforeAddOrder 返回的 sourceId |
| buyerOpenId | 买家openId | String | Y | 长度：1-64 | 支付宝买家 open_id |
| goodsId | 商品ID | String | C | 格式：雪花ID | orderType=RENT 时必填 |
| skuId | SKU ID | String | C | 格式：雪花ID | orderType=RENT 时必填 |
| quantity | 购买数量 | Number | C | 长度：1-99 | orderType=RENT 时必填 |
| originOrderId | 原订阅订单号 | String | C | 长度：1-64 | orderType=RELET/BUYOUT 时必填 |
| depositPrice | 押金金额 | String | C | 精度：2位小数，大于0 | orderType=RENT 时必填，作为芝麻免押预授权冻结金额 |
| freight | 运费 | String | N | 精度：2位小数 | 无运费不传 |
| additionalPrice | 增值服务费 | String | N | 精度：2位小数 | 无则不传 |
| rentPlanInfo | 订阅计划 | Object | C | | orderType=RENT 时必填 |
| ··rentStartTime | 租赁开始时间 | String | C | 格式：yyyy-MM-dd HH:mm:ss | rentPlanInfo 传入时必填 |
| ··rentEndTime | 租赁结束时间 | String | C | 格式：yyyy-MM-dd HH:mm:ss | rentPlanInfo 传入时必填 |
| ··installments | 期次计划 | Array | C | 长度：1-36 | rentPlanInfo 传入时必填，最长36期 |
| ····installmentNo | 期号 | Number | C | 最小值：1 | 从1开始递增 |
| ····installmentPrice | 当期订阅金额 | String | C | 精度：2位小数 | 除首期、尾期外金额需一致 |
| ····planPayTime | 计划扣款时间 | String | C | 格式：yyyy-MM-dd HH:mm:ss | 第2期及之后须到达此时间方可代扣 |
| ····buyoutPrice | 到期购买金额 | String | N | 精度：2位小数 | 仅最后一期传入 |
| shopInfo | 自提门店信息 | Object | N | | 配送方式固定 SELFPICK |
| ··shopName | 门店名称 | String | N | 长度：1-100 | |
| ··shopAddress | 门店地址 | String | N | 长度：1-300 | |
| ··shopTel | 门店电话 | String | N | 格式：手机号/座机号 | |
| receiverInfo | 收货人信息 | Object | N | | |
| ··receiverName | 收货人姓名 | String | N | 长度：1-64 | |
| ··receiverTel | 收货人电话 | String | N | 格式：手机号11位 | |
| ··detailedAddress | 详细地址 | String | N | 长度：1-300 | |
| merchantExtInfo | 商家透传数据 | Object | N | | 原样透传至支付宝创单 merchantExtInfo |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| alipayOrderId | 支付宝订单ID | String | Y | 长度：1-64 | order_id |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | out_order_id |
| path | 订单详情页路径 | String | Y | 长度：1-300 | 支付完成后前端跳转地址 |

**请求示例**:

```json
{
  "orderType": "RENT",
  "sourceId": "MjAfSVNfTlVMA==",
  "buyerOpenId": "074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
  "goodsId": "1823456789012345678",
  "skuId": "1823456789012345679",
  "quantity": 1,
  "depositPrice": "4000.00",
  "rentPlanInfo": {
    "rentStartTime": "2026-10-15 00:00:00",
    "rentEndTime": "2027-10-14 00:00:00",
    "installments": [
      {
        "installmentNo": 1,
        "installmentPrice": "400.00",
        "planPayTime": "2026-10-15 00:00:00"
      },
      {
        "installmentNo": 2,
        "installmentPrice": "400.00",
        "planPayTime": "2026-11-15 00:00:00",
        "buyoutPrice": "260000.00"
      }
    ]
  },
  "shopInfo": {
    "shopName": "某品牌体验中心",
    "shopAddress": "某市某区某路100号",
    "shopTel": "0571-88888888"
  }
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "alipayOrderId": "2026092221001004720200028594",
    "outOrderId": "6823789339978248",
    "path": "/pages/orderDetail/index?orderId=6823789339978248"
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102002 订单分页查询

**业务概述**: 管理端按条件分页查询租赁订单列表。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/page` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| pageNo | 页码 | Number | Y | 最小值：1 | |
| pageSize | 每页条数 | Number | Y | 长度：1-100 | |
| params | 查询条件 | Object | Y | | 条件均为非必填 |
| ··outOrderId | 商家侧订单号 | String | N | 长度：1-64 | |
| ··alipayOrderId | 支付宝订单ID | String | N | 长度：1-64 | |
| ··orderType | 订单类型 | String | N | 枚举：RENT/RELET/BUYOUT | |
| ··orderStatus | 订单状态 | String | N | 枚举：CREATED/SIGNED/APPROVED/DELIVERED/RECEIVED/RETURN_DELIVERED/RETURN_RECEIVED/FINISHED/CLOSED | |
| ··sourceChannel | 来源渠道 | String | N | 枚举：0/1 | 字典：D10001，1-公域 0-私域 |
| ··buyerId | 买家支付宝userId | String | N | 长度：1-32 | |
| ··createTimeBegin | 创建时间起 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··createTimeEnd | 创建时间止 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| total | 总条数 | Number | Y | | |
| current | 当前页码 | Number | Y | | |
| size | 每页条数 | Number | Y | | |
| pages | 总页数 | Number | Y | | |
| records | 订单列表 | Array | Y | | 可为空数组 |
| ··id | 订单ID | String | Y | 格式：雪花ID | |
| ··outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| ··alipayOrderId | 支付宝订单ID | String | N | 长度：1-64 | 创单成功后返回 |
| ··originOrderId | 原订阅订单号 | String | N | 长度：1-64 | 续订/买断溯源 |
| ··orderType | 订单类型 | String | Y | 枚举：RENT/RELET/BUYOUT | |
| ··orderStatus | 订单状态 | String | Y | 枚举：CREATED/SIGNED/APPROVED/DELIVERED/RECEIVED/RETURN_DELIVERED/RETURN_RECEIVED/FINISHED/CLOSED | |
| ··sourceChannel | 来源渠道 | String | Y | 枚举：0/1 | 字典：D10001 |
| ··title | 订单标题 | String | Y | 长度：1-200 | |
| ··buyerOpenId | 买家openId | String | Y | 长度：1-64 | |
| ··orderPrice | 订单总价 | String | Y | 精度：2位小数 | 单位：元 |
| ··depositPrice | 押金金额 | String | Y | 精度：2位小数 | 单位：元 |
| ··rentStartTime | 租赁开始时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··rentEndTime | 租赁结束时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··createTime | 创建时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |

**请求示例**:

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "params": {
    "orderStatus": "SIGNED",
    "createTimeBegin": "2026-09-01 00:00:00",
    "createTimeEnd": "2026-09-22 23:59:59"
  }
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "total": 1,
    "current": 1,
    "size": 10,
    "pages": 1,
    "records": [
      {
        "id": "1823456789012350001",
        "outOrderId": "6823789339978248",
        "alipayOrderId": "2026092221001004720200028594",
        "originOrderId": null,
        "orderType": "RENT",
        "orderStatus": "SIGNED",
        "sourceChannel": "1",
        "title": "某品牌Model Y 订阅",
        "buyerOpenId": "074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
        "orderPrice": "4800.00",
        "depositPrice": "4000.00",
        "rentStartTime": "2026-10-15 00:00:00",
        "rentEndTime": "2027-10-14 00:00:00",
        "createTime": "2026-09-22 10:30:00"
      }
    ]
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102003 订单详情查询

**业务概述**: 按商家侧订单号查询订单完整信息，含商品明细、订阅计划期次与签约（免押/代扣）状态。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/detail` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| order | 订单主信息 | Object | Y | | |
| ··id | 订单ID | String | Y | 格式：雪花ID | |
| ··outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| ··alipayOrderId | 支付宝订单ID | String | N | 长度：1-64 | |
| ··originOrderId | 原订阅订单号 | String | N | 长度：1-64 | |
| ··orderType | 订单类型 | String | Y | 枚举：RENT/RELET/BUYOUT | |
| ··orderStatus | 订单状态 | String | Y | 枚举：CREATED/SIGNED/APPROVED/DELIVERED/RECEIVED/RETURN_DELIVERED/RETURN_RECEIVED/FINISHED/CLOSED | |
| ··sourceChannel | 来源渠道 | String | Y | 枚举：0/1 | 字典：D10001 |
| ··title | 订单标题 | String | Y | 长度：1-200 | |
| ··buyerId | 买家支付宝userId | String | N | 长度：1-32 | |
| ··buyerOpenId | 买家openId | String | Y | 长度：1-64 | |
| ··orderPrice | 订单总价 | String | Y | 精度：2位小数 | |
| ··depositPrice | 押金金额 | String | Y | 精度：2位小数 | |
| ··freight | 运费 | String | N | 精度：2位小数 | |
| ··additionalPrice | 增值服务费 | String | N | 精度：2位小数 | |
| ··rentStartTime | 租赁开始时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··rentEndTime | 租赁结束时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··deliveryType | 配送方式 | String | Y | 枚举：SELFPICK | 汽车场景固定自提 |
| ··shopName | 门店名称 | String | N | 长度：1-100 | |
| ··shopAddress | 门店地址 | String | N | 长度：1-300 | |
| ··shopTel | 门店电话 | String | N | | |
| ··receiverName | 收货人姓名 | String | N | 长度：1-64 | |
| ··receiverTel | 收货人电话 | String | N | | |
| ··receiverAddress | 收货详细地址 | String | N | 长度：1-300 | |
| ··protocolName | 租赁协议名称 | String | N | 长度：1-100 | |
| ··protocolPath | 租赁协议路径 | String | N | 长度：1-300 | |
| ··detailPath | 订单详情页路径 | String | N | 长度：1-300 | |
| ··finishStatus | 完结方式 | String | N | 枚举：USER_RETURNED/USER_RETURNED_IN_ADVANCE/OTHER | |
| ··closeReasonCode | 关单原因编码 | String | N | 长度：1-32 | |
| ··closeReasonDesc | 关单原因描述 | String | N | 长度：1-200 | |
| ··deliveryTime | 发货时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··receiveTime | 确认收车时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··returnSendTime | 发起还车时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··returnReceiveTime | 验收收车时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··finishTime | 完结时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··closeTime | 关单时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··createTime | 创建时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |
| items | 商品明细 | Array | Y | | |
| ··id | 明细ID | String | Y | 格式：雪花ID | |
| ··itemType | 商品类型 | String | Y | 枚举：CAR_ITEM | |
| ··outItemId | 商家侧商品ID | String | Y | 长度：1-64 | |
| ··outSkuId | 商家侧SKU ID | String | N | 长度：1-64 | |
| ··itemName | 商品名称 | String | Y | 长度：1-200 | 创单快照 |
| ··itemDescription | 商品描述 | String | N | 长度：1-500 | |
| ··salePrice | 商品售价 | String | Y | 精度：2位小数 | |
| ··itemValue | 商品价值 | String | N | 精度：2位小数 | |
| ··itemCnt | 购买数量 | Number | Y | 最小值：1 | |
| installments | 订阅计划期次 | Array | N | | RENT 订单返回 |
| ··id | 期次ID | String | Y | 格式：雪花ID | |
| ··installmentNo | 期号 | Number | Y | 最小值：1 | |
| ··installmentPrice | 当期订阅金额 | String | Y | 精度：2位小数 | |
| ··planPayTime | 计划扣款时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |
| ··buyoutPrice | 到期购买金额 | String | N | 精度：2位小数 | 仅最后一期 |
| ··billStatus | 期次账单状态 | String | Y | 枚举：0/1/2/3/4 | 字典：D10002，0-待支付 1-支付中 2-已支付 3-支付失败 4-已关闭 |
| ··paidAmount | 已付金额 | String | N | 精度：2位小数 | |
| ··actualPayTime | 实际支付时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| sign | 签约信息 | Object | N | | 未签约时可为空 |
| ··signStatus | 签约状态 | String | Y | 枚举：0/1/2 | 字典：D10003，0-未签约 1-已签约 2-已解约 |
| ··freezeStatus | 预授权冻结状态 | String | Y | 枚举：0/1/2/3 | 字典：D10004，0-未冻结 1-已冻结 2-已解冻 3-已转支付 |
| ··freezeAmount | 冻结金额 | String | N | 精度：2位小数 | |
| ··deductStatus | 代扣协议状态 | String | Y | 枚举：0/1/2 | 字典：D10005，0-未签约 1-已签约 2-已解除 |
| ··signTime | 签约完成时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··freezeTime | 冻结完成时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··unfreezeTime | 解冻时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "order": {
      "id": "1823456789012350001",
      "outOrderId": "6823789339978248",
      "alipayOrderId": "2026092221001004720200028594",
      "originOrderId": null,
      "orderType": "RENT",
      "orderStatus": "SIGNED",
      "sourceChannel": "1",
      "title": "某品牌Model Y 订阅",
      "buyerId": "20880003050001",
      "buyerOpenId": "074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
      "orderPrice": "4800.00",
      "depositPrice": "4000.00",
      "freight": null,
      "additionalPrice": null,
      "rentStartTime": "2026-10-15 00:00:00",
      "rentEndTime": "2027-10-14 00:00:00",
      "deliveryType": "SELFPICK",
      "shopName": "某品牌体验中心",
      "shopAddress": "某市某区某路100号",
      "shopTel": "0571-88888888",
      "receiverName": "张三",
      "receiverTel": "13800000000",
      "receiverAddress": "某省某市某区某小区某号",
      "protocolName": "汽车订阅服务协议",
      "protocolPath": "/pages/protocol/index",
      "detailPath": "/pages/orderDetail/index?orderId=6823789339978248",
      "finishStatus": null,
      "closeReasonCode": null,
      "closeReasonDesc": null,
      "deliveryTime": null,
      "receiveTime": null,
      "returnSendTime": null,
      "returnReceiveTime": null,
      "finishTime": null,
      "closeTime": null,
      "createTime": "2026-09-22 10:30:00"
    },
    "items": [
      {
        "id": "1823456789012350002",
        "itemType": "CAR_ITEM",
        "outItemId": "12345",
        "outSkuId": "34567576565656",
        "itemName": "某品牌Model Y 订阅",
        "itemDescription": "全新车辆，按月订阅",
        "salePrice": "400.00",
        "itemValue": "260000.00",
        "itemCnt": 1
      }
    ],
    "installments": [
      {
        "id": "1823456789012350003",
        "installmentNo": 1,
        "installmentPrice": "400.00",
        "planPayTime": "2026-10-15 00:00:00",
        "buyoutPrice": null,
        "billStatus": "0",
        "paidAmount": null,
        "actualPayTime": null
      },
      {
        "id": "1823456789012350004",
        "installmentNo": 2,
        "installmentPrice": "400.00",
        "planPayTime": "2026-11-15 00:00:00",
        "buyoutPrice": "260000.00",
        "billStatus": "0",
        "paidAmount": null,
        "actualPayTime": null
      }
    ],
    "sign": {
      "signStatus": "1",
      "freezeStatus": "1",
      "freezeAmount": "4000.00",
      "deductStatus": "1",
      "signTime": "2026-09-22 10:35:00",
      "freezeTime": "2026-09-22 10:35:10",
      "unfreezeTime": null
    }
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102004 订单履约同步

**业务概述**: 商家发货（交车）或用户发起还车时，同步履约状态至支付宝；交车后订单状态变更为 DELIVERED，用户还车后变更为 RETURN_DELIVERED。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/fulfillment/send` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| operationType | 履约操作类型 | String | Y | 枚举：MERCHANT_DELIVERY_SEND/USER_DELIVERY_SEND | 商家发货交车/用户寄回还车 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "operationType": "MERCHANT_DELIVERY_SEND"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102005 订单收货确认

**业务概述**: 用户确认收车或商家验收用户寄回车辆时，同步确认收货状态至支付宝；确认后订单状态变更为 RECEIVED（订阅中）或 RETURN_RECEIVED（已收车验收）。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/fulfillment/receive` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| receiveType | 收货确认类型 | String | Y | 枚举：MERCHANT_DELIVERY_RECEIVED/USER_DELIVERY_RECEIVED | 用户确认收车/商家验收收车 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "receiveType": "MERCHANT_DELIVERY_RECEIVED"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102006 订单完结

**业务概述**: 车辆验收无误后完结订单；完结后系统自动执行预授权解冻并解除该订单关联的代扣协议。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/fulfillment/finish` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| finishStatus | 完结方式 | String | Y | 枚举：USER_RETURNED/USER_RETURNED_IN_ADVANCE/OTHER | 到期归还/提前归还/其他完结（如续租无残值） |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "finishStatus": "USER_RETURNED"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102007 订单关闭

**业务概述**: 关闭用户确认收货前的订单（如商家风控拒绝、用户取消）；关单前要求所有已支付费项全额退款完成，未签约订单24小时超时自动关单。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/close` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| reasonCode | 关单原因编码 | String | Y | 长度：1-32 | 支付宝原因编码，如 3114 |
| reasonDesc | 关单原因描述 | String | N | 长度：1-200 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "reasonCode": "3114",
  "reasonDesc": "用户资质问题"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0102008 修改租期

**业务概述**: 修改订单租赁开始结束时间及期次计划扣款时间（对应支付宝 type=RENT_PLAN_TIME）。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/modify` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| rentStartTime | 租赁开始时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |
| rentEndTime | 租赁结束时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |
| installments | 期次调整列表 | Array | N | | 仅传需要调整计划扣款时间的期次 |
| ··installmentNo | 期号 | Number | Y | 最小值：1 | |
| ··planPayTime | 新计划扣款时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "rentStartTime": "2026-10-15 00:00:00",
  "rentEndTime": "2026-10-20 00:00:00",
  "installments": [
    {
      "installmentNo": 1,
      "planPayTime": "2026-10-15 08:00:00"
    }
  ]
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0103001 获取签约串

**业务概述**: 用户关闭芝麻免押受理台后，在商家订单详情页通过本接口获取签约字符串（sign_str），由前端唤起芝麻受理台完成免押/代扣签约。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/sign` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| signStr | 签约字符串 | String | Y | | 支付宝返回 sign_str，供前端唤起受理台 |
| signLaunchMethod | 唤起方式 | String | Y | 枚举：TRADEPAY 等 | 以支付宝返回为准 |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "signStr": "app_id=2017060101317939&biz_content=%7B%22post_payments%22%3A%22%22%7D",
    "signLaunchMethod": "TRADEPAY"
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0104001 风控咨询查询

**业务概述**: 商家审核前调用支付宝租赁行业风险咨询接口，查询用户综合风险等级（T1~T10）并落库留痕。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/risk/consult` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| riskLevel | 综合风险等级 | String | Y | 枚举：T1/T2/T3/T4/T5/T6/T7/T8/T9/T10 | T1-T4低风险 T5-T6中风险 T7-T8高风险 T9-T10极高风险 |
| auditResult | 审核结论 | String | N | 枚举：1/2/3 | 字典：D10006，1-通过 2-拒绝 3-转人工，已有审核记录时返回 |
| consultResult | 风控咨询详情 | String | N | 格式：JSON | 风控接口返回的关键信息 |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "riskLevel": "T3",
    "auditResult": null,
    "consultResult": "{\"consult_risk_types\":\"COMPREHENSIVE_RISK\",\"risk_level\":\"T3\"}"
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0104002 提交风控审核结论

**业务概述**: 提交商家风控审核结论：审核通过时调用支付宝履约审核通过接口推动订单至 APPROVED；审核拒绝时关闭订单。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/risk/audit` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| auditResult | 审核结论 | String | Y | 枚举：1/2 | 字典：D10006，1-通过 2-拒绝 |
| rejectReason | 拒绝原因 | String | C | 长度：1-200 | auditResult=2 时必填 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "auditResult": "1"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0105001 订单支付

**业务概述**: 发起订单支付，支持代扣（RENT_DEDUCT）、预授权转支付（PRE_AUTH）、JSAPI 主动支付；JSAPI 场景返回 tradeNo 供前端 my.tradePay 唤起收银台。期次自动代扣由定时任务 J0102001 触发，不走本接口。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/pay` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| payMethod | 支付方式 | String | Y | 枚举：RENT_DEDUCT/PRE_AUTH/JSAPI | 代扣/预授权转支付/主动支付 |
| payItems | 费项明细 | Array | Y | 长度：1-36 | 当前这笔支付的费用项列表 |
| ··feeType | 费项类型 | String | Y | 枚举：RENT/INDEMNITY | 订阅金/赔付违约金 |
| ··installmentNo | 期号 | Number | C | 最小值：1 | feeType=RENT 时必填 |
| ··payAmount | 费项金额 | String | Y | 精度：2位小数 | 单位：元 |
| payTimeoutExpress | 支付超时时间 | String | N | 格式：如30m | JSAPI 场景有效 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| tradeNo | 支付宝交易号 | String | N | 长度：1-64 | JSAPI 场景返回，供前端 my.tradePay |
| outTradeNo | 商家侧支付单号 | String | Y | 长度：1-64 | |
| payAmount | 支付总金额 | String | Y | 精度：2位小数 | |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "payMethod": "JSAPI",
  "payItems": [
    {
      "feeType": "RENT",
      "installmentNo": 1,
      "payAmount": "400.00"
    }
  ],
  "payTimeoutExpress": "30m"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "tradeNo": "2026092222001404330000121536",
    "outTradeNo": "6823789339978248P0001",
    "payAmount": "400.00"
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0105002 端外支付同步

**业务概述**: 通过微信、线下转账等非支付宝渠道完成收款后，将已支付费项同步至支付宝订单（pay.sync），后端固定收款渠道 pay_channel=OTHER。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/order/pay/sync` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| payItems | 费项明细 | Array | Y | 长度：1-36 | 本次同步的已支付费项 |
| ··feeType | 费项类型 | String | Y | 枚举：RENT/INDEMNITY | |
| ··installmentNo | 期号 | Number | C | 最小值：1 | feeType=RENT 时必填 |
| ··payAmount | 费项金额 | String | Y | 精度：2位小数 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outTradeNo | 商家侧支付单号 | String | Y | 长度：1-64 | |
| payAmount | 同步总金额 | String | Y | 精度：2位小数 | |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "payItems": [
    {
      "feeType": "RENT",
      "installmentNo": 2,
      "payAmount": "400.00"
    }
  ]
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "outTradeNo": "6823789339978248P0002",
    "payAmount": "400.00"
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0105003 交易退款

**业务概述**: 对已支付费项发起退款（统一收单交易退款接口），取消订单售后场景须在关单前将所有已支付费项全额退回；退款请求号由后端生成保证幂等。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/refund` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| refundItems | 退款明细 | Array | Y | 长度：1-50 | |
| ··payRecordId | 原支付记录ID | String | Y | 格式：雪花ID | 关联 tb_rent_pay_record |
| ··refundAmount | 退款金额 | String | Y | 精度：2位小数，大于0 | 不得超过原支付可退余额 |
| ··refundReason | 退款原因 | String | N | 长度：1-200 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "refundItems": [
    {
      "payRecordId": "1823456789012360001",
      "refundAmount": "400.00",
      "refundReason": "取消订单退款"
    }
  ]
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0105004 支付流水分页查询

**业务概述**: 分页查询订单支付流水及费项明细，含支付宝收款与端外同步收款。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/pay/record/page` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| pageNo | 页码 | Number | Y | 最小值：1 | |
| pageSize | 每页条数 | Number | Y | 长度：1-100 | |
| params | 查询条件 | Object | Y | | 条件均为非必填 |
| ··outOrderId | 商家侧订单号 | String | N | 长度：1-64 | |
| ··outTradeNo | 商家侧支付单号 | String | N | 长度：1-64 | |
| ··tradeNo | 支付宝交易号 | String | N | 长度：1-64 | |
| ··payMethod | 支付方式 | String | N | 枚举：RENT_DEDUCT/PRE_AUTH/JSAPI | |
| ··payStatus | 支付状态 | String | N | 枚举：0/1/2/3 | 字典：D10008 |
| ··payChannel | 收款渠道 | String | N | 枚举：ALIPAY/OTHER | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| total | 总条数 | Number | Y | | |
| current | 当前页码 | Number | Y | | |
| size | 每页条数 | Number | Y | | |
| pages | 总页数 | Number | Y | | |
| records | 支付流水列表 | Array | Y | | 可为空数组 |
| ··id | 支付记录ID | String | Y | 格式：雪花ID | |
| ··outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| ··outTradeNo | 商家侧支付单号 | String | Y | 长度：1-64 | |
| ··tradeNo | 支付宝交易号 | String | N | 长度：1-64 | 端外支付时为空 |
| ··payMethod | 支付方式 | String | Y | 枚举：RENT_DEDUCT/PRE_AUTH/JSAPI | |
| ··payAmount | 支付金额 | String | Y | 精度：2位小数 | |
| ··payStatus | 支付状态 | String | Y | 枚举：0/1/2/3 | 字典：D10008，0-处理中 1-成功 2-失败 3-已全额退款 |
| ··payChannel | 收款渠道 | String | Y | 枚举：ALIPAY/OTHER | |
| ··payTime | 支付完成时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··refundAmount | 已退款金额 | String | Y | 精度：2位小数 | |
| ··items | 费项明细 | Array | Y | | |
| ····feeType | 费项类型 | String | Y | 枚举：RENT/INDEMNITY | |
| ····installmentNo | 期号 | Number | N | 最小值：1 | |
| ····payAmount | 费项金额 | String | Y | 精度：2位小数 | |

**请求示例**:

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "params": {
    "outOrderId": "6823789339978248"
  }
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "total": 1,
    "current": 1,
    "size": 10,
    "pages": 1,
    "records": [
      {
        "id": "1823456789012360001",
        "outOrderId": "6823789339978248",
        "outTradeNo": "6823789339978248P0001",
        "tradeNo": "2026092222001404330000121536",
        "payMethod": "RENT_DEDUCT",
        "payAmount": "400.00",
        "payStatus": "1",
        "payChannel": "ALIPAY",
        "payTime": "2026-10-15 08:00:05",
        "refundAmount": "0.00",
        "items": [
          {
            "feeType": "RENT",
            "installmentNo": 1,
            "payAmount": "400.00"
          }
        ]
      }
    ]
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0107001 创建售后单

**业务概述**: 创建取消订单或赔付类售后单并同步至支付宝；赔付金收取须在订单完结前发起，取消订单的退款由商家通过交易退款接口（A0105003）自行完成。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/aftersale/create` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| aftersaleType | 售后类型 | String | Y | 枚举：ORDER_CANCEL/COMPENSATION | 取消订单/赔付 |
| reasonCode | 售后原因编码 | String | Y | 枚举：NO_NEED/ITEM_DAMAGED 等 | 以支付宝原因编码为准 |
| reasonDesc | 售后原因描述 | String | N | 长度：1-200 | |
| additionalDesc | 补充说明 | String | N | 长度：1-500 | |
| fileIds | 凭证附件ID | String | N | 长度：1-500 | 多个逗号分隔，关联附件影像中心 |
| payAmount | 赔付金额 | String | C | 精度：2位小数，大于0 | aftersaleType=COMPENSATION 时必填 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outAftersaleId | 商家侧售后单号 | String | Y | 长度：1-64 | |
| alipayAftersaleId | 支付宝售后单号 | String | N | 长度：1-64 | 支付宝创建成功后返回 |

**请求示例**:

```json
{
  "outOrderId": "6823789339978248",
  "aftersaleType": "COMPENSATION",
  "reasonCode": "ITEM_DAMAGED",
  "reasonDesc": "物损赔付",
  "additionalDesc": "车辆外观划伤",
  "fileIds": "1823456789012370001,1823456789012370002",
  "payAmount": "15.00"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "outAftersaleId": "682378934923910",
    "alipayAftersaleId": "2026092221001004720200028601"
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0107002 售后单分页查询

**业务概述**: 管理端分页查询售后单列表。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/aftersale/page` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| pageNo | 页码 | Number | Y | 最小值：1 | |
| pageSize | 每页条数 | Number | Y | 长度：1-100 | |
| params | 查询条件 | Object | Y | | 条件均为非必填 |
| ··outOrderId | 商家侧订单号 | String | N | 长度：1-64 | |
| ··outAftersaleId | 商家侧售后单号 | String | N | 长度：1-64 | |
| ··aftersaleType | 售后类型 | String | N | 枚举：ORDER_CANCEL/COMPENSATION | |
| ··aftersaleStatus | 售后状态 | String | N | 枚举：0/1/2 | 字典：D10011，0-处理中 1-已完结 2-已取消 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| total | 总条数 | Number | Y | | |
| current | 当前页码 | Number | Y | | |
| size | 每页条数 | Number | Y | | |
| pages | 总页数 | Number | Y | | |
| records | 售后单列表 | Array | Y | | 可为空数组 |
| ··id | 售后单ID | String | Y | 格式：雪花ID | |
| ··outAftersaleId | 商家侧售后单号 | String | Y | 长度：1-64 | |
| ··alipayAftersaleId | 支付宝售后单号 | String | N | 长度：1-64 | |
| ··outOrderId | 商家侧订单号 | String | Y | 长度：1-64 | |
| ··aftersaleType | 售后类型 | String | Y | 枚举：ORDER_CANCEL/COMPENSATION | |
| ··reasonCode | 售后原因编码 | String | Y | 长度：1-32 | |
| ··reasonDesc | 售后原因描述 | String | N | 长度：1-200 | |
| ··additionalDesc | 补充说明 | String | N | 长度：1-500 | |
| ··fileIds | 凭证附件ID | String | N | 长度：1-500 | |
| ··payAmount | 赔付金额 | String | N | 精度：2位小数 | |
| ··payStatus | 赔付收取状态 | String | Y | 枚举：0/1/2 | 字典：D10010，0-待收取 1-已收取 2-免于收取 |
| ··aftersaleStatus | 售后状态 | String | Y | 枚举：0/1/2 | 字典：D10011 |
| ··finishTime | 完结时间 | String | N | 格式：yyyy-MM-dd HH:mm:ss | |
| ··createTime | 创建时间 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |

**请求示例**:

```json
{
  "pageNo": 1,
  "pageSize": 10,
  "params": {
    "aftersaleStatus": "0"
  }
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {
    "total": 1,
    "current": 1,
    "size": 10,
    "pages": 1,
    "records": [
      {
        "id": "1823456789012380001",
        "outAftersaleId": "682378934923910",
        "alipayAftersaleId": "2026092221001004720200028601",
        "outOrderId": "6823789339978248",
        "aftersaleType": "COMPENSATION",
        "reasonCode": "ITEM_DAMAGED",
        "reasonDesc": "物损赔付",
        "additionalDesc": "车辆外观划伤",
        "fileIds": "1823456789012370001,1823456789012370002",
        "payAmount": "15.00",
        "payStatus": "0",
        "aftersaleStatus": "0",
        "finishTime": null,
        "createTime": "2026-09-22 14:00:00"
      }
    ]
  },
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0107003 售后处理

**业务概述**: 同步售后终态至支付宝：用户取消申请（USER_CANCEL_APPLY）或售后完结（AFTERSALE_FINISH）；取消类售后完结前须确认已退款完成，售后完结后关闭对应订单。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/rent/aftersale/confirm` |
| 传参方式 | Body |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| outAftersaleId | 商家侧售后单号 | String | Y | 长度：1-64 | |
| operationType | 处理操作类型 | String | Y | 枚举：USER_CANCEL_APPLY/AFTERSALE_FINISH | 用户取消申请/售后完结 |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |

**请求示例**:

```json
{
  "outAftersaleId": "682378934923910",
  "operationType": "AFTERSALE_FINISH"
}
```

**响应示例**:

```json
{
  "code": "SY000000",
  "message": "交易成功",
  "data": {},
  "success": true,
  "timestamp": "1772775292343"
}
```

---

### A0108001 支付宝异步通知接收

**业务概述**: 统一接收支付宝异步通知（预授权冻结结果、支付结果、售后消息、订单变更），完成验签、notify_id 幂等去重、报文落库后分发至对应业务处理。

**基本信息**:

| 属性 | 值 |
| --- | --- |
| 请求方式 | POST |
| 请求路径 | `/api/v1/callback/alipay/notify` |
| 传参方式 | Body（application/x-www-form-urlencoded） |

**请求参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| biz_content | 通知业务报文 | String | Y | 格式：JSON | 报文原文，按 msg_method 解析 |
| msg_method | 消息方法名 | String | Y | 长度：1-100 | 如 alipay.commerce.rent.order.aftersale.notify |
| notify_id | 通知ID | String | Y | 长度：1-64 | 幂等去重键 |
| app_id | 来源应用ID | String | Y | 长度：1-64 | |
| sign | 签名 | String | Y | | 验签用 |
| sign_type | 签名类型 | String | Y | 枚举：RSA2 | |
| version | 接口版本 | String | Y | 长度：1-16 | 如 1.1 |
| utc_timestamp | 时间戳 | String | Y | 格式：yyyy-MM-dd HH:mm:ss | |
| charset | 编码 | String | N | 枚举：UTF-8 | |

**响应参数**:

| 字段标识 | 字段名称 | 字段类型 | 是否必填 | 值约束 | 备注 |
| --- | --- | --- | --- | --- | --- |
| （响应遵循支付宝协议） | 应答文本 | String | Y | 枚举：success/fail | 处理成功返回文本 success，不适用统一 JSON 响应结构 |

**请求示例**:

```text
biz_content={"buyer_id":"20880003050001","order_id":"2026092221001004720200028594","out_order_id":"6823789339978248","aftersale_type":"ORDER_CANCEL","aftersale_id":"2026092221001004720200028601","aftersale_status":"APPROVING","source_type":"MERCHANT"}&msg_method=alipay.commerce.rent.order.aftersale.notify&notify_id=202609222100980000000001&app_id=2021004169676031&sign=ERITJKEIJKJHKKKKKKKHJ&sign_type=RSA2&version=1.1&utc_timestamp=2026-09-22 15:00:00&charset=UTF-8
```

**响应示例**:

```text
success
```
