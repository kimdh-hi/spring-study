package com.toy.springmicrometer_grafana_prometheus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMicrometerGrafanaPrometheusApplication

fun main(args: Array<String>) {
  runApplication<SpringMicrometerGrafanaPrometheusApplication>(*args)
}
