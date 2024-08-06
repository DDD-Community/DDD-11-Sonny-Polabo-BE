package com.ddd.sonnypolabobe.global.entity

data class PageDto(
    val page: Int,
    val size: Int,
    val total: Int,
    val content: List<Any>
)
