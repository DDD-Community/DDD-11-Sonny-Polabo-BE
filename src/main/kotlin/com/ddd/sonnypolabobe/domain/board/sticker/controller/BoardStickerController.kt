package com.ddd.sonnypolabobe.domain.board.sticker.controller

import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerCreateRequest
import com.ddd.sonnypolabobe.domain.board.sticker.service.StickerService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.security.JwtUtil
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/boards/sticker")
class BoardStickerController(
    private val jwtUtil: JwtUtil,
    private val stickerService: StickerService
) {

    @Tag(name = "1.3.0")
    @GetMapping("/recent")
    fun getRecentList( @RequestHeader("Authorization") token: String?)
    : ApplicationResponse<List<String>> {
        val user = token?.let { this.jwtUtil.getAuthenticatedMemberFromToken(it) }
        return ApplicationResponse.ok(this.stickerService.getByUserId(user?.id?.toLong()))
    }

    @Tag(name = "1.3.0")
    @PostMapping
    fun createStickerToBoard(
        @RequestHeader("Authorization") token: String?,
        @RequestBody req : StickerCreateRequest
    ) : ApplicationResponse<Nothing> {
        val user = token?.let { this.jwtUtil.getAuthenticatedMemberFromToken(it) }
        this.stickerService.create(req, user?.id?.toLong())
        return ApplicationResponse.ok()
    }
}