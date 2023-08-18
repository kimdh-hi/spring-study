package com.toy.springopenfeign.config

import feign.Retryer
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
@EnableFeignClients(basePackages = ["com.toy.springopenfeign"]) // main 클래스가 아닌 곳에서 설정시 basePackages 설정필요
class OpenFeignConfig {

  @Bean
  fun retryer(): Retryer.Default {
    // 최초 0.1초 대기후 재요청
    // 최대 5번 재시도
    return Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 5)
  }
}