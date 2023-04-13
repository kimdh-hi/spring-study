package com.toy.springaws.helper

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.toy.springaws.config.AwsProperties
import org.apache.commons.lang3.StringUtils
import java.io.File

class S3UploadHelper(
  private val s3Client: AmazonS3Client,
  private val awsProperties: AwsProperties
): UploaderHelper() {
  override fun upload(file: File, path: String) {
    val key = StringUtils.removeStart(path, "$UPLOAD_URI/")
    val objectRequest = PutObjectRequest(awsProperties.getBucketName(), key, file)
    s3Client.putObject(objectRequest)
  }
}