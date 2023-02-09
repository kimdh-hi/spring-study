package com.toy.springdataenvers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class SpringDataEnversApplication

fun main(args: Array<String>) {
  runApplication<SpringDataEnversApplication>(*args)
}
