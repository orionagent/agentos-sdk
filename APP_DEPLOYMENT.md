# App Deployment

## 打开开发者模式

出厂版本默认ADB关闭，只有通过以下方式可临时开启：
1. 在任何时候（包括自检异常），单指下拉>>狂点多次时间区域
https://github.com/user-attachments/assets/37c47394-4c1e-4c8f-bb7f-6dad63fe97a6
2. 弹出动态密码输入页，该页面显示系统日期时间，动态密码的获取查看【查询动态密码】部分
- 动态密码输入正确：跳转至步骤三，可以进行adb设置。
- 动态密码输入错误：清空输入内容，停留在当前页面。

3. 当"启用调试"被打开后，显示第二个菜单"持久调试"。注意"启动调试"在重启后会恢复默认
- "持久调试"菜单默认不显示，只有"启用调试"开启后，才显示。
- "持久调试"显示后，默认是未开启状态，需要手动开启。
- 当再次关闭"启用调试"开关后，"持久调试"自动设置为禁用，且菜单隐藏。
- 以上设置重启后才能生效

## 查询动态密码

方法一：提供SN号，联系您的售前技术支持
方法二：对于二次开发代理商，访问 机器人动态密码查询

## 通过adb连接机器人

1. 可以通过Android的micro usb数据线直接连接机器人头部的接口
2. 电脑和机器人在同一个网络下可以通过adb命令 远程连接机器人
例如： adb connect 机器人IP地址:5555

## 安装APK

### 本地安装

#### 通过adb命令安装

1.    adb install 路径/文件名.apk
例如：adb install /Users/Downloads/app-debug.apk
2. 如果你想"重新安装"（即覆盖已安装的版本），可以加上 -r：
adb install -r /路径/你的.apk

#### 通过Android Studio直接安装

在Android studio下可以通过Run app按钮直接运行android工程
如果使用hotReload可能导致一些机器人能力出现问题，具体参考常见问题

### 远程安装

#### 通过ORM后台单台安装APK

#### 通过EO批量安装

## 启动APK

### 配置默认启动程序

App如果需要在开机后默认启动进入，需要在Manifest中配置
```xml
<intent-filter>
    <action android:name="action.orionstar.default.app" />
    <category android:name="android.intent.category.DEFAULT" />
</intent-filter>
```

完整的Manifest如此配置
```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.LAUNCHER"/>
     </intent-filter>
     <intent-filter>
         <action android:name="action.orionstar.default.app" />
         <category android:name="android.intent.category.DEFAULT" />
     </intent-filter>
</activity>
```

并在设置（三指下拉点击设置进入）- 开发者设置 – 开机启动程序中设置

### 如何正确的启动apk

启动APK时，为确保APK被机器人底盘正确的授权并使用机器人功能，请使用RobotOS Home调起（参见下面视频）

## 常见问题

1. 调用api返回资源使用冲突问题
sdk中提供的有关硬件使用的api，当同时使用两个接口操作同一个器件时，会发生资源使用冲突问题，比如当机器人导航时，不能使用左转右转api。
2. 通过hotReload等其他方式打开二开app可能会导致机器人RobotApi功能不可用
