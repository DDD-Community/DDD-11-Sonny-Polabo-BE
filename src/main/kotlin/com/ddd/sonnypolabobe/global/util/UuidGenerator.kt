package com.ddd.sonnypolabobe.global.util

import com.github.f4b6a3.uuid.UuidCreator
import java.util.*

object UuidGenerator {
    fun create(): UUID {
        return UuidCreator.getTimeOrderedEpochPlus1()
    }
}