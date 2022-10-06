package com.toy.jparoutingdatasource

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JpaRoutingDatasourceApplication

fun main(args: Array<String>) {
  runApplication<JpaRoutingDatasourceApplication>(*args)
}
