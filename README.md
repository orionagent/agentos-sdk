# AgentOS SDK Development Documentation

## Version Compatibility and Integration Guide

**This document provides a complete AgentOS SDK integration plan. You must strictly choose the corresponding SDK version based on the target device model and system version.**

📋 **Version Update History**: [View full changelog →](CHANGELOG.md)

### Version Mapping

- **Mini Robot**: Latest version → AgentOS SDK v0.4.5 (recommended)
- **Leopard Secretary 2 Robot**: Latest version → AgentOS SDK v0.4.5 (recommended)

⚠️ **Important**: It is recommended to use the latest ROM version for optimal performance and stability.

### Platform Compatibility Matrix

#### 🤖 Leopard Secretary 2 Robot

| System Version | ROM Version | AgentOS Version | SDK Version | Maintenance Status |
|---------|---------|------------|---------|---------|
| Release Latest | V11.7 | v1.7.0 | v0.4.5 | ✅ Latest version (recommended) |
| Release Latest | V11.6.2025092601 | v1.6.0.250925.C | v0.4.4 | ✅ Stable version |
| Release | V11.4.2025082001 | V1.4.0.250818.C | v0.3.7 | ⚠️ Legacy version |


#### 🔹 Mini Robot

| System Version | ROM Version | AgentOS Version | SDK Version | Maintenance Status |
|---------|---------|------------|---------|---------|
| Release Latest | V11.7 | v1.7.0 | v0.4.5 | ✅ Latest version (recommended) |
| Release Latest | V11.6.2025100912 | V1.6.0.250925C | v0.4.4 | ✅ Stable version |
| Release Enhanced | V10.3.2025071101 | V1.3.0.250630.C | v0.3.5 | ⚠️ Legacy version |

> **💡 Tip**: It is recommended to use the latest Release system for full feature support and the best performance.

### Recommended Production Configuration

- **Leopard Secretary 2 Robot**: **AgentOS SDK v0.4.5** + latest Release system (v1.7.0)
- **Mini Robot**: **AgentOS SDK v0.4.5** + latest Release system (v1.7.0)

---

**⚠️ Integration Notes**

Version mismatch may cause API call exceptions or runtime errors. Be sure to select the SDK version that matches the target device system version to ensure application stability and compatibility.

---

## SDK Overview

AgentOS SDK is the official development toolkit for OrionStar intelligent robots, providing a complete application development solution. The SDK integrates enterprise-grade modules such as large language model capabilities, robot hardware control interfaces, and voice interaction systems, enabling rapid development of intelligent robot applications.

## Quick Start

### Development Environment Requirements

- **Programming language**: Java / Kotlin
- **Build tool**: Gradle
- **Development platform**: Android Studio
- **Target platform**: Android

### Core Documentation

#### AgentOS SDK Documentation
- **SDK Development Documentation**: [AgentOS_SDK_Doc_v0.4.5.md](Agent/v0.4.5/AgentOS_SDK_Doc_v0.4.5.md)
  - LLM-related interfaces: conversation management, text-to-speech, intelligent interaction, and more
  - Newly added wake-word mode control features
  - Applicable to the latest versions of Leopard Secretary 2 and Mini robots
- **API Reference**: [v0.4.5](Agent/v0.4.5/API_Reference.md)
  - Complete API reference including detailed descriptions of all core classes, interfaces, methods, properties, constructors, parameters, return values, and usage examples
- **Class Path Reference**: [v0.4.5](Agent/v0.4.5/ClassPathList.md)
  - Full package paths of all key classes in the project
- **Sample Code**: [v0.4.5](Agent/v0.4.5/SampleCodes.md)
  - Typical implementation examples for each feature module
- **SDK Development Documentation (English)**: [AgentOS_SDK_Doc_v0.4.5_en.md](Agent/v0.4.5/AgentOS_SDK_Doc_v0.4.5_en.md)
  - Comprehensive guide for LLM capabilities, conversation management, TTS, intelligent interaction, wake-free functionality, and more
  - Applicable to the latest versions of Leopard Secretary 2 and Mini robots

#### Robot Native Interfaces
- **RobotOS API**: [RobotAPI.md](Robot/v11.3C/RobotAPI.md)
  - Robot native control interfaces: motion control, navigation, sensors, camera, charging, localization, and more
  - **Important update for v0.4.4+**: The SDK now automatically integrates RobotService dependencies; no manual JAR addition is required
- **RobotOS API (English)**: [RobotAPI_en.md](Robot/v11.3C/RobotAPI_en.md)
  - Complete reference for robot native control interfaces: motion control, navigation, sensors, camera, charging, localization, and more
  - **Important Update for v0.4.4+**: SDK automatically integrates RobotService dependency, no need to manually add jar packages

#### Cloud Service Interfaces
- **OpenAPI Docs**: [https://openapi.orionstar.com/opendocs/zh/index](https://openapi.orionstar.com/opendocs/zh/index)
  - Robot cloud management platform APIs covering enterprise information management, visitor systems, remote control, data analytics, and more

### AI-Assisted Development

#### Cursor AI Integration
- **Complete Guide**: [AGENTOS_CURSOR_AI_GUIDE.md](https://github.com/orionagent/agentos-sdk/blob/main/AGENTOS_CURSOR_AI_GUIDE.md)
  - Detailed Cursor AI integration steps
  - AgentOS SDK-specific development rule configuration
  - Best practices for AI-assisted development
- **Configuration Package**: [cursor-rules-dependencies.zip](https://github.com/orionagent/agentos-sdk/blob/main/cursor-rules-dependencies.zip)
  - Includes complete Cursor Rules configuration
  - Supports intelligent code generation and error detection
  - Provides professional AgentOS SDK development guidance

---

## Development Guidelines

### Technical Architecture and Collaboration

#### SDK Responsibility Split
- **AgentOS SDK**: Handles AI capabilities such as LLM integration, intelligent dialogue, action planning and execution, voice interaction (ASR/TTS), and wake-free mode
- **RobotAPI**: Handles low-level robot control, including motion, navigation, sensors, vision, mapping, charging, and other hardware capabilities
- **Collaboration model**: AgentOS SDK and RobotAPI are used together—AgentOS SDK handles intelligent interaction layers, while RobotAPI handles hardware control layers

---

**⚠️ Important: Voice and NLP Feature Migration**

On the AgentOS system, RobotAPI's original ASR, TTS, NLP, and other voice capabilities are deprecated. All voice and NLP-related features must be migrated to AgentOS SDK.

**Migration Guide**: For detailed capability differences and migration solutions, see [RobotAPI.md](Robot/v11.3C/RobotAPI.md)

### Development Workflow

#### Standard Development Steps
1. **Environment setup**: Install Android Studio and configure the development environment
2. **Documentation study**: Read AgentOS SDK docs and robot native interface docs
3. **Sample reference**: Refer to sample code ([v0.4.5](Agent/v0.4.5/SampleCodes.md))
4. **API lookup**: Use the API reference ([v0.4.5](Agent/v0.4.5/API_Reference.md)) to understand classes and method usage details
5. **Feature integration**: Integrate AgentOS SDK and robot native APIs as needed
6. **Testing and validation**: Test and validate feature implementation results

#### Best Practices
- **Low-level control**: Properly use robot native interfaces for low-level control
- **Asynchronous programming**: Follow asynchronous programming patterns and avoid blocking the main thread
- **Exception handling**: Implement robust exception handling and resource management

---

## Additional Resources

- **FAQ**: [FAQ.md](FAQ.md)
- **Cursor AI Development Guide**: [AGENTOS_CURSOR_AI_GUIDE.md](https://github.com/orionagent/agentos-sdk/blob/main/AGENTOS_CURSOR_AI_GUIDE.md)
- **AI Development Rules**: [AI_Rules/](AI_Rules/)

---

## Version Information

- **Recommended SDK version**: AgentOS SDK v0.4.5 (universal for Leopard Secretary 2 & Mini robots)
- **Robot API version**: v11.3C
- **Document last updated**: November 19, 2025
