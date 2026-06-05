# AgentSDK Sample Code Documentation

## description

description AgentSDK description Android description，description AgentSDK description。description，description。

## description

### description
- Android SDK description: descriptionAPI 26 (Android 8.0)
- JDKdescription: Java 11
- Kotlin、Javadescription

### description
AgentOS Product Version: V1.3.0.250515

## description

descriptionAndroiddescription，description[Android Studio](https://developer.android.com/studio?hl=zh-cn)，description，descriptionAndroid Studiodescription。

## AgentSDK Dependencydescription

### 1. description

#### Groovy description (settings.gradle)

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

#### Kotlin description (settings.gradle.kts)

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

### 2. descriptionDependency

#### Groovy description (app/build.gradle)

```groovy
dependencies {
    implementation 'com.orionstar.agent:sdk:0.3.2-SNAPSHOT'
    
    // 以下是Android标准库，默认kotlin项目都会依赖，
    // 如果编译报未找到错误，再添加以下依赖库
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

#### Kotlin description (app/build.gradle.kts)

```kotlin
dependencies {
    implementation("com.orionstar.agent:sdk:0.3.2-SNAPSHOT")
    
    // 以下是Android标准库，默认kotlin项目都会依赖，
    // 如果编译报未找到错误，再添加以下依赖库
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
}
```

### 3. descriptionRegister description

descriptionTable of Contentsdescription `app/src/main` Table of Contentsdescription，description `assets` Table of Contents，description `assets` Table of Contents，description `assets` Table of Contentsdescription `actionRegistry.json` description，description：

#### actionRegistry.json

```json
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

**descriptionDescription：**
- `appId`: AgentdescriptionappId，description
- `platform`: description，description：opkdescriptionapk
- `actionList`: descriptionaction（descriptionappdescription），descriptionRegister descriptionactiondescriptionAppAgentdescriptiononExecuteActionMethodsdescriptionactiondescriptionexecute，description：descriptionaction，actionListdescriptionSet description[]

## AgentSDK description

### 1. App-level Agent Implementation

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

**descriptionMethodsDescription：**
- `setPersona()`: Set  AI descriptionpersona
- `setStyle()`: Set conversation style
- `setObjective()`: Set description
- `registerAction()`: Dynamic registration Action
- `onExecuteAction()`: descriptionStatic registrationdescription Action

### 2. description Agent description

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

## AgentSDK description

### 1. Action description

```kotlin
class Action(
    name: String,           // Action 全名，建议格式：com.company.action.ACTION_NAME
    displayName: String,    // 显示名称
    desc: String,           // 功能描述，用于 AI 理解何时调用
    parameters: List<Parameter>?, // 参数列表
    executor: ActionExecutor?     // 执行器
)
```

### 2. Parameter description

```kotlin
data class Parameter(
    val name: String,           // 参数名
    val type: ParameterType,    // 参数类型
    val desc: String,           // 参数描述
    val required: Boolean,      // 是否必需
    var enumValues: List<String>? = null // 枚举值（type为ENUM时使用）
)
```

### 3. ActionExecutor description

```kotlin
interface ActionExecutor {
    fun onExecute(action: Action, params: Bundle?): Boolean
}
```

### 4. description

```kotlin
enum class ParameterType {
    STRING,    // 字符串
    INT,       // 整数
    FLOAT,     // 浮点数
    BOOLEAN,   // 布尔值
    ENUM       // 枚举
}
```

## AgentSDK description API

### 1. Agent description

```kotlin
// TTS 同步播放
AgentCore.ttsSync(text: String)

// Action 执行完成通知
action.notify(isTriggerFollowUp: Boolean)
```

### 2. description

```kotlin
// 使用 AgentSDK 提供的协程作用域
AOCoroutineScope.launch {
    // 异步操作
}
```

### 3. AppAgent descriptionMethods

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

### 4. PageAgent description

```kotlin
// 创建页面级 Agent
PageAgent(activity: Activity)
    .registerAction(action: Action)  // 注册 Action
    .blockAction(actionName: String) // 排除指定 Action
    .blockActions(actionNames: List<String>) // 排除多个 Action
    .blockAllActions() // 排除所有 Action
```

## AgentSDK description

### 1. Action description

- description，description
- Action description
- description：`com.company.module.ACTION_NAME`

### 2. description

- Use English parameter names and join multiple words with underscores
- description Action description Parameter descriptionPropertiesdescription
- description，description AI description

### 3. Action executedescription

- description ActionExecutor description
- description `action.notify()` descriptionexecutedescription
- description

### 4. Agent description

- **App description Agent**：description
- **Page description Agent**：description
- description Agent description，description

### 5. description vs Dynamic registration

- **Static registration**：description actionRegistry.json description，description
- **Dynamic registration**：descriptionRegister ，description

## Summary

description AgentSDK description：

1. **Dependencydescription**：description、description
2. **Agent description**：description Agent description
3. **Action description**：Dynamic registration、Static registration、description
4. **description API**：TTS description、description、description
5. **description**：description、description、description

description，description AgentSDK description。 
