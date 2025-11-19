# CHANGELOG

## [0.4.5] - 2025-11-19
### 新增
- **唤醒词模式控制接口**：新增三个唤醒模式相关接口
  - `AgentCore.enableWakeupMode(enabled: Boolean)` - 启用或禁用唤醒词模式
  - `AgentCore.setWakeupVadTimeout(timeout: Long)` - 设置VAD结束后的拾音超时时间（默认3000ms，范围1000-10000ms）
  - `AgentCore.setWakeupQuestionTimeout(timeout: Long)` - 设置AI问问题后的拾音窗口时长（默认10000ms，范围3000-30000ms）

### 功能说明
- **唤醒词模式**：启用后必须通过唤醒词唤醒才能开始拾音识别，避免误拾音
- **VAD超时控制**：用户说完话后继续保持拾音的时长，超时内继续说话无需再次唤醒
- **问题拾音窗口**：当AI说话以中文问号"？"结尾时，自动开启拾音窗口，用户可直接回答无需唤醒词

### 兼容性
- **豹小秘2机器人**: ROM V11.7, AgentOS v1.7.0
- **Mini机器人**: ROM V11.7, AgentOS v1.7.0

### 示例代码
```kotlin
// 启用唤醒词模式
AgentCore.enableWakeupMode(true)
AgentCore.setWakeupVadTimeout(5000L)
AgentCore.setWakeupQuestionTimeout(15000L)

// 禁用唤醒词模式
AgentCore.enableWakeupMode(false)
```

---

## [0.4.4] - 2025-09-26
### 新增
- SDK自动集成RobotService.jar依赖，无需手动添加

### 兼容性
- **豹小秘2机器人**: ROM V11.6.2025092601, AgentOS v1.6.0.250925.C
- **Mini机器人**: ROM V11.6.2025100912, AgentOS v1.6.0.250925C

---

## [0.3.7] - 2025-08-20
### 新增
- **TTS音频文件播放功能**: 新增 `playTtsAudioFile()` 和 `stopTtsAudioFile()` 接口
- **适配ROM合线版本兼容性**: 优化SDK与最新ROM版本的兼容性

### 兼容性
- **豹小秘2机器人**: ROM V11.4.2025082001, AgentOS V1.4.0.250818.C
- **Mini机器人**: 暂不支持

---

## [0.3.5] - 2025-07-11
### 新增
- 新增 `OnActionStatusChangedListener` 接口，监听系统自动处理的 Action 状态变化事件

### 兼容性
- **豹小秘2机器人**: ROM V10.3.2025071001
- **Mini机器人**: ROM V10.3.2025071101

### 示例代码
```kotlin
// 设置 Action 状态变化监听器
pageAgent.setOnActionStatusChangedListener { actionName, status, message ->
    println("Action: $actionName, Status: $status, Message: $message")
    true
}
```

---

## [0.3.3] - 2025-07-02
### 新增
- 新增 `jumpToXiaobao()` 方法，支持跳转到小豹应用
- 支持基础Release系统功能

### 兼容性
- **Mini机器人**: ROM V10.3.2025070215

---

## [0.2.2] - 2025-05-15
### 兼容性
- **豹小秘2机器人**: ROM V10.3.2025051917

### 状态
⚠️ **已停止维护** - 请升级到v0.3.x或更高版本