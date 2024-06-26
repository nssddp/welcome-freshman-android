plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("com.google.protobuf") version "0.9.4"
}

android {
    namespace = "com.example.welcome_freshman"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.welcome_freshman"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            // 设置支持的SO库架构（开发者可以根据需要，选择一个或多个平台的so）
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    };

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}


dependencies {
    // 多边形
    implementation ("androidx.graphics:graphics-shapes:1.0.0-alpha05")
//    api ("com.tencent.mm.opensdk:wechat-sdk-android:6.8.28")

//    implementation("com.airbnb.android:lottie-compose:4.1.0")
    // 为了转圈动画下载
    implementation("androidx.compose.material3:material3-android:1.2.1")

    // 为了新的滑动下载
    implementation("androidx.compose.foundation:foundation:1.6.3")

    // compose-viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // 启动页
    implementation("androidx.core:core-splashscreen:1.0.1")

    // 页面导航管理
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // 图标扩展
    implementation("androidx.compose.material:material:1.6.3")
    implementation("androidx.compose.material:material-icons-extended:1.6.3")

    // 权限申请
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // 定位
    implementation("com.baidu.lbsyun:BaiduMapSDK_Location:9.3.7")

    // 地图
    implementation ("com.baidu.lbsyun:BaiduMapSDK_Map:7.5.4")
    implementation ("com.baidu.lbsyun:BaiduMapSDK_Search:7.5.4")
    implementation ("com.baidu.lbsyun:BaiduMapSDK_Util:7.5.4")

    // 加载图片
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-gif:2.6.0")

    // 网络请求
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // 依赖注入
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // 自适应窗口
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

    // 视频
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")
    implementation("androidx.media3:media3-common:1.2.1")

    //cameraX
    val cameraxVersion = "1.3.2"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-video:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-extensions:$cameraxVersion")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.25.2")
    implementation("com.google.protobuf:protobuf-javalite:3.25.2")

//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}


