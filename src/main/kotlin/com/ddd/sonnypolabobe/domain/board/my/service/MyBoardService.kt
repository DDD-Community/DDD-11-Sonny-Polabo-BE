package com.ddd.sonnypolabobe.domain.board.my.service

import com.ddd.sonnypolabobe.domain.board.my.controller.MyBoardV2Controller
import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.domain.board.repository.BoardJooqRepository
import com.ddd.sonnypolabobe.domain.polaroid.repository.PolaroidJooqRepository
import com.ddd.sonnypolabobe.domain.polaroid.service.PolaroidService
import com.ddd.sonnypolabobe.global.entity.PageDto
import com.ddd.sonnypolabobe.global.util.UuidConverter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MyBoardService(
    private val boardJooqRepository: BoardJooqRepository,
) {
    @Transactional
    fun updateMyBoard(id: String, request: MyBoardDto.Companion.MBUpdateReq, userId: Long) {
        val board = this.boardJooqRepository.findById(UuidConverter.stringToUUID(id))
            ?: throw IllegalArgumentException("해당 보드가 존재하지 않습니다.")
        if (board.userId != userId) {
            throw IllegalArgumentException("해당 보드에 대한 권한이 없습니다.")
        }
        this.boardJooqRepository.updateTitle(UuidConverter.stringToUUID(id), request.title)
    }

    @Transactional
    fun deleteMyBoard(id: String, userId: Long) {
        val board = this.boardJooqRepository.findById(UuidConverter.stringToUUID(id))
            ?: throw IllegalArgumentException("해당 보드가 존재하지 않습니다.")
        if (board.userId != userId) {
            throw IllegalArgumentException("해당 보드에 대한 권한이 없습니다.")
        }
        this.boardJooqRepository.delete(UuidConverter.stringToUUID(id))
    }

    @Transactional(readOnly = true)
    fun getMyBoards(userId: Long, page: Int, size: Int): PageDto<MyBoardDto.Companion.PageListRes> {
        val data = this.boardJooqRepository.findAllByUserId(userId, page - 1, size)
        val totalCount = this.boardJooqRepository.selectTotalCountByUserId(userId)

        return PageDto(data, totalCount, page, size)
    }

    @Transactional(readOnly = true)
    fun getMyBoards(
        userId: Long,
        page: Int,
        size: Int,
        filter: MyBoardV2Controller.Companion.Filter
    ): PageDto<MyBoardDto.Companion.PageListRes> {
        when (filter) {
            MyBoardV2Controller.Companion.Filter.OWNER -> {
                val data = this.boardJooqRepository.findAllByUserId(userId, page - 1, size)
                val totalCount = this.boardJooqRepository.selectTotalCountByUserId(userId)
                return PageDto(data, totalCount, page, data.size)
            }

            MyBoardV2Controller.Companion.Filter.PARTICIPANT -> {
                val data = this.boardJooqRepository.findAllByParticipant(userId, page - 1, size)
                val totalCount = this.boardJooqRepository.selectTotalCountByParticipant(userId)
                return PageDto(data, totalCount, page, data.size)
            }
        }
    }

    @Transactional(readOnly = true)
    fun getTotalCount(userId: Long): MyBoardDto.Companion.TotalCountRes {
        return MyBoardDto.Companion.TotalCountRes(
            totalCreateCount = this.boardJooqRepository.selectTotalCountByUserId(userId),
            totalParticipantCount = this.boardJooqRepository.selectTotalCountByParticipant(userId)
        )
    }
}