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

@RestController
@RequestMapping("/api/v1/my/boards")
class MyBoardController(private val myBoardService: MyBoardService) {

    @Tag(name = "1.0.0")
    @Operation(
        summary = "내 보드 목록 조회", description = """
        내 보드 목록을 조회합니다.
    """
    )
    @GetMapping
    fun getMyBoards(
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam size: Int
    ): ApplicationResponse<PageDto<MyBoardDto.Companion.PageListRes>> {
        val user =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        return ApplicationResponse.ok(this.myBoardService.getMyBoards(user.id, page, size))
    }


    @Tag(name = "1.0.0")
    @Operation(
        summary = "내 보드 이름 수정",
        description = """
            내 보드 이름을 수정합니다.
        """
    )
    @PutMapping("/{id}")
    fun updateMyBoard(
        @PathVariable id: String,
        @RequestBody request: MyBoardDto.Companion.MBUpdateReq
    ): ApplicationResponse<Nothing> {
        val userId =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.myBoardService.updateMyBoard(id, request, userId.id)
        return ApplicationResponse.ok()
    }

    @Tag(name = "1.1.0")
    @Operation(
        summary = "내 보드 삭제",
        description = """
            내 보드를 삭제합니다.
        """
    )
    @DeleteMapping("/{id}")
    fun deleteMyBoard(
        @PathVariable id: String
    ): ApplicationResponse<Nothing> {
        val userId =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.myBoardService.deleteMyBoard(id, userId.id)
        return ApplicationResponse.ok()
    }

    @Tag(name = "1.1.0")
    @Operation(
        summary = "내가 만든 보드 총 개수",
        description = """
            내가 만든 보드의 총 개수를 조회합니다.
        """
    )
    @GetMapping("/total-count")
    fun getTotalCount(): ApplicationResponse<MyBoardDto.Companion.TotalCountRes> {
        val userId =
            SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        return ApplicationResponse.ok(this.myBoardService.getTotalCount(userId.id))
    }
}