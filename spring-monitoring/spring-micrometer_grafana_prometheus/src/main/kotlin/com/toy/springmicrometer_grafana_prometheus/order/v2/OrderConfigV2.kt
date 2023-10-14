package com.toy.springmicrometer_grafana_prometheus.order.v2

import io.micrometer.core.aop.CountedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderConfigV2 {

  @Bean
  fun orderService() = OrderServiceV2()

  //@Counted 관련 aop 를 위함
  @Bean
  fun countedAspect(registry: MeterRegistry) = CountedAspect(registry)
}