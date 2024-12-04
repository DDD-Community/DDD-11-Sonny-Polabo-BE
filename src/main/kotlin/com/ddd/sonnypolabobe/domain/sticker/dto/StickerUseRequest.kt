package com.ddd.sonnypolabobe.domain.sticker.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "스티커 사용 이력 저장")
data class StickerUseRequest(
    @field:Schema(description = "스티커 ID 리스트", example = "[\"STK_0001\",\"STK_0001\"]")
    val stickerIds: List<String>,
    @field:Schema(description = "게시글 ID", example = "adksjfldskjglaijg")
    @field:NotBlank(message = "게시글 ID는 필수입니다.")
    val boardId: String,
)
