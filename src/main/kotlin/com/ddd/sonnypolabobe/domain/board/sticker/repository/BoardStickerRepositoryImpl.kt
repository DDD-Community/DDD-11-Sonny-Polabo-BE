package com.ddd.sonnypolabobe.domain.board.sticker.repository

import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerCreateRequest
import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerGetResponse
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.global.util.UuidConverter
import com.ddd.sonnypolabobe.jooq.polabo.tables.BoardSticker
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class BoardStickerRepositoryImpl(
    private val dslContext: DSLContext
) : BoardStickerRepository {
    override fun findByUserId(userId: Long): List<String> {
        val jSticker = BoardSticker.BOARD_STICKER
        return this.dslContext.selectDistinct(jSticker.STICKER_ID)
            .from(jSticker)
            .where(jSticker.USER_ID.eq(userId))
            .orderBy(jSticker.CREATED_AT.desc())
            .fetchInto(String::class.java)
    }

    override fun insertOne(req: StickerCreateRequest, userId: Long?) {
        val jSticker = BoardSticker.BOARD_STICKER
        this.dslContext.insertInto(jSticker)
            .columns(
                jSticker.STICKER_ID,
                jSticker.BOARD_ID,
                jSticker.USER_ID,
                jSticker.X,
                jSticker.Y,
                jSticker.SCALE,
                jSticker.ROTATE,
                jSticker.CREATED_AT
            ).values(
                req.stickerId,
                UuidConverter.uuidToByteArray(UuidConverter.stringToUUID(req.boardId)),
                userId,
                req.x,
                req.y,
                req.scale,
                req.rotate,
                DateConverter.convertToKst(LocalDateTime.now())
            ).execute()
    }

    override fun findByBoardId(stringToUUID: UUID): List<StickerGetResponse>? {
        val jSticker = BoardSticker.BOARD_STICKER
        return this.dslContext.select(
            jSticker.ID,
            jSticker.X,
            jSticker.Y,
            jSticker.SCALE,
            jSticker.ROTATE
        )
            .from(jSticker)
            .where(jSticker.BOARD_ID.eq(UuidConverter.uuidToByteArray(stringToUUID)))
            .fetchInto(StickerGetResponse::class.java)
    }


}