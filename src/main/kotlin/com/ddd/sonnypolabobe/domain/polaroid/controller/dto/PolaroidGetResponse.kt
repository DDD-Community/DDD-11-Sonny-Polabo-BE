package com.ddd.sonnypolabobe.domain.polaroid.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

data class PolaroidGetResponse(
    @Schema(description = "폴라로이드 ID", example = "1")
    val id: Long,
    @Schema(description = "이미지 주소", example = "https://image.com/image.jpg")
    val imageUrl: String,
    @Schema(description = "한 줄 문구", example = "한 줄 메시지입니다.")
    val oneLineMessage: String,
    @Schema(description = "작성자 ID", example = "userId")
    val userId: UUID?,
)
