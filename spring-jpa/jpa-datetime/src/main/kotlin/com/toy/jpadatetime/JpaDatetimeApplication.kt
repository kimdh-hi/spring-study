package com.toy.jpadatetime

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class JpaDatetimeApplication

fun main(args: Array<String>) {
  runApplication<JpaDatetimeApplication>(*args)
}
