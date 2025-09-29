package com.study.openfeign.config

import feign.Logger
import feign.RequestInterceptor
import feign.Response
import feign.Retryer
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus

@Configuration
@EnableFeignClients(basePackages = ["com.study.openfeign"]) // main 클래스가 아닌 곳에서 설정시 basePackages 설정필요
class OpenFeignConfig {

  private val log = LoggerFactory.getLogger(OpenFeignConfig::class.java)

  @Bean
  fun retryer(): Retryer.Default {
    return Retryer.Default(1000, 2000, 3)
  }

  @Bean
  fun feignLoggerLevel(): Logger.Level {
    return Logger.Level.FULL
  }

  // @Configuration 이 붙은 모든 @FeignClient 에 적용되는 설정과 @FeignClient 의 configuration 에 지정한 bean 이 충돌되는 경우에 대한 테스트 위함
  // @FeignClient 의 configuration 에 지정한 bean 이 우선순위를 가진다.
  // 즉, @FeignClient 의 configuration 에서 requestInterceptor 를 설정한 경우 아래 헤더는 추가되지 않는다.
  // 전역으로 설정한 동일 타입의 bean 이 일괄적용되게 하고 싶다면 @FeignClient 의 configuration 에서 지정하는 bean 의 이름을 다르게 설정하자.
  @Bean
  fun requestInterceptor() = RequestInterceptor {
    it.header("userId2", "userId2...")
  }

  @Bean
  fun decoder(): ErrorDecoder {
    return ErrorDecoder { _: String, response: Response ->
      when (response.status()) {
        HttpStatus.INTERNAL_SERVER_ERROR.value() -> IllegalStateException("feignClient unknown error")
        else -> IllegalStateException("feignClient generic error")
      }
    }
  }


  @Bean
  fun vtLogInterceptor(): RequestInterceptor = RequestInterceptor {
    val thread = Thread.currentThread()
    log.info(
      "feign on virtualThread: {}",
      thread.isVirtual,
    )
  }
}
