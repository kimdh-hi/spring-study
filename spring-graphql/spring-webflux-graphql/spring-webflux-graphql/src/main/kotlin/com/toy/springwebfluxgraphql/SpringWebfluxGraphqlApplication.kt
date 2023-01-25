package com.toy.springwebfluxgraphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication(scanBasePackages = ["com.toy.springwebfluxgraphql.lec14"])
@EnableR2dbcRepositories(basePackages = ["com.toy.springwebfluxgraphql.lec14"])
class SpringWebfluxGraphqlApplication

fun main(args: Array<String>) {
  runApplication<SpringWebfluxGraphqlApplication>(*args)
}
