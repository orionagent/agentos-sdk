# AgentOS SDK v0.4.4 API description

> **Version Information**
> Current SDK Version: 0.4.4
> Dependency: `implementation 'com.orionstar.agent:sdk:0.4.4-SNAPSHOT'`

## 📋 Table of Contents

### Core Classes
- [AppAgent](#appagent) - App-level Agent，descriptionActiondescription
- [PageAgent](#pageagent) - Page-level Agent，descriptionActiondescription
- [AgentCore](#agentcore) - Agent core utility class，descriptionMethods
- [Action](#action) - Action definition class，descriptionProperties
- [ActionExecutor](#actionexecutor) - Action executor interface
- [Actions](#actions) - Built-in Action constants
- [Parameter](#parameter) - Action parameter class
- [ParameterType](#parametertype) - parameter type enum

### Base Classes
- [Agent](#agent) - Agent base class，AppAgentdescriptionPageAgentdescription

### Listener Interfaces
- [OnTranscribeListener](#ontranscribelistener) - ASRdescriptionTTSdescriptionListen to description
- [OnAgentStatusChangedListener](#onagentstatus-changedlistener) - Agentstatus change listener interface
- [OnActionStatusChangedListener](#onactionstatuschangedlistener) - descriptionActionstatus change listener interface
- [TTSCallback](#ttscallback) - TTSdescriptioncallbackdescription
- [LLMCallback](#llmcallback) - LLMdescriptioncallbackdescription
- [ITaskCallback](#itaskcallback) - task callback interface

### Data Classes
- [Transcription](#transcription) - speech transcription result class
- [LLMMessage](#llmmessage) - LLM message class
- [LLMConfig](#llmconfig) - LLM configuration class
- [LLMResponse](#llmresponse) - LLM response class
- [TokenCost](#tokencost) - token usage statistics class
- [ActionResult](#actionresult) - Action execution result class
- [TaskResult](#taskresult) - task execution result class

### Enum Classes
- [Role](#role) - message role enum
- [ActionStatus](#actionstatus) - Action status enum

### Annotation Classes
- [AgentAction](#agentaction) - Action annotation for marking Action methods
- [ActionParameter](#actionparameter) - parameter annotation for marking Action parameters

### Utility Classes
- [AOCoroutineScope](#aocoroutinescope) - Agent coroutine scope

---

## Core Classes

### AppAgent

App-level Agent，descriptionAgentdescriptionAction。Only one AppAgent instance can exist in an app。

**Package：** `com.ainirobot.agent.AppAgent`

**Constructors:**
```kotlin
AppAgent(context: Application)
```

**Parameters:**
- `context: Application` - Androidapplication instance

**Core Methods:**

#### onCreate()
```kotlin
abstract fun onCreate()
```
AppAgentdescriptioncallback，descriptionAgentdescriptionPropertiesdescriptionDynamic registrationAction。

#### onExecuteAction()
```kotlin
abstract fun onExecuteAction(action: Action, params: Bundle?): Boolean
```
descriptionStatic registrationdescriptionActionexecute。descriptionactionRegistry.jsondescriptionStatic registrationdescriptionActiondescriptionMethods。

**Parameters:**
- `action: Action` - Action object to execute
- `params: Bundle?` - Action execution parameters, nullable

**Return Value:**
- `Boolean` - truedescription，falsedescription

#### setPersona()
```kotlin
override fun setPersona(persona: String): AppAgent
```
Set AIdescriptionpersona。

**Parameters:**
- `persona: String` - role persona description

**Return Value:**
- `AppAgent` - description，description

#### setStyle()
```kotlin
override fun setStyle(style: String): AppAgent
```
Set conversation style。

**Parameters:**
- `style: String` - conversation style description

**Return Value:**
- `AppAgent` - description，description

#### setObjective()
```kotlin
override fun setObjective(objective: String): AppAgent
```
Set description。

**Parameters:**
- `objective: String` - task objective description

**Return Value:**
- `AppAgent` - description，description

#### setOnAgentStatusChangedListener()
```kotlin
override fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): AppAgent
```
Set Agentstatus change listener。

**Parameters:**
- `listener: OnAgentStatusChangedListener` - status change listener

**Return Value:**
- `AppAgent` - description，description

#### setOnTranscribeListener()
```kotlin
override fun setOnTranscribeListener(listener: OnTranscribeListener): AppAgent
```
Set ASRdescriptionTTSListen to description。

**Parameters:**
- `listener: OnTranscribeListener` - transcription listener

**Return Value:**
- `AppAgent` - description，description

#### setOnActionStatusChangedListener()
```kotlin
override fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): AppAgent
```
Set descriptionActionstatus change listener。

**Parameters:**
- `listener: OnActionStatusChangedListener` - descriptionActionstatus change listener

**Return Value:**
- `AppAgent` - description，description

#### registerAction()
```kotlin
override fun registerAction(action: Action): AppAgent
override fun registerAction(actionName: String): AppAgent
```
Dynamic registrationAction。

**Parameters:**
- `action: Action` - Action object to register
- `actionName: String` - external Action name（descriptionMethods）

**Return Value:**
- `AppAgent` - description，description

#### registerActionNames()
```kotlin
override fun registerActionNames(actionNames: List<String>): AppAgent
```
descriptionRegister descriptionActiondescription。

**Parameters:**
- `actionNames: List<String>` - external Action namedescription

**Return Value:**
- `AppAgent` - description，description

#### registerActions()
```kotlin
override fun registerActions(actionList: List<Action>): AppAgent
```
descriptionRegister Action。

**Parameters:**
- `actionList: List<Action>` - Actiondescription

**Return Value:**
- `AppAgent` - description，description

---

### PageAgent

Page-level Agent，descriptionAgentdescriptionAction。Each page can only have one PageAgent instance。

**Package：** `com.ainirobot.agent.PageAgent`

**Constructors:**
```kotlin
PageAgent(pageId: String)
PageAgent(activity: Activity)
PageAgent(fragment: Fragment)
```

**Parameters:**
- `pageId: String` - description（descriptionConstructor）
- `activity: Activity` - Android Activitydescription
- `fragment: Fragment` - Android Fragmentdescription

**Core Methods:**

#### setPersona()
```kotlin
override fun setPersona(persona: String): PageAgent
```
Set descriptionAgentdescriptionpersona。

**Parameters:**
- `persona: String` - personadescription

**Return Value:**
- `PageAgent` - description，description

#### setStyle()
```kotlin
override fun setStyle(style: String): PageAgent
```
Set descriptionAgentdescriptionconversation style。

**Parameters:**
- `style: String` - conversation style

**Return Value:**
- `PageAgent` - description，description

#### setObjective()
```kotlin
override fun setObjective(objective: String): PageAgent
```
Set descriptionAgentdescriptionplanning objective。

**Parameters:**
- `objective: String` - planning objective

**Return Value:**
- `PageAgent` - description，description

#### setOnAgentStatusChangedListener()
```kotlin
override fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): PageAgent
```
Set Agentstatus change listener。

**Parameters:**
- `listener: OnAgentStatusChangedListener` - status change listener

**Return Value:**
- `PageAgent` - description，description

#### setOnTranscribeListener()
```kotlin
override fun setOnTranscribeListener(listener: OnTranscribeListener): PageAgent
```
Set ASRdescriptionTTSListen to description。

**Parameters:**
- `listener: OnTranscribeListener` - transcription listener

**Return Value:**
- `PageAgent` - description，description

#### registerAction()
```kotlin
override fun registerAction(action: Action): PageAgent
override fun registerAction(actionName: String): PageAgent
```
Register descriptionAction。

**Parameters:**
- `action: Action` - Action object to register
- `actionName: String` - external Action name（descriptionMethods）

**Return Value:**
- `PageAgent` - description，description

#### registerActionNames()
```kotlin
override fun registerActionNames(actionNames: List<String>): PageAgent
```
descriptionRegister descriptionActiondescription。

**Parameters:**
- `actionNames: List<String>` - external Action namedescription

**Return Value:**
- `PageAgent` - description，description

#### registerActions()
```kotlin
override fun registerActions(actionList: List<Action>): PageAgent
```
descriptionRegister Action。

**Parameters:**
- `actionList: List<Action>` - Actiondescription

**Return Value:**
- `PageAgent` - description，description

#### blockAction()
```kotlin
fun blockAction(actionName: String): PageAgent
```
descriptionAction。

**Parameters:**
- `actionName: String` - descriptionActiondescription

**Return Value:**
- `PageAgent` - description，description

#### blockActions()
```kotlin
fun blockActions(actionNames: List<String>): PageAgent
```
descriptionAction。

**Parameters:**
- `actionNames: List<String>` - descriptionActionname list

**Return Value:**
- `PageAgent` - description，description

#### blockAllActions()
```kotlin
fun blockAllActions(): PageAgent
```
descriptionAction，descriptionRegister descriptionActiondescription。

**Return Value:**
- `PageAgent` - description，description

#### setOnTranscribeListener()
```kotlin
fun setOnTranscribeListener(listener: OnTranscribeListener): PageAgent
```
Set ASRdescriptionTTSListen to description。

**Parameters:**
- `listener: OnTranscribeListener` - transcription listener

**Return Value:**
- `PageAgent` - description，description

#### setOnAgentStatusChangedListener()
```kotlin
fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): PageAgent
```
Set Agentstatus change listener。

**Parameters:**
- `listener: OnAgentStatusChangedListener` - status change listener

**Return Value:**
- `PageAgent` - description，description

#### setOnActionStatusChangedListener()
```kotlin
fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): PageAgent
```
Set descriptionActionstatus change listener。

**Parameters:**
- `listener: OnActionStatusChangedListener` - descriptionActionstatus change listener

**Return Value:**
- `PageAgent` - description，description

---

### AgentCore

Agent core utility class，descriptionTTSdescription、description、LLMdescriptionMethods。

**Package：** `com.ainirobot.agent.AgentCore`

**Properties:**

#### appId
```kotlin
val appId: String
```
descriptionappId，descriptionProperties。

#### debugMode
```kotlin
var debugMode: Boolean
```
description，description。

#### isMicrophoneMuted
```kotlin
var isMicrophoneMuted: Boolean
```
description。

**Description：**
- `true` - description
- `false` - description

#### isEnableVoiceBar
```kotlin
var isEnableVoiceBar: Boolean
```
description，description。

#### isEnableWakeFree
```kotlin
var isEnableWakeFree: Boolean
```
description，descriptiontrue。

#### isDisablePlan
```kotlin
var isDisablePlan: Boolean
```
descriptionLLMdescription，descriptionLLMdescription，descriptionfalse。

**Methods:**

#### ttsSync()
```kotlin
suspend fun ttsSync(text: String, timeoutMillis: Long = 180000): TaskResult<String>
```
TTSdescription，description。

**Parameters:**
- `text: String` - description
- `timeoutMillis: Long` - timeout in milliseconds，description180description

**Return Value:**
- `TaskResult<String>` - descriptionexecutedescription，status=1descriptionsuccess，status=2descriptionfailure

#### tts()
```kotlin
fun tts(text: String, timeoutMillis: Long = 180000, callback: TTSCallback? = null)
```
TTSdescription。

**Parameters:**
- `text: String` - description
- `timeoutMillis: Long` - timeout in milliseconds，description180description
- `callback: TTSCallback?` - callback, nullable

#### stopTTS()
```kotlin
fun stopTTS()
```
descriptionTTSdescription。

#### llmSync()
```kotlin
suspend fun llmSync(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true
): TaskResult<LLMResponse>
```
LLMdescription，description。

**Parameters:**
- `messages: List<LLMMessage>` - LLMchat messagedescription
- `config: LLMConfig` - LLMdescription
- `timeoutMillis: Long` - timeout in milliseconds，description180description
- `isStreaming: Boolean` - whether to stream output, default true

**Return Value:**
- `TaskResult<LLMResponse>` - descriptionexecutedescription，status=1descriptionsuccess，status=2descriptionfailure

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
LLMdescription。

**Parameters:**
- `messages: List<LLMMessage>` - LLMchat messagedescription
- `config: LLMConfig` - LLMdescription
- `timeoutMillis: Long` - timeout in milliseconds，description180description
- `isStreaming: Boolean` - whether to stream output, default true
- `callback: LLMCallback?` - callback, nullable

#### query()
```kotlin
fun query(text: String)
```
descriptionLLMdescriptionAction。

**Parameters:**
- `text: String` - description，description："description？"

#### uploadInterfaceInfo()
```kotlin
fun uploadInterfaceInfo(interfaceInfo: String)
```
description，descriptionLLMdescription。

**Parameters:**
- `interfaceInfo: String` - description，description，description

#### clearContext()
```kotlin
fun clearContext()
```
descriptionLLMdescriptioncontextdescription。

#### jumpToXiaobao()
```kotlin
fun jumpToXiaobao(context: Context)
```
description。

**Parameters:**
- `context: Context` - context，descriptionActivity

---

### Action

Action definition class，descriptionActiondescription、description、descriptionexecutedescription。

**Package：** `com.ainirobot.agent.action.Action`

**Constructors:**

**descriptionConstructor：**
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

**descriptionActionConstructor：**
```kotlin
Action(
    name: String,
    displayName: String,
    desc: String,
    parameters: List<Parameter>?,
    executor: ActionExecutor?
)
```

**descriptionActionConstructor（descriptionname）：**
```kotlin
Action(name: String)
```

**Parameters:**
- `name: String` - actiondescription，description：com.company.action.ACTION_NAME
- `appId: String` - descriptionappId（descriptionConstructor）
- `displayName: String` - description，descriptionUIdescription
- `desc: String` - actiondescription，descriptionLLMdescriptionaction
- `parameters: List<Parameter>?` - descriptionactiondescription，description
- `executor: ActionExecutor?` - actiondescriptionexecutedescription，description

**Description：**
- descriptionActionConstructordescriptionappId
- descriptionActionConstructordescriptionRegister descriptionAction，descriptionActiondescriptionStatic registrationAction

**Properties:**

#### sid
```kotlin
var sid: String
```
descriptionactiondescriptionId，descriptionactiondescription，descriptionactiondescriptionactionId。

#### userQuery
```kotlin
var userQuery: String
```
description。

**Methods:**

#### notify()
```kotlin
fun notify(
    result: ActionResult = ActionResult(ActionStatus.SUCCEEDED),
    isTriggerFollowUp: Boolean = false
)
```
Actionexecutedescriptionexecutedescription。

**Parameters:**
- `result: ActionResult` - Actiondescriptionexecutedescription，descriptionsuccess
- `isTriggerFollowUp: Boolean` - descriptionActionexecutedescription，description

---

### ActionExecutor

Action executor interface，descriptionActiondescriptionexecutedescriptioncallbackMethods。

**Package：** `com.ainirobot.agent.action.ActionExecutor`

**Methods:**

#### onExecute()
```kotlin
fun onExecute(action: Action, params: Bundle?): Boolean
```
ActionexecutecallbackMethods。

**Parameters:**
- `action: Action` - Action object to execute
- `params: Bundle?` - Action execution parameters, nullable

**Return Value:**
- `Boolean` - truedescription，falsedescription

**Important Notes：**
- descriptionMethodsdescriptionexecutedescription
- descriptionexecute
- executedescriptionaction.notify()Methods

---

### Actions

Built-in Action constants，descriptionActiondescription。

**Package：** `com.ainirobot.agent.action.Actions`

**Actions handled by the system：**

#### SET_VOLUME
```kotlin
const val SET_VOLUME = "orion.agent.action.SET_VOLUME"
```
description。

#### SAY
```kotlin
const val SAY = "orion.agent.action.SAY"
```
description。

#### CANCEL
```kotlin
const val CANCEL = "orion.agent.action.CANCEL"
```
description，descriptionBackdescription。

#### BACK
```kotlin
const val BACK = "orion.agent.action.BACK"
```
description，descriptionBackdescription。

#### EXIT
```kotlin
const val EXIT = "orion.agent.action.EXIT"
```
description，descriptionBackdescription。

#### KNOWLEDGE_QA
```kotlin
const val KNOWLEDGE_QA = "orion.agent.action.KNOWLEDGE_QA"
```
description。

#### GENERATE_MESSAGE
```kotlin
const val GENERATE_MESSAGE = "orion.agent.action.GENERATE_MESSAGE"
```
description。

#### ADJUST_SPEED
```kotlin
const val ADJUST_SPEED = "orion.agent.action.ADJUST_SPEED"
```
description。

**Actions that must be handled by the user：**

#### CONFIRM
```kotlin
const val CONFIRM = "orion.agent.action.CONFIRM"
```
description，description。

#### CLICK
```kotlin
const val CLICK = "orion.agent.action.CLICK"
```
description，description。

---

### Parameter

Action parameter class，descriptionActiondescription。

**Package：** `com.ainirobot.agent.base.Parameter`

**Constructors:**
```kotlin
Parameter(
    name: String,
    type: ParameterType,
    desc: String,
    required: Boolean,
    enumValues: List<String>? = null
)
```

**Parameters:**
- `name: String` - description，description，description
- `type: ParameterType` - description
- `desc: String` - description，description
- `required: Boolean` - description
- `enumValues: List<String>?` - descriptiontypedescriptionENUMdescription，description，descriptionEnum Valuesdescription

---

### ParameterType

parameter type enum，descriptionActiondescriptionData Classesdescription。

**Package：** `com.ainirobot.agent.base.ParameterType`

**Enum Values:**

#### STRING
description。

#### INT
description。

#### FLOAT
description。

#### BOOLEAN
description。

#### ENUM
Enum Classesdescription，descriptionParameterdescriptionenumValuesdescription。

#### NUMBER_ARRAY
description。

#### STRING_ARRAY
description。

---

## Base Classes

### Agent

Agent base class，AppAgentdescriptionPageAgentdescription，descriptionAgentdescriptionMethodsdescriptionProperties。

**Package：** `com.ainirobot.agent.Agent`

**descriptionMethods：**

#### setPersona()
```kotlin
open fun setPersona(persona: String): Agent
```
Set descriptionAgentdescriptionpersona。

**Parameters:**
- `persona: String` - personadescription，description："description，description"

**Return Value:**
- `Agent` - description，description

#### setStyle()
```kotlin
open fun setStyle(style: String): Agent
```
Set descriptionAgentdescriptionconversation style。

**Parameters:**
- `style: String` - conversation style，description：professional, friendly, humorous

**Return Value:**
- `Agent` - description，description

#### setObjective()
```kotlin
open fun setObjective(objective: String): Agent
```
Set descriptionAgentdescriptionplanning objective。

**Parameters:**
- `objective: String` - planning objective，description，descriptionLLMdescription

**Return Value:**
- `Agent` - description，description

#### setOnAgentStatusChangedListener()
```kotlin
open fun setOnAgentStatusChangedListener(listener: OnAgentStatusChangedListener): Agent
```
Set Agentstatus change listener。

**Parameters:**
- `listener: OnAgentStatusChangedListener` - status change listener

**Return Value:**
- `Agent` - description，description

#### setOnTranscribeListener()
```kotlin
open fun setOnTranscribeListener(listener: OnTranscribeListener): Agent
```
Set TTSdescriptionASRdescriptionListen to description。

**Parameters:**
- `listener: OnTranscribeListener` - transcription listener

**Return Value:**
- `Agent` - description，description

#### setOnActionStatusChangedListener()
```kotlin
open fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): Agent
```
Set descriptionActionstatus change listener。

**Parameters:**
- `listener: OnActionStatusChangedListener` - descriptionActionstatus change listener

**Return Value:**
- `Agent` - description，description

#### removeAction()
```kotlin
fun removeAction(name: String): Action?
```
descriptionAction。

**Parameters:**
- `name: String` - Actiondescription

**Return Value:**
- `Action?` - descriptionAction，descriptionnull

**Note：** descriptionAction，descriptionAgent。

#### getAction()
```kotlin
fun getAction(name: String): Action?
```
Get Action。

**Parameters:**
- `name: String` - Actiondescription

**Return Value:**
- `Action?` - descriptionAction，descriptionnull

#### registerActionNames()
```kotlin
open fun registerActionNames(actionNames: List<String>): Agent
```
descriptionRegister descriptionActiondescription。

**Parameters:**
- `actionNames: List<String>` - external Action namedescription

**Return Value:**
- `Agent` - description，description

#### registerAction()
```kotlin
open fun registerAction(action: Action): Agent
open fun registerAction(actionName: String): Agent
```
Register Action。

**Parameters:**
- `action: Action` - Action object to register
- `actionName: String` - external Action name（descriptionMethods）

**Return Value:**
- `Agent` - description，description

#### registerActions()
```kotlin
open fun registerActions(actionList: List<Action>): Agent
```
descriptionRegister Action。

**Parameters:**
- `actionList: List<Action>` - Actiondescription

**Return Value:**
- `Agent` - description，description

---

## Listener Interfaces

### OnTranscribeListener

ASRdescriptionTTSdescriptionListen to description，descriptionGet speech recognitiondescriptiontext-to-speechdescription。

**Package：** `com.ainirobot.agent.OnTranscribeListener`

**Methods:**

#### onASRResult()
```kotlin
fun onASRResult(transcription: Transcription): Boolean
```
ASRdescriptioncallback。

**Parameters:**
- `transcription: Transcription` - transcription result object

**Return Value:**
- `Boolean` - truedescription，description；falsedescription

#### onTTSResult()
```kotlin
fun onTTSResult(transcription: Transcription): Boolean
```
TTSdescriptioncallback。

**Parameters:**
- `transcription: Transcription` - transcription result object

**Return Value:**
- `Boolean` - truedescription，description；falsedescription

---

### OnAgentStatusChangedListener

Agentstatus change listener interface，descriptionListen to Agentdescription。

**Package：** `com.ainirobot.agent.OnAgentStatusChangedListener`

**Methods:**

#### onStatusChanged()
```kotlin
fun onStatusChanged(status: String, message: String?): Boolean
```
Agentdescriptioncallback。

**Parameters:**
- `status: String` - status value，description：listening（listening）、thinking（thinking）、processing（processing）、reset_status（reset status）
- `message: String?` - description，descriptionstatusdescriptionprocessingdescription，description："selecting tools..."、"descriptionGet description..."description

**Return Value:**
- `Boolean` - truedescription；falsedescriptionUI

---

### OnActionStatusChangedListener

descriptionActionstatus change listener interface，descriptionListen to descriptionActiondescriptionexecutedescription。

**Package：** `com.ainirobot.agent.OnActionStatusChangedListener`

**Methods:**

#### onStatusChanged()
```kotlin
fun onStatusChanged(actionName: String?, status: String?, message: String?): Boolean
```
descriptionActiondescriptioncallback。

**Parameters:**
- `actionName: String?` - descriptionActiondescription，description
- `status: String?` - Actionexecutedescription，description：succeeded(success)、failed(failure)、timeout(timeout)、interrupted(interrupted)、recalled(recalled)、unsupported(unsupported)
- `message: String?` - description，description

**Return Value:**
- `Boolean` - truedescription；falsedescription，descriptionListen to description

**descriptionDescription：**
- descriptionListen to descriptionActiondescription
- descriptionAction
- callbackdescriptionexecute
- DependencySDK v0.4.4description
- description：PageAgentdescriptionfalsedescriptionAppAgentdescriptioncallback

---

### TTSCallback

TTSdescriptioncallbackdescription，descriptionITaskCallback<String>。

**Package：** `com.ainirobot.agent.TTSCallback`

**Interface Definition:**
```kotlin
interface TTSCallback : ITaskCallback<String>
```

### LLMCallback

LLMdescriptioncallbackdescription，descriptionITaskCallback<LLMResponse>。

**Package：** `com.ainirobot.agent.LLMCallback`

**Interface Definition:**
```kotlin
interface LLMCallback : ITaskCallback<LLMResponse>
```

### ITaskCallback

task callback interface，description。

**Package：** `com.ainirobot.agent.ITaskCallback`

**Interface Definition:**
```kotlin
sealed interface ITaskCallback<T> {
    fun onTaskEnd(status: Int, result: T?)
}
```

**Method Description:**

#### onTaskEnd()
```kotlin
fun onTaskEnd(status: Int, result: T?)
```
descriptionexecutedescriptioncallback。

**Parameters:**
- `status: Int` - execution status, 1 = success, 2 = failure
- `result: T?` - execution result, nullable

---

## Data Classes

### Transcription

speech transcription result class，descriptionASRdescriptionTTSdescription。

**Package：** `com.ainirobot.agent.base.Transcription`

**Constructors:**
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

**Properties:**

#### sid
```kotlin
val sid: String
```
session ID。

#### text
```kotlin
val text: String
```
description。

#### speaker
```kotlin
val speaker: String
```
description。

#### final
```kotlin
val final: Boolean
```
description，truedescription，falsedescription。

#### error
```kotlin
val error: String
```
description。

#### extra
```kotlin
val extra: Bundle?
```
description，description。

#### isUserSpeaking
```kotlin
val isUserSpeaking: Boolean
```
descriptionASRdescriptionTTSdescription，truedescriptionASRdescription（speaker == "human_user"），falsedescriptionTTSdescription。

---

### LLMMessage

LLM message class，descriptionLLMdescriptionmessage content。

**Package：** `com.ainirobot.agent.base.llm.LLMMessage`

**Constructors:**
```kotlin
LLMMessage(role: Role, content: String)
```

**Parameters:**
- `role: Role` - message role
- `content: String` - message content

**Properties:**

#### role
```kotlin
val role: Role
```
message role。

#### content
```kotlin
val content: String
```
message content。

---

### LLMConfig

LLM configuration class，descriptionLLMdescriptionSet 。

**Package：** `com.ainirobot.agent.base.llm.LLMConfig`

**Constructors:**
```kotlin
LLMConfig(
    temperature: Float = 1.0f,
    maxTokens: Int? = null,
    timeout: Int = 6,
    fileSearch: Boolean = false,
    businessInfo: String? = null
)
```

**Properties:**

#### temperature
```kotlin
val temperature: Float
```
description，description，description1.0f。

#### maxTokens
```kotlin
val maxTokens: Int?
```
descriptiontokendescription，description。

#### timeout
```kotlin
val timeout: Int
```
timeoutdescription（description），description6description。

#### fileSearch
```kotlin
val fileSearch: Boolean
```
description，descriptionfalse。

#### businessInfo
```kotlin
val businessInfo: String?
```
description，description。

---

### LLMResponse

LLM response class，descriptionLLMdescription。

**Package：** `com.ainirobot.agent.assit.LLMResponse`

**Properties:**

#### tokenCost
```kotlin
val tokenCost: TokenCost
```
Tokendescription。

#### elapsedTime
```kotlin
val elapsedTime: Float
```
description（description）。

#### message
```kotlin
val message: LLMMessage
```
descriptionmessage content。

#### status
```kotlin
val status: String
```
executedescription，"succeeded"descriptionsuccess，"failed"descriptionfailure。

#### error
```kotlin
val error: String
```
description，descriptionstatusdescription"failed"description。

---

### TokenCost

token usage statistics class，descriptionLLMdescriptionTokendescription。

**Package：** `com.ainirobot.agent.assit.TokenCost`

**Constructors:**
```kotlin
TokenCost(
    promptTokens: Int,
    completionTokens: Int,
    totalTokens: Int
)
```

**Properties:**

#### promptTokens
```kotlin
val promptTokens: Int
```
descriptionTokendescription。

#### completionTokens
```kotlin
val completionTokens: Int
```
descriptionTokendescription。

#### totalTokens
```kotlin
val totalTokens: Int
```
descriptionTokendescription。

---

### ActionResult

Action execution result class，descriptionActionexecutedescription。

**Package：** `com.ainirobot.agent.base.ActionResult`

**Constructors:**
```kotlin
ActionResult(
    status: ActionStatus,
    result: Bundle? = null,
    extra: Bundle? = null,
    sid: String = "",
    appId: String = ""
)
```

**Parameters:**
- `status: ActionStatus` - Actionexecutedescription
- `result: Bundle?` - executedescription，description
- `extra: Bundle?` - description，description
- `sid: String` - session ID，description
- `appId: String` - descriptionID，description

**Properties:**

#### status
```kotlin
val status: ActionStatus
```
Actionexecutedescription。

#### result
```kotlin
val result: Bundle?
```
executedescription。

#### extra
```kotlin
val extra: Bundle?
```
description。

#### sid
```kotlin
var sid: String
```
session ID。

#### appId
```kotlin
var appId: String
```
descriptionID。

---

### TaskResult

task execution result class，description。

**Package：** `com.ainirobot.agent.TaskResult`

**Constructors:**
```kotlin
TaskResult(status: Int, result: T? = null)
```

**Parameters:**
- `status: Int` - execution status, 1 = success, 2 = failure
- `result: T?` - execution result, nullable

**Properties:**

#### status
```kotlin
val status: Int
```
executedescription。

#### result
```kotlin
val result: T?
```
executedescription。

#### isSuccess
```kotlin
val isSuccess: Boolean
```
descriptionexecutesuccess，descriptionstatus=1descriptiontrue。

---

## Enum Classes

### Role

message role enum，descriptionLLMdescription。

**Package：** `com.ainirobot.agent.base.llm.Role`

**Enum Values:**

#### USER
description。

#### ASSISTANT
description。

#### SYSTEM
description。

---

### ActionStatus

Action status enum。

**Package：** `com.ainirobot.agent.base.ActionStatus`

**Enum Values:**

#### SUCCEEDED
executesuccess。

#### FAILED
executefailure。

#### INTERRUPTED
executedescription。

#### RECALLED
descriptionactiondescription。

---

## Annotation Classes

### AgentAction

Actiondescription，descriptionMethodsdescriptionAction。

**Package：** `com.ainirobot.agent.annotations.AgentAction`

**description：** `AnnotationTarget.FUNCTION`

**Properties:**

#### name
```kotlin
val name: String
```
Actiondescription。

#### desc
```kotlin
val desc: String
```
Actiondescription。

#### displayName
```kotlin
val displayName: String
```
Actiondescription。

**Usage Example:**
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

description，descriptionActionMethodsdescription。

**Package：** `com.ainirobot.agent.annotations.ActionParameter`

**description：** `AnnotationTarget.VALUE_PARAMETER`

**Properties:**

#### name
```kotlin
val name: String
```
description。

#### desc
```kotlin
val desc: String
```
description。

#### required
```kotlin
val required: Boolean = true
```
description，descriptiontrue。

#### enumValues
```kotlin
val enumValues: Array<String> = []
```
descriptionvaluedescription。

**Usage Example:**
```kotlin
@ActionParameter(
    name = "sentence",
    desc = "回复给用户的话",
    required = true
)
sentence: String
```

---

## Utility Classes

### AOCoroutineScope

Agent coroutine scope，descriptionAgentdescriptionexecutedescription。

**Package：** `com.ainirobot.agent.coroutine.AOCoroutineScope`

**Methods:**

#### launch()
```kotlin
fun launch(block: suspend CoroutineScope.() -> Unit): Job
```
description。

**Parameters:**
- `block: suspend CoroutineScope.() -> Unit` - description

**Return Value:**
- `Job` - description

#### cancelAll()
```kotlin
fun cancelAll()
```
description。

**Usage Example:**
```kotlin
AOCoroutineScope.launch {
    // 在协程中执行耗时操作
    AgentCore.ttsSync("Hello")
    // 完成后通知
    action.notify()
}
```

---

## Important Notes

### Action Execution Notes

1. **onExecuteMethodsdescriptionexecutedescription**
2. **Time-consuming operations must run in coroutines or background threads**
3. **executedescriptionaction.notify()Methods**
4. **onExecuteMethodsdescriptionexecute**

### Lifecycle Management

1. **Only one AppAgent instance can exist in an app**
2. **Each page can only have one PageAgent instance**
3. **AppdescriptionActiondescription**
4. **PagedescriptionActiondescription**

### Registration Methods

1. **Dynamic registration**：descriptionRegister ，description
2. **Static registration**：descriptionactionRegistry.jsondescription，description

### Parameter Naming Rules

1. **Use English parameter names and join multiple words with underscores**
2. **descriptionActiondescriptionParameterdescriptionPropertiesdescription**
3. **description，descriptionAIdescription**
