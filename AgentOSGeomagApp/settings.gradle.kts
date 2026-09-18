pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            // 【重要配置】AgentOS SDK 私有 Maven 仓库
            credentials {
                username = "agentMaven"
                password = "agentMaven"
            }
            url = uri("https://npm.ainirobot.com/repository/maven-public/")
        }
    }
}

rootProject.name = "AgentOSGeomagApp"
include(":app")
