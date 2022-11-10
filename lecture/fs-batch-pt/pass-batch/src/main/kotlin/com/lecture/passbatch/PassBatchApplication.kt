package com.lecture.passbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class PassBatchApplication

fun main(args: Array<String>) {
  runApplication<PassBatchApplication>(*args)
}
