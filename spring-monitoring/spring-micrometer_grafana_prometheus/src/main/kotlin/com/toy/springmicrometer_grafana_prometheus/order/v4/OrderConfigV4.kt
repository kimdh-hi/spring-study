package com.toy.springmicrometer_grafana_prometheus.order.v4

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderConfigV4 {

  @Bean
  fun timedAspect(registry: MeterRegistry) = TimedAspect(registry)

  @Bean
  fun orderService() = OrderServiceV4()
}