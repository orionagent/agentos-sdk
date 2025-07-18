# 机器人原生接口使用参考

## RobotOS 简介

RobotOS是猎户星空基于安卓平台开发的面向多形态硬件机器人和多种业务场景组合的开放式开发平台。

RobotOS提供了丰富的机器人业务API、机器人能力组件，使开发者可以方便的开发机器人应用。

RobotOS基于安卓平台开发，同时提供了一套针对android apk的sdk，用户可以应用RobotService.jar在机器人上开发android应用，运行RobotOS的机型均支持该sdk。

## 🚨 重要：语音功能迁移说明

**在AgentOS系统上，所有语音和NLP相关功能已迁移至AgentOS SDK，RobotAPI原有语音和NLP功能已停用：**

### 已停用的RobotAPI语音和NLP功能
- **ASR（语音识别）**：原RobotAPI的语音转文字功能
- **TTS（语音合成）**：原RobotAPI的文字转语音功能  
- **NLP（自然语言处理）**：原RobotAPI的自然语言理解和处理功能

### 新的语音和大模型解决方案
**所有语音和NLP功能现已统一使用AgentOS SDK提供：**
- ✅ **免唤醒交互**：AgentOS SDK提供更智能的免唤醒语音交互
- ✅ **ASR/TTS**：通过AgentOS SDK的`AgentCore.tts()`等接口实现
- ✅ **智能对话**：集成大模型的自然语言理解和生成，替代原有NLP功能
- ✅ **Action规划**：基于用户语音意图自动规划和执行动作
- ✅ **大模型能力**：通过大模型实现更强大的自然语言理解，替代传统NLP

### 迁移指引
如果您的项目中使用了RobotAPI的语音或NLP功能，请：
1. **移除RobotAPI相关代码**：删除原有的ASR、TTS、NLP、唤醒等RobotAPI调用
2. **集成AgentOS SDK**：参考[AgentOS SDK文档](../../Agent/v0.3.5/AgentOS_SDK_Doc_v0.3.5.md)进行集成
3. **功能对照迁移**：
   - RobotAPI唤醒 → AgentOS SDK免唤醒交互
   - RobotAPI ASR → AgentOS SDK语音识别监听
   - RobotAPI TTS → AgentOS SDK `AgentCore.tts()`接口
   - RobotAPI NLP → AgentOS SDK大模型接口和Action规划

### 协作使用
**RobotAPI与AgentOS SDK协作关系：**
- **AgentOS SDK**：负责所有AI智能交互（语音交互、智能对话、大模型能力、Action规划执行）
- **RobotAPI**：负责机器人硬件控制（运动、导航、传感器、视觉等）

**RobotAPI旧语音功能参考文档**：
以下文档帮助开发者了解RobotAPI原有的语音功能，便于识别和删除项目中的旧代码：
- [语音](https://orionbase.orionstar.com/doc?m=21&h=6065a29&lang=cn#text%E8%BD%ACmp3)

## 版本

**V11.3 版本 API Latest**


## SDK接入

### 1. 导入SDK依赖

#### 添加JAR包依赖
1. 获取SDK文件：在Robot版本目录中查找 `robotservice_xx.jar` 文件（如 `Robot/v11.3C/robotservice_11.3.jar`）。SDK文件遵循 `robotservice_<version>.jar` 命名规范，请根据实际版本选择对应目录
2. 将JAR包文件复制到Android项目的 `app/libs/` 目录下
3. 在 `app/build.gradle` 文件中添加依赖配置：

```gradle
dependencies {
    implementation files('libs/robotservice_11.3.jar')
    // 其他依赖...
}
```

### 2. 配置Manifest文件

在 `AndroidManifest.xml` 文件中添加以下配置：

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

### 3. 开机启动配置（可选）

如果需要应用在机器人开机后自动启动，请按以下步骤配置：

#### 步骤1：Manifest配置
在Activity中添加以下intent-filter：

```xml
<intent-filter>
    <action android:name="action.orionstar.default.app" />
    <category android:name="android.intent.category.DEFAULT" />
</intent-filter>
```

#### 步骤2：系统设置
在机器人系统中进行设置：
1. 三指下拉进入系统设置
2. 选择"开发者设置"
3. 在"开机启动程序"中配置您的应用

> **重要提示**：开机启动程序设置功能需要OTA3及以上版本支持

### 4. 权限配置

为了确保SDK功能正常运行，需要在 `AndroidManifest.xml` 中声明以下权限：

```xml
<!-- 网络访问权限 -->
<uses-permission android:name="android.permission.INTERNET"/>
<!-- 机器人设置提供者权限 -->
<uses-permission android:name="com.ainirobot.coreservice.robotSettingProvider" />
<!-- 外部存储读写权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

## SDK接入步骤

### 1. 创建回调接口

创建接收语音请求及系统事件的回调：

```java
public class ModuleCallback extends ModuleCallbackApi {
    @Override
    public boolean onSendRequest(int reqId, String reqType, String reqText, String reqParam)
            throws RemoteException {
                //这个回调废弃
        return true;
    }
    
    @Override 
    public void onRecovery() throws RemoteException {
        // 控制权恢复，收到该事件后，重新恢复对机器人的控制
    }
    
    @Override 
    public void onSuspend() throws RemoteException {
        // 控制权被系统剥夺，收到该事件后，所有Api调用无效
    }
}
```

### 2. 连接服务器

```java
RobotApi.getInstance().connectServer(this, new ApiListener() {
    @Override
    public void handleApiDisabled() {
        // API已禁用
    }
    
    @Override
    public void handleApiConnected() {
        // Server已连接，设置接收请求的回调，包含语音指令、系统事件等

    }
    
    @Override
    public void handleApiDisconnected() {
        // 连接已断开
    }
});
```

**重要说明：**
机器人SDK的初始化，标志着机器人把底盘能力权限移交给了当前正在运行的APP。机器人会在系统接管、发生故障、启动了其它带机器人SDK程序的时候发生连接已断开的回调。其中只有系统接管结束时，会重新connect上。

> **注意1**：所有Api必须在连接server成功之后才能够调用
> 
> **注意2**：只有前台进程，在前台运行时能正确的连接并获取机器人底盘控制权限，服务进程不工作。



### 5. 注册状态监听

```java
StatusListener statusListener = new StatusListener(){
    @Override
    public void onStatusUpdate(String type, String data) throws RemoteException {
        // 处理状态更新
    }
};

// 注册状态监听
RobotApi.getInstance().registerStatusListener(type, statusListener);

// 解除状态监听
RobotApi.getInstance().unregisterStatusListener(statusListener);
```

**支持的状态类型：**

- `Definition.STATUS_POSE`：机器人当前的坐标，持续上报
- `Definition.STATUS_POSE_ESTIMATE`：当前定位状态，定位状态发生改变时上报
- `Definition.STATUS_BATTERY`：当前电池状态信息，包括是否在充电、电量多少、是否低电量报警等

### 6. 设置reqId

很多SDK的方法需要传入参数 `reqId`，这是一个用于调试、跟踪日志的id。传入任何数字都能让函数正常工作，但为了日志追踪、调试的方便，建议把 `reqId` 做成一个自增长的static变量，或者根据业务需求，传入不同的 `reqId` 来做调用函数的区分。

## 视觉能力

### 简介

视觉能力目前主要指人员检测和识别两个模块，Api提供主要集中在PersonApi和RobotApi。

人员信息检测是本地能力，人站在机器人前方（排除光线较差的情况），机器人可以检测到前方人员，人站的较远时人脸和人体均可检测到，人站的比较近的时候只能检测到人脸信息。当人员id大于等于0时，代表着该人员的人脸信息完整，可以获取到该人人脸照片进行识别。

人员识别需要使用人脸照片进行识别，该能力需要使用网络，请注意获取人员照片时，保证人员符合采取照片条件。

> **注意**：使用机器人视觉能力的时候，不可以同时使用机器人摄像头。这将会导致视觉能力报错失效

具体使用流程详见Demo，下面为API介绍：

### Person主要信息

```java
private int id; //人脸本地识别id，此id可用于焦点跟随等。
private double distance; //距离
private double faceAngleX; //人脸x轴角度
private double faceAngleY; //人脸y轴角度
private int headSpeed; //当前机器人头部转动速度
private long latency; //数据延迟
private int facewidth; //人脸宽度
private int faceheight; //人脸高度
private int faceX; //人脸x坐标
private int faceY; //人脸y坐标
private int bodyX; //身体x坐标
private int bodyY; //身体y坐标
private int bodywidth; //身体宽度
private int bodyheight; //身体高度
private double angleInView; //人相对于机器人头部的角度
private String quality; //检查质量参数
private int age; //年龄（云端注册后会返回估算值）
private String gender; //性别（按照国家规定，此项必须授权注册后才会返回结果）
private int glasses; //是否戴眼睛
private String remoteFaceId; //如果已注册，注册的人脸远端id
private String faceRegisterTime; //注册时间
//更多Person信息，可参见SDK中com.ainirobot.coreservice.client.listener.Person类定义
```
### 注册人员变化监听

**方法名称：** `registerPersonListener` / `unregisterPersonListener`

**调用方式：**

```java
PersonListener listener = new PersonListener() {
    @Override
    public void personChanged() {
        super.personChanged();
        //人员变化时，可以调用获取当前人员列表接口获取机器人视野内所有人员
    }
};

//注册人员监听
PersonApi.getInstance().registerPersonListener(listener);

//取消注册人员监听
PersonApi.getInstance().unregisterPersonListener(listener);
```

**参数说明：**

- `listener`：人员信息变化监听
### 获取全部人员信息

**方法名称：** `getAllPersons`

**调用方式：**

```java
//获取机器人视野内全部人员信息
List<Person> personList = PersonApi.getInstance().getAllPersons();
//获取机器人视野内1m范围内所有的人员信息
List personList = PersonApi.getInstance().getAllPersons(1);
```

**参数说明：**

- `maxDistance`：机器人多大视野范围内，单位为米，机器人能识别最准确的人员距离是1-3米。

**返回：**

- `personList`：人员信息列表
### 获取检测到人体的人员列表

**方法名称：** `getAllBodyList`

**调用方式：**

```java
//获取机器人视野内所有人体信息的人员列表
List<Person> personList = PersonApi.getInstance().getAllBodyList();
//获取机器人视野内1m范围内所有的人体信息的人员列表
List personList = PersonApi.getInstance().getAllBodyList(1);
```

**参数说明：**

- `maxDistance`：机器人多大视野范围内，单位为米，机器人能识别最准确的人员距离是1-3米。

**返回：**

- `personList`：人员信息列表

### 获取检测到人脸的人员列表

**方法名称：** `getAllFaceList`

**调用方式：**

```java
//获取机器人视野内所有有人脸信息的人员列表
List<Person> personList = PersonApi.getInstance().getAllFaceList();
//获取机器人视野内1m范围内所有有人脸信息的人员列表
List personList = PersonApi.getInstance().getAllFaceList(1);
```

**参数说明：**

- `maxDistance`：机器人多大视野范围内，单位为米，机器人能识别最准确的人员距离是1-3米。

**返回：**

- `personList`：人员信息列表

### 获取检测到完整人脸的人员列表

**方法名称：** `getCompleteFaceList`

**调用方式：**

```java
//获取机器人视野内所有有完整人脸信息的人员列表
List<Person> personList = PersonApi.getInstance().getCompleteFaceList();
```

**返回：**

- `personList`：人员信息列表

### 获取正在焦点跟随的人员

此方法仅在焦点跟随进行中有效

**方法名称：** `getFocusPerson`

**调用方式：**

```java
Person person = PersonApi.getInstance().getFocusPerson();
```

**返回：**

- `person`：人员信息

焦点跟随相关API，点击这里查看。

### 获取人脸照片

**方法名称：** `getPictureById`

**调用方式：**

```java
RobotApi.getInstance().getPictureById(reqId, faceId, count, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String status = json.optString("status");
            //获取照片成功
            if (Definition.RESPONSE_OK.equals(status)) {
                JSONArray pictures = json.optJSONArray("pictures");
                if (!TextUtils.isEmpty(pictures.optString(0))) {
                    //照片本地存储全路径
                    String picturePath = pictures.optString(0);
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**参数说明：**

- `faceID`：人脸id，可通过本地人脸识别获得（Person的id）
- `count`：需要获取的照片数量，此参数目前无效，默认只有一张

> **注意**：该接口保存的图片在使用完后需要手动删除

### 自动注册

**方法名称：** `startRegister`

传入姓名，该方法会实时从摄像头中获取人脸，将该人脸注册为该姓名。日常使用该方式注册人脸

**调用方式：**

```java
RobotApi.getInstance().startRegister(reqId, personName, timeout, tryCount, secondDelay, new ActionListener() {
    @Override
    public void onResult(int status, String response) throws RemoteException {
        if (Definition.RESULT_OK != status) {
            //注册失败
            return;
        }
        try {
            JSONObject json = new JSONObject(response);
            String remoteType = json.optString(Definition.REGISTER_REMOTE_TYPE);
            String remoteName = json.optString(Definition.REGISTER_REMOTE_NAME);
            if (Definition.REGISTER_REMOTE_SERVER_EXIST.equals(remoteType)) {
                //当前用户已存在
            } else if (Definition.REGISTER_REMOTE_SERVER_NEW.equals(remoteType)) {
                //新注册用户成功
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
});
```

**参数说明：**

- `personName`：注册名称
- `timeout`：注册超时时间，注册失败时会尝试重新注册，直到超时
- `tryCount`：失败重试次数，重试的时间大于timeout后，会放弃重试
- `secondDelay`：重试时间间隔

> **注意**：不要重复注册同一个人脸

### 远程注册

**方法名称：** `remoteRegister`

**调用方式：**

```java
RobotApi.getInstance().remoteRegister(reqId, name, path, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            int code = jsonObject.optInt("code", -1);
            switch (code) {
                case 0://成功
                    break;
                case Definition.REMOTE_CODE_FACE_INVALID://图片无效
                    break;
                default://其他
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
});
```

**参数说明：**

- `name`：注册名称
- `path`：图片路径，可以通过getPictureById获取

> **注意**：不要重复注册同一个人脸

### 远程识别

**方法名称：** `getPersonInfoFromNet`

**调用方式：**

```java
RobotApi.getInstance().getPersonInfoFromNet(reqId, faceId, pictures, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            JSONObject info = json.getJSONObject("people");
            info.getString("name"); //名称
            info.getString("gender"); //性别
            info.getString("age"); //年龄
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**参数说明：**

- `faceID`：人脸id，可通过本地人脸识别获得（Person的id）
- `pictures`：人脸照片，可通过getPictureById接口获得

### 切换摄像头

**方法名称：** `switchCamera`

**调用方式：**

```java
RobotApi.getInstance().switchCamera(reqId, camera, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            if (Definition.RESPONSE_OK.equals(json.getString("status"))) {
                //切换成功
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```


**参数说明：**

- `camera`：摄像头，String 类型
  - `Definition.JSON_HEAD_FORWARD` 前置摄像头
  - `Definition.JSON_HEAD_BACKWARD` 后置摄像头

## 摄像头

机器人的摄像头有的是单摄，有的是双摄；不同产品线摄像头使用不同，详见下面文档。


#### 豹小秘2和豹小秘mini

双摄，前置摄像头可以使用 Android 标准 API 打开，也可以使用共享流获取相机预览数据（通过 Android 标准 API 打开前置摄像头，会造成机器人视觉算法暂时无法使用，释放后，一定时间后会恢复视觉算法能力）。后置摄像头只能使用Android 标准 API 打开。


#### 注意

使用Android的API打开相机，第一次授权APP使用摄像头时，APP可能会崩溃，但之后使用就没有限制了。如果遇到授权摄像头崩溃，重新开启即可，之后可正常使用摄像头。

横屏机器人注意：通过Android标准方式启动之后方向是选择90°的，这是因为机器人没有陀螺仪，程序中默认是竖屏摄像头，但机器人实际是横屏设备。解决方案很简单，通过Android API（或其它各家视频sdk的API）设置摄像头旋转-90°或摄像头旋转270°即可纠正。

以下Demo为Android相机API使用，仅供参考，如不满足，请自行根据需求上网查询

Camera1的Demo

Camera2的Demo

### 摄像头数据流共享
因为机器人视觉能力VisionSDK需要使用摄像头，如果摄像头被二次开发的程序占用，会导致机器人人脸检测、人脸识别不可用。如果想既使用摄像头，又不影响机器人视觉功能，需要使用共享数据流的方式获取VisionSDK中的摄像头数据。摄像头数据通过SurfaceShareApi获取。它主要有三个关键方法：

startRequest() : 开启共享数据流
onImageAvailable(ImageReader reader) ：这是一个回调，所有共享流里的图像数据从这里出来
stopPushStream() ：关闭共享数据流
共享流方式获取图像数据内存和CPU开销都比较大，在不使用的时候一定记得关闭，避免引起OOM。共享数据流取到的图片是YUV格式的，要渲染到surfaceview需要转换为bitmap。

具体参加下面代码：

```java
package com.ainirobot.videocall.control.sufaceshareengine;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareApi;
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareBean;
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareError;
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareListener;
import com.ainirobot.videocall.tools.ImageUtils;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
/**
 * 通过Vision SDK获取视频数据
 */
public class SurfaceShareDataEngine {
    private static final String TAG = "SurfaceShareDataEngine";
    public static final int IMAGE_DATA_FOR_LIVEME = 0;
    public static final int IMAGE_DATA_FOR_LOCAL_PREVIEW = 1;
    public static final int VISION_IMAGE_WIDTH = 640;
    public static final int VISION_IMAGE_HEIGHT = 480;
    private static final int MAX_CACHE_IMAGES = 4; //maxImages 保持和VisionSdk一致
    private MessageCallBack mCallBack;
    private ImageReader mImageReader;
    private Handler mHandler;
    private HandlerThread mBackgroundThread;
    private SurfaceShareBean mSurfaceShareBean;
    private int mCurrentError = 0;
    private long log_interval = 0;
    private MyHandler mImageHandler;
    private HandlerThread mImageHandleThread;
    private Surface mRemoteSurface;
    private static class InstanceHolder {
        public static final SurfaceShareDataEngine instance = new SurfaceShareDataEngine();
    }
    public static SurfaceShareDataEngine getInstance() {
        return InstanceHolder.instance;
    }
    private SurfaceShareDataEngine() {}
    public void setMessageCallBack(MessageCallBack callBack){
        Log.i(TAG, "set message callBack: " + callBack);
        this.mCallBack = callBack;
    }
    private boolean initSurfaceResource() {
        Log.d(TAG, "initSurfaceResource ..");
        startBackgroundThread();
        boolean init = initImageSurface();
        if (!init) {
            Log.e(TAG, "Remote Surface instance has been used");
            return false;
        }
        startHandleImageThread();
        return true;
    }
    private void startHandleImageThread() {
        mImageHandleThread = new HandlerThread("ImageHandleThread");
        mImageHandleThread.start();
        mImageHandler = new MyHandler(mImageHandleThread.getLooper());
    }
    public void startRequest() {
        initSurfaceResource();
        if (mSurfaceShareBean == null) {
            mSurfaceShareBean = new SurfaceShareBean();
            mSurfaceShareBean.setName("VideoCall");
        }
        mRemoteSurface = mImageReader.getSurface();
        Log.d(TAG, "startRequest getSurface:" + mRemoteSurface);
        SurfaceShareApi.getInstance().requestImageFrame(mRemoteSurface, mSurfaceShareBean, new SurfaceShareListener() {
            @Override
            public void onError(int error, String message) {
                super.onError(error, message);
                Log.d(TAG, "requestImageFrame onError error :" + error);
                mCurrentError = error;
                if (mCallBack != null) {
                    mCallBack.onError(error);
                }
            }
            @Override
            public void onStatusUpdate(int status, String message) {
                super.onStatusUpdate(status, message);
                Log.d(TAG, "requestImageFrame onStatusUpdate status:" + status);
            }
        });
    }
    private boolean initImageSurface() {
        if (mImageReader != null) {
            Log.d(TAG, "initImageSurface has an ImageReader instance already!");
            return false;
        }
        mImageReader = ImageReader.newInstance(VISION_IMAGE_WIDTH , VISION_IMAGE_HEIGHT, ImageFormat.YUV_420_888, MAX_CACHE_IMAGES);
        mImageReader.setOnImageAvailableListener(new MyOnImageAvailableListener(), mHandler);
        return true;
    }
    private void startBackgroundThread() {
        if (mBackgroundThread != null && mHandler != null) {
            Log.d(TAG, "startBackgroundThread has already start a mBackgroundThread");
            return;
        }
        mBackgroundThread = new HandlerThread("ExternalBackground");
        mBackgroundThread.start();
        mHandler = new Handler(mBackgroundThread.getLooper());
    }
    private class MyOnImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            long startTime = System.currentTimeMillis();
            Image image = null;
            try {
                image = reader.acquireLatestImage();
                if (image == null){
                    Log.d(TAG, "onImageAvailable image is null");
                    return;
                }
                if (mBackgroundThread == null){
                    Log.d(TAG, "onImageAvailable mBackgroundThread is null");
                    return;
                }
 //以下是猎豹的业务逻辑，此处可以改为其它业务逻辑。注意处理完成后一定要释放图片，否则极容易引起OOM崩溃
                //pushYuv(image);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (log_interval % 60 == 0){
                    Log.d(TAG, "onImageAvailable push image time:"+(System.currentTimeMillis() - startTime)+"ms");
                }
                log_interval++;
                if (image != null) {
                    image.close();
                }
            }
        }
    }
    private class MyHandler extends Handler{
        MyHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null){
                Map<Integer, byte[]> data = (Map<Integer, byte[]>) msg.obj;
                if (mCallBack != null) {
                    mCallBack.onData(data);
                }
            }
        }
    }
    private void stopBackgroundThread() {
        if (mBackgroundThread == null) {
            Log.i(TAG, "the background thread is null");
            return;
        }
        Log.i(TAG, "stopBackgroundThread thread id:" + mBackgroundThread.getName() + "," + mBackgroundThread.getThreadId());
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join(50);
            mBackgroundThread = null;
            mHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void stopHandleThread() {
        if (mImageHandleThread == null) {
            Log.i(TAG, "the mImageHandleThread is null");
            return;
        }
        mImageHandleThread.quitSafely();
        try {
            mImageHandleThread.join(50);
            mImageHandleThread = null;
            mImageHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void closeImageReader() {
        Log.d(TAG, "closeImageReader == ");
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
        Log.d(TAG, "closeImageReader mRemoteSurface release");
        if (mRemoteSurface != null){
            mRemoteSurface.release();
            mRemoteSurface = null;
        }
    }
    public void stopPushStream() {
        if (mCurrentError == SurfaceShareError.ERROR_SURFACE_SHARE_USED){
            Log.d(TAG, "stopPushStream ERROR_SURFACE_SHARE_USED .");
            return;
        }
        SurfaceShareApi.getInstance().abandonImageFrame(mSurfaceShareBean);
        stopBackgroundThread();
        stopHandleThread();
        closeImageReader();
        mSurfaceShareBean = null;
        mCurrentError = 0;
        log_interval = 0;
    }
    public interface MessageCallBack{
        void onData(Map<Integer, byte[]> bytes);
        void onError(int error);
    }
}
```

在猎户的机器人代码中的逻辑代码是这样处理YUV图像的：

```java
    pushYuv(image);
    private void pushYuv(@NonNull Image image) {
        byte[] yuv = new byte[image.getHeight() * image.getWidth() * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8];
        if (log_interval % 60 == 0){
            Log.d(TAG, "pushYuv w:" + image.getWidth() + ",h:" + image.getHeight() + ",planes.len:" + image.getPlanes().length+", log_interval:"+log_interval);
        }
        readImageIntoBuffer(image, yuv);
//        frameMirror(yuv, image.getWidth(), image.getHeight());
        if (mImageHandler == null){
            Log.d(TAG, "pushYuv mImageHandler is null");
            return;
        }
        byte[] localPreviewData = ImageUtils.generateNV21Data(image);
        Map<Integer, byte[]> dataMap = new HashMap<>();
        dataMap.put(IMAGE_DATA_FOR_LIVEME, yuv);
        dataMap.put(IMAGE_DATA_FOR_LOCAL_PREVIEW, localPreviewData);
        Message.obtain(mImageHandler,0, dataMap).sendToTarget();
    }
    public static void conver_argb_to_i420(byte[] i420, byte[] argb, int width, int height) {
        final int frameSize = width * height;
        int yIndex = 0;                   // Y start index
        int uIndex = frameSize;           // U statt index
        int vIndex = frameSize * 5 / 4; // V start index: w*h*5/4
        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                a = (argb[index] & 0xff000000) >> 24; //  is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;
                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;
                // I420(YUV420p) -> YYYYYYYY UU VV
                i420[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && i % 2 == 0) {
                    i420[uIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    i420[vIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                }
                index++;
            }
        }
    }
    private static void readImageIntoBuffer(Image image, byte[] data) {
        int width = image.getWidth();
        int height = image.getHeight();
        Image.Plane[] planes = image.getPlanes();
        if (planes == null) {
            Log.i(TAG, "get image planes is null");
            return;
        }
        int offset = 0;
        int planeLen = planes.length;
        for (int plane = 0; plane < planeLen; plane++) {
            ByteBuffer buffer = planes[plane].getBuffer();
            int rowStride = planes[plane].getRowStride();
            int pixelStride = planes[plane].getPixelStride();
            int planeWidth = plane == 0 ? width : width / 2;
            int planeHeight = plane == 0 ? height : height / 2;
            if ((pixelStride == 1) && (rowStride == planeWidth)) {
                buffer.get(data, offset, planeWidth * planeHeight);
                offset += planeWidth * planeHeight;
            } else {
                byte[] rowData = new byte[rowStride];
                for (int row = 0; row < planeHeight - 1; row++) {
                    buffer.get(rowData, 0, rowStride);
                    for (int col = 0; col < planeWidth; col++) {
                        data[(offset++)] = rowData[(col * pixelStride)];
                    }
                }
                buffer.get(rowData, 0, Math.min(rowStride, buffer.remaining()));
                for (int col = 0; col < planeWidth; col++) {
                    data[(offset++)] = rowData[(col * pixelStride)];
                }
            }
        }
    }
    public static byte[] frameMirror(byte[] data, int width, int height) {
        byte tempData;
        for (int i = 0; i < height * 3 / 2; i++) {
            for (int j = 0; j < width / 2; j++) {
                tempData = data[i * width + j];
                data[i * width + j] = data[(i + 1) * width - 1 - j];
                data[(i + 1) * width - 1 - j] = tempData;
            }
        }
        return data;
    }
    public static byte[] readRgbaImageToBuffer(Image imageFrame) {
        int w = imageFrame.getWidth();
        int h = imageFrame.getHeight();
        Image.Plane[] planes = imageFrame.getPlanes();
        if (planes == null) {
            Log.i(TAG, "get image planes is null");
            return null;
        }
        ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * w;
        Log.d(TAG, "readImageIntoBuffer: rowStride:" + rowStride + ",pixelStride:" + pixelStride + ",rowPadding:" + rowPadding);
        Bitmap bitmap = Bitmap.createBitmap(w + rowPadding / pixelStride, h, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        byte[] pixels = new byte[w * h * 4]; // Allocate for RGB
        int k = 0;
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                int color = bitmap.getPixel(y, x);
                // rgba
                pixels[k * 4 + 0] = (byte) Color.red(color);
                pixels[k * 4 + 1] = (byte) Color.green(color);
                pixels[k * 4 + 2] = (byte) Color.blue(color);
                pixels[k * 4 + 3] = (byte) Color.alpha(color);
                k++;
            }
        }
        return pixels;
    }
```

SurfaceShareDataEngine.java

## 基础运动

### 直线运动

**方法名称：** `goForward` / `goBackward`

**调用方式：**

```java
CommandListener motionListener = new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            //调用成功
        } else {
            //调用失败
        }
    }
};
RobotApi.getInstance().goForward(reqId, speed, motionListener);
RobotApi.getInstance().goForward(reqId, speed, distance, motionListener);
RobotApi.getInstance().goForward(reqId, speed, distance, avoid, motionListener);
RobotApi.getInstance().goBackward(reqId, speed, motionListener);
RobotApi.getInstance().goBackward(reqId, speed, distance, motionListener);
```

**参数说明：**

- `speed`：运动速度，单位m/s，范围为 0 ~ 1.0, 大于1.0的速度按1.0运动
- `distance`：运动距离，单位m，值需大于0。
- `avoid`：是否行走的时候进行避停，只有前进可以避停。
### 旋转运动

**方法名称：** `turnLeft` / `turnRight`

**调用方式：**

```java
CommandListener rotateListener = new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            //调用成功
        } else {
            //调用失败
        }
    }
};
RobotApi.getInstance().turnLeft(reqId, speed, rotateListener);
RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener);
RobotApi.getInstance().turnRight(reqId, speed, rotateListener);
RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener);
```

**参数说明：**

- `speed`：旋转速度，单位：度/s，范围为 0 ~ 50 度/s
- `angle`：旋转角度，单位：度，值需大于0
### 通过设定角速度、线速度的方式控制机器人

**方法名称：** `motionArcWithObstacles`

**调用方式：**

```java
RobotApi.getInstance().motionArcWithObstacles(reqID,0.5,0.5,new CommandListener(){
    @Override
    public void onResult(int result, String message, String extraData) {
    }
    @Override
    public void onStatusUpdate(int status, String data, String extraData) {
    }
});
```

**参数说明：**

- `lineSpeed`：线速度，范围 -1.2 ~ 1.2
- `angularSpeed`：角速度，范围 -2 ~ 2
### 停止

**方法名称：** `stopMove`

**调用方式：**

```java
RobotApi.getInstance().stopMove(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            //调用成功
        } else {
            //调用失败
        }
    }
});
```

> **注意：** 该接口只可用于停止前进、后退及旋转，不可用于停止导航及头部运动

### 头部云台运动

**方法名称：** `moveHead`

**调用方式：**

```java
RobotApi.getInstance().moveHead(reqId, hMode, vMode, hAngle, vAngle, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String status = json.getString("status");
            if (Definition.CMD_STATUS_OK.equals(status)) {
                //操作成功
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**参数说明：**

- `hMode`：左右转动的模式，绝对运动：absolute，相对运动：relative
- `vMode`：上下运动的模式，绝对运动：absolute，相对运动：relative
- `hAngle`：左右转动角度，范围：-120 ~ 120
- `vAngle`：上下运动的角度，范围：0 ~ 90

> **注意：** 请注意机器类型的物理型限制，mini、豹小秘2机型左右转动模式无效

### 恢复云台初始角度

**方法名称：** `resetHead`

**调用方式：**

```java
RobotApi.getInstance().resetHead(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String status = json.getString("status");
            if (Definition.CMD_STATUS_OK.equals(status)) {
                //操作成功
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

## 地图及位置

### 简介

地图和定位是机器人导航的先决条件，新建地图相当于告知机器人可行走的范围，定位相当于告知机器人目前所处的位置。机器人自带"地图工具"可以完成所有地图和点位的操作，当然你也可以自己使用api完成功能

**设点：** 指告知机器人当前点位的名称，之后可使用接口导航到这个点位

### 建图及定位

建图及定位可使用系统集成的地图工具进行操作.

> 地图坐标中，xy是机器人在地图中的位置，theta是机器人的面朝方向（单位为弧度）

### 定位（设置机器人初始坐标点）

**方法名称：** `setPoseEstimate`

**调用方式：**

```java
try {
    JSONObject params = new JSONObject();
    //x坐标
    params.put(Definition.JSON_NAVI_POSITION_X, x);
    //y坐标
    params.put(Definition.JSON_NAVI_POSITION_Y, y);
    //z坐标
    params.put(Definition.JSON_NAVI_POSITION_THETA, theta);
    RobotApi.getInstance().setPoseEstimate(reqId, params.toString(), new CommandListener() {
        @Override
        public void onResult(int result, String message) {
            if ("succeed".equals(message)) {
                //定位成功
            }
        }
    });
} catch (JSONException e) {
    e.printStackTrace();
}
```
### 判断当前是否已定位

**方法名称：** `isRobotEstimate`

**调用方式：**

```java
RobotApi.getInstance().isRobotEstimate(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if (!"true".equals(message)) {
            //当前未定位
        } else {
            //当前已定位
        }
    }
});
```
### 设置当前位置名称

**方法名称：** `setLocation`

**调用方式：**

```java
RobotApi.getInstance().setLocation(reqId, placeName, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            //保存位置点成功
        } else {
            //保存位置点失败
        }
    }
});
```

**参数说明：**

- `placeName`：位置名称

> **注意：** 调用该接口前需要确保已定位

### 根据位置名称获取坐标点

**方法名称：** `getLocation`

**调用方式：**

```java
RobotApi.getInstance().getLocation(reqId, placeName, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            //当前位置是否存在
            boolean isExist = json.getBoolean(Definition.JSON_NAVI_SITE_EXIST);
            if (isExist) {
                //x坐标
                double x = json.getDouble(Definition.JSON_NAVI_POSITION_X);
                //y坐标
                double y = json.getDouble(Definition.JSON_NAVI_POSITION_Y);
                //面朝方向
                double z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**参数说明：**

- `placeName`：位置名称

> **注意：** setLocation保存的位置会与地图关联，通过getLocation获取的时候也应该保持相同的地图，否则getLocation获取失败

### 获取当前地图所有位置点

**方法名称：** `getPlaceList`

**调用方式：**

```java
RobotApi.getInstance().getPlaceList(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONArray jsonArray = new JSONArray(message);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                //常用
                json.getString("name"); //位置名称
                json.getDouble("x"); //x坐标
                json.getDouble("y"); //y坐标
                //不常用
                json.getDouble("theta"); //面朝方向
                json.getString("id"); //位置id
                json.getLong("time");//更新时间
                json.getInt("status"); //0:正常区域，可以到 1:禁行区，不可以到 2:地图外，不可以到
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```
### 获取机器人当前坐标点

**方法名称：** `getPosition`

**调用方式：**

```java
RobotApi.getInstance().getPosition(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            //x坐标
            double x = json.getDouble(Definition.JSON_NAVI_POSITION_X);
            //y坐标
            double y = json.getDouble(Definition.JSON_NAVI_POSITION_Y);
            //面朝方向
            double z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

> **注意：** 调用该接口前需要确保已定位

### 判断机器人是否在位置点

**方法名称：** `isRobotInLocations`

**调用方式：**

```java
try {
    JSONObject params = new JSONObject();
    params.put(Definition.JSON_NAVI_TARGET_PLACE_NAME, placeName); //位置名称
    params.put(Definition.JSON_NAVI_COORDINATE_DEVIATION, range); //位置范围
    RobotApi.getInstance().isRobotInlocations(reqId,
            params.toString(), new CommandListener() {
                @Override
                public void onResult(int result, String message) {
                    try {
                        JSONObject json = new JSONObject(message);
                        //是否在目标点
                        json.getBoolean(Definition.JSON_NAVI_IS_IN_LOCATION);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
} catch (JSONException e) {
    e.printStackTrace();
}
```

**参数说明：**

- `placeName`：位置名称
- `range`：位置范围，单位m
### 获取当前地图名称

**方法名称：** `getMapName`

**调用方式：**

```java
RobotApi.getInstance().getMapName(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if (!TextUtils.isEmpty(message)) {
            //message为地图名称
            String mapName = message;
        }
    }
});
```
### 位置状态改变监听

启动一个监听回调，在位置和位置状态发生改变时触发回调。

```java
public class Pose {
    public float px, py, theta;
    public final long time;
    public String name;
    /**
     * FREE = 0;      // 正常区域，可以到
     * OBSTACLE = 1;  // 禁行区，不可以到
     * OUTSIDE = 2;   // 地图外，不可以到
     */
    public int status;
    public float distance;
}
RobotApi.getInstance().registerStatusListener(Definition.STATUS_POSE,
    new StatusListener(){
        @Override
        public void onStatusUpdate(String type, String value) {
            Pose pose = GsonUtil.fromJson(value, Pose.class);
        }
    });
```
### 切换地图

**方法名称：** `switchMap`

**调用方式：**

```java
RobotApi.getInstance().switchMap(reqId, mapName, new CommandListener(){
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            //切换地图成功
        }
    }
});
```

**参数说明：**

- `mapName`：地图名称

> **注意：** 切换地图后需要重新定位

### 读取地图PGM文件并进展示（包含点位坐标转换）

读取地图和坐标转换是一整套地图工具在支撑，示例代码如下：

```java
/*
 * getMap
 * 获得当前地图
 * 地图具有当前机器人位置信息，方向坐标与可点击导航的位置点位
 * Get the current map
 * The map has current robot position information, direction coordinates and clickable navigation positions
 * 共享内存获取 map.pgm 文件
 * */
private void getMap(final String name) {
    Log.d(TAG, "getMapPgmPFD: mapName=" + name);
    //获取 map.pgm 文件描述符
    ParcelFileDescriptor mapPgmPFD = ShareMemoryApi.getInstance().getMapPgmPFD(name);
    FileDescriptor fd = mapPgmPFD.getFileDescriptor();
    FileInputStream fileInputStream = new FileInputStream(fd);
    //从文件描述符读取数据流，解析为 RoverMap（此逻辑和之前一致）
    mRoverMap = MapppUtils.loadPFD2RoverMap(fileInputStream);
    
    //TODO 自定义操作
    
    //释放 service 层资源
    ShareMemoryApi.getInstance().releaseGetMapPgmPFD();
}

/**
 * 共享内存修改 map.pgm 文件，测试代码
 */
private void setMapPgmPFD(String mapName，RoverMap roverMap) {
    byte[] bytes = MapUtils.saveRoverMapToPFDData(roverMap);
    boolean result = ShareMemoryApi.getInstance().setMapPgmPFD(mapName, bytesT);
    Log.d(TAG, "setMapPgmPFD: result=" + result);
}
```

如果有相关需求，请拷贝或参考示例代码中NavFragment部分代码，最终可把机器人中的地图、点位信息读出并展示成如下形式。下载代码

### 打开/关闭 雷达数据上报

**方法名称：** `setNavigationLineData`

**调用方式：**

```java
RobotApi.getInstance().setNavigationLineData(reqId, enabled);
```

**参数说明：**

- `enabled`：true则启用雷达数据上报；false则关闭雷达数据上报

### 雷达数据监听

```java
RobotApi.getInstance().registerStatusListener(Definition.STATUS_LINE_DATA,
    new StatusListener(){
        @Override
        public void onStatusUpdate(String type, String value) {
            List<Pose> list = GsonUtil.getGson().fromJson(data, new TypeToken<List<Pose>>() {
            }.getType());
        }
    });
```

> **注意：** 需要setNavigationLineData开启雷达数据上报，才会有数据；使用完毕后，关闭雷达数据上报，且unregisterStatusListener，释放资源

## 导航

### 介绍

导航指机器人的行走能力，机器人可以从A点行动至B点，行走过程中可以自动规划路线，可以有效避开障碍物。

机器人导航所使用的传感器比较多，包括底部激光雷达，RGBD，头部IR传感器（部分机型）等，所以在导航中请不要遮挡这些传感器，以免出现机器人不走，路径规划失败等问题。

> **重要提示：** 机器人能执行导航动作的前提是：机器人已新建地图，并且在该地图上已经定位成功，雷达处于开启状态，请一定要注意这一点！

### 导航状态和错误码定义

导航系列API中可能出现的状态和错误码对照表

#### 导航状态定义（对应导航navigationListener的onStatusUpdate）

```java
STATUS_START_NAVIGATION = 1014; //开始导航
STATUS_START_CRUISE = 1015; //开始巡航
STATUS_NAVI_AVOID = 1018; //开始避障
STATUS_NAVI_AVOID_END = 1019; //避障结束
STATUS_NAVI_OUT_MAP = 1020; //走出地图，建议调用stopNavigation停止导航
STATUS_NAVI_MULTI_ROBOT_WAITING = 1034; //多机调度等待
STATUS_NAVI_MULTI_ROBOT_WAITING_END = 1035; //多机调度等待结束
STATUS_NAVI_GO_STRAIGHT = 1036; //开始直行
STATUS_NAVI_TURN_LEFT = 1037; //开始左转弯
STATUS_NAVI_TURN_RIGHT = 1038; //开始右转弯
STATUS_NAVI_GLOBAL_PATH_FAILED = 1025; //路径规划失败，导航失败，需要处理报错，建议调用stopNavigation停止导航
```

#### 导航错误码定义（对应导航navigationListener的onError）

```java
ERROR_DESTINATION_NOT_EXIST = -108; //目的地不存在
ERROR_DESTINATION_CAN_NOT_ARRAIVE = -109; //避障超时，目的地不可达
ERROR_IN_DESTINATION = -113; //当前已在目的地周围
ERROR_NOT_ESTIMATE = -116; //当前未定位
ERROR_MULTI_ROBOT_WAITING_TIMEOUT = -125; //多机调度等待超时
ERROR_NAVIGATION_FAILED = -120; //导航因其它原因失败，兜底失败
ACTION_RESPONSE_ALREADY_RUN = -1; //当前接口已经调用，请先停止，才能再次调用
ACTION_RESPONSE_REQUEST_RES_ERROR = -6; //已经有需要控制底盘的接口调用，请先停止，才能继续调用
```

### 导航到指定位置

**方法名称：** `startNavigation`

**结果回调：**

ActionListener navigationListener = new ActionListener() {    
        @Override
        public void onResult(int status, String response) throws RemoteException {
            switch (status) {
                case Definition.RESULT_OK:
                    if ("true".equals(response)) {
                        //导航成功
                    } else {
                        //导航失败
                    }
                    break;
               case Definition.ACTION_RESPONSE_STOP_SUCCESS://取消导航任务成功
                    break;
            }
        }    
        @Override public void onError(int errorCode, String errorString) throws RemoteException     {         
            switch (errorCode) {             
                case Definition.ERROR_NOT_ESTIMATE:              //当前未定位              
                    break;              
                case Definition.ERROR_IN_DESTINATION:              //当前机器人已经在目的地范围内              
                    break;              
                case Definition.ERROR_DESTINATION_NOT_EXIST:              //导航目的地不存在              
                    break;              
                case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE://避障超时，目的地不能到达，超时时间通过参数设置              
                    break;              
                case Definition.ACTION_RESPONSE_ALREADY_RUN:           //当前接口已经调用，请先停止，才能再次调用                                                                          
                    break;              
                case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR://已经有需要控制底盘的接口调用，请先停止，才能继续调用              
                    break;          
            }      
        }
        @Override      
        public void onStatusUpdate(int status, String data, String extraData) {                                 
            switch (status) {               
                case Definition.STATUS_NAVI_AVOID:               //当前路线已经被障碍物堵死               
                    break;                           
                case Definition.STATUS_NAVI_AVOID_END:               //障碍物已移除               
                    break;         
            }      
        }
}
**调用方式：**

1. **默认导航速度**
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, time, navigationListener);
```

2. **默认导航速度，指定避障距离**（如果此值设置为 0，机器人默认会使用默认值 0.75）*该调用方式在V10版本开始支持*
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, obsDistance, time, navigationListener);
```

3. **指定导航速度** *该调用方式在V4.12版本开始支持*
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, time, linearSpeed, angularSpeed, navigationListener);
```

4. **指定导航速度，指定避障距离**（如果此值设置为 0，机器人默认会使用默认值 0.75）*该调用方式在V10版本开始支持*
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, obsDistance, time, linearSpeed, angularSpeed, navigationListener);
```

5. **指定坐标点** *该调用方式在V4.12版本开始支持*
```java
RobotApi.getInstance().startNavigation(reqId, pose, coordinateDeviation, time, navigationListener);
```

6. **指定坐标点，指定避障距离**（如果此值设置为 0，机器人默认会使用默认值 0.75）*该调用方式在V10版本开始支持*
```java
RobotApi.getInstance().startNavigation(reqId, pose, coordinateDeviation, obsDistance, time, navigationListener);
```

7. **指定导航加速度** *该调用方式在V4.12版本开始支持*（此接口暂时仅支持招财豹）
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, time, linearSpeed, angularSpeed, isAdjustAngle, destinationRange, wheelOverCurrentRetryCount, multipleWaitTime, priority, linearAcceleration, angularAcceleration, navigationListener);
```

**参数说明：**

- `destName`：导航目的地名称（必须先通过setLocation设置）
- `pose`：导航目的地坐标点
- `obsDistance`：最大避障距离，距离目标的障碍物小于该值时，机器人停止，取值大于 0，默认 0.75，单位米。
- `coordinateDeviation`：目的地范围，如果距离目的地在该范围内，则认为已到达，建议设置为0.2，单位为米。
- `time`：避障超时时间，如果该时间内机器人的移动距离不超过0.1m，则导航失败，单位毫秒，建议30*1000。
- `linearSpeed`：导航线速度，范围：0.1 ~ 0.85 m/s 默认值：0.7 m/s。
- `angularSpeed`：导航角速度，范围：0.4 ~ 1.4 m/s 默认值：1.2 m/s 最终导航速度是结合线速度和角速度换算后得到，不同的线速度和角速度对导航运动方式有影响，建议线速度和角速度保持一定规律：angularSpeed = 0.4 + (linearSpeed – 0.1) / 3 * 4
- `isAdjustAngle`：是否适应导航结束时朝向的角度。如传false，则归正到点位设置时的角度
- `destinationRange`：目标点无法到达时，距离目标点多少距离即认为导航成功
- `wheelOverCurrentRetryCount`：导航过程中轮子堵转尝试次数
- `multipleWaitTime`：导航过程中如遇多机等待
- `priority`：默认取 0 即可，取值范围0~30，值越大优先级越高；一般用于多个机器人在导航时候避让使用
- `linearAcceleration`：导航线加速度，范围：0.4 ~ 0.8 m/s2 默认值：0.7 m/s2。
- `angularAcceleration`：导航角加速度，范围：0.4 ~ 0.9 m/s2 默认值：0.8 m/s2 最终导航角加速度速度是通过线速度换算后得到，不同的线加速度和角加速度对导航运动方式有影响，建议线加速度和角加速度保持一定规律：angularAcceleration= (linearSpeed / 0.8)

> **注意：** 调用该接口前需要确保已定位

### 停止导航到指定位置

**方法名称：** `stopNavigation`

**调用方式：**

```java
RobotApi.getInstance().stopNavigation(reqId);
```

> **注意：** 该接口只能用于停止startNavigation启动的导航

### 导航到指定坐标点

> **注意：** 该方法已经废弃，建议使用startNavigation，goPosition接口不再做维护

**方法名称：** `goPosition`

**结果回调：**

```java
CommandListener goPositionListener = new CommandListener() {
    @Override
    public void onResult(int status, String response) {
        switch (status) {
            case Definition.RESULT_OK:
                if ("true".equals(response)) {
                    //导航成功
                } else {
                    //导航失败
                }
                break;
        }
    }
    @Override
    public void onError(int errorCode, String errorString) throws RemoteException {
        switch (errorCode) {
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                //当前接口已经调用，请先停止，才能再次调用
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                //已经有需要控制底盘的接口调用，请先停止，才能继续调用
                break;
        }
    }
    @Override 
    public void onStatusUpdate(int status, String data) {
        switch (status) {
            case Definition.STATUS_NAVI_AVOID:
                //当前路线已经被障碍物堵死
                break;
            case Definition.STATUS_NAVI_AVOID_END:
                //障碍物已移除
                break;
        }
    }
};
```

**调用方式：**

1. **默认速度**

```java
try {
    JSONObject position = new JSONObject();
    //x坐标
    position.put(Definition.JSON_NAVI_POSITION_X, x);
    //y坐标
    position.put(Definition.JSON_NAVI_POSITION_Y, y);
    //z坐标
    position.put(Definition.JSON_NAVI_POSITION_THETA, theta);
    RobotApi.getInstance().goPosition(reqId, position.toString(), goPositionListener);
} catch (JSONException e) {
    e.printStackTrace();
}
```

2. **指定速度** *该调用方式在V4.12版本开始支持*

```java
try {
    JSONObject position = new JSONObject();
    //x坐标
    position.put(Definition.JSON_NAVI_POSITION_X, x);
    //y坐标
    position.put(Definition.JSON_NAVI_POSITION_Y, y);
    //z坐标
    position.put(Definition.JSON_NAVI_POSITION_THETA, theta);
    RobotApi.getInstance().goPosition(reqId, position.toString(), linearSpeed, angularSpeed, goPositionListener);
} catch (JSONException e) {
    e.printStackTrace();
}
```

**参数说明：**

- `position`：参数为json格式的坐标点 `{ "x": "x坐标", "y": "y坐标", "theta": "z坐标" }`
- `linearSpeed`：导航线速度，范围：0.1 ~ 0.85 m/s 默认值：0.7 m/s
- `angularSpeed`：导航角速度，范围：0.4 ~ 1.4 m/s 默认值：1.2 m/s

> **注意：** 最终导航速度是结合线速度和角速度换算后得到，不同的线速度和角速度对导航运动方式有影响，建议线速度和角速度保持一定规律：angularSpeed = 0.4 + (linearSpeed – 0.1) / 3 * 4

> **注意：** 调用该接口前需要确保已定位

### 停止导航到指定坐标点

**方法名称：** `stopGoPosition`

**调用方式：**

```java
RobotApi.getInstance().stopGoPosition(reqId);
```

> **注意：** goPosition 与 startNavigation 两个启动导航的接口，都有一个对应的停止接口，必须一一对应，不能混用

---

### 转向目标点方向

**方法名称：** `resumeSpecialPlaceTheta`

**方法说明：** 该接口只会左右转动到目标点方位，不会实际运动到目标点。

**调用方式：**

```java
RobotApi.getInstance().resumeSpecialPlaceTheta(reqId,
    placeName,
    new CommandListener() {
        @Override
        public void onResponse(int result, String message) {
        }
    });
```

**参数说明：**

- `reqId`：int类型 命令id
- `placeName`：String类型 目标点名称
- `listener`：CommandListener类型 消息回调

**返回值：**

- `int result`：0 命令执行 / -1 没有执行

> **注意：** 调用该接口前需要确保已定位

### 计算巡线模式下，两点之间的距离

使用这个API可以获取到两点之间的距离（仅用于计算在巡线模式下，巡线路径上的两个点之间的距离）

**方法名称：** `getNaviPathInfo`

**方法说明：** 用于计算在巡线模式下，巡线路径上的两个点之间的距离。传入的点位信息可以是巡线路径上的任意点，可通过获取点位的API来获取地图上标定的点位的信息。

**调用方式：**

```java
// 起点和终点都必须在巡线路经上，机器人开启巡线模式
Pose startPos = new Pose();
startPos.setX(-0.22329703f);
startPos.setY(1.1073834f);
startPos.setTheta(-1.2297891f);

Pose endPos = new Pose();
endPos.setX(0.09533833f);
endPos.setY(-0.7406802f);
endPos.setTheta(-2.886187f);

// 使用这个API获取路径长度，单位米
RobotApi.getInstance().getNaviPathInfo(reqID,
    startPos,
    endPos,
    new CommandListener() {
        @Override
        public void onResult(int status, String response, String extraData) {
            try {
                JSONObject json = new JSONObject(response);
                double pathLength = json.getDouble("pathLength");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void onError(int errorCode, String errorString, String extraData) {
            Log.d("OnError", errorCode);
        }
    });
```
---

### 设置机器人加速减速模式

开发招财豹时，可对机器人加速和刹车加速度进行全局设置，以适应当前的业务需求。设置方法如下：

#### 1. 在APP中添加权限

```xml
<uses-permission android:name="com.ainirobot.coreservice.robotSettingProvider" />
```

#### 2. 在代码中使用方法来更改设置

```java
public class NavigationSettings {
    // value "1" - "3": 1柔和 2正常 3急迫
    public static final String ROBOT_SETTING_NAVIGATION_BREAK_MODE_LEVEL = "robot_setting_navigation_break_mode_level";
    // value "1" - "3": 1柔和 2正常 3急迫
    public static final String ROBOT_SETTING_NAVIGATION_START_MODE_LEVEL = "robot_setting_navigation_start_mode_level";
    
    public NavigationSettings() {
    }
    
    public void setBreakModeLevel(int i) {
        i = i > 3 ? 3 : Math.max(i, 1);
        putString(ROBOT_SETTING_NAVIGATION_BREAK_MODE_LEVEL, "" + i);
    }
    
    public void setStartModeLevel(int i) {
        i = i > 3 ? 3 : Math.max(i, 1);
        putString(ROBOT_SETTING_NAVIGATION_START_MODE_LEVEL, "" + i);
    }
    
    private void putString(String key, String value) {
        if (value.equals("")) {
            return;
        }
        try {
            sendSettingBroadcast(key, value);
            // Settings.Global.putString(mContext.getContentResolver(), key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendSettingBroadcast(String key, String value) {
        RobotSettingApi.getInstance().setRobotString(key, value);
    }
}
```

## 梯控导航

### 介绍

梯控导航指机器人的行走能力，机器人可以从A点行动至B点，行走过程中可以自动规划路线，不同楼层自动切换地图，可以有效避开障碍物。

机器人导航所使用的传感器比较多，包括底部激光雷达，RGBD，头部IR传感器（部分机型）等，所以在导航中请不要遮挡这些传感器，以免出现机器人不走，路径规划失败等问题。

**机器人能执行导航动作的前提条件：**

- 机器人已新建地图，并且在该地图上已经定位成功
- 机器人必须建立巡线
- 机器人必须处于巡线的点位之上
- 机器人的地图不能使用复制的地图，会造成点位ID，楼层冲突，无法到达
- 雷达处于开启状态
- 仅支持梯控机器人

> **重要提示：** 请一定要注意以上前提条件！

> **注意：** 使用卡高梯控 ROM 需要10.3及以上；使用自研梯控 ROM 需要10.5及以上。当前机器使用梯控类型请咨询售前。

### 获取多地图点位数据信息

**方法名称：** `getMultiFloorConfigAndPose`

**调用方式：**

```java
// 获取多地图点位列表
RobotApi.getInstance().getMultiFloorConfigAndPose(Definition.DEBUG_REQ_ID, new CommandListener() {
    @Override
    public void onResult(int result, String message, String extraData) {
        Type type = new TypeToken<ArrayList<MultiFloorInfo>>() {
        }.getType();
        try {
            Log.d(TAG, "initAllMapInfo: " + result + " msg:" + message);
            mDatas = new Gson().fromJson(message, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
});
```
### 导航到指定位置

**方法名称：** `startElevatorNavigation`

**调用方式：**

```java
RobotApi.getInstance().startElevatorNavigation(Definition.DEBUG_REQ_ID,
        dest,
        floor,
        new ActionListener() {
            @Override
            public void onResult(int status, String message, String extraData) throws RemoteException {
                Log.e("mName", "onResult: " + status + " responseString：" + message + " extraData：" + extraData);
                handlerResult(status, message, extraData);
            }
            
            @Override
            public void onError(int errorCode, String errorString, String extraData) throws RemoteException {
                Log.e("mName", "onError: " + errorCode + " errorString：" + errorString + " extraData：" + extraData);
                handlerError(errorCode, errorString, extraData);
            }
            
            @Override
            public void onStatusUpdate(int status, String data, String extraData) throws RemoteException {
                Log.e("mName", "onStatusUpdate: " + status + " data：" + data + " extraData：" + extraData);
                handlerStatusUpdate(status, data, extraData);
            }
        });
```
**handlerResult 方法：**

```java
private void handlerResult(int status, String message, String extraData) {
    switch (status) {
        case Definition.RESULT_OK:
            // stop(ComponentResult.RESULT_NAVIGATION_ARRIVED, message, extraData);
            break;
        case Definition.ACTION_RESPONSE_STOP_SUCCESS:
            // stop(status, message + " 停止成功", extraData);
            break;
        case Definition.RESULT_FAILURE:
            // stop(status, message + " 导航失败", extraData);
            break;
        case Definition.STATUS_NAVI_OUT_MAP:
            // stop(ComponentError.ERROR_NAVIGATION_OUT_MAP, message, extraData);
            break;
        case Definition.STATUS_NAVI_GLOBAL_PATH_FAILED:
            // stop(ComponentError.ERROR_NAVIGATION_GLOBAL_PATH_FAILED, message, extraData);
            break;
        case Definition.STATUS_NAVI_MULTI_MAP_NOT_MATCH:
        case Definition.STATUS_NAVI_MULTI_LORA_DISCONNECT:
        case Definition.STATUS_NAVI_MULTI_LORA_CONFIG_FAIL:
        case Definition.STATUS_NAVI_MULTI_VERSION_NOT_MATCH:
            // stop(ComponentError.ERROR_MULTIPLE_MODE_ERROR, message, extraData);
            break;
        case Definition.STATUS_NAVI_WHEEL_SLIP:
            // stop(ComponentError.ERROR_NAVI_WHEEL_SLIP, message, extraData);
            break;
        default:
            // stop(status, message, extraData);
            break;
    }
}
```
**handlerError 方法：**

```java
private void handlerError(int errorCode, String message, String extraData) {
    switch (errorCode) {
        case Definition.ERROR_NOT_ESTIMATE:
            // stop(ComponentError.ERROR_NOT_ESTIMATE, "navigation not estimate", extraData);
            break;
        case Definition.ERROR_IN_DESTINATION:
            // stop(ComponentError.ERROR_NAVIGATION_ALREADY_IN_DESTINATION, "already in destination", extraData);
            break;
        case Definition.ERROR_DESTINATION_NOT_EXIST:
            // stop(ComponentError.ERROR_DESTINATION_NOT_EXIST, "destination not exist", extraData);
            break;
        case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE:
            // stop(ComponentError.ERROR_DESTINATION_CAN_NOT_ARRIVE, "navigation moving time out", extraData);
            break;
        case Definition.ERROR_MULTI_ROBOT_WAITING_TIMEOUT:
            // stop(ComponentError.ERROR_MULTI_ROBOT_WAITING_TIMEOUT);
            break;
        case Definition.ERROR_NAVIGATION_AVOID_TIMEOUT:
            // stop(ComponentError.ERROR_NAVIGATION_AVOID_TIMEOUT, "navigation avoid time out", "");
        case Definition.ACTION_RESPONSE_ALREADY_RUN:
            // stop(ComponentError.ERROR_REQUEST_RES_BUSY);
            break;
        case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
            // stop(ComponentError.ERROR_REQUEST_RES_FAILED);
            break;
        case Definition.ERROR_WHEEL_OVER_CURRENT_RUN_OUT:
            // stop(ComponentError.ERROR_WHEEL_OVER_CURRENT_RUN_OUT, "wheel over current retry count run out", extraData);
            break;
        case Definition.ACTION_RESPONSE_RES_UNAVAILBALE:
            // stop(ComponentError.ERROR_OPEN_RADAR_FAILED, "res unavailable: " + message, extraData);
            break;
        default:
            // stop(errorCode, message, extraData);
            break;
    }
}
```
**handlerStatusUpdate 方法：**

```java
private void handlerStatusUpdate(int status, String data, String extraData) {
    switch (status) {
        case Definition.STATUS_GOAL_OCCLUDED:
        case Definition.STATUS_NAVI_AVOID:
            // updateStatus(ComponentStatus.STATUS_NAVIGATION_AVOID_START,"navigation avoid start", extraData);
            break;
        case Definition.STATUS_GOAL_OCCLUDED_END:
        case Definition.STATUS_NAVI_AVOID_END:
            // updateStatus(ComponentStatus.STATUS_NAVIGATION_AVOID_END,"navigation avoid end", extraData);
            break;
        case Definition.STATUS_NAVI_OBSTACLES_AVOID:
            // updateStatus(ComponentStatus.STATUS_OBSTACLES_AVOID,"pause to obstacles avoid", extraData);
            break;
        case Definition.STATUS_START_NAVIGATION:
            // updateStatus(ComponentStatus.STATUS_START_NAVIGATION,"start navigation");
            break;
        case Definition.STATUS_NAVI_MULTI_ROBOT_WAITING:
            // updateStatus(ComponentStatus.STATUS_NAVI_MULTI_ROBOT_WAITING,"navigation multi robot waiting");
            break;
        case Definition.STATUS_NAVI_MULTI_ROBOT_WAITING_END:
            // updateStatus(ComponentStatus.STATUS_NAVI_MULTI_ROBOT_WAITING_END,"navigation multi robot waiting end");
            break;
        case Definition.STATUS_NAVI_GO_STRAIGHT:
            // updateStatus(ComponentStatus.STATUS_NAVIGATION_GO_STRAIGHT, data,extraData);
            break;
        case Definition.STATUS_NAVI_TURN_LEFT:
            // updateStatus(ComponentStatus.STATUS_NAVIGATION_TURN_LEFT, data, extraData);
            break;
        case Definition.STATUS_NAVI_TURN_RIGHT:
            // updateStatus(ComponentStatus.STATUS_NAVIGATION_TURN_RIGHT, data, extraData);
            break;
        case Definition.STATUS_NAVI_SET_PRIORITY_FAILED:
            // updateStatus(ComponentStatus.STATUS_NAVIGATION_SET_PRIORITY_FAILED, data, extraData);
            break;
        default:
            // updateStatus(status, data, extraData);
            break;
    }
}
```
**参数说明：**

- `definition.DEBUG_REQ_ID`：int类型，命令id，定义参数reqid
- `dest`：string类型，本次导航目的地，目的地不为空，且是目标楼层地图上所在的点
- `floor`：int类型，目的地所在楼层映射，目的地楼层的楼层映射（FloorIndex），不能为0，且需在MapTool多层管理的地图组中
- `listener`：ActionListener类型，功能监听，需要的回调：onResult、onError、onStatusUpdate

> **注意：** 调用该接口前需要确保已定位

### 停止导航到指定位置

**方法名称：** `stopAdvanceNavigation`

**调用方式：**

```java
RobotApi.getInstance().stopAdvanceNavigation(reqId);
```

> **注意：** 该接口只能用于停止startElevatorNavigation启动的导航

---

### 状态和错误码定义

#### onStatusUpdate
导航过程中上报的事件，上报时任务不会停止。

| 名称 | Code | 定义 |
|------|------|------|
| `STATUS_START_NAVIGATION` | 1014 | 导航开始 |
| `STATUS_NAVI_REPLACE_DESTINATION` | 1048 | 切换目的地并开始导航 |
| `STATUS_ESTIMATE_LOST` | 1045 | 定位丢失 |
| `STATUS_DISTANCE_WITH_DESTINATION` | 1050 | 距目的地距离,单位（m）例：extraData = "3" |
| `STATUS_NAVI_AVOID_IMMEDIATELY` | 1051 | 避障 |
| `STATUS_GOAL_OCCLUDED` | 1016 | 避障开始 |
| `STATUS_NAVI_AVOID` | 1018 | 避障 |
| `STATUS_GOAL_OCCLUDED_END` | 1017 | 避障结束 |
| `STATUS_NAVI_AVOID_END` | 1019 | 避障结束 |
| `STATUS_NAVI_OBSTACLES_AVOID` | 1023 | 障碍物避障 |
| `STATUS_NAVI_MULTI_ROBOT_WAITING` | 1034 | 开始等待其他机器人通过（多机避障开始） |
| `STATUS_NAVI_MULTI_ROBOT_WAITING_END` | 1035 | 多机避障结束 |
| `STATUS_NAVI_GO_STRAIGHT` | 1036 | 机器人在直线上行走 |
| `STATUS_NAVI_TURN_LEFT` | 1037 | 机器人左转 |
| `STATUS_NAVI_TURN_RIGHT` | 1038 | 机器人右转 |

#### onResult

导航功能结束时，上报的状态

| 名称 | Code | 定义 |
|------|------|------|
| `RESULT_NAVIGATION_ARRIVED` | 102 | 到达目的地 |
| `RESULT_OK` | 1 | 成功 |
| `RESULT_FAILURE` | 2 | 导航失败 |
| `RESULT_DESTINATION_AVAILABLE` | 103 | 目的地可达，未被占用 |
| `RESULT_DESTINATION_IN_RANGE` | 104 | 到达目的地范围内 |
| `ACTION_RESPONSE_STOP_SUCCESS` | 3 | action停止成功 |
| `STATUS_NAVI_OUT_MAP` | 1020 | 超出地图范围 |
| `STATUS_NAVI_GLOBAL_PATH_FAILED` | 1025 | 导航路径规划失败 |
| `STATUS_NAVI_MULTI_MAP_NOT_MATCH` | 1040 | 多机地图未匹配 |
| `STATUS_NAVI_MULTI_LORA_DISCONNECT` | 1041 | 多机lora断开连接 |
| `STATUS_NAVI_MULTI_LORA_CONFIG_FAIL` | 1042 | 多机配置有问题 |
| `STATUS_NAVI_MULTI_VERSION_NOT_MATCH` | 1043 | 视觉未匹配 |
| `STATUS_NAVI_WHEEL_SLIP` | 1044 | 导航时轮子打滑 |

#### onError

导航出现异常结束时，上报的状态

| 名称 | Code | 定义 |
|------|------|------|
| `ERROR_PARAMETER` | -102 | 参数错误 |
| `ERROR_TARGET_NOT_FOUND` | -107 | 未找到点位 |
| `ERROR_DESTINATION_NOT_EXIST` | -108 | 目的地不存在 |
| `ERROR_ESTIMATE_ERROR` | -128 | 重定位失败 |
| `ERROR_MULTIPLE_MODE_ERROR` | -127 | 多机信息错误 |
| `ERROR_PARAMS_JSON_PARSER_ERROR` | -32750026 | json解析错误 |
| `ERROR_NO_AVAILABLE_DESTINATION` | -129 | 未找到可用的目的地 |
| `ERROR_NOT_ESTIMATE` | -116 | 未定位 |
| `ERROR_IN_DESTINATION` | -113 | 已经在目的地 |
| `ERROR_DESTINATION_CAN_NOT_ARRAIVE` | -109 | 目的地不可达 |
| `ERROR_MULTI_ROBOT_WAITING_TIMEOUT` | -125 | 多机避障等待超时 |
| `ERROR_NAVIGATION_AVOID_TIMEOUT` | -136 | 机器人避障超时 |
| `ACTION_RESPONSE_ALREADY_RUN` | -1 | Request busy |
| `ACTION_RESPONSE_REQUEST_RES_ERROR` | -6 | 请求失败 |
| `ACTION_RESPONSE_RES_UNAVAILBALE` | -9 | 请求不可用 |
| `ERROR_WHEEL_OVER_CURRENT_RUN_OUT` | -124 | 轮子堵转重试次数用完 |

### 梯控示例地址

> **注意：** 具体的梯控示例代码和使用说明，请参考相关的示例项目。

## 过闸机导航

### 介绍

以下是关于机器人过闸机的 API 介绍，使用 API 需要有以下准备工作：

**准备工作：**

- 机器硬件支持过闸机
- 设置闸机线
- 建图时要设置"闸机入口"和"闸机出口"两个点位，点位名字强匹配
- 设置巡线，要保证闸机入口和闸机出口，有唯一一条巡线可通过，该巡线需要是双向线
- 完成上述步骤后，选择编辑地图，闸机线选项，可选择一条巡线作为闸机线，选择后保存并且上传云端

> **注意：** 闸机线目前是整机唯一，没有与地图的关联关系，切换地图后要重新设置

### 判断从当前位置到目标点位是否通过闸机

**方法名称：** `getGatePassingRoute`

**调用方式：**

```java
RobotApi.getInstance().getGatePassingRoute(reqId, targetPlaceName, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        // 处理返回结果
        // 如果返回的List<Pose>为空，则不需要经过闸机
        // 如果非空，则两个点即为地图里建立的"闸机入口"和"闸机出口"的点
    }
});
```

**参数说明：**

- `reqId`：命令id
- `targetPlaceName`：机器人要导航去的目标点名称
- `listener`：回调，可以返回一个`List<Pose>`，如果为空，则不需要经过闸机，如果非空，则两个点即为地图里建立的"闸机入口"和"闸机出口"的点

> **注意：** 详细使用参考 Demo

### 导航通过闸机

通过 `getGatePassingRoute` 判断需要通过闸机，则导航到离机器人最近的一个点（上一步查询得到的点），然后再控制闸机开门（目前客户自己实现），开门成功后，导航到目的地。

**实现流程：**

1. 调用 `getGatePassingRoute` 判断是否需要经过闸机
2. 如果需要经过闸机，导航到离机器人最近的点位（闸机入口或出口）
3. 控制闸机开门（需要客户自己实现）
4. 开门成功后，导航到最终目的地

## 基础场景

### 简介

基础场景是RobotOS提供的功能组件，指包含一系列功能策略的场景功能，使用场景Api可以快速实现譬如引领，注册等基础能力，运行过程中的功能状态通过回调函数告知调用侧，开发者可以根据业务需求参与决策。

## 引领

### 场景介绍

引领是方便用户二次开发而实现的一个简单的业务场景，内部集成了人脸识别和导航，可在导航中持续识别引领目标，并可实时上报引领目标的状态，用户二次开发时可根据上报的状态进行处理，更快的实现自身业务需求。

> **注意：** 此API目前仅支持带有后置摄像头的豹小秘，其它机器人请使用navigation实现类似功能

### 开始引领

**方法名称：** `startLead`

**调用方式：**

```java
LeadingParams params = new LeadingParams();
//引领目标人id
params.setPersonId(personId);
//引领目的地
params.setDestinationName(dest);
//引领目标找不到多久后上报丢失状态
params.setLostTimer(lostTime);
//引领开始多久后开启人脸识别
params.setDetectDelay(delayTime);
//引领目标距离机器人多远后上报超距状态
params.setMaxDistance(maxDistance);
RobotApi.getInstance().startLead(reqId, params, new ActionListener() {
    @Override
    public void onResult(int status, String responseString) throws RemoteException {
        switch (status) {
            case Definition.RESULT_OK:
                //成功将目标引领到目的地
                break;
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                //在引领执行中，主动调用stopLead，成功停止引领
                break;
            default:
                break;
        }
    }
    @Override
    public void onError(int errorCode, String errorString) throws RemoteException {
        switch (errorCode) {
            case Definition.ERROR_NOT_ESTIMATE:
                //当前未定位
                break;
            case Definition.ERROR_SET_TRACK_FAILED:
            case Definition.ERROR_TARGET_NOT_FOUND:
                //引领目标未找到
                break;
            case Definition.ERROR_IN_DESTINATION:
                //当前已经在引领目的地
                break;
            case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE:
                //避障超时，默认为机器人20s的前进距离不足0.1m，
                break;
            case Definition.ERROR_DESTINATION_NOT_EXIST:
                //引领目的地不存在
                break;
            case Definition.ERROR_HEAD:
                //引领中操作头部云台失败
                break;
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                //引领已经在进行中，请先停止上次引领，才能重新执行
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                //已经有需要控制底盘的接口调用，请先停止，才能继续调用
                break;
            default:
                break;
        }
    }
    @Override
    public void onStatusUpdate(int status, String data) throws RemoteException {
        switch (status) {
            case Definition.STATUS_NAVI_OUT_MAP:
                //目标点不能到达，引领目的地在地图外，有可能为地图与位置点不匹配，请重新设置位置点
                break;
            case Definition.STATUS_NAVI_AVOID:
                //当前引领路线已被障碍物堵死
                break;
            case Definition.STATUS_NAVI_AVOID_END:
                //障碍物已移除
                break;
            case Definition.STATUS_GUEST_FARAWAY:
                //引领目标距离机器人太远，判断标准通过参数maxDistance设置
                break;
            case Definition.STATUS_DEST_NEAR:
                //引领目标进入机器人maxDistance范围内
                break;
            case Definition.STATUS_LEAD_NORMAL:
                //正式开始导航
                break;
            default:
                break;
        }
    }
});
```

### 结束引领

**方法名称：** `stopLead`

**调用方式：**

```java
RobotApi.getInstance().stopLead(reqId, isResetHW);
```

**参数说明：**

- `isResetHW`：引领时会切换摄像头到后置摄像头，`isResetHW`是用于设置停止引领时是否恢复摄像头状态
  - `true`：恢复摄像头为前置
  - `false`：保持停止时的状态
## 焦点跟随

### 场景介绍

焦点跟随会根据用户指定的人脸id，持续识别并跟踪，头部云台会持续跟着目标转动，底盘也会跟着联动，直到目标静止，机器人正对目标。

### 开启焦点跟随

**方法名称：** `startFocusFollow`

> **注意1：** 此方法使用时会占用底盘资源，不可同时执行任何底盘操作，包括导航。
> 
> **注意2：** 不要反复调用此方法来跟踪同一个人，只需要在跟随丢失或主动停止跟随后，再次启动`startFocusFollow`。

**调用方式：**

```java

RobotApi.getInstance().startFocusFollow(reqId, faceId, lostTimeout, maxDistance, new ActionListener() {
    @Override
    public void onStatusUpdate(int status, String data) {
        switch (status) {
            case Definition.STATUS_TRACK_TARGET_SUCCEED:
                //跟随目标成功
                break;
            case Definition.STATUS_GUEST_LOST:
                //跟随目标丢失
                break;
            case Definition.STATUS_GUEST_FARAWAY:
                //跟随目标距离已大于设置的最大距离
                break;
            case Definition.STATUS_GUEST_APPEAR:
                //跟随目标重新进入设置的最大距离内
                break;
        }
    }
    @Override public void onError(int errorCode, String errorString) {
        switch (errorCode) {
            case Definition.ERROR_SET_TRACK_FAILED:
            case Definition.ERROR_TARGET_NOT_FOUND:
                //跟随目标未找到
                break;
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                //正在跟随中，请先停止上次跟随，才能重新执行
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                //已经有需要控制底盘的接口调用(例如：引领、导航)，请先停止，才能继续调用
                break;
        }
    }
    @Override public void onResult(int status, String responseString) {
        Log.d(TAG, "startTrackPerson onResult status: " + status);
        switch (status) {
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                //在焦点跟随过程中，主动调用stopFocusFollow，成功停止跟随
                break;
        }
    }
});
```

**参数说明：**

- `faceId`：焦点跟随的目标人脸id
- `lostTimeout`：多久识别不到目标上报目标丢失状态，一般填5-10秒
- `maxDistance`：目标距离多远上报超距状态，一般填5米

### 停止焦点跟随

**方法名称：** `stopFocusFollow`

**调用方式：**

```java
RobotApi.getInstance().stopFocusFollow(reqId);
```
## 唤醒

### 场景介绍

唤醒场景指机器人根据唤醒词呼唤的声源方位，控制机器人转动到用户角度。

**不同机型策略：**

- **小秘策略**：当角度小于45度时，只转动头部云台，大于45度时，头部云台及底盘均会转动，以最快的速度转动到声源定位的目标角度
- **mini策略**：根据声源方位，转动底盘到声源方位

### 声源定位

每次唤醒机器人时，可以获取到唤醒时唤醒人和机器人之间的夹角，这个夹角叫声源定位角度。在`ModuleCallback`的`onSendRequest`回调获得，`reqType`为`Definition.REQ_SPEECH_WAKEUP`时，`reqParams`为声源定位的角度。它是唤醒API的传入参数。

### 开始唤醒

**方法名称：** `wakeUp`

**调用方式：**

```java
RobotApi.getInstance().wakeUp(reqId, angle, new ActionListener() {
    @Override
    public void onResult(int status, String responseString) throws RemoteException {
        switch (status) {
            case Definition.RESULT_OK:
                //唤醒完成
                break;
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                //在唤醒过程中，主动调用stopWakeUp，停止唤醒
                break;
        }
    }
    @Override public void onError(int errorCode, String errorString) throws RemoteException {
        switch (errorCode) {
            case Definition.ERROR_MOVE_HEAD_FAILED:
                //头部云台移动失败
                break;
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                //当前正在唤醒中，需要先停止上次唤醒
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                //已经有需要控制底盘的接口调用(例如：引领、导航等)，请先停止，才能继续调用
            break;
        }
    }
});
```

**参数说明：**

- `angle`：声源定位角度

### 停止唤醒

**方法名称：** `stopWakeUp`

**调用方式：**

```java
RobotApi.getInstance().stopWakeUp(reqId);
```

## 电量控制

### 获取当前电量

**调用方式：**

```java
RobotSettingApi.getInstance().getRobotString(Definition.ROBOT_SETTINGS_BATTERY_INFO);
```

### 开始自动回充

**方法名称：** `startNaviToAutoChargeAction`

**调用方式：**

```java
RobotApi.getInstance().startNaviToAutoChargeAction(reqId, timeout, new ActionListener() {
    @Override
    public void onResult(int status, String responseString) throws RemoteException {
        switch (status) {
            case Definition.RESULT_OK:
                //充电成功
                break;
            case Definition.RESULT_FAILURE:
                //充电失败
                break;
        }
    }
    @Override
    public void onStatusUpdate(int status, String data) throws RemoteException {
        switch (status) {
            case Definition.STATUS_NAVI_GLOBAL_PATH_FAILED:
                //全局路径规划失败
                break;
            case Definition.STATUS_NAVI_OUT_MAP:
                //目标点不能到达，引领目的地在地图外，有可能为地图与位置点不匹配，请重新设置位置点
                break;
            case Definition.STATUS_NAVI_AVOID:
                //前往回充点路线已被障碍物堵死
                break;
            case Definition.STATUS_NAVI_AVOID_END:
                //障碍物已移除
                break;
            default:
                break;
        }
    }
});
```

**参数说明：**

- `timeout`：导航超时时间，如果超过该时间未到达充电桩位置，则认为回充失败

### 结束自动回充

**方法名称：** `stopAutoChargeAction`

**调用方式：**

```java
RobotApi.getInstance().stopAutoChargeAction(reqId, true);
```
### 停止充电并脱离充电桩

**方法名称：** `leaveChargingPile`

**调用方式：**

```java
RobotApi.getInstance().leaveChargingPile(reqId, speed, distance, new CommandListener() {
            @Override
            public void onStatusUpdate(int status, String data, String extraData) {
                Log.d(TAG, "leaveChargingPile onStatusUpdate: " + status + " data:" + data + " extraData:" + extraData);
            }
            @Override
            public void onError(int errorCode, String errorString, String extraData) {
                Log.d(TAG, "leaveChargingPile errorCode: " + errorCode + " errorString:" + errorString + " extraData:" + extraData);
                switch (errorCode) {
                    case Definition.RESULT_FAILURE_MOTION_AVOID_STOP://前方有障碍，离桩失败
                        break;
                    case Definition.RESULT_FAILURE_TIMEOUT://离桩超时(15s)
                        break;
                    case Definition.STATUS_LEAVE_PILE_OPEN_RADAR_FAILURE://雷达启动失败
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onResult(int result, String message, String extraData) {
                Log.d(TAG, "leaveChargingPile onResult: " + result + " message:" + message);
                switch (result) {
                    case Definition.RESULT_OK://离桩成功
                        //如果此处想判断充电状态 注意充电状态更新会有一定时间
                        break;
                }
            }
        });
```

**参数说明：**

- `speed`：离桩前进的速度，默认 0.7
- `distance`：离桩前进的距离，默认 0.2，单位米

> **备注1：** 线充方式无法使用该方法脱离充电。
> 
> **备注2：** 此方法需先调用 `RobotApi.getInstance().disableBattery();` 禁用系统充电后才能使用。


## 多机协作与调度

### 多机协作

多机协作包含多机调度和多机避让功能，涉及到机器人多机协作的基础能力，此处主要涉及到的通信模块也属于机器人的多机协作基础能力部分，所属系统功能。

机器人多机协作，不需要我们人为做任何操作，所有行为均是机器人系统调度。但前提是必须保证机器人在同一地图下工作，多个机器人在碰面之后，会调用我们的机器人通信，会给他们此时的工作优先级进行排序，从而来确定是避让还是继续工作，从而避免多个机器人在工作时相遇在同一地点，呆滞不动的情况。因此，只需保证机器人性能与硬件一切正常，多机协作能力即可正常工作。

### 多机协作信息

在安装了ESP32多机通讯模块的机器人上，可使用以下方法获取多机信息，可用于制作多机调度系统。

> **注意：** 目前招财豹一定有ESP32多机通讯模块，其它机器人默认不包含这个模块。

**调用方式：**

```java
RobotApi.getInstance().registerStatusListener(Definition.STATUS_MULTIPLE_ROBOT_WORKING, mStatusListener);

private StatusListener mStatusListener = new StatusListener() {
    @Override
    public void onStatusUpdate(String type, String data) {
        try {
            Type dataType = new TypeToken<List<MultiRobotStatus>>(){}.getType();
            List<MultiRobotStatus> curRobotStatus = mGson.fromJson(data, dataType);
        } catch(Exception ex) {
            // 处理异常
        }
    }
};
```

**MultiRobotStatus类定义：**

```java
public class MultiRobotStatus {
    private BasePoseBean pose;
    private BasePoseBean goal;
    private int id;
    private int priority;
    private boolean mapMatch;
    private long time;
    private int status;
    private boolean curRobot = false;
    private int errorStatus = 0;
}
```

## 系统功能

### 系统状态监控

机器人有非常多的状态、事件可以监控，来辅助完成工作。监控主要使用下面函数来完成。

**调用方式：**

```java
RobotApi.getInstance().registerStatusListener(
        Definition.STATUS_POSE_ESTIMATE, mStatusPoseListener);
```

能监控到的事件定义可以在SDK的`Definition.Java`中找到。目前提供的监控状态如下：

```java
STATUS_POSE = "navi_pose"; //位置改变
STATUS_MAP = "navi_map"; //地图改变
STATUS_EMERGENCY = "status_emergency"; //紧急状态
STATUS_POSE_ESTIMATE = "status_pose_estimate"; //机器人定位状态
STATUS_AVOID_STOPPING = "status_avoid_stopping";//动态避停
STATUS_SWITCH_MAP = "status_switch_map"; //地图切换
STATUS_RADAR = "status_radar"; //雷达状态
STATUS_BATTERY = "status_battery"; //电池充电状态
STATUS_MULTIPLE_ROBOT_WORKING = "status_multiple_robot_working"; //多机状态信息
STATUS_MAP_OUTSIDE = "status_map_outside_report"; //机器出地图(目前仅限招财)
STATUS_ROBOT_BEING_PUSHED = "status_robot_being_pushed"; //机器被推动(目前仅限招财)
```
### 设置灯效

**方法名称：** `setLight`

**调用方式：**

```java
JSONObject params = new JSONObject();
try {
    params.put(Definition.JSON_LAMB_TYPE, type);
    params.put(Definition.JSON_LAMB_TARGET, 0);
    params.put(Definition.JSON_LAMB_RGB_START, startColor);
    params.put(Definition.JSON_LAMB_RGB_END, endColor);
    params.put(Definition.JSON_LAMB_START_TIME, startTime);
    params.put(Definition.JSON_LAMB_END_TIME, endTime);
    params.put(Definition.JSON_LAMB_REPEAT, loopTime);
    params.put(Definition.JSON_LAMB_ON_TIME, duration);
    params.put(Definition.JSON_LAMB_RGB_FREEZE, finalColor);
} catch (JSONException e) {
    e.printStackTrace();
}
RobotApi.getInstance().setLight(0, params.toString(), null);
```
**参数说明：**

- `reqId`：int类型，命令id
- `params`：String类型，可包含如下参数：
  - `type`：填0
  - `startColor`：灯效起始颜色值
  - `endColor`：灯效结束颜色值
  - `startTime`：渐变开始颜色停留的时间
  - `endTime`：渐变结束颜色停留的时间
  - `loopTime`：灯效渐变重复次数
  - `duration`：渐变过程花费的时间
  - `finalColor`：渐变过渡颜色
- `listener`：ActionListener类型，消息回调

> **注意：** 颜色值使用RGB排列的16位字符串，例如"F3F3F3"或者"00FF00"。

**返回值：**

- `int result`：0 命令执行 / -1 没有执行

> **示例代码：** 设置灯效代码demo可以在这里找到（代码适用于豹小秘机器人，其他机器人请替换机器人jar包）：点击这里

### 招财豹Pro和豹小递Pro设置灯效

#### 检查是否使用ProZcbLed灯带接口

**方法名称：** `isUseProZcbLed`

**调用方式：**

```java
/**
 * 是否使用ProZcbLed灯带接口
 */
RobotApi.getInstance().isUseProZcbLed();
```

#### 设置底盘灯带效果

**方法名称：** `setProZcbLedEffect`

**调用方式：**

```java
/**
 * 控制ZcbLed灯，即Saiph Pro的底盘灯带
 * proZcbEffect 参数参考下面颜色定义
 * Zcb:招财豹
 */
RobotApi.getInstance().setProZcbLedEffect(int reqId, int proZcbEffect, CommandListener listener);
```

#### 检查是否有锁骨灯

**方法名称：** `isHasClavicleLight`

**调用方式：**

```java
/**
 * 是否有锁骨灯
 */
RobotApi.getInstance().isHasClavicleLight();
```

#### 设置锁骨灯效果

**方法名称：** `setProIobLedEffect`

**调用方式：**

```java
/**
 * 控制IobLed灯，即Saiph Pro的锁骨灯
 * proIobLedEffect 参数参考下面颜色定义
 * "Iob"应该是拼写错误，应该是Lobby，表示胸口部位的灯带。
 */
RobotApi.getInstance().setProIobLedEffect(int reqId, int proIobLedEffect, CommandListener listener);
```
#### 颜色定义

```java
public static final int ZCB2UARTLED_GREENBREATH = 0xDE10;  //绿色呼吸效果
public static final int ZCB2UARTLED_BLUEBREATH = 0xDE11;  //蓝色呼吸效果
public static final int ZCB2UARTLED_ORANGEBREATH = 0xDE12;  //橙色呼吸效果
public static final int ZCB2UARTLED_YELLOWBREATH = 0xDE13;  //黄色呼吸效果
public static final int ZCB2UARTLED_BLUENORMAL = 0xDE14;  //蓝色正常效果
public static final int ZCB2UARTLED_REDNORMAL = 0xDE15;  //红色正常效果
public static final int ZCB2UARTLED_ORANGENORMAL = 0xDE16;  //橙色正常效果
public static final int ZCB2UARTLED_YELLOWNORMAL = 0xDE17;  //黄色正常效果
public static final int ZCB2UARTLED_GREENNORMAL = 0xDE18;  //绿色正常效果
public static final int ZCB2UARTLED_TURNRIGHT = 0xDE19;  //右转效果
public static final int ZCB2UARTLED_TURNLEFT = 0xDE20;  //左转效果
public static final int ZCB2UARTLED_REGFLASH = 0xDE21;  //红色闪效果
public static final int ZCB2UARTLED_YELLOWFLASH = 0xDE22;  //黄色闪效果
public static final int ZCB2UARTLED_ALLOFF = 0xDE00;  //关闭所有zcb效果
```
获取本机SN

**方法名称：** `getRobotSn`

**调用方式：**

```java
boolean status = RobotApi.getInstance().getRobotSn(
    new CommandListener(){
        @Override
        public void onResult(int result, String message) {
            if (Definition.RESULT_OK == result) {
                String serialNum = message;
            } else {
                // 处理错误
            }
        }
    });
```

**参数说明：**

- `listener`：CommandListener 消息回调{"code":0, "message":"err msg"}

**返回值：**

- `int result`：1 命令执行 / -1 没有执行

---

### 获取系统版本

**方法名称：** `getVersion`

**调用方式：**

```java
String version = RobotApi.getInstance().getVersion();
```

---

### 禁止系统功能

系统不再接管急停事件，没有急停画面，可用于用户自定义急停画面。在急停状态下，所有底盘相关功能API不可使用，唤醒、休眠等切换机器人状态的API也不生效。

**方法名称：** `disableEmergency`

**调用方式：**

```java
RobotApi.getInstance().disableEmergency();
```

**获取Emergency状态的API：**

```java
RobotApi.getInstance().getRobotStatus(Definition.STATUS_EMERGENCY, new StatusListener(){
    @Override
    public void onStatusUpdate(String type, String data) throws RemoteException {
        Log.d("onStatusUpdate","Status:"+data);
    }
});

// 或者使用下面方法注册急停状态事件
RobotApi.getInstance().registerStatusListener(Definition.STATUS_EMERGENCY, new StatusListener(){
    @Override
    public void onStatusUpdate(String type, String data) throws RemoteException {
        Log.d("onStatusUpdate","status:"+type+";value:"+data);
    }
});
```

---

### 禁用电池界面

禁用当前电池界面，充电时可使用除底盘外client app任何能力。

如果确认不需要充电接管，推荐在APP启动连上RobotAPI成功之后，直接调用此接口禁用。此后直到APP退出，充电接管画面都会处于禁用状态。

**方法名称：** `disableBattery`

**调用方式：**

```java
RobotApi.getInstance().disableBattery();
```

**获取Battery状态的API：**

```java
RobotSettingApi.getInstance().getRobotString(Definition.ROBOT_SETTINGS_BATTERY_INFO);

// 或使用下面方法监听电池状态变化
RobotApi.getInstance().registerStatusListener(Definition.STATUS_BATTERY, listener);
```

---

### 禁用功能键

禁用功能键，招财豹头部后面按钮。

**方法名称：** `disableFunctionKey`

**调用方式：**

```java
RobotApi.getInstance().disableFunctionKey();
```

---

### 休眠功能

#### 场景介绍

休眠是让机器人在没有任务或者低电量的时候，保持低功耗运行的一种模式。

> **注意：** 使用休眠API需要添加如下权限：
> ```xml
> <uses-permission android:name="com.ainirobot.coreservice.robotSettingProvider" />
> ```

#### 开始休眠

**方法名称：** `robotStandby`

**调用方式：**

```java
RobotApi.getInstance().robotStandby(0, new CommandListener() {
    @Override
    public void onStatusUpdate(int status, String data, String extraData) {
        super.onStatusUpdate(status, data, extraData);
    }
});
```

#### 停止休眠

**方法名称：** `robotStandbyEnd`

**调用方式：**

```java
RobotApi.getInstance().robotStandbyEnd(reqId);
```

---

### 安装APK

**方法名称：** `installApk`

**调用方式：**

```java
RobotApi.getInstance().installApk(reqid, fullPathName, taskID);
```

**参数说明：**

- `reqId`：int类型，命令id
- `fullPathName`：安装包路径
- `taskID`：taskID，非空的任意字符串内容
