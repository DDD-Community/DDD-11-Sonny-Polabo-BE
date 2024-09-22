package com.ddd.sonnypolabobe.domain.user.withdraw.repository

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.util.DateConverter
import com.ddd.sonnypolabobe.jooq.polabo.enums.WithdrawType
import com.ddd.sonnypolabobe.jooq.polabo.tables.Withdraw
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class WithdrawJooqRepositoryImpl(private val dslContext: DSLContext): WithdrawJooqRepository {
    override fun insertOne(request: UserDto.Companion.WithdrawReq, userId: Long) {
        val jWithdraw = Withdraw.WITHDRAW

        this.dslContext.insertInto(jWithdraw,
            jWithdraw.USER_ID,
            jWithdraw.TYPE,
            jWithdraw.REASON,
            jWithdraw.CREATED_AT
        )
            .values(
                userId,
                WithdrawType.valueOf(request.type.name),
                request.reason,
                DateConverter.convertToKst(LocalDateTime.now())
            )
            .execute()
    }
}