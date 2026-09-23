---
name: seehoo-dev-fe-unit-test
description: 前端单元测试规范——stage04 Step4 必须调用，提供测试框架选型、文件命名、覆盖范围、mock 策略等原则性规范。
---

# seehoo-dev-fe-unit-test：前端单元测试规范

本 Skill 是 stage04 Step4（生成单元测试）的核心规范，所有单元测试产出必须遵循本规范约束。

## 测试框架选型

### 原则

- 测试框架以项目骨架 `package.json` 已安装的测试框架为准（如 Vitest / Jest / 等）
- 不引入骨架未安装的测试框架或测试辅助库
- 如果骨架未配置任何测试框架，在 TODO 中记录该阻塞项，不自行安装新框架

## 测试文件命名与目录

### 原则

- 测试文件命名风格遵循骨架既定的测试文件命名约定（如 `*.spec.ts` / `*.test.ts`）
- 测试文件放置在骨架既定的测试目录中（如 `tests/` / `__tests__/` / 同目录放置）
- 不自行创建骨架中不存在的测试目录结构

### 命名对应

- 每个被测试的源文件对应一个测试文件，测试文件名与源文件名关联（如 `LicenseForm.vue` → `LicenseForm.spec.ts`）

## 覆盖范围

### 必须覆盖

- **组件渲染**：验证组件能正常渲染，关键 DOM 结构存在
- **交互逻辑**：验证用户交互（点击、输入、提交等）触发的行为符合预期
- **API 调用 mock**：验证涉及 API 调用的组件在 mock 环境下行为正确

### 不要求覆盖

- 纯布局组件（无交互逻辑）的渲染测试可简化
- Page 级组件的测试可侧重于组件编排是否正确，不深入测试子组件内部逻辑
- 第三方 UI 库组件本身的功能不做测试（信任库的质量）

## Mock 策略

### 原则

- **优先 mock API 层**：通过 mock API 封装函数的返回值来隔离后端依赖，不 mock 组件内部实现细节
- **不 mock 子组件**：测试 View 组件时让其子 UI 组件正常渲染，不 shallow render 或 stub 子组件（骨架测试方案有特殊要求除外）
- **Store mock**：仅在测试需要控制 Store 状态时 mock Store，优先使用骨架已有的 Store 测试辅助方式

### Mock 实现

- Mock 方式遵循骨架既定的 mock 方案（如 Vitest vi.mock / Jest jest.mock）
- Mock 数据结构必须与 API 定义文档的 Response 类型对齐，不自行简化
