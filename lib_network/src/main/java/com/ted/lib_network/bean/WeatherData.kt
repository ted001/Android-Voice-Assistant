package com.ted.lib_network.bean


/**
 * FileName: WeatherData
 * Profile: 天气数据
 */

data class WeatherData(
    val error_code: Int,
    val reason: String,
    val result: ResultY
)

data class ResultY(
    val city: String,
    val future: List<Future>,
    val realtime: Realtime
)

data class Future(
    val date: String,
    val direct: String,
    val temperature: String,
    val weather: String,
    val wid: Wid
)

data class Realtime(
    val aqi: String,
    val direct: String,
    val humidity: String,
    val info: String,
    val power: String,
    val temperature: String,
    val wid: String
)

data class Wid(
    val day: String,
    val night: String
)