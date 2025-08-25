# CHANGELOG

请在此记录 AgentOS SDK 的变更日志。

## [0.3.7] - 2025-08-20
### 新增
- **TTS音频文件播放功能**: 新增 `playTtsAudioFile()` 和 `stopTtsAudioFile()` 接口
- **适配ROM合线版本兼容性**: 优化SDK与最新ROM版本的兼容性

### 兼容性
- **豹小秘2机器人**: Release Latest系统 (ROM: V11.4.2025082001, AgentOS: V1.4.0.250818.C)
- **mini机器人**: 暂不支持

---

## [0.3.5] - 2025-07-11
### 新增
- 新增 `OnActionStatusChangedListener` 接口，监听系统自动处理的 Action 状态变化事件

### 示例代码
```kotlin
// 设置 Action 状态变化监听器
pageAgent.setOnActionStatusChangedListener { actionName, status, message ->
    println("Action: $actionName, Status: $status, Message: $message")
    true
}
```

### 兼容性
- **Mini机器人**: 稳定性优化版 (ROM: V10.3.2025071101)
- **豹小秘2机器人**: Release系统 (ROM: V10.3.2025071001)

---

## [0.3.3] - 2025-07-02
### 新增
- 新增 `jumpToXiaobao()` 方法，支持跳转到小豹应用
- 支持基础Release系统功能

### 兼容性
- **Mini机器人**: Release系统 (ROM: V10.3.2025070215)

---

## [0.2.2] - 2025-05-15
### 兼容性
- **豹小秘2机器人**: Beta系统 (ROM: V10.3.2025051917)

### 状态
⚠️ **已停止维护** - 请升级到v0.3.x版本 