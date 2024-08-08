package com.ddd.sonnypolabobe.global.entity


data class PageDto<T>(
    val data: List<T>,
    val totalCount: Long,
    val totalPage: Int,
    val currentPage: Int
)