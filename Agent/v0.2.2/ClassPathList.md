# Agent SDK Import Path Overview

description Agent SDK description import description，description Agent description、description（Action）descriptionexecute、description、LLM（LLM）description、callbackListen to 、description、descriptionUtility Classesdescription。  
description，description，description Agent description、descriptionRegister 、description、description。

**Description：**  
description import description Agent SDK description，description。description IDE description。

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

# RobotOS SDK Import Path Overview

description RobotOS SDK description import description，description、description、description、description、description、description、description、description。  
description，description，description、descriptionGet 、description、description。

**Description：**  
description import description RobotOS SDK description，descriptionAPI。description。

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
```
