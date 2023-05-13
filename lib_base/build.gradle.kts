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
    //ViewPager
    api(DependenciesConfig.VIEWPAGER)
    api(DependenciesConfig.MATERIAL)
    //Lottie
    api(DependenciesConfig.LOTTIE)
    //刷新
    api(DependenciesConfig.REFRESH_KERNEL)
    api(DependenciesConfig.REFRESH_HEADER)
    api(DependenciesConfig.REFRESH_FOOT)
    //图表
    api(DependenciesConfig.CHART)

    //引入Jar与AAR
    api(files("libs/BaiduLBS_Android.jar"))
    api(files("libs/IndoorscapeAlbumPlugin.jar"))

    api(project(":lib_voice"))
    api(project(":lib_network"))
}