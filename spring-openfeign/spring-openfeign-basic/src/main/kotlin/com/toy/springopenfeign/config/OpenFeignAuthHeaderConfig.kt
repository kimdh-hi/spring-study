package com.toy.springopenfeign.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean

class OpenFeignAuthHeaderConfig {

  @Bean
  fun addAuthHeaderRequestInterceptor() = RequestInterceptor {
    it.header("userId", "userId...")
  }
}
