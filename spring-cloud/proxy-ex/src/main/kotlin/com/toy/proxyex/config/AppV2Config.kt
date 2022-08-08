package com.toy.proxyex.config

import com.toy.proxyex.app.v2.OrderControllerV2
import com.toy.proxyex.app.v2.OrderRepositoryV2
import com.toy.proxyex.app.v2.OrderServiceV2
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppV2Config {

  @Bean
  fun orderControllerV2(): OrderControllerV2 = OrderControllerV2(orderServiceV2())

  @Bean
  fun orderServiceV2(): OrderServiceV2 = OrderServiceV2(orderRepositoryV2())

  @Bean
  fun orderRepositoryV2(): OrderRepositoryV2 = OrderRepositoryV2()

}