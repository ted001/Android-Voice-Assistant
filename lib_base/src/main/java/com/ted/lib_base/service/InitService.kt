package com.ted.lib_base.service

import android.app.IntentService
import android.content.Intent
import com.ted.lib_base.helper.SoundPoolHelper
import com.ted.lib_base.helper.`fun`.AppHelper
import com.ted.lib_base.helper.`fun`.CommonSettingHelper
import com.ted.lib_base.helper.`fun`.ConsTellHelper
import com.ted.lib_base.utils.AssetsUtils
import com.ted.lib_base.utils.L
import com.ted.lib_base.utils.SpUtils
import com.ted.lib_voice.words.WordsTools

class InitService : IntentService(InitService::class.simpleName) {

    override fun onCreate() {
        super.onCreate()
        L.i("初始化开始")

    }

    override fun onHandleIntent(intent: Intent?) {
        L.i("执行初始化操作")

        SpUtils.initUtils(this)
        WordsTools.initTools(this)
        SoundPoolHelper.init(this)

        AppHelper.initHelper(this)
        CommonSettingHelper.initHelper(this)
        ConsTellHelper.initHelper(this)
        AssetsUtils.initUtils(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        L.i("初始化完成")
    }

}