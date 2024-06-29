package com.ddd.sonnypolabobe.domain.polaroid.repository

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.jooq.polabo.tables.Polaroid
import com.ddd.sonnypolabobe.jooq.polabo.tables.records.PolaroidRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PolaroidJooqRepositoryImpl(private val dslContext: DSLContext) : PolaroidJooqRepository {
    override fun insertOne(boardId: ByteArray, request: PolaroidCreateRequest): Long {
        val jPolaroid = Polaroid.POLAROID
        val insertValue = jPolaroid.newRecord().apply {
            this.boardId = boardId
            this.imageKey = request.imageKey
            this.oneLineMessage = request.oneLineMessage
            this.createdAt = LocalDateTime.now()
            this.yn = 1
            this.activeyn = 1
        }
        return this.dslContext.insertInto(jPolaroid)
            .set(insertValue)
            .returningResult(jPolaroid.ID)
            .fetchOne()?.value1() ?: 0
    }

    override fun selectOneById(id: Long): PolaroidRecord {
        val jPolaroid = Polaroid.POLAROID
        return this.dslContext
            .selectFrom(jPolaroid)
            .where(
                jPolaroid.ID.eq(id)
                    .and(jPolaroid.YN.eq(1))
                    .and(jPolaroid.ACTIVEYN.eq(1))
            )
            .fetchOne()?.original()
            ?: throw ApplicationException(CustomErrorCode.POLAROID_NOT_FOUND)
    }
}