package com.ddd.sonnypolabobe.domain.board.controller.dto

import com.ddd.sonnypolabobe.domain.polaroid.enumerate.ExtraOption
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class BoardCreateRequest(
    @field:Schema(description = "제목", example = "쏘니의 보드")
    @field:NotBlank
    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-])(?=.*[ㄱ-ㅎㅏ-ㅣ가-힣]).{1,20}$", message = "제목은 국문, 영문, 숫자, 특수문자, 띄어쓰기를 포함한 20자 이내여야 합니다.")
    val title: String,
    @field:Schema(description = "작성자 아이디", example = "null", required = false)
    var userId: Long? = null,
    @field:Schema(description = "보드 옵션 - key 값으로 THEMA를 주세요. value로는 프론트에서 지정한 숫자 혹은 식별값을 주세요.", example = "{\"THEMA\":\"value3\"}")
    val options : Map<ExtraOption, String>
)
