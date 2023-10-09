package com.toy.springmicrometer_grafana_prometheus.order.v0

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderConfigV0 {

  @Bean
  fun orderService() = OrderServiceV0()
}