# AgentOS SDK v0.3.5 API 参考文档

> **版本信息**  
> 当前SDK版本: 0.3.5  
> 依赖: `implementation 'com.orionstar.agent:sdk:0.3.5-SNAPSHOT'`

## 📋 目录

### 核心类
- [AppAgent](#appagent) - 应用级Agent，管理全局Action和应用生命周期
- [PageAgent](#pageagent) - 页面级Agent，管理页面Action和页面生命周期
- [AgentCore](#agentcore) - Agent核心功能类，提供静态方法
- [Action](#action) - Action定义类，封装动作的所有属性
- [ActionExecutor](#actionexecutor) - Action执行器接口
- [Actions](#actions) - 系统内置Action常量类
- [Parameter](#parameter) - Action参数定义类
- [ParameterType](#parametertype) - 参数类型枚举

### 基础类
- [Agent](#agent) - Agent基础类，AppAgent和PageAgent的父类

### 监听器接口
- [OnTranscribeListener](#ontranscribelistener) - ASR和TTS结果监听接口
- [OnAgentStatusChangedListener](#onagentstatus-changedlistener) - Agent状态变化监听接口
- [OnActionStatusChangedListener](#onactionstatuschangedlistener) - 系统Action状态变化监听接口
- [TTSCallback](#ttscallback) - TTS播放回调接口
- [LLMCallback](#llmcallback) - 大模型调用回调接口
- [ITaskCallback](#itaskcallback) - 任务执行回调接口

### 数据类
- [Transcription](#transcription) - 语音转录结果类
- [LLMMessage](#llmmessage) - 大模型消息类
- [LLMConfig](#llmconfig) - 大模型配置类
- [LLMResponse](#llmresponse) - 大模型响应结果类
- [TokenCost](#tokencost) - Token消耗统计类
- [ActionResult](#actionresult) - Action执行结果类
- [TaskResult](#taskresult) - 任务执行结果类

### 枚举类
- [Role](#role) - 消息角色枚举
- [ActionStatus](#actionstatus) - Action执行状态枚举

### 注解类
- [AgentAction](#agentaction) - Action注解，用于标记Action方法
- [ActionParameter](#actionparameter) - 参数注解，用于标记Action参数

### 工具类
- [AOCoroutineScope](#aocoroutinescope) - Agent协程作用域

---

## 核心类

### AppAgent

应用级Agent，管理整个应用的Agent生命周期和全局Action。一个App中只能有一个AppAgent实例。

**包路径：** `com.ainirobot.agent.AppAgent`

**构造函数：**
```kotlin
AppAgent(context: Application)
```

**参数说明：**
- `context: Application` - Android应用程序实例

**核心方法：**

#### onCreate()
```kotlin
abstract fun onCreate()
```
AppAgent初始化回调，用于配置Agent的基本属性和动态注册Action。

#### onExecuteAction()
```kotlin
abstract fun onExecuteAction(action: Action, params: Bundle?): Boolean
```
处理静态注册的Action执行。只有在actionRegistry.json中静态注册的Action才会触发此方法。

**参数说明：**
- `action: Action` - 要执行的Action对象
- `params: Bundle?` - Action执行参数，可为空

**返回值：**
- `Boolean` - true表示已处理完成，false表示不需要自行处理

#### setPersona()
```kotlin
override fun setPersona(persona: String): AppAgent
```
设置AI助手的角色人设。

**参数说明：**
- `persona: String` - 角色人设描述

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### setStyle()
```kotlin
override fun setStyle(style: String): AppAgent
```
设置对话风格。

**参数说明：**
- `style: String` - 对话风格描述

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### setObjective()
```kotlin
override fun setObjective(objective: String): AppAgent
```
设置任务目标。

**参数说明：**
- `objective: String` - 任务目标描述

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### setOnAgentStatusChangedListener()
```kotlin
override fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): AppAgent
```
设置Agent状态变化监听器。

**参数说明：**
- `listener: OnAgentStatusChangedListener` - 状态变化监听器

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### setOnTranscribeListener()
```kotlin
override fun setOnTranscribeListener(listener: OnTranscribeListener): AppAgent
```
设置ASR和TTS监听器。

**参数说明：**
- `listener: OnTranscribeListener` - 转录结果监听器

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### setOnActionStatusChangedListener()
```kotlin
override fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): AppAgent
```
设置系统Action状态变化监听器。

**参数说明：**
- `listener: OnActionStatusChangedListener` - 系统Action状态变化监听器

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### registerAction()
```kotlin
override fun registerAction(action: Action): AppAgent
override fun registerAction(actionName: String): AppAgent
```
动态注册Action。

**参数说明：**
- `action: Action` - 要注册的Action对象
- `actionName: String` - 外部Action的名称（重载方法）

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### registerActionNames()
```kotlin
override fun registerActionNames(actionNames: List<String>): AppAgent
```
批量注册外部Action名称。

**参数说明：**
- `actionNames: List<String>` - 外部Action的名称列表

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

#### registerActions()
```kotlin
override fun registerActions(actionList: List<Action>): AppAgent
```
批量注册Action。

**参数说明：**
- `actionList: List<Action>` - Action列表

**返回值：**
- `AppAgent` - 返回自身，支持链式调用

---

### PageAgent

页面级Agent，管理单个页面的Agent功能和页面级Action。每个页面只能有一个PageAgent实例。

**包路径：** `com.ainirobot.agent.PageAgent`

**构造函数：**
```kotlin
PageAgent(pageId: String)
PageAgent(activity: Activity)
PageAgent(fragment: Fragment)
```

**参数说明：**
- `pageId: String` - 页面唯一标识符（主构造函数）
- `activity: Activity` - Android Activity实例
- `fragment: Fragment` - Android Fragment实例

**核心方法：**

#### setPersona()
```kotlin
override fun setPersona(persona: String): PageAgent
```
设置此Agent的角色人设。

**参数说明：**
- `persona: String` - 人设描述

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setStyle()
```kotlin
override fun setStyle(style: String): PageAgent
```
设置此Agent的对话风格。

**参数说明：**
- `style: String` - 对话风格

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setObjective()
```kotlin
override fun setObjective(objective: String): PageAgent
```
设置此Agent的规划目标。

**参数说明：**
- `objective: String` - 规划目标

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setOnAgentStatusChangedListener()
```kotlin
override fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): PageAgent
```
设置Agent状态变化监听器。

**参数说明：**
- `listener: OnAgentStatusChangedListener` - 状态变化监听器

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setOnTranscribeListener()
```kotlin
override fun setOnTranscribeListener(listener: OnTranscribeListener): PageAgent
```
设置ASR和TTS监听器。

**参数说明：**
- `listener: OnTranscribeListener` - 转录结果监听器

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### registerAction()
```kotlin
override fun registerAction(action: Action): PageAgent
override fun registerAction(actionName: String): PageAgent
```
注册页面级Action。

**参数说明：**
- `action: Action` - 要注册的Action对象
- `actionName: String` - 外部Action的名称（重载方法）

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### registerActionNames()
```kotlin
override fun registerActionNames(actionNames: List<String>): PageAgent
```
批量注册外部Action名称。

**参数说明：**
- `actionNames: List<String>` - 外部Action的名称列表

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### registerActions()
```kotlin
override fun registerActions(actionList: List<Action>): PageAgent
```
批量注册Action。

**参数说明：**
- `actionList: List<Action>` - Action列表

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### blockAction()
```kotlin
fun blockAction(actionName: String): PageAgent
```
排除指定的全局Action。

**参数说明：**
- `actionName: String` - 要排除的Action名称

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### blockActions()
```kotlin
fun blockActions(actionNames: List<String>): PageAgent
```
排除多个全局Action。

**参数说明：**
- `actionNames: List<String>` - 要排除的Action名称列表

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### blockAllActions()
```kotlin
fun blockAllActions(): PageAgent
```
排除所有全局Action，仅当前页面注册的Action生效。

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setOnTranscribeListener()
```kotlin
fun setOnTranscribeListener(listener: OnTranscribeListener): PageAgent
```
设置ASR和TTS监听器。

**参数说明：**
- `listener: OnTranscribeListener` - 转录结果监听器

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setOnAgentStatusChangedListener()
```kotlin
fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): PageAgent
```
设置Agent状态变化监听器。

**参数说明：**
- `listener: OnAgentStatusChangedListener` - 状态变化监听器

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

#### setOnActionStatusChangedListener()
```kotlin
fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): PageAgent
```
设置系统Action状态变化监听器。

**参数说明：**
- `listener: OnActionStatusChangedListener` - 系统Action状态变化监听器

**返回值：**
- `PageAgent` - 返回自身，支持链式调用

---

### AgentCore

Agent核心功能类，提供TTS播放、麦克风控制、大模型调用等静态方法。

**包路径：** `com.ainirobot.agent.AgentCore`

**属性：**

#### appId
```kotlin
val appId: String
```
当前应用的appId，只读属性。

#### debugMode
```kotlin
var debugMode: Boolean
```
是否开启调试模式，默认开启。

#### isMicrophoneMuted
```kotlin
var isMicrophoneMuted: Boolean
```
麦克风静音状态控制。

**说明：**
- `true` - 静音
- `false` - 取消静音

#### isEnableVoiceBar
```kotlin
var isEnableVoiceBar: Boolean
```
是否开启语音条，默认开启。

#### isEnableWakeFree
```kotlin
var isEnableWakeFree: Boolean
```
是否开启免唤醒功能，默认true。

#### isDisablePlan
```kotlin
var isDisablePlan: Boolean
```
是否禁用大模型规划，禁用后不会再进行大模型规划，默认为false。

**方法：**

#### ttsSync()
```kotlin
suspend fun ttsSync(text: String, timeoutMillis: Long = 180000): TaskResult<String>
```
TTS同步播放接口，需在协程中调用。

**参数说明：**
- `text: String` - 要播放的文本
- `timeoutMillis: Long` - 超时时间，单位毫秒，默认180秒

**返回值：**
- `TaskResult<String>` - 任务执行结果，status=1表示成功，status=2表示失败

#### tts()
```kotlin
fun tts(text: String, timeoutMillis: Long = 180000, callback: TTSCallback? = null)
```
TTS异步播放接口。

**参数说明：**
- `text: String` - 要播放的文本
- `timeoutMillis: Long` - 超时时间，单位毫秒，默认180秒
- `callback: TTSCallback?` - 回调，可为空

#### stopTTS()
```kotlin
fun stopTTS()
```
强制打断TTS播放。

#### llmSync()
```kotlin
suspend fun llmSync(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true
): TaskResult<LLMResponse>
```
大模型同步调用接口，需在协程中调用。

**参数说明：**
- `messages: List<LLMMessage>` - 大模型chat message列表
- `config: LLMConfig` - 大模型配置
- `timeoutMillis: Long` - 超时时间，单位毫秒，默认180秒
- `isStreaming: Boolean` - 是否流式输出，默认true

**返回值：**
- `TaskResult<LLMResponse>` - 任务执行结果，status=1表示成功，status=2表示失败

#### llm()
```kotlin
fun llm(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true,
    callback: LLMCallback? = null
)
```
大模型异步调用接口。

**参数说明：**
- `messages: List<LLMMessage>` - 大模型chat message列表
- `config: LLMConfig` - 大模型配置
- `timeoutMillis: Long` - 超时时间，单位毫秒，默认180秒
- `isStreaming: Boolean` - 是否流式输出，默认true
- `callback: LLMCallback?` - 回调，可为空

#### query()
```kotlin
fun query(text: String)
```
通过文本形式的用户问题触发大模型规划Action。

**参数说明：**
- `text: String` - 用户问题的文本，如："今天天气怎么样？"

#### uploadInterfaceInfo()
```kotlin
fun uploadInterfaceInfo(interfaceInfo: String)
```
上传页面信息，方便大模型理解当前页面的内容。

**参数说明：**
- `interfaceInfo: String` - 页面信息描述，最好带有页面组件的层次结构，但内容不宜过长

#### clearContext()
```kotlin
fun clearContext()
```
清空大模型对话上下文记录。

#### jumpToXiaobao()
```kotlin
fun jumpToXiaobao(context: Context)
```
跳转到小豹应用。

**参数说明：**
- `context: Context` - 上下文，用于启动Activity

---

### Action

Action定义类，封装Action的名称、描述、参数和执行器。

**包路径：** `com.ainirobot.agent.action.Action`

**构造函数：**

**完整构造函数：**
```kotlin
Action(
    name: String,
    appId: String,
    displayName: String,
    desc: String,
    parameters: List<Parameter>?,
    executor: ActionExecutor?
)
```

**内部Action构造函数：**
```kotlin
Action(
    name: String,
    displayName: String,
    desc: String,
    parameters: List<Parameter>?,
    executor: ActionExecutor?
)
```

**外部Action构造函数（仅name）：**
```kotlin
Action(name: String)
```

**参数说明：**
- `name: String` - action全名，建议格式：com.company.action.ACTION_NAME
- `appId: String` - 当前应用的appId（完整构造函数）
- `displayName: String` - 显示名称，可能被用于显示到UI界面上
- `desc: String` - action描述，用以让大模型理解应该在什么时间调用此action
- `parameters: List<Parameter>?` - 期望action在被规划出时携带的参数描述，可为空
- `executor: ActionExecutor?` - action对应的执行器，可为空

**说明：**
- 内部Action构造函数会自动使用当前应用的appId
- 外部Action构造函数主要用于注册外部已存在的Action，如系统Action或其他应用的静态注册Action

**属性：**

#### sid
```kotlin
var sid: String
```
规划的action的Id，用于标识action的唯一性，同一个action每次规划都会返回不同的actionId。

#### userQuery
```kotlin
var userQuery: String
```
触发规划的用户问题。

**方法：**

#### notify()
```kotlin
fun notify(
    result: ActionResult = ActionResult(ActionStatus.SUCCEEDED),
    isTriggerFollowUp: Boolean = false
)
```
Action执行完成后需要同步执行结果。

**参数说明：**
- `result: ActionResult` - Action的执行结果，默认为成功
- `isTriggerFollowUp: Boolean` - 在Action执行完成后主动引导用户进行下一步操作，默认关闭

---

### ActionExecutor

Action执行器接口，定义Action的执行逻辑回调方法。

**包路径：** `com.ainirobot.agent.action.ActionExecutor`

**方法：**

#### onExecute()
```kotlin
fun onExecute(action: Action, params: Bundle?): Boolean
```
Action执行回调方法。

**参数说明：**
- `action: Action` - 要执行的Action对象
- `params: Bundle?` - Action执行参数，可为空

**返回值：**
- `Boolean` - true表示已处理完成，false表示不需要自行处理

**重要提示：**
- 此方法不能执行耗时操作
- 耗时操作应放到协程或线程中执行
- 执行完成后必须调用action.notify()方法

---

### Actions

系统内置Action常量类，提供系统预定义的Action名称常量。

**包路径：** `com.ainirobot.agent.action.Actions`

**系统处理的Action：**

#### SET_VOLUME
```kotlin
const val SET_VOLUME = "orion.agent.action.SET_VOLUME"
```
调整系统音量。

#### SAY
```kotlin
const val SAY = "orion.agent.action.SAY"
```
机器人兜底对话。

#### CANCEL
```kotlin
const val CANCEL = "orion.agent.action.CANCEL"
```
取消，默认处理为模拟点击Back键。

#### BACK
```kotlin
const val BACK = "orion.agent.action.BACK"
```
返回，默认处理为模拟点击Back键。

#### EXIT
```kotlin
const val EXIT = "orion.agent.action.EXIT"
```
退出，默认处理为模拟点击Back键。

#### KNOWLEDGE_QA
```kotlin
const val KNOWLEDGE_QA = "orion.agent.action.KNOWLEDGE_QA"
```
知识库问答。

#### GENERATE_MESSAGE
```kotlin
const val GENERATE_MESSAGE = "orion.agent.action.GENERATE_MESSAGE"
```
对用户说一句欢迎或者欢送语。

#### ADJUST_SPEED
```kotlin
const val ADJUST_SPEED = "orion.agent.action.ADJUST_SPEED"
```
调整机器人速度。

**需用户处理的Action：**

#### CONFIRM
```kotlin
const val CONFIRM = "orion.agent.action.CONFIRM"
```
确定，需要用户自行处理。

#### CLICK
```kotlin
const val CLICK = "orion.agent.action.CLICK"
```
点击，需要用户自行处理。

---

### Parameter

Action参数定义类，描述Action所需的参数信息。

**包路径：** `com.ainirobot.agent.base.Parameter`

**构造函数：**
```kotlin
Parameter(
    name: String,
    type: ParameterType,
    desc: String,
    required: Boolean,
    enumValues: List<String>? = null
)
```

**参数说明：**
- `name: String` - 参数名，建议使用英文，多个单词用下划线连接
- `type: ParameterType` - 参数类型
- `desc: String` - 参数描述，要能精确反应此参数的定义
- `required: Boolean` - 是否是必要参数
- `enumValues: List<String>?` - 当type为ENUM时，需要传此参数，作为枚举值选择的列表

---

### ParameterType

参数类型枚举，定义Action参数支持的数据类型。

**包路径：** `com.ainirobot.agent.base.ParameterType`

**枚举值：**

#### STRING
字符串类型。

#### INT
整数类型。

#### FLOAT
浮点数类型。

#### BOOLEAN
布尔值类型。

#### ENUM
枚举类型，需要配合Parameter的enumValues使用。

#### NUMBER_ARRAY
数字数组类型。

#### STRING_ARRAY
字符串数组类型。

---

## 基础类

### Agent

Agent基础类，AppAgent和PageAgent的父类，定义了Agent的通用方法和属性。

**包路径：** `com.ainirobot.agent.Agent`

**通用方法：**

#### setPersona()
```kotlin
open fun setPersona(persona: String): Agent
```
设置此Agent的角色人设。

**参数说明：**
- `persona: String` - 人设描述，如："你叫小豹，是一个聊天机器人"

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### setStyle()
```kotlin
open fun setStyle(style: String): Agent
```
设置此Agent的对话风格。

**参数说明：**
- `style: String` - 对话风格，如：professional, friendly, humorous

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### setObjective()
```kotlin
open fun setObjective(objective: String): Agent
```
设置此Agent的规划目标。

**参数说明：**
- `objective: String` - 规划目标，要清晰明确，以便于大模型理解

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### setOnAgentStatusChangedListener()
```kotlin
open fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): Agent
```
设置Agent状态变化监听器。

**参数说明：**
- `listener: OnAgentStatusChangedListener` - 状态变化监听器

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### setOnTranscribeListener()
```kotlin
open fun setOnTranscribeListener(listener: OnTranscribeListener): Agent
```
设置TTS和ASR识别结果监听器。

**参数说明：**
- `listener: OnTranscribeListener` - 转录结果监听器

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### setOnActionStatusChangedListener()
```kotlin
open fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): Agent
```
设置系统Action状态变化监听器。

**参数说明：**
- `listener: OnActionStatusChangedListener` - 系统Action状态变化监听器

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### removeAction()
```kotlin
fun removeAction(name: String): Action?
```
删除Action。

**参数说明：**
- `name: String` - Action名称

**返回值：**
- `Action?` - 被删除的Action，如果不存在则返回null

**注意：** 如果是在应用或页面初始化后删除Action，可能需要重新初始化Agent。

#### getAction()
```kotlin
fun getAction(name: String): Action?
```
获取Action。

**参数说明：**
- `name: String` - Action全名

**返回值：**
- `Action?` - 对应的Action，如果不存在则返回null

#### registerActionNames()
```kotlin
open fun registerActionNames(actionNames: List<String>): Agent
```
批量注册外部Action名称。

**参数说明：**
- `actionNames: List<String>` - 外部Action的名称列表

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### registerAction()
```kotlin
open fun registerAction(action: Action): Agent
open fun registerAction(actionName: String): Agent
```
注册Action。

**参数说明：**
- `action: Action` - 要注册的Action对象
- `actionName: String` - 外部Action的名称（重载方法）

**返回值：**
- `Agent` - 返回自身，支持链式调用

#### registerActions()
```kotlin
open fun registerActions(actionList: List<Action>): Agent
```
批量注册Action。

**参数说明：**
- `actionList: List<Action>` - Action列表

**返回值：**
- `Agent` - 返回自身，支持链式调用

---

## 监听器接口

### OnTranscribeListener

ASR和TTS结果监听接口，用于获取语音识别和语音合成的结果。

**包路径：** `com.ainirobot.agent.OnTranscribeListener`

**方法：**

#### onASRResult()
```kotlin
fun onASRResult(transcription: Transcription): Boolean
```
ASR识别结果回调。

**参数说明：**
- `transcription: Transcription` - 转录结果对象

**返回值：**
- `Boolean` - true表示消费了此次结果，系统将不再显示字幕；false表示不影响后续处理

#### onTTSResult()
```kotlin
fun onTTSResult(transcription: Transcription): Boolean
```
TTS播放结果回调。

**参数说明：**
- `transcription: Transcription` - 转录结果对象

**返回值：**
- `Boolean` - true表示消费了此次结果，系统将不再显示字幕；false表示不影响后续处理

---

### OnAgentStatusChangedListener

Agent状态变化监听接口，用于监听Agent的运行状态。

**包路径：** `com.ainirobot.agent.OnAgentStatusChangedListener`

**方法：**

#### onStatusChanged()
```kotlin
fun onStatusChanged(status: String, message: String?): Boolean
```
Agent状态变化回调。

**参数说明：**
- `status: String` - 状态值，包含：listening（正在听）、thinking（思考中）、processing（处理中）、reset_status（状态复位）
- `message: String?` - 状态消息，当status是processing时可能有值，如："正在选择工具..."、"正在获取天气..."等

**返回值：**
- `Boolean` - true表示不想把状态显示到默认语音条上；false表示保留系统显示状态UI

---

### OnActionStatusChangedListener

系统Action状态变化监听接口，用于监听系统内置Action的执行状态变化。

**包路径：** `com.ainirobot.agent.OnActionStatusChangedListener`

**方法：**

#### onStatusChanged()
```kotlin
fun onStatusChanged(actionName: String?, status: String?, message: String?): Boolean
```
系统Action状态变化回调。

**参数说明：**
- `actionName: String?` - 系统Action名称，可为空
- `status: String?` - Action执行状态，包含：succeeded(成功)、failed(失败)、timeout(超时)、interrupted(中断)、recalled(撤回)、unsupported(不支持)
- `message: String?` - 状态相关的消息信息，可为空

**返回值：**
- `Boolean` - true表示消费此次状态变化事件；false表示不消费，继续传递给其他监听器

**重要说明：**
- 仅监听系统内置Action的状态变化
- 不包括二次开发者自定义的Action
- 回调在子线程中执行
- 依赖SDK v0.3.5以后版本
- 事件传递：PageAgent返回false时AppAgent也能收到回调

---

### TTSCallback

TTS播放回调接口，继承自ITaskCallback<String>。

**包路径：** `com.ainirobot.agent.TTSCallback`

**接口定义：**
```kotlin
interface TTSCallback : ITaskCallback<String>
```

### LLMCallback

大模型调用回调接口，继承自ITaskCallback<LLMResponse>。

**包路径：** `com.ainirobot.agent.LLMCallback`

**接口定义：**
```kotlin
interface LLMCallback : ITaskCallback<LLMResponse>
```

### ITaskCallback

任务执行回调接口，是一个泛型密封接口。

**包路径：** `com.ainirobot.agent.ITaskCallback`

**接口定义：**
```kotlin
sealed interface ITaskCallback<T> {
    fun onTaskEnd(status: Int, result: T?)
}
```

**方法说明：**

#### onTaskEnd()
```kotlin
fun onTaskEnd(status: Int, result: T?)
```
任务执行完成回调。

**参数说明：**
- `status: Int` - 执行状态，1表示成功，2表示失败
- `result: T?` - 执行结果，可为空

---

## 数据类

### Transcription

语音转录结果类，包含ASR识别和TTS播放的结果信息。

**包路径：** `com.ainirobot.agent.base.Transcription`

**构造函数：**
```kotlin
Transcription(
    sid: String,
    text: String,
    speaker: String,
    final: Boolean,
    error: String,
    extra: Bundle? = null
)
```

**属性：**

#### sid
```kotlin
val sid: String
```
会话ID。

#### text
```kotlin
val text: String
```
文本内容。

#### speaker
```kotlin
val speaker: String
```
说话者标识。

#### final
```kotlin
val final: Boolean
```
判断是流式结果还是最终结果，true为最终结果，false为中间结果。

#### error
```kotlin
val error: String
```
错误信息。

#### extra
```kotlin
val extra: Bundle?
```
额外信息，可为空。

#### isUserSpeaking
```kotlin
val isUserSpeaking: Boolean
```
判断是ASR还是TTS内容，true为ASR结果（speaker == "human_user"），false为TTS结果。

---

### LLMMessage

大模型消息类，封装与大模型交互的消息内容。

**包路径：** `com.ainirobot.agent.base.llm.LLMMessage`

**构造函数：**
```kotlin
LLMMessage(role: Role, content: String)
```

**参数说明：**
- `role: Role` - 消息角色
- `content: String` - 消息内容

**属性：**

#### role
```kotlin
val role: Role
```
消息角色。

#### content
```kotlin
val content: String
```
消息内容。

---

### LLMConfig

大模型配置类，用于配置大模型的参数和设置。

**包路径：** `com.ainirobot.agent.base.llm.LLMConfig`

**构造函数：**
```kotlin
LLMConfig(
    temperature: Float = 1.0f,
    maxTokens: Int? = null,
    timeout: Int = 6,
    fileSearch: Boolean = false,
    businessInfo: String? = null
)
```

**属性：**

#### temperature
```kotlin
val temperature: Float
```
温度参数，控制生成文本的随机性，默认1.0f。

#### maxTokens
```kotlin
val maxTokens: Int?
```
最大token数量，可为空。

#### timeout
```kotlin
val timeout: Int
```
超时时间（秒），默认6秒。

#### fileSearch
```kotlin
val fileSearch: Boolean
```
是否启用文件搜索，默认false。

#### businessInfo
```kotlin
val businessInfo: String?
```
业务信息，可为空。

---

### LLMResponse

大模型响应结果类，包含大模型调用的完整响应信息。

**包路径：** `com.ainirobot.agent.assit.LLMResponse`

**属性：**

#### tokenCost
```kotlin
val tokenCost: TokenCost
```
Token消耗统计。

#### elapsedTime
```kotlin
val elapsedTime: Float
```
请求耗时（秒）。

#### message
```kotlin
val message: LLMMessage
```
返回的消息内容。

#### status
```kotlin
val status: String
```
执行状态，"succeeded"表示成功，"failed"表示失败。

#### error
```kotlin
val error: String
```
错误信息，当status为"failed"时包含具体错误描述。

---

### TokenCost

Token消耗统计类，用于记录大模型调用的Token使用情况。

**包路径：** `com.ainirobot.agent.assit.TokenCost`

**构造函数：**
```kotlin
TokenCost(
    promptTokens: Int,
    completionTokens: Int,
    totalTokens: Int
)
```

**属性：**

#### promptTokens
```kotlin
val promptTokens: Int
```
输入提示的Token数量。

#### completionTokens
```kotlin
val completionTokens: Int
```
完成回复的Token数量。

#### totalTokens
```kotlin
val totalTokens: Int
```
总Token数量。

---

### ActionResult

Action执行结果类，封装Action执行的状态和结果信息。

**包路径：** `com.ainirobot.agent.base.ActionResult`

**构造函数：**
```kotlin
ActionResult(
    status: ActionStatus,
    result: Bundle? = null,
    extra: Bundle? = null,
    sid: String = "",
    appId: String = ""
)
```

**参数说明：**
- `status: ActionStatus` - Action执行状态
- `result: Bundle?` - 执行结果数据，可为空
- `extra: Bundle?` - 额外信息，可为空
- `sid: String` - 会话ID，默认为空字符串
- `appId: String` - 应用ID，默认为空字符串

**属性：**

#### status
```kotlin
val status: ActionStatus
```
Action执行状态。

#### result
```kotlin
val result: Bundle?
```
执行结果数据。

#### extra
```kotlin
val extra: Bundle?
```
额外信息。

#### sid
```kotlin
var sid: String
```
会话ID。

#### appId
```kotlin
var appId: String
```
应用ID。

---

### TaskResult

任务执行结果类，泛型类。

**包路径：** `com.ainirobot.agent.TaskResult`

**构造函数：**
```kotlin
TaskResult(status: Int, result: T? = null)
```

**参数说明：**
- `status: Int` - 执行状态，1表示成功，2表示失败
- `result: T?` - 执行结果，可为空

**属性：**

#### status
```kotlin
val status: Int
```
执行状态。

#### result
```kotlin
val result: T?
```
执行结果。

#### isSuccess
```kotlin
val isSuccess: Boolean
```
是否执行成功，当status=1时返回true。

---

## 枚举类

### Role

消息角色枚举，用于大模型消息的角色标识。

**包路径：** `com.ainirobot.agent.base.llm.Role`

**枚举值：**

#### USER
用户角色。

#### ASSISTANT
助手角色。

#### SYSTEM
系统角色。

---

### ActionStatus

Action执行状态枚举。

**包路径：** `com.ainirobot.agent.base.ActionStatus`

**枚举值：**

#### SUCCEEDED
执行成功。

#### FAILED
执行失败。

#### INTERRUPTED
执行被打断。

#### RECALLED
重复规划导致当前action被打断。

---

## 注解类

### AgentAction

Action注解，用于标记成员方法是一个Action。

**包路径：** `com.ainirobot.agent.annotations.AgentAction`

**目标：** `AnnotationTarget.FUNCTION`

**属性：**

#### name
```kotlin
val name: String
```
Action的名称。

#### desc
```kotlin
val desc: String
```
Action的描述。

#### displayName
```kotlin
val displayName: String
```
Action的显示名称。

**使用示例：**
```kotlin
@AgentAction(
    name = "com.agent.demo.SHOW_SMILE_FACE",
    displayName = "笑",
    desc = "响应用户的开心、满意或正面情绪"
)
private fun showSmileFace(action: Action, @ActionParameter(...) sentence: String): Boolean {
    // 实现逻辑
    return true
}
```

---

### ActionParameter

参数注解，用于标记Action方法的参数。

**包路径：** `com.ainirobot.agent.annotations.ActionParameter`

**目标：** `AnnotationTarget.VALUE_PARAMETER`

**属性：**

#### name
```kotlin
val name: String
```
参数名。

#### desc
```kotlin
val desc: String
```
参数描述。

#### required
```kotlin
val required: Boolean = true
```
是否是必要参数，默认为true。

#### enumValues
```kotlin
val enumValues: Array<String> = []
```
限制参数的value只能从指定的值中选择。

**使用示例：**
```kotlin
@ActionParameter(
    name = "sentence",
    desc = "回复给用户的话",
    required = true
)
sentence: String
```

---

## 工具类

### AOCoroutineScope

Agent协程作用域，用于在Agent中执行协程操作。

**包路径：** `com.ainirobot.agent.coroutine.AOCoroutineScope`

**方法：**

#### launch()
```kotlin
fun launch(block: suspend CoroutineScope.() -> Unit): Job
```
启动协程。

**参数说明：**
- `block: suspend CoroutineScope.() -> Unit` - 协程代码块

**返回值：**
- `Job` - 协程任务对象

#### cancelAll()
```kotlin
fun cancelAll()
```
取消所有协程任务并关闭线程池。

**使用示例：**
```kotlin
AOCoroutineScope.launch {
    // 在协程中执行耗时操作
    AgentCore.ttsSync("Hello")
    // 完成后通知
    action.notify()
}
```

---

## 重要提示

### Action执行注意事项

1. **onExecute方法不能执行耗时操作**
2. **耗时操作必须放到协程或线程中执行**
3. **执行完成后必须调用action.notify()方法**
4. **onExecute方法默认在子线程中执行**

### 生命周期管理

1. **一个App中只能有一个AppAgent实例**
2. **每个页面只能有一个PageAgent实例**
3. **App级Action在应用前台期间生效**
4. **Page级Action在页面可见期间生效**

### 注册方式

1. **动态注册**：在代码中注册，仅当前应用内部使用
2. **静态注册**：在actionRegistry.json中配置，可被外部调用

### 参数命名规范

1. **参数名使用英文，多个单词用下划线连接**
2. **避免与Action或Parameter对象的属性名相同**
3. **提供清晰的参数描述，帮助AI理解** 