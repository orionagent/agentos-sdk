# AgentOS SDK - Cursor AI 开发指南

**版本：** v0.3.3  
**发布时间：** 2025.7.2

---

## 📋 概述

本指南将帮助您在 **Cursor** 中集成 AgentOS SDK 专用开发规则，集成后能够通过 Cursor 快速集成和开发 AgentOS SDK 相关功能，实现革命性的 AI 辅助开发体验。

### 什么是 Cursor AI？

**Cursor** 是一款基于 VS Code 构建的 AI 驱动代码编辑器，它不仅仅是一个代码提示工具，更是一个完整的 AI 编程助手。Cursor 集成了 GPT-4o、Claude 4 Sonnet 等先进 AI 模型，能够：

- 🤖 **AI 代码生成**：通过自然语言描述生成完整的代码块和函数
- 🧠 **智能理解代码库**：深度理解整个项目结构和上下文
- 🔧 **自动化编程任务**：从简单的代码补全到复杂的多文件重构
- 💬 **对话式编程**：与 AI 对话来解决编程问题和优化代码
- 🚀 **Agent 模式**：AI 可以自主完成端到端的编程任务
- 🐛 **智能调试**：自动检测和修复代码错误

### 为什么需要集成 AgentOS SDK 开发规则？

通过集成 AgentOS SDK 专用的 Cursor Rules，您可以：

- 📚 **获得专业知识**：AI 将基于 AgentOS SDK 文档提供专业的开发建议
- 🎯 **精准代码生成**：自动生成符合 AgentOS SDK 规范的代码
- 🔍 **智能错误检测**：识别 AgentOS SDK 使用中的常见错误
- 📖 **即时文档查询**：在编码过程中快速获取 API 文档和示例
- 🚀 **提升开发效率**：减少查阅文档和调试的时间

## 🚀 快速开始

### 1. 准备工作

在开始之前，请确保您已经：
- 安装了 **Cursor AI代码编辑器**（[下载地址](https://cursor.com)）
- 准备好 Android 开发环境
- 了解 AgentOS SDK 基本概念

### 2. 集成 AgentOS SDK 开发规则

#### 步骤 1：下载规则配置包
1. 下载提供的 [`cursor-rules-dependencies.zip`](https://github.com/orionagent/agentos-sdk/blob/main/cursor-rules-dependencies.zip) 文件
2. 确保下载完整且未损坏

#### 步骤 2：解压并集成规则
1. 在您的 Android 项目根目录下解压缩 zip 文件
2. 可以在现有工程中解压，或通过 Android Studio 新建项目后解压

**解压后的目录结构应包含：**
```
your-project/
├── app/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── .gradle/
├── .cursor/          # Cursor 开发规则配置
├── Agent/            # AgentOS SDK 文档
├── Robot/            # 机器人 API 文档
├── README.md         # AgentOS README
└── FAQ.md            # AgentOS 常见问题
```

#### 步骤 3：处理已有 Cursor 配置

**如果您的项目根目录已存在 `.cursor` 文件夹：**

⚠️ **重要提醒**：为避免覆盖现有配置，请手动操作：

1. 打开解压后的 `.cursor/rules/` 文件夹
2. 将 `doc-devguide.mdc` 文件复制到您现有的 `.cursor/rules/` 文件夹中
3. 直接覆盖 `.cursor` 文件夹可能导致您原有的其他 Cursor Rules 配置丢失

#### 步骤 4：验证规则集成

1. 使用 Cursor 打开您的项目
2. 进入 Cursor 设置：`Settings` → `Rules` → `Project rules`
3. 检查 `doc-devguide.mdc` 是否已生效并显示在规则列表中

## 🔧 故障排除

**Q: 开发规则没有生效怎么办？**
A: 请检查：
- `.cursor/rules/doc-devguide.mdc` 文件是否存在
- 重启 Cursor 编辑器
- 确认项目根目录结构正确

---

*最后更新：2025年7月2日*
