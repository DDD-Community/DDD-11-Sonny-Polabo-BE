package com.ddd.sonnypolabobe.domain.board.my.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.UUID

class MyBoardDto {
    companion object {
        data class MBUpdateReq(
            @JsonProperty("title")
            val title: String
        )

        data class PageListRes(
            val id: UUID,
            val title: String,
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            val createdAt: LocalDateTime,
        )

        data class GetOneRes(
            val id: UUID,
            val title: String,
            @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
            val createdAt: LocalDateTime,
            val userId: Long?
        )

        data class TotalCountRes(
            val totalCreateCount: Long,
            val totalParticipantCount: Long
        )

    }
}