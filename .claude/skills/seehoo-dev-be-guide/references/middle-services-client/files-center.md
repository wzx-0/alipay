# 影像中心 — FilesExternalHelper

## 基本信息

| 项目 | 说明 |
|------|------|
| 包路径 | `cn.seehoo.infra.plugin.middleclient.FilesExternalHelper` |
| 源码 | `business-service-plugins/middle-service-api-client` |
| 用途 | 文件上传、下载、查询、删除、克隆、规则校验 |

## 注入方式

```java
@Autowired
private FilesExternalHelper filesExternalHelper;
```

## 使用示例

### 上传文件（覆盖模式）

```java
import cn.seehoo.infra.plugin.dto.FileUploadReqDto;
import cn.seehoo.infra.plugin.middleclient.FilesExternalHelper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

List<FileUploadReqDto> files = new ArrayList<>();
FileUploadReqDto file = new FileUploadReqDto();
file.setBizId("CONTRACT20240001");
file.setFileName("合同.pdf");
file.setSubCategoryCode("contract_scan");
file.setInputStream(inputStream);
files.add(file);

filesExternalHelper.uploadFiles(files);  // 覆盖模式
// filesExternalHelper.uploadFilesStacking(files);  // 叠加模式
```

### 查询文件

```java
import cn.seehoo.infra.plugin.dto.QueryFileReqDto;
import cn.seehoo.infra.plugin.dto.QueryFileResDto;

QueryFileReqDto req = new QueryFileReqDto();
req.setBizId("CONTRACT20240001");
req.setTemplateCode("contract_template");

List<QueryFileResDto> files = filesExternalHelper.searchFiles(req);
```

### 克隆附件

```java
import cn.seehoo.infra.plugin.enums.CloneFileEnum;

filesExternalHelper.cloneFile("CONTRACT20240001", "CONTRACT20240002", CloneFileEnum.CONTRACT_ALL);
```

## DTO 说明

所有 DTO 位于 `cn.seehoo.infra.plugin.dto` 包下。

| DTO 类 | 用途 |
|--------|------|
| `FileUploadReqDto` | 文件上传请求 |
| `QueryFileReqDto` / `QueryFileResDto` | 文件查询 |
| `FilesDownloadQueryDto` | 文件下载 |
| `FileVerifyByRuleReqDto` | 规则校验 |

> **更多方法**：查阅 `business-service-plugins/middle-service-api-client` 源码，已存在的方法直接调用，不存在的标记 TODO。
