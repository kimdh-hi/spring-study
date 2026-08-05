package com.study.sqsproducer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SqsProducerApplication

fun main(args: Array<String>) {
  SpringApplication.run(SqsProducerApplication::class.java, *args)
}
