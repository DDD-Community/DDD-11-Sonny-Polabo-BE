package com.ddd.sonnypolabobe.domain.polaroid.controller

import com.ddd.sonnypolabobe.domain.polaroid.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.domain.polaroid.service.PolaroidService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards/{boardId}/polaroids")
class BoardPolaroidController(private val polaroidService: PolaroidService) {

    @Tag(name = "1.1.0")
    @Operation(
        summary = "폴라로이드 생성", description = """
        폴라로이드를 생성합니다.
        
        v2를 사용해주세요. 혹시 v1을 사용하는 곳이 남아있다면, 보드 내 폴라로이드 최대 30개 생성으로 제한 됩니다.
    """
    )
    @PostMapping
    fun create(@PathVariable boardId: String, @RequestBody @Valid request: PolaroidCreateRequest) =
        ApplicationResponse.ok(this.polaroidService.create(boardId, request))

}