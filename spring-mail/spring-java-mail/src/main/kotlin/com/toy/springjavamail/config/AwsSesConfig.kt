package com.toy.springjavamail.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region.AP_NORTHEAST_2
import software.amazon.awssdk.services.ses.SesAsyncClient

@Configuration
class AwsSesConfig(
  private val env: Environment
) {

  @Bean
  fun sesAsyncClient(): SesAsyncClient {
    val accessKey = env.getProperty("aws-secret.access-key")
    val secretKey = env.getProperty("aws-secret.secret-key")

    return SesAsyncClient.builder()
      .credentialsProvider { AwsBasicCredentials.create(accessKey, secretKey) }
      .region(AP_NORTHEAST_2)
      .build()
  }

  @Bean
  fun amazonSimpleEmailService(): AmazonSimpleEmailService {
    val accessKey = env.getProperty("aws-secret.access-key")
    val secretKey = env.getProperty("aws-secret.secret-key")

    val basicAWSCredentials = BasicAWSCredentials(accessKey, secretKey)

    return AmazonSimpleEmailServiceAsyncClientBuilder
      .standard()
      .withCredentials(AWSStaticCredentialsProvider(basicAWSCredentials))
      .withRegion(Regions.AP_NORTHEAST_2)
      .build()
  }
}