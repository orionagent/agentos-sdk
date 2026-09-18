package com.geomagnet.edu

import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.ainirobot.agent.AppAgent
import com.ainirobot.agent.action.Action
import com.ainirobot.agent.action.Actions

/**
 * 应用级 Agent。
 *
 * 对应 AgentOS SDK 文档「1.2.4 创建 AppAgent」：
 * - 一个应用中只能存在一个 AppAgent 实例
 * - 在 onCreate 中配置角色人设 / 目标，并动态注册 App 级 Action
 * - onExecuteAction 处理 actionRegistry.json 中静态注册的 Action（外部应用调用入口）
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        object : AppAgent(this) {

            override fun onCreate() {
                // 人设：定义“我是谁”
                setPersona("你叫“地磁讲解员”，是一位专业、严谨、亲切的地磁学科普专家，擅长用通俗易懂的比喻讲解地磁知识。")

                // 风格：定义“如何说话”
                setStyle("专业、严谨、亲切，条理清晰，善用比喻，回答时优先调用已注册的地磁知识Action给出完整讲解")

                // 目标：定义“要做什么”
                setObjective("当用户询问地磁、地磁场、指南针、磁偏角、地磁导航、磁法勘探、磁暴、地磁极倒转、地磁优势等相关问题时，规划并执行对应的地磁知识讲解Action，向用户详细介绍地磁的原理、应用场景、优势与历史，确保回答准确、完整、易懂。")

                // 动态注册 App 级 Action：
                // SAY —— 机器人兜底对话（让机器人能直接通过 TTS 回答问题/反问）
                // EXIT —— 用户说“退出”时触发返回
                registerAction(Actions.SAY)
                registerAction(Actions.EXIT)
            }

            /**
             * actionRegistry.json 中静态注册的 Action（被其它 AgentOS 应用调用时）执行回调。
             * 返回 true 表示已自行处理；false 表示交由系统继续处理。
             */
            override fun onExecuteAction(
                action: Action,
                params: Bundle?
            ): Boolean {
                val topic = GeomagnetismContent.findByActionName(action.name)
                if (topic != null) {
                    val intent = Intent(this@MainApplication, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(MainActivity.EXTRA_TOPIC_ACTION, action.name)
                    startActivity(intent)
                    return true
                }
                return false
            }
        }
    }
}
