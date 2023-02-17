package com.toy.jpacustomgenerator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class JpaCustomGeneratorApplication

fun main(args: Array<String>) {
  runApplication<JpaCustomGeneratorApplication>(*args)
}
