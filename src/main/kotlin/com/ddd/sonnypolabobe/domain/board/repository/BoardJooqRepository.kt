package com.ddd.sonnypolabobe.domain.board.repository

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.domain.board.repository.vo.BoardGetOneVo
import com.ddd.sonnypolabobe.domain.user.dto.GenderType
import com.ddd.sonnypolabobe.jooq.polabo.tables.Board
import org.jooq.Record6
import org.jooq.Record7
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

interface BoardJooqRepository {
    fun insertOne(request: BoardCreateRequest): ByteArray?
    fun selectOneById(id: UUID) : List<BoardGetOneVo>
    fun selectTotalCount(): Long
    fun selectTodayTotalCount(): Long
    fun findById(id: UUID): MyBoardDto.Companion.GetOneRes?
    fun updateTitle(id: UUID, title: String)
    fun delete(id: UUID)
    fun findAllByUserId(userId: Long, page: Int, size: Int): List<MyBoardDto.Companion.PageListRes>
    fun selectTotalCountByUserId(userId: Long): Long
    fun findAllByParticipant(
        userId: Long,
        page: Int,
        size: Int
    ): List<MyBoardDto.Companion.PageListRes>

    fun selectTotalCountByParticipant(userId: Long): Long
    fun selectRecommendTitle(userBirth: LocalDate?, userGender: GenderType): List<String>
}