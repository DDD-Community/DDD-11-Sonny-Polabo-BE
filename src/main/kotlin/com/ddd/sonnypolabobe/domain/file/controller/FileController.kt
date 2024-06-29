package com.ddd.sonnypolabobe.domain.file.controller

import com.ddd.sonnypolabobe.domain.file.controller.dto.ImageResignedUrlResponse
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.util.S3Util
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.bind.annotation.*
import java.io.File
import java.net.URL
import java.util.*


@RestController
@RequestMapping("/api/v1/file")
class FileController(
    private val s3Util: S3Util
) {

    @GetMapping("/pre-signed-url")
    @Operation(
        summary = "S3에 이미지를 저장하기 위한 PreSignedUrl 을 반환합니다.",
        description = """
        1. 해당 API를 호출하면, 응답 데이터에 url과 imageKey를 반환합니다.
        2. Url은 2분간 유효합니다.
        3. ImageKey는 S3에 저장될 파일의 이름입니다.
        4. PUT 메소드로 Url에 이미지 파일을 binary로 보내면, s3에 업로드됩니다.
        """
    )
    fun getPreSignedUrl(
        @RequestParam(value = "fileKey") @Schema(
            title = "유저의 보드 uuid",
            defaultValue = "01906259-94b2-74ef-8c13-554385c42943",
            example = "01906259-94b2-74ef-8c13-554385c42943"
        ) fileKey: String,
    ): ApplicationResponse<ImageResignedUrlResponse> { // fileName = env/fileKey/uuid
        var fileName = UUID.randomUUID().toString()
        fileName = (fileKey + File.separator) + fileName
        val url: URL = this.s3Util.getPreSignedUrl(fileName)
        val data = ImageResignedUrlResponse(fileName, url.toString())
        return ApplicationResponse.ok(data)
    }

    @Operation(
        summary = "S3 이미지 삭제", description = """
        S3에 저장된 이미지를 삭제합니다.
"""
    )
    @DeleteMapping("/uploaded-image")
    fun deleteImage(
        @RequestParam(value = "imageKey") imageKey: String
    ): ApplicationResponse<Nothing> {
        this.s3Util.deleteImage(imageKey)
        return ApplicationResponse.ok()
    }

    @Operation(
        summary = "S3 이미지 접근 주소"
    )
    @GetMapping("image-url")
    fun getImageUrl(@RequestParam(value = "imageKey") imageKey: String) =
        ApplicationResponse.ok(this.s3Util.getImgUrl(imageKey))
}