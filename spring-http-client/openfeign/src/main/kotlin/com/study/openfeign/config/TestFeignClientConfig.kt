package com.study.openfeign.config

import feign.Logger
import org.springframework.context.annotation.Bean

class TestFeignClientConfig {

  @Bean
  fun feignClient(): feign.Client {
    return feign.Client.Default(null, null)
  }

  @Bean
  fun loggerLevel(): Logger.Level = Logger.Level.FULL
}
