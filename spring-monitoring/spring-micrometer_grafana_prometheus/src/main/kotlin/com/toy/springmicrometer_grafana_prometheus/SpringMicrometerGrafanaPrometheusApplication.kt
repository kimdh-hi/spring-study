package com.toy.springmicrometer_grafana_prometheus

import com.toy.springmicrometer_grafana_prometheus.order.v0.OrderConfigV0
import com.toy.springmicrometer_grafana_prometheus.order.v1.OrderConfigV1
import com.toy.springmicrometer_grafana_prometheus.order.v2.OrderConfigV2
import com.toy.springmicrometer_grafana_prometheus.order.v3.OrderConfigV3
import com.toy.springmicrometer_grafana_prometheus.order.v4.OrderConfigV4
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

//@Import(OrderConfigV0::class)
//@Import(OrderConfigV1::class)
//@Import(OrderConfigV2::class)
//@Import(OrderConfigV3::class)
@Import(OrderConfigV4::class)
@SpringBootApplication(scanBasePackages = ["com.toy.springmicrometer_grafana_prometheus.controller"])
class SpringMicrometerGrafanaPrometheusApplication

fun main(args: Array<String>) {
  runApplication<SpringMicrometerGrafanaPrometheusApplication>(*args)
}
