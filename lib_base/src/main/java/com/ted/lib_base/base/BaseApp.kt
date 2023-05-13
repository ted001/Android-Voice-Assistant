package com.ted.lib_base.base

import android.app.Application
import android.content.Intent
import android.os.Build
import com.ted.lib_base.helper.ARouterHelper
import com.ted.lib_base.helper.NotificationHelper
import com.ted.lib_base.map.MapManager
import com.ted.lib_base.service.InitService
import com.ted.lib_base.utils.L
import com.ted.lib_voice.impl.OnAsrResultListener
import com.ted.lib_voice.manager.VoiceManager
import org.json.JSONObject

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initApp()
    }

    //初始化App
    private fun initApp() {
        ARouterHelper.initHelper(this)
        NotificationHelper.initHelper(this)
        MapManager.initMap(this)

        startService(Intent(this, InitService::class.java))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this, InitService::class.java))
//        } else {
//            startService(Intent(this, InitService::class.java))
//        }
    }
}