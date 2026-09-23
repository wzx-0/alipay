# 通用工具类

> 本文件定义项目常用的工具类使用规范，编码时按需读取。

---

## 一、Hutool 工具包（首选）

日常编码中**优先使用 Hutool**，避免重复造轮子。

### 1.1 字符串处理

```java
import cn.hutool.core.util.StrUtil;

// 判空
StrUtil.isEmpty(str);         // 字符串为空（null 或 ""）
StrUtil.isBlank(str);         // 字符串为空白（null、"" 或纯空格）
StrUtil.isNotEmpty(str);      // 字符串不为空
StrUtil.isNotBlank(str);      // 字符串不为空白

// 格式化
StrUtil.format("hello, {}", "world");  // "hello, world"

// 截取
StrUtil.subPre("hello", 3);   // "hel"
StrUtil.subSuf("hello", 2);   // "llo"
```

### 1.2 日期处理

```java
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.DateTime;

// 获取当前时间
Date now = DateUtil.date();

// 字符串转日期
Date date = DateUtil.parse("2024-01-15", "yyyy-MM-dd");

// 日期格式化
String formatted = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");

// 日期计算
DateTime tomorrow = DateUtil.tomorrow();
DateTime nextWeek = DateUtil.offsetDay(date, 7);
DateTime lastMonth = DateUtil.offsetMonth(date, -1);

// 日期差值
long days = DateUtil.between(date1, date2, DateUnit.DAY);
```

### 1.3 集合处理

```java
import cn.hutool.core.collection.CollUtil;

// 判空
CollUtil.isEmpty(list);
CollUtil.isNotEmpty(list);

// 分组
Map<String, List<User>> groupMap = CollUtil.groupByField(userList, "deptId");
```

### 1.4 数字处理

```java
import cn.hutool.core.util.NumberUtil;

// 四舍五入
BigDecimal result = NumberUtil.round(123.456, 2);

// 加减乘除
BigDecimal sum = NumberUtil.add(1, 2, 3);
BigDecimal product = NumberUtil.mul(2, 3);
```

---

## 二、JSON 处理 — JSONUtil

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.common.JSONUtil` |
| 所属依赖 | `cn.seehoo.infra:common-infra-extension`（已由 `framework-constraints.md` 声明必选引入） |
| 用途 | 对象与 JSON 字符串互转 |

**仅可使用以下方法（严格按方法名调用，禁止臆造）：**

```java
String json = JSONUtil.toJSONString(obj);                       // 对象 → JSON 字符串
User user  = JSONUtil.parseObject(json, User.class);            // JSON → 对象
List<User> list = JSONUtil.parseArray(json, User.class);        // JSON → List
```

**禁止事项：**
- 禁止使用 `cn.hutool.json.JSONUtil`、`com.alibaba.fastjson.JSON`、`JSONObject.parseObject()` 等其他 JSON 实现
- 禁止使用 `toJsonString`/`toBean` 等本工具类不存在的方法名
- 禁止手动拼接 JSON 字符串

---

## 三、对象复制 — BeanUtils

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.common.BeanUtils` |
| 所属依赖 | `cn.seehoo.infra:common-infra-extension`（已由 `framework-constraints.md` 声明必选引入） |
| 用途 | Entity ↔ DTO ↔ VO 之间的属性拷贝 |

**仅可使用以下方法（严格按方法名调用，禁止臆造）：**

```java
// 单对象复制：将 src 同名属性拷贝到 dest，返回 dest
TargetVO vo = BeanUtils.mapper(source, new TargetVO());
vo.setXxx(...);                                   // 差异字段后置赋值

// 列表复制：用 supplier 为每个元素创建目标对象
List<TargetVO> voList = BeanUtils.mapperCols(sourceList, TargetVO::new);

// 带缓存的单对象复制（高频调用场景）
TargetVO cached = BeanUtils.cacheMapper(source, new TargetVO());
```

**完整可用方法清单**：
- `mapper(src, dest)` / `mapper(src, dest, Converter)`
- `cacheMapper(src, dest)` / `cacheMapper(src, dest, Converter)`
- `mapperCols(srcList, Supplier)` / `mapperCols(srcList, Supplier, Converter)`

**使用场景：**
- 必须使用：Entity → VO、Entity → DTO、DTO → Entity 等同名字段的拷贝
- 配合使用：先 `mapper`/`mapperCols` 拷贝同名字段，再手动赋值差异字段（字典翻译、类型转换、业务计算）

**禁止事项：**
- 禁止手动逐字段赋值同名字段（如 `targetVO.setName(source.getName())`）
- 禁止使用反射手写拷贝逻辑
- 禁止使用 `org.springframework.beans.BeanUtils`、`cn.hutool.core.bean.BeanUtil`、MapStruct、ModelMapper 等其他实现
- 禁止使用 `copyProperties`/`copyListProperties` 等本工具类不存在的方法名

---

## 四、JEXL 动态表达式

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.jexl.JexlHelper` / `cn.seehoo.infra.plugin.jexl.JexlEngineFactory` |
| 所属依赖 | `cn.seehoo.infra:common-infra-extension`（已由 `framework-constraints.md` 声明必选引入） |
| 用途 | 动态表达式计算，适合经常变化不想发版的业务规则 |

### 使用示例

```java
import cn.seehoo.infra.plugin.jexl.JexlHelper;
import java.util.HashMap;
import java.util.Map;

public void jexlDemo() {
    Map<String, Object> context = new HashMap<>();
    context.put("amount", 10000);
    context.put("rate", 0.05);

    Object result = JexlHelper.execute("amount * rate > 500", context);
    // 结果：true
}
```

---

## 五、项目自有工具类

| 工具类 | 包路径 | 功能 |
|--------|--------|------|
| `MoneyToChineseUtil` | `cn.seehoo.infra.plugin.util.MoneyToChineseUtil` | 金额转中文大写 |
| `OpenTraceUtil` | `cn.seehoo.infra.plugin.util.OpenTraceUtil` | 链路追踪工具 |
| `StringReplaceUtil` | `cn.seehoo.infra.plugin.util.StringReplaceUtil` | 字符串替换工具 |
| `DecimalUtil` | `cn.seehoo.infra.plugin.util.DecimalUtil` | 小数处理工具 |

### 金额转中文示例

```java
import cn.seehoo.infra.plugin.util.MoneyToChineseUtil;
import java.math.BigDecimal;

// amount 从数据库或接口获取，此处仅演示转换调用
BigDecimal amount = getAmountFromDb();
String chinese = MoneyToChineseUtil.convert(amount);
```

---

## 六、分页规范

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.spg.commons.core.page.PageDto<T>` / `cn.seehoo.spg.commons.core.page.CommonPage<T>` |
| 所属依赖 | `cn.seehoo.spg.commons:commons-core:2.0.5-SNAPSHOT` |
| 用途 | 统一分页入参与反参 |

**使用前必须检查工程 `pom.xml` 是否已引入 `commons-core`，未引入则先添加：**

```xml
<dependency>
    <groupId>cn.seehoo.spg.commons</groupId>
    <artifactId>commons-core</artifactId>
    <version>2.0.5-SNAPSHOT</version>
</dependency>
```

**仅可使用以下方法（严格按方法名调用，禁止臆造）：**

```java
// 入参：取业务查询条件用 getParams()，取分页参数用 getPageNo()/getPageSize()
QueryDTO query = pageDto.getParams();
Page<Entity> page = new Page<>(pageDto.getPageNo(), pageDto.getPageSize());
IPage<Entity> entityPage = mapper.xxxPageMethod(page, query);  // mapper 的具名分页方法，按实际业务命名定义

// 反参写法 A：VO 字段与 Entity 完全一致 → convert(IPage, Class)
return CommonPage.convert(entityPage, TargetVO.class);

// 反参写法 B：需要回填差异字段 → 手动构造
List<TargetVO> voList = BeanUtils.mapperCols(entityPage.getRecords(), TargetVO::new);
CommonPage<TargetVO> result = new CommonPage<>(voList);
result.setTotal(entityPage.getTotal());
result.setCurrent(entityPage.getCurrent());
result.setSize(entityPage.getSize());
result.setPages(entityPage.getPages());
return result;
```

**完整可用方法清单**：
- `PageDto`：`getParams()` / `getPageNo()` / `getPageSize()`
- `CommonPage`：`convert(IPage<T>)` / `convert(IPage<T>, Class<P>)` / `new CommonPage<>()` / `new CommonPage<>(List<T>)` + `setTotal/setCurrent/setSize/setPages/setRecords`

**禁止事项：**
- 禁止自定义分页参数类或返回类
- 禁止直接返回 `IPage`
- 禁止使用 `pageDto.getSearch()`、`CommonPage.convert(IPage, List)` 等本工具类不存在的方法名

---

## 七、类型转换器

主要用于 EasyExcel 导入导出时的类型转换：

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.convert.*` |
| 所属依赖 | `cn.seehoo.infra:common-infra-extension`（已由 `framework-constraints.md` 声明必选引入） |

| 转换器 | 功能 |
|--------|------|
| `CommonConverter` | 通用转换器 |
| `NumberBigDecimalConverter` | Number 转 BigDecimal |

```java
import com.alibaba.excel.annotation.ExcelProperty;
import cn.seehoo.infra.plugin.convert.NumberBigDecimalConverter;
import java.math.BigDecimal;

public class ExcelDTO {

    @ExcelProperty(value = "金额", converter = NumberBigDecimalConverter.class)
    private BigDecimal amount;
}
```
