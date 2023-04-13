package com.toy.springaws.config

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig {

  @Bean
  fun s3Client() = AmazonS3ClientBuilder.standard()
    .withRegion(Regions.AP_NORTHEAST_2)
    .build()
}