package com.ddd.sonnypolabobe.domain.polaroid.repository

import com.ddd.sonnypolabobe.domain.polaroid.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.jooq.polabo.tables.Polaroid
import com.ddd.sonnypolabobe.jooq.polabo.tables.records.PolaroidRecord
import com.fasterxml.jackson.databind.ObjectMapper
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
            this.createdAt = DateConverter.convertToKst(LocalDateTime.now())
            this.yn = 1
            this.activeyn = 1
            this.nickname = request.nickname
            this.options = ObjectMapper().writeValueAsString(request.options)
        }
        return this.dslContext.insertInto(jPolaroid)
            .set(insertValue)
            .returningResult(jPolaroid.ID)
            .fetchOne()?.value1() ?: 0
    }

    override fun insertOne(boardId: ByteArray, request: PolaroidCreateRequest, userId: Long): Long {
        val jPolaroid = Polaroid.POLAROID
        val insertValue = jPolaroid.newRecord().apply {
            this.boardId = boardId
            this.imageKey = request.imageKey
            this.oneLineMessage = request.oneLineMessage
            this.createdAt = DateConverter.convertToKst(LocalDateTime.now())
            this.userId = userId
            this.yn = 1
            this.activeyn = 1
            this.nickname = request.nickname
            this.options = ObjectMapper().writeValueAsString(request.options)
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

    override fun countByBoardId(uuidToByteArray: ByteArray): Int {
        val jPolaroid = Polaroid.POLAROID
        return this.dslContext
            .selectCount()
            .from(jPolaroid)
            .where(
                jPolaroid.BOARD_ID.eq(uuidToByteArray)
                    .and(jPolaroid.YN.eq(1))
                    .and(jPolaroid.ACTIVEYN.eq(1))
            )
            .fetchOne(0, Int::class.java) ?: 0
    }

    override fun deleteById(id: Long) {
        val jPolaroid = Polaroid.POLAROID
        this.dslContext.update(jPolaroid)
            .set(jPolaroid.YN, 0)
            .set(jPolaroid.ACTIVEYN, 0)
            .where(jPolaroid.ID.eq(id))
            .execute()
    }
}