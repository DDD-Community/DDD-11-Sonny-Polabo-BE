package com.ddd.sonnypolabobe.domain.polaroid.controller

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.domain.polaroid.service.PolaroidService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards/{boardId}/polaroids")
class BoardPolaroidController(private val polaroidService: PolaroidService) {

    @Operation(summary = "폴라로이드 생성", description = """
        폴라로이드를 생성합니다.
    """)
    @PostMapping
    fun create(@PathVariable boardId : String, @RequestBody request : PolaroidCreateRequest)
    = ApplicationResponse.ok(this.polaroidService.create(boardId, request))

}