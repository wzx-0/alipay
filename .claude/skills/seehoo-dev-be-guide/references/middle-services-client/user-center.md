# 用户中心 — UserExternalHelper

## 基本信息

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.UserExternalHelper` |
| 源码 | `business-service-plugins/middle-service-api-client` |
| 用途 | 用户查询、部门用户、角色用户 |

## 注入方式

```java
@Autowired
private UserExternalHelper userExternalHelper;
```

## 使用示例

```java
import cn.seehoo.infra.plugin.dto.UserQueryDto;
import cn.seehoo.infra.plugin.dto.UserResultDto;
import cn.seehoo.spg.user.vo.UserVO;
import java.util.List;

// 1. 查询部门下所有用户
List<UserResultDto> deptUsers = userExternalHelper.queryUserListByDeptId("1001");

// 2. 查询角色下所有用户
List<UserResultDto> roleUsers = userExternalHelper.queryUserListByRoleId("2001");

// 3. 查询部门主管（部门与角色交集）
List<UserResultDto> managers = userExternalHelper.queryDeptMainUser("1001", "2001");

// 4. 复杂条件查询
UserQueryDto query = new UserQueryDto();
query.setDepartmentId("1001");
List<UserVO> users = userExternalHelper.searchUser(query);
```

## DTO 说明

| DTO 类 | 包路径 | 用途 |
|--------|--------|------|
| `UserQueryDto` | `cn.seehoo.infra.plugin.dto` | 查询入参 |
| `UserResultDto` | `cn.seehoo.infra.plugin.dto` | 用户查询结果 |
| `UserVO` | `cn.seehoo.spg.user.vo` | 用户详细信息 |

> **更多方法**：查阅 `business-service-plugins/middle-service-api-client` 源码，已存在的方法直接调用，不存在的标记 TODO。

## ⚠️ 注意

禁止直接查询用户表，所有用户相关查询统一通过 `UserExternalHelper` 调用。
