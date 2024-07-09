package com.ddd.sonnypolabobe.global.util

data class HttpLog(
    val requestMethod : String,
    val requestURI : String,
    val responseStatus : Int,
    val elapsedTime : Double,
    val headers : Map<String, String>,
    val parameters : Map<String, String>,
    val requestBody : String,
    val responseBody : String
)
