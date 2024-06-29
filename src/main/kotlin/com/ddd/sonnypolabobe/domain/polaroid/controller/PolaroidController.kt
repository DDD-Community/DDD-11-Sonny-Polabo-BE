package com.ddd.sonnypolabobe.domain.polaroid.controller

import com.ddd.sonnypolabobe.domain.polaroid.service.PolaroidService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/polaroids")
class PolaroidController(private val polaroidService: PolaroidService) {

    @Operation(summary = "폴라로이드 조회", description = """
        폴라로이드를 조회합니다.
    """)
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = ApplicationResponse.ok(this.polaroidService.getById(id))
}