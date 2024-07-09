package com.ddd.sonnypolabobe.global.util

import java.time.LocalDateTime

object DateConverter {

    fun convertToKst(date: LocalDateTime): LocalDateTime {
        return date.plusHours(9)
    }

}