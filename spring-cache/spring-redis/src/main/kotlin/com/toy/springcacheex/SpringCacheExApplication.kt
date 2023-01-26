package com.toy.springcacheex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class SpringCacheExApplication

fun main(args: Array<String>) {
  runApplication<SpringCacheExApplication>(*args)
}
