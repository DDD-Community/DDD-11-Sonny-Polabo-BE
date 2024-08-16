package com.ddd.sonnypolabobe.domain.board.my.service

import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.domain.board.repository.BoardJooqRepository
import com.ddd.sonnypolabobe.global.entity.PageDto
import com.ddd.sonnypolabobe.global.util.UuidConverter
import org.springframework.stereotype.Service

@Service
class MyBoardService(private val boardJooqRepository: BoardJooqRepository) {
    fun updateMyBoard(id: String, request: MyBoardDto.Companion.MBUpdateReq, userId: Long) {
        val board = this.boardJooqRepository.findById(UuidConverter.stringToUUID(id))
            ?: throw IllegalArgumentException("해당 보드가 존재하지 않습니다.")
        if (board.userId != userId) {
            throw IllegalArgumentException("해당 보드에 대한 권한이 없습니다.")
        }
        this.boardJooqRepository.updateTitle(UuidConverter.stringToUUID(id), request.title)
    }

    fun deleteMyBoard(id: String, userId: Long) {
        val board = this.boardJooqRepository.findById(UuidConverter.stringToUUID(id))
            ?: throw IllegalArgumentException("해당 보드가 존재하지 않습니다.")
        if (board.userId != userId) {
            throw IllegalArgumentException("해당 보드에 대한 권한이 없습니다.")
        }
        this.boardJooqRepository.delete(UuidConverter.stringToUUID(id))
    }

    fun getMyBoards(userId: Long, page: Int, size: Int): PageDto<MyBoardDto.Companion.PageListRes> {
        val data = this.boardJooqRepository.findAllByUserId(userId, page, size)
        val totalCount = this.boardJooqRepository.selectTotalCountByUserId(userId)
        val totalPage = totalCount.toInt() / size + 1

        return PageDto(data, totalCount, totalPage, page, size)
    }
}