package com.ted.lib_network.bean


/**
 * FileName: RobotData
 * Profile:
 */

data class RobotData(
    val intent: Intent,
    val results: List<ResultX>
)

data class Intent(
    val actionName: String,
    val code: Int,
    val intentName: String,
    val parameters: Parameters
)

data class ResultX(
    val groupType: Int,
    val resultType: String,
    val values: Values
)

data class Parameters(
    val nearby_place: String
)

data class Values(
    val text: String,
    val url: String
)