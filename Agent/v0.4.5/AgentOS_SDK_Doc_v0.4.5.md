# AgentOS SDK for APK V0.4.5 Documentation

## 📋 Version Information

![SDK](https://img.shields.io/badge/SDK_Version-v0.4.5-blue)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![API](https://img.shields.io/badge/API-26+-orange)
![Language](https://img.shields.io/badge/Language-Kotlin%20%7C%20Java-purple)

## 📚 Table of Contents

- [1. Overview](#1-overview)
  - [1.1 Environment Requirements](#11-environment-requirements)
    - [1.1.1 Development Environment](#111-development-environment)
    - [1.1.2 Runtime Environment](#112-runtime-environment)
  - [1.2 Quick Start](#12-quick-start)
    - [1.2.1 Configuring Maven Repository](#121-configuring-maven-repository)
    - [1.2.2 Adding SDK Dependency](#122-adding-sdk-dependency)
    - [1.2.3 Configuring Action Registry](#123-configuring-action-registry)
    - [1.2.4 Creating AppAgent](#124-creating-appagent)
    - [1.2.5 Creating PageAgent](#125-creating-pageagent)
    - [1.2.6 Complete Example](#126-complete-example)
    - [1.2.7 Development Summary](#127-development-summary)
- [2. Action Details](#2-action-details)
  - [2.1 Action Concept](#21-action-concept)
    - [2.1.1 Basic Properties](#211-basic-properties)
    - [2.1.2 Parameter Definition](#212-parameter-definition)
  - [2.2 Action Registration Mechanism](#22-action-registration-mechanism)
    - [2.2.1 App-Level Action](#221-app-level-action)
    - [2.2.2 Page-Level Action](#222-page-level-action)
  - [2.3 Action Execution Flow](#23-action-execution-flow)
    - [2.3.1 Execution Callback](#231-execution-callback)
    - [2.3.2 Execution Result Notification](#232-execution-result-notification)
  - [2.4 System Built-in Actions](#24-system-built-in-actions)
- [3. Agent Role Configuration - Personalize Your AI](#3-agent-role-configuration---personalize-your-ai)
  - [3.1 Overview](#31-overview)
  - [3.2 Three Core Configurations](#32-three-core-configurations)
  - [3.3 Configuration Hierarchy and Priority](#33-configuration-hierarchy-and-priority)
  - [3.4 API Interface Details](#34-api-interface-details)
  - [3.5 Extended Configuration Capabilities](#35-extended-configuration-capabilities)
  - [3.6 Configuration Best Practices](#36-configuration-best-practices)
  - [3.7 Typical Application Scenarios](#37-typical-application-scenarios)
- [4. Core API Interfaces](#4-core-api-interfaces)
  - [4.1 Microphone Control](#41-microphone-control)
  - [4.2 Wake-Free Functionality](#42-wake-free-functionality)
  - [4.3 ASR and TTS Listening](#43-asr-and-tts-listening)
  - [4.4 Agent Status Listening](#44-agent-status-listening)
  - [4.5 Voice Bar Control](#45-voice-bar-control)
  - [4.6 TTS Speech Synthesis](#46-tts-speech-synthesis)
  - [4.7 TTS Audio File Playback](#47-tts-audio-file-playback)
  - [4.8 Large Language Model Interface](#48-large-language-model-interface)
  - [4.9 Text Commands](#49-text-commands)
  - [4.10 Perception Information Reporting](#410-perception-information-reporting)
  - [4.11 Conversation History Management](#411-conversation-history-management)
  - [4.12 Disable Large Language Model Planning](#412-disable-large-language-model-planning)
  - [4.13 Application Switching](#413-application-switching)
  - [4.14 System Action Status Listening](#414-system-action-status-listening)
  - [4.15 Wake Word Mode Control](#415-wake-word-mode-control)
- [5. Advanced Features](#5-advanced-features)
  - [5.1 Annotation-Driven Action Registration](#51-annotation-driven-action-registration)
    - [5.1.1 App-Level Dynamic Registration](#511-app-level-dynamic-registration)
    - [5.1.2 Page-Level Dynamic Registration](#512-page-level-dynamic-registration)
    - [5.1.3 Annotation Class Description](#513-annotation-class-description)
- [6. Action Design Best Practices](#6-action-design-best-practices)
  - [6.1 Common Design Pitfalls](#61-common-design-pitfalls)
  - [6.2 Best Practice Principles](#62-best-practice-principles)
  - [6.3 Correct Example: Breaking Down Into Multiple Specialized Actions](#63-correct-example-breaking-down-into-multiple-specialized-actions)
  - [6.4 Design Checklist](#64-design-checklist)
- [7. Project Resources](#7-project-resources)
  - [7.1 Sample Projects](#71-sample-projects)
  - [7.2 Development Resources](#72-development-resources)
- [8. Frequently Asked Questions (FAQ)](#8-frequently-asked-questions-faq)
- [9. Technical Support](#9-technical-support)

---

# 1. Overview

AgentOS SDK is an intelligent interaction development toolkit for the Android platform, providing developers with a complete solution for building intelligent robot applications. The SDK supports both app-level and page-level Agent development, implementing core functionalities such as natural language interaction, intelligent action planning, and execution.

## 1.1 Environment Requirements

### 1.1.1 Development Environment

**Basic Requirements**
- **Android SDK**: Minimum support for API 26 (Android 8.0)
- **JDK Version**: Java 11
- **Development Language**: Kotlin / Java
- **Build Tool**: Gradle

**Recommended Development Environment**
- **Android Studio**: Used for basic environment setup and debugging
- **Cursor**: Provides AI-assisted coding capabilities, [detailed configuration guide](../../AGENTOS_CURSOR_AI_GUIDE.md)

### 1.1.2 Runtime Environment

**System Requirements**
- **AgentOS Product Version**: V1.3.0.250515
- **RobotAPI Version**: 11.3

## 1.2 Quick Start

> **Prerequisites**: If you are new to Android development, please first install [Android Studio](https://developer.android.com/studio), download the empty project template we provide, and open it with Android Studio before proceeding with the following steps.

### 1.2.1 Configuring Maven Repository

**Groovy DSL Configuration**

If your project uses Groovy build scripts, add the following configuration to the `settings.gradle` file in your project root directory:

```Groovy
dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
                mavenCentral()
                maven { // IMPORTANT: Add Maven repository
                    credentials {
                        username = "agentMaven"
                        password = "agentMaven"
                    }
                    url "https://npm.ainirobot.com/repository/maven-public/"
                } // END: Maven repository configuration
        }
}
```

**Kotlin DSL Configuration**

If your project uses Kotlin build scripts, add the following configuration to the `settings.gradle.kts` file in your project root directory:

```Kotlin
dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
                mavenCentral()
                maven { // IMPORTANT: Add Maven repository
                    credentials.username = "agentMaven"
                    credentials.password = "agentMaven"
                    url = uri("https://npm.ainirobot.com/repository/maven-public/")
                } // END: Maven repository configuration
        }
}
```

### 1.2.2 Adding SDK Dependency

> **💡 Important Update for v0.4.4**: The SDK has automatically integrated the RobotService.jar dependency. Developers no longer need to manually add the robot native API jar package. The SDK automatically manages all necessary dependencies through Maven.

**Groovy DSL Configuration**

Add the following dependency to the `app/build.gradle` file:

```Groovy
dependencies {
        implementation 'com.orionstar.agent:sdk:0.4.5-SNAPSHOT' // IMPORTANT: Agent SDK dependency

        // The following are Android standard libraries, typically included in Kotlin projects by default
        // Add the following libraries only if you encounter missing library errors during compilation
        implementation 'androidx.core:core-ktx:1.13.1'
        implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

**Kotlin DSL Configuration**

Add the following dependency to the `app/build.gradle.kts` file:

```Kotlin
dependencies {
        implementation("com.orionstar.agent:sdk:0.4.5-SNAPSHOT") // IMPORTANT: Agent SDK dependency

        // The following are Android standard libraries, typically included in Kotlin projects by default
        // Add the following libraries only if you encounter missing library errors during compilation
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.6.1")
}
```

### 1.2.3 Configuring Action Registry

Create an `assets` directory (if it doesn't exist) in the `app/src/main` directory, then create the `actionRegistry.json` file:

```JSON
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

**Configuration Explanation**
- **appId**: Unique identifier for the Agent application, must be applied for in the [reception backend](https://jiedai.ainirobot.com/web/portal/#/frame/hmag-agentos/hmag-agentos.agentapp/)
- **platform**: Platform identifier, supports `opk` or `apk`
- **actionList**: List of Actions exposed externally. Actions declared in the registry must be handled in the AppAgent's `onExecuteAction` method

> **Application Scenario**: In this example, we will develop an intelligent assistant named "Leopard Sister" with emotion perception capabilities, who can recognize user emotional changes and respond accordingly.

### 1.2.4 Creating AppAgent

> **Important Constraint**: Only one AppAgent instance can exist in an application.

Add the following code to the `onCreate` method of `MainApplication`:

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
                // Set the role persona
                setPersona("Your name is Leopard Sister, a smart, warm, and slightly witty virtual assistant.")
                // Set the role objective
                setObjective("Through natural conversation and appropriate emotional expression, make users feel understood, accompanied, and emotionally resonant, thereby enhancing communication comfort and trust.")
            }

            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // Handle statically registered actions here. Return false if you don't need to handle them, or return true if you handle them and don't need further processing.
                // Default returns false
                return false
            }
         }
    }
}
```

### 1.2.5 Creating PageAgent

> **Important Constraint**: Only one PageAgent instance can exist on each page.

**Design Philosophy**: Implementing an Emotion-Aware Intelligent Assistant

In daily communication, emotion recognition is often more important than the content of language itself. This section defines emotion perception capabilities for the assistant:

**Emotion Response Action List**
- 😊 **Positive Emotion Response** - Response when detecting user happiness, satisfaction, and other positive emotions
- 😢 **Negative Emotion Comfort** - Comfort when detecting user sadness, disappointment, and other negative emotions
- 😠 **Anger Emotion Guidance** - Guidance when detecting user anger, dissatisfaction, and other intense emotions

Add the following code to `MainActivity.kt`:

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

        // Add page-level Agent
        PageAgent(this)
            .registerAction(
                Action(
                    name = "com.agent.demo.SHOW_SMILE_FACE",
                    displayName = "Smile",
                    desc = "Respond to user happiness, satisfaction, or positive emotions",
                    parameters = listOf(
                        Parameter(
                            "sentence",
                            ParameterType.STRING,
                            "Response to say to the user",
                            true
                        )
                    ),
                    executor = object : ActionExecutor {

                        override fun onExecute(action: Action, params: Bundle?): Boolean {
                            AOCoroutineScope.launch {
                                // Display a smiling face
                                showFaceImage(R.drawable.ic_smile)
                                // Play the response to the user
                                params?.getString("sentence")?.let { AgentCore.ttsSync(it) }
                                // Report Action execution status after playback completes
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

> **Key Reminder**: After any Action execution completes, you must call the `action.notify()` method to notify the system of the execution status. For detailed information, please refer to [2.3.2 Execution Result Notification](#232-execution-result-notification).

### 1.2.6 Complete Example

The Quick Start section above provides the basic integration approach. More complete sample projects include facial expression display functionality for multiple emotional scenarios. You can download and run them directly to experience the features.

**Sample Project Address**: [AgentSDKSample](https://github.com/orionagent/AgentSDKSample)

### 1.2.7 Development Summary

**Core Development Process**
1. **SDK Integration**: Configure Maven repository, add dependencies, create Action registry
2. **Role Definition**: Define the application's persona and objectives
3. **Page Development**: Create business pages, set page-level objectives, upload page information
4. **Action System**: Define and register Actions with AgentOS SDK
   - Define Action names, descriptions, and parameters
   - Implement Action handling logic
   - Test Action execution through voice triggers

**🔥 Important Note**: After Action registration is complete, it takes effect automatically. Users can directly interact with the AI through voice. For detailed activation mechanisms, please refer to [2.2 Action Registration Mechanism](#22-action-registration-mechanism).



# 2. Action Details

## 2.1 Action Concept

The core mechanism of AgentOS is to execute corresponding skill modules through user intent recognition. These skill modules are called Actions. For example, when a user asks "Do I need to bring an umbrella to Shenzhen tomorrow?", the system recognizes the weather query intent and calls the corresponding weather query Action (such as: `orion.agent.action.WEATHER`).

### Action Planning Mechanism

The large language model (LLM) understands and selects Actions based on text descriptions, with the following priority:

**1. Action Description (`desc`)**
- The LLM primarily uses this attribute to determine whether it matches user intent
- Must clearly, accurately, and specifically describe the Action's functionality and use cases
- Avoid vague expressions to ensure the large language model understands precisely

```kotlin
// ✅ Compliant description
desc = "Query weather conditions for a specified city and date, including temperature, humidity, wind force, and other information"

// ❌ Non-compliant description
desc = "Weather"  // Too simple, lacks specific functionality explanation
desc = "Handle user requests"  // Too generalized, unable to distinguish specific functionality
```

**2. Action Name (`name`)**
- Serves as auxiliary judgment basis, supplementing LLM understanding
- Should have clear business meaning, avoiding generic naming
- Multiple Actions' names should have obvious differentiation

```kotlin
// ✅ Compliant names
name = "com.example.weather.QUERY_WEATHER"
name = "com.example.music.PLAY_SONG"

// ❌ Non-compliant names
name = "ACTION1"  // No business meaning
name = "HANDLE"   // Too generic
name = "com.example.DO_SOMETHING"  // Unclear specific functionality
```

**3. Parameter Description (`Parameter.desc`)**
- Affects the LLM's ability to understand and accurately extract parameter values
- Each parameter's description must precisely reflect its definition and purpose
- Helps the LLM correctly extract corresponding information from user input

```kotlin
// ✅ Compliant parameter descriptions
Parameter("city", ParameterType.STRING, "City name for weather query, such as Beijing, Shanghai", false)
Parameter("song_name", ParameterType.STRING, "Name of the song to play", true)

// ❌ Non-compliant parameter descriptions
Parameter("city", ParameterType.STRING, "City", false)  // Too simple
Parameter("data", ParameterType.STRING, "Data", false)  // Unclear meaning
Parameter("param1", ParameterType.STRING, "Parameter 1", false)  // No actual meaning
```

> **Core Principle**: All attributes described through text directly impact the LLM's understanding and planning accuracy. Pay attention to the quality of every description.

### Naming Specification Constraints

**Action Name Specification**
- **Format Requirement**: `com.company.module.ACTION_NAME` (company domain + module + Action brief name)
- **Brief Name Capitalization**: The Action brief name portion must use uppercase letters
- **Business Meaning**: Names should have clear business meaning, avoiding generic naming
- **Uniqueness**: Ensure uniqueness within the application, avoiding conflicts with Actions from other apps

```kotlin
// ✅ Compliant
"com.example.weather.QUERY_WEATHER"
"com.example.music.PLAY_SONG"

// ❌ Non-compliant
"com.example.weather.query_weather"  // Brief name not capitalized
"ACTION1"  // No domain prefix, no business meaning
```

**Parameter Name Specification**
- **Language Requirement**: Use English naming
- **Connection Method**: Connect multiple words with underscores
- **Avoid Conflicts**: Must not be the same as or similar to Action or Parameter object attribute names
- **Clear Semantics**: Names should clearly express parameter purpose

```kotlin
// ✅ Compliant
"city_name", "start_date", "user_id"

// ❌ Non-compliant
"cityName"  // Uses camelCase naming
"name"      // Conflicts with Parameter object attribute names
"data1"     // No clear semantics
```

### 2.1.1 Basic Properties

The core properties of the Action class are defined as follows:

```Kotlin
package com.ainirobot.agent.action

open class Action(
    /**
      * Full action name to avoid conflicts with actions from other apps
      */
      name: String,
    /**
      * Current application's appId
      */
      appId: String,
    /**
      * Action display name, may be used on UI interfaces, can be in any language
      */
      displayName: String,
    /**
      * Action description, used to help the large language model understand when to call this action
      */
      desc: String,
    /**
      * Description of expected parameters when the action is planned
      */
      parameters: List<Parameter>?,
    /**
      * Executor corresponding to the action, invoked after action planning completes
      */
      @Transient
    var executor: ActionExecutor?
): ActionEntity(...), Parcelable {

    /**
     * Action ID of the planned action, used to identify action uniqueness.
     * Each planned action returns a different actionId.
     */
    var sid: String = ""

    /**
     * User question that triggered planning
     */
    var userQuery: String = ""
}
```

**Best Practice**: When creating an Action, clearly and accurately describe each property so the large language model can accurately understand the Action's functionality and make precise choices.

**Key Requirements**:
- **Concrete Descriptions**: The `desc` property must detail the Action's specific functionality and use scenarios
- **Avoid Ambiguity**: Multiple Actions' descriptions must not be too similar, ensuring the large language model can accurately distinguish between them

### 2.1.2 Parameter Definition

Action parameters are used to extract key information from user queries. For example, from the query "I want to check the weather in Beijing today", we can extract the `city` and `date` parameters.

Parameter object properties are defined as follows:

```kotlin
data class Parameter(
    /**
      * Parameter name
      */
    val name: String,
    /**
      * Parameter type
      */
    val type: ParameterType,
    /**
      * Parameter description
      */
    val desc: String,
    /**
      * Whether this is a required parameter
      */
    val required: Boolean,
    /**
      * When type is enum, pass this parameter as the list of enumeration value options
      */
    var enumValues: List<String>? = null
)
```

**Weather Query Example**

```kotlin
val weatherAction = Action(
    "orion.agent.action.WEATHER",
    "Query Weather",
    "Query weather conditions for a specified city and date",
    parameters = listOf(
        Parameter(
            "city",
            ParameterType.STRING,
            "City name for weather query",
            false
        ),
        Parameter(
            "date",
            ParameterType.STRING,
            "Date to query weather for, such as today, tomorrow, the day after tomorrow",
            false
        )
    ),
    executor = object : ActionExecutor {
        override fun onExecute(action: Action, params: Bundle?): Boolean {
            // Get parameters
            val city = params?.getString("city") ?: "Current Location"
            val date = params?.getString("date") ?: "Today"

            // Handle weather query logic
            // ...

            return true
        }
    }
)
```

**Parameter Parsing Example**

After successful Action registration, when a user says to the robot "What's the weather like in Beijing tomorrow?", the large language model automatically plans the weather Action and invokes the `onExecute` method, passing the parsed parameters:

```
Parsed parameter values:
city: 'Beijing'
date: 'Tomorrow'
```

## 2.2 Action Registration Mechanism

> **⚠️ Important Reminder:**
>
> **Action Registration Impact**: If no Actions are registered, the large language model cannot plan any Actions to respond to user requests.
>
> **Typical Example**: Without registering the `Actions.SAY` system Action, the robot cannot answer user questions or ask clarification questions through TTS.
>
> **Development Choice**: Developers can choose to register corresponding Actions based on business needs, or call the large language model directly through `AgentCore.llm()` for complex scenarios where Actions cannot meet the requirements.

Actions are divided into two types based on their lifecycle: App-level and Page-level, with different activation periods.

### Action Activation Mechanism

**Automatic Lifecycle Management**: The SDK automatically monitors application and page lifecycle changes, enabling or disabling Actions at appropriate times:

- **AppAgent Activation Timing**:
  - Actions automatically take effect when the application comes to the foreground
  - Actions automatically stop taking effect when the application exits or goes to the background

- **PageAgent Activation Timing**:
  - Actions automatically take effect when the page becomes visible to the user
  - Actions automatically stop taking effect when the page is not visible

**Voice Interaction Flow**: After Actions take effect, users can directly interact with the AI through voice without additional operations:
1. User voice input → 2. Large language model understands intent → 3. Automatically plans Action → 4. Executes corresponding logic

**Action Planning Mechanism**:
- Once an Action is successfully registered, when the user provides voice input (with microphone open) or calls `AgentCore.query()`, the LLM automatically plans to the highest matching Action
- The LLM determines the match degree based on the Action's description, name, and parameter descriptions, selecting the Action that best matches user intent for execution

**Important Constraints**:
- **Planning Scope Limitation**: The LLM can only plan to Actions currently in effect. Expired Actions cannot be planned

### 2.2.1 App-Level Action

App-level Actions are global Actions that respond to user requests throughout the application runtime (when in foreground). When the application exits or goes to the background, these Actions will no longer respond. App-level Actions support both **dynamic registration** and **static registration** methods.

#### Dynamic Registration

Dynamically registered app-level Actions respond to user's real-time intents throughout the application lifecycle. Registration must be called in the AppAgent's `onCreate` method.

**Example: Registering an Exit Action**

```Kotlin
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Actions

// Add app-level Agent
object : AppAgent(this) {

    /**
      * Callback for AppAgent initialization
      * App-level Actions that need dynamic registration can be registered in this method
      */
      override fun onCreate() {
        // Dynamically register Action
        // Example: Register system Action EXIT here. When user says "exit", it triggers a BACK event
        registerAction(Actions.EXIT)
    }
}
```

**External Action Registration**

Dynamic registration supports registering both private Actions of the current application and external Actions (such as system Actions or Actions statically registered in other AgentOS applications).

**Example: Registering Weather Application Action**

```Kotlin
import android.os.Bundle
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor

// Register a weather query Action, provided you have installed an AgentOS application containing this Action
// (must be statically registered in the registry to be available)
registerAction(
    Action("com.agent.tool.WEATHER_HOME").also  {
         it.executor = object : ActionExecutor {

            override fun onExecute(action: Action, params: Bundle?): Boolean {
                // Return false if you don't need to handle it. Return true if you handle it and don't need further processing.
                // This only logs a message, so it returns false, meaning it will launch the weather app to query weather
                // If you want to query weather yourself, you would call your weather query interface here and return true
                println("User just queried weather")
                return false
            }
        }
    }
)
```

#### Static Registration

Static registration is specifically for providing external calling interfaces to the current application. Statically registered Actions do not take effect during the current application's runtime by default. To make them effective in the current application, you need to perform dynamic registration again.

**Configuration Requirements**
- Only App-level Actions can be statically registered
- Must be configured in the `actionRegistry.json` registry
- Requires detailed parameter descriptions

**Example: Static Registration Configuration for Weather Application**

```JSON
{
  "appId": "app_43e38d01cfad05d3bb2d8ce3a66f7aa2",
  "platform": "apk",
  "actionList": [
    {
      "name": "com.agent.tool.WEATHER_HOME",
      "displayName": "Open weather query homepage",
      "desc": "Display weather information when user wants to inquire about weather",
      "parameters": [
        {
          "name": "city",
          "type": "string",
          "desc": "Which city's weather to query",
          "required": false
        },
        {
          "name": "date",
          "type": "string",
          "desc": "What date's weather to query",
          "required": false
        }
      ]
    }
  ]
}
```

> **Parameter Explanation**: `required` being `false` indicates the parameter is optional. When the parameter is empty, the executor must handle it (such as using the current location city, defaulting to today's date, etc.).

**Execution Handling for Statically Registered Actions**

Statically registered Actions must be handled in the AppAgent's `onExecuteAction` method:

```Kotlin
import android.os.Bundle
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action

object : AppAgent(this) {
    /**
      * Callback for executing statically registered actions from the actionRegistry.json registry
      * Note: Only actions that can be called externally can use static registration,
      * and this method only executes when called externally (by other apps)
      */
      override fun onExecuteAction(
        action: Action,
        params: Bundle?
    ): Boolean {
        return when (action.name) {
            "com.agent.tool.WEATHER_HOME" -> {
                // Open weather homepage
                startWeatherHomePage(action, params)
                true
            }
            else -> false
        }
    }
}
```

> **Registration Method Summary**:
> - **Static Registration**: For providing external calling interfaces
> - **Dynamic Registration**: For internal application calling

### 2.2.2 Page-Level Action

Page-level Actions must be declared during page (Activity or Fragment) initialization and only take effect when the current page is visible to the user. When the page exits or is covered by another page, these Actions will no longer take effect.

#### Dynamic Registration

Since Page-level Actions only take effect on the current page, they **can only be dynamically registered and cannot be statically registered in the registry**, meaning they cannot provide external interfaces.

**Example: Emotion Response Action Registration**

```kotlin
PageAgent(this)
    .blockAction("com.xxx.yyy.TTT") // Exclude specified Action
    .blockActions( // Exclude specified Action list
        listOf(
            "com.xxx.yyy.TTT",
            "com.xxx.yyy.RRR",
        )
    )
    .blockAllActions() // Exclude all Actions
    .registerAction(
        Action(
            name = "com.agent.demo.SHOW_SMILE_FACE",
            displayName = "Smile",
            desc = "Respond to user happiness, satisfaction, or positive emotions",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "Response to say to the user",
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
            displayName = "Cry",
            desc = "Respond to user sadness, disappointment, or help-seeking emotions",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "Response to say to the user, providing comfort",
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
            displayName = "Angry",
            desc = "Respond to user anger, dissatisfaction, or complaint emotions",
            parameters = listOf(
                Parameter(
                    "sentence",
                    ParameterType.STRING,
                    "Response to say to the user, trying to eliminate user's negative emotions",
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

#### Action Filtering Mechanism

When you have registered multiple global Actions at the App-level but don't want certain global Actions to be triggered on specific pages, you can use the following filtering methods:

**Filter Single Action**

```Kotlin
// Filter out specified global Action, parameter is Action's name
pageAgent.blockAction("com.xxx.yyy.TTT")
```

**Filter Multiple Actions**

```Kotlin
// List of Actions to filter
pageAgent.blockActions(
    listOf(
        "com.xxx.yyy.TTT",
        "com.xxx.yyy.RRR",
    )
)
```

**Filter All Actions**

```Kotlin
// Filter out all global Actions registered in AppAgent
// Only page-level registered Actions take effect
pageAgent.blockAllActions()
```

## 2.3 Action Execution Flow

### 2.3.1 Execution Callback

> **⚠️ Important Note:**
> 1. **RobotOS Migration**: If you were previously using the RobotOS system, the original domains and skills need to be migrated to AgentOS's Action callbacks and re-implemented
> 2. **Functionality Implementation**: AgentOS will not automatically call the Xiaobao application or system components. All functionalities (such as weather queries, patrols, etc.) must be implemented by developers in Actions

**Execution of Dynamically Registered Actions**

Dynamically registered Actions require setting an executor, where specific business logic is implemented:

```Kotlin
import android.os.Bundle
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor
import com.ainirobot.agent.base.Parameter
import com.ainirobot.agent.base.ParameterType

Action(
    name = "com.agent.demo.SHOW_ANGRY_FACE",
    displayName = "Angry",
    desc = "Respond to user anger, dissatisfaction, or complaint emotions",
    parameters = listOf(
        Parameter(
            "sentence",
            ParameterType.STRING,
            "Response to say to the user, trying to eliminate user's negative emotions",
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

**Execution of Statically Registered Actions**

Statically registered Actions must be handled in the AppAgent's `onExecuteAction` method:

```Kotlin
override fun onExecuteAction(
    action: Action,
    params: Bundle?
): Boolean {
    return false
}
```

> **Return Value Explanation**:
> - `true`: Indicates custom handling of this Action, no further processing needed
> - `false`: Indicates not handling this Action, system continues processing

### 2.3.2 Action Execution Completion Notification

> **🚨 Critical Warning**
> **The following rules are critical for normal Action system operation. Developers must strictly follow them, as violations will cause system abnormalities:**

**Execution Constraints**
1. **Time-consuming operations cannot be executed in the Action execution callback method**
2. **When handling an Action, in addition to returning `true`, you must call the `action.notify()` method after the Action execution completes**
3. **The execution callback method runs on a worker thread by default**

**Correct Handling of Time-Consuming Operations**
- The `onExecute` method should return `true` immediately
- Place time-consuming operations (such as network requests, file operations, TTS playback, etc.) in coroutines or threads
- After time-consuming operations complete, call the `action.notify()` method to notify the system of the execution result

**notify() Method Explanation**

```Kotlin
package com.ainirobot.agent.action

/**
  * Notify the system when Action execution completes
  * Must be called after Action processing finishes to report completion status and result
  *
  *  @param  result Action execution result (default: SUCCEEDED)
  *  @param  isTriggerFollowUp Whether to proactively guide user for next steps after completion (default: false)
  */
fun notify(
    result: ActionResult = ActionResult(ActionStatus.SUCCEEDED),
    isTriggerFollowUp: Boolean = false
)
```

**❌ Incorrect Examples**

**Kotlin Version**

```Kotlin
class MyActionExecutor : ActionExecutor {

    override fun onExecute(action: Action, params: Bundle?): Boolean {
        // ❌ Incorrect: Executing time-consuming operations directly in onExecute
        Thread.sleep(3000) // 3-second time-consuming operation

        // Notify completion
        action.notify()
        return true

        // This method takes 3 seconds total, but will be forcibly interrupted at 2 seconds!
    }
}
```

**Java Version**

```Java
@Override
public boolean onExecute(Action action, Bundle params) {
    // ❌ Incorrect: Executing time-consuming operations directly in onExecute
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    // Notify completion
    action.notify();
    return true;
}
```

**✅ Correct Examples**

**Kotlin Version**

```Kotlin
class MyActionExecutor : ActionExecutor {

    override fun onExecute(action: Action, params: Bundle?): Boolean {
        // ✅ Correct: Immediately launch coroutine for time-consuming operation
        AOCoroutineScope.launch {
            try {
                // Execute time-consuming operation in coroutine
                delay(3000) // 3-second time-consuming operation

                // Notify execution result after completion
                action.notify()

            } catch (e: Exception) {
                action.notify(ActionResult(ActionStatus.FAILED))
            }
        }

        // Return true immediately, don't wait for time-consuming operation
        return true
    }
}
```

**Java Version**

```Java
@Override
public boolean onExecute(Action action, Bundle params) {
    // Launch background task immediately
    new Thread(() -> {
        try {
            Thread.sleep(5000); // Time-consuming operation executes in background
            action.notify(); // Notify after completion
        } catch (Exception e) {
            action.notify(new ActionResult(ActionStatus.FAILED));
        }
    }).start();

    return true; // Return immediately without blocking
}
```

## 2.4 System Built-in Actions

System built-in Actions are predefined functional modules in AgentOS, including system functions, commands, and applications. The namespace for system Actions is `orion.agent.action`.

> **Note**: Not all system Actions have implementation by the system. Some Actions (such as `orion.agent.action.CLICK` click events) still require users to handle them.

**Actions Automatically Handled by System**

```Kotlin
package com.ainirobot.agent.action

object Actions {
    /**
      * Adjust system volume
      */
    const val SET_VOLUME = "orion.agent.action.SET_VOLUME"
    /**
      * Robot fallback response
      */
    const val SAY = "orion.agent.action.SAY"
    /**
      * Cancel
      */
    const val CANCEL = "orion.agent.action.CANCEL"
    /**
      * Back
      */
    const val BACK = "orion.agent.action.BACK"
    /**
      * Exit
      */
    const val EXIT = "orion.agent.action.EXIT"
    /**
      * Knowledge base question answering
      */
    const val KNOWLEDGE_QA = "orion.agent.action.KNOWLEDGE_QA"
    /**
      * Generate welcome or farewell message for user
      */
    const val GENERATE_MESSAGE = "orion.agent.action.GENERATE_MESSAGE"
    /**
      * Adjust robot speed
      */
    const val ADJUST_SPEED = "orion.agent.action.ADJUST_SPEED"
}
```

> **Default Behavior**: `CANCEL`, `BACK`, and `EXIT` are handled by default as simulating a Back key press.

### Knowledge Base Question Answering Usage Example

Using `KNOWLEDGE_QA` as an example, explaining how to register a system built-in Action and the trigger mechanism for knowledge base Q&A:

```Kotlin
registerAction(Actions.KNOWLEDGE_QA)
```

After developers register this Action, when users query the robot through voice and hit the knowledge base configured in the reception backend, it executes automatically. The system automatically handles knowledge base queries and plays back the configured TTS responses.

**Actions Requiring User Handling**

```Kotlin
package com.ainirobot.agent.action

object Actions {
    /**
      * Confirm
      */
    const val CONFIRM = "orion.agent.action.CONFIRM"
    /**
      * Click
      */
    const val CLICK = "orion.agent.action.CLICK"
}
```

> **Related Functionality**: For monitoring system Action execution status, please refer to [System Action Status Listening](#414-system-action-status-listening).

# 3. Agent Role Configuration - Personalize Your AI

## 3.1 Overview

AgentOS SDK provides powerful role configuration features, allowing you to customize unique identity, style, and objectives for your AI assistant. Through three core configuration methods, you can create an intelligent assistant with distinctive personality.

## 3.2 Three Core Configurations

### Persona Configuration (setPersona)
**Function**: Define the AI assistant's identity characteristics and basic attributes
**Focus**: Static identity information, answering "who am I"
**Application Scenarios**: Character identity, personality traits, background settings

### Style Configuration (setStyle)
**Function**: Set the AI assistant's conversation style and expression method
**Focus**: Communication method, answering "how to speak"
**Application Scenarios**: Language style, expression method, emotional tone

### Objective Configuration (setObjective)
**Function**: Clarify the AI assistant's task objectives and behavioral principles
**Focus**: Business processes and special requirements, answering "what to do"
**Application Scenarios**: Business objectives, behavioral norms, special requirements

## 3.3 Configuration Hierarchy and Priority

### Configuration Hierarchy
```
PageAgent Configuration > AppAgent Configuration > System Default
```

**Explanation**: When PageAgent does not set a configuration, it automatically inherits the AppAgent's configuration

## 3.4 API Interface Details

### setPersona()
```kotlin
setPersona(persona: String): Agent
```

**Parameter Explanation:**
- `persona: String` - Persona description, such as: "Your name is Xiaobao, a chatbot"

### setStyle()
```kotlin
setStyle(style: String): Agent
```

**Parameter Explanation:**
- `style: String` - Conversation style, such as: "professional, friendly, humorous"

### setObjective()
```kotlin
setObjective(objective: String): Agent
```

**Parameter Explanation:**
- `objective: String` - Planning objective, must be clear and definite for easy understanding by the large language model

> **Note**: All three methods support method chaining and can be used in both AppAgent and PageAgent. For specific usage examples, please refer to the Quick Start section earlier.

## 3.5 Extended Configuration Capabilities

When basic three-item configurations cannot meet complex requirements, you can combine with **Perception Information Reporting** functionality (see [section 4.10](#410-perception-information-reporting)) for more flexible information transfer:

```kotlin
// Upload complex page information and business status
val pageInfo = """
Current Product: ${product.name}
User Status: ${user.memberLevel} Member
Special Tip: This product is on promotion
"""

AgentCore.uploadInterfaceInfo(pageInfo)
```

> **Tip**: For detailed usage of the `uploadInterfaceInfo` method, please refer to [4.10 Perception Information Reporting](#410-perception-information-reporting) section.

## 3.6 Configuration Best Practices

### Core Principles
- **Specific Persona**: Use concrete character definitions, avoid abstract descriptions
- **Consistent Style**: Maintain style consistency throughout the application
- **Clear Objectives**: Ensure the AI understands specific business objectives

### Example Comparison

**❌ Not Recommended**
```kotlin
setPersona("You are an assistant")
setObjective("Help users")
```

**✅ Recommended**
```kotlin
setPersona("You are the exclusive shopping consultant for 'Leopard Shopping' with 3 years of e-commerce experience")
setObjective("Based on user preferences and budget, recommend the most suitable products and provide purchase advice")
```

## 3.7 Typical Application Scenarios

### E-commerce Application
```kotlin
// Global configuration
setPersona("Senior shopping consultant")
setObjective("Help users make wise purchasing decisions")

// Page-level configuration
// Product List Page: Help users quickly filter products
// Detail Page: Provide detailed product feature introduction
// Shopping Cart: Assist in completing order confirmation
```

### Educational Application
```kotlin
// Global configuration
setPersona("Experienced educational assistant")
setStyle("Patient, encouraging, step-by-step")
setObjective("Help users master knowledge, inspire learning interest")

// Page-level configuration
// Course Page: Guide completion of current course learning
// Practice Page: Provide exercise answer and learning suggestions
```

---

# 4. Core API Interfaces

## 4.1 Microphone Control

### Feature Introduction
Microphone control provides precise management of audio input. By default, applications integrating AgentOS SDK automatically open the microphone when entering the foreground and automatically close it when the application exits or goes to the background. Developers can also manually control microphone status based on business needs.

### Application Scenarios
- Scenarios requiring temporary muting
- Preventing audio interference when playing audio/video content
- Audio control requirements in specific business processes

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

// Set microphone mute status
AgentCore.isMicrophoneMuted = true // Mute
AgentCore.isMicrophoneMuted = false // Unmute
```

> **🔍 Troubleshooting**: If experiencing voice interaction no response issues, please refer to [FAQ - Voice Interaction No Response](../../FAQ.md#voice-interaction-no-response) for self-checking

## 4.2 Wake-Free Functionality

### Feature Introduction
Wake-free is AgentOS's intelligent microphone control feature, implementing precise voice interaction through **multimodal intelligent recognition** (combining visual and acoustic signals):

**Enabled State** (default):
- Adopts **vision and acoustic fusion algorithm**, comprehensively analyzing user interaction intent
- Activates microphone only when user is **facing the robot**; side or back-facing speech will not be picked up
- Significantly reduces **crosstalk interference** and false triggers in multi-user scenarios

**Disabled State**:
- Switches to **unconditional listening** mode, continuously monitoring environmental audio
- ⚠️ **Multi-user scenario risk**: Simultaneous dialogue by different users easily produces speech conflicts and misrecognition
- Suitable for single-user or scenarios needing broader listening range

### Application Scenarios
- **Recommended to Enable**: Exhibition halls, lobbies and other multi-person environments, ensuring service accuracy
- **Caution to Disable**: Private spaces or specific application scenarios requiring long-distance listening

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Whether to enable wake-free functionality, default true, enabled
 */
var isEnableWakeFree: Boolean
```

## 4.3 ASR and TTS Listening

### Feature Introduction
By setting listeners, developers can obtain real-time content and status of speech recognition (ASR) and speech synthesis (TTS), achieving comprehensive monitoring of the voice interaction process.

### Application Scenarios
- Need to obtain speech recognition results for business processing
- Monitor TTS playback content and status
- Implement custom voice interaction UI

### API Interface

**Set Listener**

```Kotlin
import com.ainirobot.agent.OnTranscribeListener

/**
 * Set ASR and TTS listener
 */
fun setOnTranscribeListener(listener: OnTranscribeListener): Agent
```

**Listener Interface Definition**

```Kotlin
import com.ainirobot.agent.base.Transcription

/**
 * Listen to ASR and TTS output
 */
interface OnTranscribeListener {

    fun onASRResult(transcription: Transcription): Boolean

    fun onTTSResult(transcription: Transcription): Boolean

}
```

> **Note**: `setOnTranscribeListener` is a member method of AppAgent and PageAgent.

### Usage Explanation

**Get Content**
- `transcription.text`: Get text content

**Determine Result Type**
- `transcription.final`: `true` indicates final result, `false` indicates intermediate result

**Return Value Handling**
- Return `true`: Indicates consuming this result, system will no longer display subtitles to the bottom subtitle bar
- Return `false`: Indicates listening only without interception, not affecting system subsequent processing (recommended)

> **Thread Reminder**: `onTranscribe` callback executes on a worker thread.

## 4.4 Agent Status Listening

### Feature Introduction
Agent status listening provides real-time monitoring of Agent's thinking and processing process, helping developers understand Agent's working status and implement corresponding UI feedback.

### API Interface

```Kotlin
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.OnAgentStatusChangedListener

PageAgent(this)
    .setOnAgentStatusChangedListener(object : OnAgentStatusChangedListener {

        override fun onStatusChanged(status: String, message: String?): Boolean {
            // Display different UI based on different statuses here. Note: currently on worker thread
            // Return true if you don't want to display status on default voice bar, false to keep system display
            return true
        }
    })
```

**Listener Interface Definition**

```Kotlin
/**
 * Agent status change listener
 */
interface OnAgentStatusChangedListener {

    fun onStatusChanged(status: String, message: String?): Boolean

}
```

### Status Explanation

**Status Types**
- `listening`: Listening for user input
- `thinking`: Thinking, analyzing user intent
- `processing`: Processing, executing relevant operations
- `reset_status`: Status reset, returning to initial state

**Message Explanation**
- When `status` is `processing`, `message` may contain specific processing information, such as: "Selecting tool...", "Getting weather...", "Summarizing answer...", etc.
- For other statuses, `message` is empty

## 4.5 Voice Bar Control

### Feature Introduction
Control the display and hiding of system's default voice bar to meet UI needs of different application scenarios.

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Whether to enable voice bar, enabled by default
 */
var isEnableVoiceBar: Boolean
```

## 4.6 TTS Speech Synthesis

### Feature Introduction
Actively call the system's speech synthesis interface to convert specified text to audio and automatically play it, supporting both synchronous and asynchronous calling methods.

### Application Scenarios
- Play welcome message when application launches
- Voice prompts in business processes
- Proactive voice interaction with users
- Real-time dialogue and continuous playback
- Most voice playback requirements

### API Interface

**Synchronous Calling Interface**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * TTS interface, synchronous calling
 * Note: This interface must be called in a coroutine
 *
 * @param text Text to play
 * @param timeoutMillis Timeout duration in milliseconds
 *
 * @return TaskResult<String> Task execution result, status=1 indicates success, status=2 indicates failure
 */
suspend fun ttsSync(text: String, timeoutMillis: Long = 180000): TaskResult<String>
```

**Asynchronous Calling Interface**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * TTS interface, asynchronous calling, execution status returned via TTSCallback
 *
 * @param text Text to play
 * @param timeoutMillis Timeout duration in milliseconds
 * @param callback Callback, status=1 indicates successful playback, status=2 indicates playback failure
 */
fun tts(
    text: String,
    timeoutMillis: Long = 180000,
    callback: TTSCallback? = null
)
```

### ⚠️ Important Notes

> **Playback Behavior Characteristics**
> - **Automatic Append Playback**: Multiple calls to `tts()` or `ttsSync()` append audio content to the playback queue without interrupting the previous playback
> - **Queue-Based Playback**: Suitable for scenarios of continuous playback of multiple segments

> **Playback Interruption and Callback Mechanism**
> - When TTS playback is terminated or interrupted in any way, it triggers a playback failure callback (`status=2`)
> - User speaking to the robot during TTS playback triggers speech recognition, **automatically interrupting current TTS playback**

> **Timeout Duration Explanation**
> - TTS uses **streaming playback** mechanism, generating and playing audio simultaneously, not a pre-generated file mode
> - `timeoutMillis` parameter is **total playback timeout duration**, from calling start to playback completion
> - ⚠️ **Setting timeout too short may interrupt long text playback**. Recommend setting timeout reasonably based on text length


**Stop Playback**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Force interrupt TTS playback
 */
fun stopTTS()
```

## 4.7 TTS Audio File Playback

### Feature Introduction
TTS audio file playback provides a pre-generated audio file-based speech synthesis solution. Unlike streaming TTS, this function downloads complete audio files from the network first, then plays them. It features that once download completes, playback process is no longer affected by network fluctuations.

### Technical Features
- **Network Stability**: After audio download completes, playback is unaffected by network fluctuations
- **Complete Lifecycle**: Provides detailed callback chain from network request to playback completion
- **Replacement Playback**: Each call interrupts the previous playback

### Application Scenarios
- Network environment unstable special scenarios
- Applications needing detailed playback status monitoring

### API Interface

**Asynchronous Playback Interface**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * TTS audio file playback interface, asynchronous calling
 * Downloads audio file from network and plays it, supporting complete playback lifecycle callbacks
 *
 * @param text Text to play
 * @param timeoutMillis Timeout duration in milliseconds
 * @param callback Complete playback lifecycle callback
 * @param showCaption Whether to display text in subtitle bar, default true
 */
fun playTtsAudioFile(
    text: String,
    timeoutMillis: Long = 15000,
    callback: TTSAudioFileCallback? = null,
    showCaption: Boolean = true
)
```

**Stop Playback Interface**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Force interrupt TTS audio file playback
 */
fun stopTtsAudioFile()
```

### Callback Interface Definition

> **💡 Interface Feature**: All methods in the `TTSAudioFileCallback` interface have default empty implementations. Developers can selectively override only the methods they care about, without needing to implement all methods.

```Kotlin
import com.ainirobot.agent.base.ITaskCallback

/**
 * TTS audio file playback callback interface
 * Provides complete playback lifecycle callbacks
 *
 * Note: All callback methods have default empty implementations. Developers only need to override the methods they care about.
 */
interface TTSAudioFileCallback : ITaskCallback<String> {

    /**
     * Network request started
     * @param text Text to play
     */
    fun onRequestStart(text: String) {}

    /**
     * Network request successful, audio data download complete
     * @param text Text being played
     * @param audioSize Audio data size in bytes
     */
    fun onRequestSuccess(text: String, audioSize: Int) {}

    /**
     * Network request failed
     * @param text Text to play
     * @param error Error message
     */
    fun onRequestFailed(text: String, error: String) {}

    /**
     * Audio preparation complete, about to start playback
     * @param text Text to play
     */
    fun onPlaybackPrepared(text: String) {}

    /**
     * Audio playback started
     * @param text Text being played
     */
    fun onPlaybackStarted(text: String) {}

    /**
     * Audio playback completed
     * @param text Text that was played
     */
    fun onPlaybackCompleted(text: String) {}

    /**
     * Audio playback interrupted externally
     * @param text Text being played
     */
    fun onPlaybackInterrupted(text: String) {}

    /**
     * Audio playback error
     * @param text Text being played
     * @param what Error type
     * @param extra Additional error information
     */
    fun onPlaybackError(text: String, what: Int, extra: Int) {}

    /**
     * ITaskCallback implementation, only to satisfy interface requirements
     *
     * Note: In current implementation, this method will never be called because:
     * - TTSAudioFileCallback is only used for playTtsAudioFile command
     * - playTtsAudioFile command uses detailed lifecycle callbacks
     * - Only streaming TTS etc. commands call onTaskEnd
     *
     * This method exists only because ITaskCallback<String> is abstract interface requiring implementation
     */
    override fun onTaskEnd(status: Int, result: String?) {
        // Empty implementation - this method never called in current architecture
    }
}
```

### Usage Example

```Kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.base.TTSAudioFileCallback

// Basic usage
AgentCore.playTtsAudioFile("Welcome to AgentOS SDK")

// Playback behavior demonstration - subsequent call interrupts previous one
AgentCore.playTtsAudioFile("First audio segment with longer playback time...")
Thread.sleep(1000) // Wait 1 second
AgentCore.playTtsAudioFile("Second audio segment") // Immediately interrupts first segment playback

// Simple callback usage - only care about completion
AgentCore.playTtsAudioFile(
    text = "Will have prompt after playback completes",
    callback = object : TTSAudioFileCallback {
        override fun onPlaybackCompleted(text: String) {
            println("Playback complete: $text")
            // Execute subsequent logic
        }

        override fun onPlaybackError(text: String, what: Int, extra: Int) {
            println("Playback error: $text")
        }
    }
)

// Complete callback usage
AgentCore.playTtsAudioFile(
    text = "Playing important notification for you",
    timeoutMillis = 30000,
    showCaption = true,
    callback = object : TTSAudioFileCallback {

        override fun onRequestStart(text: String) {
            println("Starting audio file request: $text")
        }

        override fun onRequestSuccess(text: String, audioSize: Int) {
            println("Audio download successful: $text, size: ${audioSize} bytes")
        }

        override fun onRequestFailed(text: String, error: String) {
            println("Audio download failed: $text, error: $error")
        }

        override fun onPlaybackPrepared(text: String) {
            println("Audio preparation complete: $text")
        }

        override fun onPlaybackStarted(text: String) {
            println("Starting playback: $text")
        }

        override fun onPlaybackCompleted(text: String) {
            println("Playback complete: $text")
        }

        override fun onPlaybackInterrupted(text: String) {
            println("Playback interrupted: $text")
        }

        override fun onPlaybackError(text: String, what: Int, extra: Int) {
            println("Playback error: $text, what: $what, extra: $extra")
        }
    }
)
```

### Difference from Streaming TTS

| Feature | Streaming TTS (section 4.6) | TTS Audio File Playback (section 4.7) |
|---------|---------------------------|-------------------------------------|
| **Playback Mode** | Streaming, generate and play simultaneously | Pre-generated file, playback after download |
| **Playback Behavior** | Automatically append, no previous playback interruption | Each call interrupts previous playback |
| **Network Dependency** | Continuous network connection | One-time download |
| **Playback Stability** | Affected by network fluctuations | Stable playback after download completes |
| **Lifecycle Callback** | Simple success/failure callback | Complete playback lifecycle callback |
| **Default Timeout** | 180 seconds | 15 seconds |
| **Use Cases** | Real-time dialogue, continuous playback, daily use | Unstable network environment |

### ⚠️ Important Notes

> **Playback Behavior Characteristics**
> - **Each call interrupts previous playback**: New `playTtsAudioFile()` calls immediately stop current audio playback
> - **Non-append playback mode**: Unlike streaming TTS's automatic append, audio file playback uses replacement mode

> **Timeout Duration Setting**
> - Default timeout is 15 seconds, mainly for network download phase
> - Playback phase after audio download completes is not limited by this timeout
> - Recommend setting timeout reasonably based on network environment and text length

> **Playback Interruption Mechanism**
> - User voice input automatically interrupts current playback, triggering `onPlaybackInterrupted` callback
> - Calling `stopTtsAudioFile()` forcibly stops playback
> - New `playTtsAudioFile()` call interrupts current audio playback
> - When playback is interrupted, `onPlaybackCompleted` callback is not triggered

> **Caption Display Control**
> - `showCaption` parameter controls whether to display text in system subtitle bar
> - Setting to `false` enables silent playback, suitable for background sound effects etc.

## 4.8 Large Language Model (LLM) Interface

### Feature Introduction
Provides direct calling capability for large language models (LLM), supporting complex dialogue scenarios and customized intelligent interaction requirements.

> **💡 Usage Guide**: If you're unsure about LLM interface use cases, it's recommended to first check the FAQ document [What to do when persona and PageAgent cannot meet business requirements](../../FAQ.md#what-to-do-when-persona-and-pageagent-cannot-meet-business-requirements), which includes detailed usage examples and best practices.

### API Interface

**Synchronous Calling Interface**

```Kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.assit.LLMResponse
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage

/**
  * Large language model (LLM) interface, synchronous calling
  * Note: This interface must be called in a coroutine
  *
  *  @param  messages LLM chat messages
  *  @param  config LLM configuration
  *  @param  timeoutMillis Timeout duration in milliseconds
  *  @param  isStreaming Whether streaming output, true for streaming output (automatically calls TTS streaming playback), false for non-streaming output (returns execution result)
  *
  *  @return  TaskResult<LLMResponse> Task execution result, status=1 indicates success, status=2 indicates failure
  */
suspend fun llmSync(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true
): TaskResult<LLMResponse>
```

**Asynchronous Calling Interface**

```kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage

/**
  * Large language model (LLM) interface, asynchronous calling. Execution status returned via LLMCallback
  *
  *  @param  messages LLM chat messages
  *  @param  config LLM configuration
  *  @param  timeoutMillis Timeout duration in milliseconds
  *  @param  isStreaming Whether streaming output, true for streaming output (automatically calls TTS streaming playback), false for non-streaming output (returns execution result)
  *  @param  callback Callback, status=1 indicates playback successful, status=2 indicates playback failed
  */
fun llm(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    isStreaming: Boolean = true,
    callback: LLMCallback? = null
)
```

## 4.9 Text Commands

### Feature Introduction
Simulate user voice input through text form to trigger large language model planning and Action execution, enabling programmatic intelligent interaction.

### Application Scenarios
- When user manually clicks a button, equivalent to voice input (such as clicking "confirm" button equivalent to saying "confirm")
- Proactively initiate interaction when application launches
- Automated testing and debugging

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

/**
  * Trigger large language model planning of Actions through user question text
  *
  *  @param  text User question text, such as: How's the weather today?
  */
fun query(text: String)
```

## 4.10 Perception Information Reporting

### Feature Introduction
Upload application scene perception information to help AgentOS better understand current page content and user context, improving the accuracy and relevance of interaction.

### Application Scenarios
- Report displayed information lists to enable voice references (e.g., "I want to see number 3")
- Report current task progress and status
- Report page UI component hierarchy and structure

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Upload interface information to help large language model understand current page content and UI state
 *
 * @param interfaceInfo Interface information description (page content and UI component hierarchy), preferably with hierarchy structure but not excessively long
 */
fun uploadInterfaceInfo(interfaceInfo: String)
```

> **⚠️ Important Tip**: `uploadInterfaceInfo` uses **overwrite update** mechanism
> - Each call **completely replaces** previously uploaded information
> - To retain partial information, include the complete page information in the new call
> - Recommend re-uploading complete page status information when page content changes

## 4.11 Conversation History Management

### Feature Introduction
Clear large language model conversation context records to prevent historical dialogue from interfering with current interaction, ensuring accuracy and relevance of dialogue.

### Application Scenarios
- When application resets conversation content
- When user switches to new topic
- When needing to start completely new dialogue scenario

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Clear large language model conversation context records
 */
fun clearContext()
```

## 4.12 Disable Large Language Model Planning

### Feature Introduction
Control whether to disable large language model planning functionality. By default, each user interaction with AgentOS goes through large language model planning. When set to `true`, large language model planning is disabled, and users can handle large language model calls themselves.

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * Whether to disable large language model planning. When disabled, no more large language model planning occurs.
 * true means disabled, default is false.
 * Users can set to true if they need to handle large language model calls themselves
 */
var isDisablePlan: Boolean
```

## 4.13 Application Switching

### Feature Introduction
Provides quick switching to Xiaobao application functionality, enabling seamless application switching.

### API Interface

```Kotlin
import com.ainirobot.agent.AgentCore
import android.content.Context

/**
 * Switch to Xiaobao application
 *
 * @param context Context for launching Activity
 */
fun jumpToXiaobao(context: Context)
```

> **Note**: Ensure Xiaobao application is installed before calling this method. If not installed, corresponding tips will be output in logs.

## 4.14 System Action Status Listening

### Feature Introduction
Listen to execution status changes of [system built-in Actions](#24-system-built-in-actions), excluding custom Actions.

### API Interface

```Kotlin
fun setOnActionStatusChangedListener(listener: OnActionStatusChangedListener): Agent
```

### Usage Example

```Kotlin
PageAgent(this)
    .setOnActionStatusChangedListener { actionName, status, message ->
        println("System Action: $actionName, Status: $status, Message: $message")
        false // Return false indicates not consuming event, continue passing to other listeners (AppAgent)
    }
```

**Status Types**: `succeeded`(success)、`failed`(failure)、`timeout`(timeout)、`interrupted`(interrupted)、`recalled`(recalled)、`unsupported`(unsupported)

> **Note**: Callback executes on a worker thread.

## 4.15 Wake Word Mode Control

### Feature Introduction

Wake word mode is a new voice interaction control feature in AgentOS SDK v0.4.5, providing more flexible listening trigger mechanisms. Through three core interfaces, developers can precisely control wake word-based voice interaction behavior, adapting to interaction requirements in different scenarios.

### Core Interfaces

#### enableWakeupMode()

Enable or disable wake word mode.

**API Interface**

```kotlin
import com.ainirobot.agent.AgentCore

/**
 * Enable/disable wake word mode
 *
 * @param enabled true indicates enabling wake word mode, false indicates disabling (default)
 */
AgentCore.enableWakeupMode(enabled: Boolean)
```

**Behavior Explanation**

- **enabled = true (Enable wake word mode)**
  - Must wake up through wake word to start voice recognition
  - Avoids false triggering

- **enabled = false (Disable wake word mode, default)**
  - Voice recognition starts when VAD detects speech
  - No wake word required

**Usage Example**

```kotlin
// Enable wake word mode
AgentCore.enableWakeupMode(true)

// Disable wake word mode
AgentCore.enableWakeupMode(false)
```

#### setWakeupVadTimeout()

Set Voice Activity Detection (VAD) end listening timeout duration.

**API Interface**

```kotlin
import com.ainirobot.agent.AgentCore

/**
 * Set Voice Activity Detection (VAD) end listening timeout duration
 *
 * @param timeout Timeout duration in milliseconds, range: 1000ms ~ 10000ms, default 3000ms
 */
AgentCore.setWakeupVadTimeout(timeout: Long)
```

**Function Explanation**

Controls continued listening duration after user finishes speaking (when VAD detects speech end). If user continues speaking within the timeout window, no additional wake word is required.

**Usage Example**

```kotlin
// Set VAD timeout to 5 seconds
AgentCore.setWakeupVadTimeout(5000L)
```

#### setWakeupQuestionTimeout()

Set listening window duration after AI asks questions.

**API Interface**

```kotlin
import com.ainirobot.agent.AgentCore

/**
 * Set listening window duration after AI asks questions
 *
 * @param timeout Timeout duration in milliseconds, range: 3000ms ~ 30000ms, default 10000ms
 */
AgentCore.setWakeupQuestionTimeout(timeout: Long)
```

**Function Explanation**

When AI speech ends with "?", automatically opens listening window, allowing users to respond directly without wake word.

**Trigger Rules**

- AI speech ends with Chinese question mark "?"
- 1.5-second delay for confirmation to avoid false triggering
- Examples:
  - ✅ AI: "What would you like to drink?" → Trigger listening
  - ❌ AI: "It's very cold today. It's raining outside." → No trigger

**Usage Example**

```kotlin
// Set question listening window to 15 seconds
AgentCore.setWakeupQuestionTimeout(15000L)
```

### Usage Explanation

Three interfaces work together to control wake word mode:

1. **enableWakeupMode()** - Enable/disable wake word mode (main switch)
2. **setWakeupVadTimeout()** - VAD timeout duration (continued listening duration after user speech)
3. **setWakeupQuestionTimeout()** - Question listening window duration (automatic listening duration after AI asks questions)

**Sample Code**

```kotlin
// Enable wake word mode
AgentCore.enableWakeupMode(true)
AgentCore.setWakeupVadTimeout(5000L)
AgentCore.setWakeupQuestionTimeout(15000L)

// Disable wake word mode
AgentCore.enableWakeupMode(false)
```

# 5. Advanced Features

## 5.1 Annotation-Driven Action Registration

### Feature Introduction
For developers wishing to simplify Action registration process, AgentOS SDK provides annotation-based automatic registration mechanism. By adding annotations to methods, the SDK automatically identifies and registers these Actions at runtime, greatly simplifying the development process.

### 5.1.1 App-Level Dynamic Registration

When using annotations in Application for automatic Action registration, you need to use the `AppAgent(Application)` constructor, then create member methods in the Application and add corresponding annotations.

**Sample Code**

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

        // AppAgent initialization
        object : AppAgent(this) {

            override fun onCreate() {
                // Set role persona
                setPersona("Your name is Leopard Sister, a smart, warm, and slightly witty virtual assistant.")
                // Set role objective
                setObjective("Through natural conversation and appropriate emotional expression, make users feel understood, accompanied, and emotionally resonant, thereby enhancing communication comfort and trust.")
            }

            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                // Handle statically registered actions here. Return false if you don't need to handle them, or return true if you handle them and don't need further processing.
                // Default returns false
                return false
            }
        }
    }

    @AgentAction(
        name = "com.agent.demo.SHOW_SMILE_FACE",
        displayName = "Smile",
        desc = "Respond to user happiness, satisfaction, or positive emotions"
    )
    private fun showSmileFace(
        action: Action,
        @ActionParameter(
            name = "sentence",
            desc = "Response to say to the user"
        )
        sentence: String
    ): Boolean {
        AOCoroutineScope.launch {
            // Play response to user
            AgentCore.ttsSync(sentence)
            // Report Action execution status after playback completes
            action.notify(isTriggerFollowUp = false)
        }
        return true
    }
}
```

### 5.1.2 Page-Level Dynamic Registration

When using annotations in a page to automatically register Actions, you need to use the `PageAgent(Activity)` or `PageAgent(Fragment)` constructor, then create member methods in the corresponding Activity or Fragment and add annotations.

**Sample Code**

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

        // PageAgent initialization
        PageAgent(this)
    }

    @AgentAction(
        name = "com.agent.demo.SHOW_SMILE_FACE",
        displayName = "Smile",
        desc = "Respond to user happiness, satisfaction, or positive emotions"
    )
    private fun showSmileFace(
        action: Action,
        @ActionParameter(
            name = "sentence",
            desc = "Response to say to the user"
        )
        sentence: String
    ): Boolean {
        AOCoroutineScope.launch {
            // Play response to user
            AgentCore.ttsSync(sentence)
            // Report Action execution status after playback completes
            action.notify(isTriggerFollowUp = false)
        }
        return true
    }
}
```

### 5.1.3 Annotation Class Description

AgentOS SDK provides two core annotations: `@AgentAction` and `@ActionParameter`.

#### @AgentAction Annotation

`@AgentAction` is used to mark methods as Action handlers, defining basic information about Actions.

```kotlin
package com.ainirobot.agent.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AgentAction(
    /**
      * Action name
      */
      val name: String,
    /**
      * Action description
      */
      val desc: String,
    /**
      * Action display name
      */
      val displayName: String
)
```

#### @ActionParameter Annotation

`@ActionParameter` is used to mark method parameters, defining Action parameter information.

```kotlin
package com.ainirobot.agent.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActionParameter(
    /**
      * Parameter name
      */
      val name: String,
    /**
      * Parameter description
      */
      val desc: String,
    /**
      * Whether this is a required parameter
      */
      val required: Boolean = true,
    /**
      * Restrict parameter value to only select from specified values
      */
      val enumValues: Array<String> = []
)
```

# 6. Action Design Best Practices

> **⚠️ Important Reminder**: Action design quality directly affects the large language model's understanding and execution. Poor design may result in features not triggering correctly, parameter parsing errors, poor user experience, etc. Please strictly follow the best practice principles in this section.

In actual development, many developers fall into common pitfalls when designing Actions. The following uses comparative examples to illustrate Action design best practices.

## 6.1 Common Design Pitfalls

**❌ Incorrect Example: Action with Overly Complex Functionality**

```kotlin
Action(
    name = "com.service.robot.HANDLE_TASK",
    displayName = "Handle Task",
    desc = "Intelligent service robot assistant that can perform multiple service tasks, including but not limited to information broadcasting, task reminders, item delivery and other various services. Responds flexibly based on user needs.",
    parameters = listOf(
        Parameter("task_type", ParameterType.STRING, "Task type", true),
        Parameter("target_location", ParameterType.STRING, "Target location", false),
        Parameter("content", ParameterType.STRING, "Task content", false),
        Parameter("items", ParameterType.STRING, "Related items", false),
        Parameter("schedule_time", ParameterType.STRING, "Execution time", false)
    )
)
```

**Problem Analysis:**
- Functionality responsibilities unclear, one Action handles multiple tasks
- Too many parameters with vague meanings
- Description too broad, hard for large language model to accurately understand use cases
- Lack of parameter enumeration restrictions, easily producing uncontrollable inputs

## 6.2 Best Practice Principles

Remember three core points when writing `Actions`:

**1. Single Responsibility Principle: One Action does one type of task**
- Each Action should be responsible for one clear function, avoid mixing multiple functions
- Avoid differentiating business logic through parameters
- If you find needing to judge a `type` parameter to decide what to actually do, the design is too generalized and should be split
- Action names and descriptions should be accurate and detailed, letting the large language model clearly understand functional boundaries and use scenarios

**2. Precise Parameter Design: Each parameter must have clear type, description, and whether required, plus enumeration restrictions (if applicable)**
- Moderate parameter count (recommend 1-3)
- Each parameter has clear business meaning and accurate description
- Use ParameterType.ENUM and enumValues when needing to restrict value range, avoiding free strings causing confusion
- Reasonably set required and optional parameters

**3. Clear Slot Semantics: Clear semantics and validation mechanisms for potential slot positions from user input**
- Analyze and design key information slots user might provide (such as room number, task type, destination, etc.)
- Define clear semantic scope and value rules for each slot
- Ensure slot information can be accurately extracted and validated, avoiding ambiguous parsing

## 6.3 Correct Example: Breaking Down Into Multiple Specialized Actions

**✅ Example 1: Leading to Destination Action (1 parameter)**

```kotlin
Action(
    name = "com.service.robot.LEADING_TO_DESTINATION",
    displayName = "Lead to Destination",
    desc = "Robot leads user to specified destination, providing route guidance and escort service",
    parameters = listOf(
        Parameter(
            name = "destination",
            type = ParameterType.STRING,
            desc = "Destination name, such as restroom, elevator, service desk, meeting room, etc.",
            required = true
        )
    )
)
```

**✅ Example 2: Play Music Action (2 parameters)**

```kotlin
Action(
    name = "com.entertainment.robot.PLAY_MUSIC",
    displayName = "Play Music",
    desc = "Play specified song by specified artist",
    parameters = listOf(
        Parameter(
            name = "song_name",
            type = ParameterType.STRING,
            desc = "Song name",
            required = false
        ),
        Parameter(
            name = "artist_name",
            type = ParameterType.STRING,
            desc = "Artist name",
            required = false
        )
    )
)
```

**✅ Example 3: Adjust Air Conditioner Action (1 parameter, using enumeration type)**

```kotlin
Action(
    name = "com.smart.home.ADJUST_AIRCON",
    displayName = "Adjust Air Conditioner",
    desc = "Adjust air conditioner operating mode",
    parameters = listOf(
        Parameter(
            name = "mode",
            type = ParameterType.ENUM,
            desc = "Air conditioner mode",
            required = true,
            enumValues = listOf("Cooling", "Heating", "Dehumidification", "Ventilation", "Auto")
        )
    )
)
```

## 6.4 Design Checklist

When designing Actions, please check the following points:

- [ ] **Single Function**: Does the Action only handle one clear business function?
- [ ] **Streamlined Parameters**: Is parameter count reasonable (recommend 1-3)?
- [ ] **Accurate Description**: Are Action and parameter descriptions concrete and specific?
- [ ] **Enumeration Restrictions**: Do parameters needing value range restrictions use ParameterType.ENUM?
- [ ] **Reasonable Required**: Is the required parameter setting consistent with business logic?
- [ ] **Naming Convention**: Do you follow naming specification constraints?

> **🔥 Core Reminder**: If you find an Action needing to judge different business logic through parameters, this usually means the design is too complex and should be split into multiple specialized Actions. Good Action design is the key foundation for successful AgentOS applications!

# 7. Project Resources

## 7.1 Sample Projects

### Template Project
Provides basic project structure and configuration, suitable for quick start of AgentOS SDK development.

**Project Address**: [AgentSDKSampleEmpty](https://github.com/orionagent/AgentSDKSampleEmpty)

### Complete Sample Project
Contains multiple functionality examples and best practices, showcasing complete AgentOS SDK capabilities.

**Project Address**: [AgentSDKSample](https://github.com/orionagent/AgentSDKSample)

## 7.2 Development Resources

### Reception Backend
Backend system for applying for AppId and managing Agent applications.

[Visit Reception Backend](https://jiedai.ainirobot.com/web/portal/#)


# 8. Frequently Asked Questions (FAQ)

When encountering problems during AgentOS SDK development, it's recommended to first check the FAQ document, which contains solutions for common problems including development environment configuration, voice interaction troubleshooting, debugging tool usage, and more.

**📋 [View Complete FAQ Document](../../FAQ.md)**

# 9. Technical Support

For any questions, please contact the technical support team.
