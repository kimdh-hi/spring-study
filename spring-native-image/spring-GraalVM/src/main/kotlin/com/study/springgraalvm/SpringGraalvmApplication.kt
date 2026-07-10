package com.study.springgraalvm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SpringGraalvmApplication

fun main(args: Array<String>) {
  runApplication<SpringGraalvmApplication>(*args)
}
