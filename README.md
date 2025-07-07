# AgentOS SDK 开发指南

## 版本兼容性说明

**请根据您的系统版本选择对应的SDK版本进行开发。**

### 系统版本对应关系

| 系统类型 | 机器人ROM版本 | AgentOS版本 | 支持的SDK版本 |
|---------|-------------|------------|-------------|
| Beta系统 | V10.3.2025051917 | V1.2.0.250515.C | v0.2.2 |
| Release系统 | V10.3.2025070215 | V1.3.0.250630.C | v0.3.x |

> **AgentOS SDK v0.2.2 - Beta系统专用**  
> **[查看v0.2.2版本示例](https://github.com/orionagent/AgentSDKSample/tree/main/AgentRole_v0.2.2)**

### 设备兼容性

- **豹小秘2机器人**
  - Beta系统：仅支持 v0.2.2
  - Release系统：v0.3.3 支持（系统版本即将发布）
- **Mini机器人**
  - Release系统：完全支持 v0.3.3（已发布）

### 当前推荐版本

**AgentOS SDK v0.3.3**

---

**⚠️ 重要提醒**

使用错误的SDK版本可能导致功能异常或无法正常运行。开发前请确认您的系统版本并选择对应的SDK版本。

---

## 概述

AgentOS SDK 为开发者提供了完整的猎户星空智能机器人应用开发平台，集成了大模型能力、机器人控制接口、语音交互系统等核心功能模块。

## 快速开始

### 开发环境要求

- **编程语言**：Java / Kotlin
- **构建工具**：Gradle
- **开发平台**：Android Studio
- **目标平台**：Android

### 核心文档

#### AgentOS SDK 文档
- **主要文档**：[AgentOS_SDK_Doc_v0.3.3.md](Agent/v0.3.3/AgentOS_SDK_Doc_v0.3.3.md)
  - 大模型相关能力接口：对话管理、语音合成、智能交互等
- **API参考**：[API_Reference.md](Agent/v0.3.3/API_Reference.md)
  - 完整的API参考文档，包含所有核心类、接口、方法、属性、构造函数、参数说明、返回值、使用示例等详细说明
- **类路径参考**：[ClassPathList.md](Agent/v0.3.3/ClassPathList.md)
  - 项目中所有关键类的完整包路径
- **示例代码**：[SampleCodes.md](Agent/v0.3.3/SampleCodes.md)
  - 各功能模块的典型实现示例

#### 机器人原生接口
- **RobotOS API**：[RobotAPI.md](Robot/v11.3C/RobotAPI.md)
  - 机器人原生控制接口：运动控制、导航、传感器、相机、充电、定位等

#### 云端服务接口
- **OpenAPI文档**：[https://openapi.orionstar.com/opendocs/zh/index](https://openapi.orionstar.com/opendocs/zh/index)
  - 机器人云端管理平台：涵盖企业信息管理、访客系统、远程控制、数据统计等API服务

### AI辅助开发

#### Cursor AI 集成
- **完整指南**：[AGENTOS_CURSOR_AI_GUIDE.md](https://github.com/orionagent/agentos-sdk/blob/main/AGENTOS_CURSOR_AI_GUIDE.md)
  - Cursor AI 集成步骤详解
  - AgentOS SDK 专用开发规则配置
  - AI 辅助开发最佳实践
- **配置包**：[cursor-rules-dependencies.zip](https://github.com/orionagent/agentos-sdk/blob/main/cursor-rules-dependencies.zip)
  - 包含完整的 Cursor Rules 配置
  - 支持智能代码生成和错误检测
  - 提供专业的 AgentOS SDK 开发建议

---

## 开发规范

### 技术架构与协作关系

#### SDK职责划分
- **AgentOS SDK**：负责大模型集成、智能对话、Action规划执行、语音交互（ASR/TTS）、免唤醒等AI能力
- **RobotAPI**：负责机器人底层控制，包括运动控制、导航、传感器、视觉、地图、充电等硬件能力
- **协作关系**：AgentOS SDK与RobotAPI协作使用，AgentOS SDK处理智能交互层面，RobotAPI处理硬件控制层面

---

**⚠️ 重要：语音和NLP功能迁移**

在AgentOS系统上，RobotAPI原有的ASR、TTS、NLP等语音能力已停用，所有语音和NLP相关功能需要迁移到AgentOS SDK。

**迁移指引**：详细的功能区别和迁移方案请参阅 [RobotAPI.md](Robot/v11.3C/RobotAPI.md)

### 开发流程

#### 标准开发步骤
1. **环境准备**：安装 Android Studio，配置开发环境
2. **文档学习**：阅读 AgentOS SDK 文档和机器人原生接口文档
3. **示例参考**：参考 [SampleCodes.md](Agent/v0.3.3/SampleCodes.md) 获取示例代码
4. **API查阅**：通过 [API_Reference.md](Agent/v0.3.3/API_Reference.md) 了解详细的类和方法使用说明
5. **功能集成**：按需集成 AgentOS SDK 和机器人原生API
6. **测试验证**：测试验证功能实现效果

#### 最佳实践
- **底层控制**：合理使用机器人原生接口进行底层控制
- **异步编程**：遵循异步编程模式，避免阻塞主线程
- **异常处理**：做好异常处理和资源管理

---

## 其他资源

- **常见问题**：[FAQ.md](FAQ.md)
- **Cursor AI 开发指南**：[AGENTOS_CURSOR_AI_GUIDE.md](https://github.com/orionagent/agentos-sdk/blob/main/AGENTOS_CURSOR_AI_GUIDE.md)
- **AI开发规则**：[AI_Rules/](AI_Rules/)

---

## 版本信息

- **当前推荐版本**：AgentOS SDK v0.3.3
- **机器人API版本**：v11.3C
- **文档更新时间**：2025年07月05日
