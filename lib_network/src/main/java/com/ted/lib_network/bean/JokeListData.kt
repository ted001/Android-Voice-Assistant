package com.ted.lib_network.bean


/**
 * FileName: JokeListData
 * Profile: 笑话列表
 */

data class JokeListData(
    val error_code: Int,
    val reason: String,
    val result: Results
)

data class Results(
    val `data`: List<Data>
)

data class Data(
    val content: String,
    val hashId: String,
    val unixtime: Int,
    val updatetime: String
)