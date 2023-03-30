package com.toy.springr2dbc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.toy.springr2dbc.repository"])
@EnableR2dbcRepositories(basePackages = ["com.toy.springr2dbc.r2dbc"])
class SpringR2dbcApplication

fun main(args: Array<String>) {
  runApplication<SpringR2dbcApplication>(*args)
}
