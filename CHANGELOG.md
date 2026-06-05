# CHANGELOG

## [0.4.5] - 2025-11-19
### Added
- **Wake-word mode control interfaces**: Added three wake-mode related interfaces
  - `AgentCore.enableWakeupMode(enabled: Boolean)` - Enable or disable wake-word mode
  - `AgentCore.setWakeupVadTimeout(timeout: Long)` - Set pickup timeout after VAD ends (default 3000ms, range 1000-10000ms)
  - `AgentCore.setWakeupQuestionTimeout(timeout: Long)` - Set pickup window after AI asks a question (default 10000ms, range 3000-30000ms)

### Feature Description
- **Wake-word mode**: After enabling, pickup recognition starts only after wake-word activation, preventing unintended pickup
- **VAD timeout control**: Duration to continue pickup after the user finishes speaking; speaking again within timeout does not require re-wakeup
- **Question pickup window**: When AI speech ends with a Chinese question mark "？", a pickup window is automatically opened so users can respond directly without a wake word

### Compatibility
- **Leopard Secretary 2 Robot**: ROM V11.7, AgentOS v1.7.0
- **Mini Robot**: ROM V11.7, AgentOS v1.7.0

### Sample Code
```kotlin
// Enable wake-word mode
AgentCore.enableWakeupMode(true)
AgentCore.setWakeupVadTimeout(5000L)
AgentCore.setWakeupQuestionTimeout(15000L)

// Disable wake-word mode
AgentCore.enableWakeupMode(false)
```

---

## [0.4.4] - 2025-09-26
### Added
- SDK now auto-integrates the RobotService.jar dependency; no manual addition required

### Compatibility
- **Leopard Secretary 2 Robot**: ROM V11.6.2025092601, AgentOS v1.6.0.250925.C
- **Mini Robot**: ROM V11.6.2025100912, AgentOS v1.6.0.250925C

---

## [0.3.7] - 2025-08-20
### Added
- **TTS audio file playback**: Added `playTtsAudioFile()` and `stopTtsAudioFile()` interfaces
- **ROM merged-line compatibility support**: Improved SDK compatibility with the latest ROM versions

### Compatibility
- **Leopard Secretary 2 Robot**: ROM V11.4.2025082001, AgentOS V1.4.0.250818.C
- **Mini Robot**: Not supported yet

---

## [0.3.5] - 2025-07-11
### Added
- Added `OnActionStatusChangedListener` interface to listen for Action state change events handled automatically by the system

### Compatibility
- **Leopard Secretary 2 Robot**: ROM V10.3.2025071001
- **Mini Robot**: ROM V10.3.2025071101

### Sample Code
```kotlin
// Set Action status change listener
pageAgent.setOnActionStatusChangedListener { actionName, status, message ->
    println("Action: $actionName, Status: $status, Message: $message")
    true
}
```

---

## [0.3.3] - 2025-07-02
### Added
- Added `jumpToXiaobao()` method to support navigation to the Xiaobao app
- Support for base Release system features

### Compatibility
- **Mini Robot**: ROM V10.3.2025070215

---

## [0.2.2] - 2025-05-15
### Compatibility
- **Leopard Secretary 2 Robot**: ROM V10.3.2025051917

### Status
⚠️ **No longer maintained** - Please upgrade to v0.3.x or later
