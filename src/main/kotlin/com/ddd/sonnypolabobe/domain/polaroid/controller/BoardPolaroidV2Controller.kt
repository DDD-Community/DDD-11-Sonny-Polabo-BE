package com.ddd.sonnypolabobe.domain.polaroid.controller

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.domain.polaroid.service.PolaroidService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.security.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "1.1.0")
@RestController
@RequestMapping("/api/v2/boards/{boardId}/polaroids")
class BoardPolaroidV2Controller(
    private val polaroidService: PolaroidService,
    private val jwtUtil: JwtUtil
) {

    @Operation(
        summary = "폴라로이드 생성", description = """
        폴라로이드를 생성합니다.
        
        작성자 닉네임이 추가되었습니다.
    """
    )
    @PostMapping
    fun create(
        @PathVariable boardId: String,
        @RequestBody @Valid request: PolaroidCreateRequest,
        @RequestHeader("Authorization") token: String?
    ): ApplicationResponse<Long> {
        val userInfo = token?.let { this.jwtUtil.getAuthenticatedMemberFromToken(it) }
        userInfo?.let {
            if(request.nickname == null) {
                request.nickname = userInfo.nickname
            }
        }
        val userId = userInfo?.id?.toLong()
            ?: return ApplicationResponse.ok(this.polaroidService.create(boardId, request))
        return ApplicationResponse.ok(this.polaroidService.create(boardId, request, userId))
    }


}