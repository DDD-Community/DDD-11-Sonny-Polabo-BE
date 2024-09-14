package com.ddd.sonnypolabobe.domain.board.sticker.repository

import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerCreateRequest
import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerGetResponse
import java.util.*

interface BoardStickerRepository {
    fun findByUserId(id: Long) : List<String>
    fun insertOne(req: StickerCreateRequest, userId: Long?)
    fun findByBoardId(stringToUUID: UUID): List<StickerGetResponse>?
}