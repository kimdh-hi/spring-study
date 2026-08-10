package com.study.presignedurl.infra

import io.awspring.cloud.s3.S3Template
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.NoSuchKeyException
import java.time.Duration

@Component
class S3Storage(
  private val s3Template: S3Template,
  private val s3Client: S3Client,
  @Value("\${app.bucket}") private val bucket: String,
  @Value("\${app.url-ttl}") private val ttl: Duration,
) {
  fun uploadUrl(key: String): String = s3Template.createSignedPutURL(bucket, key, ttl).toString()

  fun downloadUrl(key: String): String = s3Template.createSignedGetURL(bucket, key, ttl).toString()

  fun exists(key: String): Boolean = try {
    s3Client.headObject { it.bucket(bucket).key(key) }
    true
  } catch (_: NoSuchKeyException) {
    false
  }
}
