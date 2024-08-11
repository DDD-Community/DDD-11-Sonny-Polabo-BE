package com.ddd.sonnypolabobe.domain.user.repository

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.jooq.polabo.enums.UserGender
import com.ddd.sonnypolabobe.jooq.polabo.tables.User
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserJooqRepositoryImpl(private val dslContext: DSLContext) : UserJooqRepository{
    override fun insertOne(request: UserDto.Companion.CreateReq): Long {
        val jUser = User.USER
        return this.dslContext.insertInto(jUser,
            jUser.EMAIL,
            jUser.NICK_NAME,
            jUser.CREATED_AT,
            jUser.YN,
            jUser.BIRTH_DT,
            jUser.GENDER
            )
            .values(
                request.email,
                request.nickName,
                DateConverter.convertToKst(LocalDateTime.now()),
                1,
                request.birthDt,
                UserGender.valueOf(request.gender.name)
            )
            .returningResult(jUser.ID)
            .fetchOne(0, Long::class.java) ?: 0
    }

    override fun findById(id: Long): UserDto.Companion.Res? {
        val jUser = User.USER
        val record = this.dslContext.selectFrom(jUser)
            .where(jUser.ID.eq(id))
            .fetchOne()

        return record?.let {
            UserDto.Companion.Res(
                id = it.id!!,
                email = it.email!!,
                nickName = it.nickName!!,
                yn = it.yn?.toInt() == 1,
                createdAt = it.createdAt!!,
                updatedAt = it.updatedAt
            )
        }
    }

    override fun findByEmail(email: String): UserDto.Companion.Res? {
        val jUser = User.USER
        val record = this.dslContext.selectFrom(jUser)
            .where(jUser.EMAIL.eq(email))
            .fetchOne()

        return record?.let {
            UserDto.Companion.Res(
                id = it.id!!,
                email = it.email!!,
                nickName = it.nickName!!,
                yn = it.yn?.toInt() == 1,
                createdAt = it.createdAt!!,
                updatedAt = it.updatedAt
            )
        }
    }

    override fun updateProfile(request: UserDto.Companion.UpdateReq, userId: Long) {
        val jUser = User.USER
        this.dslContext.update(jUser)
            .set(jUser.NICK_NAME, request.nickName)
            .set(jUser.UPDATED_AT, DateConverter.convertToKst(LocalDateTime.now()))
            .where(jUser.ID.eq(userId))
            .execute()
    }
}