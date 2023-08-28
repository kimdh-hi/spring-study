package com.toy.springopenfeign.config

import feign.Response
import feign.RetryableException
import feign.Retryer
import feign.codec.ErrorDecoder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import java.util.*
import java.util.concurrent.TimeUnit


@Configuration
@EnableFeignClients(basePackages = ["com.toy.springopenfeign"]) // main 클래스가 아닌 곳에서 설정시 basePackages 설정필요
class OpenFeignConfig {

  // Retryer.Default(a, b ,c)
  // 재시도 시 최초 a 만큼 대기 후 c 번 재요청 (최대 b 만큼 대기)
  // a * 1.5 후 재시도
  @Bean
  fun retryer(): Retryer.Default {
    return Retryer.Default(1000, 2000, 3)
  }

  @Bean
  fun decoder(): ErrorDecoder {
    return ErrorDecoder { _: String, response: Response ->
      when(response.status()) {
        HttpStatus.INTERNAL_SERVER_ERROR.value() -> RetryableException(response.status(), response.reason(), response.request().httpMethod(), null, response.request())
        else -> IllegalStateException("feignClient generic error")
      }
    }
  }
}