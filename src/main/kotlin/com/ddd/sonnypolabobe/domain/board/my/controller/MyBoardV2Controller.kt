package com.ddd.sonnypolabobe.domain.board.my.controller

import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.domain.board.my.service.MyBoardService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.entity.PageDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@Tag(name = "1.1.0")
@RestController
@RequestMapping("/api/v2/my/boards")
class MyBoardV2Controller(private val myBoardService : MyBoardService) {

    @Operation(summary = "내 보드 목록 조회 - v2", description = """
        내 보드 목록을 조회합니다.
        필터가 추가되었습니다.
    """)
    @GetMapping
    fun getMyBoards(
        @RequestParam(name = "page", defaultValue = "1") page : Int,
        @RequestParam size : Int,
        @RequestParam filter : Filter
    ) : ApplicationResponse<PageDto<MyBoardDto.Companion.PageListRes>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        return ApplicationResponse.ok(this.myBoardService.getMyBoards(user.id, page, size, filter))
    }

    companion object {
        enum class Filter {
            OWNER, PARTICIPANT
        }
    }

}