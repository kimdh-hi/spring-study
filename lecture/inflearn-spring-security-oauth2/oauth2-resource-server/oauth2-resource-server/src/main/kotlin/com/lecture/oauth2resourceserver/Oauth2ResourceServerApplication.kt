package com.lecture.oauth2resourceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Oauth2ResourceServerApplication

fun main(args: Array<String>) {
  runApplication<Oauth2ResourceServerApplication>(*args)
}
