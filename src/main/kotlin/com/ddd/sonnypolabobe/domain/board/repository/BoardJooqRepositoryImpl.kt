package com.ddd.sonnypolabobe.domain.board.repository

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.global.util.UuidConverter
import com.ddd.sonnypolabobe.global.util.UuidGenerator
import com.ddd.sonnypolabobe.jooq.polabo.tables.Board
import com.ddd.sonnypolabobe.jooq.polabo.tables.Polaroid
import org.jooq.DSLContext
import org.jooq.Record6
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class BoardJooqRepositoryImpl(
    private val dslContext: DSLContext
) : BoardJooqRepository {
    override fun insertOne(request: BoardCreateRequest): ByteArray? {
        val jBoard = Board.BOARD
        val id = UuidConverter.uuidToByteArray(UuidGenerator.create())
        val insertValue = jBoard.newRecord().apply {
            this.id = id
            this.title = request.title
            this.createdAt = DateConverter.convertToKst(LocalDateTime.now())
            this.yn = 1
            this.activeyn = 1
            this.userId = request.userId
        }
        val result = this.dslContext.insertInto(jBoard)
            .set(insertValue)
            .execute()

        return if (result == 1) id else null
    }

    override fun selectOneById(id: UUID): Array<out Record6<String?, Long?, String?, String?, LocalDateTime?, Long?>> {
        val jBoard = Board.BOARD
        val jPolaroid = Polaroid.POLAROID
        return this.dslContext
            .select(
                jBoard.TITLE,
                jPolaroid.ID,
                jPolaroid.IMAGE_KEY,
                jPolaroid.ONE_LINE_MESSAGE,
                jPolaroid.CREATED_AT,
                jPolaroid.USER_ID
            )
            .from(jBoard)
            .leftJoin(jPolaroid).on(
                jBoard.ID.eq(jPolaroid.BOARD_ID).and(jPolaroid.YN.eq(1))
                    .and(jPolaroid.ACTIVEYN.eq(1))
            )
            .where(
                jBoard.ID.eq(UuidConverter.uuidToByteArray(id)).and(jBoard.YN.eq(1))
                    .and(jBoard.ACTIVEYN.eq(1))
            )
            .orderBy(jPolaroid.CREATED_AT.desc())
            .fetchArray()

    }

    override fun selectTotalCount(): Long {
        val jBoard = Board.BOARD
        return this.dslContext
            .selectCount()
            .from(jBoard)
            .where(jBoard.YN.eq(1).and(jBoard.ACTIVEYN.eq(1)))
            .fetchOne(0, Long::class.java) ?: 0
    }

    override fun selectTodayTotalCount(): Long {
        val jBoard = Board.BOARD
        return this.dslContext
            .selectCount()
            .from(jBoard)
            .where(
                jBoard.CREATED_AT.greaterOrEqual(DateConverter.convertToKst(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)))
                    .and(jBoard.CREATED_AT.lessThan(DateConverter.convertToKst(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)))
                    .and(jBoard.YN.eq(1))
                    .and(jBoard.ACTIVEYN.eq(1))
            ))
            .fetchOne(0, Long::class.java) ?: 0L
    }

    override fun findById(id: UUID): MyBoardDto.Companion.GetOneRes? {
        val jBoard = Board.BOARD
        return this.dslContext.selectFrom(jBoard)
            .where(jBoard.ID.eq(UuidConverter.uuidToByteArray(id)))
            .fetchOne()?.map {
                MyBoardDto.Companion.GetOneRes(
                    id = UuidConverter.byteArrayToUUID(it.get("id", ByteArray::class.java)!!),
                    title = it.get("title", String::class.java)!!,
                    createdAt = it.get("created_at", LocalDateTime::class.java)!!,
                    userId = it.get("user_id", Long::class.java),
                )
            }
    }

    override fun updateTitle(id: UUID, title: String) {
        val jBoard = Board.BOARD
        this.dslContext.update(jBoard)
            .set(jBoard.TITLE, title)
            .where(jBoard.ID.eq(UuidConverter.uuidToByteArray(id)))
            .execute()
    }

    override fun delete(id: UUID) {
        val jBoard = Board.BOARD
        this.dslContext.update(jBoard)
            .set(jBoard.YN, 0)
            .where(jBoard.ID.eq(UuidConverter.uuidToByteArray(id)))
            .execute()
    }

    override fun findAllByUserId(
        userId: Long,
        page: Int,
        size: Int
    ): List<MyBoardDto.Companion.PageListRes> {
        val jBoard = Board.BOARD
        val data = this.dslContext.select(
            jBoard.ID,
            jBoard.TITLE,
            jBoard.CREATED_AT
        )
            .from(jBoard)
            .where(jBoard.USER_ID.eq(userId).and(jBoard.YN.eq(1)).and(jBoard.ACTIVEYN.eq(1)))
            .orderBy(jBoard.CREATED_AT.desc())
            .limit(size)
            .offset(page * size)
            .fetch()

        return data.map {
            MyBoardDto.Companion.PageListRes(
                id = UuidConverter.byteArrayToUUID(it.get("id", ByteArray::class.java)!!),
                title = it.get("title", String::class.java)!!,
                createdAt = it.get("created_at", LocalDateTime::class.java)!!,
            )
        }
    }

    override fun selectTotalCountByUserId(userId: Long): Long {
        val jBoard = Board.BOARD
        return this.dslContext
            .selectCount()
            .from(jBoard)
            .where(jBoard.USER_ID.eq(userId).and(jBoard.YN.eq(1)).and(jBoard.ACTIVEYN.eq(1)))
            .fetchOne(0, Long::class.java) ?: 0L
    }
}