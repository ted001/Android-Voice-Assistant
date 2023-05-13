package com.ted.lib_voice.impl


interface OnNluResultListener {

    //=======================App 操作=======================

    //打开App
    fun openApp(appName: String)

    //卸载App
    fun unInstallApp(appName: String)

    //其他App
    fun otherApp(appName: String)

    //=======================通用设置=======================

    //返回
    fun back()

    //主页
    fun home()

    //音量+
    fun setVolumeUp()

    //音量-
    fun setVolumeDown()

    //取消
    fun quit()

    //=======================电话场景=======================

    //拨打联系人
    fun callPhoneForName(name: String)

    //拨打电话
    fun callPhoneForNumber(phone: String)

    //=======================笑话=======================

    //播放笑话
    fun playJoke()

    //笑话列表
    fun jokeList()

    //=======================星座=======================

    //星座时间
    fun conTellTime(name: String)

    //星座详情
    fun conTellInfo(name: String)

    //=======================机器人=======================

    //机器人
    fun aiRobot(text: String)

    //=======================天气=======================

    //查询天气
    fun queryWeather(city: String)

    //查询天气详情
    fun queryWeatherInfo(city: String)

    //=======================地图=======================

    //周边搜索
    fun nearByMap(poi: String)

    //规划 - 导航
    fun routeMap(address: String)

    //=======================其他=======================

    //听不懂你的话
    fun nluError()
}