package com.toy.emailauthentication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
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
      .region(Region.AP_NORTHEAST_2)
      .build()
  }

//  @Bean
//  fun amazonSimpleEmailService(): AmazonSimpleEmailService {
//    val accessKey = env.getProperty("aws-secret.access-key")
//    val secretKey = env.getProperty("aws-secret.secret-key")
//
//    val basicAWSCredentials = BasicAWSCredentials(accessKey, secretKey)
//
//    return AmazonSimpleEmailServiceAsyncClientBuilder
//      .standard()
//      .withCredentials(AWSStaticCredentialsProvider(basicAWSCredentials))
//      .withRegion(Regions.AP_NORTHEAST_2)
//      .build()
//  }
}