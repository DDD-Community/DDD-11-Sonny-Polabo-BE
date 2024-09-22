package com.ddd.sonnypolabobe.domain.board.controller.dto

import com.ddd.sonnypolabobe.domain.polaroid.dto.PolaroidGetResponse
import io.swagger.v3.oas.annotations.media.Schema

data class BoardGetResponse(
    @field:Schema(description = "제목", example = "쏘니의 보드")
    val title: String,
    @field:Schema(description = "폴라로이드")
    val items: List<PolaroidGetResponse>,
)