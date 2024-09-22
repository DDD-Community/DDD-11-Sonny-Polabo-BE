package com.ddd.sonnypolabobe.domain.board.my.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.UUID

class MyBoardDto {
    companion object {
        data class MBUpdateReq(
            @JsonProperty("title")
            @field:Schema(description = "제목", example = "쏘니의 보드")
            val title: String
        )

        data class PageListRes(
            @field:Schema(description = "보드 아이디", example = "01906259-94b2-74ef-8c13-554385c42943")
            val id: UUID,
            @field:Schema(description = "제목", example = "쏘니의 보드")
            val title: String,
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            @field:Schema(description = "생성일", example = "2021-07-01")
            val createdAt: LocalDateTime,
        )

        data class GetOneRes(
            @field:Schema(description = "보드 아이디", example = "01906259-94b2-74ef-8c13-554385c42943")
            val id: UUID,
            @field:Schema(description = "제목", example = "쏘니의 보드")
            val title: String,
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            @field:Schema(description = "생성일", example = "2021-07-01")
            val createdAt: LocalDateTime,
            @field:Schema(description = "작성자 아이디", example = "null", required = false)
            val userId: Long?
        )

        data class TotalCountRes(
            @field:Schema(description = "총 보드 생성 수", example = "100")
            val totalCreateCount: Long,
            @field:Schema(description = "총 참여자 수", example = "1000")
            val totalParticipantCount: Long
        )

    }
}