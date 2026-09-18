package com.geomagnet.edu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ainirobot.agent.AgentCore
import com.ainirobot.agent.PageAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.ActionExecutor
import com.ainirobot.agent.action.ActionResult
import com.ainirobot.agent.action.ActionStatus
import com.ainirobot.agent.coroutine.AOCoroutineScope
import com.geomagnet.edu.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * 地磁知识讲解员主页面。
 *
 * 对应 AgentOS SDK 文档「1.2.5 创建 PageAgent」：
 * - 每个页面只能存在一个 PageAgent 实例
 * - 页面级 Action 仅在当前页面对用户可见时生效
 * - 所有 Action 执行完成后必须调用 action.notify() 上报状态
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TOPIC_ACTION = "extra_topic_action"
    }

    private lateinit var binding: ActivityMainBinding

    /** 页面级 Agent（懒加载，确保单例） */
    private val pageAgent: PageAgent by lazy { PageAgent(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPageAgent()
        setupTopicList()

        // 感知信息上报：让大模型理解当前页面内容（文档 4.10 节）
        AgentCore.uploadInterfaceInfo(buildPageInfo())

        // 若由外部静态 Action 拉起，直接展示对应主题
        intent?.getStringExtra(EXTRA_TOPIC_ACTION)?.let { actionName ->
            GeomagnetismContent.findByActionName(actionName)?.let { showTopic(it) }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent.getStringExtra(EXTRA_TOPIC_ACTION)?.let { actionName ->
            GeomagnetismContent.findByActionName(actionName)?.let { showTopic(it) }
        }
    }

    /** 注册页面级 Action：地磁知识库的每个主题对应一个单一职责的 Action */
    private fun setupPageAgent() {
        GeomagnetismContent.TOPICS.forEach { topic ->
            pageAgent.registerAction(
                Action(
                    name = topic.actionName,
                    displayName = topic.title,
                    desc = topic.desc,
                    parameters = null,
                    executor = object : ActionExecutor {

                        /**
                         * Action 执行回调。
                         * 注意：回调运行在子线程，且不能执行耗时操作——
                         * 必须立即返回 true，把耗时逻辑（TTS 播报等）放到协程中执行，
                         * 完成后调用 action.notify() 上报执行结果（文档 2.3 节）。
                         */
                        override fun onExecute(action: Action, params: Bundle?): Boolean {
                            AOCoroutineScope.launch {
                                try {
                                    // 在 UI 线程更新页面展示
                                    runOnUiThread { showTopic(topic) }

                                    // TTS 语音播报（同步接口，需在协程中调用）
                                    AgentCore.ttsSync(topic.speechText)

                                    // 执行完成，及时上报系统
                                    action.notify()
                                } catch (e: Exception) {
                                    action.notify(ActionResult(ActionStatus.FAILED))
                                }
                            }
                            return true
                        }
                    }
                )
            )
        }
    }

    /** 主题卡片列表：点击卡片 = 以文本指令模拟语音提问（文档 4.9 节） */
    private fun setupTopicList() {
        binding.topicList.layoutManager = LinearLayoutManager(this)
        binding.topicList.adapter = TopicAdapter(GeomagnetismContent.TOPICS) { topic ->
            AgentCore.query(topic.queryDemo)
        }
    }

    /** 展示某个主题的详细内容，并上报页面感知信息 */
    private fun showTopic(topic: GeomagTopic) {
        binding.detailTitle.text = topic.title
        binding.detailSubtitle.text = topic.subtitle
        binding.detailBody.text = topic.sections.joinToString("\n\n") { (head, body) -> "【$head】\n$body" }
        AgentCore.uploadInterfaceInfo(topic.interfaceInfo)
    }

    /** 页面初始感知信息 */
    private fun buildPageInfo(): String {
        val names = GeomagnetismContent.TOPICS.joinToString("、") { it.title }
        return "当前页面：地磁知识讲解员主界面。用户可点击以下话题卡片或直接语音提问：$names。"
    }
}
