package com.ddd.sonnypolabobe.domain.board.controller.dto

import com.ddd.sonnypolabobe.domain.polaroid.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.domain.polaroid.enumerate.ExtraOption
import io.swagger.v3.oas.annotations.media.Schema

data class BoardGetResponse(
    @field:Schema(description = "제목", example = "쏘니의 보드")
    val title: String,
    @field:Schema(description = "폴라로이드")
    val items: List<PolaroidGetResponse>,
    @field:Schema(description = "작성자 여부", example = "true")
    val isMine : Boolean,
    @field:Schema(description = "옵션", example = "{\"THEMA\":\"value3\"}")
    val options : Map<ExtraOption, String>?
)