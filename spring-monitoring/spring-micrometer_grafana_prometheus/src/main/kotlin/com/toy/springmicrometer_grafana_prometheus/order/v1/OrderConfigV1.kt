package com.toy.springmicrometer_grafana_prometheus.order.v1

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderConfigV1 {

  @Bean
  fun orderService(registry: MeterRegistry) = OrderServiceV1(registry)
}