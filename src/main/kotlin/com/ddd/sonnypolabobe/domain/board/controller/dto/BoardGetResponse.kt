package com.ddd.sonnypolabobe.domain.board.controller.dto

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import io.swagger.v3.oas.annotations.media.Schema

data class BoardGetResponse(
    @Schema(description = "제목", example = "쏘니의 보드")
    val title: String,
    @Schema(description = "작성자", example = "작성자입니다.")
    val items: List<PolaroidGetResponse>
)