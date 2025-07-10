# FAQ

## 目录

1. [你需要知道的概念](#你需要知道的概念)
   - [应用授权](#应用授权)
   - [应用被挂起](#应用被挂起)
   - [应用恢复挂起](#应用恢复挂起)

2. [打开开发者模式](#打开开发者模式)
   - [背景](#背景)
   - [开启方法](#开启方法)
   - [查询动态密码](#查询动态密码)

3. [如何获取机器人原始日志](#如何获取机器人原始日志)
   - [1. 查看机器人上所有保存好的日志](#1-查看机器人上所有保存好的日志)
   - [2. 拉取指定时间段的日志](#2-拉取指定时间段的日志)

4. [常用的adb命令](#常用的adb命令)
   - [1. adb devices](#1-adb-devices)
   - [2. adb shell](#2-adb-shell)
   - [3. adb push](#3-adb-push)
   - [4. adb pull](#4-adb-pull)
   - [5. adb install](#5-adb-install)

5. [常见问题及解决方案](#常见问题及解决方案)
   - [语音交互无响应](#语音交互无响应)
   - [非Activity/Fragment开发方式的PageAgent生命周期管理](#非activityfragment开发方式的pageagent生命周期管理)

## 你需要知道的概念

### 应用授权
只有当app成功连接上RobotOS，并且app界面在前端显示时，app才能成功被授权使用sdk，当app界面退到后台，app当即被挂起。

### 应用被挂起
机器人使用中会遇到一些系统事件，比如急停，低电，OTA，硬件异常等，当发生这些系统事件时，RobotOS会接管业务，这时前台的业务apk就会被挂起，收到onsuspend事件，业务apk也不再具有使用api的能力。

### 应用恢复挂起
对应挂起事件，当系统事件消失后，RobotOS会把业务控制权交还给当前app，当前apk恢复使用RobotApi的能力。

## 打开开发者模式

### 背景

为了保证机器系统安全，以后版本默认关闭USB调试（插线adb），机器及对应版本如下：

- **豹小秘**：V6.9及以后版本
- **mini**：V6.13及以后版本

### 开启方法

出厂版本默认ADB关闭，只有通过以下方式可临时开启：

1. 在任何时候（包括自检异常），单指下拉>>狂点多次时间区域

2. 弹出动态密码输入页，该页面显示系统日期时间，动态密码的获取查看【查询动态密码】部分

   - **动态密码输入正确**：跳转至步骤三，可以进行adb设置。
   - **动态密码输入错误**：清空输入内容，停留在当前页面。

3. 当"启用调试"被打开后，显示第二个菜单"持久调试"。注意"启动调试"在重启后会恢复默认

   - "持久调试"菜单默认不显示，只有"启用调试"开启后，才显示。
   - "持久调试"显示后，默认是未开启状态，需要手动开启。
   - 当再次关闭"启用调试"开关后，"持久调试"自动设置为禁用，且菜单隐藏。
   - 以上设置重启后才能生效

   为了方便开发者，还提供"打开MIMI"，"开启系统导航栏"，"打开设置"，三个附带的快捷功能。

4. 新生产的机器人都支持wifi ADB调试，在这里还可以开启wifi adb，方便调试。

### 查询动态密码

提供SN号，联系您的售前/售后技术支持

如果需要查看打开开发者模式的视频，请访问：[打开开发者模式详细教程](https://doc.orionstar.com/blog/knowledge-base/%e6%89%93%e5%bc%80%e5%bc%80%e5%8f%91%e8%80%85%e6%a8%a1%e5%bc%8f/#undefined)

## 如何获取机器人原始日志

### 1. 查看机器人上所有保存好的日志

使用以下命令进入机器人shell并查看日志目录：

```bash
adb shell
cd /sdcard/logs/offlineLogs/821/
ls -l
```

### 2. 拉取指定时间段的日志

退出adb shell后，使用adb pull命令取出需要时间段的日志：

```bash
adb pull /sdcard/logs/offlineLogs/821/logcat.log-2020-05-22-11-00-07-062.tgz
```

> **注意**：日志文件名包含具体的时间戳，请根据实际需要的时间段选择对应的日志文件。

## 常用的adb命令

机器人开发无论使用OPK还是APK，都是基于Android进行开发，所以首先我们要保证Android环境没有问题。Android环境中比较重要的是adb命令的使用。下面列举一些常用adb命令：

### 1. adb devices

这条命令用来查询当前连接的设备号，当电脑正常连上了机器人，它会返回机器人列表，例如：

```bash
black_mac:dexlib mac$ adb devices
List of devices attached
KTS17Q080284    device
```

如果没有返回连接的机器人列表，就无法调试。首先请检查USB线是否连上了机器人，并且牢固。如果遇到其它问题，请百度搜索 "adb devices 无法返回正确结果"。

### 2. adb shell

这条命令用来进入机器人的终端shell，下面列举几个常用的adb shell命令：

**查看机器SN码：**
```bash
adb shell getprop|grep serial
```

### 3. adb push

这条命令用来向机器人推文件，在手动升级机器人的时候会用到，我们需要把升级包push到机器人对应文件夹。

```bash
adb push xxx.opk /system/vendor/opk/
```

**手动OTA升级机器：**

在adb命令正常打开，且连接上机器后，执行以下命令：

```bash
# 打开ota service
adb shell am start -n com.ainirobot.ota/.MainActivity

# 把ota包push进去，xxx为ota包所在文件路径
adb push xxxx /sdcard/ota/download/update.zip
```

包push完成后，点击机器人页面的"开始升级"

### 4. adb pull

这条命令用来从机器人取文件，当我们要取机器日志的时候会用到。

**获取机器人最近的日志：**
```bash
adb pull /sdcard/logs/offlineLogs/821/
```

**获取机器人指定时间的日志：**

1. 用此命令查看机器人上所有保存好的日志：
```bash
adb shell
cd /sdcard/logs/offlineLogs/821/
ls -l
```

2. 退出adb shell后，使用adb pull取出需要时间段的日志：
```bash
adb pull /sdcard/logs/offlineLogs/821/logcat.log-2020-05-22-11-00-07-062
```

### 5. adb install

这个命令用来向机器中安装应用，通常用来安装开发的APK包。

**安装APK：**
```bash
adb install -r -d xx.apk  # xx.apk是你文件所在的绝对路径
```

> **更多adb命令**：请自行百度搜索 "adb 使用教程" 获取更详细的信息。

## 常见问题及解决方案

### 语音交互无响应

**问题描述**：通过语音输入与机器人交互时，系统无响应或无法正常执行预期Action。

**排查步骤**：
1. **网络连接验证**：确认设备已连接网络，ASR服务依赖网络连接进行语音识别处理。可通过机器人网络检测功能测试网络状态。
2. **系统状态检查**：验证机器人当前不处于充电、OTA升级或其他系统事件状态，这些状态会导致应用被挂起。
3. **麦克风状态确认**：
   - 验证麦克风硬件状态为开启
   - 确认当前应用为二开应用或小豹应用（仅这两种应用类型麦克风默认开启）
   - 检查代码中是否调用了麦克风关闭相关API
4. **用户交互位置**：确保用户位于机器人正前方，人脸朝向机器人，距离保持在有效识别范围内
5. **免唤醒功能状态**：如应用正在调用摄像头，可通过关闭免唤醒功能或关闭摄像头调用进行测试。确认为免唤醒问题后，建议临时关闭免唤醒功能以避免与摄像头调用冲突，或采用摄像头数据流共享方式避免资源抢占。
6. **ASR/TTS服务状态**：确认ASR（自动语音识别）与TTS（文本转语音）服务的监听字幕条处于开启状态。
7. **Action注册验证**：确保应用已注册至少一个SAY Action，避免大模型Action规划失效导致无响应。

### 非Activity/Fragment开发方式的PageAgent生命周期管理

**问题描述**：使用Flutter等跨平台框架或自定义UI框架开发时，无法直接使用Activity/Fragment构造PageAgent，需要手动管理PageAgent的生命周期。

**解决方案**：
1. **手动创建PageAgent**：使用pageId构造函数创建PageAgent实例，不依赖Activity/Fragment生命周期。
2. **生命周期管理**：开发者需要在适当的时机手动调用PageAgent的生命周期方法：
   - **页面显示时**：调用`begin()`方法激活PageAgent，使注册的Action开始生效
   - **页面隐藏时**：调用`end()`方法暂停PageAgent，停止Action响应
   - **页面销毁时**：调用`destroy()`方法释放PageAgent资源
3. **状态同步**：确保PageAgent的生命周期状态与实际页面可见性保持一致，避免在页面不可见时仍然响应语音交互。

**代码示例**：
```kotlin
// 创建PageAgent实例，需要提供唯一的pageId
val pageAgent = PageAgent("your_page_id")

// 注册Action（在begin()之前）
pageAgent.registerAction(yourAction)

// 页面显示时激活
pageAgent.begin()

// 页面隐藏时暂停
pageAgent.end()

// 页面销毁时释放
pageAgent.destroy()
```



**注意事项**：
- 必须严格按照页面的实际生命周期调用对应方法
- 避免在页面已销毁后仍然保持PageAgent活跃状态
- 确保Action注册在PageAgent激活之前完成
- **页面切换管理**：在页面切换时，必须及时结束前一个PageAgent，然后创建并激活新页面的PageAgent

