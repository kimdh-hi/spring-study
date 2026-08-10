package com.study.presignedurl.infra

import io.awspring.cloud.s3.S3Template
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload
import software.amazon.awssdk.services.s3.model.CompletedPart
import software.amazon.awssdk.services.s3.model.NoSuchKeyException
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.time.Duration

@Component
class S3Storage(
  private val s3Template: S3Template,
  private val s3Client: S3Client,
  private val s3Presigner: S3Presigner,
  @Value("\${app.bucket}") private val bucket: String,
  @Value("\${app.ttl.upload}") private val uploadTtl: Duration,
  @Value("\${app.ttl.download}") private val downloadTtl: Duration,
) {
  fun uploadUrl(key: String): String =
    s3Template.createSignedPutURL(bucket, key, uploadTtl).toString()

  fun downloadUrl(key: String): String =
    s3Template.createSignedGetURL(bucket, key, downloadTtl).toString()

  fun exists(key: String): Boolean = try {
    s3Client.headObject { it.bucket(bucket).key(key) }
    true
  } catch (_: NoSuchKeyException) {
    false
  }

  fun startMultipart(key: String, contentType: String): String =
    s3Client.createMultipartUpload { it.bucket(bucket).key(key).contentType(contentType) }.uploadId()

  fun partUrls(key: String, uploadId: String, partCount: Int): List<String> =
    (1..partCount).map { partNumber ->
      s3Presigner.presignUploadPart { presign ->
        presign.signatureDuration(uploadTtl)
          .uploadPartRequest {
            it.bucket(bucket).key(key).uploadId(uploadId).partNumber(partNumber)
          }
      }.url().toString()
    }

  fun completeMultipart(key: String, uploadId: String, parts: List<Pair<Int, String>>) {
    val completed = CompletedMultipartUpload.builder()
      .parts(parts.map { (partNumber, etag) ->
        CompletedPart.builder().partNumber(partNumber).eTag(etag).build()
      })
      .build()
    s3Client.completeMultipartUpload {
      it.bucket(bucket).key(key).uploadId(uploadId).multipartUpload(completed)
    }
  }
}
