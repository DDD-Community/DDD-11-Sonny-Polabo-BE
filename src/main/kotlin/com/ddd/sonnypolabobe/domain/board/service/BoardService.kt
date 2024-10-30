package com.ddd.sonnypolabobe.domain.board.service

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.repository.BoardJooqRepository
import com.ddd.sonnypolabobe.domain.polaroid.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.domain.polaroid.enumerate.ExtraOption
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.global.security.AuthenticatedMember
import com.ddd.sonnypolabobe.global.util.S3Util
import com.ddd.sonnypolabobe.global.util.UuidConverter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BoardService(
    private val boardJooqRepository: BoardJooqRepository,
    private val s3Util: S3Util,
    @Value("\${limit.count}")
    private val limit: Int
) {
    @Transactional
    fun create(request: BoardCreateRequest): UUID =
        this.boardJooqRepository.insertOne(request)?.let { UuidConverter.byteArrayToUUID(it) }
            ?: throw ApplicationException(CustomErrorCode.BOARD_CREATED_FAILED)

    @Transactional(readOnly = true)
    fun getById(id: String, user: AuthenticatedMember?): List<BoardGetResponse> {
        return id.run {
            val queryResult =
                boardJooqRepository.selectOneById(UuidConverter.stringToUUID(this@run))

            if (queryResult.isEmpty()) throw ApplicationException(CustomErrorCode.BOARD_NOT_FOUND)

            val groupByTitle = queryResult.groupBy { it.title }

            groupByTitle.map { entry ->
                val title = entry.key
                val polaroids = entry.value.map {
                    PolaroidGetResponse(
                        id = it.polaroidId ?: 0L,
                        imageUrl = it.imageKey?.let { it1 -> s3Util.getImgUrl(it1) } ?: "",
                        oneLineMessage = it.oneLineMessage ?: "폴라보와의 추억 한 줄",
                        userId = it.userId ?: 0L,
                        nickname = it.nickname ?: "",
                        isMine = it.userId == user?.id?.toLong(),
                        createdAt = it.createdAt,
                        options = it.polaroidOptions?.let {
                            ObjectMapper().readValue(
                                it,
                                object : TypeReference<Map<ExtraOption, String>>() {})
                        }

                    )
                }.filter { it.id != 0L }.distinctBy { it.id }

                BoardGetResponse(
                    title = title ?: "",
                    items = polaroids,
                    isMine = queryResult.first().ownerId == user?.id?.toLong(),
                    options = queryResult.first().options?.let {
                        ObjectMapper().readValue(
                            it,
                            object : TypeReference<Map<ExtraOption, String>>() {})
                    }
                )
            }
        }
    }

    @Transactional(readOnly = true)
    fun getTotalCount(): Long = this.boardJooqRepository.selectTotalCount()

    @Transactional(readOnly = true)
    fun createAvailable(): Long {
        return this.boardJooqRepository.selectTodayTotalCount().let {
            if (it > limit) 0 else limit - it
        }
    }

    @Transactional(readOnly = true)
    fun recommendTitle(user: UserDto.Companion.Res): List<String> {
        val userBirthYear = user.birthDt
        val userGender = user.gender
        return this.boardJooqRepository.selectRecommendTitle(userBirth = userBirthYear, userGender)
    }

}