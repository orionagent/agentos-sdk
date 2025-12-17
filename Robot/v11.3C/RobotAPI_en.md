# Robot Native Interface Usage Reference

## RobotOS Introduction

RobotOS is an open development platform by Orion Star, developed on the Android platform for multi-morphology hardware robots and diverse business scenario combinations.

RobotOS provides rich robot business APIs and robot capability components, enabling developers to conveniently develop robot applications.

Based on the Android platform, RobotOS provides an SDK for Android APK development. Users can apply RobotService.jar to develop Android applications on robots. All robot models running RobotOS support this SDK.

## 🚨 Important: Speech Functionality Migration Notice

**On the AgentOS system, all speech and NLP-related functionalities have been migrated to AgentOS SDK. The original speech and NLP functionalities in RobotAPI have been deprecated:**

### Deprecated RobotAPI Speech and NLP Functionalities
- **ASR (Automatic Speech Recognition)**: Original RobotAPI speech-to-text functionality
- **TTS (Text-to-Speech)**: Original RobotAPI text-to-speech functionality
- **NLP (Natural Language Processing)**: Original RobotAPI natural language understanding and processing functionality

### New Speech and Large Language Model Solutions
**All speech and NLP functionalities are now provided through AgentOS SDK:**
- ✅ **Wake-free interaction**: AgentOS SDK provides more intelligent wake-free speech interaction
- ✅ **ASR/TTS**: Implemented through AgentOS SDK interfaces such as `AgentCore.tts()`
- ✅ **Intelligent conversation**: Integration of large language models for natural language understanding and generation, replacing original NLP functionality
- ✅ **Action planning**: Automatic action planning and execution based on user speech intent
- ✅ **Large language model capabilities**: Powerful natural language understanding through large language models, replacing traditional NLP

### Migration Guide
If your project uses RobotAPI speech or NLP functionalities, please:
1. **Remove RobotAPI-related code**: Delete original ASR, TTS, NLP, and wake-up RobotAPI calls
2. **Integrate AgentOS SDK**: Refer to [AgentOS SDK Documentation](../../Agent/v0.3.5/AgentOS_SDK_Doc_v0.3.5.md) for integration
3. **Functionality Mapping Migration**:
   - RobotAPI wake-up → AgentOS SDK wake-free interaction
   - RobotAPI ASR → AgentOS SDK speech recognition listening
   - RobotAPI TTS → AgentOS SDK `AgentCore.tts()` interface
   - RobotAPI NLP → AgentOS SDK large language model interface and action planning

### Collaborative Use
**Collaborative Relationship between RobotAPI and AgentOS SDK:**
- **AgentOS SDK**: Responsible for all AI intelligent interactions (speech interaction, intelligent conversation, large language model capabilities, action planning execution)
- **RobotAPI**: Responsible for robot hardware control (motion, navigation, sensors, vision, etc)

**RobotAPI Legacy Speech Functionality Reference Documentation:**
The following documentation helps developers understand the original RobotAPI speech functionality, facilitating the identification and removal of legacy code in projects:
- [Speech](https://orionbase.orionstar.com/doc?m=21&h=6065a29&lang=cn#text%E8%BD%ACmp3)

## Version

**V11.3 Version API Latest**


## SDK Integration

### 1. Import SDK Dependencies

#### Add JAR Package Dependencies
1. Obtain SDK file: Find the `robotservice_xx.jar` file in the Robot version directory (such as `Robot/v11.3C/robotservice_11.3.jar`). SDK file follows the `robotservice_<version>.jar` naming convention. Please select the corresponding directory according to the actual version
2. Copy the JAR package file to the `app/libs/` directory of the Android project
3. Add dependency configuration in the `app/build.gradle` file:

```gradle
dependencies {
    implementation files('libs/robotservice_11.3.jar')
    // Other dependencies...
}
```

### 2. Configure Manifest File

Add the following configuration in the `AndroidManifest.xml` file:

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

### 3. Startup Configuration (Optional)

If you need the application to automatically start after the robot boots up, please configure according to the following steps:

#### Step 1: Manifest Configuration
Add the following intent-filter in Activity:

```xml
<intent-filter>
    <action android:name="action.orionstar.default.app" />
    <category android:name="android.intent.category.DEFAULT" />
</intent-filter>
```

#### Step 2: System Settings
Configure in the robot system:
1. Swipe down with three fingers to enter system settings
2. Select "Developer Settings"
3. Configure your application in "Startup Programs"

> **Important Note**: Startup program setting function requires OTA3 or later version support

### 4. Permission Configuration

To ensure the SDK functions properly, declare the following permissions in `AndroidManifest.xml`:

```xml
<!-- Internet access permission -->
<uses-permission android:name="android.permission.INTERNET"/>
<!-- Robot settings provider permission -->
<uses-permission android:name="com.ainirobot.coreservice.robotSettingProvider" />
<!-- External storage read/write permission -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

## SDK Integration Steps

### 1. Create Callback Interface

Create a callback to receive speech requests and system events:

```java
public class ModuleCallback extends ModuleCallbackApi {
    @Override
    public boolean onSendRequest(int reqId, String reqType, String reqText, String reqParam)
            throws RemoteException {
                // This callback is deprecated
        return true;
    }

    @Override
    public void onRecovery() throws RemoteException {
        // Control right restored. After receiving this event, regain control of the robot
    }

    @Override
    public void onSuspend() throws RemoteException {
        // Control right removed by system. After receiving this event, all API calls are invalid
    }
}
```

### 2. Connect to Server

```java
RobotApi.getInstance().connectServer(this, new ApiListener() {
    @Override
    public void handleApiDisabled() {
        // API Disabled
    }

    @Override
    public void handleApiConnected() {
        // Server connected, set callbacks to receive requests, including speech instructions, system events, etc

    }

    @Override
    public void handleApiDisconnected() {
        // Connection Disconnected
    }
});
```

**Important Note:**
Initialization of the robot SDK marks the robot transferring chassis capability permissions to the currently running APP. The robot will trigger the disconnection callback when the system takes over, a fault occurs, or another robot SDK program is started. Only when system takeover ends will it reconnect.

> **Note 1**: All APIs must be called after successfully connecting to the server
>
> **Note 2**: Only foreground processes running in the foreground can correctly connect and obtain robot chassis control permissions. Background services do not work.



### 5. Register Status Listener

```java
StatusListener statusListener = new StatusListener(){
    @Override
    public void onStatusUpdate(String type, String data) throws RemoteException {
        // Handle status update
    }
};

// Register status listener
RobotApi.getInstance().registerStatusListener(type, statusListener);

// Unregister status listener
RobotApi.getInstance().unregisterStatusListener(statusListener);
```

**Supported Status Types:**

- `Definition.STATUS_POSE`: Robot current coordinates, continuously reported
- `Definition.STATUS_POSE_ESTIMATE`: Current localization status, reported when localization status changes
- `Definition.STATUS_BATTERY`: Current battery status information, including charging status, battery level, low battery alarm, etc

### 6. Set reqId

Many SDK methods require passing the `reqId` parameter, which is an ID for debugging and log tracking.Passing any number allows the function to work normally, but for convenience in log tracking and debugging, it is recommended to make `reqId` a self-incrementing static variable, or pass different `reqId` values based on business needs to distinguish function calls.

## Vision Capability

### Introduction

Vision capability currently mainly refers to person detection and recognition modules. APIs are mainly provided through PersonApi and RobotApi.

Person detection is a local capability. When a person stands in front of the robot (excluding poor lighting conditions), the robot can detect the person. When the person is far away, both face and body can be detected. When the person is close, only face information can be detected. When the person ID is greater than or equal to 0, it indicates that the person face information is complete and can be used to obtain face photos for recognition.

Person recognition requires using face photos for recognition. This capability requires network usage. Please ensure that persons meet the conditions for taking photos when acquiring person photos.

> **Note**: When using robot vision capability, do not use the robot camera simultaneously. This will cause vision capability errors and failures

See the Demo for specific usage flow. The following is the API introduction:

### Main Person Information

```java
private int id; // Person face local recognition ID, this ID can be used for focus following, etc
private double distance; // Distance
private double faceAngleX; // Person face X-axis angle
private double faceAngleY; // Person face Y-axis angle
private int headSpeed; // Current robot head rotation speed
private long latency; // Data latency
private int facewidth; // Face width
private int faceheight; // Face height
private int faceX; // Face X coordinate
private int faceY; // Face Y coordinate
private int bodyX; // Body X coordinate
private int bodyY; // Body Y coordinate
private int bodywidth; // Body width
private int bodyheight; // Body height
private double angleInView; // Person angle relative to robot head
private String quality; // Quality check parameter
private int age; // Age (estimated value returned after cloud registration)
private String gender; // Gender (according to national regulations, this item will only return results after authorization and registration)
private int glasses; // Wearing glasses
private String remoteFaceId; // If registered, the registered person face remote ID
private String faceRegisterTime; // Registration time
// For more Person information, see the Person class definition in com.ainirobot.coreservice.client.listener in the SDK
```
### Register Person Change Listener

**Method Name:** `registerPersonListener` / `unregisterPersonListener`

**Invocation Method:**

```java
PersonListener listener = new PersonListener() {
    @Override
    public void personChanged() {
        super.personChanged();
        // When persons change, you can call the get current person list interface to get all persons in the robot vision
    }
};

// Register person listener
PersonApi.getInstance().registerPersonListener(listener);

// Unregister person listener
PersonApi.getInstance().unregisterPersonListener(listener);
```

**Parameter Description:**

- `listener`: Person information change listener
### Get All Persons Information

**Method Name:** `getAllPersons`

**Invocation Method:**

```java
// Get all persons information in robot vision
List<Person> personList = PersonApi.getInstance().getAllPersons();
// Get all persons information within 1m range in robot vision
List personList = PersonApi.getInstance().getAllPersons(1);
```

**Parameter Description:**

- `maxDistance`: Within what vision range of the robot, unit in meters. The robot can recognize persons most accurately at a distance of 1-3 meters.

**Return Value:**

- `personList`: Person information list
### Get Person List with Body Detected

**Method Name:** `getAllBodyList`

**Invocation Method:**

```java
// Get person list with all body information in robot vision
List<Person> personList = PersonApi.getInstance().getAllBodyList();
// Get person list with all body information within 1m range in robot vision
List personList = PersonApi.getInstance().getAllBodyList(1);
```

**Parameter Description:**

- `maxDistance`: Within what vision range of the robot, unit in meters. The robot can recognize persons most accurately at a distance of 1-3 meters.

**Return Value:**

- `personList`: Person information list

### Get Person List with Face Detected

**Method Name:** `getAllFaceList`

**Invocation Method:**

```java
// Get person list with all face information in robot vision
List<Person> personList = PersonApi.getInstance().getAllFaceList();
// Get person list with all face information within 1m range in robot vision
List personList = PersonApi.getInstance().getAllFaceList(1);
```

**Parameter Description:**

- `maxDistance`: Within what vision range of the robot, unit in meters. The robot can recognize persons most accurately at a distance of 1-3 meters.

**Return Value:**

- `personList`: Person information list

### Get Person List with Complete Face Detected

**Method Name:** `getCompleteFaceList`

**Invocation Method:**

```java
// Get person list with all complete face information in robot vision
List<Person> personList = PersonApi.getInstance().getCompleteFaceList();
```

**Return Value:**

- `personList`: Person information list

### Get Person in Focus Following

This method is only effective when focus following is in progress

**Method Name:** `getFocusPerson`

**Invocation Method:**

```java
Person person = PersonApi.getInstance().getFocusPerson();
```

**Return Value:**

- `person`: Person information

For focus following related APIs, click here to view.

### Get Face Photo

**Method Name:** `getPictureById`

**Invocation Method:**

```java
RobotApi.getInstance().getPictureById(reqId, faceId, count, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String status = json.optString("status");
            // Photo retrieved successfully
            if (Definition.RESPONSE_OK.equals(status)) {
                JSONArray pictures = json.optJSONArray("pictures");
                if (!TextUtils.isEmpty(pictures.optString(0))) {
                    // Full local storage path of photo
                    String picturePath = pictures.optString(0);
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**Parameter Description:**

- `faceID`: Face ID, obtainable through local face recognition (Person ID)
- `count`: Number of photos to retrieve, this parameter is currently ineffective, default is one

> **Note**: Images saved by this interface need to be manually deleted after use

### Auto Registration

**Method Name:** `startRegister`

Pass in the name. This method will capture faces from the camera in real-time and register the face with the given name. Use this method for daily person registration

**Invocation Method:**

```java
RobotApi.getInstance().startRegister(reqId, personName, timeout, tryCount, secondDelay, new ActionListener() {
    @Override
    public void onResult(int status, String response) throws RemoteException {
        if (Definition.RESULT_OK != status) {
            // Registration failed
            return;
        }
        try {
            JSONObject json = new JSONObject(response);
            String remoteType = json.optString(Definition.REGISTER_REMOTE_TYPE);
            String remoteName = json.optString(Definition.REGISTER_REMOTE_NAME);
            if (Definition.REGISTER_REMOTE_SERVER_EXIST.equals(remoteType)) {
                // Current user already exists
            } else if (Definition.REGISTER_REMOTE_SERVER_NEW.equals(remoteType)) {
                // New user registration successful
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
});
```

**Parameter Description:**

- `personName`: Registration name
- `timeout`: Timeout duration, attempts to re-register on failure until timeout
- `tryCount`: Number of retry attempts on failure, will give up retrying after retry time exceeds timeout
- `secondDelay`: Retry interval

> **Note**: Do not repeatedly register the same face

### Remote Registration

**Method Name:** `remoteRegister`

**Invocation Method:**

```java
RobotApi.getInstance().remoteRegister(reqId, name, path, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            int code = jsonObject.optInt("code", -1);
            switch (code) {
                case 0:// Success
                    break;
                case Definition.REMOTE_CODE_FACE_INVALID:// Invalid image
                    break;
                default:// Others
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
});
```

**Parameter Description:**

- `name`: Registration name
- `path`: Image path, obtainable through getPictureById

> **Note**: Do not repeatedly register the same face

### Remote Recognition

**Method Name:** `getPersonInfoFromNet`

**Invocation Method:**

```java
RobotApi.getInstance().getPersonInfoFromNet(reqId, faceId, pictures, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            JSONObject info = json.getJSONObject("people");
            info.getString("name"); // Name
            info.getString("gender"); // Gender
            info.getString("age"); // Age
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**Parameter Description:**

- `faceID`: Face ID, obtainable through local face recognition (Person ID)
- `pictures`: Face photo, obtainable through getPictureById interface

### Switch Camera

**Method Name:** `switchCamera`

**Invocation Method:**

```java
RobotApi.getInstance().switchCamera(reqId, camera, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            if (Definition.RESPONSE_OK.equals(json.getString("status"))) {
                // Switch successful
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```


**Parameter Description:**

- `camera`: Camera, String type
  - `Definition.JSON_HEAD_FORWARD` Front camera
  - `Definition.JSON_HEAD_BACKWARD` Rear camera

## Camera

The robot cameras are either single or dual. Different product lines use different cameras. See the documentation below for details.


#### Leopard Secretary 2 and Leopard Secretary Mini

Dual cameras. The front camera can be opened using Android standard API or shared stream to get camera preview data. (Opening the front camera using Android standard API will temporarily disable robot vision algorithms. After release, vision algorithm capability will be restored after some time). Rear camera can only be opened using Android standard API.


#### Note

When using Android API to open camera, the APP may crash the first time you authorize it to use the camera, but there are no restrictions after that. If you encounter a crash when authorizing the camera, just restart it. After that, you can use the camera normally.

Landscape Robot Note: After starting through Android standard method, the orientation is 90°, because the robot has no gyroscope, the program defaults to portrait camera, but the robot is actually a landscape device. The solution is simple: set camera rotation to -90° or 270° through Android API (or other video SDK APIs) to correct it

The following Demos are for Android camera API usage, for reference only. If not satisfied, please search online according to your needs

Camera1 Demo

Camera2 Demo

### Camera Data Stream Sharing
Because the robot vision capability VisionSDK needs to use the camera, if the camera is occupied by secondary development programs, it will make robot face detection and face recognition unavailable. If you want to use the camera without affecting robot vision functionality, you need to use shared data stream to get camera data from VisionSDK. Camera data is obtained through SurfaceShareApi.It has three main methods:

startRequest() : Start shared data stream
onImageAvailable(ImageReader reader) :This is a callback where all image data from shared streams comes out
stopPushStream() :Close shared data stream
Obtaining image data via shared streams has large memory and CPU overhead. Be sure to close it when not in use to avoid OOM.Images obtained from shared data streams are in YUV format. To render to surfaceview, they need to be converted to bitmap.

See the code below for details:

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
 * Get Video Data through Vision SDK
 */
public class SurfaceShareDataEngine {
    private static final String TAG = "SurfaceShareDataEngine";
    public static final int IMAGE_DATA_FOR_LIVEME = 0;
    public static final int IMAGE_DATA_FOR_LOCAL_PREVIEW = 1;
    public static final int VISION_IMAGE_WIDTH = 640;
    public static final int VISION_IMAGE_HEIGHT = 480;
    private static final int MAX_CACHE_IMAGES = 4; // maxImages keep consistent with VisionSdk
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
 // The following is Leopard business logic, which can be changed to other business logic. Be sure to release images after processing, otherwise it may easily cause OOM crash
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

In Orion robot code, the logic code handles YUV images like this:

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

## Basic Motion

### Linear Motion

**Method Name:** `goForward` / `goBackward`

**Invocation Method:**

```java
CommandListener motionListener = new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            // Call successful
        } else {
            // Call failed
        }
    }
};
RobotApi.getInstance().goForward(reqId, speed, motionListener);
RobotApi.getInstance().goForward(reqId, speed, distance, motionListener);
RobotApi.getInstance().goForward(reqId, speed, distance, avoid, motionListener);
RobotApi.getInstance().goBackward(reqId, speed, motionListener);
RobotApi.getInstance().goBackward(reqId, speed, distance, motionListener);
```

**Parameter Description:**

- `speed`: Motion speed, unit: m/s, range 0 ~ 1.0. Speeds greater than 1.0 will be capped at 1.0
- `distance`: Motion distance, unit: m. Value must be greater than 0.
- `avoid`: Whether to perform obstacle avoidance while walking. Only forward motion supports obstacle avoidance.
### Rotational Motion

**Method Name:** `turnLeft` / `turnRight`

**Invocation Method:**

```java
CommandListener rotateListener = new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            // Call successful
        } else {
            // Call failed
        }
    }
};
RobotApi.getInstance().turnLeft(reqId, speed, rotateListener);
RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener);
RobotApi.getInstance().turnRight(reqId, speed, rotateListener);
RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener);
```

**Parameter Description:**

- `speed`: Rotation speed, unit: degree/s, range 0 ~ 50 degrees/s
- `angle`: Rotation angle, unit: degree. Value must be greater than 0
### Control Robot through Angular/Linear Velocity

**Method Name:** `motionArcWithObstacles`

**Invocation Method:**

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

**Parameter Description:**

- `lineSpeed`: Linear speed, range -1.2 ~ 1.2
- `angularSpeed`: Angular speed, range -2 ~ 2
### Stop

**Method Name:** `stopMove`

**Invocation Method:**

```java
RobotApi.getInstance().stopMove(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            // Call successful
        } else {
            // Call failed
        }
    }
});
```

> **Note:** This interface can only be used to stop forward, backward and rotation motions. It cannot be used to stop navigation or head motion.

### Head Gimbal Motion

**Method Name:** `moveHead`

**Invocation Method:**

```java
RobotApi.getInstance().moveHead(reqId, hMode, vMode, hAngle, vAngle, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String status = json.getString("status");
            if (Definition.CMD_STATUS_OK.equals(status)) {
                // Operation successful
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**Parameter Description:**

- `hMode`: Mode for horizontal rotation. Absolute motion: `absolute`. Relative motion: `relative`
- `vMode`: Mode for vertical motion. Absolute motion: `absolute`. Relative motion: `relative`
- `hAngle`: Horizontal rotation angle, range: -120 ~ 120 degrees
- `vAngle`: Vertical motion angle, range: 0 ~ 90 degrees

> **Note:** Please note the physical limitations of different machine types. For mini and Xiaobao Express 2 models, horizontal rotation mode has no effect.

### Reset Gimbal to Initial Angle

**Method Name:** `resetHead`

**Invocation Method:**

```java
RobotApi.getInstance().resetHead(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            String status = json.getString("status");
            if (Definition.CMD_STATUS_OK.equals(status)) {
                // Operation successful
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

## Map and Localization

### Introduction

Map and localization are prerequisites for robot navigation. Creating a new map tells the robot the walkable range, and localization tells the robot its current position. The robot comes with a "Map Tool" that can complete all map and location operations, or you can use the API yourself

**Set Location:** Inform the robot of the current location's name, which can then be used with navigation interface to navigate to this location

### Map Creation and Localization

Map creation and localization can be performed using the system-integrated map tools.

> In map coordinates, x and y represent the robot's position in the map, and theta represents the robot's facing direction (unit: radians)

### Localization (Set Robot Initial Position)

**Method Name:** `setPoseEstimate`

**Invocation Method:**

```java
try {
    JSONObject params = new JSONObject();
    // X coordinate
    params.put(Definition.JSON_NAVI_POSITION_X, x);
    // Y coordinate
    params.put(Definition.JSON_NAVI_POSITION_Y, y);
    // Z coordinate
    params.put(Definition.JSON_NAVI_POSITION_THETA, theta);
    RobotApi.getInstance().setPoseEstimate(reqId, params.toString(), new CommandListener() {
        @Override
        public void onResult(int result, String message) {
            if ("succeed".equals(message)) {
                // Localization successful
            }
        }
    });
} catch (JSONException e) {
    e.printStackTrace();
}
```
### Check if Currently Localized

**Method Name:** `isRobotEstimate`

**Invocation Method:**

```java
RobotApi.getInstance().isRobotEstimate(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if (!"true".equals(message)) {
            // Currently not localized
        } else {
            // Currently localized
        }
    }
});
```
### Set Current Location Name

**Method Name:** `setLocation`

**Invocation Method:**

```java
RobotApi.getInstance().setLocation(reqId, placeName, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            // Location point saved successfully
        } else {
            // Location point save failed
        }
    }
});
```

**Parameter Description:**

- `placeName`: Location name

> **Note:** Before calling this interface, ensure localization is complete

### Get Position Coordinates by Location Name

**Method Name:** `getLocation`

**Invocation Method:**

```java
RobotApi.getInstance().getLocation(reqId, placeName, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            // Whether current location exists
            boolean isExist = json.getBoolean(Definition.JSON_NAVI_SITE_EXIST);
            if (isExist) {
                // X coordinate
                double x = json.getDouble(Definition.JSON_NAVI_POSITION_X);
                // Y coordinate
                double y = json.getDouble(Definition.JSON_NAVI_POSITION_Y);
                // Facing direction
                double z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

**Parameter Description:**

- `placeName`: Location name

> **Note:** Locations saved by setLocation are associated with the map. When retrieving locations via getLocation, you should use the same map, otherwise getLocation will fail

### Get All Position Points on Current Map

**Method Name:** `getPlaceList`

**Invocation Method:**

```java
RobotApi.getInstance().getPlaceList(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONArray jsonArray = new JSONArray(message);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                // Commonly used
                json.getString("name"); // Location name
                json.getDouble("x"); // X coordinate
                json.getDouble("y"); // Y coordinate
                // Rarely used
                json.getDouble("theta"); // Facing direction
                json.getString("id"); // Location ID
                json.getLong("time"); // Update time
                json.getInt("status"); // 0: Normal area, can reach 1: Forbidden area, cannot reach 2: Outside map, cannot reach
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```
### Get Robot Current Position

**Method Name:** `getPosition`

**Invocation Method:**

```java
RobotApi.getInstance().getPosition(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        try {
            JSONObject json = new JSONObject(message);
            // X coordinate
            double x = json.getDouble(Definition.JSON_NAVI_POSITION_X);
            // Y coordinate
            double y = json.getDouble(Definition.JSON_NAVI_POSITION_Y);
            // Facing direction
            double z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }
});
```

> **Note:** Before calling this interface, ensure localization is complete

### Check if Robot is at Position Point

**Method Name:** `isRobotInLocations`

**Invocation Method:**

```java
try {
    JSONObject params = new JSONObject();
    params.put(Definition.JSON_NAVI_TARGET_PLACE_NAME, placeName); // Location name
    params.put(Definition.JSON_NAVI_COORDINATE_DEVIATION, range); // Location range
    RobotApi.getInstance().isRobotInlocations(reqId,
            params.toString(), new CommandListener() {
                @Override
                public void onResult(int result, String message) {
                    try {
                        JSONObject json = new JSONObject(message);
                        // Whether at target location
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

**Parameter Description:**

- `placeName`: Location name
- `range`: Location range, unit: m
### Get Current Map Name

**Method Name:** `getMapName`

**Invocation Method:**

```java
RobotApi.getInstance().getMapName(reqId, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        if (!TextUtils.isEmpty(message)) {
            // message is the map name
            String mapName = message;
        }
    }
});
```
### Listen for Position State Changes

Start a listener callback that triggers when location and location state change.

```java
public class Pose {
    public float px, py, theta;
    public final long time;
    public String name;
    /**
     * FREE = 0;      // Normal area, can reach
     * OBSTACLE = 1;  // Forbidden area, cannot reach
     * OUTSIDE = 2;   // Outside map, cannot reach
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
### Switch Map

**Method Name:** `switchMap`

**Invocation Method:**

```java
RobotApi.getInstance().switchMap(reqId, mapName, new CommandListener(){
    @Override
    public void onResult(int result, String message) {
        if ("succeed".equals(message)) {
            // Map switched successfully
        }
    }
});
```

**Parameter Description:**

- `mapName`: Map name

> **Note:** After switching maps, re-localization is required

### Read and Display Map PGM File (including point coordinate conversion)

Map reading and coordinate conversion are supported by a complete set of map tools. Example code is as follows:

```java
/*
 * getMap
 * Get current map
 * The map has current robot location information, direction coordinates and clickable navigation location points
 * Get the current map
 * The map has current robot position information, direction coordinates and clickable navigation positions
 * Get map.pgm file from shared memory
 * */
private void getMap(final String name) {
    Log.d(TAG, "getMapPgmPFD: mapName=" + name);
    // Get map.pgm file descriptor
    ParcelFileDescriptor mapPgmPFD = ShareMemoryApi.getInstance().getMapPgmPFD(name);
    FileDescriptor fd = mapPgmPFD.getFileDescriptor();
    FileInputStream fileInputStream = new FileInputStream(fd);
    // Read data stream from file descriptor, parse as RoverMap (this logic is consistent with before)
    mRoverMap = MapppUtils.loadPFD2RoverMap(fileInputStream);

    // TODO Custom operation

    // Release service layer resources
    ShareMemoryApi.getInstance().releaseGetMapPgmPFD();
}

/**
 * Modify map.pgm file in shared memory, test code
 */
private void setMapPgmPFD(String mapName, RoverMap roverMap) {
    byte[] bytes = MapUtils.saveRoverMapToPFDData(roverMap);
    boolean result = ShareMemoryApi.getInstance().setMapPgmPFD(mapName, bytesT);
    Log.d(TAG, "setMapPgmPFD: result=" + result);
}
```

If you have related requirements, please copy or refer to the NavFragment part of the sample code. Finally, you can read out the map and position information from the robot and display it as follows. Download code

### Enable/Disable Radar Data Reporting

**Method Name:** `setNavigationLineData`

**Invocation Method:**

```java
RobotApi.getInstance().setNavigationLineData(reqId, enabled);
```

**Parameter Description:**

- `enabled`: `true` to enable radar data reporting; `false` to disable radar data reporting

### Radar Data Listener

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

> **Note:** You need to call setNavigationLineData to enable radar data reporting before data becomes available. After use, disable radar data reporting and unregisterStatusListener to release resources

## Navigation

### Introduction

Navigation refers to the robot's walking ability. The robot can move from point A to point B, automatically planning routes during movement and effectively avoiding obstacles.

Robot navigation uses many sensors, including bottom LIDAR, RGBD, head IR sensors (on some models), etc. Please do not block these sensors during navigation to avoid robot immobility or path planning failure.

> **Important Note:** The prerequisite for robot to execute navigation is: the robot has created a new map, is successfully localized on this map, and the LIDAR is in the enabled state. Please pay special attention to this!

### Navigation Status and Error Code Definition

Status and error code reference table that may appear in navigation series APIs

#### Navigation Status Definition (corresponding to NavigationListener's onStatusUpdate method)

```java
STATUS_START_NAVIGATION = 1014; // Start navigation
STATUS_START_CRUISE = 1015; // Start patrol
STATUS_NAVI_AVOID = 1018; // Start obstacle avoidance
STATUS_NAVI_AVOID_END = 1019; // Obstacle avoidance finished
STATUS_NAVI_OUT_MAP = 1020; // Out of map. It is recommended to call stopNavigation to stop navigation
STATUS_NAVI_MULTI_ROBOT_WAITING = 1034; // Multi-robot scheduling waiting
STATUS_NAVI_MULTI_ROBOT_WAITING_END = 1035; // Multi-robot scheduling wait finished
STATUS_NAVI_GO_STRAIGHT = 1036; // Start moving straight
STATUS_NAVI_TURN_LEFT = 1037; // Start turning left
STATUS_NAVI_TURN_RIGHT = 1038; // Start turning right
STATUS_NAVI_GLOBAL_PATH_FAILED = 1025; // Path planning failed, navigation failed, need to handle error, It is recommendedmended to call stopNavigation to stop navigation
```

#### Navigation Error Code Definition (corresponding to NavigationListener's onError method)

```java
ERROR_DESTINATION_NOT_EXIST = -108; // Destination does not exist
ERROR_DESTINATION_CAN_NOT_ARRAIVE = -109; // Obstacle avoidance timeout, destination unreachable
ERROR_IN_DESTINATION = -113; // Currently near destination
ERROR_NOT_ESTIMATE = -116; // Currently not localized
ERROR_MULTI_ROBOT_WAITING_TIMEOUT = -125; // Multi-robot scheduling wait timeout
ERROR_NAVIGATION_FAILED = -120; // Navigation failed for other reasons, fallback failed
ACTION_RESPONSE_ALREADY_RUN = -1; // This interface has been called, please stop first before calling again
ACTION_RESPONSE_REQUEST_RES_ERROR = -6; // Already have interface call that needs to control chassis, please stop first before continuing
```

### Navigate to Specified Location

**Method Name:** `startNavigation`

**Result Callback:**

ActionListener navigationListener = new ActionListener() {
        @Override
        public void onResult(int status, String response) throws RemoteException {
            switch (status) {
                case Definition.RESULT_OK:
                    if ("true".equals(response)) {
                        // Navigation successful
                    } else {
                        // Navigation failed
                    }
                    break;
               case Definition.ACTION_RESPONSE_STOP_SUCCESS:// Cancel navigation task successfully
                    break;
            }
        }
        @Override public void onError(int errorCode, String errorString) throws RemoteException     {
            switch (errorCode) {
                case Definition.ERROR_NOT_ESTIMATE:              // Currently not localized
                    break;
                case Definition.ERROR_IN_DESTINATION:              // Current robot is already within destination range
                    break;
                case Definition.ERROR_DESTINATION_NOT_EXIST:              // Navigation destination does not exist
                    break;
                case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE:// Obstacle avoidance timeout, destination unreachable. Timeout duration is set via parameter
                    break;
                case Definition.ACTION_RESPONSE_ALREADY_RUN:           // This interface has been called, please stop first before calling again
                    break;
                case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:// Already have interface call that needs to control chassis, please stop first before continuing
                    break;
            }
        }
        @Override
        public void onStatusUpdate(int status, String data, String extraData) {
            switch (status) {
                case Definition.STATUS_NAVI_AVOID:               // Current route is blocked by obstacles
                    break;
                case Definition.STATUS_NAVI_AVOID_END:               // Obstacle has been removed
                    break;
            }
        }
}
**Invocation Method:**

1. **Default Navigation Speed**
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, time, navigationListener);
```

2. **Default Navigation Speed with Specified Obstacle Avoidance Distance** (If this value is set to 0, robot will use default value 0.75) * This calling method is supported from version V10 *
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, obsDistance, time, navigationListener);
```

3. **Specified Navigation Speed** * This calling method is supported from version V4.12 *
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, time, linearSpeed, angularSpeed, navigationListener);
```

4. **Specified Navigation Speed with Specified Obstacle Avoidance Distance** (If this value is set to 0, robot will use default value 0.75) * This calling method is supported from version V10 *
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, obsDistance, time, linearSpeed, angularSpeed, navigationListener);
```

5. **Specified Coordinates** * This calling method is supported from version V4.12 *
```java
RobotApi.getInstance().startNavigation(reqId, pose, coordinateDeviation, time, navigationListener);
```

6. **Specified Coordinates with Specified Obstacle Avoidance Distance** (If this value is set to 0, robot will use default value 0.75) * This calling method is supported from version V10 *
```java
RobotApi.getInstance().startNavigation(reqId, pose, coordinateDeviation, obsDistance, time, navigationListener);
```

7. **Specified Navigation Acceleration** (This calling method is supported from version V4.12. This interface currently only supports Caizhao Leopard)
```java
RobotApi.getInstance().startNavigation(reqId, destName, coordinateDeviation, time, linearSpeed, angularSpeed, isAdjustAngle, destinationRange, wheelOverCurrentRetryCount, multipleWaitTime, priority, linearAcceleration, angularAcceleration, navigationListener);
```

**Parameter Description:**

- `destName`: Navigation destination name (must be set first via setLocation)
- `pose`: Navigation destination coordinates
- `obsDistance`: Maximum obstacle avoidance distance. Robot stops when obstacles are closer than this distance. Value must be greater than 0, default 0.75, unit: meters
- `coordinateDeviation`: Destination range. If the distance to the destination is within this range, it is considered reached. Recommended value is 0.2, unit: meters
- `time`: Obstacle avoidance timeout duration. If robot movement is less than 0.1m within this time, navigation fails. Unit: milliseconds, recommended 30*1000
- `linearSpeed`: Navigation linear speed, range: 0.1 ~ 0.85 m/s, default: 0.7 m/s
- `angularSpeed`: Navigation angular speed, range: 0.4 ~ 1.4 rad/s, default: 1.2 rad/s. The final navigation speed is calculated based on the combination of linear and angular speeds. Different combinations of linear and angular speeds affect navigation motion patterns. It is recommended to maintain a certain relationship: angularSpeed = 0.4 + (linearSpeed – 0.1) / 3 * 4
- `isAdjustAngle`: Whether to adjust the facing angle when navigation finishes. If `false`, it will return to the angle set at the location point
- `destinationRange`: When target point is unreachable, the distance at which navigation is considered successful
- `wheelOverCurrentRetryCount`: Retry count for wheel blockage during navigation
- `multipleWaitTime`: Wait time for multi-robot encountered during navigation
- `priority`: Default is 0, range 0~30. Higher value means higher priority. Generally used for multi-robot avoidance during navigation
- `linearAcceleration`: Navigation linear acceleration, range: 0.4 ~ 0.8 m/s², default: 0.7 m/s²
- `angularAcceleration`: Navigation angular acceleration, range: 0.4 ~ 0.9 rad/s², default: 0.8 rad/s². The final navigation angular acceleration speed is obtained through linear speed conversion. It is recommended to maintain a certain relationship between linear acceleration and angular acceleration

> **Note:** Before calling this interface, ensure localization is complete

### Stop Navigation to Specified Location

**Method Name:** `stopNavigation`

**Invocation Method:**

```java
RobotApi.getInstance().stopNavigation(reqId);
```

> **Note:** This interface can only be used to stop navigation started by startNavigation

### Navigate to Specified Coordinates

> **Note:** This method is deprecated. It is recommended to use startNavigation. The goPosition interface is no longer maintained

**Method Name:** `goPosition`

**Result Callback:**

```java
CommandListener goPositionListener = new CommandListener() {
    @Override
    public void onResult(int status, String response) {
        switch (status) {
            case Definition.RESULT_OK:
                if ("true".equals(response)) {
                    // Navigation successful
                } else {
                    // Navigation failed
                }
                break;
        }
    }
    @Override
    public void onError(int errorCode, String errorString) throws RemoteException {
        switch (errorCode) {
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                // This interface has been called, please stop first before calling again
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                // Already have interface call that needs to control chassis, please stop first before continuing
                break;
        }
    }
    @Override
    public void onStatusUpdate(int status, String data) {
        switch (status) {
            case Definition.STATUS_NAVI_AVOID:
                // Current route is blocked by obstacles
                break;
            case Definition.STATUS_NAVI_AVOID_END:
                // Obstacle has been removed
                break;
        }
    }
};
```

**Invocation Method:**

1. **Default Speed**

```java
try {
    JSONObject position = new JSONObject();
    // X coordinate
    position.put(Definition.JSON_NAVI_POSITION_X, x);
    // Y coordinate
    position.put(Definition.JSON_NAVI_POSITION_Y, y);
    // Z coordinate
    position.put(Definition.JSON_NAVI_POSITION_THETA, theta);
    RobotApi.getInstance().goPosition(reqId, position.toString(), goPositionListener);
} catch (JSONException e) {
    e.printStackTrace();
}
```

2. **Specified Speed** * This calling method is supported from version V4.12 *

```java
try {
    JSONObject position = new JSONObject();
    // X coordinate
    position.put(Definition.JSON_NAVI_POSITION_X, x);
    // Y coordinate
    position.put(Definition.JSON_NAVI_POSITION_Y, y);
    // Z coordinate
    position.put(Definition.JSON_NAVI_POSITION_THETA, theta);
    RobotApi.getInstance().goPosition(reqId, position.toString(), linearSpeed, angularSpeed, goPositionListener);
} catch (JSONException e) {
    e.printStackTrace();
}
```

**Parameter Description:**

- `position`: Parameter is a JSON-formatted coordinate point `{ "x": "x coordinate", "y": "y coordinate", "theta": "z coordinate" }`
- `linearSpeed`: Navigation linear speed, range: 0.1 ~ 0.85 m/s, default: 0.7 m/s
- `angularSpeed`: Navigation angular speed, range: 0.4 ~ 1.4 rad/s, default: 1.2 rad/s

> **Note:** The final navigation speed is calculated by combining linear and angular speeds. Different combinations affect navigation motion patterns. It is recommended to maintain a certain relationship between linear and angular speeds = 0.4 + (linearSpeed – 0.1) / 3 * 4

> **Note:** Before calling this interface, ensure localization is complete

### Stop Navigation to Specified Coordinates

**Method Name:** `stopGoPosition`

**Invocation Method:**

```java
RobotApi.getInstance().stopGoPosition(reqId);
```

> **Note:** Both goPosition and startNavigation have corresponding stop interfaces that must be paired correctly and cannot be mixed

---

### Turn to Target Direction

**Method Name:** `resumeSpecialPlaceTheta`

**Method Description:** This interface only rotates horizontally to face the target location, it does not actually move to the target point.

**Invocation Method:**

```java
RobotApi.getInstance().resumeSpecialPlaceTheta(reqId,
    placeName,
    new CommandListener() {
        @Override
        public void onResponse(int result, String message) {
        }
    });
```

**Parameter Description:**

- `reqId`: int type, command ID
- `placeName`: String type, target location name
- `listener`: CommandListener type, message callback

**Return Value:**

- `int result`: 0 = command executed / -1 = command not executed

> **Note:** Before calling this interface, ensure localization is complete

### Calculate Distance Between Two Points in Patrol Mode

This API can be used to get the distance between two points (only for calculating the distance between two points on the patrol route in patrol mode)

**Method Name:** `getNaviPathInfo`

**Method Description:** Used to calculate the distance between two points on the patrol route in patrol mode. The input point information can be any point on the patrol route and can be obtained through the point retrieval API from the map.

**Invocation Method:**

```java
// Start and end points must be on the patrol line route, robot starts patrol line mode
Pose startPos = new Pose();
startPos.setX(-0.22329703f);
startPos.setY(1.1073834f);
startPos.setTheta(-1.2297891f);

Pose endPos = new Pose();
endPos.setX(0.09533833f);
endPos.setY(-0.7406802f);
endPos.setTheta(-2.886187f);

// Use this API to get path length, unit: meters
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

### Set Robot Acceleration/Deceleration Mode

When developing for Caizhao Leopard, you can globally set the robot's acceleration and braking acceleration to adapt to current business needs. The setting method is as follows:

#### 1. Add Permissions in APP

```xml
<uses-permission android:name="com.ainirobot.coreservice.robotSettingProvider" />
```

#### 2. Use Methods in Code to Change Settings

```java
public class NavigationSettings {
    // value "1" - "3": 1 soft 2 normal 3 urgent
    public static final String ROBOT_SETTING_NAVIGATION_BREAK_MODE_LEVEL = "robot_setting_navigation_break_mode_level";
    // value "1" - "3": 1 soft 2 normal 3 urgent
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

## Elevator Navigation

### Introduction

Elevator navigation refers to the robot's walking ability. The robot can move from point A to point B, automatically planning routes during movement, auto-switching maps for different floors, and effectively avoiding obstacles.

Robot navigation uses many sensors, including bottom LIDAR, RGBD, head IR sensors (on some models), etc. Please do not block these sensors during navigation to avoid robot immobility or path planning failure.

**Prerequisites for robot to execute navigation:**

- Robot has created new maps and is successfully localized on each map
- Robot must establish patrol lines
- Robot must be positioned at a patrol point
- Robot maps cannot use duplicated maps, which may cause point ID and floor conflicts, making navigation impossible
- LIDAR is in the enabled state
- Only supports elevator-control capable robots

> **Important Note:** Please pay special attention to the above prerequisites!

> **Note:** For third-party elevator control ROM, version 10.3 or higher is required. For proprietary elevator control ROM, version 10.5 or higher is required. Please consult pre-sales for your machine's elevator control type.

### Get Multi-Floor Location Data Information

**Method Name:** `getMultiFloorConfigAndPose`

**Invocation Method:**

```java
// Get multi-floor location list
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
### Navigate to Specified Location

**Method Name:** `startElevatorNavigation`

**Invocation Method:**

```java
RobotApi.getInstance().startElevatorNavigation(Definition.DEBUG_REQ_ID,
        dest,
        floor,
        new ActionListener() {
            @Override
            public void onResult(int status, String message, String extraData) throws RemoteException {
                Log.e("mName", "onResult: " + status + " responseString:" + message + " extraData:" + extraData);
                handlerResult(status, message, extraData);
            }

            @Override
            public void onError(int errorCode, String errorString, String extraData) throws RemoteException {
                Log.e("mName", "onError: " + errorCode + " errorString:" + errorString + " extraData:" + extraData);
                handlerError(errorCode, errorString, extraData);
            }

            @Override
            public void onStatusUpdate(int status, String data, String extraData) throws RemoteException {
                Log.e("mName", "onStatusUpdate: " + status + " data:" + data + " extraData:" + extraData);
                handlerStatusUpdate(status, data, extraData);
            }
        });
```
**handlerResult Method:**

```java
private void handlerResult(int status, String message, String extraData) {
    switch (status) {
        case Definition.RESULT_OK:
            // stop(ComponentResult.RESULT_NAVIGATION_ARRIVED, message, extraData);
            break;
        case Definition.ACTION_RESPONSE_STOP_SUCCESS:
            // stop(status, message + " Stop successful", extraData);
            break;
        case Definition.RESULT_FAILURE:
            // stop(status, message + " Navigation failed", extraData);
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
**handlerError Method:**

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
**handlerStatusUpdate Method:**

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
**Parameter Description:**

- `definition.DEBUG_REQ_ID`: int type, command ID, defines parameter reqId
- `dest`: String type, destination for this navigation, must not be empty and must be a point on the target floor map
- `floor`: int type, floor mapping where destination is located. Must not be 0 and must be in a map group managed by MapTool
- `listener`: ActionListener type, function listener with required callbacks: onResult, onError, onStatusUpdate

> **Note:** Before calling this interface, ensure localization is complete

### Stop Navigation to Specified Location

**Method Name:** `stopAdvanceNavigation`

**Invocation Method:**

```java
RobotApi.getInstance().stopAdvanceNavigation(reqId);
```

> **Note:** This interface can only be used to stop navigation started by startElevatorNavigation

---

### Status and Error Code Definition

#### onStatusUpdate
Events reported during navigation. Tasks will not stop when events are reported.

| Name | Code | Definition |
|------|------|------|
| `STATUS_START_NAVIGATION` | 1014 | Navigation started |
| `STATUS_NAVI_REPLACE_DESTINATION` | 1048 | Switch destination and start navigation |
| `STATUS_ESTIMATE_LOST` | 1045 | Localization lost |
| `STATUS_DISTANCE_WITH_DESTINATION` | 1050 | Distance to destination, unit (m). Example: extraData = "3" |
| `STATUS_NAVI_AVOID_IMMEDIATELY` | 1051 | Obstacle avoidance |
| `STATUS_GOAL_OCCLUDED` | 1016 | Obstacle avoidance started |
| `STATUS_NAVI_AVOID` | 1018 | Obstacle avoidance |
| `STATUS_GOAL_OCCLUDED_END` | 1017 | Obstacle avoidance finished |
| `STATUS_NAVI_AVOID_END` | 1019 | Obstacle avoidance finished |
| `STATUS_NAVI_OBSTACLES_AVOID` | 1023 | Obstacle avoidance |
| `STATUS_NAVI_MULTI_ROBOT_WAITING` | 1034 | Start waiting for other robots to pass (multi-robot avoidance started) |
| `STATUS_NAVI_MULTI_ROBOT_WAITING_END` | 1035 | Multi-robot avoidance finished |
| `STATUS_NAVI_GO_STRAIGHT` | 1036 | Robot moving in straight line |
| `STATUS_NAVI_TURN_LEFT` | 1037 | Robot turning left |
| `STATUS_NAVI_TURN_RIGHT` | 1038 | Robot turning right |

#### onResult

Status reported when navigation finishes.

| Name | Code | Definition |
|------|------|------|
| `RESULT_NAVIGATION_ARRIVED` | 102 | Arrived at destination |
| `RESULT_OK` | 1 | successful |
| `RESULT_FAILURE` | 2 | navigationfailed |
| `RESULT_DESTINATION_AVAILABLE` | 103 | Destination reachable, not occupied |
| `RESULT_DESTINATION_IN_RANGE` | 104 | Within destination range |
| `ACTION_RESPONSE_STOP_SUCCESS` | 3 | actionstopsuccessful |
| `STATUS_NAVI_OUT_MAP` | 1020 | Out of map range |
| `STATUS_NAVI_GLOBAL_PATH_FAILED` | 1025 | Navigation path planning failed |
| `STATUS_NAVI_MULTI_MAP_NOT_MATCH` | 1040 | Multi-robot map not matched |
| `STATUS_NAVI_MULTI_LORA_DISCONNECT` | 1041 | Multi-robot LoRa disconnected |
| `STATUS_NAVI_MULTI_LORA_CONFIG_FAIL` | 1042 | Multi-robot configuration error |
| `STATUS_NAVI_MULTI_VERSION_NOT_MATCH` | 1043 | Vision not matched |
| `STATUS_NAVI_WHEEL_SLIP` | 1044 | Wheel slip during navigation |

#### onError

Status reported when navigation finishes with an exception.

| Name | Code | Definition |
|------|------|------|
| `ERROR_PARAMETER` | -102 | Parameter error |
| `ERROR_TARGET_NOT_FOUND` | -107 | Point not found |
| `ERROR_DESTINATION_NOT_EXIST` | -108 | Destination does not exist |
| `ERROR_ESTIMATE_ERROR` | -128 | Re-localization failed |
| `ERROR_MULTIPLE_MODE_ERROR` | -127 | Multi-robot information error |
| `ERROR_PARAMS_JSON_PARSER_ERROR` | -32750026 | JSON parsing error |
| `ERROR_NO_AVAILABLE_DESTINATION` | -129 | No available destination found |
| `ERROR_NOT_ESTIMATE` | -116 | Not localized |
| `ERROR_IN_DESTINATION` | -113 | Already at destination |
| `ERROR_DESTINATION_CAN_NOT_ARRAIVE` | -109 | Destination unreachable |
| `ERROR_MULTI_ROBOT_WAITING_TIMEOUT` | -125 | Multi-robot avoidance wait timeout |
| `ERROR_NAVIGATION_AVOID_TIMEOUT` | -136 | Robot obstacle avoidance timeout |
| `ACTION_RESPONSE_ALREADY_RUN` | -1 | Request busy |
| `ACTION_RESPONSE_REQUEST_RES_ERROR` | -6 | Request failed |
| `ACTION_RESPONSE_RES_UNAVAILBALE` | -9 | Request unavailable |
| `ERROR_WHEEL_OVER_CURRENT_RUN_OUT` | -124 | Wheel blockage retry count exhausted |

### Elevator Navigation Example Address

> **Note:** For specific elevator navigation example code and usage instructions, please refer to the related sample projects.

## Gate Passing Navigation

### Introduction

The following are API introductions for robot gate passing. Before using the API, the following preparations are required:

**Preparation:**

- Machine hardware supports gate passing
- Set up gate line
- When creating map, set "gate entrance" and "gate exit" points with exact name matching
- Set patrol line to ensure gate entrance and exit have a unique patrol path that is bidirectional
- After completing the above steps, select Edit Map and gate line option to choose a patrol as the gate line, then save and upload to cloud

> **Note:** The gate line is unique for the machine and not associated with any map. It must be reset when switching maps

### Check if Gate Passing is Needed from Current Position

**Method Name:** `getGatePassingRoute`

**Invocation Method:**

```java
RobotApi.getInstance().getGatePassingRoute(reqId, targetPlaceName, new CommandListener() {
    @Override
    public void onResult(int result, String message) {
        // Process return result
        // If returned List<Pose> is empty, no need to pass through gate
        // If not empty, the two points are the "gate entrance" and "gate exit" in the map
    }
});
```

**Parameter Description:**

- `reqId`: Command ID
- `targetPlaceName`: Target location name for robot navigation
- `listener`: Callback that can return a `List<Pose>`. If empty, no gate passing is needed. If not empty, the two points are the "gate entrance" and "gate exit" points in the map

> **Note:** For detailed usage, refer to the Demo

### Navigate Through Gate

Use `getGatePassingRoute` to check if gate passing is needed. If so, navigate to the nearest point (obtained from the previous query), then control the gate to open (currently implemented by clients). After successful opening, navigate to the final destination.

**Implementation Flow:**

1. Call `getGatePassingRoute` to check if gate passing is needed
2. If gate passing is needed, navigate to the nearest point (gate entrance or exit)
3. Control the gate to open (must be implemented by client)
4. After successful opening, navigate to the final destination

## Basic Scenarios

### Introduction

Basic scenarios are functional components provided by RobotOS that include a series of functional policies. Using scenario APIs can quickly implement basic capabilities such as leading and registration. During execution, functional status is reported through callback functions, allowing developers to participate based on business requirements.

## Leading

### Scenario Introduction

Leading is a simple business scenario implemented for user secondary development. It internally integrates face recognition and navigation, continuously identifies leading targets during navigation, and reports target status in real-time. Users can respond based on reported status during secondary development to quickly implement their business requirements.

> **Note:** This API currently only supports Xiaobao Express with rear-facing camera. Other robots should use navigation to implement similar functionality

### Start Leading

**Method Name:** `startLead`

**Invocation Method:**

```java
LeadingParams params = new LeadingParams();
// Leading target person ID
params.setPersonId(personId);
// Leading destination
params.setDestinationName(dest);
// Report lost status after target not found
params.setLostTimer(lostTime);
// Time after leading starts before face recognition begins
params.setDetectDelay(delayTime);
// Report exceeding distance status after leading target is this far from robot
params.setMaxDistance(maxDistance);
RobotApi.getInstance().startLead(reqId, params, new ActionListener() {
    @Override
    public void onResult(int status, String responseString) throws RemoteException {
        switch (status) {
            case Definition.RESULT_OK:
                // Successfully led target to destination
                break;
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                // Manually called stopLead during leading, successfully stopped leading
                break;
            default:
                break;
        }
    }
    @Override
    public void onError(int errorCode, String errorString) throws RemoteException {
        switch (errorCode) {
            case Definition.ERROR_NOT_ESTIMATE:
                // Currently not localized
                break;
            case Definition.ERROR_SET_TRACK_FAILED:
            case Definition.ERROR_TARGET_NOT_FOUND:
                // Leading target not found
                break;
            case Definition.ERROR_IN_DESTINATION:
                // Currently already at leading destination
                break;
            case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE:
                // Obstacle avoidance timeout. Default: if robot moves less than 0.1m in 20 seconds
                break;
            case Definition.ERROR_DESTINATION_NOT_EXIST:
                // Leading destination does not exist
                break;
            case Definition.ERROR_HEAD:
                // Head gimbal operation failed during leading
                break;
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                // Leading is already in progress. Please stop the previous leading before executing again
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                // Already have interface call that needs to control chassis, please stop first before continuing
                break;
            default:
                break;
        }
    }
    @Override
    public void onStatusUpdate(int status, String data) throws RemoteException {
        switch (status) {
            case Definition.STATUS_NAVI_OUT_MAP:
                // Target point cannot be reached. Leading destination is outside map. May be due to map and position point mismatch. Please reset position point
                break;
            case Definition.STATUS_NAVI_AVOID:
                // Current leading route is blocked by obstacles
                break;
            case Definition.STATUS_NAVI_AVOID_END:
                // Obstacle has been removed
                break;
            case Definition.STATUS_GUEST_FARAWAY:
                // Leading target is too far from robot. Judgment standard is set via maxDistance parameter
                break;
            case Definition.STATUS_DEST_NEAR:
                // Leading target enters robot's maxDistance range
                break;
            case Definition.STATUS_LEAD_NORMAL:
                // Officially start navigation
                break;
            default:
                break;
        }
    }
});
```

### Stop Leading

**Method Name:** `stopLead`

**Invocation Method:**

```java
RobotApi.getInstance().stopLead(reqId, isResetHW);
```

**Parameter Description:**

- `isResetHW`: During leading, the camera switches to rear-facing. This parameter sets whether to restore camera state when stopping leading
  - `true`: Restore camera to front-facing
  - `false`: Keep the state at stop time
## Focus Following

### Scenario Introduction

Focus following tracks a specific person by face ID. The head gimbal continuously follows the target, the chassis moves in sync, until the target stops and the robot faces the target.

### Start Focus Following

**Method Name:** `startFocusFollow`

> **Note 1:** This method occupies chassis resources and cannot execute any chassis operations simultaneously, including navigation.
>
> **Note 2:** Do not repeatedly call this method to track the same person. Only call `startFocusFollow` again after losing tracking or manually stopping it.

**Invocation Method:**

```java

RobotApi.getInstance().startFocusFollow(reqId, faceId, lostTimeout, maxDistance, new ActionListener() {
    @Override
    public void onStatusUpdate(int status, String data) {
        switch (status) {
            case Definition.STATUS_TRACK_TARGET_SUCCEED:
                // Follow target successfully
                break;
            case Definition.STATUS_GUEST_LOST:
                // Follow target lost
                break;
            case Definition.STATUS_GUEST_FARAWAY:
                // Follow target distance exceeds set maximum distance
                break;
            case Definition.STATUS_GUEST_APPEAR:
                // Follow target re-enters set maximum distance range
                break;
        }
    }
    @Override public void onError(int errorCode, String errorString) {
        switch (errorCode) {
            case Definition.ERROR_SET_TRACK_FAILED:
            case Definition.ERROR_TARGET_NOT_FOUND:
                // Follow target not found
                break;
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                // Following in progress. Please stop previous following before executing again
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                // Already have interface call that needs to control chassis (e.g., leading, navigation)se stop first before continuing
                break;
        }
    }
    @Override public void onResult(int status, String responseString) {
        Log.d(TAG, "startTrackPerson onResult status: " + status);
        switch (status) {
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                // During focus following, manually called stopFocusFollow, successfully stopped following
                break;
        }
    }
});
```

**Parameter Description:**

- `faceId`: Target person face ID for focus following
- `lostTimeout`: Report target lost status after not recognizing target for this duration, typically 5-10 seconds
- `maxDistance`: Report exceeding max distance status when target is this far away, typically 5 meters

### Stop Focus Following

**Method Name:** `stopFocusFollow`

**Invocation Method:**

```java
RobotApi.getInstance().stopFocusFollow(reqId);
```
## Wake-up

### Scenario Introduction

Wake-up scenario means the robot turns towards the sound source direction when the wake word is spoken.

**Different Machine Type Strategies:**

- **Xiaobao Express Strategy**: When angle is less than 45°, only the head gimbal rotates. When greater than 45°, both head gimbal and chassis rotate to the target angle at maximum speed
- **Mini Strategy**: Rotate chassis towards sound source direction based on sound source location

### Sound Source Localization

When robot wakes up, you can get the angle between the waker and robot, called sound source localization angle. This is obtained in `ModuleCallback`'s `onSendRequest` callback, where `reqType` is `Definition.REQ_SPEECH_WAKEUP`, and `reqParams` is the sound source localization angle. It is a parameter for the wake-up API.

### Start Wake-up

**Method Name:** `wakeUp`

**Invocation Method:**

```java
RobotApi.getInstance().wakeUp(reqId, angle, new ActionListener() {
    @Override
    public void onResult(int status, String responseString) throws RemoteException {
        switch (status) {
            case Definition.RESULT_OK:
                // Wake up completed
                break;
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                // During wake-up, manually called stopWakeUp, stopped wake-up
                break;
        }
    }
    @Override public void onError(int errorCode, String errorString) throws RemoteException {
        switch (errorCode) {
            case Definition.ERROR_MOVE_HEAD_FAILED:
                // Head gimbal movement failed
                break;
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
                // Currently waking up. Need to stop previous wake-up first
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
                // Already have interface call that needs to control chassis (e.g., leading, navigation)ase stop first before continuing
            break;
        }
    }
});
```

**Parameter Description:**

- `angle`: Sound source localization angle

### Stop Wake-up

**Method Name:** `stopWakeUp`

**Invocation Method:**

```java
RobotApi.getInstance().stopWakeUp(reqId);
```

## Battery Management

### Get Current Battery Level

**Invocation Method:**

```java
RobotSettingApi.getInstance().getRobotString(Definition.ROBOT_SETTINGS_BATTERY_INFO);
```

### Start Auto Charging

**Method Name:** `startNaviToAutoChargeAction`

**Invocation Method:**

```java
RobotApi.getInstance().startNaviToAutoChargeAction(reqId, timeout, new ActionListener() {
    @Override
    public void onResult(int status, String responseString) throws RemoteException {
        switch (status) {
            case Definition.RESULT_OK:
                // Charging successful
                break;
            case Definition.RESULT_FAILURE:
                // Charging failed
                break;
        }
    }
    @Override
    public void onStatusUpdate(int status, String data) throws RemoteException {
        switch (status) {
            case Definition.STATUS_NAVI_GLOBAL_PATH_FAILED:
                // Global path planning failed
                break;
            case Definition.STATUS_NAVI_OUT_MAP:
                // Target point cannot be reached. Leading destination is outside map. May be due to map and position point mismatch. Please reset position point
                break;
            case Definition.STATUS_NAVI_AVOID:
                // Route to charging pile is blocked by obstacles
                break;
            case Definition.STATUS_NAVI_AVOID_END:
                // Obstacle has been removed
                break;
            default:
                break;
        }
    }
});
```

**Parameter Description:**

- `timeout`: Navigation timeout duration. If charging pile is not reached within this time, auto-charging is considered failed

### Stop Auto Charging

**Method Name:** `stopAutoChargeAction`

**Invocation Method:**

```java
RobotApi.getInstance().stopAutoChargeAction(reqId, true);
```
### Stop Charging and Undock from Charging Pile

**Method Name:** `leaveChargingPile`

**Invocation Method:**

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
                    case Definition.RESULT_FAILURE_MOTION_AVOID_STOP:// Obstacle ahead, undocking failed
                        break;
                    case Definition.RESULT_FAILURE_TIMEOUT:// Undocking timeout (15s)
                        break;
                    case Definition.STATUS_LEAVE_PILE_OPEN_RADAR_FAILURE:// Radar startup failed
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onResult(int result, String message, String extraData) {
                Log.d(TAG, "leaveChargingPile onResult: " + result + " message:" + message);
                switch (result) {
                    case Definition.RESULT_OK:// Undocking successful
                        // If you want to check charging status here, note that charging status updates take some time
                        break;
                }
            }
        });
```

**Parameter Description:**

- `speed`: Speed for moving away from charging pile, default 0.7
- `distance`: Distance to move away from charging pile, default 0.2, unit: meters

> **Note 1:** This method cannot be used for contact charging undocking.
>
> **Note 2:** This method requires calling `RobotApi.getInstance().disableBattery();` to disable system charging first.


### Multi-Robot Coordination and Scheduling

#### Multi-Robot Coordination

Multi-robot coordination includes multi-robot scheduling and avoidance capabilities, involving fundamental multi-robot cooperation abilities. The communication module involved here is part of the robot's multi-robot cooperation foundation and system functions.

Robot multi-robot coordination requires no manual operation; all behaviors are system-scheduled. However, all robots must work on the same map. When multiple robots meet, they communicate through the robot communication module, which sorts their work priorities to determine whether to yield or continue working. This prevents multiple robots from encountering each other at the same location and becoming stuck. Therefore, as long as robot performance and hardware are functioning normally, multi-robot cooperation will work properly.

#### Multi-Robot Coordination Information

On robots with installed ESP32 multi-robot communication module, you can use the following methods to get multi-robot information for creating multi-robot scheduling systems.

> **Note:** Currently, Caizhao Leopard has ESP32 multi-robot communication module. Other robots do not include this module by default.

**Invocation Method:**

```java
RobotApi.getInstance().registerStatusListener(Definition.STATUS_MULTIPLE_ROBOT_WORKING, mStatusListener);

private StatusListener mStatusListener = new StatusListener() {
    @Override
    public void onStatusUpdate(String type, String data) {
        try {
            Type dataType = new TypeToken<List<MultiRobotStatus>>(){}.getType();
            List<MultiRobotStatus> curRobotStatus = mGson.fromJson(data, dataType);
        } catch(Exception ex) {
            // Handle exception
        }
    }
};
```

**MultiRobotStatus Class Definition:**

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

## System Functions

### System Status Monitoring

Robot has many states and events that can be monitored to assist work. Monitoring is mainly done using the functions below.

**Invocation Method:**

```java
RobotApi.getInstance().registerStatusListener(
        Definition.STATUS_POSE_ESTIMATE, mStatusPoseListener);
```

Event definitions that can be monitored can be found in SDK's `Definition.java`. Currently provided monitoring states are as follows:

```java
STATUS_POSE = "navi_pose"; // Location changed
STATUS_MAP = "navi_map"; // Map changed
STATUS_EMERGENCY = "status_emergency"; // Emergency status
STATUS_POSE_ESTIMATE = "status_pose_estimate"; // Robot localization status
STATUS_AVOID_STOPPING = "status_avoid_stopping"; // Dynamic obstacle avoidance stop
STATUS_SWITCH_MAP = "status_switch_map"; // Map switching
STATUS_RADAR = "status_radar"; // Radar status
STATUS_BATTERY = "status_battery"; // Battery charging status
STATUS_MULTIPLE_ROBOT_WORKING = "status_multiple_robot_working"; // Multi-robot status information
STATUS_MAP_OUTSIDE = "status_map_outside_report"; // Robot outside map (currently Caizhao only)
STATUS_ROBOT_BEING_PUSHED = "status_robot_being_pushed"; // Robot being pushed (currently Caizhao only)
```
### Set LED Effect

**Method Name:** `setLight`

**Invocation Method:**

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
**Parameter Description:**

- `reqId`: int type, command ID
- `params`: String type, can include the following parameters:
  - `type`: Fill 0
  - `startColor`: LED effect starting color value
  - `endColor`: LED effect ending color value
  - `startTime`: Duration for starting color to stay
  - `endTime`: Duration for ending color to stay
  - `loopTime`: LED effect gradient repeat count
  - `duration`: Time spent on gradient process
  - `finalColor`: Gradient transition color
- `listener`: ActionListener type, message callback

> **Note:** Color values use 16-bit RGB strings, such as "F3F3F3" or "00FF00".

**Return Value:**

- `int result`: 0 = command executed / -1 = command not executed

> **Example Code:** LED effect setting demo can be found here (code applies to Xiaobao Express robot, for other robots please replace the robot JAR package): click here

### Setting LED Effects for Caizhao Leopard Pro and Xiaobao Express Pro

#### Check if Using ProZcbLed Light Strip Interface

**Method Name:** `isUseProZcbLed`

**Invocation Method:**

```java
/**
 * Whether to use ProZcbLed light strip interface
 */
RobotApi.getInstance().isUseProZcbLed();
```

#### Set Chassis Light Strip Effect

**Method Name:** `setProZcbLedEffect`

**Invocation Method:**

```java
/**
 * Control ZcbLed light, i.e., Saiph Pro chassis light strip
 * proZcbEffect parameter refer to color definition below
 * Zcb: Caizhao Leopard
 */
RobotApi.getInstance().setProZcbLedEffect(int reqId, int proZcbEffect, CommandListener listener);
```

#### Check if Clavicle Light Exists

**Method Name:** `isHasClavicleLight`

**Invocation Method:**

```java
/**
 * Whether clavicle light exists
 */
RobotApi.getInstance().isHasClavicleLight();
```

#### Set Clavicle Light Effect

**Method Name:** `setProIobLedEffect`

**Invocation Method:**

```java
/**
 * Control IobLed light, i.e., Saiph Pro clavicle light
 * proIobLedEffect parameter refer to color definition below
 * "Iob" appears to be a spelling error, should be Lobby, refers to chest light strip
 */
RobotApi.getInstance().setProIobLedEffect(int reqId, int proIobLedEffect, CommandListener listener);
```
#### Color Definition

```java
public static final int ZCB2UARTLED_GREENBREATH = 0xDE10;  // Green breathing effect
public static final int ZCB2UARTLED_BLUEBREATH = 0xDE11;  // Blue breathing effect
public static final int ZCB2UARTLED_ORANGEBREATH = 0xDE12;  // Orange breathing effect
public static final int ZCB2UARTLED_YELLOWBREATH = 0xDE13;  // Yellow breathing effect
public static final int ZCB2UARTLED_BLUENORMAL = 0xDE14;  // Blue normal effect
public static final int ZCB2UARTLED_REDNORMAL = 0xDE15;  // Red normal effect
public static final int ZCB2UARTLED_ORANGENORMAL = 0xDE16;  // Orange normal effect
public static final int ZCB2UARTLED_YELLOWNORMAL = 0xDE17;  // Yellow normal effect
public static final int ZCB2UARTLED_GREENNORMAL = 0xDE18;  // Green normal effect
public static final int ZCB2UARTLED_TURNRIGHT = 0xDE19;  // Right turn effect
public static final int ZCB2UARTLED_TURNLEFT = 0xDE20;  // Left turn effect
public static final int ZCB2UARTLED_REGFLASH = 0xDE21;  // Red flashing effect
public static final int ZCB2UARTLED_YELLOWFLASH = 0xDE22;  // Yellow flashing effect
public static final int ZCB2UARTLED_ALLOFF = 0xDE00;  // Turn off all ZCB effects
```
Get Device SN

**Method Name:** `getRobotSn`

**Invocation Method:**

```java
boolean status = RobotApi.getInstance().getRobotSn(
    new CommandListener(){
        @Override
        public void onResult(int result, String message) {
            if (Definition.RESULT_OK == result) {
                String serialNum = message;
            } else {
                // Handle error
            }
        }
    });
```

**Parameter Description:**

- `listener`: CommandListener message callback {"code":0, "message":"err msg"}

**Return Value:**

- `int result`: 1 = command executed / -1 = command not executed

---

### Get System Version

**Method Name:** `getVersion`

**Invocation Method:**

```java
String version = RobotApi.getInstance().getVersion();
```

---

### Disable System Functions

System no longer handles emergency stop events, no emergency stop screen. Can be used for custom emergency stop screen. When emergency stopped, all chassis-related APIs are disabled, and state switching APIs like wake-up and sleep are ineffective.

**Method Name:** `disableEmergency`

**Invocation Method:**

```java
RobotApi.getInstance().disableEmergency();
```

**API for Getting Emergency Status:**

```java
RobotApi.getInstance().getRobotStatus(Definition.STATUS_EMERGENCY, new StatusListener(){
    @Override
    public void onStatusUpdate(String type, String data) throws RemoteException {
        Log.d("onStatusUpdate","Status:"+data);
    }
});

// Or use the method below to register emergency stop status event
RobotApi.getInstance().registerStatusListener(Definition.STATUS_EMERGENCY, new StatusListener(){
    @Override
    public void onStatusUpdate(String type, String data) throws RemoteException {
        Log.d("onStatusUpdate","status:"+type+";value:"+data);
    }
});
```

---

### Disable Battery Interface

Disable current battery interface. During charging, all client app capabilities except chassis operations are available.

If charging takeover is not needed, it is recommended to call this interface immediately after successful RobotAPI connection on app startup. The charging takeover screen will remain disabled until app exit.

**Method Name:** `disableBattery`

**Invocation Method:**

```java
RobotApi.getInstance().disableBattery();
```

**API for Getting Battery Status:**

```java
RobotSettingApi.getInstance().getRobotString(Definition.ROBOT_SETTINGS_BATTERY_INFO);

// Or use the method below to listen for battery status changes
RobotApi.getInstance().registerStatusListener(Definition.STATUS_BATTERY, listener);
```

---

### Disable Function Keys

Disable function keys (buttons on the back of Caizhao Leopard's head).

**Method Name:** `disableFunctionKey`

**Invocation Method:**

```java
RobotApi.getInstance().disableFunctionKey();
```

---

### Sleep Function

#### Scenario Introduction

Sleep is a low-power running mode for robots when there are no tasks or low battery.

> **Note:** Using sleep API requires adding the following permissions:
> ```xml
> <uses-permission android:name="com.ainirobot.coreservice.robotSettingProvider" />
> ```

#### Start Sleep

**Method Name:** `robotStandby`

**Invocation Method:**

```java
RobotApi.getInstance().robotStandby(0, new CommandListener() {
    @Override
    public void onStatusUpdate(int status, String data, String extraData) {
        super.onStatusUpdate(status, data, extraData);
    }
});
```

#### Stop Sleep

**Method Name:** `robotStandbyEnd`

**Invocation Method:**

```java
RobotApi.getInstance().robotStandbyEnd(reqId);
```

---

### Install APK

**Method Name:** `installApk`

**Invocation Method:**

```java
RobotApi.getInstance().installApk(reqid, fullPathName, taskID);
```

**Parameter Description:**

- `reqId`: int type, command ID
- `fullPathName`: Installation package path
- `taskID`: Task ID, any non-empty string content
