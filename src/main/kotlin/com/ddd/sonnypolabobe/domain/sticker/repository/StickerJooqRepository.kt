package com.ddd.sonnypolabobe.domain.sticker.repository

import com.ddd.sonnypolabobe.domain.sticker.dto.StickerUseRequest
import com.ddd.sonnypolabobe.domain.user.dto.UserDto

interface StickerJooqRepository {
    fun insertAll(request: StickerUseRequest, user: UserDto.Companion.Res)
    fun readAllByUserId(userId: Long): Set<String>
}