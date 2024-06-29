package com.ddd.sonnypolabobe.domain.board.service

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.repository.BoardJooqRepository
import com.ddd.sonnypolabobe.domain.polaroid.controller.dto.PolaroidGetResponse
import com.ddd.sonnypolabobe.global.util.S3Util
import com.ddd.sonnypolabobe.global.util.UuidConverter
import org.springframework.stereotype.Service
import java.util.*

@Service
class BoardService(
    private val boardJooqRepository: BoardJooqRepository,
    private val s3Util: S3Util
) {
    fun create(request: BoardCreateRequest): UUID? {
        return this.boardJooqRepository.insertOne(request)?.let { UuidConverter.byteArrayToUUID(it) }
    }

    fun getById(id: String): List<BoardGetResponse> {
        return id.run {
            val result = boardJooqRepository.selectOneById(UuidConverter.stringToUUID(this@run))
            result.map {
                val polaroidId = it.value2()
                if (polaroidId != null) {
                    BoardGetResponse(
                        title = it.value1() ?: "폴라보의 보드",
                        items = listOf(
                            PolaroidGetResponse(
                                id = polaroidId,
                                imageUrl = it.value3()?.let { it1 -> s3Util.getImgUrl(it1) } ?: "",
                                oneLineMessage = it.value4() ?: "폴라보와의 추억 한 줄",
                                userId = it.value6()?.let { it1 -> UuidConverter.byteArrayToUUID(it1) },
                            )
                        )
                    )
                } else {
                    BoardGetResponse(
                        title = it.value1() ?: "폴라보의 보드",
                        items = emptyList()
                    )
                }
            }
        }
    }

    fun getTotalCount(): String {
        return this.boardJooqRepository.selectTotalCount().toString()
    }

}