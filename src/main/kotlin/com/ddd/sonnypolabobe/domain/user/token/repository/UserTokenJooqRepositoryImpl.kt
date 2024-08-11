package com.ddd.sonnypolabobe.domain.user.token.repository

import com.ddd.sonnypolabobe.domain.user.token.dto.UserTokenDto
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.jooq.polabo.tables.UserToken
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserTokenJooqRepositoryImpl(private val dslContext: DSLContext) : UserTokenJooqRepository {
    override fun insertOne(userToken: UserTokenDto) {
        val jUserToken = UserToken.USER_TOKEN
        val insertValue = jUserToken.newRecord().apply {
            this.userId = userToken.userId
            this.accessToken = userToken.accessToken
            this.accessExpiredAt = userToken.expiredAt
            this.createdAt = DateConverter.convertToKst(LocalDateTime.now())
            this.updatedAt = DateConverter.convertToKst(LocalDateTime.now())
            this.refreshToken = userToken.refreshToken
        }
        this.dslContext.insertInto(jUserToken,
            jUserToken.USER_ID,
            jUserToken.ACCESS_TOKEN,
            jUserToken.ACCESS_EXPIRED_AT,
            jUserToken.CREATED_AT,
            jUserToken.UPDATED_AT,
            jUserToken.REFRESH_TOKEN
        )
            .values(
                insertValue.userId,
                insertValue.accessToken,
                insertValue.accessExpiredAt,
                insertValue.createdAt,
                insertValue.updatedAt,
                insertValue.refreshToken
            )
            .onDuplicateKeyUpdate()
            .set(jUserToken.USER_ID, insertValue.userId)
            .execute()
    }

    override fun findByRefreshToken(token: String): UserTokenDto? {
        val jUserToken = UserToken.USER_TOKEN
        return this.dslContext.selectFrom(jUserToken)
            .where(jUserToken.REFRESH_TOKEN.eq(token))
            .fetchOne()?.map {
                UserTokenDto(
                    userId = it.get(jUserToken.USER_ID)!!,
                    accessToken = it.get(jUserToken.ACCESS_TOKEN)!!,
                    expiredAt = it.get(jUserToken.ACCESS_EXPIRED_AT)!!,
                    refreshToken = it.get(jUserToken.REFRESH_TOKEN)!!
                )
            }
    }

    override fun updateByUserId(userToken: UserTokenDto) {
        val jUserToken = UserToken.USER_TOKEN
        this.dslContext.update(jUserToken)
            .set(jUserToken.ACCESS_TOKEN, userToken.accessToken)
            .set(jUserToken.ACCESS_EXPIRED_AT, userToken.expiredAt)
            .set(jUserToken.UPDATED_AT, DateConverter.convertToKst(LocalDateTime.now()))
            .where(jUserToken.USER_ID.eq(userToken.userId))
            .execute()
    }
}