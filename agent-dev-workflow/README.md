# Agent 开发工作流

基于 Agent、SOP 与 Skill 的可执行开发流程体系 ，覆盖从需求分析到开发自测归档的全链路。

## 快速开始

1. 将 PRD 文档放入 `inputs/` 目录，命名格式：`PRD-{需求编号}-{功能名称}.md`
2. 在对话中发起指令，例如：「执行 stage01」或「全流程执行」

## 📁 项目目录结构

| 目录                  | 用途                 |
|---------------------|--------------------|
| `inputs/`           | 开发阶段输入文档           |
| `sops/`             | 开发环节执行规范集合         |
| `todos/`            | 过程跟踪：TODO 清单       |
| `artifacts/`        | 非代码产物目录，按环节划分子目录：  |
| ↳ `stage01-功能分析/`   | 功能清单、模块边界等         |
| ↳ `stage02-技术设计/`   | ER图、DDL、API定义/交互图等 |
| ↳ `stage03-设计评审/`   | 评审记录、修订确认等         |
| ↳ `stage04-前端编码实现/` | 前端代码编写             |
| ↳ `stage05-后端编码实现/` | 后端代码编写             |
| ↳ `stage06-前端代码审查/` | 审查报告、修复记录等         |
| ↳ `stage07-后端代码审查/` | 审查报告、修复记录等         |
| ↳ `stage08-开发测试/`   | 测试报告、Bug修复记录等      |
| ↳ `stage09-产物归档/`   | 归档清单、交付物索引等        |

## 📂 路径占位符声明

> SOP 中出现的所有 `<占位符>` 均在此声明实际路径。

| 占位符 | 实际值 | 说明 |
|--------|--------|------|
| `<PRD文档>` | `inputs/PRD-{需求编号}-{功能名称}.md` | 产品需求文档 |
| `<TODO文件>` | `todos/{需求编号}-todo.md` | 进度跟踪文件 |
| `<前端FRD>` | `FRD-{需求编号}-{功能名称}-前端.md` | 位于 `<功能分析目录>` |
| `<后端FRD>` | `FRD-{需求编号}-{功能名称}-后端.md` | 位于 `<功能分析目录>` |
| `<API清单>` | `API清单-{需求编号}-{功能名称}.md` | 位于 `<功能分析目录>` |
| `<业务编码清单>` | `业务编码清单-{需求编号}-{功能名称}.md` | 位于 `<功能分析目录>` |
| `<设计评审报告>` | `设计评审报告-{需求编号}-{功能名称}.md` | 位于 `<设计评审目录>` |
| `<前端代码库根>` | `leasing-web/` | 前端项目根 |
| `<后端代码库根>` | `leasing-service/` | 后端项目根 |
| `<功能分析目录>` | `artifacts/stage01-功能分析/` | stage01 产物目录 |
| `<技术设计目录>` | `artifacts/stage02-技术设计/` | stage02 产物目录 |
| `<设计评审目录>` | `artifacts/stage03-设计评审/` | stage03 产物目录 |
| `<前端审查目录>` | `artifacts/stage06-前端代码审查/` | stage06 产物目录 |
| `<后端审查目录>` | `artifacts/stage07-后端代码审查/` | stage07 产物目录 |
| `<开发测试目录>` | `artifacts/stage08-开发测试/` | stage08 产物目录 |
| `<产物归档目录>` | `artifacts/stage09-产物归档/` | stage09 产物目录 |

## 执行者配置

> 主 Agent 执行各 Stage 时的子 Agent 委派依据。

### Stage → 执行者 映射

| Stage | 执行者                       |
|-------|---------------------------|
| stage01 功能分析 | feature-analysis-expert   |
| stage02 技术设计 | tech-design-expert        |
| stage03 设计评审 | tech-design-expert        |
| stage04 前端编码 | frontend-coding-expert    |
| stage05 后端编码 | backend-coding-expert     |
| stage06 前端审查 | frontend-coding-expert    |
| stage07 后端审查 | backend-coding-expert     |
| stage08 开发测试 | backend-coding-expert     |
| stage09 产物归档 | artifact-archive-expert   |

## 开发流程（9 个阶段）

```
stage01 功能分析 → stage02 技术设计 → stage03 设计评审
      ↓
stage04 前端编码    stage05 后端编码
      ↓                ↓
stage06 前端审查    stage07 后端审查
      ↓                ↓
       └──→ stage08 开发测试 → stage09 产物归档
```

每个阶段的详细 SOP 见 `sops/` 目录下对应文件，产出物自动归档至 `artifacts/stageXX-xxx/`。
