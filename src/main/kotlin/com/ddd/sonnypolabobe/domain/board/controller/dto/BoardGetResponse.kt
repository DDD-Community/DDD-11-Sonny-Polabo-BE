package com.ddd.sonnypolabobe.domain.board.controller.dto

import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerGetResponse
import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import io.swagger.v3.oas.annotations.media.Schema

data class BoardGetResponse(
    @Schema(description = "제목", example = "쏘니의 보드")
    val title: String,
    @Schema(description = "폴라로이드")
    val items: List<PolaroidGetResponse>,
    @Schema(description = "스티커 리스트")
    val stickers : List<StickerGetResponse>?
)