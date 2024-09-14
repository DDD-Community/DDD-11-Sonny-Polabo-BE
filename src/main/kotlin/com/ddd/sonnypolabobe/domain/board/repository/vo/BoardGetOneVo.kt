package com.ddd.sonnypolabobe.domain.board.repository.vo

import java.time.LocalDateTime
import java.util.UUID

data class BoardGetOneVo(
    val id: UUID?,
    val title: String?,
    val polaroidId : Long?,
    val imageKey : String?,
    val oneLineMessage: String?,
    val createdAt: LocalDateTime?,
    val userId : Long?,
    val nickName: String?,
    val options: String?
)
