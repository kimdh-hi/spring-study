package com.toy.namingserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class NamingServerApplication

fun main(args: Array<String>) {
    runApplication<NamingServerApplication>(*args)
}
