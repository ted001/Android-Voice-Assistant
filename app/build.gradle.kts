plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

//Android属性
android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        //ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }

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
    implementation(project(":lib_base"))

    if (!ModuleConfig.isApp) {
        implementation(project(":module_app_manager"))
        implementation(project(":module_constellation"))
        implementation(project(":module_developer"))
        implementation(project(":module_joke"))
        implementation(project(":module_map"))
        implementation(project(":module_setting"))
        implementation(project(":module_voice_setting"))
        implementation(project(":module_weather"))
    }

    //运行时注解
    kapt(DependenciesConfig.AROUTER_COMPILER)

}