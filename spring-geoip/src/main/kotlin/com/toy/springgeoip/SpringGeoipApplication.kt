package com.toy.springgeoip

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringGeoipApplication

fun main(args: Array<String>) {
  runApplication<SpringGeoipApplication>(*args)
}
