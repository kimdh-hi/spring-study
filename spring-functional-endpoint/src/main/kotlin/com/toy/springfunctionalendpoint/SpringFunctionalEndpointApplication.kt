package com.toy.springfunctionalendpoint

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringFunctionalEndpointApplication

fun main(args: Array<String>) {
  runApplication<SpringFunctionalEndpointApplication>(*args) {
    addInitializers(routeBeans)
  }
}
