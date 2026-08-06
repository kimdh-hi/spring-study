package com.study.sqsconsumer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SqsConsumerApplication

fun main(args: Array<String>) {
  SpringApplication.run(SqsConsumerApplication::class.java, *args)
}
