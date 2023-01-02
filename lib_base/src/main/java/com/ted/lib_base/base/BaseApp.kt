package com.ted.lib_base.base

import android.app.Application
import com.ted.lib_base.helper.ARouterHelper

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initApp()
    }

    //初始化App
    private fun initApp() {
        ARouterHelper.initHelper(this)
    }
}