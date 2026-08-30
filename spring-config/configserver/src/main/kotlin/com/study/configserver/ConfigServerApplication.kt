package com.study.configserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableConfigServer
class ConfigServerApplication

fun main(args: Array<String>) {
  runApplication<ConfigServerApplication>(*args)
}
