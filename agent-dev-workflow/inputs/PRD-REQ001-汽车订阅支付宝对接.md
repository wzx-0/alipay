# 汽车订阅解决方案-茂电

# 版本记录

| **版本** | **发布日期** | **更新内容** |
| --- | --- | --- |
| v1.0 | 2026-03-04 | 汽车订阅解决方案文档对外发布 |
| V1.1 | 2026-03-05 | 1、增加扣款规则参考租赁行业交易管理规范[https://rulecenter.alipay.com/gateway/details?docId=323300234](https://rulecenter.alipay.com/gateway/details?docId=323300234)<br>2、增加风控规则解读 |
| V1.2 | 2026-03-06 | 步骤二：用户在点击 **提交订单** 按钮后，新增支付宝传给商家的参数 |

# 一、接入流程

## 1.1 流程概述

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/Mp7ld7b5YvkoMOBQ/img/2d9099c1-e9fb-41d7-bcef-7309b6bac857.jpg)

## 1.2 接入要求

1、基础要求

*   对[支付宝企业账号](https://opendocs.alipay.com/b/0c54ro)开放。
    
*   完成[入驻以及小程序的接入](https://opendocs.alipay.com/mini/developer/getting-started?pathHash=b59eb07b)。
    
*   合作商家小程序服务必须以遵守[支付宝小程序运营规范](https://opendocs.alipay.com/mini/operation/dprvr1)为前提。
    
*   合作商家小程序准入必须以遵守[支付宝小程序准入规则](https://opendocs.alipay.com/b/0a8g3y?pathHash=492574ba)为前提。
    
*   支付宝账户类目要求（暂定）：消费品租赁（B0051）
    
    *   _MCC（商家类目码）查看/修改：支付宝商家平台 - 账号中心 - 商家信息 - 商家基本信息_
        

*   小程序类目要求：二手/租赁。
    

2、资质要求

*   营业执照： 提供《营业执照》且经营范围需包含租赁等相关内容；
    
*   公司注册资本满100万； 
    
*   个人或个体工商家不准入 。
    
*   适用标准：小程序需要在半年内无严重违规（包括但不限于违法犯罪、欺诈、无照经营、侵权、风险交易、销售禁售商品、销售不具备资质商品、假货等违规类型）。
    

3、设备环境流程

*   本地开发接入插件时，如果需要更新插件版本，需要到开放平台控制台进行操作才会生效，详情可查看 [插件版本升级](https://opendocs.alipay.com/mini/plugin/version-upgrade)。
    
*   本插件使用了新版框架提供的能力，需设置小程序的最低基础库版本至**2.8.9**及以上。配置操作详情可查看 [配置最低基础库版本](https://opendocs.alipay.com/mini/framework/lib#%E8%AE%BE%E7%BD%AE%E6%9C%80%E4%BD%8E%E5%9F%BA%E7%A1%80%E5%BA%93%E7%89%88%E6%9C%AC)。
    

4、小程序规范

*   申请实名认证 
    
*   商家已经有线上的小程序
    
*   适用小程序类目：实物类目，更多开放类目还在逐步拓展中。
    
*   适用商家范围：小程序开发者为国内非个人主体（即企业、个体工商家）开发者。
    
*   适用标准：小程序需要在半年内无严重违规（包括但不限于违法犯罪、欺诈、无照经营、侵权、风险交易、销售禁售商品、销售不具备资质商品、假货等违规类型）。
    
*   支付宝客户端 10.1.85 及以上版本支持已使用分包的主体小程序应用插件。
    
*   不支持较早版本的 APPID 为 8 位数字的小程序接入该插件。
    
*   静态加载仅支持一个小程序最多关联 10 个插件，动态加载无限制。
    
*   本地开发接入插件时，如果需要更新插件版本，需要到开放平台控制台进行操作才会生效，详情可查看 [插件版本升级](https://opendocs.alipay.com/mini/plugin/version-upgrade)。
    
*   本插件使用了新版框架提供的能力，需设置小程序的最低基础库版本至**2.8.9**及以上。配置操作详情可查看 [配置最低基础库版本](https://opendocs.alipay.com/mini/framework/lib#%E8%AE%BE%E7%BD%AE%E6%9C%80%E4%BD%8E%E5%9F%BA%E7%A1%80%E5%BA%93%E7%89%88%E6%9C%AC)。
    

## 1.3 接入准备

### 1.3.1 产品开通

请联系芝麻运营完成产品开通

| **名称** | **类型** | **产品能力** | **准入规则** |
| --- | --- | --- | --- |
| [租赁行业交易组件](https://opendocs.alipay.com/mini/0calzt?pathHash=58e02832&ref=api) | 小程序产品 | 公域基础产品之一，也是租赁行业小程序核心产品能力，串联信息流、订单流、资金流 | *   开放接入<br>    <br>*   快捷下单插件必须接入 |
| 保证金缴纳 | 条款 | 公域基础产品之一，用于公域订单的消费者保障，浮动保证金模式 | *   开放，商家B站缴纳 |
| 慧推宝协议 | 条款 | 公域基础产品之一，用于约定公域订单账期及抽佣比例的相关内容 | *   开放，商家B站签约 |
| JSAPI支付 | 支付产品 | 公域基础产品之一，用户主动支付需使用JSAPI支付，支持小程序和收款主体为异主体的情况。 | *   开放接入 |
| [预授权支付](https://b.alipay.com/page/product-workspace/product-detail/I1080300001000065324) | 支付产品 | 商家冻结用户资金/信用额度作为预付款（即押金），用户交易完成后，商户向支付宝请求从用户账户扣除实际消费金额或全额解冻。 | *   开放接入 |
| 租赁行业代扣 | 支付产品 | 商家代扣能力的一种形式，基于交易组件的信息进行代扣，适用于公私域全量订单 | *   在符合规则的前提下，商家可自助签约 |
| 芝麻免押 | 信用产品 | 帮助商家通过免押金/免订金服务，降低用户的使用门槛，提升用户体验，从而提高用户的下单转化率，以此增加商家的交易量。 | *   开放接入 |
| [获取会员信息](https://open.alipay.com/develop/uni/mini/apply-privacy?appId=2021004169676031&bundleId=com.alipay.alipaywallet) | 基础产品 | 在获得用户授权后，可通过接口获取用户实名认证信息。 | *   需要实名认证的商家，联系支付宝运营同学申请接入 |
| 提报商品 | 基础产品 | 商家可以在支付宝平台提报**租赁类（实物）商品**，详见[小程序商品提报操作指南](https://opendocs.alipay.com/b/076cxe?pathHash=23d4767c%E5%95%86%E5%93%81%E6%8F%90%E6%8A%A5%E6%93%8D%E4%BD%9C%E6%8C%87%E5%8D%97)，[小程序商品创建接口](https://opendocs.alipay.com/mini/4880cf68_alipay.open.app.item.create)。 | *   快捷下单插件必须提报商品到商品库 |

### 1.3.2 订购插件

*   在服务市场订购租赁快捷下单插件：
    
    *   商家请登录商家平台订购：[订购租赁快捷下单插件](https://b.alipay.com/page/fw-market/home/detail/AM010401000000120604)
        
    *   服务商请登录服务商平台：[订购租赁快捷下单插件](https://p.alipay.com/page/fw-market/home/detail/AM010401000000120604)
        

### 1.3.3 发布商品

*   接入插件前，请确保商家已经在支付宝平台上报至少一个**租赁类（实物）商品**并通过审核，详见[小程序商品提报操作指南](https://opendocs.alipay.com/b/076cxe?pathHash=23d4767c%E5%95%86%E5%93%81%E6%8F%90%E6%8A%A5%E6%93%8D%E4%BD%9C%E6%8C%87%E5%8D%97)，[小程序商品创建接口](https://opendocs.alipay.com/mini/4880cf68_alipay.open.app.item.create)。
    
*   插件内商品详情页会展示商家提报到商品库的商品要素，主要包含：商品名称、商品封面图主图、商品价格、商品规格、租期套餐、详情长图等必选信息。
    
*   **注意⚠️**：为不影响用户下单，**汽车订阅商品提报请遵循以下SOP操作：**
    

# 二、开发接入

2.0 业务经营能力

**商家后台相关智能经营，数据分析，智能IM等相关功能，需要找支付宝业务对接人进行开通**。

后台地址：[https://b.alipay.com/page/recycle-im/home](https://b.alipay.com/page/recycle-im/home)

![image.png](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/mPdnpE5aK6QRpqw9/img/4637dbc6-7e37-406f-ad8a-2ac2f6c25534.png)

### 2.1 用户流程图

[请至钉钉文档查看「白板」](https://alidocs.dingtalk.com/i/nodes/R4GpnMqJzG9aabLBcLDe7e2E8Ke0xjE3?cid=79457208891&corpId=ding42f1731ef067e4ae&doc_type=wiki_doc&iframeQuery=anchorId%3DX02mmeof1pu8yfkxodxbqt&utm_medium=im_card&utm_scene=team_space&utm_source=im)

### 2.2 系统交互（下单部分）

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/Mp7ld7b5YvkoMOBQ/img/d881cb4f-20b5-4ca3-a59e-d1f0c5762aa9.png)

### 2.3 汽车订阅交易正向流程图

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/Mp7ld7b5YvkoMOBQ/img/1ef46104-0fab-4c20-b394-c1216c1b8ddb.jpg)

| 租赁业务状态 | 汽车订阅状态映射 | 节点释义 |
| --- | --- | --- |
| 申请中 | 用户下单发起申请 | 该阶段只是创业务单，用户尚未++签约支付产品（免押&代扣）++ |
| 待审核 | 待审核 | 用户完成++支付产品签约（免押&代扣）++，待商家审核用户的信用资质 |
| 待发货 | 待发货（待签合同） | 审核通过，待与用户沟通车辆选配和订阅方案细节，确认成交意向&签署订阅合同 |
| 已发货 | 已发货（车辆交付中） | 商家与用户确认成交（以++签署订阅合同++为准），正式启动车辆交付流程 |
| 租赁中 | 订阅中 | 用户确认收车后发起 |
| 已寄回 | 完成还车 | 用户完成还车 |
| 已完结 | 订单完结 | 用户已归还/已买断/已续租 |

# 三、涉及API接口 

[https://opendocs.alipay.com/solution/0h845z?pathHash=a06d35e3](https://opendocs.alipay.com/solution/0h845z?pathHash=a06d35e3) 支付宝开放平台文档

## 步骤一：在小程序中声明使用插件

系统交互图中2.1.1唤起插件下单页面

### 在 `app.json` 中声明插件引用：

小程序首次接入租赁快捷下单插件，需要在小程序的 `app.json` 中进行以下配置：

```json
{
  "lazyCodeLoading": "requiredComponents",
  "plugins": {
    "goodsDetailPlugin": {
      "version": "*",
      "provider": "2021003177653028",
    }
  }
}
```

### 商家小程序自跳转至汽车下单页（调试阶段可用）：

```typescript
my.navigateTo({
  url: 'plugin://goodsDetailPlugin/rentCarOrder?appId=YOUR_APPID&outItemId=xxx&outSkuId=xxx&quantity=1&duration=365&rentDeliveryType=SELFPICK&source=rentGoodsDetail',
});
```

URL Search 参数：

| 参数 | 类型 | 说明 |
| --- | --- | --- |
| appId | string | 商家小程序 AppID |
| outItemId | string | 商家侧商品 ID |
| outSkuId | string | 商家侧 SKU ID |
| quantity | number | 购买数量 |
| duration | number | 租期（天） |
| rentDeliveryType | string | 配送方式，汽车场景固定传 `SELFPICK`（自提） |

## 步骤二：创建业务订单

### 注册下单回调函数

该步骤发生在用户在点击「提交订单」按钮的回调函数中，需要商家提前预设（在小程序的 `app.ts` 中编写），相关示例代码如下：

```javascript
const goodsDetailPlugin = requirePlugin('goodsDetailPlugin');

goodsDetailPlugin.setRentCarOrderSubmitCallback(async (params) => {
  // TODO：此处需要开发者自行实现
  
  // 1. 订单前置判断接口获取 sourceId
  // 参考：https://opendocs.alipay.com/mini/512c3ce1_my.checkBeforeAddOrder?pathHash=1a9f62f4

  // 2. 调用openapi创建业务单，也就是下方的步骤二
  // 参考：https://opendocs.alipay.com/solution/badca106_alipay.commerce.rent.order.create?scene=common&pathHash=c6dbc94d

  // 3. 返回订单号、订单详情页地址以及接口的报错信息（可选）
  return {
    success: true,
    orderId: "",     // 支付宝侧交易订单号
    outOrderId: "",  // 商家侧交易订单号
    path: "", // 商家订单详情页链接，例如：pages/orderDetial/index?orderId=123
    errorCode: "", // 错误码（可选）
    errorMsg: ""   // 错误信息（可选）
  };
});
```

#### 入参说明（Object params）

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| itemInfo.outItemId | string | 商家侧商品 ID |
| itemInfo.outSkuId | string | 商家侧 SKU ID |
| itemInfo.salePrice | string | 商品售价 |
| itemInfo.title | string | 商品名称 |
| orderInfo.quantity | number | 购买数量 |
| orderInfo.activityConsultId | string | 优惠活动咨询 ID，传入创单接口 |
| stagePayPlan.stagePayPlanInfos | array | 分期还款计划（含期数、金额、时间） |
| rentInfo.depositPrice | string | 押金金额 |
| rentInfo.totalRent | string | 租金总额 |
| rentInfo.duration | number | 租期（天） |
| rentInfo.serviceId | string | 芝麻服务 ID（风控用） |
| rentInfo.categoryId | string | 品类 ID（风控用） |
| rentInfo.payeeUserId | string | 收款方 userId |
| rentInfo.rentDeliveryType | string | 配送方式 |
| merchantExtInfo | object | 商家自定义透传数据 |

#### 返回值说明（Obejct result）

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| success | boolean | ✅ | 是否创单成功 |
| orderId | string | ✅ | 支付宝平台订单 ID（用于支付受理） |
| outOrderId | string | ✅ | 商家侧订单 ID |
| path | string | ✅ | 支付完成后跳转的订单详情页路径 |
| errorCode | string | — | 失败时的错误码 |
| errorMsg | string | — | 失败时的错误信息 |

### 调用服务端接口创建租赁订单

系统交互图中3.1.2唤起插件下单页面，接口文档：[alipay.commerce.rent.order.create(租赁订单创建)](https://opendocs.alipay.com/solution/badca106_alipay.commerce.rent.order.create?scene=common&pathHash=b8b4601f)

（1）关键参数

*   **order\_type 订单类型**
    
    *   首次订阅传：RENT、续订: RELET、订阅到期后购买: BUYOUT
        

*   **rent\_plan\_info 订阅计划**
    
    *   installments 数组，每一项代表1期。最长36个月。
        
    *   installments.installment\_no 期号，从1开始递增
        
    *   installments.installment\_price 当期订阅金额。除首期、尾期租金外，其他每期金额需保持一致
        
    *   installments.plan\_pay\_time 当期计划扣款时间（第2期及之后的订阅金，需在此日期后才可发起代扣）
        
    *   installments.buyout\_price 最后一期需传入，表示到期购买金额
        

*   **price\_info 订单价格信息**
    
    *   order\_price 订单总价order\_price=运费freight+增值服务费additional\_price+sum{每期订阅金额installment\_price}
        
    *   运费、增值服务费依据实际情况选传
        
    *   deposit\_price 押金会作为芝麻免押产品的预授权冻结金额，金额需大于0
        

*   **rent\_sign\_info 支付产品签约信息**
    
    *   credit\_info 芝麻信用服务信息。zm\_service\_id、category\_id联系BD提供
        
    *   fund\_auth\_freeze\_info.freeze\_notify\_url 预授权冻结成功通知地址，必传
        
    *   rent\_deduct\_info.sign\_scene 如订单需要代扣，则固定传入sign\_scene=RENT\_DEDUCT
        

*   **item\_infos 商品信息**
    
    *   当订单类型为 RENT 时需要传入。数组，汽车订阅限定只能传1项。
        
    *   **item\_type固定传入CAR\_ITEM**
        
    *   如商家已对接支付宝商品库（对接见 [alipay.open.app.item.create 小程序商品创建接口](https://opendocs.alipay.com/mini/4880cf68_alipay.open.app.item.create?referPath=0dqecn_e89fd65e)），可直接传入商品的 out\_item\_id ，out\_sku\_id，其余商品字段会自动映射。如未对接商品库，则需商家传入商品详细信息
        
    *   如商品库信息与商家传入的信息有冲突，则以商家传入信息为主。
        
    *   商品库商品编码获取路径为 [商家平台](b.alipay.com) > **工作台** > **商品** \> **商品列表**。  
        

*   **delivery\_info 履约配送信息**
    
    *   delivery\_type固定传入SELFPICK
        

*   **sub\_merchant 二级商户信息**
    
    *   汽车订阅目前不涉及
        
        *   如商家有自己的二级商户或已对接[直付通产品](https://opendocs.alipay.com/solution/0denvi)，需要在创单时传入 merchant\_id，二级商户ID，后续在订单支付-支付创建 接口时会自动关联二级商户信息，无需再在支付时指定。
            

*   **trade\_app\_id 异主体收单信息**
    
    *   trade\_app\_id 当商家收单appid与下单appid不一致时必传此字段，后端会自动关联收单商户的appid
        

*   **biz\_identity** **订单业务身份** 固定传 CAR\_SUBSCRIPTION ，_**由于汽车订阅是邀请开放类目，需要找支付宝业务人员对商户的APPID和PID开白名单**_
    

*   **其他**
    
    *   outer\_order\_source 端外订单来源：汽车订阅不需传入
        
    *   订单信息查询，可使用[alipay.commerce.rent.order.query（租赁订单查询接口）](https://opendocs.alipay.com/solution/a8f9d9cb_alipay.commerce.rent.order.query?referPath=0h845z_d2c7ca4f)查询租赁订单的信息与账单信息
        

（2）请求示例

```json
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.create&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
-F 'app_auth_token=${app_auth_token}' \
-F 'biz_content={
  "out_order_id":"6823789339978248",
  "order_type":"RENT",
  "title":"iPhone 16 Pro Max",
  "source_id":"MjAfSVNfTlVMA==",
  "path_info":{
    "protocols":[
      {
        "protocol_path":"pages/home/index\u0000?xxxx=xx",
        "protocol_name":"xxx租赁协议"
      }
    ],
    "detail_path":"/pages/index/index?orderId=10190608609185"
  },
  "buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
  "price_info":{
    "order_price":"5110.00",
    "deposit_price":"4000.00"
  },
  "rent_plan_info":{
    "installments":[
      {
        "plan_pay_time":"2025-06-15 00:00:00",
        "installment_price":"400.00",
        "installment_no":1
      },
      {
        "buyout_price":"4000.00",
        "plan_pay_time":"2025-07-15 00:00:00",
        "installment_price":"400.00",
        "installment_no":2
      }
    ],
    "rent_start_time":"2025-06-15 00:00:00",
    "rent_end_time":"2026-06-14 00:00:00"
  },
  "rent_sign_info":{
    "credit_info":{
      "zm_service_id":"201504232100100******00028594",
      "category_id":"xxxx"
    },
    "fund_auth_freeze_info":{
      "freeze_notify_url":"https://www.merchant.com/***"
    },
    "rent_deduct_info":{
      "sign_scene":"RENT_DEDUCT"
    }
  },
  "item_infos":[
    {
      "item_type":"CAR_ITEM",
      "out_sku_id":"34567576565656",
      "out_item_id":"12345",
      "item_cnt":"1",
      "item_name":"iPhone16",
      "item_description":"全新iPhone 16 Pro Max，极速审核发货",
      "sale_price":"88.88",
      "item_value":"688.88"
    }
  ],
  "delivery_info":{
    "shop_info":{
      "address":"门店地址",
      "tel_number":"132********",
      "name":"门店名称"
    },
    "delivery_type":"SELFPICK"
  },
  "default_receiving_address":{
    "detailed_address":"中国浙江省杭州市西湖区xx小区xx号",
    "tel_number":"132*******",
    "receiver_name":"张三"
  }
}' 
```

（3）响应示例

```json
{
  "alipay_commerce_rent_order_create_response": {
    "code": "10000",
    "msg": "Success",
    "order_id": "2025042321001004720200028594",
    "out_order_id": "6823789339978248"
  },
  "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
}
```

## 步骤三：订单签约

接口文档：[alipay.commerce.rent.order.sign(租赁订单签约)](https://opendocs.alipay.com/solution/ba884d5b_alipay.commerce.rent.order.sign?scene=common&pathHash=8bb66b2a)

1、关键参数

*   **集成的场景**
    
    *   汽车订阅场景下，下单页插件将自动唤起芝麻免押受理台（无需集成该接口）
        
    *   如果用户关闭退出受理台，则可以在商户自有订单详情页，通过集成该接口的方式再次唤起。
        

*   **rent\_sign\_info 签约参数**
    
    *   参考创建订单接口，保持一致即可
        

2、请求示例

```json
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.sign&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2025042321001004720200028594",
	"rent_sign_info":{
    "fund_auth_freeze_info":{
      "freeze_notify_url":"https://www.merchant.com/***"
    },
    "rent_deduct_info":{
      "sign_scene":"RENT_DEDUCT"
    }
  }
}' 
```

3、响应示例

```json
{
    "alipay_commerce_rent_order_sign_response": {
        "code": "10000",
        "msg": "Success",
        "sign_str": "app_id=2017060101317939&biz_content=%7B%22post_payments%22%3A%22%22%2C%22order_title%22%3A%22XX%E7%A7%xxx",
        "sign_launch_method": "TRADEPAY"
    },
    "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
}
```

获取到签约字符串 sign\_str 之后，小程序前端通过[接入支付产品开通页#方案二](https://opendocs.alipay.com/solution/0ho1xz?pathHash=4fb1c438#%E6%96%B9%E6%A1%88%E4%BA%8C%EF%BC%9A%E9%80%9A%E8%BF%87%E6%8E%A5%E5%8F%A3%E7%AD%BE%E7%BA%A6)中方式唤起芝麻受理台。

## 步骤四：商家审核

1、平台风控结果获取，请求风控咨询接口查询综合风险等级，alipay.commerce.rent.risk.consult(租赁行业风险咨询)

[https://opendocs.alipay.com/solution/305bba67\_alipay.commerce.rent.risk.consult?scene=common&pathHash=ecc22e68](https://opendocs.alipay.com/solution/305bba67_alipay.commerce.rent.risk.consult?scene=common&pathHash=ecc22e68)

consult\_risk\_types｜咨询的风险类型  参数值 COMPREHENSIVE\_RISK

风控建议：新增补充风控等级和审核结果映射规则，isk\_level字段取值范围T1-T10（T1 ~ T4: 低风险；T5 ~ T6: 中风险；T7 ~ T8: 高风险；T9 ~ T10: 极高风险），审核建议：T1-T4 直接通过；T5-T6 综合判断；T7-T10 建议拒绝。

本功能由 租安盾-汽车订阅定制版风控提供，商家可参考并结合自身风控水位进行管理，如需要特色风控策略可联系芝麻信用业务同学对接风控专家实现。

2、同步审核结果，用户下单后完成 ，支付宝租赁交易订单会自动更新为 SIGNED 用户已签约。

**商家风控审核通过：**调用 alipay.commerce.rent.order.fulfillment.approve 接口推动订单状态至 APPROVED

如果商家风控审核拒绝则调关单接口关闭订单；

[https://opendocs.alipay.com/solution/33cfb907\_alipay.commerce.rent.order.fulfillment.approve?scene=common&pathHash=a43104c1](https://opendocs.alipay.com/solution/33cfb907_alipay.commerce.rent.order.fulfillment.approve?scene=common&pathHash=a43104c1)

## 请求示例

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.fulfillment.approve&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"out_order_id":"6823789339978248",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5"
}' 

```

## 步骤五：订单支付

扣款规则参考租赁行业交易管理规范[https://rulecenter.alipay.com/gateway/details?docId=323300234](https://rulecenter.alipay.com/gateway/details?docId=323300234)

[**alipay.commerce.rent.order.pay(租赁订单支付)**](https://opendocs.alipay.com/solution/864d1da5_alipay.commerce.rent.order.pay?scene=common&pathHash=dac028d7)

*   **pay\_method 支付方式**
    
    *   RENT\_DEDUCT 代扣
        
    *   PRE\_AUTH 预授权转支付
        
    *   JSAPI 主动支付
        

*   **pay\_items 当前这笔支付的费用项明细列表**
    
    *   type 费项类型。RENT 表示订阅金
        
    *   installment\_no 期号。type为RENT时必传
        

*   **其他**
    
    *   订单支付接口的使用受租赁行业规范管控，哪些费项类型支持使用什么支付方式等细则参考：[3.2.2【8.22更新】订单支付接口](https://xfpzl.yuque.com/dgw58m/apg2ie/krlamdgkonw48d6v#ppha4)。
        
    *   退款请参考 [alipay.trade.refund(统一收单交易退款接口)](https://opendocs.alipay.com/solution/b4dd6a97_alipay.trade.refund?scene=common&pathHash=d975e036)
        

（1）请求示例

```json
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.pay&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"out_trade_no":"20250615test0001",
	"order_id":"2025061501502300000005250010123456",
	"pay_method":"RENT_DEDUCT",
	"pay_amount":"300.00",
	"pay_items":[
		{
			"type":"RENT",
			"pay_amount":"300.00",
			"installment_no":1
		}
	],
	"pay_notify_url":"http://api.xxxmerchant.com/receive_notify",
	"pay_timeout_express":"30m"
}' 
```

（2）响应示例

```json
{
    "alipay_commerce_rent_order_pay_response": {
        "code": "10000",
        "msg": "Success",
        "trade_no": "2025112011001004330000121536",
        "out_trade_no": "6823789339978248",
        "order_id": "2025042321001004720200028594",
        "pay_amount": "300.00"
    },
    "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
}
```

JSAPI支付的场景下，获取到trade\_no后，在小程序端调用 [my.tradePay（发起支付）](https://opendocs.alipay.com/mini/05xhsr?referPath=05x9ku_a7b61cca)接口唤起支付宝收银台，引导用户完成支付。

## 步骤六：交车/还车

商家审核通过后可调用alipay.commerce.rent.order.fulfillment.send同步订单发货信息，type 传商家发货MERCHANT\_DELIVERY\_SEND，商家发货后主订单状态变更为 DELIVERED。

[https://opendocs.alipay.com/solution/8cb23ca6\_alipay.commerce.rent.order.fulfillment.send?scene=common&pathHash=88983d4b](https://opendocs.alipay.com/solution/8cb23ca6_alipay.commerce.rent.order.fulfillment.send?scene=common&pathHash=88983d4b)

关键参数

*   type可以区分商户发货还是用户寄回
    

请求示例

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.fulfillment.send&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"type":"MERCHANT_DELIVERY_SEND"
}' 
```

用户还车时，商家需要调用alipay.commerce.rent.order.fulfillment.send接口来同步订单状态，type传用户寄回:USER\_DELIVERY\_SEND，主订单状态将变更为 RETURN\_DELIVERED。

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.fulfillment.send&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"type":"USER_DELIVERY_SEND"
}' 
```

## 步骤7：用户/商家确认收货接口

接口

alipay.commerce.rent.order.fulfillment.receive(履约订单确认收货)

[https://opendocs.alipay.com/solution/40da0088\_alipay.commerce.rent.order.fulfillment.receive?scene=common&pathHash=a9f69bb9](https://opendocs.alipay.com/solution/40da0088_alipay.commerce.rent.order.fulfillment.receive?scene=common&pathHash=a9f69bb9)

关键参数

*   type可以区分用户收货还是商户收到用户寄回的货
    

请求示例

用户确认收货后，商家可调用 alipay.commerce.rent.order.fulfillment.receive 完成用户收货，type 传商家发货，用户已经收货:MERCHANT\_DELIVERY\_RECEIVED，确认收货后主订单状态变更为 RECEIVED。

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.fulfillment.receive&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"type":"MERCHANT_DELIVERY_RECEIVED"
}' 
```

商家确认用户归还可调用 alipay.commerce.rent.order.fulfillment.receive 接口同步订单状态；

注意：此时主订单状态变更为RETURN\_RECEIVED，不会释放用户押金，解除代扣等，商家完成设备验收并完成相应押金退还、售后赔付扣款等操作后，商家再操作同步订单归还完结；

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.fulfillment.receive&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"type":"USER_DELIVERY_RECEIVED"
}' 
```

## 步骤8：订单归还完结

接口：alipay.commerce.rent.order.fulfillment.finish(履约订单完成)

[https://opendocs.alipay.com/solution/539698ab\_alipay.commerce.rent.order.fulfillment.finish?scene=common&pathHash=73ad9b39](https://opendocs.alipay.com/solution/539698ab_alipay.commerce.rent.order.fulfillment.finish?scene=common&pathHash=73ad9b39)

场景：用户归还车辆，商家如过验收确认物车辆无损可调用 [alipay.commerce.rent.order.fulfillment.finish](https://opendocs.alipay.com/solution/539698ab_alipay.commerce.rent.order.fulfillment.finish?scene=common&pathHash=f41c01dc) 推动订单到完结态。商家验收时发现有物损需要用户进行赔付，详见订单售后能力。

1.  **到期归还：**status传： 用户租赁到期，已经归还商品USER\_RETURNED。
    
2.  **提前归还：**如未到租期结束时间，用户提前归还商品，status传：用户提前完成归还USER\_RETURNED\_IN\_ADVANCE
    
3.  **其他完结**：如用户续租后商品无残值，用户无需归还，**status**传：其他场景下的订单完结OTHER
    

订单完结后，系统将自动操作预授权解冻以及该订单关联的代扣协议会自动解除。

## 请求示例

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.fulfillment.finish&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"out_order_id":"6823789339978248",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"status":"USER_RETURNED"
}' 
```

## 步骤9：售后部分

|  | **用户/商家在商家私域发起售后**<br>**（本文档重点介绍）** |
| --- | --- |
| **发起方** | ✅用户<br>✅商家 |
| **售后单创建方** | ✅商家 |
| **售后类型** | ✅取消订单<br>✅赔付 |
| **售后接口** | *   [alipay.commerce.rent.order.aftersale.create(租赁售后单创建)](https://opendocs.alipay.com/solution/ca658e3e_alipay.commerce.rent.order.aftersale.create?scene=common&pathHash=4b663677)<br>    <br>*   [alipay.commerce.rent.order.aftersale.confirm(租赁售后订单商户审核接口)](https://opendocs.alipay.com/solution/07247f17_alipay.commerce.rent.order.aftersale.confirm?scene=common&pathHash=e7639ff0) |

| **售后类型** | **原因分类** | **子原因** | **可发起的订单状态** | **是否涉及商家退款** | **退款金额** | **是否涉及用户支付** | **支付费项** | **对订单状态影响** |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 取消订单 | 取消订单 | 不想要了... | 订单确认收货前（商家已确认、已发货），可发起 | ✅ | <=已支付费项 | ✅ | 违约金 | 售后完结，关闭订单 |
|  | 取消订单 | 商家风控拒绝/缺货等 |  | ✅ | \=已支付费项 | ❌ |  |
| 赔付 | 用户违约 | 提前归还 | 订单确认收货后，可发起 | ❌ | 无 | ✅ | 违约金 | ❌ |
|  |  | 逾期归还 | 订单确认收货后，可发起 | ❌ | 无 | ✅ | 违约金 | ❌ |
|  | 设备问题 | 物损赔付 | 订单确认收货后，可发起 | ❌ | 无 | ✅ | 赔付金 | ❌ |
|  |  | 车辆维修 | 订单确认收货后，可发起 | ❌ | 无 | ✅ | 赔付金 |  |
|  |  | 车辆丢失 | 订单确认收货后，可发起 | ❌ | 无 | ✅ | 赔付金 | 售后完结，关闭订单 |
|  |  | 激活折旧 | 订单发货后，可发起 | ❌ | 无 | ✅ | 赔付金 |  |

#### 1、取消订单

**售后场景：**在++用户确认签收租赁物前++，用户、商家任何一方均可发起取消订单；

**赔付金收取：**参考支付宝定的扣款规范[@耘业](https://yuque.antfin.com/liyunhe.lyh)，取消订单售后支持收取物物损等的用户赔付金；

**售后退款：**如果订单已支付了订阅费用等其他费项，取消订单售后需要商家++自行调用++[alipay.trade.refund(统一收单交易退款接口)](https://opendocs.alipay.com/mini/824da765_alipay.trade.refund?scene=common&pathHash=b18b975d)完成对应费项退款。

**【取消订单售后流程**】

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/Mp7ld7b5YvkoMOBQ/img/2d8dcd64-0dc1-4b14-8fb4-f7345dbde0a5.png)

#### 2、赔付

*   **售后场景：**在++用户确认收货后++，由于用户原因导致车辆损坏/丢失、提前归还违约、逾期归还违约等场景，商家可发起++赔付类售后单++；
    

**退款：**商家自行调用[alipay.trade.refund(统一收单交易退款接口)](https://opendocs.alipay.com/solution/b4dd6a97_alipay.trade.refund?scene=common&pathHash=394e6b4e)发起退款；

**退货=提前归还**：用户签收后如提前退货，需要用户归还设备，商家确认收货后，商家调用 [alipay.commerce.rent.order.fulfillment.finish](https://opendocs.alipay.com/solution/539698ab_alipay.commerce.rent.order.fulfillment.finish?scene=common&pathHash=f41c01dc)接口更新状态「用户提前完成归还USER\_RETURNED\_IN\_ADVANCE」，即订单完结；

*   **赔付金/违约金收取：**参考《消费品租赁行业交易管理规范》，赔付类售后单支持收取租赁物损坏/丢失赔付金、提前归还/逾期归还违约金。
    

注意：赔付金/违约金收取需要在订单完结前发起，订单完结后不支持再发起支付；

![image](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/Mp7ld7b5YvkoMOBQ/img/89fade80-5c1e-4592-ae18-8cd7d6a2250d.png)

3、请求示例

（1）alipay.commerce.rent.order.aftersale.create(租赁售后单创建)

[https://opendocs.alipay.com/solution/ca658e3e\_alipay.commerce.rent.order.aftersale.create?scene=common&pathHash=6ba6dc85](https://opendocs.alipay.com/solution/ca658e3e_alipay.commerce.rent.order.aftersale.create?scene=common&pathHash=6ba6dc85)

请求示例

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.aftersale.create&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"buyer_id":"20880003050001",
	"buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"order_id":"2025042321001004720200028594",
	"aftersale_type":"ORDER_CANCEL",
	"out_aftersale_id":"682378934923910",
	"path":"/pages/index/index?orderId=10190608609185",
	"reason_code":"NO_NEED",
	"additional_description":"用户不想订阅了",
	"additional_media_list":[
		{
			"type":"image",
			"url":"https://img.xxx.com?imgId=xxx"
		}
	]
}' 
```
```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.aftersale.create&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"buyer_id":"20880003050001",
	"buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"order_id":"2025042321001004720200028594",
	"aftersale_type":"COMPENSATION",
	"out_aftersale_id":"682378934923910",
	"path":"/pages/index/index?orderId=10190608609185",
	"reason_code":"ITEM_DAMAGED",
	"additional_description":"物品损坏赔付",
	"additional_media_list":[
		{
			"type":"image",
			"url":"https://img.xxx.com?imgId=xxx"
		}
	],
	"pay_items":[
		{
			"pay_amount":"15.00",
			"type":"INDEMNITY"
		}
	]
}' 
```

（2）alipay.commerce.rent.order.aftersale.confirm(租赁订单售后处理接口)

[https://opendocs.alipay.com/solution/07247f17\_alipay.commerce.rent.order.aftersale.confirm?scene=common&pathHash=aa1e22b8](https://opendocs.alipay.com/solution/07247f17_alipay.commerce.rent.order.aftersale.confirm?scene=common&pathHash=aa1e22b8)

关键参数

*   私域场景：商家只需同步售后终态，即只会用到USER\_CANCEL\_APPLY和AFTERSALE\_FINISH；
    

请求示例

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.aftersale.confirm&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"buyer_id":"20880003050001",
	"buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"aftersale_id":"2025060510020721020000000000",
	"operation_type":"AFTERSALE_FINISH"
}' 
```
```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.aftersale.confirm&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"buyer_id":"20880003050001",
	"buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"aftersale_id":"2025060510020721020000000000",
	"operation_type":"USER_CANCEL_APPLY"
}' 
```

（3）接口alipay.commerce.rent.order.aftersale.notify(租赁售后订单消息通知)1

[https://opendocs.alipay.com/solution/887dee78\_alipay.commerce.rent.order.aftersale.notify?scene=common&pathHash=09b9eb62](https://opendocs.alipay.com/solution/887dee78_alipay.commerce.rent.order.aftersale.notify?scene=common&pathHash=09b9eb62)

关键参数

*   source\_type 私域场景下只会有 MERCHANT
    

请求示例

```sql
curl -X POST 'NOTIFY_URL' \
--header 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
--data-urlencode 'charset=UTF-8' \
--data-urlencode 'biz_content={
	"buyer_id":"20880003050001",
	"buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"order_id":"2024031401502300000003710001413316",
	"out_order_id":"6823789339978248",
	"aftersale_type":"ORDER_CANCEL",
	"aftersale_id":"2015042321001004720200028594",
	"out_aftersale_id":"1126823789339978248",
	"aftersale_status":"APPROVING",
	"source_type":"MERCHANT",
	"create_time":"2025-05-06 11:22:33",
	"operation_type":"ORDER_CANCEL",
	"operation_time":"2025-05-06 11:22:33",
	"need_operation":"false",
	"reason_description":"押金问题-押金太高",
	"additional_description":"不想要了",
	"additional_media_list":[
		{
			"type":"image",
			"url":"https://img.xxx.com?imgId=xxx"
		}
	]
}' \
--data-urlencode 'utc_timestamp=${now}' \
--data-urlencode 'sign=${sign}' \
--data-urlencode 'app_id=${appid}' \
--data-urlencode 'version=1.1' \
--data-urlencode 'sign_type=RSA2' \
--data-urlencode 'notify_id=${notify_id}' \
--data-urlencode 'msg_method=alipay.commerce.rent.order.aftersale.notify'
```

## 步骤10：修改租赁的开始结束时间接口

```sql
alipay.commerce.rent.order.modify
```

[https://opendocs.alipay.com/solution/1c272104\_alipay.commerce.rent.order.modify?scene=common&pathHash=161294dd](https://opendocs.alipay.com/solution/1c272104_alipay.commerce.rent.order.modify?scene=common&pathHash=161294dd)

## 关键参数

*   type=RENT\_PLAN\_TIME时，可以修改租赁的开始结束时间
    

## 请求示例

```sql
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.modify&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2015042321001004720200028594",
	"out_order_id":"6823789339978248",
	"user_id":"20880003050001",
	"open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"type":"RENT_PLAN_TIME",
	"rent_plan_info":{
		"installments":[
			{
				"plan_pay_time":"2025-06-15 08:00:00",
				"installment_no":1
			}
		],
		"rent_start_time":"2025-06-15 00:00:00",
		"rent_end_time":"2025-06-20 00:00:00"
	}
}' 
```

## 步骤11:用户续订

仍调用本接口（ alipay.commerce.rent.order.create ）创建新订单进行续订，新订单需要用户重新签订代扣与预授权相关协议，续订新单在免押预授权时默认“续租必过”；。 

*   order\_type ：RELET
    
*   relet\_info ：传入原始订阅订单的订单号信息；后续再次续订时，传入上一次续订的订单号
    
*   续订订单无需商家审核及发货，原订单的租期结束日期次日系统将自动推进续订订单到确认收货（支持商家主动调用确认收货接口提前确认收货）；
    
*   续订成功后，原订单租期计划完整支付完成后，原订单自动完结
    

## 步骤12:用户订阅到期购买

用户订阅到期（完成所有订阅金支付），与商家协商一致，可以发起购买请求。仍调用本接口（ alipay.commerce.rent.order.create ）创建购买订单，平台自动发起jsapi支付，商家获取tradeno后，商家调用[my.tradePay](https://opendocs.alipay.com/solution/b934d23a_my.tradePay?pathHash=dc58408e)拉起收银台完成支付，购买订单支付成功后，原订单自动完结。

*   订单类型 order\_type：BUYOUT 
    
*   租赁购买创单信息 buyout\_info.origin\_order\_id：传入原始订阅订单信息
    

## 步骤13:端外支付同步

1、接口：[alipay.commerce.rent.order.pay.sync(租赁订单支付主动同步)](https://opendocs.alipay.com/solution/4b455284_alipay.commerce.rent.order.pay.sync?scene=common&pathHash=e1674154)

2、使用场景

通过微信、线下转账等非支付宝交易完成支付收款后，通过此接口同步已支付的费项

3、请求示例

```json
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.pay.sync&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"out_trade_no":"20250615test0001",
	"order_id":"2025061501502300000005250010123456",
	"pay_items":[
		{
			"type":"RENT",
			"pay_amount":"300.00",
			"installment_no":2
		},
		{
			"type":"RENT",
			"pay_amount":"300.00",
			"installment_no":3
		}
	],
	"pay_channel":"OTHER"
}' 
```

4、响应示例

```json
{
    "alipay_commerce_rent_order_pay_sync_response": {
        "code": "10000",
        "msg": "Success",
        "out_trade_no": "6823789339978248",
        "order_id": "2025042321001004720200028594",
        "pay_amount": "90.00"
    },
    "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
}
```

# 步骤14:取消订单

1、接口

[alipay.commerce.rent.order.close(租赁订单关闭)](https://opendocs.alipay.com/solution/1bcd13ad_alipay.commerce.rent.order.close?scene=common&pathHash=10237c50)

2、关键参数

使用条件

订单状态在用户确认收货前（不含确认收货），允许使用该接口关闭订单

未完成订单签约（预授权冻结）的订单，24小时会超时自动关单。商户也可以通过该接口提前关单

关闭订单前，要求所有已支付费项全额退款完成

3、请求示例

```json
curl 'https://openapi.alipay.com/gateway.do?charset=UTF-8&method=alipay.commerce.rent.order.close&format=json&sign=${sign}&app_id=${appid}&version=1.0&sign_type=RSA2&timestamp=${now}' \
 -F 'app_auth_token=${app_auth_token}' \
 -F 'biz_content={
	"order_id":"2025042321001004720200028594",
	"out_order_id":"6823789339978248",
	"buyer_id":"2088102146225135",
	"buyer_open_id":"074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5",
	"reason_code":"3114",
	"reason_desc":"用户资质问题"
}' 
```

4、响应示例

```json
{
    "alipay_commerce_rent_order_close_response": {
        "code": "10000",
        "msg": "Success"
    },
    "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
}
```

# 四、私域接入指南

## 4.1 配置相关工作

需要找对接的芝麻运营升级信用服务

## 4.2 调用流程

核心变化如下：

*   商品详情页、确认订单页等为商家侧自建页面
    
*   私域芝麻分门槛分下调为650分
    
*   唤起免押签约需要单独单独调用签约API，其中在商家小程序（私域）调用时需要传入预授权类目DEPOSIT\_CAR\_LEASING\_PRI，公域链路仍按之前的预授权类目传入。
    
*   私域订单，商家仍循查询综合风险等级。
    

[请至钉钉文档查看「白板」](https://alidocs.dingtalk.com/i/nodes/R4GpnMqJzG9aabLBcLDe7e2E8Ke0xjE3?cid=79457208891&corpId=ding42f1731ef067e4ae&doc_type=wiki_doc&iframeQuery=anchorId%3DX02mpcadn5oaolrz9ap3ew&utm_medium=im_card&utm_scene=team_space&utm_source=im)