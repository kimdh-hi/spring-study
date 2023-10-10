package com.toy.springmicrometer_grafana_prometheus

import com.toy.springmicrometer_grafana_prometheus.order.v0.OrderConfigV0
import com.toy.springmicrometer_grafana_prometheus.order.v1.OrderConfigV1
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

//@Import(OrderConfigV0::class)
@Import(OrderConfigV1::class)
@SpringBootApplication(scanBasePackages = ["com.toy.springmicrometer_grafana_prometheus.controller"])
class SpringMicrometerGrafanaPrometheusApplication

fun main(args: Array<String>) {
  runApplication<SpringMicrometerGrafanaPrometheusApplication>(*args)
}
