package com.ddd.sonnypolabobe.global.util

import com.amazonaws.HttpMethod
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.ddd.sonnypolabobe.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@Component
class S3Util(
    @Value("\${cloud.aws.credentials.access-key}")
    private val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val region: String,
    @Value("\${running.name}")
    private val runningName: String,
    @Value("\${aws.cloudfront.domain}")
    private val cloudfrontDomain: String,
) {

    fun awsCredentials(): BasicAWSCredentials = BasicAWSCredentials(accessKey, secretKey)
    fun amazonS3Client(): AmazonS3 = AmazonS3ClientBuilder.standard()
        .withCredentials(AWSStaticCredentialsProvider(awsCredentials()))
        .withRegion(region)
        .build()

    fun getPreSignedUrl(fileName: String): URL {
        val path: String = (runningName + File.separator) + fileName
        val request = GeneratePresignedUrlRequest(bucket, path)
        request.expiration = Date(Instant.now().plus(2, ChronoUnit.MINUTES).toEpochMilli())
        request.method = HttpMethod.PUT
        return amazonS3Client().generatePresignedUrl(request)
    }

    fun getImgUrl(fileName: String): String = "$cloudfrontDomain/$runningName/$fileName"

    fun deleteImage(fileUrl: String) {
        try {
            val fileKey = "$runningName/$fileUrl"
            amazonS3Client().deleteObject(DeleteObjectRequest(bucket, fileKey))
        } catch (e: Exception) {
            e.printStackTrace()
            logger().error("S3 이미지 삭제 실패 fileUrl: {}", fileUrl)
        }
    }
}
