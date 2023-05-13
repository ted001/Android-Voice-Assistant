package com.ted.lib_base.helper.`fun`

import android.content.Context
import com.ted.lib_base.R


/**
 * FileName: ConsTellHelper
 * Profile: 星座帮助类
 *
 * 根据传递进来的星座名称反馈时间
 */
object ConsTellHelper {

    private lateinit var mNameArray: Array<String>
    private lateinit var mTimeArray: Array<String>

    fun initHelper(mContext: Context) {
        mNameArray = mContext.resources.getStringArray(R.array.ConstellArray)
        mTimeArray = mContext.resources.getStringArray(R.array.ConstellTimeArray)
    }

    fun getConsTellTime(consTellName: String): String {
        mNameArray.forEachIndexed { index, s ->
            if (s == consTellName) {
                return mTimeArray[index]
            }
        }
        return "查询不到结果"
    }
}