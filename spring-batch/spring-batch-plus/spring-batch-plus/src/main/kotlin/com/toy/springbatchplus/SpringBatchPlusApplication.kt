package com.toy.springbatchplus

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class SpringBatchPlusApplication

fun main(args: Array<String>) {
  runApplication<SpringBatchPlusApplication>(*args)
}
