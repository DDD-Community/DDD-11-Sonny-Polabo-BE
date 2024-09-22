package com.ddd.sonnypolabobe.global.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object DateConverter {

    fun convertToKst(date: LocalDateTime): LocalDateTime = date.plusHours(9)

    fun dateToLocalDateTime(date: Date): LocalDateTime =
        LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Seoul"))

}