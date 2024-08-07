package com.ddd.sonnypolabobe.domain.polaroid.service

import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.domain.polaroid.repository.PolaroidJooqRepository
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.global.util.S3Util
import com.ddd.sonnypolabobe.global.util.UuidConverter
import org.springframework.stereotype.Service

@Service
class PolaroidService(private val polaroidJooqRepository: PolaroidJooqRepository, private val s3Util: S3Util){
    fun create(boardId: String, request: PolaroidCreateRequest): Long {
        val boardIdUuid = UuidConverter.stringToUUID(boardId)
        val countByBoardId = this.polaroidJooqRepository.countByBoardId(UuidConverter.uuidToByteArray(boardIdUuid))
        if(countByBoardId > 50) throw ApplicationException(CustomErrorCode.POLAROID_COUNT_EXCEEDED)
        return this.polaroidJooqRepository.insertOne(UuidConverter.uuidToByteArray(boardIdUuid), request)
    }

    fun getById(id: Long): PolaroidGetResponse {
        return this.polaroidJooqRepository.selectOneById(id).let {
            PolaroidGetResponse(
                id = it.id!!,
                imageUrl = s3Util.getImgUrl(it.imageKey!!),
                oneLineMessage = it.oneLineMessage ?: "",
                userId = it.userId
            )
        }

    }
}