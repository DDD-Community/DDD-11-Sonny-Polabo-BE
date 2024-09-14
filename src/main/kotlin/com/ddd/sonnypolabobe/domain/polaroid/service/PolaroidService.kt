package com.ddd.sonnypolabobe.domain.polaroid.service

import com.ddd.sonnypolabobe.domain.board.repository.BoardJooqRepository
import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidCreateRequest
import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.domain.polaroid.enumerate.PolaroidOption
import com.ddd.sonnypolabobe.domain.polaroid.repository.PolaroidJooqRepository
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.global.util.S3Util
import com.ddd.sonnypolabobe.global.util.UuidConverter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PolaroidService(
    private val polaroidJooqRepository: PolaroidJooqRepository,
    private val boardJooqRepository: BoardJooqRepository,
    private val s3Util: S3Util
) {
    @Transactional
    fun create(boardId: String, request: PolaroidCreateRequest): Long {
        val boardIdUuid = UuidConverter.stringToUUID(boardId)
        val countByBoardId =
            this.polaroidJooqRepository.countByBoardId(UuidConverter.uuidToByteArray(boardIdUuid))
        if (countByBoardId > 30) throw ApplicationException(CustomErrorCode.POLAROID_COUNT_EXCEEDED)
        return this.polaroidJooqRepository.insertOne(
            UuidConverter.uuidToByteArray(boardIdUuid),
            request
        )
    }

    @Transactional(readOnly = true)
    fun getById(id: Long, user: UserDto.Companion.Res): PolaroidGetResponse {
        val data = this.polaroidJooqRepository.selectOneById(id)
        val boardWriter =
            this.boardJooqRepository.findById(UuidConverter.byteArrayToUUID(data.boardId!!))
                ?: throw ApplicationException(CustomErrorCode.BOARD_NOT_FOUND)
        return data.let {
            PolaroidGetResponse(
                id = it.id!!,
                imageUrl = s3Util.getImgUrl(it.imageKey!!),
                oneLineMessage = it.oneLineMessage ?: "",
                userId = it.userId,
                nickname = it.nickname ?: "",
                isMine = boardWriter.userId == user.id,
                createdAt = it.createdAt,
                options = it.options?.let{ ObjectMapper().readValue(it, object : TypeReference<Map<PolaroidOption, String>>() {})}
            )
        }
    }

    @Transactional
    fun create(boardId: String, request: PolaroidCreateRequest, userId: Long): Long {
        val boardIdUuid = UuidConverter.stringToUUID(boardId)
        val countByBoardId =
            this.polaroidJooqRepository.countByBoardId(UuidConverter.uuidToByteArray(boardIdUuid))
        if (countByBoardId > 30) throw ApplicationException(CustomErrorCode.POLAROID_COUNT_EXCEEDED)
        return this.polaroidJooqRepository.insertOne(
            UuidConverter.uuidToByteArray(boardIdUuid),
            request,
            userId
        )
    }

    @Transactional
    fun deleteById(id: Long, user: UserDto.Companion.Res) {
        val data = this.polaroidJooqRepository.selectOneById(id)
        val boardWriter =
            this.boardJooqRepository.findById(UuidConverter.byteArrayToUUID(data.boardId!!))
                ?: throw ApplicationException(CustomErrorCode.BOARD_NOT_FOUND)
        if (boardWriter.userId != user.id) throw ApplicationException(CustomErrorCode.POLAROID_DELETE_FAILED)
        this.polaroidJooqRepository.deleteById(id)
    }
}