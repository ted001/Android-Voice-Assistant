package com.ted.lib_base.helper

import android.content.Context
import android.media.SoundPool


/**
 * FileName: SoundPoolHelper
 * Profile: 播放铃声
 */
object SoundPoolHelper {

    private lateinit var mContext: Context
    private lateinit var mSoundPool: SoundPool

    fun init(mContext: Context) {
        this.mContext = mContext

        mSoundPool = SoundPool.Builder().setMaxStreams(1).build()
    }

    /**
     * 播放
     */
    fun play(resId: Int) {
        val poolId = mSoundPool.load(mContext, resId, 1)
        mSoundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                /**
                 * 第一个参数：ID
                 * 第二个参数：左音量 0.0 - 1.0
                 * 第三个参数：右音量 0.0 - 1.0
                 * 第四个参数：优先级
                 * 第五个参数：重复数
                 * 第六个参数：速率 0.5 - 2.0
                 */
                mSoundPool.play(poolId, 1f, 1f, 1, 0, 1f)
            }
        }
    }
}