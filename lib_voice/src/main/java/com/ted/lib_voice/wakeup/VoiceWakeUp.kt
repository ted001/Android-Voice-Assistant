package com.ted.lib_voice.wakeup

import android.content.Context
import android.nfc.Tag
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.ted.lib_voice.key.VoiceAppKey
import com.ted.lib_voice.manager.VoiceManager
import org.json.JSONObject
import kotlin.math.log


/**
 * FileName: VoiceWakeUp
 * Profile: 语音唤醒
 */
object VoiceWakeUp {

    //启动数据
    private lateinit var wakeUpJson: String

    //唤醒对象
    private lateinit var wp: EventManager

    //初始化唤醒
    fun initWakeUp(mContext: Context, listener: EventListener) {
        val map = HashMap<Any, Any>()
        map[SpeechConstant.APP_ID] = VoiceAppKey.VOICE_APP_ID
        map[SpeechConstant.APP_KEY] = VoiceAppKey.VOICE_APP_KEY
        map[SpeechConstant.SECRET] = VoiceAppKey.VOICE_APP_SECRET
        //本地文本路径
        map[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
        //是否获取音量
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        //转换成Json
        wakeUpJson = JSONObject(map as Map<Any, Any>).toString()

        //设置监听器
        wp = EventManagerFactory.create(mContext, "wp")
        wp.registerListener(listener)

        startWakeUp()
    }

    //启动唤醒
    fun startWakeUp() {
        Log.i("VoiceManager", wakeUpJson)
        wp.send(SpeechConstant.WAKEUP_START, wakeUpJson, null, 0, 0)
    }

    //停止唤醒
    fun stopWakeUp() {
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
    }
}