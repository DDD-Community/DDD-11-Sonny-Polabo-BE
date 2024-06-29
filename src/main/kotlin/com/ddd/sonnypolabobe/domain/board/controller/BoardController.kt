package com.ddd.sonnypolabobe.domain.board.controller

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.service.BoardService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Board API", description = "보드 관련 API")
@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    private val boardService: BoardService
) {
    @Operation(summary = "보드 생성", description = """
        보드를 생성합니다.
        userId는 추후 회원가입 기능이 추가될 것을 대비한 것입니다. 지금은 null로 주세요.
    """)
    @PostMapping
    fun create(@RequestBody request : BoardCreateRequest)
    = ApplicationResponse.ok(this.boardService.create(request))

    @Operation(summary = "보드 조회", description = """
        보드를 조회합니다.
    """)
    @GetMapping("/{id}")
    fun get(@PathVariable id : String)
    = ApplicationResponse.ok(this.boardService.getById(id))

    @Operation(summary = "보드 누적 생성 수 조회", description = """
        보드 누적 생성 수를 조회합니다.
    """)
    @GetMapping("/total-count")
    fun getTotalCount() = ApplicationResponse.ok(this.boardService.getTotalCount())
}