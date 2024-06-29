package com.ddd.sonnypolabobe.global.entity

import java.time.LocalDateTime
import java.util.UUID

interface BaseEntity {
    val id : UUID
    val yn : Boolean
    val createdAt : LocalDateTime
    val updatedAt : LocalDateTime

}