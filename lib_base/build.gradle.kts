plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    namespace = "com.ted.lib_base"
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

        consumerProguardFiles("consumer-rules.pro")
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

}

dependencies {
    //Kotlin基础库
    api(DependenciesConfig.STD_LIB)
    //Android标准库
    api(DependenciesConfig.APP_COMPAT)
    //Kotlin核心库
    api(DependenciesConfig.KTX_CORE)
    //EventBus
    api(DependenciesConfig.EVENT_BUS)
    //ARouter
    api(DependenciesConfig.AROUTER)
    //RecyclerView
    api(DependenciesConfig.RECYCLERVIEW)
    //AndPermissions
    api(DependenciesConfig.AND_PERMISSIONS)


    api(project(":lib_voice"))
    api(project(":lib_network"))
}