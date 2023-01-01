plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

//Android属性
android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName


    }

    //签名配置
    signingConfigs {
        register("release") {
            //别名
            keyAlias = "AIVoice"
            //别名密码
            keyPassword = "123456"
            //路径
            storeFile = file("/src/main/jks/aivoice.jks")
            //密码
            storePassword = "123456"
        }
    }

    //编译类型
    buildTypes {
        getByName("debug") {

        }
        getByName("release") {
            isMinifyEnabled = false
            //自动签名打包
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    //输出类型
    android.applicationVariants.all {
        //编译类型
        val buildType = this.buildType.name
        outputs.all {
            //输出APK
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                if (buildType == "release") {
                    this.outputFileName = "AI_V${defaultConfig.versionName}_$buildType.apk"
                }
            }
        }
    }

    //依赖操作
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //Android标准库
    implementation(DependenciesConfig.APP_COMPAT)
    //Kotlin核心库
    implementation(DependenciesConfig.KTX_CORE)

}