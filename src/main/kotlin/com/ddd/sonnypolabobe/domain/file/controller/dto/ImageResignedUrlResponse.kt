package com.ddd.sonnypolabobe.domain.file.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ImageResignedUrlResponse(
    @field:Schema(description = "S3에 저장될 이미지 키", example = "imageKey")
    val imageKey : String,
    @field:Schema(description = "이미지 업로드를 위한 URL", example = "https://cloudfront.net/imageKey")
    val url : String
)
