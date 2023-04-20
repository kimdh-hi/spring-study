package com.lecture.reactivekafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveKafkaApplication

fun main(args: Array<String>) {
  runApplication<ReactiveKafkaApplication>(*args)
}
