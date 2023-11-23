package com.toy.serviceb.config

import com.toy.serviceb.ServiceAClient
import feign.Feign
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfig {

  @Bean
  fun serviceAClient(): ServiceAClient {
    return Feign.builder()
      .target(ServiceAClient::class.java, "http://localhost:8081")
  }
}