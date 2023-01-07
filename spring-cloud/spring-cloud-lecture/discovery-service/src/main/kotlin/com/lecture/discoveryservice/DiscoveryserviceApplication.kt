package com.lecture.discoveryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class DiscoveryserviceApplication

fun main(args: Array<String>) {
  runApplication<DiscoveryserviceApplication>(*args)
}
