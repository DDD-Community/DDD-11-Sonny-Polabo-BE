package com.ddd.sonnypolabobe.domain.polaroid.controller

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.domain.polaroid.service.PolaroidService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/polaroids")
class PolaroidController(private val polaroidService: PolaroidService) {

    @Tag(name = "1.2.0")
    @Operation(
        summary = "폴라로이드 조회", description = """
        폴라로이드를 조회합니다.
        DTO 에 작성자 여부를 추가했습니다.
    """
    )
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ApplicationResponse<PolaroidGetResponse> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        return ApplicationResponse.ok(this.polaroidService.getById(id, user))
    }

    @Tag(name = "1.2.0")
    @Operation(
        summary = "폴라로이드 삭제", description = """
        폴라로이드를 삭제합니다.
    """
    )
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ApplicationResponse<Nothing> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.polaroidService.deleteById(id, user)
        return ApplicationResponse.ok()
    }
}