package com.toy.springwebfluxgraphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.toy.springwebfluxgraphql.sec01.lec03"])
class SpringWebfluxGraphqlApplication

fun main(args: Array<String>) {
  runApplication<SpringWebfluxGraphqlApplication>(*args)
}
