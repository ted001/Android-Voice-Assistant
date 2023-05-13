package com.ted.lib_base.helper.`fun`

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.view.KeyEvent


/**
 * FileName: CommonSettingHelper
 * Profile: 通用设置帮助类
 */
object CommonSettingHelper {

    private lateinit var mContext: Context

    private lateinit var inst: Instrumentation

    fun initHelper(mContext: Context) {
        this.mContext = mContext

        inst = Instrumentation()
    }

    //返回
    fun back() {
        Thread(Runnable { inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK) }).start()
    }

    //主页
    fun home() {
        //Thread(Runnable { inst.sendKeyDownUpSync(KeyEvent.KEYCODE_HOME) }).start()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        mContext.startActivity(intent)
    }

    //音量+
    fun setVolumeUp() {
        Thread(Runnable { inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_UP) }).start()

    }

    //音量-
    fun setVolumeDown() {
        Thread(Runnable { inst.sendKeyDownUpSync(KeyEvent.KEYCODE_VOLUME_DOWN) }).start()
    }
}