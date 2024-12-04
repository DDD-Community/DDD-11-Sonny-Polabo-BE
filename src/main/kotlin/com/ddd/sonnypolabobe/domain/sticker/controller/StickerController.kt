package com.ddd.sonnypolabobe.domain.sticker.controller

import com.ddd.sonnypolabobe.domain.sticker.dto.StickerUseRequest
import com.ddd.sonnypolabobe.domain.sticker.service.StickerService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stickers")
class StickerController(
    private val stickerService: StickerService
) {

    @Tag(name = "1.5.0")
    @Operation(
        summary = "스티커 사용 이력 저장", description = "보드에 사용한 스티커 ID를 배열에 담아 모두 보내주세요."
    )
    @PostMapping("/use")
    fun useSticker(
        @RequestBody @Valid request: StickerUseRequest
    ) : ApplicationResponse<Nothing> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        stickerService.use(request, user)
        return ApplicationResponse.ok()
    }

    @Tag(name = "1.5.0")
    @Operation(
        summary = "최근 사용한 스티커 조회", description = "최근 30일 이내에 사용한 스티커를 조회합니다."
    )
    @GetMapping("/recent-use")
    fun getRecentUseSticker() : ApplicationResponse<Set<String>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        return ApplicationResponse.ok(stickerService.getRecentUse(user))
    }
}