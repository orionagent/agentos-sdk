# AgentOS SDK for APK V0.4.5文档

## 📋 版本信息

![SDK](https://img.shields.io/badge/SDK_Version-v0.4.5-blue)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![API](https://img.shields.io/badge/API-26+-orange)
![Language](https://img.shields.io/badge/Language-Kotlin%20%7C%20Java-purple)

## 📚 目录

- [1. 概述](#1-概述)
  - [1.1 环境要求](#11-环境要求)
    - [1.1.1 开发环境](#111-开发环境)
    - [1.1.2 运行环境](#112-运行环境)
  - [1.2 快速开始](#12-快速开始)
    - [1.2.1 配置Maven仓库](#121-配置maven仓库)
    - [1.2.2 添加SDK依赖](#122-添加sdk依赖)
    - [1.2.3 配置Action注册表](#123-配置action注册表)
    - [1.2.4 创建AppAgent](#124-创建appagent)
    - [1.2.5 创建PageAgent](#125-创建pageagent)
    - [1.2.6 完整示例](#126-完整示例)
    - [1.2.7 开发总结](#127-开发总结)
- [2. Action详解](#2-action详解)
  - [2.1 Action概念](#21-action概念)
    - [2.1.1 基础属性](#211-基础属性)
    - [2.1.2 参数定义](#212-参数定义)
  - [2.2 Action注册机制](#22-action注册机制)
    - [2.2.1 App级Action](#221-app级action)
    - [2.2.2 Page级Action](#222-page级action)
  - [2.3 Action执行流程](#23-action执行流程)
    - [2.3.1 执行回调](#231-执行回调)
    - [2.3.2 执行结果通知](#232-执行结果通知)
  - [2.4 系统内置Action](#24-系统内置action)
- [3. Agent角色配置 - 让您的AI更有个性](#3-agent角色配置---让您的ai更有个性)
  - [3.1 概述](#31-概述)
  - [3.2 三大核心配置](#32-三大核心配置)
  - [3.3 配置层级与优先级](#33-配置层级与优先级)
  - [3.4 API接口详解](#34-api接口详解)
  - [3.5 扩展配置能力](#35-扩展配置能力)
  - [3.6 配置最佳实践](#36-配置最佳实践)
  - [3.7 典型应用场景](#37-典型应用场景)
- [4. 核心API接口](#4-核心api接口)
  - [4.1 麦克风控制](#41-麦克风控制)
  - [4.2 免唤醒功能](#42-免唤醒功能)
  - [4.3 ASR与TTS监听](#43-asr与tts监听)
  - [4.4 Agent状态监听](#44-agent状态监听)
  - [4.5 语音条控制](#45-语音条控制)
  - [4.6 TTS语音合成](#46-tts语音合成)
  - [4.7 TTS音频文件播放](#47-tts音频文件播放)
  - [4.8 大模型接口](#48-大模型接口)
  - [4.9 文本指令](#49-文本指令)
  - [4.10 感知信息上报](#410-感知信息上报)
  - [4.11 对话历史管理](#411-对话历史管理)
  - [4.12 禁用大模型规划](#412-禁用大模型规划)
  - [4.13 应用跳转](#413-应用跳转)
  - [4.14 系统Action状态监听](#414-系统action状态监听)
  - [4.15 唤醒词模式控制](#415-唤醒词模式控制)
- [5. 进阶功能](#5-进阶功能)
  - [5.1 注解驱动的Action注册](#51-注解驱动的action注册)
    - [5.1.1 App级动态注册](#511-app级动态注册)
    - [5.1.2 Page级动态注册](#512-page级动态注册)
    - [5.1.3 注解类说明](#513-注解类说明)
- [6. Action设计最佳实践](#6-action设计最佳实践)
  - [6.1 常见设计误区](#61-常见设计误区)
  - [6.2 最佳实践原则](#62-最佳实践原则)
  - [6.3 正确示例：拆分为多个专门Action](#63-正确示例拆分为多个专门action)
  - [6.4 设计检查清单](#64-设计检查清单)
- [7. 项目资源](#7-项目资源)
  - [7.1 示例项目](#71-示例项目)
  - [7.2 开发资源](#72-开发资源)
- [8. 常见问题FAQ](#8-常见问题faq)
- [9. 技术支持](#9-技术支持)

---

# 1. 概述

AgentOS SDK是一个面向Android平台的智能交互开发套件，为开发者提供了构建智能机器人应用的完整解决方案。SDK支持应用级和页面级的Agent开发，实现自然语言交互、智能动作规划和执行等核心功能。

## 1.1 环境要求

### 1.1.1 开发环境

**基础要求**
- **Android SDK**: 最低支持 API 26 (Android 8.0)
- **JDK版本**: Java 11
- **开发语言**: Kotlin / Java
- **构建工具**: Gradle

**推荐开发环境**
- **Android Studio**: 用于基础环境构建和调试
- **Cursor**: 提供AI辅助编码能力，[详细配置指南](../../AGENTOS_CURSOR_AI_GUIDE.md)

### 1.1.2 运行环境

**系统要求**
- **AgentOS产品版本**: V1.3.0.250515
- **RobotAPI版本**: 11.3

## 1.2 快速开始

> **前置条件**: 如果您是Android开发新手，请先安装[Android Studio](https://developer.android.com/studio?hl=zh-cn)，并下载我们提供的空项目模板，使用Android Studio打开后再进行以下步骤。

### 1.2.1 配置Maven仓库

**Groovy DSL配置**

如果您的项目使用Groovy构建脚本，请在项目根目录的`settings.gradle`文件中添加以下配置：

```Groovy
dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
                mavenCentral()
                maven { // 【重要配置】新增的maven仓库
                    credentials {
                        username = "agentMaven"
                        password = "agentMaven"
                    }
                    url "https://npm.ainirobot.com/repository/maven-public/"
                } // 【重要配置结束】
        }
}
```

**Kotlin DSL配置**

如果您的项目使用Kotlin构建脚本，请在项目根目录的`settings.gradle.kts`文件中添加以下配置：

```Kotlin
dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
                mavenCentral()
                maven { // 【重要配置】新增的maven仓库
                    credentials.username = "agentMaven"
                    credentials.password = "agentMaven"
                    url = uri("https://npm.ainirobot.com/repository/maven-public/")
                } // 【重要配置结束】
        }
}
```

### 1.2.2 添加SDK依赖

> **💡 v0.4.4重要更新**: SDK已自动集成RobotService.jar依赖，开发者无需手动添加机器人原生API的jar包。SDK会通过Maven自动管理所有必要的依赖。

**Groovy DSL配置**

在`app/build.gradle`文件中添加以下依赖：

```Groovy
dependencies {
        implementation 'com.orionstar.agent:sdk:0.4.5-SNAPSHOT' // 【重要配置】Agent SDK依赖

        // 以下是Android标准库，默认kotlin项目都会依赖，
        // 如果编译报未找到错误，再添加以下依赖库
        implementation 'androidx.core:core-ktx:1.13.1'
        implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

**Kotlin DSL配置**

在`app/build.gradle.kts`文件中添加以下依赖：

```Kotlin
dependencies {
        implementation("com.orionstar.agent:sdk:0.4.5-SNAPSHOT") // 【重要配置】Agent SDK依赖

        // 以下是Android标准库，默认kotlin项目都会依赖，
        // 如果编译报未找到错误，再添加以下依赖库
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.6.1")
}
```

### 1.2.3 配置Action注册表

在`app/src/main`目录下创建`assets`目录（如不存在），然后创建`actionRegistry.json`文件：

```JSON
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

**配置说明**
- **appId**: Agent应用的唯一标识符，需在[接待后台](https://jiedai.ainirobot.com/web/portal/#/frame/hmag-agentos/hmag-agentos.agentapp/)申请
- **platform**: 运行平台标识，支持`opk`或`apk`
- **actionList**: 对外暴露的Action列表，在注册表中声明的Action需要在AppAgent的`onExecuteAction`方法中处理

> **应用场景**: 在本示例中，我们将开发一个具有情感感知能力的智能助手"豹姐姐"，她能够识别用户的情绪变化并做出相应的回应。

### 1.2.4 创建AppAgent

> **重要约束**: 一个应用中只能存在一个AppAgent实例。

在`MainApplication`的`onCreate`方法中添加以下代码：

```Kotlin
package com.ainirobot.agent.sample

import android.app.Application
import android.os.Bundle
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        object : AppAgent(this) {

            override fun onCreate() {
                // 设定角色人设
                setPersona("你叫豹姐姐，是一位聪明、亲切又略带俏皮的虚拟助手。")
                // 设定角色目标
                setObjective("通过自然的对话和合适的情绪表达，让用户感受到理解、陪伴与情感共鸣，从而提升交流的舒适感和信任感。")
            }

            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // 在此处处理静态注册的action，如果你不需要处理，请返回false，如果要自行处理且不需要后续处理，则返回true
                // 默认返回false
                return false
            }
         }
    }
}
```

### 1.2.5 创建PageAgent

> **重要约束**: 每个页面只能存在一个PageAgent实例。

**设计理念**: 实现情感感知型智能助手

在日常交流中，情感识别往往比语言内容更为重要。本节将为助手定义情感感知能力：

**情感响应Action列表**
- 😊 **积极情绪响应** - 感知到用户高兴、满意等正面情绪时的回应
- 😢 **消极情绪安慰** - 感知到用户难过、失落等负面情绪时的安慰
- 😠 **愤怒情绪疏导** - 感知到用户愤怒、不满等激动情绪时的疏导

在`MainActivity.kt`中添加以下代码：

```Kotlin
package com.ainirobot.agent.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor
import com.ainirobot.agent.base.Parameter
import com.ainirobot.agent.base.ParameterType
import com.ainirobot.agent.coroutine.AOCoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 添加页面级Agent
        PageAgent(this)
            .registerAction(
                Action(
                    name = "com.agent.demo.SHOW_SMILE_FACE",
                    displayName = "笑",
                    desc = "响应用户的开心、满意或正面情绪",
                    parameters = listOf(
                        Parameter(
                            "sentence",
                            ParameterType.STRING,
                            "回复给用户的话",
                            true
                        )
                    ),
                    executor = object : ActionExecutor {

                        override fun onExecute(action: Action, params: Bundle?): Boolean {
                            AOCoroutineScope.launch {
                                // 展示笑脸
                                showFaceImage(R.drawable.ic_smile)
                                // 播放给用户说的话
                                params?.getString("sentence")?.let { AgentCore.ttsSync(it) }
                                // 播放完成后，及时上报Action的执行状态
                                action.notify(isTriggerFollowUp = false)
                            }
                            return true
                        }
                    }
                )
            )
    }
}
```

> **关键提醒**: 任何Action执行完成后都必须调用`action.notify()`方法通知系统执行状态。详细说明请参考[2.3.2 执行结果通知](#232-执行结果通知)。

### 1.2.6 完整示例

上述快速开始部分提供了基础接入方式。更完整的示例项目包含了多种情感场景的表情展示功能，您可以直接下载运行体验。

**示例项目地址**: [AgentSDKSample](https://github.com/orionagent/AgentSDKSample)

### 1.2.7 开发总结

**核心开发流程**
1. **SDK集成**: 配置Maven仓库、添加依赖、创建Action注册表
2. **角色设定**: 定义App的人设和目标
3. **页面开发**: 创建业务页面，设置页面级目标，上传页面信息
4. **Action系统**: 定义并注册Action到AgentOS SDK
   - 定义Action的名称、描述和参数
   - 实现Action的处理逻辑
   - 通过语音触发测试Action执行

**🔥 重要说明**: Action注册完成后会自动生效，用户可直接通过语音与AI交互。详细的生效机制请参考[2.2 Action注册机制](#22-action注册机制)。



# 2. Action详解

## 2.1 Action概念

AgentOS的核心机制是通过识别用户意图来执行相应的技能模块，这些技能模块被称为Action。例如，当用户询问"我明天去深圳需要带伞吗？"时，系统会识别出查询天气的意图，并调用相应的天气查询Action（如：`orion.agent.action.WEATHER`）。

### Action规划机制

大模型通过文本描述理解和选择Action，优先级如下：

**1. Action描述（`desc`）**
- 大模型主要依据此属性判断是否符合用户意图
- 必须清晰、准确、具体地描述Action的功能和使用场景
- 避免模糊表述，确保大模型能准确理解

```kotlin
// ✅ 符合规范的描述
desc = "查询指定城市和日期的天气情况，包括温度、湿度、风力等信息"

// ❌ 违反规范的描述
desc = "天气"  // 过于简单，缺乏具体功能说明
desc = "处理用户请求"  // 过于泛化，无法区分具体功能
```

**2. Action名称（`name`）**
- 作为辅助判断依据，补充描述信息
- 应具有明确的业务含义，避免泛化命名
- 多个Action的名称要有明显区分度

```kotlin
// ✅ 符合规范的名称
name = "com.example.weather.QUERY_WEATHER"
name = "com.example.music.PLAY_SONG"

// ❌ 违反规范的名称
name = "ACTION1"  // 无业务含义
name = "HANDLE"   // 过于泛化
name = "com.example.DO_SOMETHING"  // 不明确具体功能
```

**3. 参数描述（`Parameter.desc`）**
- 影响大模型对参数的理解和提取准确性
- 每个参数的描述都要精确反映其定义和用途
- 帮助大模型从用户输入中正确提取对应信息

```kotlin
// ✅ 符合规范的参数描述
Parameter("city", ParameterType.STRING, "要查询天气的城市名称，如北京、上海", false)
Parameter("song_name", ParameterType.STRING, "要播放的歌曲名称", true)

// ❌ 违反规范的参数描述
Parameter("city", ParameterType.STRING, "城市", false)  // 过于简单
Parameter("data", ParameterType.STRING, "数据", false)  // 含义不明确
Parameter("param1", ParameterType.STRING, "参数1", false)  // 无实际含义
```

> **核心原则**：所有通过文本描述的属性都直接影响大模型的理解和规划准确性，务必重视每一个描述的质量。

### 命名规范约束

**Action名称规范**
- **格式要求**：`com.company.module.ACTION_NAME`（公司域名+模块+Action简名）
- **简名大写**：Action简名部分必须使用大写字母
- **业务含义**：名称应具有明确的业务含义，避免泛化命名
- **唯一性**：确保在应用内唯一，避免与其他app的Action冲突

```kotlin
// ✅ 符合规范
"com.example.weather.QUERY_WEATHER"
"com.example.music.PLAY_SONG"

// ❌ 违反规范
"com.example.weather.query_weather"  // 简名未大写
"ACTION1"  // 无域名前缀，无业务含义
```

**Parameter名称规范**
- **语言要求**：使用英文命名
- **连接方式**：多个单词用下划线连接
- **避免冲突**：不得与Action或Parameter对象的属性名相同或相似
- **语义清晰**：名称应能清楚表达参数用途

```kotlin
// ✅ 符合规范
"city_name", "start_date", "user_id"

// ❌ 违反规范
"cityName"  // 使用驼峰命名
"name"      // 与Parameter对象属性名冲突
"data1"     // 无明确语义
```

### 2.1.1 基础属性

Action类的核心属性定义如下：

```Kotlin
package com.ainirobot.agent.action

open class Action(
    /**
      * action全名，避免与其它app中的action冲突
      */
      name: String,
    /**
      * 当前应用的appId
      */
      appId: String,
    /**
      * action显示名称，可能被用于显示到UI界面上，可以是中文等
      */
      displayName: String,
    /**
      * action描述，用以让大模型理解应该在什么时间调用此action
      */
      desc: String,
    /**
      * 期望action在被规划出时携带的参数描述
      */
      parameters: List<Parameter>?,
    /**
      * action对应的执行器，当action规划完成后会回调此接口
      */
      @Transient
    var executor: ActionExecutor?
): ActionEntity(...), Parcelable {

    /**
     * 规划的action的Id，用于标识action的唯一性，同一个action每次规划都会返回不同的actionId
     */
    var sid: String = ""

    /**
     * 触发规划的用户问题
     */
    var userQuery: String = ""
}
```

**最佳实践**: 创建Action时需要清晰、准确地描述各项属性，以便大模型能够准确理解Action的功能并做出精确的选择。

**关键要求**:
- **描述具体化**: `desc`属性必须详细描述Action的具体功能和使用场景
- **避免歧义**: 多个Action的描述不能过于相似，确保大模型能够准确区分

### 2.1.2 参数定义

Action参数用于从用户的查询中提取关键信息。例如，对于"我想查一下北京今天的天气"这个查询，可以提取`city`和`date`两个参数。

参数对象的属性定义：

```kotlin
data class Parameter(
    /**
      * 参数名
      */
    val name: String,
    /**
      * 参数类型
      */
    val type: ParameterType,
    /**
      * 参数描述
      */
    val desc: String,
    /**
      * 是否是必要参数
      */
    val required: Boolean,
    /**
      * 当type为enum时，需要传此参数，作为枚举值选择的列表
      */
    var enumValues: List<String>? = null
)
```

**天气查询示例**

```kotlin
val weatherAction = Action(
    "orion.agent.action.WEATHER",
    "查询天气",
    "查询指定城市和日期的天气情况",
    parameters = listOf(
        Parameter(
            "city",
            ParameterType.STRING,
            "要查询天气的城市名称",
            false
        ),
        Parameter(
            "date",
            ParameterType.STRING,
            "要查询的日期，如今天、明天、后天",
            false
        )
    ),
    executor = object : ActionExecutor {
        override fun onExecute(action: Action, params: Bundle?): Boolean {
            // 获取参数
            val city = params?.getString("city") ?: "当前位置"
            val date = params?.getString("date") ?: "今天"

            // 处理天气查询逻辑
            // ...

            return true
        }
    }
)
```

**参数解析示例**

注册Action成功后，当用户对机器人说："明天北京天气怎么样？"时，大模型会自动规划到天气Action并回调到`onExecute`方法，传递解析后的参数：

```
解析后的参数值:
city: '北京'
date: '明天'
```

## 2.2 Action注册机制

> **⚠️ 重要提醒**：
>
> **Action注册影响**：如果一个Action都没有注册，大模型将无法规划任何Action来响应用户请求。
>
> **典型例子**：不注册`Actions.SAY`这个系统Action，机器人就无法通过TTS直接回答用户问题或反问澄清问题。
>
> **开发选择**：开发者可以根据业务需要选择注册相应的Action，或在Action无法满足的复杂场景下通过`AgentCore.llm()`直接调用大模型。

Action按生命周期分为App级和Page级两种类型，它们的活跃周期不同。

### Action生效机制

**自动生命周期管理**: SDK会自动监听应用和页面的生命周期变化，在合适的时机启用或停用Action：

- **AppAgent生效时机**:
  - 应用进入前台时，Action自动开始生效
  - 应用退出前台时，Action自动停止生效

- **PageAgent生效时机**:
  - 页面对用户可见时，Action自动开始生效
  - 页面不可见时，Action自动停止生效

**语音交互流程**: Action生效后，用户可直接通过语音与AI交互，无需额外操作：
1. 用户语音输入 → 2. 大模型理解意图 → 3. 自动规划Action → 4. 执行对应逻辑

**Action规划机制**:
- 只要成功注册了Action，当用户通过语音输入（麦克风打开状态下）或调用`AgentCore.query()`时，大模型会自动规划到匹配度最高的Action上
- 大模型根据Action的描述、名称和参数描述来判断匹配度，选择最符合用户意图的Action执行

**重要约束**:
- **规划范围限制**: 大模型只能规划到当前正在生效的Action，已失效的Action无法被规划

### 2.2.1 App级Action

App级Action是全局Action，在整个应用运行期间（处于前台时）都会响应用户请求。当应用退出或进入后台时，这些Action将不再响应。App级Action支持**动态注册**和**静态注册**两种方式。

#### 动态注册

动态注册的App级Action用于在应用整个生命周期内响应用户的实时意图。需要在AppAgent的`onCreate`方法中调用注册。

**示例：注册退出Action**

```Kotlin
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Actions

// 添加应用级Agent
object : AppAgent(this) {

    /**
      * AppAgent初始化的回调
      * 需要动态注册的App级Action，可以此方法中注册
      */
      override fun onCreate() {
        // 动态注册Action
        // 示例：此处注册了系统Action：EXIT，当用户说"退出"时，会触发BACK事件
        registerAction(Actions.EXIT)
    }
}
```

**外部Action注册**

动态注册支持注册当前应用的私有Action，也支持注册外部Action（如系统Action或其他AgentOS应用中静态注册的Action）。

**示例：注册天气应用的Action**

```Kotlin
import android.os.Bundle
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor

// 注册一个查天气的Action，前提是你已经安装了包含此Action的AgentOS应用（必须是在注册表中静态注册才可以）
registerAction(
    Action("com.agent.tool.WEATHER_HOME").also  {
         it.executor = object : ActionExecutor {

            override fun onExecute(action: Action, params: Bundle?): Boolean {
                // 如果你不需要处理，请返回false，如果要自行处理且不需要后续处理，则返回true
                // 此处只打印了一个日志，所以返回false，结果就是会调起天气App查询天气
                // 如果你想自己查天气，那么就需要以此处调用自己的查天气接口，并返回true即可
                println("用户刚查了天气")
                return false
            }
        }
    }
)
```

#### 静态注册

静态注册专门用于向外部提供调用当前应用的入口。静态注册的Action默认不会在当前应用运行期间生效，如需在当前应用中也生效，需要再次进行动态注册。

**配置要求**
- 只有App级Action才可以静态注册
- 必须在`actionRegistry.json`注册表中配置
- 需要添加详细的参数描述

**示例：天气应用的静态注册配置**

```JSON
{
  "appId": "app_43e38d01cfad05d3bb2d8ce3a66f7aa2",
  "platform": "apk",
  "actionList": [
    {
      "name": "com.agent.tool.WEATHER_HOME",
      "displayName": "打开查询天气的首页",
      "desc": "当用户想要询问天气时，显示天气情况",
      "parameters": [
        {
          "name": "city",
          "type": "string",
          "desc": "要查询哪个城市的天气",
          "required": false
        },
        {
          "name": "date",
          "type": "string",
          "desc": "要查询什么日期的天气",
          "required": false
        }
      ]
    }
  ]
}
```

> **参数说明**: `required`为`false`表示参数可选，当参数为空时需要执行端自行处理（如使用当前定位城市、默认今天日期等）。

**静态注册Action的执行处理**

静态注册的Action需要在AppAgent的`onExecuteAction`方法中处理：

```Kotlin
import android.os.Bundle
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action

object : AppAgent(this) {
    /**
      * actionRegistry.json注册表中静态注册的action需要执行的回调
      * 注：只有可以被外部调用的action才可以使用静态注册，且此方法只能是被外部（其它app）调用时才会执行
      */
      override fun onExecuteAction(
        action: Action,
        params: Bundle?
    ): Boolean {
        return when (action.name) {
            "com.agent.tool.WEATHER_HOME" -> {
                // 打开天气首页
                startWeatherHomePage(action, params)
                true
            }
            else -> false
        }
    }
}
```

> **注册方式总结**:
> - **静态注册**: 用于向外部提供调用接口
> - **动态注册**: 用于当前应用内部调用

### 2.2.2 Page级Action

Page级Action需要在页面（Activity或Fragment）初始化时声明，仅在当前页面对用户可见时生效。当页面退出或被其他页面覆盖时，这些Action将不再生效。

#### 动态注册

由于Page级Action只在当前页面生效，因此**只能动态注册，不能在注册表中静态注册**，即不能向外部提供接口。

**示例：情感响应Action注册**

```kotlin
PageAgent(this)
    .blockAction("com.xxx.yyy.TTT") // 排除指定Action
    .blockActions( // 排除指定Action列表
        listOf(
            "com.xxx.yyy.TTT",
            "com.xxx.yyy.RRR",
        )
    )
    .blockAllActions() // 排除所有Action
    .registerAction(
        Action(
            name = "com.agent.demo.SHOW_SMILE_FACE",
            displayName = "笑",
            desc = "响应用户的开心、满意或正面情绪",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "回复给用户的话",
                    true
                )
            ),
            executor = object : ActionExecutor {

                override fun onExecute(action: Action, params: Bundle?): Boolean {
                    showFaceImage(R.drawable.ic_smile)
                    handleAction(action, params)
                    return true
                }
            }
        )
    )
    .registerAction(
        Action(
            name = "com.agent.demo.SHOW_CRY_FACE",
            displayName = "哭",
            desc = "响应用户的难过、失落或求助情绪",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "回复给用户的话，给予安慰",
                    true
                )
            ),
            executor = object : ActionExecutor {

                override fun onExecute(action: Action, params: Bundle?): Boolean {
                    showFaceImage(R.drawable.ic_cry)
                    handleAction(action, params)
                    return true
                }
            }
        )
    )
    .registerAction(
        Action(
            name = "com.agent.demo.SHOW_ANGRY_FACE",
            displayName = "生气",
            desc = "响应用户的愤怒、不满或投诉情绪",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "回复给用户的话，尽可能消除用户的负面情绪",
                    true
                )
            ),
            executor = object : ActionExecutor {

                override fun onExecute(action: Action, params: Bundle?): Boolean {
                    showFaceImage(R.drawable.ic_angry)
                    handleAction(action, params)
                    return true
                }
            }
        )
    )
```

#### Action过滤机制

当您在App级注册了多个全局Action，但在特定页面不希望某些全局Action被触发时，可以使用以下过滤方式：

**过滤单个Action**

```Kotlin
// 过滤掉指定的全局Action，参数为Action的name
pageAgent.blockAction("com.xxx.yyy.TTT")
```

**过滤多个Action**

```Kotlin
// 过滤的Action列表
pageAgent.blockActions(
    listOf(
        "com.xxx.yyy.TTT",
        "com.xxx.yyy.RRR",
    )
)
```

**过滤所有Action**

```Kotlin
// 过滤掉所有在AppAgent中注册的全局Action
// 仅当前页面注册的Action生效
pageAgent.blockAllActions()
```

## 2.3 Action执行流程

### 2.3.1 执行回调

> **⚠️ 重要说明**：
> 1. **RobotOS迁移**：如果之前使用RobotOS系统，原有的领域和技能需要迁移到AgentOS的Action回调中重新实现
> 2. **功能实现**：AgentOS不会自动调用小豹应用或系统组件，所有功能（如天气查询、巡逻等）都需要开发者在Action中自行实现

**动态注册Action的执行**

动态注册的Action需要设置执行器，在执行器中实现具体的业务逻辑：

```Kotlin
import android.os.Bundle
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor
import com.ainirobot.agent.base.Parameter
import com.ainirobot.agent.base.ParameterType

Action(
    name = "com.agent.demo.SHOW_ANGRY_FACE",
    displayName = "生气",
    desc = "响应用户的愤怒、不满或投诉情绪",
    parameters = listOf(
        Parameter(
            "sentence",
            ParameterType.STRING,
            "回复给用户的话，尽可能消除用户的负面情绪",
            true
        )
    ),
    executor = object : ActionExecutor {

        override fun onExecute(action: Action, params: Bundle?): Boolean {
            showFaceImage(R.drawable.ic_angry)
            handleAction(action, params)
            return true
        }
    }
)
```

**静态注册Action的执行**

静态注册的Action需要在AppAgent的`onExecuteAction`方法中实现：

```Kotlin
override fun onExecuteAction(
    action: Action,
    params: Bundle?
): Boolean {
    return false
}
```

> **返回值说明**:
> - `true`: 表示已自定义处理此Action，不需要后续处理
> - `false`: 表示不处理此Action，交由系统继续处理

### 2.3.2 执行结果通知

> **🚨 重要警告**
> **以下规则是Action系统正常运行的关键，违反将导致系统异常，开发者必须严格遵守：**

**执行约束**
1. **Action执行回调方法中不能执行耗时操作**
2. **处理Action时，除了返回`true`外，还必须在Action执行完成后调用`action.notify()`方法**
3. **执行回调方法默认运行在子线程中**

**耗时操作的正确处理方式**
- `onExecute`方法应该立即返回`true`
- 将耗时操作（如网络请求、文件操作、TTS播放等）放到协程或线程中执行
- 耗时操作完成后，调用`action.notify()`方法通知系统执行结果

**notify方法说明**

```Kotlin
package com.ainirobot.agent.action

/**
  * Action执行完成后需要同步执行结果
  *
  *  @param  result Action的执行结果
  *  @param  isTriggerFollowUp 在Action执行完成后主动引导用户进行下一步操作，默认关闭
  */
fun notify(
    result: ActionResult = ActionResult(ActionStatus.SUCCEEDED),
    isTriggerFollowUp: Boolean = false
)
```

**❌ 错误示例**

**Kotlin版本**

```Kotlin
class MyActionExecutor : ActionExecutor {

    override fun onExecute(action: Action, params: Bundle?): Boolean {
        // ❌ 错误：直接在onExecute中执行耗时操作
        Thread.sleep(3000) // 3秒耗时操作

        // 通知执行完成
        action.notify()
        return true

        // 这个方法总共耗时3秒，会在2秒时被强制中断！
    }
}
```

**Java版本**

```Java
@Override
public boolean onExecute(Action action, Bundle params) {
    // ❌ 错误：直接在onExecute中执行耗时操作
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // 通知执行完成
    action.notify();
    return true;
}
```

**✅ 正确示例**

**Kotlin版本**

```Kotlin
class MyActionExecutor : ActionExecutor {

    override fun onExecute(action: Action, params: Bundle?): Boolean {
        // ✅ 正确：立即启动协程执行耗时操作
        AOCoroutineScope.launch {
            try {
                // 在协程中执行耗时操作
                delay(3000) // 3秒耗时操作

                // 完成后通知执行结果
                action.notify()

            } catch (e: Exception) {
                action.notify(ActionResult(ActionStatus.FAILED))
            }
        }

        // 立即返回true，不等待耗时操作完成
        return true
    }
}
```

**Java版本**

```Java
@Override
public boolean onExecute(Action action, Bundle params) {
    // 立即启动后台任务
    new Thread(() -> {
        try {
            Thread.sleep(5000); // 耗时操作在后台执行
            action.notify(); // 完成后通知
        } catch (Exception e) {
            action.notify(new ActionResult(ActionStatus.FAILED));
        }
    }).start();

    return true; // 立即返回，不阻塞
}
```

## 2.4 系统内置Action

系统内置Action是AgentOS预定义的功能模块，包含系统功能、指令和应用等。系统Action的命名空间为`orion.agent.action`。

> **注意**: 并非所有系统Action都由系统实现了执行逻辑，部分Action（如`orion.agent.action.CLICK`点击事件）仍需要用户自行处理。

**系统自动处理的Action**

```Kotlin
package com.ainirobot.agent.action

object Actions {
    /**
      * 调整系统音量
      */
    const val SET_VOLUME = "orion.agent.action.SET_VOLUME"
    /**
      * 机器人兜底对话
      */
    const val SAY = "orion.agent.action.SAY"
    /**
      * 取消
      */
    const val CANCEL = "orion.agent.action.CANCEL"
    /**
      * 返回
      */
    const val BACK = "orion.agent.action.BACK"
    /**
      * 退出
      */
    const val EXIT = "orion.agent.action.EXIT"
    /**
      * 知识库问答
      */
    const val KNOWLEDGE_QA = "orion.agent.action.KNOWLEDGE_QA"
    /**
      * 对用户说一句欢迎或者欢送语
      */
    const val GENERATE_MESSAGE = "orion.agent.action.GENERATE_MESSAGE"
    /**
      * 调整机器人速度
      */
    const val ADJUST_SPEED = "orion.agent.action.ADJUST_SPEED"
}
```

> **默认行为**: `CANCEL`、`BACK`和`EXIT`默认处理为模拟点击Back键。

### 知识库问答使用示例

以`KNOWLEDGE_QA`为例，说明如何注册系统内置Action以及知识库问答的触发机制：

```Kotlin
registerAction(Actions.KNOWLEDGE_QA)
```

开发者注册此Action后，当用户通过语音询问机器人，如果命中接待后台配置的知识库时会自动执行，系统会自动处理知识库查询并播放配置好的TTS回答。

**需要用户处理的Action**

```Kotlin
package com.ainirobot.agent.action

object Actions {
    /**
      * 确定
      */
    const val CONFIRM = "orion.agent.action.CONFIRM"
    /**
      * 点击
      */
    const val CLICK = "orion.agent.action.CLICK"
}
```

> **相关功能**: 如需监听系统Action的执行状态，请参考[系统Action状态监听](#414-系统action状态监听)。

# 3. Agent角色配置 - 让您的AI更有个性

## 3.1 概述

AgentOS SDK提供了强大的角色配置功能，让您可以为AI助手定制独特的身份、风格和目标。通过三个核心配置方法，您可以打造出具有鲜明个性的智能助手。

## 3.2 三大核心配置

### 人设配置 (setPersona)
**作用**：定义AI助手的身份特征和基本属性
**侧重点**：静态身份信息，回答"我是谁"
**应用场景**：角色身份、性格特点、背景设定

### 风格配置 (setStyle)
**作用**：设置AI助手的对话风格和表达方式
**侧重点**：交流方式，回答"如何说话"
**应用场景**：语言风格、表达方式、情感色彩

### 目标配置 (setObjective)
**作用**：明确AI助手的任务目标和行为准则
**侧重点**：业务流程和特殊要求，回答"要做什么"
**应用场景**：业务目标、行为规范、特殊要求

## 3.3 配置层级与优先级

### 配置层级
```
PageAgent配置 > AppAgent配置 > 系统默认
```

**说明**：PageAgent未设置时，自动继承AppAgent的配置

## 3.4 API接口详解

### setPersona()
```kotlin
setPersona(persona: String): Agent
```

**参数说明：**
- `persona: String` - 人设描述，如："你叫小豹，是一个聊天机器人"

### setStyle()
```kotlin
setStyle(style: String): Agent
```

**参数说明：**
- `style: String` - 对话风格，如："professional, friendly, humorous"

### setObjective()
```kotlin
setObjective(objective: String): Agent
```

**参数说明：**
- `objective: String` - 规划目标，要清晰明确，以便于大模型理解

> **说明**：三个方法均支持链式调用，在AppAgent和PageAgent中都可使用。具体使用示例请参考前面的快速开始章节。

## 3.5 扩展配置能力

当基础的三项配置无法满足复杂需求时，可以结合使用**感知信息上报**功能（详见[4.9节](#49-感知信息上报)）进行更灵活的信息传递：

```kotlin
// 上传复杂的页面信息和业务状态
val pageInfo = """
当前商品：${product.name}
用户状态：${user.memberLevel}会员
特殊提示：该商品正在促销中
"""

AgentCore.uploadInterfaceInfo(pageInfo)
```

> **提示**：`uploadInterfaceInfo`方法的详细用法请参考[4.10 感知信息上报](#410-感知信息上报)章节。

## 3.6 配置最佳实践

### 核心原则
- **人设要具体**：使用具体的角色定义，避免抽象描述
- **风格要一致**：保持整个应用的风格统一性
- **目标要明确**：确保AI理解具体的业务目标

### 示例对比

**❌ 不推荐**
```kotlin
setPersona("你是一个助手")
setObjective("帮助用户")
```

**✅ 推荐**
```kotlin
setPersona("你是'小豹购物'的专属购物顾问，拥有3年电商经验")
setObjective("根据用户喜好和预算，推荐最合适的商品并提供购买建议")
```

## 3.7 典型应用场景

### 电商应用
```kotlin
// 全局配置
setPersona("资深购物顾问")
setObjective("帮助用户做出明智的购买决策")

// 页面级配置
// 商品列表页：帮助用户快速筛选商品
// 详情页：详细介绍商品特性
// 购物车：协助完成订单确认
```

### 教育应用
```kotlin
// 全局配置
setPersona("经验丰富的教育助手")
setStyle("耐心、鼓励、循序渐进")
setObjective("帮助用户掌握知识，激发学习兴趣")

// 页面级配置
// 课程页：引导完成当前课程学习
// 练习页：提供习题解答和学习建议
```

---

# 4. 核心API接口

## 4.1 麦克风控制

### 功能介绍
麦克风控制功能提供了对音频输入的精确管理。默认情况下，集成AgentOS SDK的应用会在进入前台时自动开启麦克风，在应用退出或进入后台时自动关闭麦克风。开发者也可以根据业务需求手动控制麦克风状态。

### 应用场景
- 需要临时静音的场景
- 播放音视频内容时防止音频干扰
- 特定业务流程中的音频控制需求

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

// 设置麦克风静音状态
AgentCore.isMicrophoneMuted = true // 静音
AgentCore.isMicrophoneMuted = false // 取消静音
```

> **🔍 故障排查**：如遇语音交互无响应问题，请参考 [FAQ - 语音交互无响应](../../FAQ.md#语音交互无响应) 进行自检排查

## 4.2 免唤醒功能

### 功能介绍
免唤醒是AgentOS的智能收音控制功能，通过**多模态智能识别**实现精准的语音交互：

**开启状态**（默认）：
- 采用**视觉与声学融合算法**，综合分析用户交互意图
- 仅当用户**正对机器人**时激活收音，侧向或背对机器人说话将不会被拾音
- 显著降低多客户场景下的**交叉干扰**和误触发

**关闭状态**：
- 切换为**无条件收音**模式，持续监听环境音频
- ⚠️ **多客户场景风险**：不同用户同时对话时易产生语音冲突和误识别
- 适用于单一用户或需要更宽松收音范围的特定场景

### 应用场景
- **推荐开启**：展厅、大厅等多人环境，确保服务准确性
- **谨慎关闭**：私密空间或需要远距离收音的特殊应用场景

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 是否开启免唤醒功能，默认true，开启
 */
var isEnableWakeFree: Boolean
```

## 4.3 ASR与TTS监听

### 功能介绍
通过设置监听器，开发者可以获取到语音识别（ASR）和语音合成（TTS）的实时内容和状态，实现对语音交互过程的全面监控。

### 应用场景
- 需要获取语音识别结果进行业务处理
- 监控TTS播放内容和状态
- 实现自定义的语音交互UI

### API接口

**设置监听器**

```Kotlin
import com.ainirobot.agent.OnTranscribeListener

/**
 * 设置ASR和TTS监听器
 */
fun setOnTranscribeListener(listener: OnTranscribeListener): Agent
```

**监听器接口定义**

```Kotlin
import com.ainirobot.agent.base.Transcription

/**
 * 监听ASR和TTS输出
 */
interface OnTranscribeListener {

    fun onASRResult(transcription: Transcription): Boolean

    fun onTTSResult(transcription: Transcription): Boolean

}
```

> **注意**: `setOnTranscribeListener`是AppAgent和PageAgent的成员方法。

### 使用说明

**获取内容**
- `transcription.text`: 获取文本内容

**判断结果类型**
- `transcription.final`: `true`表示最终结果，`false`表示中间结果

**返回值处理**
- 返回`true`: 表示消费了此次结果，系统将不再显示字幕到底部字幕条
- 返回`false`: 表示仅监听不拦截，不影响系统后续处理（推荐）

> **线程提醒**: `onTranscribe`回调在子线程中执行。

## 4.4 Agent状态监听

### 功能介绍
Agent状态监听提供了对Agent思考和处理过程的实时监控，帮助开发者了解Agent的工作状态并实现相应的UI反馈。

### API接口

```Kotlin
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.OnAgentStatusChangedListener

PageAgent(this)
    .setOnAgentStatusChangedListener(object : OnAgentStatusChangedListener {

        override fun onStatusChanged(status: String, message: String?): Boolean {
            // 在此可以根据不同的状态显示不同的UI，注：当前是子线程
            // 如果不想把状态显示到默认语音条上，则返回true，如果想保留系统显示状态UI，则返回false
            return true
        }
    })
```

**监听器接口定义**

```Kotlin
/**
 * Agent状态变化监听
 */
interface OnAgentStatusChangedListener {

    fun onStatusChanged(status: String, message: String?): Boolean

}
```

### 状态说明

**状态类型**
- `listening`: 正在听取用户输入
- `thinking`: 思考中，正在分析用户意图
- `processing`: 处理中，正在执行相关操作
- `reset_status`: 状态复位，回到初始状态

**消息说明**
- 当`status`为`processing`时，`message`可能包含具体的处理信息，如："正在选择工具..."、"正在获取天气..."、"正在总结答案..."等
- 其他状态时`message`为空

## 4.5 语音条控制

### 功能介绍
控制系统默认语音条的显示和隐藏，满足不同应用场景的UI需求。

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 是否开启语音条，默认开启
 */
var isEnableVoiceBar: Boolean
```

## 4.6 TTS语音合成

### 功能介绍
主动调用系统的语音合成接口，将指定文本转换为音频并自动播放，支持同步和异步两种调用方式。

### 应用场景
- 应用启动时播放欢迎语
- 业务流程中的语音提示
- 主动与用户进行语音交互
- 实时对话和连续播报
- 大多数语音播放需求

### API接口

**同步调用接口**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * TTS接口，同步调用
 * 注：此接口需在协程中调用
 *
 * @param text 要播放的文本
 * @param timeoutMillis 超时时间，单位毫秒
 *
 * @return TaskResult<String> 任务执行结果，status=1表示成功，status=2表示失败
 */
suspend fun ttsSync(text: String, timeoutMillis: Long = 180000): TaskResult<String>
```

**异步调用接口**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * TTS接口，异步调用，返回状态通过TTSCallback回调
 *
 * @param text 要播放的文本
 * @param timeoutMillis 超时时间，单位毫秒
 * @param callback 回调，status=1表示播放成功，status=2表示播放失败
 */
fun tts(
    text: String,
    timeoutMillis: Long = 180000,
    callback: TTSCallback? = null
)
```

### ⚠️ 重要注意事项

> **播放行为特性**
> - **自动追加播放**：多次调用`tts()`或`ttsSync()`会将音频内容追加到播放队列，不会中断前一次播放
> - **队列化播放**：适合连续播报多段内容的场景

> **播放中断与回调机制**
> - TTS播放被任何方式终止或打断时，都会触发播放失败回调（`status=2`）
> - 用户在TTS播放过程中对机器人说话会触发语音识别，**自动打断当前TTS播放**

> **超时时间说明**
> - TTS采用**流式播放**机制，音频边生成边播放，非预生成文件模式
> - `timeoutMillis` 参数为**完整播放超时时间**，即从调用开始到播放完成的总时长
> - ⚠️ **超时时间设置过短可能导致长文本播放被中断**，建议根据文本长度合理设置


**停止播放**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 强制打断TTS播放
 */
fun stopTTS()
```

## 4.7 TTS音频文件播放

### 功能介绍
TTS音频文件播放功能提供了基于预生成音频文件的语音合成方案。与流式TTS不同，该功能会先从网络下载完整的音频文件，然后进行播放。其特点是下载完成后播放过程不再受网络波动影响。

### 技术特点
- **网络稳定性**：音频下载完成后，播放过程不受网络波动影响
- **完整生命周期**：提供从网络请求到播放完成的详细回调链
- **替换播放**：每次调用会中断上一次播放

### 应用场景
- 网络环境不稳定的特殊场景
- 需要详细播放状态监控的应用

### API接口

**异步播放接口**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * TTS音频文件播放接口，异步调用
 * 从网络下载音频文件并播放，支持完整的播放生命周期回调
 *
 * @param text 要播放的文本
 * @param timeoutMillis 超时时间，单位毫秒
 * @param callback 完整的播放生命周期回调
 * @param showCaption 是否在字幕条显示文本，默认为true
 */
fun playTtsAudioFile(
    text: String,
    timeoutMillis: Long = 15000,
    callback: TTSAudioFileCallback? = null,
    showCaption: Boolean = true
)
```

**停止播放接口**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 强制打断TTS音频文件播放
 */
fun stopTtsAudioFile()
```

### 回调接口定义

> **💡 接口特性**：`TTSAudioFileCallback` 接口中的所有方法都有默认的空实现，开发者可以选择性地重写需要的方法，无需实现全部方法。

```Kotlin
import com.ainirobot.agent.base.ITaskCallback

/**
 * TTS音频文件播放回调接口
 * 提供完整的播放生命周期回调
 *
 * 注意：所有回调方法都有默认空实现，开发者只需重写关心的方法即可
 */
interface TTSAudioFileCallback : ITaskCallback<String> {

    /**
     * 网络请求开始
     * @param text 要播放的文本
     */
    fun onRequestStart(text: String) {}

    /**
     * 网络请求成功，音频数据下载完成
     * @param text 播放的文本
     * @param audioSize 音频数据大小（字节）
     */
    fun onRequestSuccess(text: String, audioSize: Int) {}

    /**
     * 网络请求失败
     * @param text 播放的文本
     * @param error 错误信息
     */
    fun onRequestFailed(text: String, error: String) {}

    /**
     * 音频准备完成，即将开始播放
     * @param text 播放的文本
     */
    fun onPlaybackPrepared(text: String) {}

    /**
     * 音频播放开始
     * @param text 播放的文本
     */
    fun onPlaybackStarted(text: String) {}

    /**
     * 音频播放完成
     * @param text 播放的文本
     */
    fun onPlaybackCompleted(text: String) {}

    /**
     * 音频播放被外部打断停止
     * @param text 播放的文本
     */
    fun onPlaybackInterrupted(text: String) {}

    /**
     * 音频播放出错
     * @param text 播放的文本
     * @param what 错误类型
     * @param extra 额外错误信息
     */
    fun onPlaybackError(text: String, what: Int, extra: Int) {}

    /**
     * ITaskCallback 的实现，仅用于满足接口要求
     *
     * 注意：在当前实现中，此方法永远不会被调用，因为：
     * - TTSAudioFileCallback 只用于 playTtsAudioFile 命令
     * - playTtsAudioFile 命令使用详细的生命周期回调
     * - 只有流式TTS等命令才会调用 onTaskEnd
     *
     * 此方法存在的唯一原因是 ITaskCallback<String> 是抽象接口，必须实现
     */
    override fun onTaskEnd(status: Int, result: String?) {
        // 空实现 - 此方法在当前架构中不会被调用
    }
}
```

### 使用示例

```Kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.base.TTSAudioFileCallback

// 基础使用
AgentCore.playTtsAudioFile("欢迎使用AgentOS SDK")

// 播放行为演示 - 后一次调用会中断前一次
AgentCore.playTtsAudioFile("第一段音频，播放时间较长...")
Thread.sleep(1000) // 等待1秒
AgentCore.playTtsAudioFile("第二段音频") // 会立即中断第一段音频的播放

// 简单回调使用 - 只关心播放完成
AgentCore.playTtsAudioFile(
    text = "播放完成后会有提示",
    callback = object : TTSAudioFileCallback {
        override fun onPlaybackCompleted(text: String) {
            println("播放完成: $text")
            // 执行后续逻辑
        }

        override fun onPlaybackError(text: String, what: Int, extra: Int) {
            println("播放出错: $text")
        }
    }
)

// 完整回调使用
AgentCore.playTtsAudioFile(
    text = "正在为您播放重要通知",
    timeoutMillis = 30000,
    showCaption = true,
    callback = object : TTSAudioFileCallback {

        override fun onRequestStart(text: String) {
            println("开始请求音频文件: $text")
        }

        override fun onRequestSuccess(text: String, audioSize: Int) {
            println("音频下载成功: $text, 大小: ${audioSize}字节")
        }

        override fun onRequestFailed(text: String, error: String) {
            println("音频下载失败: $text, 错误: $error")
        }

        override fun onPlaybackPrepared(text: String) {
            println("音频准备完成: $text")
        }

        override fun onPlaybackStarted(text: String) {
            println("开始播放: $text")
        }

        override fun onPlaybackCompleted(text: String) {
            println("播放完成: $text")
        }

        override fun onPlaybackInterrupted(text: String) {
            println("播放被中断: $text")
        }

        override fun onPlaybackError(text: String, what: Int, extra: Int) {
            println("播放错误: $text, what: $what, extra: $extra")
        }
    }
)
```

### 与流式TTS的区别

| 特性 | 流式TTS (4.6节) | TTS音频文件播放 (4.7节) |
|------|----------------|----------------------|
| **播放模式** | 流式播放，边生成边播放 | 预生成文件，下载后播放 |
| **播放行为** | 自动追加，不中断前一次播放 | 每次调用会中断上一次播放 |
| **网络依赖** | 持续网络连接 | 一次性下载 |
| **播放稳定性** | 受网络波动影响 | 下载完成后播放稳定 |
| **生命周期回调** | 简单的成功/失败回调 | 完整的播放生命周期回调 |
| **超时时间** | 默认180秒 | 默认15秒 |
| **适用场景** | 实时对话、连续播报、日常播放 | 网络不稳定环境 |

### ⚠️ 重要注意事项

> **播放行为特性**
> - **每次调用会中断上一次播放**：新的`playTtsAudioFile()`调用会立即停止当前正在播放的音频
> - **非追加播放模式**：与流式TTS的自动追加不同，音频文件播放采用替换模式

> **超时时间设置**
> - 默认超时时间为15秒，主要用于网络下载阶段
> - 音频下载完成后的播放阶段不受此超时限制
> - 建议根据网络环境和文本长度合理设置超时时间

> **播放中断机制**
> - 用户语音输入会自动中断当前播放，触发`onPlaybackInterrupted`回调
> - 调用`stopTtsAudioFile()`会强制停止播放
> - 新的`playTtsAudioFile()`调用会中断正在播放的音频
> - 播放被中断时不会触发`onPlaybackCompleted`回调

> **字幕显示控制**
> - `showCaption`参数控制是否在系统字幕条显示文本
> - 设置为`false`可实现静默播放，适合背景音效等场景

## 4.8 大模型接口

### 功能介绍
提供对大模型的直接调用能力，支持复杂的对话场景和自定义的智能交互需求。

> **💡 使用指南**：如果您对大模型接口的使用场景不太了解，建议先查看FAQ文档 [人设和PageAgent都无法满足业务需求怎么办](../../FAQ.md#人设和pageagent都无法满足业务需求怎么办) 中的相关问题解答，其中包含了详细的使用示例和最佳实践。

### API接口

**同步调用接口**

```Kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.assit.LLMResponse
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage

/**
  * 大模型接口，同步调用
  * 注：此接口需在协程中调用
  *
  *  @param  messages 大模型chat message
  *  @param  config 大模型配置
  *  @param  timeoutMillis 超时时间，单位毫秒
  *  @param  isStreaming 是否流式输出，true表示流式输出（会自动调用TTS流式播放），false表示非流式输出（会返回执行结果）
  *
  *  @return  TaskResult<LLMResponse> 任务执行结果，status=1表示成功，status=2表示失败
  */
suspend fun llmSync(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true
): TaskResult<LLMResponse>
```

**异步调用接口**

```kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage

/**
  * 大模型接口，异步调用，返回状态通过LLMCallback回调
  *
  *  @param  messages 大模型chat message
  *  @param  config 大模型配置
  *  @param  timeoutMillis 超时时间，单位毫秒
  *  @param  isStreaming 是否流式输出，true表示流式输出（会自动调用TTS流式播放），false表示非流式输出（会返回执行结果）
  *  @param  callback 回调，status=1表示播放成功，status=2表示播放失败
  */
fun llm(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true,
    callback: LLMCallback? = null
)
```

## 4.9 文本指令

### 功能介绍
通过文本形式模拟用户语音输入，触发大模型的规划和Action执行，实现程序化的智能交互。

### 应用场景
- 用户手动点击按钮时，等效于语音输入（如点击"确定"按钮等效于说"确定"）
- 应用启动时主动发起交互
- 自动化测试和调试

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
  * 通过文本形式的用户问题触发大模型规划Action
  *
  *  @param  text 用户问题的文本，如：今天天气怎么样？
  */
fun query(text: String)
```

## 4.10 感知信息上报

### 功能介绍
上传应用的场景感知信息，帮助AgentOS更好地理解当前页面内容和用户上下文，提升交互的准确性和相关性。

### 应用场景
- 上报屏幕显示的信息列表，支持用户通过语音引用（如"我想看看第3个"）
- 上报当前任务进展状态
- 上报页面组件的层次结构信息

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 上传页面信息，方便大模型理解当前页面的内容
 *
 * @param interfaceInfo 页面信息描述，最好带有页面组件的层次结构，但内容不宜过长
 */
fun uploadInterfaceInfo(interfaceInfo: String)
```

> **⚠️ 重要提示**：`uploadInterfaceInfo` 采用**覆盖式更新**机制
> - 每次调用都会**完全替换**之前上传的信息
> - 如需保留部分信息，请在新的调用中包含完整的页面信息
> - 建议在页面内容发生变化时重新上传完整的页面状态信息

## 4.11 对话历史管理

### 功能介绍
清空大模型的对话上下文记录，防止历史对话干扰当前交互，确保对话的准确性和相关性。

### 应用场景
- 应用重置对话内容时
- 用户切换到新的话题时
- 需要开始全新对话场景时

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 清空大模型对话上下文记录
 */
fun clearContext()
```

## 4.12 禁用大模型规划

### 功能介绍
控制是否禁用大模型规划功能。默认情况下，AgentOS与用户的每次交互都会通过大模型进行规划。当设置为`true`时，将禁用大模型规划，用户可以自行处理大模型的调用。

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 是否禁用大模型规划，禁用后不会再进行大模型规划，true表示禁用，默认为false
 * 用户如果需要自行处理大模型的调用则可设置为true
 */
var isDisablePlan: Boolean
```

## 4.13 应用跳转

### 功能介绍
提供快速跳转到小豹应用的功能，实现应用间的无缝切换。

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore
import android.content.Context

/**
 * 跳转到小豹应用
 *
 * @param context 上下文，用于启动Activity
 */
fun jumpToXiaobao(context: Context)
```

> **注意**: 调用此方法前请确保小豹应用已安装，如果未安装会在日志中输出相应提示信息。

## 4.14 系统Action状态监听

### 功能介绍
监听[系统内置Action](#24-系统内置action)的执行状态变化，不包括自定义Action。

### API接口

```Kotlin
fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): Agent
```

### 使用示例

```Kotlin
PageAgent(this)
    .setOnActionStatusChangedListener { actionName, status, message ->
        println("System Action: $actionName, Status: $status, Message: $message")
        false // 返回false表示不消费事件，继续传递给其他监听器(AppAgent)
    }
```

**状态类型**: `succeeded`(成功)、`failed`(失败)、`timeout`(超时)、`interrupted`(中断)、`recalled`(撤回)、`unsupported`(不支持)

> **注意**: 回调在子线程中执行。

## 4.15 唤醒词模式控制

### 功能介绍

唤醒词模式是AgentOS SDK v0.4.5新增的语音交互控制功能，提供更灵活的拾音触发机制。通过三个核心接口，开发者可以精确控制基于唤醒词的语音交互行为，适配不同场景下的交互需求。

### 核心接口

#### enableWakeupMode()

启用或禁用唤醒词模式。

**API接口**

```kotlin
import com.ainirobot.agent.AgentCore

/**
 * 启用/禁用唤醒词模式
 *
 * @param enabled true表示启用唤醒词模式，false表示禁用（默认）
 */
AgentCore.enableWakeupMode(enabled: Boolean)
```

**行为说明**

- **enabled = true（启用唤醒词模式）**
  - 必须通过唤醒词唤醒才能开始拾音识别
  - 避免误拾音

- **enabled = false（禁用唤醒词模式，默认）**
  - VAD检测到语音即开始识别
  - 无需唤醒词

**使用示例**

```kotlin
// 启用唤醒词模式
AgentCore.enableWakeupMode(true)

// 禁用唤醒词模式
AgentCore.enableWakeupMode(false)
```

#### setWakeupVadTimeout()

设置VAD结束后的拾音超时时间。

**API接口**

```kotlin
import com.ainirobot.agent.AgentCore

/**
 * 设置VAD结束后的拾音超时时间
 *
 * @param timeout 超时时间，单位毫秒，范围：1000ms ~ 10000ms，默认 3000ms
 */
AgentCore.setWakeupVadTimeout(timeout: Long)
```

**功能说明**

控制用户说完话（VAD END）后，继续保持拾音的时长。如果超时内用户继续说话，无需再次唤醒。

**使用示例**

```kotlin
// 设置VAD超时为5秒
AgentCore.setWakeupVadTimeout(5000L)
```

#### setWakeupQuestionTimeout()

设置AI问问题后的拾音窗口时长。

**API接口**

```kotlin
import com.ainirobot.agent.AgentCore

/**
 * 设置AI问问题后的拾音窗口时长
 *
 * @param timeout 超时时间，单位毫秒，范围：3000ms ~ 30000ms，默认 10000ms
 */
AgentCore.setWakeupQuestionTimeout(timeout: Long)
```

**功能说明**

当AI说话以"？"结尾时，自动开启拾音窗口，用户可直接回答，无需唤醒词。

**触发规则**

- AI说话以中文问号"？"结尾
- 延迟1.5秒确认，避免误判
- 示例：
  - ✅ AI："你想喝什么？" → 触发拾音
  - ❌ AI："今天很冷。外面下雨了。" → 不触发

**使用示例**

```kotlin
// 设置问题拾音窗口为15秒
AgentCore.setWakeupQuestionTimeout(15000L)
```

### 使用说明

三个接口配合使用控制唤醒词模式：

1. **enableWakeupMode()** - 启用/禁用唤醒词模式（总开关）
2. **setWakeupVadTimeout()** - VAD超时时间（用户说话后保持拾音的时长）
3. **setWakeupQuestionTimeout()** - 问题拾音窗口时长（AI问问题后自动拾音的时长）

**示例代码**

```kotlin
// 启用唤醒词模式
AgentCore.enableWakeupMode(true)
AgentCore.setWakeupVadTimeout(5000L)
AgentCore.setWakeupQuestionTimeout(15000L)

// 禁用唤醒词模式
AgentCore.enableWakeupMode(false)
```

# 5. 进阶功能

## 5.1 注解驱动的Action注册

### 功能介绍
对于希望简化Action注册流程的开发者，AgentOS SDK提供了基于注解的自动注册机制。通过在方法上添加注解，SDK会在运行时自动识别并注册这些Action，大大简化了开发流程。

### 5.1.1 App级动态注册

在Application中使用注解方式自动注册Action时，需要使用`AppAgent(Application)`构造方法，然后在Application中创建成员方法并添加相应注解。

**示例代码**

```kotlin
import android.app.Application
import android.os.Bundle
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.annotations.ActionParameter
import com.ainirobot.agent.annotations.AgentAction
import com.ainirobot.agent.coroutine.AOCoroutineScope
import kotlinx.coroutines.launch

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // AppAgent初始化
        object : AppAgent(this) {

            override fun onCreate() {
                // 设定角色人设
                setPersona("你叫豹姐姐，是一位聪明、亲切又略带俏皮的虚拟助手。")
                // 设定角色目标
                setObjective("通过自然的对话和合适的情绪表达，让用户感受到理解、陪伴与情感共鸣，从而提升交流的舒适感和信任感。")
            }

            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // 在此处处理静态注册的action，如果你不需要处理，请返回false，如果要自行处理且不需要后续处理，则返回true
                // 默认返回false
                return false
            }
        }
    }

    @AgentAction(
        name = "com.agent.demo.SHOW_SMILE_FACE",
        displayName = "笑",
        desc = "响应用户的开心、满意或正面情绪"
    )
    private fun showSmileFace(
        action: Action,
        @ActionParameter(
            name = "sentence",
            desc = "回复给用户的话"
        )
        sentence: String
    ): Boolean {
        AOCoroutineScope.launch {
            // 播放给用户说的话
            AgentCore.ttsSync(sentence)
            // 播放完成后，及时上报Action的执行状态
            action.notify(isTriggerFollowUp = false)
        }
        return true
    }
}
```

### 5.1.2 Page级动态注册

在页面中使用注解方式自动注册Action时，需要使用`PageAgent(Activity)`或`PageAgent(Fragment)`构造方法，然后在对应的Activity或Fragment中创建成员方法并添加注解。

**示例代码**

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.annotations.ActionParameter
import com.ainirobot.agent.annotations.AgentAction
import com.ainirobot.agent.coroutine.AOCoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // PageAgent初始化
        PageAgent(this)
    }

    @AgentAction(
        name = "com.agent.demo.SHOW_SMILE_FACE",
        displayName = "笑",
        desc = "响应用户的开心、满意或正面情绪"
    )
    private fun showSmileFace(
        action: Action,
        @ActionParameter(
            name = "sentence",
            desc = "回复给用户的话"
        )
        sentence: String
    ): Boolean {
        AOCoroutineScope.launch {
            // 播放给用户说的话
            AgentCore.ttsSync(sentence)
            // 播放完成后，及时上报Action的执行状态
            action.notify(isTriggerFollowUp = false)
        }
        return true
    }
}
```

### 5.1.3 注解类说明

AgentOS SDK提供了两个核心注解：`@AgentAction`和`@ActionParameter`。

#### @AgentAction注解

`@AgentAction`用于标记方法为Action处理器，定义Action的基本信息。

```kotlin
package com.ainirobot.agent.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AgentAction(
    /**
      * Action的名称
      */
      val name: String,
    /**
      * Action的描述
      */
      val desc: String,
    /**
      * Action的显示名称
      */
      val displayName: String
)
```

#### @ActionParameter注解

`@ActionParameter`用于标记方法参数，定义Action的参数信息。

```kotlin
package com.ainirobot.agent.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActionParameter(
    /**
      * 参数名
      */
      val name: String,
    /**
      * 参数描述
      */
      val desc: String,
    /**
      * 是否是必要参数
      */
      val required: Boolean = true,
    /**
      * 限制参数的value只能从指定的值中选择
      */
      val enumValues: Array<String> = []
)
```

# 6. Action设计最佳实践

> **⚠️ 重要提醒**：Action设计的质量直接影响大模型的理解和执行效果。不当的设计可能导致功能无法正确触发、参数解析错误、用户体验差等问题。请务必遵循本节的最佳实践原则。

在实际开发中，很多开发者在设计Action时容易陷入一些常见误区。以下通过对比示例来说明Action设计的最佳实践。

## 6.1 常见设计误区

**❌ 错误示例：功能过于复杂的Action**

```kotlin
Action(
    name = "com.service.robot.HANDLE_TASK",
    displayName = "处理任务",
    desc = "智能服务机器人助手，可以执行多种服务任务，包括但不限于信息播报、任务提醒、物品配送等各类服务，根据用户需求灵活响应",
    parameters = listOf(
        Parameter("task_type", ParameterType.STRING, "任务类型", true),
        Parameter("target_location", ParameterType.STRING, "目标位置", false),
        Parameter("content", ParameterType.STRING, "任务内容", false),
        Parameter("items", ParameterType.STRING, "相关物品", false),
        Parameter("schedule_time", ParameterType.STRING, "执行时间", false)
    )
)
```

**问题分析：**
- 功能职责不明确，一个Action承担多种任务
- 参数过多且含义模糊
- 描述过于宽泛，大模型难以准确理解使用场景
- 缺乏参数枚举限制，容易产生不可控的输入

## 6.2 最佳实践原则

写 `Action` 时，记住三个核心点：

**1. 单一职责原则：一个 Action 只做一类任务**
- 每个Action只负责一个明确的功能，避免多个功能混在一起
- 避免通过参数来区分不同的业务逻辑
- 如果发现需要判断 `type` 参数再决定具体干什么，说明设计得太笼统，应该拆分
- Action名称和描述要准确详细，让大模型能清楚理解功能边界和使用场景

**2. 参数设计精准：每个参数都要写清楚类型、描述、是否必填，以及枚举限制（如果适用）**
- 参数数量适中（建议1-3个）
- 每个参数都有明确的业务含义和准确的描述
- 需要限制取值范围时使用ParameterType.ENUM和enumValues，避免自由字符串带来混乱
- 合理设置必填和可选参数

**3. 槽位语义明确：对用户输入可能出现的槽位要有明确语义与验证机制**
- 分析并设计用户可能输入的关键信息槽位（如房间号、事项类型、目的地等）
- 为每个槽位定义清晰的语义范围和取值规则
- 确保槽位信息能被准确提取和验证，避免歧义解析

## 6.3 正确示例：拆分为多个专门Action

**✅ 示例1：引领带路Action（1个参数）**

```kotlin
Action(
    name = "com.service.robot.LEADING_TO_DESTINATION",
    displayName = "引领带路",
    desc = "机器人引领用户前往指定目的地，提供路线指引和陪同服务",
    parameters = listOf(
        Parameter(
            name = "destination",
            type = ParameterType.STRING,
            desc = "目的地名称，如卫生间、电梯、服务台、会议室等",
            required = true
        )
    )
)
```

**✅ 示例2：播放音乐Action（2个参数）**

```kotlin
Action(
    name = "com.entertainment.robot.PLAY_MUSIC",
    displayName = "播放音乐",
    desc = "播放指定歌手的指定歌曲",
    parameters = listOf(
        Parameter(
            name = "song_name",
            type = ParameterType.STRING,
            desc = "歌曲名称",
            required = false
        ),
        Parameter(
            name = "artist_name",
            type = ParameterType.STRING,
            desc = "歌手名称",
            required = false
        )
    )
)
```

**✅ 示例3：调节空调Action（1个参数，使用枚举类型）**

```kotlin
Action(
    name = "com.smart.home.ADJUST_AIRCON",
    displayName = "调节空调",
    desc = "调节空调的运行模式",
    parameters = listOf(
        Parameter(
            name = "mode",
            type = ParameterType.ENUM,
            desc = "空调模式",
            required = true,
            enumValues = listOf("制冷", "制热", "除湿", "送风", "自动")
        )
    )
)
```

## 6.4 设计检查清单

在设计Action时，请检查以下要点：

- [ ] **功能单一**：Action是否只负责一个明确的业务功能？
- [ ] **参数精简**：参数数量是否合理（建议1-3个）？
- [ ] **描述准确**：Action和参数的描述是否具体明确？
- [ ] **枚举限制**：需要限制取值范围的参数是否使用了ParameterType.ENUM？
- [ ] **必填合理**：required参数设置是否符合业务逻辑？
- [ ] **命名规范**：是否遵循了命名规范约束？

> **🔥 核心提醒**：如果发现Action需要通过参数来判断执行不同的业务逻辑，这通常意味着设计过于复杂，应该考虑拆分为多个专门的Action。良好的Action设计是AgentOS应用成功的关键基础！

# 7. 项目资源

## 7.1 示例项目

### 模板项目
提供基础的项目结构和配置，适合快速开始AgentOS SDK开发。

**项目地址**: [AgentSDKSampleEmpty](https://github.com/orionagent/AgentSDKSampleEmpty)

### 完整示例项目
包含多种功能示例和最佳实践，展示AgentOS SDK的完整能力。

**项目地址**: [AgentSDKSample](https://github.com/orionagent/AgentSDKSample)

## 7.2 开发资源

### 接待后台
用于申请AppId和管理Agent应用的后台系统。

[点击访问接待后台](https://jiedai.ainirobot.com/web/portal/#)


# 8. 常见问题FAQ

在使用AgentOS SDK开发过程中遇到问题时，建议优先查看FAQ文档，其中包含了开发环境配置、语音交互问题排查、调试工具使用等常见问题的解决方案。

**📋 [查看完整FAQ文档](../../FAQ.md)**

# 9. 技术支持

如有任何问题，请联系技术支持团队。
