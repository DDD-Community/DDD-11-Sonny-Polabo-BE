package com.ddd.sonnypolabobe.domain.board.entity

import com.ddd.sonnypolabobe.global.entity.BaseEntity
import com.ddd.sonnypolabobe.global.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

class BoardEntity() : BaseEntity {
    override val id: UUID = UuidGenerator.create()
    var title: String = ""
    var userId : UUID? = null
    override var yn: Boolean = true
    override val createdAt: LocalDateTime = LocalDateTime.now()
    override var updatedAt: LocalDateTime = LocalDateTime.now()
}