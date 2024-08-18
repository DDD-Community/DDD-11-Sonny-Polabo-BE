package com.ddd.sonnypolabobe.global.entity


data class PageDto<T>(
    val data: List<T>,
    val totalCount: Long,
    var totalPage: Int,
    val currentPage: Int,
    val size : Int
) {

    constructor(
        data: List<T>,
        totalCount: Long,
        page: Int,
        size: Int
    ) : this(data, totalCount, 0, page, size
    )
    init {
        totalPage = totalCount.toInt() / size
    }
}