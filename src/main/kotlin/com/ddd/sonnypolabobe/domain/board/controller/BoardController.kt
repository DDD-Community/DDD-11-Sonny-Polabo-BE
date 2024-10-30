package com.ddd.sonnypolabobe.domain.board.controller

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.service.BoardService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.security.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    private val boardService: BoardService,
    private val jwtUtil: JwtUtil
) {
    @Tag(name = "1.4.0")
    @Operation(
        summary = "보드 생성", description = """
        보드를 생성합니다.
        options 필드 추가했습니다. 폴라로이드 옵션과 동일하게 구성했습니다. 
        key : THEMA, value : 프론트에서 지정한 숫자 혹은 식별값
    """
    )
    @PostMapping
    fun create(@RequestBody request: BoardCreateRequest) : ApplicationResponse<UUID> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        request.userId = user.id
        return ApplicationResponse.ok(this.boardService.create(request))
    }

    @Tag(name = "1.4.0")
    @Operation(
        summary = "보드 조회", description = """
        보드를 조회합니다.
        DTO 필드 수정했습니다. 옵션이 추가되었습니다.
        
    """
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: String,
            @RequestHeader("Authorization") token: String?
    ) : ApplicationResponse<List<BoardGetResponse>> {
        val user = token?.let { this.jwtUtil.getAuthenticatedMemberFromToken(it) }
        return ApplicationResponse.ok(this.boardService.getById(id, user))
    }

    @Tag(name = "1.0.0")
    @Operation(
        summary = "보드 누적 생성 수 조회", description = """
        보드 누적 생성 수를 조회합니다.
    """
    )
    @GetMapping("/total-count")
    fun getTotalCount() = ApplicationResponse.ok(this.boardService.getTotalCount())

    @Tag(name = "1.0.0")
    @Operation(
        summary = "오늘 생성 가능한 보드 수 조회", description = """
        오늘 생성 가능한 보드 수를 조회합니다.
    """
    )
    @GetMapping("/create-available")
    fun createAvailable() = ApplicationResponse.ok(this.boardService.createAvailable())

    @Tag(name = "1.2.0")
    @Operation(
        summary = "보드명 주제 추천", description = """
        보드명 주제를 추천합니다.
    """
    )
    @GetMapping("/recommend-title")
    fun recommendTitle() : ApplicationResponse<List<String>> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
       return ApplicationResponse.ok(this.boardService.recommendTitle(user))
    }
}