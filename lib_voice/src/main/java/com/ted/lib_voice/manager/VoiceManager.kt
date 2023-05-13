package com.ted.lib_voice.manager

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.ted.lib_voice.asr.VoiceAsr
import com.ted.lib_voice.impl.OnAsrResultListener
import com.ted.lib_voice.tts.VoiceTTS
import com.ted.lib_voice.wakeup.VoiceWakeUp
import org.json.JSONObject

object VoiceManager : EventListener {

    private var TAG = VoiceManager::class.java.simpleName

    //接口
    private lateinit var mOnAsrResultListener: OnAsrResultListener

    fun initManager(mContext: Context, mOnAsrResultListener: OnAsrResultListener) {
        this.mOnAsrResultListener = mOnAsrResultListener

        VoiceTTS.initTTS(mContext)
        VoiceAsr.initAsr(mContext, this)
        VoiceWakeUp.initWakeUp(mContext, this)
    }

    //------------------------TTS START------------------------
    //播放
    fun ttsStart(text: String) {
        Log.d(TAG, "开始TTS：$text")
        VoiceTTS.start(text, null)
    }

    //播放并且监听结果
    fun ttsStart(text: String, mOnTTSResultListener: VoiceTTS.OnTTSResultListener) {
        Log.d(TAG, "开始TTS-：$text")
        VoiceTTS.start(text, mOnTTSResultListener)
    }

    //暂停
    fun ttsPause() {
        VoiceTTS.pause()
    }

    //继续播放
    fun ttsResume() {
        VoiceTTS.resume()
    }

    //停止播放
    fun ttsStop() {
        VoiceTTS.stop()
    }

    //释放
    fun ttsRelease() {
        VoiceTTS.release()
    }

    //设置发音人
    fun setPeople(people: String) {
        VoiceTTS.setPeople(people)
    }

    //设置语速
    fun setVoiceSpeed(speed: String) {
        VoiceTTS.setVoiceSpeed(speed)
    }

    //设置音量
    fun setVoiceVolume(volume: String) {
        VoiceTTS.setVoiceVolume(volume)
    }

    //------------------------TTS END------------------------

    //------------------------WakeUp START------------------------

    //启动唤醒
    fun startWakeUp() {
        Log.d(TAG, "启动唤醒")
        VoiceWakeUp.startWakeUp()
    }

    //停止唤醒
    fun stopWakeUp() {
        VoiceWakeUp.stopWakeUp()
    }

    //------------------------WakeUp END------------------------


    //------------------------ASR START------------------------
    //开始识别
    fun startAsr() {
        VoiceAsr.startAsr()
    }

    //停止识别
    fun stopAsr() {
        VoiceAsr.stopAsr()
    }

    //取消识别
    fun cancelAsr() {
        VoiceAsr.cancelAsr()
    }

    //销毁
    fun releaseAsr() {
        VoiceAsr.releaseAsr(this)
    }

    //------------------------ASR END------------------------

    override fun onEvent(
        name: String?,
        params: String?,
        byte: ByteArray?,
        offset: Int,
        length: Int
    ) {
        //语音前置状态
        when (name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_READY -> mOnAsrResultListener.wakeUpReady()
            SpeechConstant.CALLBACK_EVENT_ASR_BEGIN -> mOnAsrResultListener.asrStartSpeak()
            SpeechConstant.CALLBACK_EVENT_ASR_END -> mOnAsrResultListener.asrStopSpeak()
        }
        //去除脏数据
        if (params == null) {
            return
        }
        val allJson = JSONObject(params)
        Log.i("Test", "allJson:$name:$allJson")
        when (name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> mOnAsrResultListener.wakeUpSuccess(
                allJson
            )
            SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR -> mOnAsrResultListener.voiceError("唤醒失败")
            SpeechConstant.CALLBACK_EVENT_ASR_FINISH -> mOnAsrResultListener.asrResult(allJson)
            SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL -> {
                mOnAsrResultListener.updateUserText(allJson.optString("best_result"))
                byte?.let {
                    val nlu = JSONObject(String(byte, offset, length))
                    mOnAsrResultListener.nluResult(nlu)
                }
            }
        }
    }

}