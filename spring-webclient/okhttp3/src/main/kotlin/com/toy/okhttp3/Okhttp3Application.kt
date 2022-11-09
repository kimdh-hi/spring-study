package com.toy.okhttp3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class Okhttp3Application

fun main(args: Array<String>) {
  runApplication<Okhttp3Application>(*args)
}
