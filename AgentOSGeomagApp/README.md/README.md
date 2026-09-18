# AgentOSGeomagApp — 地磁知识讲解员（Android Agent）

基于 **AgentOS SDK for APK V0.4.12** 开发的地磁科普智能体应用。程序按照
[AgentOS_SDK_Doc_v0.4.12.md](https://github.com/orionagent/agentos-sdk/blob/main/Agent/v0.4.12/AgentOS_SDK_Doc_v0.4.12.md)
的规范实现，对**地磁**进行系统、详细的介绍，覆盖：**原理、应用场景、优势、变化与磁暴、导航、勘探、航天与生物导航、发展历史、常见问题**等主题。

用户可以直接对机器人说“介绍一下地磁”“地磁是怎么产生的”“地磁有什么优势”，
也可以点击页面上的话题卡片，由大模型自动规划并执行对应的 Action，完成**界面展示 + TTS 语音播报**。

---

## 一、功能特性

| 特性 | 说明 |
| --- | --- |
| 语音交互 | 免唤醒收音，用户直接口语提问，大模型自动规划 Action |
| 文本指令 | 点击话题卡片 → `AgentCore.query()` 模拟语音提问（SDK 4.9 节） |
| 知识讲解 | 10 个单一职责 Action，每个主题含结构化图文 + TTS 播报 |
| 角色配置 | `setPersona` / `setStyle` / `setObjective` 三层人设（SDK 3 章） |
| 页面感知 | `AgentCore.uploadInterfaceInfo` 实时上报页面内容（SDK 4.10 节） |
| 外部调用 | `actionRegistry.json` 静态注册，可被其他 AgentOS 应用调用（SDK 2.2.1 节） |

## 二、Action 清单

全部 Action 遵循命名规范 `com.company.module.ACTION_NAME`（简名大写）、单一职责、1–3 参数：

| Action 名称 | 展示名 | 触发示例 |
| --- | --- | --- |
| `com.geomagnet.edu.GEOMAG_OVERVIEW` | 地磁概述 | “地磁是什么？” |
| `com.geomagnet.edu.GEOMAG_PRINCIPLE` | 地磁原理 | “地磁是怎么产生的？” |
| `com.geomagnet.edu.GEOMAG_VARIATION` | 地磁变化与磁暴 | “地磁场会倒转吗？” |
| `com.geomagnet.edu.GEOMAG_APPLICATION` | 地磁应用场景 | “地磁有哪些应用？” |
| `com.geomagnet.edu.GEOMAG_NAVIGATION` | 地磁导航与定位 | “地磁怎么用于导航？” |
| `com.geomagnet.edu.GEOMAG_EXPLORATION` | 磁法勘探与古地磁 | “磁法勘探是怎么回事？” |
| `com.geomagnet.edu.GEOMAG_SPACE_BIOLOGY` | 航天与生物导航 | “动物靠地磁怎么导航？” |
| `com.geomagnet.edu.GEOMAG_ADVANTAGE` | 地磁优势与局限 | “地磁相比 GPS 有什么优势？” |
| `com.geomagnet.edu.GEOMAG_HISTORY` | 地磁学发展历史 | “地磁学历史是怎样的？” |
| `com.geomagnet.edu.GEOMAG_FAQ` | 地磁常见问题 | “指南针为什么指向北？” |

## 三、环境要求（与 SDK 文档一致）

- **Android SDK**：最低支持 API 26（Android 8.0）
- **JDK**：Java 11
- **构建工具**：Gradle 7.5（AGP 7.4.2 / Kotlin 1.8.22，均兼容 JDK 11）
- **运行环境**：AgentOS 产品 ROM V12.6、RobotAPI 12.2.1（机器人设备）

## 四、快速开始

### 1. 申请 AppId 并替换

打开 `app/src/main/assets/actionRegistry.json`，将占位 appId 替换为在**接待后台**申请的真实 AppId：

```json
{ "appId": "app_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "platform": "apk", "actionList": [...] }
```

### 2. 用 Android Studio 打开项目

- 使用 Android Studio（推荐 Flamingo 及以上）打开项目根目录 `AgentOSGeomagApp`；
- Android Studio 会根据 `gradle/wrapper/gradle-wrapper.properties` 自动下载 Gradle 7.5；
- 首次同步会自动从 Maven 仓库 `https://npm.ainirobot.com/repository/maven-public/` 拉取
  `com.orionstar.agent:sdk:0.4.12-SNAPSHOT`（账号 `agentMaven` / `agentMaven` 已配置在 `settings.gradle.kts`）。

> 若使用命令行构建：`gradle wrapper` 生成 wrapper 后执行 `gradlew assembleDebug`。

### 3. 安装运行

- 将 APK 安装到已升级 **AgentOS V12.6** 的机器人设备；
- 进入应用后，对机器人说话即可交互；
- 点击页面话题卡片，可触发同样的讲解流程。

## 五、项目结构

```
AgentOSGeomagApp/
├── settings.gradle.kts          # Maven 仓库（含 AgentOS 私有仓库）
├── build.gradle.kts             # 根构建脚本（AGP / Kotlin 版本）
├── gradle.properties
├── gradle/wrapper/              # Gradle Wrapper 配置
├── app/
│   ├── build.gradle.kts         # SDK 依赖、minSdk 26、Java 11、ViewBinding
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/actionRegistry.json   # Action 静态注册表（对外接口）
│       ├── java/com/geomagnet/edu/
│       │   ├── MainApplication.kt       # AppAgent：人设/目标 + 静态 Action 分发
│       │   ├── MainActivity.kt          # PageAgent：注册 10 个知识 Action
│       │   ├── GeomagnetismContent.kt   # ★ 地磁知识库（全部内容，唯一数据源）
│       │   └── TopicAdapter.kt          # 话题卡片列表
│       └── res/                          # 布局 / 颜色 / 主题 / 图标
└── docs/地磁知识库.md                    # 可读知识库文档（亦可粘贴至接待后台知识库）
```

## 六、实现要点（对应 SDK 文档章节）

| 要求 | 本实现 |
| --- | --- |
| 配置 Maven 仓库（1.2.1） | `settings.gradle.kts` 新增 `npm.ainirobot.com` 仓库与凭据 |
| 添加 SDK 依赖（1.2.2） | `implementation("com.orionstar.agent:sdk:0.4.12-SNAPSHOT")` |
| Action 注册表（1.2.3） | `assets/actionRegistry.json`，静态注册 10 个地磁 Action |
| 创建 AppAgent（1.2.4） | `MainApplication`：`setPersona/setStyle/setObjective` + 动态注册 `Actions.SAY/EXIT` + `onExecuteAction` |
| 创建 PageAgent（1.2.5） | `MainActivity`：`PageAgent(this).registerAction(...)`，页面可见时生效 |
| Action 命名规范（2.1） | `com.geomagnet.edu.GEOMAG_*`，简名大写，desc 具体化 |
| 执行结果通知（2.3.2） | 协程中完成 TTS 后调用 `action.notify()`；失败 `notify(ActionResult(ActionStatus.FAILED))`；`onExecute` 立即返回 `true` 不阻塞 |
| 角色配置（3 章） | App 级三层配置，PageAgent 未覆盖时继承 |
| 文本指令（4.9） | 点击卡片 → `AgentCore.query(topic.queryDemo)` |
| 感知信息上报（4.10） | 页面初始化与切换主题时 `AgentCore.uploadInterfaceInfo(...)` |

## 七、常见问题

**Q：没有机器人设备，能否运行？**
A：本程序依赖 AgentOS ROM（V12.6）提供的语音与 Agent 能力，需在机器人设备上运行；
纯 Android 模拟器无法提供完整交互，但工程本身可在 Android Studio 中正常编译。

**Q：地磁内容是否可靠？**
A：内容要点（磁场强度、地磁三要素、IGRF-14、磁极漂移速度、布容–松山界限、张衡一号/Swarm 等）
均依据公开科学资料核实，详见 `docs/地磁知识库.md` 末尾的资料来源。

**Q：如何增加新的地磁话题？**
A：在 `GeomagnetismContent.kt` 的 `TOPICS` 中新增一条 `GeomagTopic`（含 Action 名称、描述、内容、播报文本），
再同步在 `actionRegistry.json` 中登记，即可自动生成新的可语音触发的 Action。
