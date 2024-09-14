package com.ddd.sonnypolabobe.domain.board.sticker.service

import com.ddd.sonnypolabobe.domain.board.sticker.dto.StickerCreateRequest
import com.ddd.sonnypolabobe.domain.board.sticker.repository.BoardStickerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StickerService(
    private val boardStickerRepository: BoardStickerRepository
) {
    @Transactional(readOnly = true)
    fun getByUserId(id: Long?): List<String>
    = id?.let { this.boardStickerRepository.findByUserId(id) } ?: emptyList()

    @Transactional
    fun create(req: StickerCreateRequest, userId: Long?) {
        this.boardStickerRepository.insertOne(req, userId)
    }
}