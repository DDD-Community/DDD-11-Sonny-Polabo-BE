package com.ddd.sonnypolabobe.domain.polaroid.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.UUID

data class PolaroidGetResponse(
    @Schema(description = "폴라로이드 ID", example = "1")
    val id: Long,
    @Schema(description = "이미지 주소", example = "https://image.com/image.jpg")
    val imageUrl: String,
    @Schema(description = "한 줄 문구", example = "한 줄 메시지입니다.")
    val oneLineMessage: String,
    @Schema(description = "작성자 ID", example = "userId")
    val userId: Long?,
    @Schema(description = "작성자 닉네임", example = "nickname")
    val nickname: String,
    @Schema(description = "작성자인지 여부", example = "true")
    val isMine: Boolean,
    @Schema(description = "생성일시", example = "2021-01-01T00:00:00")
    val createdAt: LocalDateTime?
)
