plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.geomagnet.edu"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.geomagnet.edu"
        // 最低支持 API 26 (Android 8.0)，符合 AgentOS SDK 文档要求
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 【重要配置】AgentOS SDK 依赖（v0.4.12，已自动集成 RobotService.jar）
    implementation("com.orionstar.agent:sdk:0.4.12-SNAPSHOT")

    // Android 标准库
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // 协程（AOCoroutineScope.launch 依赖）
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
