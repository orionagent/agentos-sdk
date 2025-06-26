# AgentOS SDK 开发指南

## 当前AgentOS SDK版本
v0.3.3

## 📋 概述
AgentOS SDK 为开发者提供了完整的猎户星空智能机器人应用开发能力，包括大模型集成、机器人控制、语音交互等功能。

## 🚀 快速开始

### 1. 核心文档
- **AgentOS SDK 文档**：[Agent/v0.3.3/AgentOS_SDK_Doc_v0.3.3.md](Agent/v0.3.3/AgentOS_SDK_Doc_v0.3.3.md)
  - 大模型相关能力接口：对话管理、语音合成、智能交互等
- **机器人原生接口**：[Robot/v11.3C/RobotAPI.md](Robot/v11.3C/RobotAPI.md)
  - 机器人原生控制接口：运动控制、导航、传感器、相机、充电、定位等
- **类路径参考**：[Agent/v0.3.3/ClassPathList.md](Agent/v0.3.3/ClassPathList.md)
  - 项目中所有关键类的完整包路径
- **示例代码**：[Agent/v0.3.3/SampleCodes.md](Agent/v0.3.3/SampleCodes.md)
  - 各功能模块的典型实现示例

### 2. 开发环境配置

#### 开发工具
- **编程语言**：Java / Kotlin
- **构建工具**：Gradle
- **开发平台**：Android Studio
- **目标平台**：Android

#### AI辅助开发 (推荐)
如果您使用 Cursor 等 AI 开发工具：
- 将项目提供的 `.cursor` 文件夹放置到项目根目录下
- AI Rules 配置将自动生效，提升开发效率

## 📚 开发规范

### 技术架构
- **大模型功能**：基于 AgentOS SDK 实现对话、语音、智能交互
- **机器人控制**：基于 RobotOS 原生接口实现运动、导航、传感器控制
- **代码风格**：遵循 Android 开发最佳实践

### 开发流程
1. **环境准备**：安装 Android Studio，配置开发环境
2. **文档学习**：阅读 AgentOS SDK 文档和机器人原生接口文档
3. **示例参考**：参考 `SampleCodes.md` 获取示例代码
4. **类路径确认**：通过 `ClassPathList.md` 确认正确的类导入路径
5. **功能集成**：按需集成 AgentOS SDK 和机器人原生API
6. **测试验证**：测试验证功能实现效果

### 最佳实践
- 合理使用机器人原生接口进行底层控制
- 遵循异步编程模式，避免阻塞主线程
- 做好异常处理和资源管理

## 📖 其他资源
- **常见问题**：[FAQ.md](FAQ.md)
- **AI开发规则**：[AI_Rules/](AI_Rules/)

## 📌 版本信息
- **当前推荐版本**：AgentOS SDK v0.3.3
- **机器人API版本**：v11.3C

---
*更新时间：2025年06月25日*
