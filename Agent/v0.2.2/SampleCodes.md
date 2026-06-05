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
    
    // translated textAndroidtranslated textkotlintranslated text
    // translated text
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

#### Kotlin description (app/build.gradle.kts)

```kotlin
dependencies {
    implementation("com.orionstar.agent:sdk:0.3.2-SNAPSHOT")
    
    // translated textAndroidtranslated textkotlintranslated text
    // translated text
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

        // translated textAgent
        object : AppAgent(this) {

            /**
             * AppAgent translated text
             * translated textAgent translated textAction
             */
            override fun onCreate() {
                // translated textAI translated text
                setPersona(
                    "translated text"
                )

                // translated text
                setStyle("translated text")

                // translated textAI translated text
                setObjective(
                    "translated text"
                )

                // translated textAction
                registerAction(Actions.EXIT)
            }

            /**
             * translated textAction translated text
             * translated textactionRegistry.json translated textAction translated text
             */
            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // translated textAction
                // translated textfalse translated texttrue translated text
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
        // ... translated textAndroid translated text

        // translated textAgent translated textAction
        createPageAgent()
    }

    private fun createPageAgent() {
        PageAgent(this)
            .registerAction(createSmileAction())
            .registerAction(createCryAction())
            .registerAction(createAngryAction())
    }

    /**
     * translated textAction
     */
    private fun createSmileAction(): Action {
        return Action(
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
                    showFaceImage(R.drawable.ic_smile)
                    handleAction(action, params)
                    return true
                }
            }
        )
    }

    /**
     * translated textAction
     */
    private fun createCryAction(): Action {
        return Action(
            name = "com.agent.demo.SHOW_CRY_FACE",
            displayName = "translated text",
            desc = "translated text",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "translated text",
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
     * translated textAction
     */
    private fun createAngryAction(): Action {
        return Action(
            name = "com.agent.demo.SHOW_ANGRY_FACE",
            displayName = "translated text",
            desc = "translated text",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "translated text",
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
     * translated text
     */
    private fun showFaceImage(resId: Int) {
        runOnUiThread {
            findViewById<ImageView>(R.id.face_view).setImageResource(resId)
        }
    }

    /**
     * translated textAction translated text
     */
    private fun handleAction(action: Action, params: Bundle?) {
        AOCoroutineScope.launch {
            // translated textTTS translated text
            params?.getString("sentence")?.let { 
                AgentCore.ttsSync(it) 
            }
            // translated textAction translated text
            action.notify(isTriggerFollowUp = false)
        }
    }
}
```

## AgentSDK description

### 1. Action description

```kotlin
class Action(
    name: String,           // Action translated textcom.company.action.ACTION_NAME
    displayName: String,    // translated text
    desc: String,           // translated textAI translated text
    parameters: List<Parameter>?, // translated text
    executor: ActionExecutor?     // translated text
)
```

### 2. Parameter description

```kotlin
data class Parameter(
    val name: String,           // translated text
    val type: ParameterType,    // translated text
    val desc: String,           // translated text
    val required: Boolean,      // translated text
    var enumValues: List<String>? = null // translated texttypetranslated textENUMtranslated text
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
    STRING,    // translated text
    INT,       // translated text
    FLOAT,     // translated text
    BOOLEAN,   // translated text
    ENUM       // translated text
}
```

## AgentSDK description API

### 1. Agent description

```kotlin
// TTS translated text
AgentCore.ttsSync(text: String)

// Action translated text
action.notify(isTriggerFollowUp: Boolean)
```

### 2. description

```kotlin
// translated textAgentSDK translated text
AOCoroutineScope.launch {
    // translated text
}
```

### 3. AppAgent descriptionMethods

```kotlin
// translated text
setPersona(persona: String)

// translated text
setStyle(style: String)

// translated text
setObjective(objective: String)

// translated textAction
registerAction(action: Action)
```

### 4. PageAgent description

```kotlin
// translated textAgent
PageAgent(activity: Activity)
    .registerAction(action: Action)  // translated textAction
    .blockAction(actionName: String) // translated textAction
    .blockActions(actionNames: List<String>) // translated textAction
    .blockAllActions() // translated textAction
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
