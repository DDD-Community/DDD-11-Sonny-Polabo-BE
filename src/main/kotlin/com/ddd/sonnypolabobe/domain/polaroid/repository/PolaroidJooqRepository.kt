package com.ddd.sonnypolabobe.domain.polaroid.repository

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.jooq.polabo.tables.records.PolaroidRecord

interface PolaroidJooqRepository {
    fun insertOne(boardId: ByteArray, request: PolaroidCreateRequest): Long
    fun selectOneById(id: Long): PolaroidRecord
}