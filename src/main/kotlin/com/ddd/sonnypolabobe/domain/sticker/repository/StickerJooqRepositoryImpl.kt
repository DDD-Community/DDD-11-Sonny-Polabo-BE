package com.ddd.sonnypolabobe.domain.sticker.repository

import com.ddd.sonnypolabobe.domain.sticker.dto.StickerUseRequest
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.global.util.UuidConverter
import com.ddd.sonnypolabobe.jooq.polabo.tables.references.STICKER_USE_HISTORY
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class StickerJooqRepositoryImpl(private val dslContext: DSLContext) : StickerJooqRepository{
    override fun insertAll(request: StickerUseRequest, user: UserDto.Companion.Res) {
        dslContext.batchInsert(
            request.stickerIds.map {
                STICKER_USE_HISTORY.newRecord().apply {
                    this.userId = user.id
                    this.stickerId = it
                    this.boardId = UuidConverter.uuidToByteArray(UuidConverter.stringToUUID(request.boardId))
                    this.createdAt = DateConverter.convertToKst(LocalDateTime.now())
                }
            }
        ).execute()

    }

    override fun readAllByUserId(userId: Long): Set<String> {
        return dslContext.select(STICKER_USE_HISTORY.STICKER_ID)
            .from(STICKER_USE_HISTORY)
            .where(STICKER_USE_HISTORY.USER_ID.eq(userId).and(
                STICKER_USE_HISTORY.CREATED_AT.gt(
                    DateConverter.convertToKst(LocalDateTime.now().minusDays(30))
                )
            ))
            .fetchInto(String::class.java)
            .toSet()
    }
}