package com.ddd.sonnypolabobe.global.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

object DateConverter {

    fun convertToKst(date: LocalDateTime): LocalDateTime {
        return date.plusHours(9)
    }

    fun dateToLocalDateTime(date: Date) : LocalDateTime {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Seoul"))
    }

}