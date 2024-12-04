package com.ddd.sonnypolabobe.domain.sticker.service

import com.ddd.sonnypolabobe.domain.sticker.dto.StickerUseRequest
import com.ddd.sonnypolabobe.domain.sticker.repository.StickerJooqRepository
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StickerService(
    private val stickerJooqRepository: StickerJooqRepository
) {
    @Transactional
    fun use(request: StickerUseRequest, user: UserDto.Companion.Res) {
        stickerJooqRepository.insertAll(request, user)
    }

    @Transactional(readOnly = true)
    fun getRecentUse(user: UserDto.Companion.Res): Set<String> {
        return stickerJooqRepository.readAllByUserId(user.id)
    }
}