package com.ddd.sonnypolabobe.domain.board.sticker.dto

data class StickerCreateRequest(
    val boardId : String,
    val stickerId: String,
    val x : String,
    val y : String,
    val scale: String,
    val rotate: String
)
