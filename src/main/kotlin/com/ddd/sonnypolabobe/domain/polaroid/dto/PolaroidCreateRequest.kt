package com.ddd.sonnypolabobe.domain.polaroid.dto

import com.ddd.sonnypolabobe.domain.polaroid.enumerate.PolaroidOption
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "폴라로이드 생성 요청 v2")
data class PolaroidCreateRequest(
    @field:Schema(description = "이미지 키", example = "imageKey")
    val imageKey : String,
    @field:Schema(description = "한 줄 문구", example = "한 줄 메시지입니다. 최대 20자까지 가능합니다.")
    @field:Size(max = 20)
    val oneLineMessage : String,
    @field:Schema(description = "작성자 닉네임이 null 이면서 회원가입된 유저라면, 유저의 닉네임을 자동할당합니다.", example = "작성자 닉네임")
    var nickname : String?,
    @field:Schema(description = "폴라로이드 옵션", example = "{\"FONT\":\"value1\",\"FILTER\":\"value2\",\"THEMA\":\"value3\"}")
    val options : Map<PolaroidOption, String>
)