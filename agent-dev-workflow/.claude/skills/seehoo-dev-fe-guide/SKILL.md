---
name: seehoo-dev-fe-guide
description: 前端编码核心规范——stage04 Step2/3 必须调用，提供通用规范、API 封装层、组件层、页面层四个子规范。Step2 加载 common+api-layer+page-layer；Step3 加载 common+component-layer+page-layer。
---

# seehoo-dev-fe-guide：前端编码核心规范

本 Skill 是 stage04 前端编码环节的核心规范，所有编码产出必须遵循本规范约束。

## 子规范加载规则

根据 SOP 步骤加载对应子规范：

| SOP 步骤         | 加载子规范                                                                            |
| ---------------- | ------------------------------------------------------------------------------------- |
| Step2 基础层落地 | `references/common.md` + `references/api-layer.md` + `references/page-layer.md`       |
| Step3 UI 层实现  | `references/common.md` + `references/component-layer.md` + `references/page-layer.md` |

调用本 Skill 后，请根据当前步骤加载上述子规范文件，它们位于本 Skill 目录下：

- `references/common.md`
- `references/api-layer.md`
- `references/component-layer.md`
- `references/page-layer.md`

## 核心原则

1. **骨架适配优先**：所有命名、目录、技术选型以项目骨架已有方案为准，不引入与骨架不一致的模式。
2. **设计对齐**：代码实现必须与 stage01 FRD-前端 和 stage02 API 定义/交互时序图严格对齐，不得自行增减功能或变更接口。
3. **职责分离**：API 封装层、组件层、页面层各层职责清晰，不跨层混用。
