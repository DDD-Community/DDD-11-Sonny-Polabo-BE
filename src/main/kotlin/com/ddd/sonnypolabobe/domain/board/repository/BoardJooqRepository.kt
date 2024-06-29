package com.ddd.sonnypolabobe.domain.board.repository

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import org.jooq.Record6
import java.time.LocalDateTime
import java.util.*

interface BoardJooqRepository {
    fun insertOne(request: BoardCreateRequest): ByteArray?
    fun selectOneById(id: UUID) : Array<out Record6<String?, Long?, String?, String?, LocalDateTime?, ByteArray?>>
    fun selectTotalCount(): Long
}