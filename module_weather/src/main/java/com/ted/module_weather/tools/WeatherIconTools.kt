package com.ted.module_weather.tools

import com.ted.module_weather.R


/**
 * FileName: WeatherIconTools
 * Profile:
 */
object WeatherIconTools {

    //天气天气状态获取图片
    fun getIcon(wid: String): Int {
        when (wid) {
            "00" -> return R.drawable.img_00
            "01" -> return R.drawable.img_01
            "02" -> return R.drawable.img_02
            "03" -> return R.drawable.img_03
            "04" -> return R.drawable.img_04
            "05" -> return R.drawable.img_05
            "06" -> return R.drawable.img_06
            "07" -> return R.drawable.img_07
            "08" -> return R.drawable.img_08
            "09" -> return R.drawable.img_09
            "10" -> return R.drawable.img_10
            "11" -> return R.drawable.img_11
            "12" -> return R.drawable.img_12
            "13" -> return R.drawable.img_13
            "14" -> return R.drawable.img_14
            "15" -> return R.drawable.img_15
            "16" -> return R.drawable.img_16
            "17" -> return R.drawable.img_17
            "18" -> return R.drawable.img_18
            "19" -> return R.drawable.img_19
            "20" -> return R.drawable.img_20
            "21" -> return R.drawable.img_21
            "22" -> return R.drawable.img_22
            "23" -> return R.drawable.img_23
            "24" -> return R.drawable.img_24
            "25" -> return R.drawable.img_25
            "26" -> return R.drawable.img_26
            "27" -> return R.drawable.img_27
            "28" -> return R.drawable.img_28
            "29" -> return R.drawable.img_29
            "30" -> return R.drawable.img_30
            "31" -> return R.drawable.img_31
            "53" -> return R.drawable.img_53
            else -> return R.drawable.img_00
        }
    }

    //根据天气获取图片
    fun getWeatherIcon(weather: String): Int {
        when (weather) {
            "晴" -> return R.drawable.img_00
            "多云" -> return R.drawable.img_01
            "阴" -> return R.drawable.img_02
            "阵雨" -> return R.drawable.img_03
            "雷阵雨" -> return R.drawable.img_04
            "雷阵雨伴有冰雹" -> return R.drawable.img_05
            "雨夹雪" -> return R.drawable.img_06
            "小雨" -> return R.drawable.img_07
            "中雨" -> return R.drawable.img_08
            "大雨" -> return R.drawable.img_09
            "暴雨" -> return R.drawable.img_10
            "大暴雨" -> return R.drawable.img_11
            "特大暴雨" -> return R.drawable.img_12
            "阵雪" -> return R.drawable.img_13
            "小雪" -> return R.drawable.img_14
            "中雪" -> return R.drawable.img_15
            "大雪" -> return R.drawable.img_16
            "暴雪" -> return R.drawable.img_17
            "雾" -> return R.drawable.img_18
            "冻雨" -> return R.drawable.img_19
            "沙尘暴" -> return R.drawable.img_20
            "小到中雨" -> return R.drawable.img_21
            "中到大雨" -> return R.drawable.img_22
            "大到暴雨" -> return R.drawable.img_23
            "暴雨到大暴雨" -> return R.drawable.img_24
            "大暴雨到特大暴雨" -> return R.drawable.img_25
            "小到中雪" -> return R.drawable.img_26
            "中到大雪" -> return R.drawable.img_27
            "大到暴雪" -> return R.drawable.img_28
            "浮尘" -> return R.drawable.img_29
            "扬沙" -> return R.drawable.img_30
            "强沙尘暴" -> return R.drawable.img_31
            "霾" -> return R.drawable.img_53
            else -> {
                //进行裁剪
                val list = weather.split("转")
                if (list.size >= 2) {
                    return getWeatherIcon(list[1])
                }
                //查不到类型
                return R.drawable.img_00
            }
        }
    }
}
