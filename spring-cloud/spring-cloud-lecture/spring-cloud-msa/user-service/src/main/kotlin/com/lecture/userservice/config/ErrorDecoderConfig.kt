package com.lecture.userservice.config

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Configuration
class ErrorDecoderConfig {

  @Bean
  fun feignErrorDecoder() = FeignErrorDecoder()
}


class FeignErrorDecoder: ErrorDecoder {
  override fun decode(methodKey: String, response: Response): Exception = when (response.status()) {
    400 -> {
      Exception(response.reason())
    }

    404 -> {
      if (methodKey.contains("findOrderById")) {
        ResponseStatusException(HttpStatus.BAD_REQUEST, "empty")
      } else {
        Exception(response.reason())
      }
    }

    else -> {
      Exception(response.reason())
    }
  }
}