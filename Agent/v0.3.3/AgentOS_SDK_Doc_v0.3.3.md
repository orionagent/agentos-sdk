# AgentOS SDK for APK V0.3.3文档

## 📋 版本信息

![SDK](https://img.shields.io/badge/SDK_Version-v0.3.3-blue)
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
  - [1.3 Agent角色配置 - 让您的AI更有个性](#13-agent角色配置---让您的ai更有个性)
    - [1.3.1 概述](#131-概述)
    - [1.3.2 三大核心配置](#132-三大核心配置)
    - [1.3.3 配置层级与优先级](#133-配置层级与优先级)
    - [1.3.4 API接口详解](#134-api接口详解)
    - [1.3.5 扩展配置能力](#135-扩展配置能力)
    - [1.3.6 配置最佳实践](#136-配置最佳实践)
    - [1.3.7 典型应用场景](#137-典型应用场景)
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
- [3. 核心API接口](#3-核心api接口)
  - [3.1 麦克风控制](#31-麦克风控制)
  - [3.2 ASR与TTS监听](#32-asr与tts监听)
  - [3.3 Agent状态监听](#33-agent状态监听)
  - [3.4 语音条控制](#34-语音条控制)
  - [3.5 TTS语音合成](#35-tts语音合成)
  - [3.6 大模型接口](#36-大模型接口)
  - [3.7 文本指令](#37-文本指令)
  - [3.8 感知信息上报](#38-感知信息上报)
  - [3.9 对话历史管理](#39-对话历史管理)
  - [3.10 免唤醒功能](#310-免唤醒功能)
  - [3.11 禁用大模型规划](#311-禁用大模型规划)
  - [3.12 应用跳转](#312-应用跳转)
- [4. 进阶功能](#4-进阶功能)
  - [4.1 注解驱动的Action注册](#41-注解驱动的action注册)
    - [4.1.1 App级动态注册](#411-app级动态注册)
    - [4.1.2 Page级动态注册](#412-page级动态注册)
    - [4.1.3 注解类说明](#413-注解类说明)
- [5. 项目资源](#5-项目资源)
- [6. 技术支持](#6-技术支持)

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

**Groovy DSL配置**

在`app/build.gradle`文件中添加以下依赖：

```Groovy
dependencies {
        implementation 'com.orionstar.agent:sdk:0.3.3-SNAPSHOT' // 【重要配置】Agent SDK依赖
        
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
        implementation("com.orionstar.agent:sdk:0.3.3-SNAPSHOT") // 【重要配置】Agent SDK依赖
        
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

> **关键提醒**: 任何Action执行完成后都必须调用`action.notify()`方法通知系统执行状态。

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

# 1.3 Agent角色配置 - 让您的AI更有个性

## 1.3.1 概述

AgentOS SDK提供了强大的角色配置功能，让您可以为AI助手定制独特的身份、风格和目标。通过三个核心配置方法，您可以打造出具有鲜明个性的智能助手。

## 1.3.2 三大核心配置

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

## 1.3.3 配置层级与优先级

### 配置层级
```
PageAgent配置 > AppAgent配置 > 系统默认
```

**说明**：PageAgent未设置时，自动继承AppAgent的配置

## 1.3.4 API接口详解

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

## 1.3.5 扩展配置能力

当基础的三项配置无法满足复杂需求时，可以结合使用**感知信息上报**功能（详见[3.8节](#38-感知信息上报)）进行更灵活的信息传递：

```kotlin
// 上传复杂的页面信息和业务状态
val pageInfo = """
当前商品：${product.name}
用户状态：${user.memberLevel}会员
特殊提示：该商品正在促销中
"""

AgentCore.uploadInterfaceInfo(pageInfo)
```

> **提示**：`uploadInterfaceInfo`方法的详细用法请参考[3.8 感知信息上报](#38-感知信息上报)章节。

## 1.3.6 配置最佳实践

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

## 1.3.7 典型应用场景

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

# 2. Action详解

## 2.1 Action概念

AgentOS的核心机制是通过识别用户意图来执行相应的技能模块，这些技能模块被称为Action。例如，当用户询问"我明天去深圳需要带伞吗？"时，系统会识别出查询天气的意图，并调用相应的天气查询Action（如：`orion.agent.action.WEATHER`）。

### 2.1.1 基础属性

Action类的核心属性定义如下：

```Kotlin
package com.ainirobot.agent.action

open class Action(
    /**
      * action全名，结构最好是公司域名+action简名，避免与其它app中的action冲突
      * action简名必须大写，示例：com.orion.action.WEATHER
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

> **最佳实践**: 创建Action时需要清晰、准确地描述各项属性，以便大模型能够准确理解Action的功能并做出精确的选择。

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

> **重要提醒**: 
> - 参数的`desc`属性必须能够精确反映参数的定义，确保大模型理解准确
> - 参数名`name`建议使用英文，多个单词用下划线连接
> - **参数名不得与Action对象或Parameter对象的属性名相同或相似**，以避免歧义

## 2.2 Action注册机制

Action按生命周期分为App级和Page级两种类型，它们的活跃周期不同。

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

# 3. 核心API接口

## 3.1 麦克风控制

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

## 3.2 ASR与TTS监听

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

## 3.3 Agent状态监听

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

## 3.4 语音条控制

### 功能介绍
控制系统默认语音条的显示和隐藏，满足不同应用场景的UI需求。

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 是否开启语音条，默认开启
 */
var isEnableVoiceBar: Boolean
    get() = appAgent?.isEnableVoiceBar ?: true
    set(value) {
        appAgent?.isEnableVoiceBar = value
    }
```

## 3.5 TTS语音合成

### 功能介绍
主动调用系统的语音合成接口，将指定文本转换为音频并自动播放，支持同步和异步两种调用方式。

### 应用场景
- 应用启动时播放欢迎语
- 业务流程中的语音提示
- 主动与用户进行语音交互

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
suspend fun ttsSync(text: String, timeoutMillis: Long = 180000): TaskResult<String> {
    return this.appAgent?.api?.ttsSync(text, timeoutMillis) ?: TaskResult(2)
}
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
) {
    this.appAgent?.api?.tts(text, timeoutMillis, callback)
}
```

**停止播放**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 强制打断TTS播放
 */
fun stopTTS() {
    this.appAgent?.api?.stopTTS()
}
```

## 3.6 大模型接口

### 功能介绍
提供对大模型的直接调用能力，支持复杂的对话场景和自定义的智能交互需求。

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
): TaskResult<LLMResponse> {
    return this.appAgent?.api?.llmSync(messages, config, timeoutMillis, isStreaming) ?: TaskResult(2)
}
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
) {
    this.appAgent?.api?.llm(messages, config, timeoutMillis, isStreaming, callback)
}
```

## 3.7 文本指令

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
fun query(text: String) {
    this.appAgent?.api?.query(text)
}
```

## 3.8 感知信息上报

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
fun uploadInterfaceInfo(interfaceInfo: String) {
    this.appAgent?.api?.uploadInterfaceInfo(interfaceInfo)
}
```

## 3.9 对话历史管理

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
fun clearContext() {
    this.appAgent?.api?.clearContext()
}
```

## 3.10 免唤醒功能

### 功能介绍
免唤醒是AgentOS的核心功能，能够自动过滤环境噪音和周围其他人声，专注服务于当前与机器人交互的用户。该功能默认开启，会智能识别用户的说话意图并严格限制收音范围。

### API接口

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 是否开启免唤醒功能，默认true，开启
 */
var isEnableWakeFree: Boolean
    get() = appAgent?.isEnableWakeFree ?: true
    set(value) {
        appAgent?.isEnableWakeFree = value
    }
```

## 3.11 禁用大模型规划

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
    get() = appAgent?.isDisablePlan ?: false
    set(value) {
        appAgent?.isDisablePlan = value
    }
```

## 3.12 应用跳转

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

# 4. 进阶功能

## 4.1 注解驱动的Action注册

### 功能介绍
对于希望简化Action注册流程的开发者，AgentOS SDK提供了基于注解的自动注册机制。通过在方法上添加注解，SDK会在运行时自动识别并注册这些Action，大大简化了开发流程。

### 4.1.1 App级动态注册

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

### 4.1.2 Page级动态注册

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

### 4.1.3 注解类说明

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

# 5. 项目资源

## 5.1 示例项目

### 模板项目
提供基础的项目结构和配置，适合快速开始AgentOS SDK开发。

**项目地址**: [AgentSDKSampleEmpty](https://github.com/orionagent/AgentSDKSampleEmpty)

### 完整示例项目
包含多种功能示例和最佳实践，展示AgentOS SDK的完整能力。

**项目地址**: [AgentSDKSample](https://github.com/orionagent/AgentSDKSample)

## 5.2 开发资源

### 接待后台
用于申请AppId和管理Agent应用的后台系统。

**访问地址**: https://jiedai.ainirobot.com/web/portal/#/frame/hmag-agentos/hmag-agentos.agentapp/


# 6. 技术支持

如有任何问题，请联系技术支持团队。
