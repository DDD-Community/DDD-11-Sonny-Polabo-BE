package com.ddd.sonnypolabobe.domain.polaroid.dto

import com.ddd.sonnypolabobe.domain.polaroid.enumerate.ExtraOption
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PolaroidGetResponse(
    @field:Schema(description = "폴라로이드 ID", example = "1")
    val id: Long,
    @field:Schema(description = "이미지 주소", example = "https://image.com/image.jpg")
    val imageUrl: String,
    @field:Schema(description = "한 줄 문구", example = "한 줄 메시지입니다.")
    val oneLineMessage: String,
    @field:Schema(description = "작성자 ID", example = "userId")
    val userId: Long?,
    @field:Schema(description = "작성자 닉네임", example = "nickname")
    val nickname: String,
    @field:Schema(description = "작성자인지 여부", example = "true")
    val isMine: Boolean,
    @field:Schema(description = "생성일시", example = "2021-01-01T00:00:00")
    val createdAt: LocalDateTime?,
    @field:Schema(description = "옵션")
    val options: Map<ExtraOption, String>?
)
