package com.toy.springaws.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cloud.aws")
data class AwsProperties(
  val credentials: AwsCredentials,
  val region: AwsRegion,
  val s3: AwsS3
) {
  fun getAccessKey() = credentials.accessKey
  fun getSecretKey() = credentials.secretKey
  fun getRegion() = region.static
  fun getBucketName() = s3.bucket

  data class AwsCredentials(
    val accessKey: String,
    val secretKey: String
  )

  data class AwsRegion(
    val static: String
  )

  data class AwsS3(
    val bucket: String
  )
}
