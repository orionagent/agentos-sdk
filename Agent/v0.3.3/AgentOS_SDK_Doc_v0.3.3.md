# Agent SDK for APK V0.3.0文档

> ## 📣 **版本信息**
> 当前SDK版本: 0.3.0

# 1. 概述

> Agent SDK 是一个用于机器人交互的Android开发套件，提供了应用与机器人Agent服务进行通信的能力。SDK支持应用级和页面级的Agent开发，可以实现自然语言交互、动作规划和执行等功能。

## 1.1 环境要求

### 1.1.1 开发环境

* Android SDK 版本: 最低支持API 26 (Android 8.0)
* JDK版本: Java 11
* Kotlin、Java语言开发
- 建议开发环境：
  - Android Studio：基础环境构建和调试
  - Cursor：提供AI编码能力

### 1.1.2 运行环境

AgentOS Product Version: V1.3.0.250515

RobotApi Version: 11.3

## 1.2 快速开始

> 如果没有任何Android开发经验，请先下载安装[Android Studio](https://developer.android.com/studio?hl=zh-cn)，然后下载我们提供的空项目，用Android Studio打开此空项目后再开始接下来的步骤。

### 1.2.1 配置仓库

如果你的gradle脚本使用的 **Groovy** ，那在项目根目录下会有一个**settings.gradle**文件，在此文件中以下位置添加maven配置（以下代码中 **【重要配置】标记的部分** ）

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

如果你的gradle脚本使用的是 **Kotlin** ，那么在项目根目录下会有一个**settings.gradle.kts**文件，在此文件中以下位置添加maven配置（以下代码中 **【重要配置】标记的部分** ）

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

### 1.2.2 添加依赖

如果你的gradle脚本使用的 **Groovy** ，那在**项目根目录**的 **app/** **目录下会有一个****build.gradle****文件，在此文件中以下位置添加maven配置（以下代码中 **【重要配置】标记的部分** ）

```Groovy
dependencies {
        implementation 'com.orionstar.agent:sdk:0.3.2-SNAPSHOT' // 【重要配置】Agent SDK依赖
        
        // 以下是Android标准库，默认kotlin项目都会依赖，
        // 如果编译报未找到错误，再添加以下依赖库
        implementation 'androidx.core:core-ktx:1.13.1'
        implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

如果你的gradle脚本使用的是 **Kotlin** ，那么在**项目根目录**的 **app/** **目录下会有一个****build.gradle.kts****文件，在此文件中以下位置添加maven配置（以下代码中 **【重要配置】标记的部分** ）

```Kotlin
dependencies {
        implementation("com.orionstar.agent:sdk:0.3.2-SNAPSHOT") // 【重要配置】Agent SDK依赖
        
        // 以下是Android标准库，默认kotlin项目都会依赖，
        // 如果编译报未找到错误，再添加以下依赖库
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.6.1")
}
```

### 1.2.3 添加注册表

查看项目根目录的**app/src/main**目录下，是否包含**assets**目录，如果没有请先创建**assets**目录，然后在**assets**目录下创建**actionRegistry.json**文件，并在文件中添加以下配置

```JSON
{
  "appId": "app_ebbd1e6e22d6499eb9c220daf095d465",
  "platform": "apk",
  "actionList": []
}
```

 **appId** ：Agent应用的appId，需在[接待后台](https://jiedai.ainirobot.com/web/portal/#/frame/hmag-agentos/hmag-agentos.agentapp/)申请

 **platform** ：当前运行的平台，如：**opk**或**apk**

 **actionList** ：可以从外部调起的action（只能是app级），在注册表中声名的action需要在AppAgent的[onExecuteAction](https://cheetah-mobile.feishu.cn/docx/FwCQdP1WboqJm3xv5Yic8SxdnWf?fromScene=spaceOverview#share-KHucdip2BoLGHZx74aYcIC1snAh)方法中处理action的执行，注：如果不想对外暴露action，actionList可以设置为空数组[]

> 📣 在这个项目中，我们将一起开发一个有个性、能感知情绪的虚拟助手。她不仅能和你对话，还能察觉你的情绪变化，并做出恰当回应——是的，她不再是冷冰冰的程序，而是一位会关心你感受的"豹姐姐"！

### 1.2.4 添加AppAgent

> 📣 **注意事项**
> 1. 一个App中只能有一个AppAgent实例

> 📣 在这里，我们为虚拟助手：豹姐姐，进行角色人设、角色目标等基础设定。

在项目的MainApplication的onCreate方法中添加以下代码（ **加粗部分** ），如果没有MainApplication.kt文件，请参考[示例项目](https://cheetah-mobile.feishu.cn/docx/FwCQdP1WboqJm3xv5Yic8SxdnWf?fromScene=spaceOverview#doxcnh7OkltLezA2VX1XufECh6f)

```Kotlin
package com.ainirobot.agent.sample

import android.app.Application
import android.os.Bundle
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        object  : AppAgent(this) {
              
            override   fun   onCreate()  {
                  // 设定角色人设
                 setPersona("你叫豹姐姐，是一位聪明、亲切又略带俏皮的虚拟助手。")
                  // 设定角色目标
                 setObjective("通过自然的对话和合适的情绪表达，让用户感受到理解、陪伴与情感共鸣，从而提升交流的舒适感和信任感。")
             }

              override   fun   onExecuteAction(
                 action:  Action,
                 params:  Bundle?
             ):  Boolean  {
                  // 在此处处理静态注册的action，如果你不需要处理，请返回false，如果要自行处理且不需要后续处理，则返回true
                  // 默认返回false
                  return   false
             }
         }
    }
}
```
> 📣 到这一步，我们的 App 已经有了一个拥有"个性"的虚拟角色。接下来，我们要给她添加一些技能（Actoion），让她学会根据用户的情绪做出反应！

### 1.2.5 添加PageAgent

> 📣 **注意事项**
> 1. 每个页面只能有一个PageAgent实例

示例：在此页面中添加三个Action分别表示感受到用户**高兴、伤心、生气**三种情绪，并根据不同情境跟用户对话

> 📣 **设计思路：让助手"有情绪感知"**
> 在日常交流中，人的情绪变化往往比语言更关键。本节我们为助手定义几个情绪感知型技能（Action）

**情绪感知技能列表：**
- 😊 **展示笑脸，语音回复** - 当感知到用户高兴、满意或正面情绪时
- 😢 **展示伤心，语音安慰** - 当感知到用户难过、失落或负面情绪时  
- 😠 **展示生气，语音回应** - 当感知到用户愤怒、不满或激动情绪时

你可以把它理解成："当助手感知到用户的某种情绪（用户输入），就会下发一个对应的 Action，让虚拟助手用语音和表情做出回应"。

在MainActivity.kt中添加以下代码（代码中只添加了一个显示表情的Action，你可以按示例添加另外两个）

```Kotlin
package com.ainirobot.agent.sample

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

> 📣 **注意：在任何一个Action执行完成后都需要调用action的nofity()方法**

> 🎉 **现在你完成了一个能察觉你的情绪变化，并做出恰当回应的"豹姐姐"助手**

### 1.2.6 完整示例

上边的QUICK START是为了方便接入，下面的[示例项目](https://cheetah-mobile.feishu.cn/docx/FwCQdP1WboqJm3xv5Yic8SxdnWf?fromScene=spaceOverview#doxcnrKf01IXamqIap0oEQqKgWc)也很简单，但添加了不同场景显示不同表情的功能，可能会更有趣一些，可以直接下载运行。

### 1.2.7 总结

开启一个基于Agent SDK的App ，主要有以下主要核心步骤。
1. 集成Agent SDK（配置仓库、添加依赖、创建注册表）
2. 设定App的人设、设定App级别的目标
3. 创建业务页面，并设置页面级的目标，上传页面信息
4. （很重要，这是你的应用和AgentOS交互的核心环节）基于App全局/特定页面，定义并注册Action到Agent SDK，可以同时注册多个Action。
  - 第一步：定义Action的名字，描述，参数及描述
  - 第二步：实现Action的处理逻辑
  - 第三步：通过语音触发，看看是否能顺利规划并且执行到你定义的Action

# 2. Action详解

## 2.1 什么是Action

> AgentOS的核心是识别用户的意图执行合适的技能，而这个技能即为Action，如：用户问“我明天去深圳需要带伞吗？”，识别用户的意图是查询天气，那调用对应的查天气的技能（Action），比如：orion.agent.action.WEATHER

### 2.1.1 基础属性

Action下基础属性及描述如下：

```Kotlin
package com.ainirobot.agent.action

class Action(
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
)
```

> 📣 **注：创建Action时需要清晰描述Action的各项属性，方便大模型理解Action的功能，能够更精确的选择合适的Action**

### 2.1.2 Action参数

Action参数是想让大模型从用户的Query中抽取的核心内容，如：“我想查一下北京今天的天气”，那么可以从中抽取city和date两个字段。

参数描述对象的基本属性如下：

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

> 📣 **注：参数的desc也要能精确反应此参数的定义，让大模型的理解更精准；而对于参数的name最好使用英文单词，多个单词间以下划线\_连接；另外，name一定不要与Action对象或Parameter对象的属性名相同或类似，避免出现歧义，** 🚨 **<span style="background-color: #ff4444; color: black; padding: 2px 4px; border-radius: 3px;">这非常重要！！！这非常重要！！！这非常重要！！！</span>**

## 2.2 Action注册

> Action分为App级和Page级两种，区别在于其活跃的生命周期不同。

### 2.2.1 App级Action

> App级Action即为全局Action，Action在整个应用运行（处于前台）期间都会被响应，如果应用退出或进入后台则不会被响应，App级的Action注册支持**动态注册**和**静态注册**两种。

#### 动态注册

动态注册的App级Action，是为了在应用整个生命周期内响应用户的实时意图， **在应用处于前台期间一直生效，应用退出或进入后台时则不再响应** 。 **需要在AppAgent的onCreate方法中调用注册，如以下示例，注册了一个**退出的Action：

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
        // 示例：此处注册了系统Action：EXIT，当用户说“退出”时，会触发BACK事件
        registerAction(Actions.EXIT)
    }
}
```

**动态注册**支持注册在当前应用中**新创建**的私有Action，也支持注册外部的Action，如：**系统Action**或者其它 **AgentOS App** （集成AgentSDK的应用）中**静态注册的Action。**

以下示例，注册一个打开**天气App**中的 **打开天气首页Action** ：

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

首先，只有 **App级的Action才可以静态注册** ，静态注册的Action是为了向外部提供调起当前应用的入口，默认并不会在当前App运行期间生效，如果想要在当前App中也生效，需要再次动态注册即可。

**静态注册**必须在**actionRegistry.json**注册表中配置，并添加详细的参数描述等。

以下示例，是在**天气App**中向外部提供一个可以**打开天气首页查天气**的Action：

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

> 📣 **注：required为false，表示参数可以为空，如果为空时需要执行端自行处理，如：使用当前定位的城市，时间默认为今天等。**

静态注册的Action，最终的执行器是在AppAgent的**onExecuteAction**方法中，如果对外公开了多个Action，则需要通过actionName判断不同的Action并分别处理。

以下还是**天气App**为例，我们在上一步中，已经在**天气App**的**注册表**中添加了[com.agent.tool.WEATHER\_HOME](https://cheetah-mobile.feishu.cn/docx/FwCQdP1WboqJm3xv5Yic8SxdnWf?fromScene=spaceOverview#doxcnwGQmqyHAiMPRdoQw7dEf0g)的Action，那天气App中AgentAgent的**onExecuteAction**方法必须处理此Action。

以下示例

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

> 📣 **注：简单来说，静态注册的Action为了让外部调用，动态注册的Action为了让当前应用内部调用**

### 2.2.2 Page级Action

> Page级的Action需要在页面（Activity或Fragment）初始化时声名，且只在当前页面对用户可见时生效，当页面退出或者被其它页面覆盖则不再生效

#### 动态注册

因为**Page级Action**只在当前页面生效，所以它 **只能动态注册，不能在注册表中注册** ，即不能向外部提供接口

以下示例定义了三个Action，根据用户的情绪显示三种不同的表情

在Activity的onCreate方法中创建

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

#### Action过滤

如果你在App级注册了N个全局的Action，但在当前页面上不想规划其中一个或多个全局Action，那么可以通过以下方式排除掉指定的全局Action

**过滤指定的一个Action**

```Kotlin
// 过滤掉指定的全局Action，参数为Action的name
pageAgent.blockAction("com.xxx.yyy.TTT")
```

**过滤指定的多个Action**

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

## 2.3 Action执行

> Action执行上面已经说的很详细，此处只是为了再强调一下

### 2.3.1 执行回调

动态注册Action的具体执行，需要为Action对象设置一个执行器，在执行器中添加你要执行的代码，如：

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
    executor =  object  : ActionExecutor {

          override   fun   onExecute(action:  Action, params:  Bundle?):  Boolean  {
             showFaceImage(R.drawable.ic_angry)
             handleAction(action, params)
              return   true
         }
     }
)
```

静态注册Action的具体执行，需要在AppAgent的onExecuteAction方法中实现，如：

```Kotlin
override fun onExecuteAction(
    action: Action,
    params: Bundle?
): Boolean {
    return false
}
```

> 📣 **注：不管是哪种注册方式，执行的回调都带有一个Boolean的返回值，表示你是否已经处理过此Action，如果你不处理，请返回false，如果要自定义处理且不需要后续处理，则返回true**

### 2.3.2 执行结果

**<span style="background-color: #ff4444; color: black; padding: 4px 8px; border-radius: 4px; font-weight: bold;">这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！这非常重要，必不可少！！！</span>**

1. 首先，**任何Action的执行** **回调** **方法中都不能执行耗时操作。**
2. 其次，如果你要处理一个Action，除了**在执行的** **回调** **方法返回值返回true**之外，还需要在**Action执行完成后手动调用action的成员方法nofity()** 把执行状态或结果同步给系统，具体的时机用户可以自行定义，如：页面加载完成、天气播报完成、到达一个目的地等。
3. 最后，执行的回调方法默认都是 **子线程** 。

notify是Action类的成员方法，说明如下：

```Kotlin
package com.ainirobot.agent.action

/**
  * Action执行完成后需要同步执行结果
  *
  *  @param  result Action的执行结果
  *  @param  isTriggerFollowUp 在Action执行完成后主动引导用户进行下一步操作，默认开启
  */
fun notify(
    result: ActionResult = ActionResult(ActionStatus.SUCCEEDED),
    isTriggerFollowUp: Boolean = true
)
```

## 2.4 系统内置Action

> 系统Action是系统内置的功能Action，包含部分系统的功能、指令和应用等，系统Action的**namespace**是**orion.agent.action。**
>
> 系统Action并不是都由系统实现了执行逻辑，有些还是需要用户自行处理逻辑，只是系统内置了这些Action的定义而已，如：orion.agent.action.CLICK（点击事件）

1. 系统处理的Action

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

> 📣 **注：CANCEL、BACK和EXIT默认处理为模拟点击Back键**

2. 需用户处理的Action

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

# 3. 核心功能接口

1. ## 麦克风开关
### 介绍

• 默认只要集成了AgentSDK的应用，在应用进入前台时会自动打开麦克风，应用退出或进入后台会自动关闭麦克风，当然你也可以自行开启和关闭，通过以下方式

### 应用场景
• 需要手动闭麦的场景。

• 播放音视频时防止干扰时。

```Kotlin
import com.ainirobot.agent.AgentCore

// 设置麦克风静音状态
AgentCore.isMicrophoneMuted = true // 静音
AgentCore.isMicrophoneMuted = false // 取消静音
```

2. ## 获取ASR和TTS的结果

### 介绍
- 通过设置setOnTranscribeListener监听系统回调的方式，获取到对话交互的内容。
### 应用场景
- 如果你的应用想获取到ASR识别/TTS播报的内容，可以通过以下方法。

```Kotlin
import com.ainirobot.agent.base
import com.ainirobot.agent.OnTranscribeListener

/**
 * 设置ASR和TTS监听器
 */
fun setOnTranscribeListener(listener: OnTranscribeListener): Agent
```
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

- setOnTranscribeListener是AppAgent和PageAgent的成员方法。

### 获取ASR/TTS 内容  
- transcription.text 是文本内容。

### 判断是ASR还是TTS内容
- 通过transcription.isUserSpeaking判断，true时为ASR结果，false为TTS结果。

### 判断是流式结果还是最终结果
- 通过transcription.final判断，true为最终结果，false为中间结果。

**onTranscribe** 回调函数返回值的设定
- 返回true时，代表你告知系统你消费了此次结果，系统将不再把字幕显示在底部的字幕条上。
- （**建议**）返回false时，将不影响后续系统对于ASR/TTS结果的分发。

> 📣 **注意：onTranscribe 回调是在子线程中。**

## 3. Agent状态监听
此接口监听了Agent在思考或处理过程中的一系列状态，同样它也是AppAgent和PageAgent的成员方法。
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

```Kotlin

/**
 * Agent状态变化监听
 */
interface OnAgentStatusChangedListener {

    fun onStatusChanged(status: String, message: String?): Boolean

}
```
**status**，目前只包含：**listening（正在听）**、**thinking（思考中）**、**processing（处理中）**、**reset_status（状态复位）**四种状态，以后可能会扩展
**message**，当**status**是**processing**时，**message**可能会有值，如：**“正在选择工具...”、“正在获取天气...”、“正在总结答案...”**等，当**status**为其它状态时**message**为空

## 4. 关闭语音条

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
## 5. 播放TTS/停止播放TTS

### 介绍
- 主动调用系统的接口，合成指定文本为音频，并自动进行播报
### 应用场景
- 需要在一些业务流程中，驱动机器人说出某一句话时，比如应用启动时，让机器人播报“欢迎光临”，就可以主动调用。

**播报TTS**

```Kotlin
同步调用接口
import com.ainirobot.agent.AgentCore

/**
 * TTS接口，同步调用
 * 注：此接口需在协程中调用
 *
 * @param text 要播放的文本
 * @param timeoutMillis 超时时间，单位毫秒
 *
 * @return 返回1表示成功，返回0表示失败
 */
suspend fun ttsSync(text: String, timeoutMillis: Long = 180000): Int {
    return this.appAgent?.api?.ttsSync(text, timeoutMillis) ?: 0
}
```

```Kotlin
异步调用接口
import com.ainirobot.agent.AgentCore

/**
 * TTS接口，异步调用，返回状态通过TaskCallback回调
 *
 * @param text 要播放的文本
 * @param timeoutMillis 超时时间，单位毫秒
 * @param callback 回调，status=1表示播放成功，status=0表示播放失败
 */
fun tts(
    text: String,
    timeoutMillis: Long = 180000,
    callback: TaskCallback? = null
) {
    this.appAgent?.api?.tts(text, timeoutMillis, callback)
}
```

**停止播报**

```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 强制打断TTS播放
 */
fun stopTTS() {
    this.appAgent?.api?.stopTTS()
}
```

## 6. 大模型接口
- 同步接口调用
```Kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage

/**
  * 大模型接口，同步调用
  * 注：此接口需在协程中调用
  *
  *  @param  messages 大模型chat message
  *  @param  config 大模型配置
  *  @param  timeoutMillis 超时时间，单位毫秒
  *
  *  @return  返回1表示成功，返回0表示失败
  */
suspend fun llmSync(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000
): Int {
    return this.appAgent?.api?.llmSync(messages, config, timeoutMillis) ?: 0
}
```
- 异步接口调用
```kotlin
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage

/**
  * 大模型接口，异步调用，返回状态通过TaskCallback回调
  *
  *  @param  messages 大模型chat message
  *  @param  config 大模型配置
  *  @param  timeoutMillis 超时时间，单位毫秒
  *  @param  callback 回调，status=1表示播放成功，status=2表示播放失败
  */
fun llm(
    messages: List<LLMMessage>,
    config: LLMConfig,
    timeoutMillis: Long = 180000,
    callback: TaskCallback? = null
) {
    this.appAgent?.api?.llm(messages, config, timeoutMillis, callback)
}
```

## 7. 文本指令

- 介绍
  - 当你需要在没有用户语音交互的时候希望触发大模型的规划和执行时，推荐使用。
- 应用场景
  - 用户手动点击一个页面的按钮“确定”，等效于用户说了“确定”，通过QueryByText即可。
  - 比如应用启动页面，在用户开始交互之前，主动去跟用户交互，可以通过QueryByText去驱动。

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

## 8. 感知信息上报
- 介绍
  - 当你需要上传一些你的应用的场景的感知信息，辅助AgentOS去理解和规划你的任务时，建议调用。
- 应用场景
  - 比如你的屏幕上有很多的信息，你希望AgentOS感知到用户看到的信息（比如用户可以问，我想看看第3个），你可以把屏幕的信息整理成一定的格式上报，比如当前任务进展、比如数据清单。
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

## 9. 清空对话历史
**介绍**
- 清空上下文对话历史，防止之前的对话干扰到新的对话。

**应用场景**
- 比如应用重置了对话的内容，用户更换了一个话题等等
```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 清空大模型对话上下文记录
 */
fun clearContext() {
    this.appAgent?.api?.clearContext()
}
```

## 10. 免唤醒开关
免唤醒是AgentOS的一项重要功能，它能自动过滤环境音以及周围的其他人声，只服务于当前正在与机器人交互的用户；默认此功能为开，会严格限制收音，只采集机器人面前用户的声音，且会智能识别用户是否在说话。

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

## 11. 禁用强制规划
默认情况下，AgentOS与用户的每一次交互返回的结果都是以Action作为载体，但在有些场景下用户可能不需要规划Action，只想调用大模型与用户交流，那么此时就可以禁用掉强制规划Action的功能，具体接口如下：
```Kotlin
import com.ainirobot.agent.AgentCore

/**
 * 是否禁用大模型规划，禁用后不会再进行大模型规划，true表示禁用，默认为false
 * 用户如果需要自行处理大模型与大模型的调用则可设置为true
 */
var isDisablePlan: Boolean
    get() = appAgent?.isDisablePlan ?: false
    set(value) {
        appAgent?.isDisablePlan = value
    }
```


# 4. 进阶功能

## 注解实现Action动态注册

> 如果觉得手动注册麻烦，那么我们还提供了更简单便捷的注册方式，只需要在Application、Activity或Fragment中添加成员方法并添加注解，那么SDK会在运行时自动识别这些Action并注册

### App级动态注册

在应用内使用注解方式自动注册时，需要使用 **AppAgent** (Application)构造方法，然后在**Application**中创建 **成员方法** ，最后添加注解即可

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
        object : AppAgent(this@MainApplication) {

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
         name =  "com.agent.demo.SHOW_SMILE_FACE",
         displayName =  "笑",
         desc =  "响应用户的开心、满意或正面情绪"
     )
      private   fun   showSmileFace(
         action:  Action,
          @ActionParameter(
             name =  "sentence",
             desc =  "回复给用户的话"
         )
         sentence:  String
     ):  Boolean  {
         AOCoroutineScope.launch  {
              // 播放给用户说的话
             AgentCore.ttsSync(sentence)
              // 播放完成后，及时上报Action的执行状态
             action.notify(isTriggerFollowUp =  false)
         }
          return   true
     }
}
```

### Page级动态注册

在页面内部使用注解方式自动注册时，需要使用 **PageAgent** (Activity)或 **PageAgent** (Fragment)构造方法，然后在对应的**Activity**或**Fragment**中创建 **成员方法** ，最后添加注解即可

```kotlin
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {  v, insets ->
              val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

          // PageAgent初始化
        PageAgent(this)
    }

    @AgentAction(
         name =  "com.agent.demo.SHOW_SMILE_FACE",
         displayName =  "笑",
         desc =  "响应用户的开心、满意或正面情绪"
     )
      private   fun   showSmileFace(
         action:  Action,
          @ActionParameter(
             name =  "sentence",
             desc =  "回复给用户的话"
         )
         sentence:  String
     ):  Boolean  {
         AOCoroutineScope.launch  {
              // 播放给用户说的话
             AgentCore.ttsSync(sentence)
              // 播放完成后，及时上报Action的执行状态
             action.notify(isTriggerFollowUp =  false)
         }
          return true
     }
}
```

### 注解类说明

> 注解类有两个：**AgentAction**和**ActionParameter**

#### **AgentAction**

AgentAction是作用于**成员方法**上，用来标记该方法是一个Action

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

#### **ActionParameter**

ActionParameter是作用于**方法参数**上，用来标记Action的参数

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

# 5. 项目源码

#### 模版项目

https://github.com/orionagent/AgentSDKSampleEmpty

#### 示例项目

https://github.com/orionagent/AgentSDKSample



# 6. 技术支持

如有任何问题，请联系技术支持团队。
