package com.ddd.sonnypolabobe.domain.board.service

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.repository.BoardJooqRepository
import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.global.util.S3Util
import com.ddd.sonnypolabobe.global.util.UuidConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class BoardService(
    private val boardJooqRepository: BoardJooqRepository,
    private val s3Util: S3Util,
    @Value("\${limit.count}")
    private val limit: Int
) {
    fun create(request: BoardCreateRequest): UUID =
        this.boardJooqRepository.insertOne(request)?.let { UuidConverter.byteArrayToUUID(it) }
            ?: throw ApplicationException(CustomErrorCode.BOARD_CREATED_FAILED)

    fun getById(id: String): List<BoardGetResponse> {
        return id.run {
            val queryResult =
                boardJooqRepository.selectOneById(UuidConverter.stringToUUID(this@run))
            val groupByTitle = queryResult.groupBy { it.value1() }
            groupByTitle.map { entry ->
                val title = entry.key
                val polaroids = entry.value.map {
                    PolaroidGetResponse(
                        id = it.value2() ?: 0L,
                        imageUrl = it.value3()?.let { it1 -> s3Util.getImgUrl(it1) } ?: "",
                        oneLineMessage = it.value4() ?: "폴라보와의 추억 한 줄",
                        userId = it.value6() ?: 0L,
                        nickname = it.value7() ?: ""
                    )
                }.filter { it.id != 0L }
                BoardGetResponse(title = title ?: "", items = polaroids)
            }
        }
    }

    fun getTotalCount(): Long = this.boardJooqRepository.selectTotalCount()
    fun createAvailable(): Long {
        return this.boardJooqRepository.selectTodayTotalCount().let {
            if (it > limit) 0 else limit - it
        }
    }

}