package com.ted.lib_voice.words

import android.content.Context
import com.ted.lib_voice.R
import kotlin.random.Random


/**
 * FileName: WordsTools
 * Profile: 词条工具
 */
object WordsTools {

    //唤醒词条
    private lateinit var wakeupArray: Array<String>

    //无法应答
    private lateinit var noAnswerArray: Array<String>

    //暂不支持功能
    private lateinit var noSupportArray: Array<String>

    //退下
    private lateinit var quitArray: Array<String>

    //初始化工具
    fun initTools(mContext: Context) {
        mContext.apply {
            wakeupArray = resources.getStringArray(R.array.WakeUpListArray)
            noAnswerArray = resources.getStringArray(R.array.NoAnswerArray)
            noSupportArray = resources.getStringArray(R.array.NoSupportArray)
            quitArray = resources.getStringArray(R.array.QuitArray)
        }
    }

    //唤醒
    fun wakeupWords(): String {
        return randomArray(wakeupArray)
    }

    //无法回答
    fun noAnswerWords(): String {
        return randomArray(noAnswerArray)
    }

    //暂不支持
    fun noSupportWords(): String {
        return randomArray(noSupportArray)
    }

    //退出
    fun quitWords(): String {
        return randomArray(quitArray)
    }

    //随机数组
    private fun randomArray(array: Array<String>): String {
        return array[Random.nextInt(array.size)]
    }

    /**
     * 对给定的整数进行随机值
     */
    fun randomInt(maxSize: Int): Int {
        return Random.nextInt(maxSize)
    }
}