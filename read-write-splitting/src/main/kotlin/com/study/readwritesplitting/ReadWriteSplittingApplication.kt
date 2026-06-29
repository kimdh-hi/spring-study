package com.study.readwritesplitting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ReadWriteSplittingApplication

fun main(args: Array<String>) {
  runApplication<ReadWriteSplittingApplication>(*args)
}
