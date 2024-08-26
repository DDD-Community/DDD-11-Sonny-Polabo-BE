package com.ddd.sonnypolabobe.domain.board.repository

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.jooq.polabo.tables.Board
import org.jooq.Record6
import java.time.LocalDateTime
import java.util.*

interface BoardJooqRepository {
    fun insertOne(request: BoardCreateRequest): ByteArray?
    fun selectOneById(id: UUID) : Array<out Record6<String?, Long?, String?, String?, LocalDateTime?, Long?>>
    fun selectTotalCount(): Long
    fun selectTodayTotalCount(): Long
    fun findById(id: UUID): MyBoardDto.Companion.GetOneRes?
    fun updateTitle(id: UUID, title: String)
    fun delete(id: UUID)
    fun findAllByUserId(userId: Long, page: Int, size: Int): List<MyBoardDto.Companion.PageListRes>
    fun selectTotalCountByUserId(userId: Long): Long
}