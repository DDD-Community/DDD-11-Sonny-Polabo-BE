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

@Tag(name = "Board API", description = "보드 관련 API")
@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    private val boardService: BoardService,
    private val jwtUtil: JwtUtil
) {
    @Operation(
        summary = "보드 생성", description = """
        보드를 생성합니다.
        userId는 추후 회원가입 기능이 추가될 것을 대비한 것입니다. 지금은 null로 주세요.
        
        userId 데이터는 백에서 채울 것입니다.!
    """
    )
    @PostMapping
    fun create(@RequestBody request: BoardCreateRequest) : ApplicationResponse<UUID> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        request.userId = user.id
        return ApplicationResponse.ok(this.boardService.create(request))
    }

    @Tag(name = "1.3.0")
    @Operation(
        summary = "보드 조회", description = """
        보드를 조회합니다.
        DTO 필드 수정했습니다. 스티커 리스트 추가했습니다.
        
    """
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: String,
            @RequestHeader("Authorization") token: String?
    ) : ApplicationResponse<List<BoardGetResponse>> {
        val user = token?.let { this.jwtUtil.getAuthenticatedMemberFromToken(it) }
        return ApplicationResponse.ok(this.boardService.getById(id, user))
    }

    @Operation(
        summary = "보드 누적 생성 수 조회", description = """
        보드 누적 생성 수를 조회합니다.
    """
    )
    @GetMapping("/total-count")
    fun getTotalCount() = ApplicationResponse.ok(this.boardService.getTotalCount())

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