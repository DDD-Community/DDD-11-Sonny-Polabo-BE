package com.ddd.sonnypolabobe.domain.board.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.util.*

data class BoardCreateRequest(
    @Schema(description = "제목", example = "쏘니의 보드")
    @field:NotBlank
    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-])(?=.*[ㄱ-ㅎㅏ-ㅣ가-힣]).{1,20}$", message = "제목은 국문, 영문, 숫자, 특수문자, 띄어쓰기를 포함한 20자 이내여야 합니다.")
    val title: String,
    @Schema(description = "작성자 아이디", example = "null", required = false)
    val userId: UUID? = null
)
