package com.toy.springmicrometer_grafana_prometheus.order.v3

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderConfigV3 {

  @Bean
  fun orderService(registry: MeterRegistry) = OrderServiceV3(registry)
}