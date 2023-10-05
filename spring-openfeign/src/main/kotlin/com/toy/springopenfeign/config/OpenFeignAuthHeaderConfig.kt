package com.toy.springopenfeign.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean

class OpenFeignAuthHeaderConfig {

  @Bean
  fun requestInterceptor() = RequestInterceptor {
    it.header("userId", "userId...")
  }
}