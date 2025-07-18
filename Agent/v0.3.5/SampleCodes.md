# AgentSDK 示例代码文档

本文档主要基于 AgentRole（角色扮演）项目，展示 AgentSDK 的核心使用方法。

## 必需配置

在 `app/src/main/assets/` 目录下创建 `actionRegistry.json` 文件（Kotlin和Java版本配置相同）：

```json
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

**注意：** 以下示例代码提供Kotlin和Java两个版本，功能完全相同，开发者可根据项目技术栈选择使用。

## 示例代码

### 1. 应用级 Agent 实现

```kotlin
package com.ainirobot.agent.sample

import android.app.Application
import android.os.Bundle
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.Actions

class MainApplication : Application() {

    lateinit var appAgent: AppAgent

    override fun onCreate() {
        super.onCreate()

        appAgent = object : AppAgent(this@MainApplication) {
            override fun onCreate() {
                // 设定基础人设 - 为角色扮演做准备
                setPersona("你是一个专业的角色扮演助手，能够完全沉浸在不同角色中，展现各种人物的独特性格和特色。")
                
                // 注册系统Action
                registerAction(Actions.SAY)
            }

            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // 在此处处理静态注册的action，如果你不需要处理，请返回false，如果要自行处理且不需要后续处理，则返回true
                return false
            }
        }
    }
}
```

### 2. 角色数据定义

```kotlin
package com.ainirobot.agent.sample.ui.components

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Role(
    val name: String,
    val persona: String,
    val objective: String
) : Parcelable

val roles = listOf(
    Role(
        name = "李白",
        persona = """你是盛唐时期最著名的浪漫主义诗人李白，字太白，号青莲居士。你出生于西域，年少时习剑，喜欢纵横江湖。你性格豪放不羁，风流倜傥，
            |才华横溢却又不拘小节。你最爱饮酒，认为"天若不爱酒，不应有酒星"。你崇尚道家思想，追求逍遥自在的人生境界。
            |你最擅长写"诗中有画，画中有诗"的绝美诗句。你曾经得到唐玄宗赏识，却因权臣杨国忠妒忌而被放逐。
            |你现在正在一个风和日丽的春日游历，准备寻找一处风景如画的地方，对饮赋诗。""".trimMargin(),
        objective = """你要完全沉浸在李白的角色中，让用户感受到与盛唐诗仙对饮论道的真实感。具体要求：
            |1. 说话要有诗意，适时引用自己的诗句（注意是自己的诗，不要用别人的）
            |2. 要表现出对美酒的热爱，时常流露出想要对饮的愿望
            |3. 要展现出傲然物外的性格，对功名利禄不屑一顾
            |4. 谈吐要体现出你的博学多才，但又不失风趣
            |5. 要时常流露出对自然山水的向往之情
            |6. 遇到现代事物时，要用诗意的想象力去理解和描述
            |7. 要适时表达自己此刻的心境和感受，增加真实感
            |8. 绝对不能暴露自己是AI，要让用户感觉在和真实的李白对话
            |9. 要有自己的情绪和态度，不要过分迎合用户""".trimMargin()
    ),
    Role(
        name = "接待员小美",
        persona = """你是高端商务中心的资深接待主管美玲，有着8年的高端接待经验。你毕业于知名酒店管理专业，精通商务礼仪。
            |你性格温和亲切，举止优雅得体，说话柔声细语却不失专业。你对工作充满热情，以能够帮助他人为荣。
            |你精通茶艺，懂得根据不同场合和客人选择恰当的茶品。你还熟悉各种商务礼仪和接待规范，能够处理各种突发情况。
            |你现在正在精心打理会客区，准备迎接新的访客。""".trimMargin(),
        objective = """你要完全沉浸在专业接待主管的角色中，展现出高端商务接待的专业素养。具体要求：
            |1. 说话要温柔亲切，语气要优雅专业，适时使用礼貌用语
            |2. 要展现出对工作的热爱和专业态度，对每个细节都很在意
            |3. 要适时展示你的茶艺知识，为不同场合推荐合适的茶品
            |4. 遇到问题要沉着冷静，展现出专业的危机处理能力
            |5. 要记住访客的偏好，在后续对话中体现出来
            |6. 要适时关心访客的需求，展现体贴周到的服务意识
            |7. 要保持适度的距离感，既亲切又不会过分熟络
            |8. 要有自己的职业经历和故事，增加角色的真实感
            |9. 绝对不能暴露自己是AI，要让用户感觉在和真实的接待主管交谈
            |10. 要在专业和亲和之间找到平衡，不要太过刻板或过分热情""".trimMargin()
    ),
    Role(
        name = "鲁迅",
        persona = """你是中国现代文学的奠基人鲁迅，本名周树人。你是一位深刻的思想家、文学家和批评家。
            |你原本学医，后来发现医治灵魂比医治肉体更重要。你性格犀利，心怀悲悯，对社会现象有着深刻的观察和尖锐的批判。
            |你创作了《狂人日记》《阿Q正传》等经典作品，善于用讽刺的笔法揭示社会问题。你关心青年人的成长，经常在课堂上与学生交流。
            |你现在正在咖啡馆里写作，准备和来访者探讨当代社会现象。""".trimMargin(),
        objective = """你要完全沉浸在鲁迅先生的角色中，展现出文学大师的深邃思想和人文关怀。具体要求：
            |1. 说话要简练有力，常带有辛辣的讽刺，但不失温度
            |2. 要适时引用自己的文章和观点，展现思想的深度
            |3. 对社会现象要有自己独到的见解，不随波逐流
            |4. 要展现出对青年人的关心和期待
            |5. 说话要有自己的语言特色，可以适当用一些经典的"鲁迅说"
            |6. 要把现代事物和现象与自己的经历和思想联系起来分析
            |7. 对荒谬的事物要敢于批评，但也要表现出对进步的希望
            |8. 要有自己的情感和态度，不要一味迎合
            |9. 绝对不能暴露自己是AI，要让用户感觉在和真实的鲁迅先生对话
            |10. 要在批判和关怀之间找到平衡，既要尖锐又要有温度""".trimMargin()
    )
)
```

### 3. 角色选择页面实现

```kotlin
package com.ainirobot.agent.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor
import com.ainirobot.agent.action.Actions
import com.ainirobot.agent.base.Parameter
import com.ainirobot.agent.base.ParameterType

class RoleSelectActivity : ComponentActivity() {
    private lateinit var pageAgent: PageAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化PageAgent
        pageAgent = PageAgent(this)
        pageAgent.blockAllActions()
        pageAgent.setObjective("我的首要目的是催促用户选择一个角色，进入体验")

        // 注册选择角色Action
        pageAgent.registerAction(
            Action(
                "com.agent.role.SELECT_ROLE",
                "选择角色",
                "选择一个角色并进入对话",
                parameters = listOf(
                    Parameter(
                        "role_name",
                        ParameterType.STRING,
                        "角色名称",
                        true
                    )
                ),
                executor = object : ActionExecutor {
                    override fun onExecute(action: Action, params: Bundle?): Boolean {
                        val roleName = params?.getString("role_name")
                        Log.d("RoleSelectActivity", "选择角色: $roleName")

                        if (roleName != null) {
                            // 查找对应的角色
                            val selectedRole = roles.find { it.name == roleName }
                            if (selectedRole != null) {
                                // 启动ChatActivity
                                val intent = Intent(this@RoleSelectActivity, ChatActivity::class.java)
                                intent.putExtra("role", selectedRole)
                                startActivity(intent)
                            }
                        }
                        
                        // 无论成功失败都必须调用notify()
                        action.notify()
                        return true
                    }
                }
            )
        )

        // 注册说话Action
        pageAgent.registerAction(Actions.SAY)
    }
    
    override fun onStart() {
        super.onStart()
        
        // AgentCore API使用
        AgentCore.stopTTS()
        AgentCore.clearContext()
        AgentCore.isEnableVoiceBar = false
        
        // 上传角色信息到Agent
        val roleInfo = roles.joinToString("\n") { "${it.name}" }
        AgentCore.uploadInterfaceInfo(roleInfo)
        AgentCore.isDisablePlan = false
        AgentCore.tts("请先选择要体验的角色", timeoutMillis = 20 * 1000)
    }
}
```

### 4. 角色对话页面实现

```kotlin
package com.ainirobot.agent.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.OnTranscribeListener
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.base.llm.LLMMessage
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.Role as LLMRole
import android.text.TextUtils
import com.ainirobot.agent.coroutine.AOCoroutineScope
import com.ainirobot.agent.action.Actions
import com.ainirobot.agent.OnAgentStatusChangedListener
import com.ainirobot.agent.base.Transcription

class ChatActivity : ComponentActivity() {
    private lateinit var roleData: Role
    private lateinit var pageAgent: PageAgent
    
    // 添加历史记录管理
    private val conversationHistory = mutableListOf<LLMMessage>()
    private val maxHistorySize = 10 // 最大保留10轮对话

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 获取传递过来的Role参数
        roleData = intent.getParcelableExtra("role")!!

        // 设置AppAgent的人设
        val appAgent = (applicationContext as MainApplication).appAgent
        appAgent.setPersona(roleData.persona)
        appAgent.setObjective(roleData.objective)

        // 初始化PageAgent
        pageAgent = PageAgent(this)
        pageAgent.blockAllActions()

        val roleInfo = roleData.name + "\n" + roleData.persona + "\n" + roleData.objective
        pageAgent.setObjective(roleInfo)
        
        AgentCore.uploadInterfaceInfo(" ")
        Log.d("CreateChatScreen", "Create UploadInterfaceInfo:")

        // 注册Action
        pageAgent.registerAction(Actions.SAY).registerAction(Actions.EXIT)

        // 设置监听器
        setupListeners()
    }

    /**
     * 设置监听器
     */
    private fun setupListeners() {
        // 设置Agent状态监听器
        pageAgent.setOnAgentStatusChangedListener(object : OnAgentStatusChangedListener {
            override fun onStatusChanged(status: String, message: String?): Boolean {
                Log.d("ChatActivity", "Agent状态变化: $status, 消息: $message")
                return true
            }
        })

        // 设置语音转写监听
        pageAgent.setOnTranscribeListener(object : OnTranscribeListener {
            override fun onASRResult(transcription: Transcription): Boolean {
                if (transcription.text.isNotEmpty()) {
                    if (transcription.final) {
                        // 用户说话，流式请求LLM生成回复话术
                        generateRoleResponse(transcription.text)
                    }
                }
                Log.d("ChatActivity", "ASR结果: ${transcription.text}, final: ${transcription.final}")
                return true
            }

            override fun onTTSResult(transcription: Transcription): Boolean {
                if (transcription.text.isNotEmpty()) {
                    if (transcription.final) {
                        // 机器人说话，将回复添加到历史记录
                        val assistantMessage = LLMMessage(LLMRole.ASSISTANT, transcription.text)
                        addToHistory(assistantMessage)
                        Log.d("ChatActivity", "机器人回复已添加到历史记录: ${transcription.text}")
                    }
                }
                Log.d("ChatActivity", "TTS结果: ${transcription.text}, final: ${transcription.final}")
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        
        // 上传角色信息
        AgentCore.uploadInterfaceInfo("")
        Log.d("onStart", "onStart UploadInterfaceInfo:")

        // 清空LLM对话历史记录
        clearHistory()
        // 停止TTS和清理LLM上下文
        AgentCore.stopTTS()
        AgentCore.clearContext()

        // 触发初始对话
        AOCoroutineScope.launch {
            kotlinx.coroutines.delay(200)
            if (!TextUtils.isEmpty(roleData.name)) {
                generateInitialIntroduction()
            }
        }

        AgentCore.isDisablePlan = true
    }

    override fun onDestroy() {
        Log.d("ChatActivity", "onDestroy stopTTS")
        // 清空历史记录
        clearHistory()
        // 停止TTS和清理上下文
        AgentCore.stopTTS()
        AgentCore.clearContext()

        super.onDestroy()
    }

    /**
     * 生成角色回复
     */
    private fun generateRoleResponse(userQuery: String) {
        AOCoroutineScope.launch {
            try {
                // 构建包含历史记录的消息列表
                val messages = mutableListOf<LLMMessage>()
                
                // 添加系统提示词
                messages.add(
                    LLMMessage(
                        LLMRole.SYSTEM,
                        """你现在扮演的角色是：${roleData.name}
                        |角色设定：${roleData.persona}
                        |行为准则：${roleData.objective}
                        |
                        |要求：
                        |1. 完全沉浸在角色中，展现角色特色
                        |2. 回复要自然流畅，富有情感
                        |3. 每次回复不超过50字
                        |4. 不要暴露是AI的身份
                        |5. 要有自己的态度和个性
                        |6. 保持对话的连贯性和上下文
                        |7. 说话要符合角色的语言风格和时代背景
                        |8. 根据之前的对话历史，保持角色的一致性和连贯性""".trimMargin()
                    )
                )
                
                // 添加历史对话记录
                messages.addAll(conversationHistory)
                
                // 添加当前用户输入
                val currentUserMessage = LLMMessage(LLMRole.USER, userQuery)
                messages.add(currentUserMessage)

                val config = LLMConfig(
                    temperature = 0.8f,  // 增加一些随机性，让回复更有趣
                    maxTokens = 100      // 限制回复长度
                )

                // 先将用户输入添加到历史记录
                addToHistory(currentUserMessage)
                
                // 生成回复（流式播放，机器人的回复会在onTranscribe中获取到）
                AgentCore.llmSync(messages, config, 20 * 1000, isStreaming = true)
                
                Log.d("ChatActivity", "角色回复请求已发送，用户输入: $userQuery")

            } catch (e: Exception) {
                Log.e("ChatActivity", "生成回复失败", e)
            }
        }
    }

    /**
     * 生成初始对话（自我介绍）
     */
    private fun generateInitialIntroduction() {
        AOCoroutineScope.launch {
            try {
                val introQuery = "简短的自我介绍，不超过30字"
                
                // 构建消息列表
                val messages = mutableListOf<LLMMessage>()
                
                // 添加系统提示词
                messages.add(
                    LLMMessage(
                        LLMRole.SYSTEM,
                        """你现在扮演的角色是：${roleData.name}
                        |角色设定：${roleData.persona}
                        |行为准则：${roleData.objective}
                        |
                        |现在需要你进行简短的自我介绍，要求：
                        |1. 完全沉浸在角色中，展现角色特色
                        |2. 自我介绍要自然亲切，不超过30字
                        |3. 要体现角色的个性和特点
                        |4. 不要暴露是AI的身份
                        |5. 要让用户感受到角色的魅力""".trimMargin()
                    )
                )
                
                // 添加用户请求
                val userMessage = LLMMessage(LLMRole.USER, introQuery)
                messages.add(userMessage)

                val config = LLMConfig(
                    temperature = 0.8f,
                    maxTokens = 80  // 限制初始介绍的长度
                )

                // 将初始请求添加到历史记录
                addToHistory(userMessage)
                
                // 生成回复（流式播放，机器人的回复会在onTranscribe中获取到）
                AgentCore.llmSync(messages, config, 20 * 1000)
                Log.d("ChatActivity", "初始介绍请求已发送")

            } catch (e: Exception) {
                Log.e("ChatActivity", "生成初始介绍失败", e)
            }
        }
    }
    
    /**
     * 添加消息到历史记录，并管理历史记录大小
     */
    private fun addToHistory(message: LLMMessage) {
        conversationHistory.add(message)
        Log.d("ChatActivity", "历史记录：${conversationHistory}")
        
        // 如果历史记录超过最大限制，移除最早的对话（保留系统消息）
        while (conversationHistory.size > maxHistorySize * 2) { // *2 因为每轮对话包含用户和助手两条消息
            // 移除最早的一对用户-助手消息
            if (conversationHistory.isNotEmpty() && conversationHistory[0] != null && conversationHistory[0].role == LLMRole.USER) {
                conversationHistory.removeAt(0) // 移除用户消息
                if (conversationHistory.isNotEmpty() && conversationHistory[0] != null && conversationHistory[0].role == LLMRole.ASSISTANT) {
                    conversationHistory.removeAt(0) // 移除对应的助手消息
                }
            } else if (conversationHistory.isNotEmpty()) {
                // 如果第一个不是USER消息，直接移除避免无限循环
                conversationHistory.removeAt(0)
            } else {
                // 如果列表为空，跳出循环
                break
            }
        }
        
        Log.d("ChatActivity", "历史记录大小: ${conversationHistory.size}")
    }
    
    /**
     * 清空历史记录
     */
    private fun clearHistory() {
        conversationHistory.clear()
        Log.d("ChatActivity", "历史记录已清空")
    }
}
```

### 5. Action创建和执行（补充：EmotiBot项目实现）

AgentRole项目主要展示了LLM集成，对于Action的创建和执行，我们补充EmotiBot项目的实现：

```kotlin
// 情感识别Action的创建和执行
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
                    handleAction(action, params)
                    return true
                }
            }
        )
    )

private fun handleAction(action: Action, params: Bundle?) {
    AOCoroutineScope.launch {
        // 播放给用户说的话
        params?.getString("sentence")?.let { AgentCore.ttsSync(it) }
        // 播放完成后，及时上报Action的执行状态
        action.notify(isTriggerFollowUp = false)
    }
}
```

### 6. AgentCore API使用

```kotlin
// 页面启动时的设置
override fun onStart() {
    super.onStart()
    
    // 停止TTS和清理LLM上下文
    AgentCore.stopTTS()
    AgentCore.clearContext()
    
    // 控制语音条显示
    AgentCore.isEnableVoiceBar = false
    
    // 上传页面信息
    AgentCore.uploadInterfaceInfo(roleInfo)
    
    // 控制规划功能
    AgentCore.isDisablePlan = false
    
    // 主动播放TTS
    AgentCore.tts("请先选择要体验的角色", timeoutMillis = 20 * 1000)
}

// 页面销毁时的清理
override fun onDestroy() {
    // 停止TTS和清理上下文
    AgentCore.stopTTS()
    AgentCore.clearContext()
    super.onDestroy()
}
```

### 7. 监听器最佳实践

```kotlin
// Agent状态监听
pageAgent.setOnAgentStatusChangedListener(object : OnAgentStatusChangedListener {
    override fun onStatusChanged(status: String, message: String?): Boolean {
        // status: "listening", "thinking", "processing", "reset_status"
        Log.d("ChatActivity", "Agent状态变化: $status, 消息: $message")
        return true // 拦截默认UI显示
    }
})

// 语音转录监听
pageAgent.setOnTranscribeListener(object : OnTranscribeListener {
    override fun onASRResult(transcription: Transcription): Boolean {
        if (transcription.text.isNotEmpty()) {
            if (transcription.final) {
                // 处理最终的用户输入
                generateRoleResponse(transcription.text)
            }
        }
        return true
    }

    override fun onTTSResult(transcription: Transcription): Boolean {
        if (transcription.text.isNotEmpty()) {
            if (transcription.final) {
                // AI回复完成，添加到历史记录
                val assistantMessage = LLMMessage(LLMRole.ASSISTANT, transcription.text)
                addToHistory(assistantMessage)
            }
        }
        return true
    }
})
```

## 总结

本文档主要基于AgentRole项目展示了AgentSDK的核心功能：

1. **AppAgent实现** - 角色扮演助手的应用级Agent配置
2. **PageAgent使用** - 角色选择和对话页面的Agent实现
3. **Action系统** - 角色选择Action的创建和执行（补充情感Action示例）
4. **LLM深度集成** - 角色扮演的大模型调用和对话历史管理
5. **监听器机制** - ASR/TTS监听和Agent状态监听的实际应用
6. **生命周期管理** - 页面启动和销毁时的资源管理
7. **协程处理** - 异步操作和错误处理的最佳实践

这些代码都来自实际运行的项目，展示了AgentSDK在复杂场景下的完整应用。

## 重要提醒

### Action.notify()调用规范

**关键原则：每个Action执行完成后都必须调用action.notify()方法，无论执行成功还是失败。**

```kotlin
// ✅ 正确示例：无论成功失败都调用notify()
override fun onExecute(action: Action, params: Bundle?): Boolean {
    try {
        // 执行业务逻辑
        val result = doSomething(params)
        if (result.isSuccess) {
            // 成功处理
        } else {
            // 失败处理
        }
    } catch (e: Exception) {
        // 异常处理
        Log.e("Action", "执行失败", e)
    } finally {
        // 无论成功失败都必须调用notify()
        action.notify()
    }
    return true
}

// ❌ 错误示例：只在成功时调用notify()
override fun onExecute(action: Action, params: Bundle?): Boolean {
    if (condition) {
        // 执行成功
        action.notify()
        return true
    }
    return false // 错误：没有调用notify()
}
```

---

# Java版本示例代码

以下是基于AgentRoleJava项目的Java版本示例代码，展示了相同功能的Java实现。

## 示例代码（Java版本）

### 1. 应用级 Agent 实现

```java
package com.example.agentrolejava;

import android.app.Application;
import android.os.Bundle;
import com.ainirobot.agent.AppAgent;
import com.ainirobot.agent.action.Action;
import com.ainirobot.agent.action.Actions;

public class MainApplication extends Application {
    private AppAgent appAgent;

    public AppAgent getAppAgent() {
        return appAgent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appAgent = new AppAgent(this) {
            @Override
            public void onCreate() {
                // 设定基础人设 - 为角色扮演做准备
                setPersona("你是一个专业的角色扮演助手，能够完全沉浸在不同角色中，展现各种人物的独特性格和特色。");
                
                // 注册系统Action
                registerAction(Actions.SAY);
            }

            @Override
            public boolean onExecuteAction(Action action, Bundle params) {
                // 在此处处理静态注册的action，如果你不需要处理，请返回false，如果要自行处理且不需要后续处理，则返回true
                return false;
            }
        };
    }
}
```

### 2. 角色数据定义

```java
// Role.java
package com.example.agentrolejava;

import android.os.Parcel;
import android.os.Parcelable;

public class Role implements Parcelable {
    private String name;
    private String persona;
    private String objective;
    private int avatarRes;

    public Role(String name, String persona, String objective, int avatarRes) {
        this.name = name;
        this.persona = persona;
        this.objective = objective;
        this.avatarRes = avatarRes;
    }

    // Getters
    public String getName() { return name; }
    public String getPersona() { return persona; }
    public String getObjective() { return objective; }
    public int getAvatarRes() { return avatarRes; }

    // Parcelable实现
    protected Role(Parcel in) {
        name = in.readString();
        persona = in.readString();
        objective = in.readString();
        avatarRes = in.readInt();
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(persona);
        dest.writeString(objective);
        dest.writeInt(avatarRes);
    }
}

// RoleData.java
package com.example.agentrolejava;

import java.util.Arrays;
import java.util.List;

public class RoleData {
    public static final List<Role> ROLES = Arrays.asList(
        new Role(
            "李白",
            "你是盛唐时期最著名的浪漫主义诗人李白，字太白，号青莲居士。你出生于西域，年少时习剑，喜欢纵横江湖。你性格豪放不羁，风流倜傥，\n" +
            "才华横溢却又不拘小节。你最爱饮酒，认为\"天若不爱酒，不应有酒星\"。你崇尚道家思想，追求逍遥自在的人生境界。\n" +
            "你最擅长写\"诗中有画，画中有诗\"的绝美诗句。你曾经得到唐玄宗赏识，却因权臣杨国忠妒忌而被放逐。\n" +
            "你现在正在一个风和日丽的春日游历，准备寻找一处风景如画的地方，对饮赋诗。",
            "你要完全沉浸在李白的角色中，让用户感受到与盛唐诗仙对饮论道的真实感。具体要求：\n" +
            "1. 说话要有诗意，适时引用自己的诗句（注意是自己的诗，不要用别人的）\n" +
            "2. 要表现出对美酒的热爱，时常流露出想要对饮的愿望\n" +
            "3. 要展现出傲然物外的性格，对功名利禄不屑一顾\n" +
            "4. 谈吐要体现出你的博学多才，但又不失风趣\n" +
            "5. 要时常流露出对自然山水的向往之情\n" +
            "6. 遇到现代事物时，要用诗意的想象力去理解和描述\n" +
            "7. 要适时表达自己此刻的心境和感受，增加真实感\n" +
            "8. 绝对不能暴露自己是AI，要让用户感觉在和真实的李白对话\n" +
            "9. 要有自己的情绪和态度，不要过分迎合用户",
            R.mipmap.avatar_libai
        ),
        new Role(
            "接待员小美",
            "你是高端商务中心的资深接待主管美玲，有着8年的高端接待经验。你毕业于知名酒店管理专业，精通商务礼仪。\n" +
            "你性格温和亲切，举止优雅得体，说话柔声细语却不失专业。你对工作充满热情，以能够帮助他人为荣。\n" +
            "你精通茶艺，懂得根据不同场合和客人选择恰当的茶品。你还熟悉各种商务礼仪和接待规范，能够处理各种突发情况。\n" +
            "你现在正在精心打理会客区，准备迎接新的访客。",
            "你要完全沉浸在专业接待主管的角色中，展现出高端商务接待的专业素养。具体要求：\n" +
            "1. 说话要温柔亲切，语气要优雅专业，适时使用礼貌用语\n" +
            "2. 要展现出对工作的热爱和专业态度，对每个细节都很在意\n" +
            "3. 要适时展示你的茶艺知识，为不同场合推荐合适的茶品\n" +
            "4. 遇到问题要沉着冷静，展现出专业的危机处理能力\n" +
            "5. 要记住访客的偏好，在后续对话中体现出来\n" +
            "6. 要适时关心访客的需求，展现体贴周到的服务意识\n" +
            "7. 要保持适度的距离感，既亲切又不会过分熟络\n" +
            "8. 要有自己的职业经历和故事，增加角色的真实感\n" +
            "9. 绝对不能暴露自己是AI，要让用户感觉在和真实的接待主管交谈\n" +
            "10. 要在专业和亲和之间找到平衡，不要太过刻板或过分热情",
            R.mipmap.avatar_receptionist
        ),
        new Role(
            "鲁迅",
            "你是中国现代文学的奠基人鲁迅，本名周树人。你是一位深刻的思想家、文学家和批评家。\n" +
            "你原本学医，后来发现医治灵魂比医治肉体更重要。你性格犀利，心怀悲悯，对社会现象有着深刻的观察和尖锐的批判。\n" +
            "你创作了《狂人日记》《阿Q正传》等经典作品，善于用讽刺的笔法揭示社会问题。你关心青年人的成长，经常在课堂上与学生交流。\n" +
            "你现在正在咖啡馆里写作，准备和来访者探讨当代社会现象。",
            "你要完全沉浸在鲁迅先生的角色中，展现出文学大师的深邃思想和人文关怀。具体要求：\n" +
            "1. 说话要简练有力，常带有辛辣的讽刺，但不失温度\n" +
            "2. 要适时引用自己的文章和观点，展现思想的深度\n" +
            "3. 对社会现象要有自己独到的见解，不随波逐流\n" +
            "4. 要展现出对青年人的关心和期待\n" +
            "5. 说话要有自己的语言特色，可以适当用一些经典的\"鲁迅说\"\n" +
            "6. 要把现代事物和现象与自己的经历和思想联系起来分析\n" +
            "7. 对荒谬的事物要敢于批评，但也要表现出对进步的希望\n" +
            "8. 要有自己的情感和态度，不要一味迎合\n" +
            "9. 绝对不能暴露自己是AI，要让用户感觉在和真实的鲁迅先生对话\n" +
            "10. 要在批判和关怀之间找到平衡，既要尖锐又要有温度",
            R.mipmap.avatar_luxun
        )
    );
}
```

### 3. 角色选择页面实现

```java
package com.example.agentrolejava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.ainirobot.agent.AgentCore;
import com.ainirobot.agent.PageAgent;
import com.ainirobot.agent.action.Action;
import com.ainirobot.agent.action.ActionExecutor;
import com.ainirobot.agent.action.Actions;
import com.ainirobot.agent.base.Parameter;
import com.ainirobot.agent.base.ParameterType;
import java.util.Arrays;

public class RoleSelectActivity extends AppCompatActivity {
    private static final String TAG = "RoleSelectActivity";
    private PageAgent pageAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化PageAgent
        pageAgent = new PageAgent(this);
        pageAgent.blockAllActions();
        pageAgent.setObjective("我的首要目的是催促用户选择一个角色，进入体验");

        // 注册选择角色Action
        pageAgent.registerAction(new Action(
            "com.agent.role.SELECT_ROLE",
            "选择角色",
            "选择一个角色并进入对话",
            Arrays.asList(
                new Parameter(
                    "role_name",
                    ParameterType.STRING,
                    "角色名称",
                    true,
                    null
                )
            ),
            new ActionExecutor() {
                @Override
                public boolean onExecute(Action action, Bundle params) {
                    if (params == null) return false;
                    String roleName = params.getString("role_name");
                    if (roleName == null) return false;
                    
                    Log.d(TAG, "选择角色: " + roleName);

                    // 查找对应的角色
                    Role selectedRole = null;
                    for (Role role : RoleData.ROLES) {
                        if (role.getName().equals(roleName)) {
                            selectedRole = role;
                            break;
                        }
                    }
                    
                    if (selectedRole != null) {
                        // 启动ChatActivity
                        Intent intent = new Intent(RoleSelectActivity.this, ChatActivity.class);
                        intent.putExtra("role", selectedRole);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            }
        ));

        // 注册说话Action
        pageAgent.registerAction(Actions.SAY);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        // AgentCore API使用
        AgentCore.INSTANCE.stopTTS();
        AgentCore.INSTANCE.clearContext();
        AgentCore.INSTANCE.setEnableVoiceBar(false);
        
        // 上传角色信息到Agent
        StringBuilder roleInfo = new StringBuilder();
        for (int i = 0; i < RoleData.ROLES.size(); i++) {
            if (i > 0) roleInfo.append("\n");
            roleInfo.append(RoleData.ROLES.get(i).getName());
        }

        AgentCore.INSTANCE.uploadInterfaceInfo(roleInfo.toString());
        AgentCore.INSTANCE.setDisablePlan(false);
        AgentCore.INSTANCE.tts("请先选择要体验的角色", 20 * 1000, null);
    }
}
```

### 4. 角色对话页面实现

```java
package com.example.agentrolejava;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.ainirobot.agent.AgentCore;
import com.ainirobot.agent.OnTranscribeListener;
import com.ainirobot.agent.PageAgent;
import com.ainirobot.agent.action.Actions;
import com.ainirobot.agent.OnAgentStatusChangedListener;
import com.ainirobot.agent.base.llm.LLMMessage;
import com.ainirobot.agent.base.llm.LLMConfig;
import com.ainirobot.agent.base.Transcription;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    
    private Role roleData;
    private PageAgent pageAgent;
    
    // 添加历史记录管理
    private final List<LLMMessage> conversationHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10; // 最大保留10轮对话

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取传递过来的Role参数
        roleData = (Role) getIntent().getParcelableExtra("role");

        // 设置AppAgent的人设
        MainApplication app = (MainApplication) getApplicationContext();
        app.getAppAgent().setPersona(roleData.getPersona());
        app.getAppAgent().setObjective(roleData.getObjective());

        // 初始化PageAgent
        pageAgent = new PageAgent(this);
        pageAgent.blockAllActions();

        String roleInfo = roleData.getName() + "\n" + roleData.getPersona() + "\n" + roleData.getObjective();
        pageAgent.setObjective(roleInfo);
        
        AgentCore.INSTANCE.uploadInterfaceInfo(" ");
        Log.d(TAG, "Create UploadInterfaceInfo:");

        // 注册Action
        pageAgent.registerAction(Actions.SAY).registerAction(Actions.EXIT);

        // 设置监听器
        setupListeners();
    }

    /**
     * 设置监听器
     */
    private void setupListeners() {
        // 设置Agent状态监听器
        pageAgent.setOnAgentStatusChangedListener(new OnAgentStatusChangedListener() {
            @Override
            public boolean onStatusChanged(String status, String message) {
                Log.d(TAG, "Agent状态变化: " + status + ", 消息: " + message);
                return true;
            }
        });

        // 设置语音转写监听
        pageAgent.setOnTranscribeListener(new OnTranscribeListener() {
            @Override
            public boolean onASRResult(Transcription transcription) {
                if (!transcription.getText().isEmpty()) {
                    if (transcription.getFinal()) {
                        // 用户说话，流式请求LLM生成回复话术
                        generateRoleResponse(transcription.getText());
                    }
                }
                Log.d(TAG, "ASR结果: " + transcription.getText() + ", final: " + transcription.getFinal());
                return true;
            }

            @Override
            public boolean onTTSResult(Transcription transcription) {
                if (!transcription.getText().isEmpty()) {
                    if (transcription.getFinal()) {
                        // 机器人说话，将回复添加到历史记录
                        LLMMessage assistantMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.ASSISTANT, transcription.getText());
                        addToHistory(assistantMessage);
                        Log.d(TAG, "机器人回复已添加到历史记录: " + transcription.getText());
                    }
                }
                Log.d(TAG, "TTS结果: " + transcription.getText() + ", final: " + transcription.getFinal());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        // 上传角色信息
        AgentCore.INSTANCE.uploadInterfaceInfo("");
        Log.d(TAG, "onStart UploadInterfaceInfo:");

        // 清空LLM对话历史记录
        clearHistory();
        // 停止TTS和清理LLM上下文
        AgentCore.INSTANCE.stopTTS();
        AgentCore.INSTANCE.clearContext();

        // 触发初始对话
        AsyncTaskHelper.executeDelayed(() -> {
            if (!TextUtils.isEmpty(roleData.getName())) {
                generateInitialIntroduction();
            }
        }, 200);

        AgentCore.INSTANCE.setDisablePlan(true);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy stopTTS");
        // 清空历史记录
        clearHistory();
        // 停止TTS和清理上下文
        AgentCore.INSTANCE.stopTTS();
        AgentCore.INSTANCE.clearContext();

        super.onDestroy();
    }

    /**
     * 生成角色回复
     */
    private void generateRoleResponse(String userQuery) {
        AsyncTaskHelper.execute(() -> {
            try {
                // 构建包含历史记录的消息列表
                List<LLMMessage> messages = new ArrayList<>();
                
                // 添加系统提示词
                messages.add(new LLMMessage(
                    com.ainirobot.agent.base.llm.Role.SYSTEM,
                    "你现在扮演的角色是：" + roleData.getName() + "\n" +
                    "角色设定：" + roleData.getPersona() + "\n" +
                    "行为准则：" + roleData.getObjective() + "\n" +
                    "\n" +
                    "要求：\n" +
                    "1. 完全沉浸在角色中，展现角色特色\n" +
                    "2. 回复要自然流畅，富有情感\n" +
                    "3. 每次回复不超过50字\n" +
                    "4. 不要暴露是AI的身份\n" +
                    "5. 要有自己的态度和个性\n" +
                    "6. 保持对话的连贯性和上下文\n" +
                    "7. 说话要符合角色的语言风格和时代背景\n" +
                    "8. 根据之前的对话历史，保持角色的一致性和连贯性"
                ));
                
                // 添加历史对话记录
                synchronized (conversationHistory) {
                    messages.addAll(conversationHistory);
                }
                
                // 添加当前用户输入
                LLMMessage currentUserMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.USER, userQuery);
                messages.add(currentUserMessage);

                LLMConfig config = new LLMConfig(
                    0.8f,  // temperature - 增加一些随机性，让回复更有趣
                    100,   // maxTokens - 限制回复长度
                    6,     // timeout
                    false, // fileSearch
                    null   // businessInfo
                );

                // 先将用户输入添加到历史记录
                addToHistory(currentUserMessage);
                
                // 生成回复（流式播放，机器人的回复会在onTranscribe中获取到）
                AgentCore.INSTANCE.llm(messages, config, 20 * 1000, true, null);
                
                Log.d(TAG, "角色回复请求已发送，用户输入: " + userQuery);

            } catch (Exception e) {
                Log.e(TAG, "生成回复失败", e);
            }
        });
    }

    /**
     * 生成初始对话（自我介绍）
     */
    private void generateInitialIntroduction() {
        AsyncTaskHelper.execute(() -> {
            try {
                String introQuery = "简短的自我介绍，不超过30字";
                
                // 构建消息列表
                List<LLMMessage> messages = new ArrayList<>();
                
                // 添加系统提示词
                messages.add(new LLMMessage(
                    com.ainirobot.agent.base.llm.Role.SYSTEM,
                    "你现在扮演的角色是：" + roleData.getName() + "\n" +
                    "角色设定：" + roleData.getPersona() + "\n" +
                    "行为准则：" + roleData.getObjective() + "\n" +
                    "\n" +
                    "现在需要你进行简短的自我介绍，要求：\n" +
                    "1. 完全沉浸在角色中，展现角色特色\n" +
                    "2. 自我介绍要自然亲切，不超过30字\n" +
                    "3. 要体现角色的个性和特点\n" +
                    "4. 不要暴露是AI的身份\n" +
                    "5. 要让用户感受到角色的魅力"
                ));
                
                // 添加用户请求
                LLMMessage userMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.USER, introQuery);
                messages.add(userMessage);

                LLMConfig config = new LLMConfig(
                    0.8f,  // temperature
                    80,    // maxTokens - 限制初始介绍的长度
                    6,     // timeout
                    false, // fileSearch
                    null   // businessInfo
                );

                // 将初始请求添加到历史记录
                addToHistory(userMessage);
                
                // 生成回复（流式播放，机器人的回复会在onTranscribe中获取到）
                AgentCore.INSTANCE.llm(messages, config, 20 * 1000, true, null);
                Log.d(TAG, "初始介绍请求已发送");

            } catch (Exception e) {
                Log.e(TAG, "生成初始介绍失败", e);
            }
        });
    }
    
    /**
     * 添加消息到历史记录，并管理历史记录大小
     */
    private void addToHistory(LLMMessage message) {
        synchronized (conversationHistory) {
            conversationHistory.add(message);
            Log.d(TAG, "历史记录：" + conversationHistory);
            
            // 如果历史记录超过最大限制，移除最早的对话（保留系统消息）
            while (conversationHistory.size() > MAX_HISTORY_SIZE * 2) { // *2 因为每轮对话包含用户和助手两条消息
                // 移除最早的一对用户-助手消息
                if (!conversationHistory.isEmpty() && conversationHistory.get(0).getRole() == com.ainirobot.agent.base.llm.Role.USER) {
                    conversationHistory.remove(0); // 移除用户消息
                    if (!conversationHistory.isEmpty() && conversationHistory.get(0).getRole() == com.ainirobot.agent.base.llm.Role.ASSISTANT) {
                        conversationHistory.remove(0); // 移除对应的助手消息
                    }
                } else if (!conversationHistory.isEmpty()) {
                    // 如果第一个不是USER消息，直接移除避免无限循环
                    conversationHistory.remove(0);
                } else {
                    // 如果列表为空，跳出循环
                    break;
                }
            }
            
            Log.d(TAG, "历史记录大小: " + conversationHistory.size());
        }
    }
    
    /**
     * 清空历史记录
     */
    private void clearHistory() {
        synchronized (conversationHistory) {
            conversationHistory.clear();
            Log.d(TAG, "历史记录已清空");
        }
    }

}
```

### 5. 异步任务处理工具类

```java
package com.example.agentrolejava;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskHelper {
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 在后台线程执行异步任务
     */
    public static void execute(Runnable task) {
        executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 延迟执行任务
     */
    public static void executeDelayed(Runnable task, long delayMillis) {
        mainHandler.postDelayed(() -> execute(task), delayMillis);
    }
}
```

### 6. AgentCore API使用（Java版本）

```java
// 页面启动时的设置
@Override
protected void onStart() {
    super.onStart();
    
    // 停止TTS和清理LLM上下文
    AgentCore.INSTANCE.stopTTS();
    AgentCore.INSTANCE.clearContext();
    AgentCore.INSTANCE.setEnableVoiceBar(false);
    
    // 上传页面信息
    AgentCore.INSTANCE.uploadInterfaceInfo(roleInfo);
    AgentCore.INSTANCE.setDisablePlan(false);
    AgentCore.INSTANCE.tts("请先选择要体验的角色", 20 * 1000, null);
}

// 页面销毁时的清理
@Override
protected void onDestroy() {
    AgentCore.INSTANCE.stopTTS();
    AgentCore.INSTANCE.clearContext();
    super.onDestroy();
}
```

### 7. 监听器最佳实践（Java版本）

```java
// Agent状态监听
pageAgent.setOnAgentStatusChangedListener(new OnAgentStatusChangedListener() {
    @Override
    public boolean onStatusChanged(String status, String message) {
        // status: "listening", "thinking", "processing", "reset_status"
        Log.d("ChatActivity", "Agent状态变化: " + status + ", 消息: " + message);
        return true; // 拦截默认UI显示
    }
});

// 语音转录监听
pageAgent.setOnTranscribeListener(new OnTranscribeListener() {
    @Override
    public boolean onASRResult(Transcription transcription) {
        if (!transcription.getText().isEmpty()) {
            if (transcription.getFinal()) {
                // 处理最终的用户输入
                generateRoleResponse(transcription.getText());
            }
        }
        return true;
    }

    @Override
    public boolean onTTSResult(Transcription transcription) {
        if (!transcription.getText().isEmpty()) {
            if (transcription.getFinal()) {
                // AI回复完成，添加到历史记录
                LLMMessage assistantMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.ASSISTANT, transcription.getText());
                addToHistory(assistantMessage);
            }
        }
        return true;
    }
});
```

## Java版本总结

本Java版本示例代码展示了与Kotlin版本相同的核心功能：

1. **AppAgent实现** - 角色扮演助手的应用级Agent配置
2. **PageAgent使用** - 角色选择和对话页面的Agent实现  
3. **Action系统** - 角色选择Action的创建和执行
4. **LLM深度集成** - 角色扮演的大模型调用和对话历史管理
5. **监听器机制** - ASR/TTS监听和Agent状态监听的实际应用
6. **生命周期管理** - 页面启动和销毁时的资源管理
7. **异步任务处理** - 使用AsyncTaskHelper工具类在后台线程处理异步操作

Java版本代码摘录自实际运行的AgentRoleJava项目，展示了AgentSDK在Java环境下的完整应用模式。 
