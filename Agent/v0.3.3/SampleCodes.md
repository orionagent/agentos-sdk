# AgentSDK 示例代码文档

## 项目概述

本项目是一个基于 AgentSDK 的 Android 应用示例，展示了如何使用 AgentSDK 创建具有情感交互能力的智能助手应用。应用通过识别用户的情绪状态，显示相应的表情图标并进行语音回复。

## 环境要求

### 开发环境
- Android SDK 版本: 最低支持API 26 (Android 8.0)
- JDK版本: Java 11
- Kotlin、Java语言开发

### 运行环境
AgentOS Product Version: V1.3.0.250515

## 快速开始

如果没有任何Android开发经验，请先下载安装[Android Studio](https://developer.android.com/studio?hl=zh-cn)，然后下载我们提供的空项目，用Android Studio打开此空项目后再开始接下来的步骤。

## AgentSDK 依赖配置

### 1. 配置仓库

#### Groovy 配置 (settings.gradle)

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven {
            credentials {
                username = "agentMaven"
                password = "agentMaven"
            }
            url "https://npm.ainirobot.com/repository/maven-public/"
        }
    }
}
```

#### Kotlin 配置 (settings.gradle.kts)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven {
            credentials.username = "agentMaven"
            credentials.password = "agentMaven"
            url = uri("https://npm.ainirobot.com/repository/maven-public/")
        }
    }
}
```

### 2. 添加依赖

#### Groovy 配置 (app/build.gradle)

```groovy
dependencies {
    implementation 'com.orionstar.agent:sdk:0.3.3-SNAPSHOT'
    
    // 以下是Android标准库，默认kotlin项目都会依赖，
    // 如果编译报未找到错误，再添加以下依赖库
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

#### Kotlin 配置 (app/build.gradle.kts)

```kotlin
dependencies {
    implementation("com.orionstar.agent:sdk:0.3.3-SNAPSHOT")
    
    // 以下是Android标准库，默认kotlin项目都会依赖，
    // 如果编译报未找到错误，再添加以下依赖库
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
}
```

### 3. 添加注册表

查看项目根目录的 `app/src/main` 目录下，是否包含 `assets` 目录，如果没有请先创建 `assets` 目录，然后在 `assets` 目录下创建 `actionRegistry.json` 文件，并在文件中添加以下配置：

#### actionRegistry.json

```json
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

**配置说明：**
- `appId`: Agent应用的appId，需在接待后台申请
- `platform`: 当前运行的平台，如：opk或apk
- `actionList`: 可以从外部调起的action（只能是app级），在注册表中声名的action需要在AppAgent的onExecuteAction方法中处理action的执行，注：如果不想对外暴露action，actionList可以设置为空数组[]

## AgentSDK 核心组件

### 1. 应用级 Agent 实现

#### MainApplication.kt

```kotlin
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.Actions

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 创建应用级 Agent
        object : AppAgent(this) {

            /**
             * AppAgent 初始化回调
             * 用于配置 Agent 的基本属性和动态注册 Action
             */
            override fun onCreate() {
                // 设定 AI 助手的角色人设
                setPersona(
                    "你叫豹姐姐，是一位聪明、亲切又略带俏皮的虚拟助手，擅长倾听与共情。你在对话中能够敏锐捕捉用户情绪，用适当的表情反应来陪伴用户。"
                )

                // 设定对话风格
                setStyle("语气温和、有耐心")

                // 设定 AI 助手的任务目标
                setObjective(
                    "根据与用户的对话展示不同的表情，以响应用户的情绪，让用户感受到理解、陪伴与情感共鸣，从而提升交流的舒适感和信任感。"
                )

                // 动态注册系统 Action
                registerAction(Actions.EXIT)
            }

            /**
             * 处理静态注册的 Action 执行
             * 只有在 actionRegistry.json 中静态注册的 Action 才会触发此方法
             */
            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // 处理静态注册的 Action
                // 返回 false 表示不需要自行处理，返回 true 表示已处理完成
                return false
            }
        }
    }
}
```

**核心方法说明：**
- `setPersona()`: 设置 AI 助手的角色人设
- `setStyle()`: 设置对话风格
- `setObjective()`: 设置任务目标
- `registerAction()`: 动态注册 Action
- `onExecuteAction()`: 处理静态注册的 Action

### 2. 页面级 Agent 实现

#### MainActivity.kt

```kotlin
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
        // ... 标准 Android 初始化代码

        // 创建页面级 Agent 并注册多个情感响应 Action
        createPageAgent()
    }

    private fun createPageAgent() {
        PageAgent(this)
            .registerAction(createSmileAction())
            .registerAction(createCryAction())
            .registerAction(createAngryAction())
    }

    /**
     * 创建笑脸 Action
     */
    private fun createSmileAction(): Action {
        return Action(
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
    }

    /**
     * 创建哭脸 Action
     */
    private fun createCryAction(): Action {
        return Action(
            name = "com.agent.demo.SHOW_CRY_FACE",
            displayName = "哭",
            desc = "响应用户的难过、失落或求助情绪",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "回复给用户的话，给于安慰",
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
    }

    /**
     * 创建生气 Action
     */
    private fun createAngryAction(): Action {
        return Action(
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
    }

    /**
     * 显示表情图片
     */
    private fun showFaceImage(resId: Int) {
        runOnUiThread {
            findViewById<ImageView>(R.id.face_view).setImageResource(resId)
        }
    }

    /**
     * 处理 Action 执行
     */
    private fun handleAction(action: Action, params: Bundle?) {
        AOCoroutineScope.launch {
            // 使用 TTS 播放回复内容
            params?.getString("sentence")?.let { 
                AgentCore.ttsSync(it) 
            }
            // 通知 Action 执行完成
            action.notify(isTriggerFollowUp = false)
        }
    }
}
```

## AgentSDK 核心概念

### 1. Action 结构

```kotlin
class Action(
    name: String,           // Action 全名，建议格式：com.company.action.ACTION_NAME
    displayName: String,    // 显示名称
    desc: String,           // 功能描述，用于 AI 理解何时调用
    parameters: List<Parameter>?, // 参数列表
    executor: ActionExecutor?     // 执行器
)
```

### 2. Parameter 结构

```kotlin
data class Parameter(
    val name: String,           // 参数名
    val type: ParameterType,    // 参数类型
    val desc: String,           // 参数描述
    val required: Boolean,      // 是否必需
    var enumValues: List<String>? = null // 枚举值（type为ENUM时使用）
)
```

### 3. ActionExecutor 接口

```kotlin
interface ActionExecutor {
    fun onExecute(action: Action, params: Bundle?): Boolean
}
```

### 4. 参数类型

```kotlin
enum class ParameterType {
    STRING,    // 字符串
    INT,       // 整数
    FLOAT,     // 浮点数
    BOOLEAN,   // 布尔值
    ENUM       // 枚举
}
```

## AgentSDK 核心 API

### 1. Agent 核心功能

```kotlin
// TTS 同步播放
AgentCore.ttsSync(text: String)

// Action 执行完成通知
action.notify(isTriggerFollowUp: Boolean)
```

### 2. 协程作用域

```kotlin
// 使用 AgentSDK 提供的协程作用域
AOCoroutineScope.launch {
    // 异步操作
}
```

### 3. AppAgent 配置方法

```kotlin
// 设置角色人设
setPersona(persona: String)

// 设置对话风格
setStyle(style: String)

// 设置任务目标
setObjective(objective: String)

// 动态注册 Action
registerAction(action: Action)
```

### 4. PageAgent 使用

```kotlin
// 创建页面级 Agent
PageAgent(activity: Activity)
    .registerAction(action: Action)  // 注册 Action
    .blockAction(actionName: String) // 排除指定 Action
    .blockActions(actionNames: List<String>) // 排除多个 Action
    .blockAllActions() // 排除所有 Action
```

## AgentSDK 最佳实践

### 1. Action 命名规范

- 使用公司域名作为前缀，避免冲突
- Action 名称使用大写字母
- 格式：`com.company.module.ACTION_NAME`

### 2. 参数设计原则

- 参数名使用英文，多个单词用下划线连接
- 避免与 Action 或 Parameter 对象的属性名相同
- 提供清晰的参数描述，帮助 AI 理解

### 3. Action 执行处理

- 在 ActionExecutor 中处理异常情况
- 及时调用 `action.notify()` 通知执行状态
- 返回正确的布尔值表示处理结果

### 4. Agent 生命周期

- **App 级 Agent**：应用前台期间生效
- **Page 级 Agent**：页面可见期间生效
- 合理选择 Agent 级别，避免资源浪费

### 5. 静态 vs 动态注册

- **静态注册**：在 actionRegistry.json 中配置，可被外部调用
- **动态注册**：在代码中注册，仅当前应用内部使用

## 总结

本示例展示了 AgentSDK 的核心功能：

1. **依赖配置**：仓库配置、版本管理
2. **Agent 实现**：应用级和页面级 Agent 的创建和配置
3. **Action 系统**：动态注册、静态注册、参数处理
4. **核心 API**：TTS 播放、协程使用、状态通知
5. **最佳实践**：命名规范、错误处理、生命周期管理

通过这个示例，开发者可以快速理解和使用 AgentSDK 构建智能交互应用。 