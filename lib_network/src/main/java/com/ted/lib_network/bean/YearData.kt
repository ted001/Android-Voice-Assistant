package com.ted.lib_network.bean


/**
 * FileName: Year
 * Profile:
 */
data class YearData(
    val career: List<String>,
    val date: String,
    val error_code: Int,
    val finance: List<String>,
    val future: String,
    val health: List<String>,
    val love: List<String>,
    val luckeyStone: String,
    val mima: Mima,
    val name: String,
    val resultcode: String,
    val year: Int
)

data class Mima(
    val info: String,
    val text: List<String>
)