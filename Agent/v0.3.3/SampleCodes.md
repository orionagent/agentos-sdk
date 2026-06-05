# AgentSDK Sample Code Documentation

description AgentRole（description）description，description AgentSDK descriptionMethods。

## Required Configuration

description `app/src/main/assets/` Table of Contentsdescription `actionRegistry.json` description（KotlindescriptionJavadescription）：

```json
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

**Note：** descriptionKotlindescriptionJavadescription，description，description。

## Sample Code

### 1. App-level Agent Implementation

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
                // translated text
                setPersona("translated text")
                
                // translated textAction
                registerAction(Actions.SAY)
            }

            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // translated textaction，translated textfalse，translated texttrue
                return false
            }
        }
    }
}
```

### 2. Role Data Definition

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
        name = "translated text",
        persona = """translated text
            |translated text"translated text"。translated text
            |translated text"translated text"translated text
            |translated text""".trimMargin(),
        objective = """translated text
            |1. translated text
            |2. translated text
            |3. translated text
            |4. translated text
            |5. translated text
            |6. translated text
            |7. translated text
            |8. translated textAI，translated text
            |9. translated text""".trimMargin()
    ),
    Role(
        name = "translated text",
        persona = """translated text
            |translated text
            |translated text
            |translated text""".trimMargin(),
        objective = """translated text
            |1. translated text
            |2. translated text
            |3. translated text
            |4. translated text
            |5. translated text
            |6. translated text
            |7. translated text
            |8. translated text
            |9. translated textAI，translated text
            |10. translated text""".trimMargin()
    ),
    Role(
        name = "translated text",
        persona = """translated text
            |translated text
            |translated textQtranslated text
            |translated text""".trimMargin(),
        objective = """translated text
            |1. translated text
            |2. translated text
            |3. translated text
            |4. translated text
            |5. translated text"translated text"
            |6. translated text
            |7. translated text
            |8. translated text
            |9. translated textAI，translated text
            |10. translated text""".trimMargin()
    )
)
```

### 3. Role Selection Page Implementation

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

        // translated textPageAgent
        pageAgent = PageAgent(this)
        pageAgent.blockAllActions()
        pageAgent.setObjective("translated text")

        // translated textAction
        pageAgent.registerAction(
            Action(
                "com.agent.role.SELECT_ROLE",
                "translated text",
                "translated text",
                parameters = listOf(
                    Parameter(
                        "role_name",
                        ParameterType.STRING,
                        "translated text",
                        true
                    )
                ),
                executor = object : ActionExecutor {
                    override fun onExecute(action: Action, params: Bundle?): Boolean {
                        val roleName = params?.getString("role_name")
                        Log.d("RoleSelectActivity", "translated text: $roleName")

                        if (roleName != null) {
                            // translated text
                            val selectedRole = roles.find { it.name == roleName }
                            if (selectedRole != null) {
                                // translated textChatActivity
                                val intent = Intent(this@RoleSelectActivity, ChatActivity::class.java)
                                intent.putExtra("role", selectedRole)
                                startActivity(intent)
                            }
                        }
                        
                        // translated textnotify()
                        action.notify()
                        return true
                    }
                }
            )
        )

        // translated textAction
        pageAgent.registerAction(Actions.SAY)
    }
    
    override fun onStart() {
        super.onStart()
        
        // AgentCore APItranslated text
        AgentCore.stopTTS()
        AgentCore.clearContext()
        AgentCore.isEnableVoiceBar = false
        
        // translated textAgent
        val roleInfo = roles.joinToString("\n") { "${it.name}" }
        AgentCore.uploadInterfaceInfo(roleInfo)
        AgentCore.isDisablePlan = false
        AgentCore.tts("Please select a role to try first", timeoutMillis = 20 * 1000)
    }
}
```

### 4. Role Conversation Page Implementation

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
    
    // translated text
    private val conversationHistory = mutableListOf<LLMMessage>()
    private val maxHistorySize = 10 // translated text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // translated textRoletranslated text
        roleData = intent.getParcelableExtra("role")!!

        // translated textAppAgenttranslated text
        val appAgent = (applicationContext as MainApplication).appAgent
        appAgent.setPersona(roleData.persona)
        appAgent.setObjective(roleData.objective)

        // translated textPageAgent
        pageAgent = PageAgent(this)
        pageAgent.blockAllActions()

        val roleInfo = roleData.name + "\n" + roleData.persona + "\n" + roleData.objective
        pageAgent.setObjective(roleInfo)
        
        AgentCore.uploadInterfaceInfo(" ")
        Log.d("CreateChatScreen", "Create UploadInterfaceInfo:")

        // translated textAction
        pageAgent.registerAction(Actions.SAY).registerAction(Actions.EXIT)

        // translated text
        setupListeners()
    }

    /**
     * translated text
     */
    private fun setupListeners() {
        // translated textAgenttranslated text
        pageAgent.setOnAgentStatusChangedListener(object : OnAgentStatusChangedListener {
            override fun onStatusChanged(status: String, message: String?): Boolean {
                Log.d("ChatActivity", "Agenttranslated text: $status, message: $message")
                return true
            }
        })

        // translated text
        pageAgent.setOnTranscribeListener(object : OnTranscribeListener {
            override fun onASRResult(transcription: Transcription): Boolean {
                if (transcription.text.isNotEmpty()) {
                    if (transcription.final) {
                        // translated textLLMtranslated text
                        generateRoleResponse(transcription.text)
                    }
                }
                Log.d("ChatActivity", "ASRtranslated text: ${transcription.text}, final: ${transcription.final}")
                return true
            }

            override fun onTTSResult(transcription: Transcription): Boolean {
                if (transcription.text.isNotEmpty()) {
                    if (transcription.final) {
                        // Robot speaking; add reply to history
                        val assistantMessage = LLMMessage(LLMRole.ASSISTANT, transcription.text)
                        addToHistory(assistantMessage)
                        Log.d("ChatActivity", "Robot reply added to history: ${transcription.text}")
                    }
                }
                Log.d("ChatActivity", "TTStranslated text: ${transcription.text}, final: ${transcription.final}")
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        
        // translated text
        AgentCore.uploadInterfaceInfo("")
        Log.d("onStart", "onStart UploadInterfaceInfo:")

        // translated textLLMtranslated text
        clearHistory()
        // StopTTStranslated textLLMtranslated text
        AgentCore.stopTTS()
        AgentCore.clearContext()

        // translated text
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
        // translated text
        clearHistory()
        // StopTTStranslated text
        AgentCore.stopTTS()
        AgentCore.clearContext()

        super.onDestroy()
    }

    /**
     * translated text
     */
    private fun generateRoleResponse(userQuery: String) {
        AOCoroutineScope.launch {
            try {
                // translated text
                val messages = mutableListOf<LLMMessage>()
                
                // translated text
                messages.add(
                    LLMMessage(
                        LLMRole.SYSTEM,
                        """You are now playing the role of: ${roleData.name}
                        |Role profile: ${roleData.persona}
                        |Behavior guidelines: ${roleData.objective}
                        |
                        |Requirements:
                        |1. Fully immerse in the role and show its characteristics
                        |2. translated text
                        |3. translated text
                        |4. translated textAItranslated text
                        |5. translated text
                        |6. translated text
                        |7. translated text
                        |8. translated text""".trimMargin()
                    )
                )
                
                // translated text
                messages.addAll(conversationHistory)
                
                // translated text
                val currentUserMessage = LLMMessage(LLMRole.USER, userQuery)
                messages.add(currentUserMessage)

                val config = LLMConfig(
                    temperature = 0.8f,  // translated text
                    maxTokens = 100      // translated text
                )

                // translated text
                addToHistory(currentUserMessage)
                
                // translated textonTranscribetranslated text
                AgentCore.llmSync(messages, config, 20 * 1000, isStreaming = true)
                
                Log.d("ChatActivity", "translated text: $userQuery")

            } catch (e: Exception) {
                Log.e("ChatActivity", "Failed to generate reply", e)
            }
        }
    }

    /**
     * translated text
     */
    private fun generateInitialIntroduction() {
        AOCoroutineScope.launch {
            try {
                val introQuery = "Give a short self-introduction in under 30 words"
                
                // translated text
                val messages = mutableListOf<LLMMessage>()
                
                // translated text
                messages.add(
                    LLMMessage(
                        LLMRole.SYSTEM,
                        """You are now playing the role of: ${roleData.name}
                        |Role profile: ${roleData.persona}
                        |Behavior guidelines: ${roleData.objective}
                        |
                        |Now provide a brief self-introduction with these requirements:
                        |1. Fully immerse in the role and show its characteristics
                        |2. Make the introduction natural and friendly, under 30 words
                        |3. Reflect the role's personality and traits
                        |4. translated textAItranslated text
                        |5. Let users feel the role's charm""".trimMargin()
                    )
                )
                
                // translated text
                val userMessage = LLMMessage(LLMRole.USER, introQuery)
                messages.add(userMessage)

                val config = LLMConfig(
                    temperature = 0.8f,
                    maxTokens = 80  // translated text
                )

                // translated text
                addToHistory(userMessage)
                
                // translated textonTranscribetranslated text
                AgentCore.llmSync(messages, config, 20 * 1000)
                Log.d("ChatActivity", "Initial introduction request sent")

            } catch (e: Exception) {
                Log.e("ChatActivity", "Failed to generate initial introduction", e)
            }
        }
    }
    
    /**
     * translated text
     */
    private fun addToHistory(message: LLMMessage) {
        conversationHistory.add(message)
        Log.d("ChatActivity", "History: ${conversationHistory}")
        
        // translated text
        while (conversationHistory.size > maxHistorySize * 2) { // *2 translated text
            // translated text
            if (conversationHistory.isNotEmpty() && conversationHistory[0] != null && conversationHistory[0].role == LLMRole.USER) {
                conversationHistory.removeAt(0) // translated text
                if (conversationHistory.isNotEmpty() && conversationHistory[0] != null && conversationHistory[0].role == LLMRole.ASSISTANT) {
                    conversationHistory.removeAt(0) // translated text
                }
            } else if (conversationHistory.isNotEmpty()) {
                // translated textUSERtranslated text
                conversationHistory.removeAt(0)
            } else {
                // translated text
                break
            }
        }
        
        Log.d("ChatActivity", "History size: ${conversationHistory.size}")
    }
    
    /**
     * translated text
     */
    private fun clearHistory() {
        conversationHistory.clear()
        Log.d("ChatActivity", "History cleared")
    }
}
```

### 5. Actiondescriptionexecute（description：EmotiBotdescription）

AgentRoledescriptionLLMdescription，descriptionActiondescriptionexecute，descriptionEmotiBotdescription：

```kotlin
// translated textActiontranslated text
PageAgent(this)
    .registerAction(
        Action(
            name = "com.agent.demo.SHOW_SMILE_FACE",
            displayName = "Smile",
            desc = "Respond to the user's positive emotions",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "Reply text for the user",
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
        // translated text
        params?.getString("sentence")?.let { AgentCore.ttsSync(it) }
        // translated textActiontranslated text
        action.notify(isTriggerFollowUp = false)
    }
}
```

### 6. AgentCore APIdescription

```kotlin
// translated text
override fun onStart() {
    super.onStart()
    
    // StopTTStranslated textLLMtranslated text
    AgentCore.stopTTS()
    AgentCore.clearContext()
    
    // translated text
    AgentCore.isEnableVoiceBar = false
    
    // translated text
    AgentCore.uploadInterfaceInfo(roleInfo)
    
    // translated text
    AgentCore.isDisablePlan = false
    
    // translated textTTS
    AgentCore.tts("Please select a role to try first", timeoutMillis = 20 * 1000)
}

// translated text
override fun onDestroy() {
    // StopTTStranslated text
    AgentCore.stopTTS()
    AgentCore.clearContext()
    super.onDestroy()
}
```

### 7. Listen to description

```kotlin
// Agenttranslated text
pageAgent.setOnAgentStatusChangedListener(object : OnAgentStatusChangedListener {
    override fun onStatusChanged(status: String, message: String?): Boolean {
        // status: "listening", "thinking", "processing", "reset_status"
        Log.d("ChatActivity", "Agenttranslated text: $status, message: $message")
        return true // translated textUItranslated text
    }
})

// translated text
pageAgent.setOnTranscribeListener(object : OnTranscribeListener {
    override fun onASRResult(transcription: Transcription): Boolean {
        if (transcription.text.isNotEmpty()) {
            if (transcription.final) {
                // translated text
                generateRoleResponse(transcription.text)
            }
        }
        return true
    }

    override fun onTTSResult(transcription: Transcription): Boolean {
        if (transcription.text.isNotEmpty()) {
            if (transcription.final) {
                // AItranslated text
                val assistantMessage = LLMMessage(LLMRole.ASSISTANT, transcription.text)
                addToHistory(assistantMessage)
            }
        }
        return true
    }
})
```

## Summary

descriptionAgentRoledescriptionAgentSDKdescription：

1. **AppAgentdescription** - descriptionApp-level Agentdescription
2. **PageAgentdescription** - descriptionAgentdescription
3. **Actiondescription** - descriptionActiondescriptionexecute（descriptionActiondescription）
4. **LLMdescription** - descriptionLLMdescription
5. **Listen to description** - ASR/TTSListen to descriptionAgentdescriptionListen to description
6. **description** - description
7. **description** - description

description，descriptionAgentSDKdescription。

## description

### Action.notify()description

**description：descriptionActionexecutedescriptionaction.notify()Methods，descriptionexecutesuccessdescriptionfailure。**

```kotlin
// ✅ translated textnotify()
override fun onExecute(action: Action, params: Bundle?): Boolean {
    try {
        // translated text
        val result = doSomething(params)
        if (result.isSuccess) {
            // translated text
        } else {
            // translated text
        }
    } catch (e: Exception) {
        // translated text
        Log.e("Action", "translated text", e)
    } finally {
        // translated textnotify()
        action.notify()
    }
    return true
}

// ❌ translated textnotify()
override fun onExecute(action: Action, params: Bundle?): Boolean {
    if (condition) {
        // translated text
        action.notify()
        return true
    }
    return false // translated textnotify()
}
```

---

# Javadescription

descriptionAgentRoleJavadescriptionJavadescription，descriptionJavadescription。

## description（Javadescription）

### 1. App-level Agent Implementation

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
                // translated text
                setPersona("translated text");
                
                // translated textAction
                registerAction(Actions.SAY);
            }

            @Override
            public boolean onExecuteAction(Action action, Bundle params) {
                // translated textaction，translated textfalse，translated texttrue
                return false;
            }
        };
    }
}
```

### 2. Role Data Definition

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

    // Parcelabletranslated text
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
            "translated text",
            "translated text\n" +
            "translated text\"translated text\"。translated text\n" +
            "translated text\"translated text\"translated text\n" +
            "translated text",
            "translated text\n" +
            "1. translated text\n" +
            "2. translated text\n" +
            "3. translated text\n" +
            "4. translated text\n" +
            "5. translated text\n" +
            "6. translated text\n" +
            "7. translated text\n" +
            "8. translated textAI，translated text\n" +
            "9. translated text",
            R.mipmap.avatar_libai
        ),
        new Role(
            "translated text",
            "translated text\n" +
            "translated text\n" +
            "translated text\n" +
            "translated text",
            "translated text\n" +
            "1. translated text\n" +
            "2. translated text\n" +
            "3. translated text\n" +
            "4. translated text\n" +
            "5. translated text\n" +
            "6. translated text\n" +
            "7. translated text\n" +
            "8. translated text\n" +
            "9. translated textAI，translated text\n" +
            "10. translated text",
            R.mipmap.avatar_receptionist
        ),
        new Role(
            "translated text",
            "translated text\n" +
            "translated text\n" +
            "translated textQtranslated text\n" +
            "translated text",
            "translated text\n" +
            "1. translated text\n" +
            "2. translated text\n" +
            "3. translated text\n" +
            "4. translated text\n" +
            "5. translated text\"translated text\"\n" +
            "6. translated text\n" +
            "7. translated text\n" +
            "8. translated text\n" +
            "9. translated textAI，translated text\n" +
            "10. translated text",
            R.mipmap.avatar_luxun
        )
    );
}
```

### 3. Role Selection Page Implementation

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

        // translated textPageAgent
        pageAgent = new PageAgent(this);
        pageAgent.blockAllActions();
        pageAgent.setObjective("translated text");

        // translated textAction
        pageAgent.registerAction(new Action(
            "com.agent.role.SELECT_ROLE",
            "translated text",
            "translated text",
            Arrays.asList(
                new Parameter(
                    "role_name",
                    ParameterType.STRING,
                    "translated text",
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
                    
                    Log.d(TAG, "translated text: " + roleName);

                    // translated text
                    Role selectedRole = null;
                    for (Role role : RoleData.ROLES) {
                        if (role.getName().equals(roleName)) {
                            selectedRole = role;
                            break;
                        }
                    }
                    
                    if (selectedRole != null) {
                        // translated textChatActivity
                        Intent intent = new Intent(RoleSelectActivity.this, ChatActivity.class);
                        intent.putExtra("role", selectedRole);
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            }
        ));

        // translated textAction
        pageAgent.registerAction(Actions.SAY);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        // AgentCore APItranslated text
        AgentCore.INSTANCE.stopTTS();
        AgentCore.INSTANCE.clearContext();
        AgentCore.INSTANCE.setEnableVoiceBar(false);
        
        // translated textAgent
        StringBuilder roleInfo = new StringBuilder();
        for (int i = 0; i < RoleData.ROLES.size(); i++) {
            if (i > 0) roleInfo.append("\n");
            roleInfo.append(RoleData.ROLES.get(i).getName());
        }

        AgentCore.INSTANCE.uploadInterfaceInfo(roleInfo.toString());
        AgentCore.INSTANCE.setDisablePlan(false);
        AgentCore.INSTANCE.tts("Please select a role to try first", 20 * 1000, null);
    }
}
```

### 4. Role Conversation Page Implementation

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
    
    // translated text
    private final List<LLMMessage> conversationHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10; // translated text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // translated textRoletranslated text
        roleData = (Role) getIntent().getParcelableExtra("role");

        // translated textAppAgenttranslated text
        MainApplication app = (MainApplication) getApplicationContext();
        app.getAppAgent().setPersona(roleData.getPersona());
        app.getAppAgent().setObjective(roleData.getObjective());

        // translated textPageAgent
        pageAgent = new PageAgent(this);
        pageAgent.blockAllActions();

        String roleInfo = roleData.getName() + "\n" + roleData.getPersona() + "\n" + roleData.getObjective();
        pageAgent.setObjective(roleInfo);
        
        AgentCore.INSTANCE.uploadInterfaceInfo(" ");
        Log.d(TAG, "Create UploadInterfaceInfo:");

        // translated textAction
        pageAgent.registerAction(Actions.SAY).registerAction(Actions.EXIT);

        // translated text
        setupListeners();
    }

    /**
     * translated text
     */
    private void setupListeners() {
        // translated textAgenttranslated text
        pageAgent.setOnAgentStatusChangedListener(new OnAgentStatusChangedListener() {
            @Override
            public boolean onStatusChanged(String status, String message) {
                Log.d(TAG, "Agenttranslated text: " + status + ", message: " + message);
                return true;
            }
        });

        // translated text
        pageAgent.setOnTranscribeListener(new OnTranscribeListener() {
            @Override
            public boolean onASRResult(Transcription transcription) {
                if (!transcription.getText().isEmpty()) {
                    if (transcription.getFinal()) {
                        // translated textLLMtranslated text
                        generateRoleResponse(transcription.getText());
                    }
                }
                Log.d(TAG, "ASRtranslated text: " + transcription.getText() + ", final: " + transcription.getFinal());
                return true;
            }

            @Override
            public boolean onTTSResult(Transcription transcription) {
                if (!transcription.getText().isEmpty()) {
                    if (transcription.getFinal()) {
                        // Robot speaking; add reply to history
                        LLMMessage assistantMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.ASSISTANT, transcription.getText());
                        addToHistory(assistantMessage);
                        Log.d(TAG, "Robot reply added to history: " + transcription.getText());
                    }
                }
                Log.d(TAG, "TTStranslated text: " + transcription.getText() + ", final: " + transcription.getFinal());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        // translated text
        AgentCore.INSTANCE.uploadInterfaceInfo("");
        Log.d(TAG, "onStart UploadInterfaceInfo:");

        // translated textLLMtranslated text
        clearHistory();
        // StopTTStranslated textLLMtranslated text
        AgentCore.INSTANCE.stopTTS();
        AgentCore.INSTANCE.clearContext();

        // translated text
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
        // translated text
        clearHistory();
        // StopTTStranslated text
        AgentCore.INSTANCE.stopTTS();
        AgentCore.INSTANCE.clearContext();

        super.onDestroy();
    }

    /**
     * translated text
     */
    private void generateRoleResponse(String userQuery) {
        AsyncTaskHelper.execute(() -> {
            try {
                // translated text
                List<LLMMessage> messages = new ArrayList<>();
                
                // translated text
                messages.add(new LLMMessage(
                    com.ainirobot.agent.base.llm.Role.SYSTEM,
                    "You are now playing the role of: " + roleData.getName() + "\n" +
                    "Role profile: " + roleData.getPersona() + "\n" +
                    "Behavior guidelines: " + roleData.getObjective() + "\n" +
                    "\n" +
                    "Requirements:\n" +
                    "1. Fully immerse in the role and show its characteristics\n" +
                    "2. translated text\n" +
                    "3. translated text\n" +
                    "4. translated textAItranslated text\n" +
                    "5. translated text\n" +
                    "6. translated text\n" +
                    "7. translated text\n" +
                    "8. translated text"
                ));
                
                // translated text
                synchronized (conversationHistory) {
                    messages.addAll(conversationHistory);
                }
                
                // translated text
                LLMMessage currentUserMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.USER, userQuery);
                messages.add(currentUserMessage);

                LLMConfig config = new LLMConfig(
                    0.8f,  // temperature - translated text
                    100,   // maxTokens - translated text
                    6,     // timeout
                    false, // fileSearch
                    null   // businessInfo
                );

                // translated text
                addToHistory(currentUserMessage);
                
                // translated textonTranscribetranslated text
                AgentCore.INSTANCE.llm(messages, config, 20 * 1000, true, null);
                
                Log.d(TAG, "translated text: " + userQuery);

            } catch (Exception e) {
                Log.e(TAG, "Failed to generate reply", e);
            }
        });
    }

    /**
     * translated text
     */
    private void generateInitialIntroduction() {
        AsyncTaskHelper.execute(() -> {
            try {
                String introQuery = "Give a short self-introduction in under 30 words";
                
                // translated text
                List<LLMMessage> messages = new ArrayList<>();
                
                // translated text
                messages.add(new LLMMessage(
                    com.ainirobot.agent.base.llm.Role.SYSTEM,
                    "You are now playing the role of: " + roleData.getName() + "\n" +
                    "Role profile: " + roleData.getPersona() + "\n" +
                    "Behavior guidelines: " + roleData.getObjective() + "\n" +
                    "\n" +
                    "Now provide a brief self-introduction with these requirements:\n" +
                    "1. Fully immerse in the role and show its characteristics\n" +
                    "2. Make the introduction natural and friendly, under 30 words\n" +
                    "3. Reflect the role's personality and traits\n" +
                    "4. translated textAItranslated text\n" +
                    "5. Let users feel the role's charm"
                ));
                
                // translated text
                LLMMessage userMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.USER, introQuery);
                messages.add(userMessage);

                LLMConfig config = new LLMConfig(
                    0.8f,  // temperature
                    80,    // maxTokens - translated text
                    6,     // timeout
                    false, // fileSearch
                    null   // businessInfo
                );

                // translated text
                addToHistory(userMessage);
                
                // translated textonTranscribetranslated text
                AgentCore.INSTANCE.llm(messages, config, 20 * 1000, true, null);
                Log.d(TAG, "Initial introduction request sent");

            } catch (Exception e) {
                Log.e(TAG, "Failed to generate initial introduction", e);
            }
        });
    }
    
    /**
     * translated text
     */
    private void addToHistory(LLMMessage message) {
        synchronized (conversationHistory) {
            conversationHistory.add(message);
            Log.d(TAG, "History: " + conversationHistory);
            
            // translated text
            while (conversationHistory.size() > MAX_HISTORY_SIZE * 2) { // *2 translated text
                // translated text
                if (!conversationHistory.isEmpty() && conversationHistory.get(0).getRole() == com.ainirobot.agent.base.llm.Role.USER) {
                    conversationHistory.remove(0); // translated text
                    if (!conversationHistory.isEmpty() && conversationHistory.get(0).getRole() == com.ainirobot.agent.base.llm.Role.ASSISTANT) {
                        conversationHistory.remove(0); // translated text
                    }
                } else if (!conversationHistory.isEmpty()) {
                    // translated textUSERtranslated text
                    conversationHistory.remove(0);
                } else {
                    // translated text
                    break;
                }
            }
            
            Log.d(TAG, "History size: " + conversationHistory.size());
        }
    }
    
    /**
     * translated text
     */
    private void clearHistory() {
        synchronized (conversationHistory) {
            conversationHistory.clear();
            Log.d(TAG, "History cleared");
        }
    }

}
```

### 5. descriptionUtility Classes

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
     * translated text
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
     * translated text
     */
    public static void executeDelayed(Runnable task, long delayMillis) {
        mainHandler.postDelayed(() -> execute(task), delayMillis);
    }
}
```

### 6. AgentCore APIdescription（Javadescription）

```java
// translated text
@Override
protected void onStart() {
    super.onStart();
    
    // StopTTStranslated textLLMtranslated text
    AgentCore.INSTANCE.stopTTS();
    AgentCore.INSTANCE.clearContext();
    AgentCore.INSTANCE.setEnableVoiceBar(false);
    
    // translated text
    AgentCore.INSTANCE.uploadInterfaceInfo(roleInfo);
    AgentCore.INSTANCE.setDisablePlan(false);
    AgentCore.INSTANCE.tts("Please select a role to try first", 20 * 1000, null);
}

// translated text
@Override
protected void onDestroy() {
    AgentCore.INSTANCE.stopTTS();
    AgentCore.INSTANCE.clearContext();
    super.onDestroy();
}
```

### 7. Listen to description（Javadescription）

```java
// Agenttranslated text
pageAgent.setOnAgentStatusChangedListener(new OnAgentStatusChangedListener() {
    @Override
    public boolean onStatusChanged(String status, String message) {
        // status: "listening", "thinking", "processing", "reset_status"
        Log.d("ChatActivity", "Agenttranslated text: " + status + ", message: " + message);
        return true; // translated textUItranslated text
    }
});

// translated text
pageAgent.setOnTranscribeListener(new OnTranscribeListener() {
    @Override
    public boolean onASRResult(Transcription transcription) {
        if (!transcription.getText().isEmpty()) {
            if (transcription.getFinal()) {
                // translated text
                generateRoleResponse(transcription.getText());
            }
        }
        return true;
    }

    @Override
    public boolean onTTSResult(Transcription transcription) {
        if (!transcription.getText().isEmpty()) {
            if (transcription.getFinal()) {
                // AItranslated text
                LLMMessage assistantMessage = new LLMMessage(com.ainirobot.agent.base.llm.Role.ASSISTANT, transcription.getText());
                addToHistory(assistantMessage);
            }
        }
        return true;
    }
});
```

## Javadescription

descriptionJavadescriptionKotlindescription：

1. **AppAgentdescription** - descriptionApp-level Agentdescription
2. **PageAgentdescription** - descriptionAgentdescription  
3. **Actiondescription** - descriptionActiondescriptionexecute
4. **LLMdescription** - descriptionLLMdescription
5. **Listen to description** - ASR/TTSListen to descriptionAgentdescriptionListen to description
6. **description** - description
7. **description** - descriptionAsyncTaskHelperUtility Classesdescription

JavadescriptionAgentRoleJavadescription，descriptionAgentSDKdescriptionJavadescription。 
