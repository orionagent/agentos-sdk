# Agent SDK Import 路径概览

本列表汇总了 Agent SDK 开发中常用的所有核心 import 路径，涵盖了 Agent 生命周期、动作（Action）定义与执行、参数与注解、LLM（大模型）集成、回调监听、任务与消息处理、以及常用工具类等模块。  
开发者可根据实际业务需求，按需引用相关类，快速实现与 Agent 相关的交互、动作注册、语音处理、消息通信等功能。

**说明：**  
本 import 列表适用于 Agent SDK 的全功能开发场景，便于统一查阅和复制粘贴。实际开发时可根据 IDE 自动补全和具体业务需求精简引用。

```kotlin
import com.ainirobot.agent.base.ActionEntity
import com.ainirobot.agent.base.ActionParams
import com.ainirobot.agent.base.ActionResult
import com.ainirobot.agent.base.ActionStatus
import com.ainirobot.agent.base.Parameter
import com.ainirobot.agent.base.ParameterType
import com.ainirobot.agent.base.Transcription
import com.ainirobot.agent.base.llm.LLMConfig
import com.ainirobot.agent.base.llm.LLMMessage
import com.ainirobot.agent.base.llm.Role
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor
import com.ainirobot.agent.action.Actions
import com.ainirobot.agent.annotations.AgentAction
import com.ainirobot.agent.annotations.ActionParameter
import com.ainirobot.agent.OnAgentStatusChangedListener
import com.ainirobot.agent.OnTranscribeListener
import com.ainirobot.agent.ITaskCallback
import com.ainirobot.agent.TTSCallback
import com.ainirobot.agent.LLMCallback
import com.ainirobot.agent.TaskResult
import com.ainirobot.agent.assit.LLMResponse
import com.ainirobot.agent.base.AppInfo 
import com.ainirobot.agent.base.PageInfo
import com.ainirobot.agent.base.ActionRegistry
import com.ainirobot.agent.base.Message
import com.ainirobot.agent.Agent
import com.ainirobot.agent.AgentType
import com.ainirobot.agent.assit.TokenCost
import com.ainirobot.agent.base.utils.DigitalUtil
import com.ainirobot.agent.base.utils.ShortUuid
```

---

# RobotOS SDK Import 路径概览

本列表汇总了 RobotOS SDK 开发中常用的所有核心 import 路径，涵盖了机器人底层服务、导航定位、动作控制、传感器数据、人脸识别、语音交互、设备管理、异常处理等模块。  
开发者可根据实际机器人功能需求，按需引用相关类，快速实现机器人的移动控制、传感器数据获取、人机交互、系统配置等底层功能。

**说明：**  
本 import 列表适用于 RobotOS SDK 的全功能开发场景，涵盖机器人硬件控制和系统服务的完整API。实际开发时可根据具体功能模块按需引用。

```java
import com.ainirobot.base.OrionBase;
import com.ainirobot.coreservice.IModuleRegistry;
import com.ainirobot.coreservice.IRobotBinderPool;
import com.ainirobot.coreservice.bean.BackgroundTaskEvent;
import com.ainirobot.coreservice.bean.LoadMapBean;
import com.ainirobot.coreservice.bean.Task;
import com.ainirobot.coreservice.client.Definition.TrackMode;
import com.ainirobot.coreservice.client.account.AccountApi;
import com.ainirobot.coreservice.client.actionbean.AngleResetBean;
import com.ainirobot.coreservice.client.actionbean.AutoChargeBean;
import com.ainirobot.coreservice.client.actionbean.BodyFollowBean;
import com.ainirobot.coreservice.client.actionbean.CheckObstacleBean;
import com.ainirobot.coreservice.client.actionbean.CommandBean;
import com.ainirobot.coreservice.client.actionbean.ControlElectricDoorBean;
import com.ainirobot.coreservice.client.actionbean.CruiseParams;
import com.ainirobot.coreservice.client.actionbean.CruiseRouteBean;
import com.ainirobot.coreservice.client.actionbean.FaceTrackBean;
import com.ainirobot.coreservice.client.actionbean.FocusFollowBean;
import com.ainirobot.coreservice.client.actionbean.GoPositionBean;
import com.ainirobot.coreservice.client.actionbean.GoPositionByTypeBean;
import com.ainirobot.coreservice.client.actionbean.GongFuBean;
import com.ainirobot.coreservice.client.actionbean.HeadTurnBean;
import com.ainirobot.coreservice.client.actionbean.HeadTurnBean.HeadTurnMode;
import com.ainirobot.coreservice.client.actionbean.InspectActionBean;
import com.ainirobot.coreservice.client.actionbean.LeadingParams;
import com.ainirobot.coreservice.client.actionbean.LedLightBean;
import com.ainirobot.coreservice.client.actionbean.NaviCmdTimeOutBean;
import com.ainirobot.coreservice.client.actionbean.NavigationAdvancedBean;
import com.ainirobot.coreservice.client.actionbean.NavigationBean;
import com.ainirobot.coreservice.client.actionbean.ObstacleInAreaBean;
import com.ainirobot.coreservice.client.actionbean.PictureInfo;
import com.ainirobot.coreservice.client.actionbean.PlaceBean;
import com.ainirobot.coreservice.client.actionbean.Pose;
import com.ainirobot.coreservice.client.actionbean.PushReportBean;
import com.ainirobot.coreservice.client.actionbean.RadarAlignBean;
import com.ainirobot.coreservice.client.actionbean.RegisterBean;
import com.ainirobot.coreservice.client.actionbean.ResetEstimateParams;
import com.ainirobot.coreservice.client.actionbean.RobotStandbyBean;
import com.ainirobot.coreservice.client.actionbean.SearchPersonBean;
import com.ainirobot.coreservice.client.actionbean.SmartFocusFollowBean;
import com.ainirobot.coreservice.client.actionbean.StartCreateMapBean;
import com.ainirobot.coreservice.client.actionbean.StopCreateMapBean;
import com.ainirobot.coreservice.client.actionbean.UnRegisterBean;
import com.ainirobot.coreservice.client.actionbean.WakeUpBean;
import com.ainirobot.coreservice.client.ashmem.ShareMemoryApi;
import com.ainirobot.coreservice.client.exception.InvalidArgumentException;
import com.ainirobot.coreservice.client.exception.NoSuchKeyException;
import com.ainirobot.coreservice.client.exception.ValueFormatException;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.listener.Person;
import com.ainirobot.coreservice.client.listener.PersonInfoListener;
import com.ainirobot.coreservice.client.log.RLog;
import com.ainirobot.coreservice.client.messagedispatcher.StatusDispatcher;
import com.ainirobot.coreservice.client.module.ModuleCallbackApi;
import com.ainirobot.coreservice.client.permission.PermissionApi;
import com.ainirobot.coreservice.client.person.PersonApi;
import com.ainirobot.coreservice.client.robotsetting.RobotSettingApi;
import com.ainirobot.coreservice.client.robotsetting.RobotSettingListener;
import com.ainirobot.coreservice.client.surfaceshare.SurfaceShareApi;
import com.ainirobot.coreservice.core.InternalDef;
import com.ainirobot.coreservice.listener.IActionListener;
import com.ainirobot.coreservice.utils.FileUtils;
import com.ainirobot.coreservice.utils.SettingDataHelper;
import com.ainirobot.coreservice.utils.Utils;
import com.ainirobot.coreservice.utils.ZipUtils;
import com.ainirobot.coreservice.client.ApiListener
import com.ainirobot.coreservice.client.RobotApi
```
