plugins {
    if (ModuleConfig.isApp) {
        id("com.android.application")
    } else {
        id("com.android.library")
    }
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    namespace = "com.ted.module_setting"
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32
//        if (ModuleConfig.isApp) {
//            applicationId = ModuleConfig.MODULE_SETTING
//        }
        //ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    //动态替换资源
    sourceSets {
        getByName("main") {
            if (ModuleConfig.isApp) {
                manifest.srcFile("src/main/manifest/AndroidManifest.xml")
            } else {
                manifest.srcFile("src/main/AndroidManifest.xml")
            }
        }
    }
}

dependencies {
    implementation(project(":lib_base"))
    //运行时注解
    kapt(DependenciesConfig.AROUTER_COMPILER)
}