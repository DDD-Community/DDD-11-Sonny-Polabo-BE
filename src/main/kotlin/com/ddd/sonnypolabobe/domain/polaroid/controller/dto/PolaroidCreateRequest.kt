package com.ddd.sonnypolabobe.domain.polaroid.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "폴라로이드 생성 요청")
data class PolaroidCreateRequest(
    @Schema(description = "이미지 키", example = "imageKey")
    val imageKey : String,
    @Schema(description = "한 줄 문구", example = "한 줄 메시지입니다. 최대 20자까지 가능합니다.")
    @field:Size(max = 20)
    val oneLineMessage : String
)