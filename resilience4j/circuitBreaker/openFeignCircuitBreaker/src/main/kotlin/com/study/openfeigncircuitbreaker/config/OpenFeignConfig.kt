package com.study.openfeigncircuitbreaker.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.study.openfeigncircuitbreaker.exception.OpenFeignException
import feign.codec.ErrorDecoder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.study.openfeigncircuitbreaker")
class OpenFeignConfig {
  @Bean
  fun errorDecoder(objectMapper: ObjectMapper) = ErrorDecoder { _, response ->
    runCatching {
      val openFeignException = objectMapper.readValue<OpenFeignException>(response.body().asInputStream())
      OpenFeignException(openFeignException.errorCode, httpStatusCode = response.status(), openFeignException.message)
    }.getOrElse {
      OpenFeignException(9999, httpStatusCode = response.status(), "unknown feignClient api call.")
    }
  }
}
