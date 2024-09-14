package com.ddd.sonnypolabobe.domain.board.repository

import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardCreateRequest
import com.ddd.sonnypolabobe.domain.board.controller.dto.BoardGetResponse
import com.ddd.sonnypolabobe.domain.board.my.dto.MyBoardDto
import com.ddd.sonnypolabobe.domain.board.repository.vo.BoardGetOneVo
import com.ddd.sonnypolabobe.domain.user.dto.GenderType
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.global.util.UuidConverter
import com.ddd.sonnypolabobe.global.util.UuidGenerator
import com.ddd.sonnypolabobe.jooq.polabo.enums.UserGender
import com.ddd.sonnypolabobe.jooq.polabo.tables.Board
import com.ddd.sonnypolabobe.jooq.polabo.tables.BoardSticker
import com.ddd.sonnypolabobe.jooq.polabo.tables.Polaroid
import com.ddd.sonnypolabobe.jooq.polabo.tables.User
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Repository
class BoardJooqRepositoryImpl(
    private val dslContext: DSLContext
) : BoardJooqRepository {
    override fun insertOne(request: BoardCreateRequest): ByteArray? {
        val jBoard = Board.BOARD
        val id = UuidConverter.uuidToByteArray(UuidGenerator.create())
        val insertValue = jBoard.newRecord().apply {
            this.id = id
            this.title = request.title
            this.createdAt = DateConverter.convertToKst(LocalDateTime.now())
            this.yn = 1
            this.activeyn = 1
            this.userId = request.userId
        }
        val result = this.dslContext.insertInto(jBoard)
            .set(insertValue)
            .execute()

        return if (result == 1) id else null
    }

    override fun selectOneById(id: UUID): List<BoardGetOneVo> {
        val jBoard = Board.BOARD
        val jPolaroid = Polaroid.POLAROID

        return this.dslContext
            .select(
                jBoard.ID.convertFrom { it?.let{UuidConverter.byteArrayToUUID(it) } },
                jBoard.TITLE,
                jPolaroid.ID.`as`(BoardGetOneVo::polaroidId.name),
                jPolaroid.IMAGE_KEY,
                jPolaroid.ONE_LINE_MESSAGE,
                jPolaroid.CREATED_AT,
                jPolaroid.USER_ID,
                jPolaroid.NICKNAME,
                jPolaroid.OPTIONS
            )
            .from(jBoard)
            .leftJoin(jPolaroid).on(
                jBoard.ID.eq(jPolaroid.BOARD_ID).and(jPolaroid.YN.eq(1))
                    .and(jPolaroid.ACTIVEYN.eq(1))
            )
            .where(
                jBoard.ID.eq(UuidConverter.uuidToByteArray(id)).and(jBoard.YN.eq(1))
                    .and(jBoard.ACTIVEYN.eq(1))
            )
            .orderBy(jPolaroid.CREATED_AT.desc())
            .fetchInto(BoardGetOneVo::class.java)

    }

    override fun selectTotalCount(): Long {
        val jBoard = Board.BOARD
        return this.dslContext
            .selectCount()
            .from(jBoard)
            .fetchOne(0, Long::class.java) ?: 0
    }

    override fun selectTodayTotalCount(): Long {
        val jBoard = Board.BOARD
        return this.dslContext
            .selectCount()
            .from(jBoard)
            .where(
                jBoard.CREATED_AT.greaterOrEqual(DateConverter.convertToKst(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)))
                    .and(jBoard.CREATED_AT.lessThan(DateConverter.convertToKst(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)))
                    .and(jBoard.YN.eq(1))
                    .and(jBoard.ACTIVEYN.eq(1))
            ))
            .fetchOne(0, Long::class.java) ?: 0L
    }

    override fun findById(id: UUID): MyBoardDto.Companion.GetOneRes? {
        val jBoard = Board.BOARD
        return this.dslContext.selectFrom(jBoard)
            .where(jBoard.ID.eq(UuidConverter.uuidToByteArray(id)))
            .fetchOne()?.map {
                MyBoardDto.Companion.GetOneRes(
                    id = UuidConverter.byteArrayToUUID(it.get("id", ByteArray::class.java)!!),
                    title = it.get("title", String::class.java)!!,
                    createdAt = it.get("created_at", LocalDateTime::class.java)!!,
                    userId = it.get("user_id", Long::class.java),
                )
            }
    }

    override fun updateTitle(id: UUID, title: String) {
        val jBoard = Board.BOARD
        this.dslContext.update(jBoard)
            .set(jBoard.TITLE, title)
            .where(jBoard.ID.eq(UuidConverter.uuidToByteArray(id)))
            .execute()
    }

    override fun delete(id: UUID) {
        val jBoard = Board.BOARD
        this.dslContext.update(jBoard)
            .set(jBoard.YN, 0)
            .where(jBoard.ID.eq(UuidConverter.uuidToByteArray(id)))
            .execute()
    }

    override fun findAllByUserId(
        userId: Long,
        page: Int,
        size: Int
    ): List<MyBoardDto.Companion.PageListRes> {
        val jBoard = Board.BOARD
        val data = this.dslContext.select(
            jBoard.ID,
            jBoard.TITLE,
            jBoard.CREATED_AT
        )
            .from(jBoard)
            .where(jBoard.USER_ID.eq(userId).and(jBoard.YN.eq(1)).and(jBoard.ACTIVEYN.eq(1)))
            .orderBy(jBoard.CREATED_AT.desc())
            .limit(size)
            .offset(page)
            .fetch()

        return data.map {
            MyBoardDto.Companion.PageListRes(
                id = UuidConverter.byteArrayToUUID(it.get("id", ByteArray::class.java)!!),
                title = it.get("title", String::class.java)!!,
                createdAt = it.get("created_at", LocalDateTime::class.java)!!,
            )
        }
    }

    override fun selectTotalCountByUserId(userId: Long): Long {
        val jBoard = Board.BOARD
        return this.dslContext
            .selectCount()
            .from(jBoard)
            .where(jBoard.USER_ID.eq(userId).and(jBoard.YN.eq(1)).and(jBoard.ACTIVEYN.eq(1)))
            .fetchOne(0, Long::class.java) ?: 0L
    }

    override fun findAllByParticipant(
        userId: Long,
        page: Int,
        size: Int
    ): List<MyBoardDto.Companion.PageListRes> {
        val jBoard = Board.BOARD
        val jPolaroid = Polaroid.POLAROID
        val boardSubQuery =
            this.dslContext.select(jBoard.ID.`as`("id"))
            .from(jBoard)
            .where(jBoard.USER_ID.eq(userId).and(jBoard.YN.eq(1)).and(jBoard.ACTIVEYN.eq(1)))
            .asTable()

        val data = this.dslContext.select(
            jBoard.ID,
            jBoard.TITLE,
            jBoard.CREATED_AT
        )
            .from(jBoard)
            .innerJoin(jPolaroid).on(
                jBoard.ID.eq(jPolaroid.BOARD_ID).and(jPolaroid.USER_ID.eq(userId))
                    .and(jPolaroid.YN.eq(1)).and(jPolaroid.ACTIVEYN.eq(1))
            )
            .where(jBoard.ID.notIn(
                this.dslContext.select(boardSubQuery.field("id", ByteArray::class.java))
                    .from(boardSubQuery)
            ))
            .orderBy(jBoard.CREATED_AT.desc())
            .limit(size)
            .offset(page)

        return data
            .map {
            MyBoardDto.Companion.PageListRes(
                id = UuidConverter.byteArrayToUUID(it.get("id", ByteArray::class.java)!!),
                title = it.get("title", String::class.java)!!,
                createdAt = it.get("created_at", LocalDateTime::class.java)!!,
            )
        }.distinct()
    }

    override fun selectTotalCountByParticipant(userId: Long): Long {
        val jBoard = Board.BOARD
        val jPolaroid = Polaroid.POLAROID
        val boardSubQuery =
            this.dslContext.select(jBoard.ID.`as`("id"))
                .from(jBoard)
                .where(jBoard.USER_ID.eq(userId).and(jBoard.YN.eq(1)).and(jBoard.ACTIVEYN.eq(1)))
                .asTable()
        return this.dslContext
            .select(DSL.countDistinct(jBoard.ID))
            .from(jBoard)
            .innerJoin(jPolaroid).on(
                jBoard.ID.eq(jPolaroid.BOARD_ID).and(jPolaroid.USER_ID.eq(userId))
                    .and(jPolaroid.YN.eq(1)).and(jPolaroid.ACTIVEYN.eq(1))
            )
            .where(jBoard.ID.notIn(
                this.dslContext.select(boardSubQuery.field("id", ByteArray::class.java))
                    .from(boardSubQuery)
            ))
            .fetchOne(0, Long::class.java)
            ?: 0L
    }

    override fun selectRecommendTitle(userBirth: LocalDate?, userGender: GenderType): List<String> {
        val jBoard = Board.BOARD
        val jUser = User.USER
        val jPolaroid = Polaroid.POLAROID
        // 현재 날짜 기준으로 연령대를 계산하는 로직
        var userAgeGroup : String = "20-29세"
        if (userBirth != null) {
            val age = ChronoUnit.YEARS.between(userBirth, LocalDate.now())
            userAgeGroup = if (age < 15) {
                "15세 미만"
            } else if (age < 20) {
                "15-19세"
            } else if (age < 30) {
                "20-29세"
            } else if (age < 40) {
                "30-39세"
            } else if (age < 50) {
                "40-49세"
            } else if (age < 60) {
                "50-59세"
            } else {
                "60대 이상"
            }
        }

        // 기준일 (30일 전)
        val thirtyDaysAgo = LocalDateTime.now().minusDays(30)

        // 쿼리 작성
        return this.dslContext.select(jBoard.TITLE)
            .from(jBoard)
            .join(jUser)
            .on(jBoard.USER_ID.eq(jUser.ID))
            .leftJoin(
                this.dslContext.select(jPolaroid.BOARD_ID, count().`as`("polaroid_count"))
                    .from(jPolaroid)
                    .where(jPolaroid.YN.eq(1)
                        .and(jPolaroid.ACTIVEYN.eq(1)))
                    .groupBy(jPolaroid.BOARD_ID)
                    .asTable("sub_query")
            )
            .on(jBoard.ID.eq(field(name("sub_query", "board_id"), jBoard.ID.dataType)))
            .where(jBoard.YN.eq(1)
                .and(jBoard.ACTIVEYN.eq(1))
                .and(jBoard.CREATED_AT.greaterOrEqual(thirtyDaysAgo))
                .and(genderAndAgeGroupMatch(userGender, userAgeGroup))
        )
        .orderBy(field("sub_query.polaroid_count", Int::class.java).desc(), jBoard.CREATED_AT.desc())
            .limit(8)
            .fetchInto(String::class.java)
    }

    // 성별 및 연령대 일치 조건을 위한 메서드
    private fun  genderAndAgeGroupMatch( userGender : GenderType, userAgeGroup: String?): Condition {
        return User.USER.GENDER.eq(UserGender.valueOf(userGender.name))
            .or(User.USER.BIRTH_DT.isNotNull().and(ageGroupCondition(userAgeGroup)))
    }

    // 연령대 계산 로직에 따른 조건을 처리하는 메서드
    private fun ageGroupCondition(ageGroup: String?) : Condition{
        return `when`(User.USER.BIRTH_DT.ge(LocalDate.now().minusYears(15)), "15세 미만")
        .`when`(User.USER.BIRTH_DT.ge(LocalDate.now().minusYears(19)), "15-19세")
        .`when`(User.USER.BIRTH_DT.ge(LocalDate.now().minusYears(29)), "20-29세")
        .`when`(User.USER.BIRTH_DT.ge(LocalDate.now().minusYears(39)), "30-39세")
        .`when`(User.USER.BIRTH_DT.ge(LocalDate.now().minusYears(49)), "40-49세")
        .`when`(User.USER.BIRTH_DT.ge(LocalDate.now().minusYears(59)), "50-59세")
        .otherwise("60대 이상").eq(ageGroup);
    }

}