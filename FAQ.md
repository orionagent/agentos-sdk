# FAQ

## Table of Contents

1. [Concepts You Need to Know](#concepts-you-need-to-know)
   - [App Authorization](#app-authorization)
   - [App Suspended](#app-suspended)
   - [App Resume from Suspension](#app-resume-from-suspension)

2. [Enable Developer Mode](#enable-developer-mode)
   - [Background](#background)
   - [How to Enable](#how-to-enable)
   - [Query Dynamic Password](#query-dynamic-password)

3. [How to Get Raw Robot Logs](#how-to-get-raw-robot-logs)
   - [1. View All Saved Logs on the Robot](#1-view-all-saved-logs-on-the-robot)
   - [2. Pull Logs for a Specific Time Range](#2-pull-logs-for-a-specific-time-range)

4. [Common ADB Commands](#common-adb-commands)
   - [1. adb devices](#1-adb-devices)
   - [2. adb shell](#2-adb-shell)
   - [3. adb push](#3-adb-push)
   - [4. adb pull](#4-adb-pull)
   - [5. adb install](#5-adb-install)

5. [Common Issues and Solutions](#common-issues-and-solutions)
   - [No Response in Voice Interaction](#no-response-in-voice-interaction)
   - [PageAgent Lifecycle Management for Non-Activity/Fragment Development](#pageagent-lifecycle-management-for-non-activityfragment-development)
   - [Do I Need to Reimplement Features When Migrating from RobotOS to AgentOS?](#do-i-need-to-reimplement-features-when-migrating-from-robotos-to-agentos)
   - [Will AgentOS Automatically Call Xiaobao App Features?](#will-agentos-automatically-call-xiaobao-app-features)
   - [What If Persona and PageAgent Cannot Meet Business Requirements?](#what-if-persona-and-pageagent-cannot-meet-business-requirements)

## Concepts You Need to Know

### App Authorization
An app can be authorized to use the SDK only when it successfully connects to RobotOS and its UI is displayed in the foreground. Once the app moves to the background, it is suspended immediately.

### App Suspended
During robot operation, system events may occur, such as emergency stop, low battery, OTA, or hardware exceptions. When these events occur, RobotOS takes over business control. The foreground business APK is suspended, receives the `onsuspend` event, and can no longer use APIs.

### App Resume from Suspension
When the corresponding system event disappears, RobotOS returns business control to the current app, and the APK regains the ability to use RobotApi.

## Enable Developer Mode

### Background

To ensure system security, USB debugging (wired ADB) is disabled by default in later versions. Applicable devices and versions:

- **Leopard Secretary**: V6.9 and later
- **Mini**: V6.13 and later

### How to Enable

ADB is disabled by default in factory versions and can only be enabled temporarily through the following method:

1. At any time (including during self-check exceptions), swipe down with one finger >> tap the time area rapidly multiple times.

2. A dynamic password input page pops up. This page displays the system date and time. For how to get the dynamic password, see [Query Dynamic Password](#query-dynamic-password).

   - **Dynamic password correct**: Jump to step 3 and configure ADB.
   - **Dynamic password incorrect**: Input is cleared and you remain on the current page.

3. After **Enable Debugging** is turned on, a second menu **Persistent Debugging** is shown. Note that **Enable Debugging** resets to default after reboot.

   - The **Persistent Debugging** menu is hidden by default and appears only after **Enable Debugging** is turned on.
   - After appearing, **Persistent Debugging** is off by default and must be manually enabled.
   - If **Enable Debugging** is turned off again, **Persistent Debugging** is automatically disabled and hidden.
   - The above settings take effect after reboot.

   For convenience, three additional shortcuts are also provided: **Open MIMI**, **Enable System Navigation Bar**, and **Open Settings**.

4. Newly manufactured robots support Wi-Fi ADB debugging. You can also enable Wi-Fi ADB here for easier debugging.

### Query Dynamic Password

Provide the SN and contact your pre-sales/after-sales technical support.

If you need a video tutorial for enabling developer mode, visit: [Detailed Developer Mode Tutorial](https://doc.orionstar.com/blog/knowledge-base/%e6%89%93%e5%bc%80%e5%bc%80%e5%8f%91%e8%80%85%e6%a8%a1%e5%bc%8f/#undefined)

## How to Get Raw Robot Logs

### 1. View All Saved Logs on the Robot

Use the following commands to enter the robot shell and view the log directory:

```bash
adb shell
cd /sdcard/logs/offlineLogs/821/
ls -l
```

### 2. Pull Logs for a Specific Time Range

After exiting `adb shell`, use `adb pull` to retrieve logs for the target time range:

```bash
adb pull /sdcard/logs/offlineLogs/821/logcat.log-2020-05-22-11-00-07-062.tgz
```

> **Note**: Log filenames include exact timestamps. Select the corresponding file based on your required time range.

## Common ADB Commands

Whether robot development uses OPK or APK, it is Android-based. So first ensure your Android environment is ready. ADB command usage is especially important. Below are commonly used ADB commands:

### 1. adb devices

This command queries currently connected device IDs. When the computer is properly connected to the robot, it returns a robot list, for example:

```bash
black_mac:dexlib mac$ adb devices
List of devices attached
KTS17Q080284    device
```

If no connected robot list is returned, debugging is not possible. First check whether the USB cable is connected firmly. For other issues, search for "adb devices cannot return correct results".

### 2. adb shell

This command enters the robot terminal shell. Common `adb shell` command examples:

**Check robot SN:**
```bash
adb shell getprop|grep serial
```

### 3. adb push

This command pushes files to the robot. It is used during manual robot upgrades, where you need to push the upgrade package to the corresponding robot folder.

```bash
adb push xxx.opk /system/vendor/opk/
```

**Manual OTA upgrade:**

After ADB is enabled and connected to the robot, run:

```bash
# Start ota service
adb shell am start -n com.ainirobot.ota/.MainActivity

# Push ota package, xxxx is the package path
adb push xxxx /sdcard/ota/download/update.zip
```

After push is complete, click **Start Upgrade** on the robot page.

### 4. adb pull

This command pulls files from the robot. It is commonly used when retrieving robot logs.

**Get recent robot logs:**
```bash
adb pull /sdcard/logs/offlineLogs/821/
```

**Get robot logs for a specific time:**

1. Use this command to view all saved logs on the robot:
```bash
adb shell
cd /sdcard/logs/offlineLogs/821/
ls -l
```

2. Exit `adb shell`, then use `adb pull` to retrieve logs for the required time range:
```bash
adb pull /sdcard/logs/offlineLogs/821/logcat.log-2020-05-22-11-00-07-062
```

### 5. adb install

This command installs apps onto the robot, usually for installing your developed APK package.

**Install APK:**
```bash
adb install -r -d xx.apk  # xx.apk is the absolute path to your file
```

> **More ADB commands**: Search for "ADB usage tutorial" for more details.

## Common Issues and Solutions

### No Response in Voice Interaction

**Issue description**: When interacting with the robot via voice input, the system does not respond or fails to execute expected Actions.

**Troubleshooting steps**:
1. **Network verification**: Confirm the device is connected to the network. ASR depends on network connectivity for speech recognition processing. You can test network status using the robot network check feature.
2. **System state check**: Verify the robot is not in charging, OTA upgrade, or other system event states. These states will suspend the app.
3. **Microphone status confirmation**:
   - Verify microphone hardware is enabled
   - Confirm the current app is a secondary-developed app or Xiaobao app (microphone is enabled by default only for these app types)
   - Check whether the code calls APIs that disable the microphone
4. **User position**: Ensure the user stands in front of the robot, facing it, and within the effective recognition distance
5. **Wake-free status**: If the app is using the camera, test by disabling wake-free mode or camera usage. If confirmed as a wake-free issue, temporarily disable wake-free mode to avoid camera conflicts, or use camera stream sharing to avoid resource contention.
6. **ASR/TTS service status**: Confirm the subtitle listening bar for ASR (Automatic Speech Recognition) and TTS (Text-to-Speech) services is enabled.
7. **Action registration check**: Ensure the app has registered at least one SAY Action to avoid no response due to failed LLM action planning.

### PageAgent Lifecycle Management for Non-Activity/Fragment Development

**Issue description**: When using cross-platform frameworks like Flutter or custom UI frameworks, you cannot directly construct `PageAgent` from `Activity`/`Fragment` and must manually manage the PageAgent lifecycle.

**Solution**:
1. **Create PageAgent manually**: Use the `pageId` constructor to create a `PageAgent` instance without depending on `Activity`/`Fragment` lifecycle.
2. **Lifecycle management**: Developers must manually call PageAgent lifecycle methods at proper times:
   - **When page is shown**: Call `begin()` to activate PageAgent and enable registered Actions
   - **When page is hidden**: Call `end()` to pause PageAgent and stop Action responses
   - **When page is destroyed**: Call `destroy()` to release PageAgent resources
3. **State sync**: Keep PageAgent lifecycle state consistent with actual page visibility to avoid voice responses when the page is not visible.

**Code example**:
```kotlin
// Create PageAgent instance with a unique pageId
val pageAgent = PageAgent("your_page_id")

// Register Action (before begin())
pageAgent.registerAction(yourAction)

// Activate when page is shown
pageAgent.begin()

// Pause when page is hidden
pageAgent.end()

// Release when page is destroyed
pageAgent.destroy()
```



**Notes**:
- You must strictly call corresponding methods according to actual page lifecycle
- Avoid keeping PageAgent active after the page is destroyed
- Ensure Action registration is complete before PageAgent activation
- **Page switching management**: During page switching, you must promptly end the previous PageAgent, then create and activate a PageAgent for the new page

### Do I Need to Reimplement Features When Migrating from RobotOS to AgentOS?

**Yes, business logic migration is required.**

- **Trigger mode change**: RobotOS uses domain and skill matching → AgentOS uses Action matching
- **Code migration**: Move original business logic into Action callbacks
- **Example**: Navigation used to run after skill matching; now execute the same logic inside navigation Action callbacks

### Will AgentOS Automatically Call Xiaobao App Features?

**No. Automatic invocation is not supported and must be implemented by developers.**

- **Core principle**: AgentOS does not automatically invoke Xiaobao apps or system components
- **Development requirement**: All features must be implemented within Actions
- **Example**: For calendar queries, developers must call calendar APIs and process results instead of calling built-in Xiaobao components
- **Jump support**: You can use `AgentCore.jumpToXiaobao()` to jump to Xiaobao app home

### What If Persona and PageAgent Cannot Meet Business Requirements?

**Prioritize optimizing persona and Action design; use advanced interfaces when necessary.**

#### Basic optimization options
- **Persona optimization**: Improve persona information to enhance intelligent interaction
- **Action design**: Optimize Action logic to satisfy business feature requirements

#### Dynamic information update
- **Interface**: `uploadInterfaceInfo()`
- **Use cases**: UI changes, task progress updates, or new information that must be sent to the LLM
- **Purpose**: Update app state in real time and keep the LLM synchronized

#### Complex dialogue scenarios
- **Interfaces**: `llmSync()` and `llm()`
- **Use cases**: Complex dialogues and custom intelligent interaction requirements
- **Implementation**:

```kotlin
val introQuery = "Give a short self-introduction in under 30 words"

// Build message list
val messages = mutableListOf<LLMMessage>()

// Add system prompt
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

// Add user request
val userMessage = LLMMessage(LLMRole.USER, introQuery)
messages.add(userMessage)

val config = LLMConfig(
    temperature = 0.8f,
    maxTokens = 80  // Limit initial introduction length
)

// Generate response (stream playback, robot response is received in onTranscribe)
AgentCore.llmSync(messages, config, 20 * 1000)
```
