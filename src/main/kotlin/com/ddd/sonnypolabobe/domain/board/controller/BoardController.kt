package com.ddd.sonnypolabobe.domain.board.controller

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.service.BoardService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.logger
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Board API", description = "보드 관련 API")
@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    private val boardService: BoardService
) {
    @Operation(
        summary = "보드 생성", description = """
        보드를 생성합니다.
        userId는 추후 회원가입 기능이 추가될 것을 대비한 것입니다. 지금은 null로 주세요.
        
        userId 데이터는 백에서 채울 것입니다.!
    """
    )
    @PostMapping
    fun create(@RequestBody request: BoardCreateRequest)
            : ApplicationResponse<UUID> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        request.userId = user.id
        return ApplicationResponse.ok(this.boardService.create(request))
    }

    @Tag(name = "1.1.0")
    @Operation(
        summary = "보드 조회", description = """
        보드를 조회합니다.
        DTO 필드 수정했습니다. 폴라로이드에 닉네임 필드 추가
    """
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = ApplicationResponse.ok(this.boardService.getById(id))

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
}