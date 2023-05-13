package com.ted.lib_network.http


/**
 * FileName: HttpUrl
 * Profile: 网络地址
 */
object HttpUrl {

    //天气
    const val WEATHER_BASE_URL = "http://apis.juhe.cn/"

    //天气查询
    const val WEATHER_ACTION = "simpleWeather/query"

    //天气
    const val JOKE_BASE_URL = "http://v.juhe.cn/"

    //随机笑话
    const val JOKE_ONE_ACTION = " joke/randJoke.php"

    //笑话列表
    const val JOKE_LIST_ACTION = "joke/content/text.php"

    //星座
    const val CONS_TELL_BASE_URL = "http://web.juhe.cn:8080/"

    //星座详情
    const val CONS_TELL_ACTION = "constellation/getAll"

    //机器人
    const val ROBOT_BASE_URL = "http://openapi.tuling123.com/"

    //机器人对话
    const val ROBOT_ACTION = "openapi/api/v2"


}